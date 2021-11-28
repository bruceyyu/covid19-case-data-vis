package comp3111.covid;

import javafx.application.Platform;
import comp3111.covid.controller.ChartController;
import comp3111.covid.controller.TableController;
import comp3111.covid.core.data.CSVFileOperator;
import comp3111.covid.core.tabtype.ChartType;
import comp3111.covid.core.tabtype.TableType;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Transform;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * The main controlle class for our application.
 */
public class Controller {

    private static CSVFileOperator fileOperator;

    @FXML
    private Tab tabTaskZero;

    @FXML
    private TextField textfieldDataset;

    @FXML
    TabPane mainTabPane;

    /**
     * Chart A's controller. Made public for testing purposes.
     */
    @FXML
    public ChartController chartAController;

    /**
     * Chart B's controller. Made public for testing purposes.
     */
    @FXML
    public ChartController chartBController;

    /**
     * Chart C's controller. Made public for testing purposes.
     */
    @FXML
    public ChartController chartCController;

    /**
     * Table A's controller. Made public for testing purposes.
     */
    @FXML
    public TableController tableAController;

    /**
     * Table B's controller. Made public for testing purposes.
     */
    @FXML
    public TableController tableBController;

    /**
     * Table C's controller. Made public for testing purposes.
     */
    @FXML
    public TableController tableCController;

    @FXML
    VBox masterVB;

<<<<<<< Updated upstream
    public void initialize() {

    }


=======
    /**
     * Help popup to show help info to the user.
     */
    @FXML
    public void helpPopup() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "DeCOVID is a software that displays and visualizes COVID Data. It's developed by CHEN Yipu, YU Yue, and XU Mingshi with the power of JavaFX. \nFor more information please visit https://github.com/Bruceshark/comp3111_proj_T03", ButtonType.CLOSE);
        alert.setTitle("COMP 3111 - Team 03");
        alert.setHeaderText("Thanks for using our software :)");
        alert.show();
    }
>>>>>>> Stashed changes

    /**
     * Save as image callback to save a chart into an image.
     */
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
        loadFileInternal(file);
    }

    /**
     * Actual file loading an checking logic. Made public for testing purposes.
     * @param file a new CSV file
     */
    public void loadFileInternal(File file) {
        if (file != null) {
            System.out.println(file.toString());

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
                textfieldDataset.setText(file.getName());
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

