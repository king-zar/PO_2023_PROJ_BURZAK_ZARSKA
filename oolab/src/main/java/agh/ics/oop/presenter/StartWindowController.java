package agh.ics.oop.presenter;
import agh.ics.oop.Configuration;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.variants.MapVariant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import agh.ics.oop.SimulationApp;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private ChoiceBox<String> saveStatsChoiceBox;

    private Stage primaryStage;

    private SimulationEngine engine = new SimulationEngine();

    // moglyby byc inne, tak zostalo przyjete, zeby symulacja byla sensowna
    private static final int MIN_MAP_DIMENSION = 5; // dla mniejszych map sredni sens ma symulacja
    private static final int MAX_MAP_DIMENSION = 20; // bo inaczej slabo widac
    private static final int MAX_STEPS = 200;
    private static final int MAX_NUTRITION = 5;
    private static final int MAX_MUTATIONS = 5;
    private static final int MAX_GENOME_LENGTH = 10;
    private static final int MAX_WATER_AREAS = 5;

    @FXML
    private void initialize() {
        mutationVariantChoiceBox.setItems(FXCollections.observableArrayList("RANDOM", "SLIGHT_CORRECTION"));
        mutationVariantChoiceBox.setValue("RANDOM"); // domyslnie RANDOM

        mapVariantChoiceBox.setItems(FXCollections.observableArrayList("EARTH_LIKE", "TIDES_OUTFLOWS"));

        saveStatsChoiceBox.setItems(FXCollections.observableArrayList("YES", "NO"));
        saveStatsChoiceBox.setValue("NO"); // domyslnie nie

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
        try {
            System.out.println("Start Simulation clicked!");

            if (!validateParameters()) {
                return;
            }

            Configuration configuration = getConfiguration();
            SimulationApp simulationApp = new SimulationApp(configuration, getFlagForFileSaving());

            engine.runAsyncInThreadPool(simulationApp);

        } catch (NumberFormatException e) {
            displayErrorDialog("Invalid parameter", "Please enter valid numeric parameters.");
        }
    }
    public void setStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    public void saveConfiguration(ActionEvent ev) {
        System.out.println("Saving config to file");
        // Save config to JSON File, let user pick location
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save configuration");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
        fileChooser.setInitialFileName("simulation_config.json");
        File file = fileChooser.showSaveDialog(primaryStage);
        if (file == null) {
            System.out.println("No file selected");
            return;
        }
        Configuration config = getConfiguration();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(config);

        // write to file
        FileWriter writer;
        try {
            writer = new FileWriter(file);
            writer.write(json);
            writer.close();
        } catch (Exception e) {
            System.out.println("Error while saving config to file");
            e.printStackTrace();
        }
    }

    public Configuration getConfiguration() {
        Configuration config = new Configuration(
                Integer.parseInt(widthField.getText()),
                Integer.parseInt(heightField.getText()),
                Integer.parseInt(stepsField.getText()),
                Integer.parseInt(initialPlantCount.getText()),
                Integer.parseInt(plantToGrowPerStep.getText()),
                Integer.parseInt(animalCount.getText()),
                Integer.parseInt(initialAnimalEnergy.getText()),
                Integer.parseInt(maxPlantNutrition.getText()),
                Integer.parseInt(energyToReproduce.getText()),
                Integer.parseInt(minMutations.getText()),
                Integer.parseInt(maxMutations.getText()),
                Integer.parseInt(genomeLength.getText()),
                mapVariantChoiceBox.getValue(),
                mutationVariantChoiceBox.getValue(),
                Integer.parseInt(waterAreasCount.getText()),
                Integer.parseInt(initialWaterAreaSize.getText()),
                Integer.parseInt(inflowOutflowSize.getText()),
                Integer.parseInt(energyLostInReproduction.getText())
        );

        return config;
    }

    private boolean getFlagForFileSaving() {
        if (saveStatsChoiceBox.getValue().equals("NO")) {
            return false;
        } else {
            return true;
        }
    }

    @FXML
    public void loadConfiguration(ActionEvent ev) {
        System.out.println("Loading config...");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load configuration");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file == null) {
            System.out.println("No file selected");
            return;
        }
        FileReader reader;
        try {
            reader = new FileReader(file);
            Gson gson = new Gson();
            Configuration config = gson.fromJson(reader, Configuration.class);

            widthField.setText(String.valueOf(config.width));
            heightField.setText(String.valueOf(config.height));
            stepsField.setText(String.valueOf(config.simulationSteps));
            initialPlantCount.setText(String.valueOf(config.initialPlantCount));
            plantToGrowPerStep.setText(String.valueOf(config.plantToGrowPerStep));
            animalCount.setText(String.valueOf(config.initialAnimalCount));
            initialAnimalEnergy.setText(String.valueOf(config.initialAnimalEnergy));
            maxPlantNutrition.setText(String.valueOf(config.maxPlantNutrition));
            energyToReproduce.setText(String.valueOf(config.energyToReproduce));
            minMutations.setText(String.valueOf(config.minMutations));
            maxMutations.setText(String.valueOf(config.maxMutations));
            genomeLength.setText(String.valueOf(config.genomeLength));
            mapVariantChoiceBox.setValue(config.mapVariant);
            mutationVariantChoiceBox.setValue(config.mutationVariant);
            waterAreasCount.setText(String.valueOf(config.waterAreasCount));
            initialWaterAreaSize.setText(String.valueOf(config.initialWaterAreaSize));
            inflowOutflowSize.setText(String.valueOf(config.inflowOutflowSize));
            energyLostInReproduction.setText(String.valueOf(config.energyLostInReproduction));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean validateParameters() {
        String mapVariant = mapVariantChoiceBox.getValue();

        if (!isValidMapVariant(mapVariant)) {
            displayErrorDialog("Invalid parameter", "Please select a value for Mutation Variant.");
            return false;
        }

        String[] fieldNames = {
                "Map Width",
                "Map Height",
                "Simulation Steps",
                "Initial Plant Count",
                "Plants To Grow Per Step",
                "Initial Animal Count",
                "Initial Animal Energy",
                "Max Plant Nutrition",
                "Energy To Reproduce",
                "Min Mutations",
                "Max Mutations",
                "Genome Length",
                "Water Areas Count",
                "Initial Water Area Size",
                "Inflow/Outflow Size",
                "Energy Lost In Reproduction"
        };

        int width = Integer.parseInt(widthField.getText());
        int height = Integer.parseInt(heightField.getText());
        int mapPlaces = width * height;
        int initialEnergy = Integer.parseInt(initialAnimalEnergy.getText());

        int[] valuesToValidate = {
                width,
                height,
                Integer.parseInt(stepsField.getText()),
                Integer.parseInt(initialPlantCount.getText()),
                Integer.parseInt(plantToGrowPerStep.getText()),
                Integer.parseInt(animalCount.getText()),
                initialEnergy,
                Integer.parseInt(maxPlantNutrition.getText()),
                Integer.parseInt(energyToReproduce.getText()),
                Integer.parseInt(minMutations.getText()),
                Integer.parseInt(maxMutations.getText()),
                Integer.parseInt(genomeLength.getText()),
                Integer.parseInt(waterAreasCount.getText()),
                Integer.parseInt(initialWaterAreaSize.getText()),
                Integer.parseInt(inflowOutflowSize.getText()),
                Integer.parseInt(energyLostInReproduction.getText())
        };

        int[] minValues = {MIN_MAP_DIMENSION, MIN_MAP_DIMENSION, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 1};
        int[] maxValues = {MAX_MAP_DIMENSION, MAX_MAP_DIMENSION, MAX_STEPS, mapPlaces, mapPlaces/10,
                mapPlaces, mapPlaces/4, MAX_NUTRITION, initialEnergy, MAX_MUTATIONS,
                MAX_MUTATIONS, MAX_GENOME_LENGTH, MAX_WATER_AREAS, mapPlaces/20, mapPlaces/20,
                initialEnergy/2};

        for (int i = 0; i < valuesToValidate.length; i++) {
            if (valuesToValidate[i] < minValues[i] || valuesToValidate[i] > maxValues[i]) {
                displayErrorDialog("Invalid parameter", "Please enter a valid value for " + fieldNames[i] +
                        " (should be between " + minValues[i] + " and " + maxValues[i] + ").");
                return false;
            }
        }

        return true;
    }

    private boolean isValidMapVariant(String mapVariant) {
        if (mapVariant == null || mapVariant.isEmpty()) {
            return false;
        }

        return true;
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    private void displayErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Parsing arguments error");
        alert.setContentText(content);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/myszojelen.jpg")));

        alert.showAndWait();
    }
}
