package agh.ics.oop.presenter;

import agh.ics.oop.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SimulationPresenter implements MapChangeListener {
    private static final double CELL_WIDTH = 50.0;
    private static final double CELL_HEIGHT = 50.0;

    @FXML
    private Label infoLabel;

    @FXML
    private GridPane mapGrid;

    private TidesOutflowsMap worldMap;
    private SimulationConfig config;

    private boolean simulationRunning = true;

    public boolean isSimulationRunning () {
        return simulationRunning;
    }

    @Override
    public void mapChanged(WorldMap map, String message) {
        Platform.runLater(() -> {
            drawMap();
        });
    }

    public void setWorldMap(TidesOutflowsMap worldMap) {
        this.worldMap = worldMap;
        this.worldMap.subscribe(this);
        drawMap();
    }

    public void setConfig(SimulationConfig config) {
        this.config = config;
    }

    private void drawMap() {
        Platform.runLater(() -> {
            clearGrid();
            drawGrid();
        });
    }

    private void clearGrid() {
        mapGrid.getChildren().clear();
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    private void drawGrid() {
        Boundary bounds = worldMap.getCurrentBounds();
        int columns = bounds.upperRight().getX() - bounds.lowerLeft().getX() + 1;
        int rows = bounds.upperRight().getY() - bounds.lowerLeft().getY() + 1;

        addColumns(columns);
        addRows(rows);
        addLabels(bounds);
    }

    private void addColumns(int columns) {
        for (int col = 0; col < columns; col++) {
            ColumnConstraints column = new ColumnConstraints(CELL_WIDTH);
            mapGrid.getColumnConstraints().add(column);
        }
    }

    private void addRows(int rows) {
        for (int row = 0; row < rows; row++) {
            RowConstraints rowConstraint = new RowConstraints(CELL_HEIGHT);
            mapGrid.getRowConstraints().add(rowConstraint);
        }
    }

    private void addLabels(Boundary bounds) {
        for (int col = bounds.lowerLeft().getX(); col <= bounds.upperRight().getX(); col++) {
            for (int row = bounds.lowerLeft().getY(); row <= bounds.upperRight().getY(); row++) {
                addLabel(col, row, bounds);
            }
        }
    }

    private void addLabel(int col, int row, Boundary bounds) {
        Vector2d currentPosition = new Vector2d(col, row);

        Collection<Animal> animalsAtPosition = worldMap.getAnimalsAt(currentPosition);
        Grass grassAtPosition = worldMap.getGrassAt(currentPosition);
        Water waterAtPosition = worldMap.getWaterAt(currentPosition);

        Label label = createLabelForPosition(animalsAtPosition, grassAtPosition, waterAtPosition);

        if (label != null) {
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.add(label, col - bounds.lowerLeft().getX(), row - bounds.lowerLeft().getY());
        }
    }

    private Label createLabelForPosition(Collection<Animal> animals, Grass grass, Water water) {
        if (!animals.isEmpty()) {
            if (animals.size() == 1) {
                return createAnimalLabel(animals.iterator().next());  // Jedno zwierzę
            } else {
                // Więcej niż jedno zwierzę, pokaż losowe
                List<Animal> animalsList = new ArrayList<>(animals);
                Collections.shuffle(animalsList);
                return createAnimalLabel(animalsList.get(0));
            }
        } else if (water != null) {
            return createWaterLabel(water);
        } else if (grass != null) {
            return createGrassLabel(grass);  // Brak zwierząt, trawa obecna
        } else {
            return null;
        }
    }

    private Label createAnimalLabel(Animal animal) {
        Circle circle = new Circle(Math.min(CELL_WIDTH, CELL_HEIGHT) / 2);
        double energyPercentage = Math.min((animal.getEnergyLevel() * 1.0 / config.getInitialAnimalEnergy()), 1.0);
        Color color = Color.hsb(270, 1.0, energyPercentage);
        circle.setFill(color);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(circle);

        return new Label("", stackPane);
    }

    private Label createGrassLabel(Grass grass) {
        Rectangle rectangle = new Rectangle(CELL_WIDTH, CELL_HEIGHT);
        rectangle.setFill(Color.GREEN);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(rectangle);

        return new Label("", stackPane);
    }

    private Label createWaterLabel(Water water) {
        Rectangle rectangle = new Rectangle(CELL_WIDTH, CELL_HEIGHT);
        rectangle.setFill(Color.BLUE);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(rectangle);

        return new Label("", stackPane);
    }

    @FXML
    public void startSimulation() {
        if (!simulationRunning) {
            // Rozpocznij symulację
            infoLabel.setText("Simulation started!");
            simulationRunning = true;
        }
    }

    @FXML
    public void pauseSimulation() {
        if (simulationRunning) {
            // Wstrzymaj symulację
            infoLabel.setText("Simulation paused.");
            simulationRunning = false;
        }
    }
}
