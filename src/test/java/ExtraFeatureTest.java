import comp3111.covid.Controller;
import comp3111.covid.controller.ChartController;
import comp3111.covid.controller.TableController;
import comp3111.covid.core.data.SortPolicy;
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

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.Test;

import org.testfx.framework.junit.ApplicationTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;

public class ExtraFeatureTest extends ApplicationTest {

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
    private Tab chartA;
    private ChartController chartAController;
    private Controller ctrl;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(UI_FILE));


        VBox root = (VBox) loader.load();
        chartAController = loader.<Controller>getController().chartAController;
        ChartController chartBController = loader.<Controller>getController().chartBController;
        ChartController chartCController = loader.<Controller>getController().chartCController;
        ctrl = loader.<Controller>getController();
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
    public void testLookUp() {
        TabPane tabPane = (TabPane) s.lookup("#mainTabPane");
        tabPane.getSelectionModel().select(1);
        clickOn("#text");
        sleep(50);
        write("Ch");
        CheckListViewWithList<String> checkListViewWithList = from(tabPane.getTabs().get(1).getContent())
                .lookup("#tableCountryList").query();
        assertEquals("Chad", checkListViewWithList.getItems().get(0));
        assertEquals("China", checkListViewWithList.getItems().get(2));

        tabPane.getSelectionModel().select(4);
        clickOn("#chartText");
        sleep(50);
        write("ch");
        CheckListViewWithList<String> chartAList = from(tabPane.getTabs().get(4).getContent())
                .lookup("#chartCountryList").query();
        assertEquals("Chad", chartAList.getItems().get(0));
        assertEquals("China", chartAList.getItems().get(2));
        //sleep(10000);
    }

    @Test
    public void testSort() {
        TabPane tabPane = (TabPane) s.lookup("#mainTabPane");
        tabPane.getSelectionModel().select(1);

        Node lookUpRoot = tabPane.getTabs().get(1).getContent();

        CheckListViewWithList<String> checkListViewWithList = from(tabPane.getTabs().get(1).getContent())
                .lookup("#tableCountryList").query();
        ChoiceBox<SortPolicy> choiceBox = from(lookUpRoot).lookup("#choiceBox").query();
//        choiceBox.getSelectionModel().select(0);
//        assertEquals("Afghanistan", checkListViewWithList.getItems().get(0));
        interactNoWait(
                () -> {
                    choiceBox.getSelectionModel().select(1);
                }
        );

        assertEquals("Vatican", checkListViewWithList.getItems().get(0));


    }

    @Test
    public void loadNewFileSuccessTest() {
        interact(() ->
                {ctrl.loadFileInternal(new File("src/main/resources/dataset/owid-covid-data.csv"));}
        );
        type(KeyCode.ENTER);

        TextField fileName = lookup("#textfieldDataset").query();
        assertEquals("owid-covid-data.csv",fileName.getText());

        TabPane tabPane = (TabPane) s.lookup("#mainTabPane");
        tabPane.getSelectionModel().select(1);
        Node lookUpRoot = tabPane.getTabs().get(1).getContent();


        DatePicker datePicker = from(lookUpRoot).lookup("#datePicker").query();
        datePicker.setValue(LocalDate.of(2021, 11, 10));
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
        assertEquals(BigInteger.valueOf(156414), confirmed);

    }

    @Test
    public void loadFileFailTest() {
        interact(() ->
                {ctrl.loadFileInternal(new File("illegal.csv"));}
        );
        type(KeyCode.ENTER);

        TextField fileName = lookup("#textfieldDataset").query();
        assertEquals("COVID_Dataset_v1.0.csv",fileName.getText());

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
    public void loadFileNull() {
        interact(() ->
                {ctrl.loadFileInternal(null);}
        );
        type(KeyCode.ENTER);

        TextField fileName = lookup("#textfieldDataset").query();
        assertEquals("COVID_Dataset_v1.0.csv",fileName.getText());

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
    public void saveImageInvalid() {
        clickOn("#fileMenu");
        clickOn("#saveImg");
        type(KeyCode.ENTER);
    }

    @Test
    public void saveImage1() {
        TabPane tabPane = (TabPane)this.s.lookup("#mainTabPane");
        tabPane.getSelectionModel().select(4);
        Node lookUpRoot = ((Tab)tabPane.getTabs().get(4)).getContent();
        this.clickOn("#datePickerEnd", new MouseButton[0]);
        this.clickOn("#datePickerStart", new MouseButton[0]);
        CheckListViewWithList<String> checkListViewWithList = (CheckListViewWithList)this.from(new Node[]{lookUpRoot}).lookup("#chartCountryList").query();
        checkListViewWithList.getCheckModel().check(0);
        checkListViewWithList.getCheckModel().check(1);
        this.clickOn("#doConfirmChart", new MouseButton[0]);

        clickOn("#fileMenu");
        clickOn("#saveImg");
//        sleep(10000); user save the image in this time
    }

    @Test
    public void saveImage2() {
        TabPane tabPane = (TabPane)this.s.lookup("#mainTabPane");
        tabPane.getSelectionModel().select(5);
        Node lookUpRoot = ((Tab)tabPane.getTabs().get(5)).getContent();
        this.clickOn("#datePickerEnd", new MouseButton[0]);
        this.clickOn("#datePickerStart", new MouseButton[0]);
        CheckListViewWithList<String> checkListViewWithList = (CheckListViewWithList)this.from(new Node[]{lookUpRoot}).lookup("#chartCountryList").query();
        checkListViewWithList.getCheckModel().check(0);
        checkListViewWithList.getCheckModel().check(1);
        this.clickOn("#doConfirmChart", new MouseButton[0]);

        clickOn("#fileMenu");
        clickOn("#saveImg");
//        sleep(10000); user save the image in this time
    }

    @Test
    public void saveImage3() {
        TabPane tabPane = (TabPane)this.s.lookup("#mainTabPane");
        tabPane.getSelectionModel().select(6);
        Node lookUpRoot = ((Tab)tabPane.getTabs().get(6)).getContent();
        this.clickOn("#datePickerEnd", new MouseButton[0]);
        this.clickOn("#datePickerStart", new MouseButton[0]);
        CheckListViewWithList<String> checkListViewWithList = (CheckListViewWithList)this.from(new Node[]{lookUpRoot}).lookup("#chartCountryList").query();
        checkListViewWithList.getCheckModel().check(0);
        checkListViewWithList.getCheckModel().check(1);
        this.clickOn("#doConfirmChart", new MouseButton[0]);

        clickOn("#fileMenu");
        clickOn("#saveImg");
//        sleep(10000); user save the image in this time
    }
}
