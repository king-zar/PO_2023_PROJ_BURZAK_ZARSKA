package agh.ics.oop;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class StartWindow extends Application {
    @FXML
    private TextField widthField;

    @FXML
    private TextField heightField;

    @FXML
    private TextField stepsField;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Simulation Setup");

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("start_window.fxml"));
            BorderPane root = loader.load();

            // referencje do pol tekstowych
            widthField = (TextField) loader.getNamespace().get("widthField");
            heightField = (TextField) loader.getNamespace().get("heightField");
            stepsField = (TextField) loader.getNamespace().get("stepsField");

            // jak zamkniemy okienko, to wszystkie potomne tez sie zamkna
            primaryStage.setOnCloseRequest(event -> {
                Platform.exit();
                System.exit(0);
            });

            Scene scene = new Scene(root, root.getMinWidth(), root.getMinHeight());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

