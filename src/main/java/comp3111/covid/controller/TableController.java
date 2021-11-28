package comp3111.covid.controller;

import comp3111.covid.core.*;
import comp3111.covid.core.data.CSVFileOperator;
import comp3111.covid.core.data.DailyStatistics;
import comp3111.covid.core.data.SortPolicy;
import comp3111.covid.core.data.SortPolicyE;
import comp3111.covid.core.tabtype.TableType;
import comp3111.covid.core.uisetters.TableSetter;
import comp3111.covid.ui.CheckListViewWithList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

/**
 * Table Controller class for table tabs
 */
public class TableController {

    private CSVFileOperator fileOperator;

    private TableType type;

    @FXML
    DatePicker datePicker;

    @FXML
    TextField text;

    @FXML
    CheckListViewWithList<String> checkListViewWithList;

    @FXML
    Label tableTitle;

    @FXML
    TableView<DailyStatistics> table;

    @FXML
    private TableColumn<DailyStatistics, String> tableCountry;

    @FXML
    private TableColumn<DailyStatistics, BigInteger> tableStat1;

    @FXML
    private TableColumn<DailyStatistics, Double> tableStat2;

    @FXML
    private CheckListViewWithList<String> tableCountryList;

    @FXML
    private Tab pane;

    @FXML
    private ChoiceBox<SortPolicy> choiceBox;

    /**
     * Initialize the table, set the table type as well as the fileOperator used for
     * data lookup. This will also set some properties of the UI.
     * @param type Table Type
     * @param fileOperator fileOperator
     */
    public void init(TableType type, CSVFileOperator fileOperator) {
        this.fileOperator = fileOperator;
        this.type = type;
        switch (type) {
            case A:
                tableStat1.setText("Total Cases");
                tableStat2.setText("Total Cases (per 1M)");
                pane.setText("Cases Table");
                break;
            case B:
                tableStat1.setText("Total Deaths");
                tableStat2.setText("Total Deaths (per 1M)");
                pane.setText("Deaths Table");
                break;
            case C:
                tableStat1.setText("Fully Vaccinated");
                tableStat2.setText("Vaccination Rate");
                pane.setText("Vaccination Table");
                break;
        }

        datePicker.getEditor().setDisable(true);
        datePicker.getEditor().setOpacity(1);

        tableTitle.setText("");

        final Callback<DatePicker, DateCell> tableCellFactory =
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);

                                if (item.isBefore(
                                        utils.dateToLocalDate(fileOperator.getMinimumDate()))
                                ) {
                                    setDisable(true);
                                    getStyleClass().add("disabled-date");
                                }
                                if (item.isAfter(utils.dateToLocalDate(fileOperator.getMaximumDate()))) {
                                    setDisable((true));
                                    getStyleClass().add("disabled-date");
                                }
                            }
                        };
                    }
                };
        datePicker.setDayCellFactory(tableCellFactory);

        choiceBox.getItems().clear();
        choiceBox.getItems().add(new SortPolicy("Country name", SortPolicyE.NAME));
        choiceBox.getItems().add(new SortPolicy("Population", SortPolicyE.POP));
        choiceBox.getItems().add(new SortPolicy("Population density", SortPolicyE.POP_D));
        choiceBox.getItems().add(new SortPolicy("Median age", SortPolicyE.MED));
        choiceBox.getItems().add(new SortPolicy("GDP per capita", SortPolicyE.GDP));
        choiceBox.getSelectionModel().select(0);
        choiceBox.setOnAction((event) -> {
            SortPolicy selectedItem = choiceBox.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                    List<String> countryNames = fileOperator.searchCountry(text.getText(), selectedItem.policyE);
                    tableCountryList.update(countryNames);}


        });

        text.setText("");
        text.textProperty().addListener((observable, oldValue, newValue) -> {
            //String countryName = tableAText.getText();
            List<String> countryNames = fileOperator.searchCountry(newValue, choiceBox.getSelectionModel().getSelectedItem().policyE);
            tableCountryList.update(countryNames);
            text.requestFocus();
        });

        List<String> countryNames = fileOperator.getAllCountries();
        tableCountryList.init(countryNames);
        table.getItems().clear();



    }

    @FXML
    void doConfirmTable(ActionEvent event) {
        String setterRes = "";
        switch (type) {
            case A:
                tableCountry.setCellValueFactory(new PropertyValueFactory<DailyStatistics, String>("country"));
                tableStat1.setCellValueFactory(new PropertyValueFactory<DailyStatistics, BigInteger>("cumulativeInfected"));
                tableStat2.setCellValueFactory(new PropertyValueFactory<DailyStatistics, Double>("infectedPerMillion"));
                setterRes = TableSetter.update(fileOperator, datePicker, tableCountryList, table);
                break;
            case B:
                tableCountry.setCellValueFactory(new PropertyValueFactory<DailyStatistics, String>("country"));
                tableStat1.setCellValueFactory(new PropertyValueFactory<DailyStatistics, BigInteger>("cumulativeDeath"));
                tableStat2.setCellValueFactory(new PropertyValueFactory<DailyStatistics, Double>("deathPerMillion"));
                setterRes = TableSetter.update(fileOperator, datePicker, tableCountryList, table);
                break;
            case C:
                tableCountry.setCellValueFactory(new PropertyValueFactory<DailyStatistics, String>("country"));
                tableStat1.setCellValueFactory(new PropertyValueFactory<DailyStatistics, BigInteger>("cumulativeVaccinated"));
                tableStat2.setCellValueFactory(new PropertyValueFactory<DailyStatistics, Double>("vaccinationRate"));
                setterRes = TableSetter.updateTableC(fileOperator, datePicker, tableCountryList, table);
//                override: double to percentage
                tableStat2.setCellFactory((tableColumn) -> {
                    TableCell<DailyStatistics, Double> tableCell = new TableCell<>() {
                        @Override
                        protected void updateItem(Double item, boolean empty) {
                            System.out.println(item);
                            super.updateItem(item, empty);
                            if(!empty){
                                this.setText(item.toString() + "%");
                            }
                        }
                    };

                    return tableCell;
                });
                break;
        }

        if (setterRes != "success") {
            Alert alert = new Alert(Alert.AlertType.ERROR, setterRes, ButtonType.YES);
            alert.show();
            return;
        }
        String dateString = utils.localDateToString(datePicker.getValue(), "MMMM dd, uuuu");
        switch (type) {
            case A:
                tableTitle.setText("Number of Confirmed COVID-19 Cases as of " + dateString);
                break;
            case B:
                tableTitle.setText("Number of Confirmed COVID-19 Deaths as of " + dateString);
                break;
            case C:
                tableTitle.setText("Rate of Vaccination against COVID-19 as of " + dateString);
                break;
        }


    }

}
