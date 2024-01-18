package agh.ics.oop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
    public void startSimulation(ActionEvent event) {
        System.out.println("Start Simulation clicked!");

        SimulationApp simulationApp = new SimulationApp(
                Integer.parseInt(widthField.getText()),
                Integer.parseInt(heightField.getText()),
                Integer.parseInt(stepsField.getText())
        );

        simulationApp.start(new Stage());
    }
}
