package comp3111.covid;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import comp3111.covid.controller.ChartController;
import comp3111.covid.controller.TableController;
import comp3111.covid.core.*;
import comp3111.covid.ui.CheckListViewWithList;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.transform.Transform;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.util.StringConverter;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.time.LocalDate;
import java.util.*;

/**
 * Building on the sample skeleton for 'ui.fxml' Controller Class generated by SceneBuilder
 */
public class Controller {

    private static CSVFileOperator fileOperator;

    @FXML
    private Tab tabTaskZero;

    @FXML
    private TextField textfieldDataset;

    @FXML
    TabPane mainTabPane;

    @FXML
    ChartController chartAController;

    @FXML
    ChartController chartBController;

    @FXML
    ChartController chartCController;

    @FXML
    TableController tableAController;

    @FXML
    TableController tableBController;

    @FXML
    TableController tableCController;

    public void initialize() {

    }



    @FXML
    public void saveAsPng() {
        int tabID = mainTabPane.getSelectionModel().getSelectedIndex();
        WritableImage image;
        SnapshotParameters snapshotParameters = new SnapshotParameters();
        snapshotParameters.setTransform(Transform.scale(2, 2));
        if (tabID == 4) {
            LineChart<Number, Number> chart = chartAController.chart;
            image = new WritableImage((int) Math.rint(2 * chart.getWidth()), (int) Math.rint(2 * chart.getHeight()));
            chart.snapshot(snapshotParameters, image);
        } else if (tabID == 5) {
            LineChart<Number, Number> chartB = chartBController.chart;
            image = new WritableImage((int) Math.rint(2 * chartB.getWidth()), (int) Math.rint(2 * chartB.getHeight()));
            chartB.snapshot(snapshotParameters, image);
        } else if (tabID == 6) {
            LineChart<Number, Number> chartC = chartBController.chart;
            image = new WritableImage((int) Math.rint(2 * chartC.getWidth()), (int) Math.rint(2 * chartC.getHeight()));
            chartC.snapshot(snapshotParameters, image);
        } else { // error case
            Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot generate image.", ButtonType.YES);
            alert.show();
            return;
        }


        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image (*.png)", "*.png"));
        File file = fileChooser.showSaveDialog(mainTabPane.getScene().getWindow());

        if (file != null) {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    @FXML
    void loadNewFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Data Set");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV file (*.csv)", "*.csv"));
        File file = fileChooser.showOpenDialog(mainTabPane.getScene().getWindow());

        if (file != null) {
            System.out.println(file.toString());
            textfieldDataset.setText(file.getName());
            try {
                CSVFileOperator fileOperator = new CSVFileOperator(file.getAbsolutePath());
                tableAController.init(TableType.A, fileOperator);
                tableBController.init(TableType.B, fileOperator);
                tableCController.init(TableType.C, fileOperator);
                chartAController.initData(ChartType.A, fileOperator);
                chartBController.initData(ChartType.B, fileOperator);
                chartCController.initData(ChartType.C, fileOperator);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successfully loaded \"" + file.getName() + "\".", ButtonType.YES);
                alert.show();
                return;
            } catch (FileNotFoundException fileNotFoundException) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot find file.", ButtonType.YES);
                alert.show();
                return;
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Corrupted file.", ButtonType.YES);
                alert.show();
                return;
            }

        }
    }



}

