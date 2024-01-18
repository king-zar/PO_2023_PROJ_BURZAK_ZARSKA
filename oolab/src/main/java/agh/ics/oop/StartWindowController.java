package agh.ics.oop;

import agh.ics.oop.model.MutationVariant;
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
                MutationVariant.valueOf(mutationVariantChoiceBox.getValue())
        );

        simulationApp.start(new Stage());
    }
}
