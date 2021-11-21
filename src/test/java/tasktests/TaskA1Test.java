package tasktests;

import comp3111.covid.Controller;
import comp3111.covid.controller.ChartController;
import comp3111.covid.controller.TableController;
import comp3111.covid.core.data.CSVFileOperator;
import comp3111.covid.core.data.DailyStatistics;
import comp3111.covid.core.tabtype.ChartType;
import comp3111.covid.core.tabtype.TableType;
import comp3111.covid.ui.CheckListViewWithList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;

import static org.junit.Assert.*;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.Test;

import org.testfx.framework.junit.ApplicationTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Collection;

public class TaskA1Test extends ApplicationTest {

    private static final String UI_FILE = "/fxmls/ui_seperated.fxml";  //file in the folder of src/main/resources/
    private static CSVFileOperator fileOperator;
    static {
        try {
            fileOperator = new CSVFileOperator("src" + File.separator + "main" + File.separator + "resources" + File.separator + "COVID_Dataset_v1.0.csv");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            String currentPath = null;
            try {
                currentPath = new java.io.File(".").getCanonicalPath();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            System.out.println("Current dir:" + currentPath);

            String currentDir = System.getProperty("user.dir");
            System.out.println("Current dir using System:" + currentDir);
        }
    }

    private Scene s;
    private ChartController chartAController;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(UI_FILE));


        VBox root = (VBox) loader.load();
        chartAController = loader.<Controller>getController().chartAController;
        ChartController chartBController = loader.<Controller>getController().chartBController;
        ChartController chartCController = loader.<Controller>getController().chartCController;
        chartAController.initData(ChartType.A, fileOperator);
        chartBController.initData(ChartType.B, fileOperator);
        chartCController.initData(ChartType.C, fileOperator);

        TableController tableAController = loader.<Controller>getController().tableAController;
        TableController tableBController = loader.<Controller>getController().tableBController;
        TableController tableCController = loader.<Controller>getController().tableCController;
        tableAController.init(TableType.A, fileOperator);
        tableBController.init(TableType.B, fileOperator);
        tableCController.init(TableType.C, fileOperator);
        Scene scene =  new Scene(root);
        stage.setScene(scene);
        stage.setTitle("COMP3111 Team-T03");
        stage.show();
        s = scene;
    }

    @Test
    public void testValidDate() {
        TabPane tabPane = (TabPane) s.lookup("#mainTabPane");
        tabPane.getSelectionModel().select(1);
        Node lookUpRoot = tabPane.getTabs().get(1).getContent();


        DatePicker datePicker = from(lookUpRoot).lookup("#datePicker").query();
        datePicker.setValue(LocalDate.of(2021, 5, 10));
        LocalDate a = datePicker.getValue();
        clickOn("#datePicker");
        CheckListViewWithList<String> checkListViewWithList = from(lookUpRoot)
                .lookup("#tableCountryList").query();
        checkListViewWithList.getCheckModel().check(0);
        checkListViewWithList.getCheckModel().check(1);

        clickOn("#doConfirmTable");

        TableView<DailyStatistics> table = from(lookUpRoot).lookup("#table").queryTableView();
        TableColumn<DailyStatistics, BigInteger> column1 = (TableColumn<DailyStatistics, BigInteger>) table.getColumns().get(1);
        BigInteger confirmed =  (BigInteger) column1.getCellObservableValue(0).getValue();
        assertEquals(BigInteger.valueOf(62063), confirmed);


    }

    @Test
    public void testSort() {

    }
}
