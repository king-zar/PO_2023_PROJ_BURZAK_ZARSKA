package agh.ics.oop.presenter;

import agh.ics.oop.SimulationApp;
import agh.ics.oop.model.variants.MapVariant;
import agh.ics.oop.model.variants.MutationVariant;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class StartWindowController {

    @FXML
    private TextField widthField;

    @FXML
    private TextField heightField;

    @FXML
    private TextField stepsField;

    @FXML
    private TextField animalCount;

    @FXML
    private TextField initialPlantCount;

    @FXML
    private TextField plantToGrowPerStep;

    @FXML
    private TextField minMutations;

    @FXML
    private TextField maxMutations;

    @FXML
    private ChoiceBox<String> mutationVariantChoiceBox;

    @FXML
    private ChoiceBox<String> mapVariantChoiceBox;

    @FXML
    private TextField maxPlantNutrition;

    @FXML
    private TextField initialAnimalEnergy;

    @FXML
    private TextField genomeLength;

    @FXML
    private TextField energyToReproduce;

    @FXML
    private TextField energyLostInReproduction;

    @FXML
    private TextField waterAreasCount;

    @FXML
    private TextField initialWaterAreaSize;

    @FXML
    private TextField inflowOutflowSize;

    @FXML
    private BorderPane rootBorderPane; // Zmiana typu na BorderPane

    @FXML
    private void initialize() {
        mutationVariantChoiceBox.setItems(FXCollections.observableArrayList("RANDOM", "SLIGHT_CORRECTION"));
        mutationVariantChoiceBox.setValue("RANDOM"); // domyslnie RANDOM

        mapVariantChoiceBox.setItems(FXCollections.observableArrayList("EARTH_LIKE", "TIDES_OUTFLOWS"));

        mapVariantChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            boolean isTidesOutflows = "TIDES_OUTFLOWS".equals(newValue);
            // Toggle editability of fields for EARTH_LIKE variant
            waterAreasCount.setEditable(isTidesOutflows);
            initialWaterAreaSize.setEditable(isTidesOutflows);
            inflowOutflowSize.setEditable(isTidesOutflows);

            // Set default values for EARTH_LIKE variant
            if (!isTidesOutflows) {
                waterAreasCount.setText("0");
                initialWaterAreaSize.setText("0");
                inflowOutflowSize.setText("0");

            } else {
                waterAreasCount.setText("");
                initialWaterAreaSize.setText("");
                inflowOutflowSize.setText("");
            }
        });
    }

    @FXML
    public void startSimulation(ActionEvent event) {
        System.out.println("Start Simulation clicked!");

        SimulationApp simulationApp = new SimulationApp(
                Integer.parseInt(widthField.getText()),
                Integer.parseInt(heightField.getText()),
                Integer.parseInt(stepsField.getText()),
                Integer.parseInt(animalCount.getText()),
                Integer.parseInt(initialAnimalEnergy.getText()),
                Integer.parseInt(initialPlantCount.getText()),
                Integer.parseInt(plantToGrowPerStep.getText()),
                MutationVariant.valueOf(mutationVariantChoiceBox.getValue()),
                Integer.parseInt(minMutations.getText()),
                Integer.parseInt(maxMutations.getText()),
                MapVariant.valueOf(mapVariantChoiceBox.getValue()),
                Integer.parseInt(maxPlantNutrition.getText()),
                Integer.parseInt(genomeLength.getText()),
                Integer.parseInt(energyToReproduce.getText()),
                Integer.parseInt(energyLostInReproduction.getText()),
                Integer.parseInt(waterAreasCount.getText()),
                Integer.parseInt(initialWaterAreaSize.getText()),
                Integer.parseInt(inflowOutflowSize.getText())
        );

        simulationApp.start(new Stage());
    }
}
