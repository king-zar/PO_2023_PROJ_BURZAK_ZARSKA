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
    private BorderPane rootBorderPane; // Zmiana typu na BorderPane

    @FXML
    private void initialize() {
        mutationVariantChoiceBox.setItems(FXCollections.observableArrayList("RANDOM", "SLIGHT_CORRECTION"));
        mutationVariantChoiceBox.setValue("RANDOM"); // domyslnie RANDOM

        mapVariantChoiceBox.setItems(FXCollections.observableArrayList("EARTH_LIKE", "TIDES_OUTFLOWS"));
        mapVariantChoiceBox.setValue("EARTH_LIKE"); // domyslnie EARTH_LIKE
    }

    @FXML
    public void startSimulation(ActionEvent event) {
        System.out.println("Start Simulation clicked!");

        SimulationApp simulationApp = new SimulationApp(
                Integer.parseInt(widthField.getText()),
                Integer.parseInt(heightField.getText()),
                Integer.parseInt(stepsField.getText()),
                Integer.parseInt(animalCount.getText()),
                Integer.parseInt(initialPlantCount.getText()),
                Integer.parseInt(plantToGrowPerStep.getText()),
                MutationVariant.valueOf(mutationVariantChoiceBox.getValue()),
                Integer.parseInt(minMutations.getText()),
                Integer.parseInt(maxMutations.getText()),
                MapVariant.valueOf(mapVariantChoiceBox.getValue())
        );

        simulationApp.start(new Stage());
    }
}
