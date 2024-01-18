package agh.ics.oop.presenter;

import agh.ics.oop.SimulationApp;
import agh.ics.oop.model.variants.MutationVariant;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
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
    private TextField plantCount;

    @FXML
    private TextField minMutations;

    @FXML
    private TextField maxMutations;

    @FXML
    private ChoiceBox<String> mutationVariantChoiceBox;

    @FXML
    private void initialize() {
        mutationVariantChoiceBox.setItems(FXCollections.observableArrayList("RANDOM", "SLIGHT_CORRECTION"));
    }

    @FXML
    public void startSimulation(ActionEvent event) {
        System.out.println("Start Simulation clicked!");

        SimulationApp simulationApp = new SimulationApp(
                Integer.parseInt(widthField.getText()),
                Integer.parseInt(heightField.getText()),
                Integer.parseInt(stepsField.getText()),
                Integer.parseInt(animalCount.getText()),
                Integer.parseInt(plantCount.getText()),
                MutationVariant.valueOf(mutationVariantChoiceBox.getValue()),
                Integer.parseInt(minMutations.getText()),
                Integer.parseInt(maxMutations.getText())
        );

        simulationApp.start(new Stage());
    }
}
