package agh.ics.oop.presenter;

import agh.ics.oop.model.Statistics;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class StatisticsPresenter {

    @FXML
    private VBox statisticsVBox;
    @FXML
    private Label totalAnimalsLabel;
    @FXML
    private Label totalWatersLabel;
    @FXML
    private Label totalPlantsLabel;
    @FXML
    private Label freeFieldsLabel;
    @FXML
    private Label mostCommonGenotypeLabel;
    @FXML
    private Label averageEnergyLabel;
    @FXML
    private Label averageLifeSpanLabel;
    @FXML
    private Label averageChildrenLabel;
    @FXML
    private Button saveStatisticsButton;


    private Statistics statistics;

    public void initialize(Statistics stats) {
        setStatistics(stats);
        updateStatisticsDisplay();
    }

    private void setStatistics(Statistics stats) {
        this.statistics = stats;
    }

    public void updateStatisticsDisplay() {
        javafx.application.Platform.runLater(() -> {
            totalAnimalsLabel.setText("Total Animals: " + statistics.getTotalAnimals());
            totalPlantsLabel.setText("Total Plants: " + statistics.getTotalPlants());
            totalWatersLabel.setText("Total Waters: " + statistics.getTotalWaters());
            freeFieldsLabel.setText("Free Fields: " + statistics.getFreeFields());
            mostCommonGenotypeLabel.setText("Most Common Genotype: " + getMostCommonGenotype());
            averageEnergyLabel.setText("Average Energy: " + String.format("%.2f", statistics.getAverageEnergy()));
            averageLifeSpanLabel.setText("Average Lifespan: " + String.format("%.2f", statistics.getAverageLifeSpan()));
            averageChildrenLabel.setText("Average Children: " + String.format("%.2f", statistics.getAverageChildren()));
        });
    }

    private String getMostCommonGenotype() {
        List<Integer> mostCommonGenotypeList = statistics.getMostCommonGenotype();
        if (mostCommonGenotypeList != null) {
            return mostCommonGenotypeList.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(" "));
        }
        return "No Data";
    }

    @FXML
    private void onSaveStatistics() {
        System.out.println("VBox: " + statisticsVBox);
        System.out.println("Scene: " + (statisticsVBox != null ? statisticsVBox.getScene() : "null"));

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Statistics");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        Stage stage = (Stage) statisticsVBox.getScene().getWindow();
        System.out.println("SaveStat");
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            saveStatisticsToFile(file);
        }
    }

    private void saveStatisticsToFile(File file) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.append("Total Animals,Total Plants,Free Fields,Most Common Genotype,Average Energy,Average Lifespan,Average Children\n");
            writer.append(statistics.getTotalAnimals() + "," +
                    statistics.getTotalPlants() + "," +
                    statistics.getFreeFields() + "," +
                    getMostCommonGenotype() + "," +
                    statistics.getAverageEnergy() + "," +
                    statistics.getAverageLifeSpan() + "," +
                    statistics.getAverageChildren() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
