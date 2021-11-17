package comp3111.covid.controller;

import comp3111.covid.core.*;
import comp3111.covid.ui.CheckListViewWithList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.time.LocalDate;
import java.util.List;

/**
 * Table Controller class for Table Tabs
 */
public class TableController {

    private TableType tableType;

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
    private TableColumn<DailyStatistics, Integer> tableStat1;

    @FXML
    private TableColumn<DailyStatistics, Double> tableStat2;

    @FXML
    private CheckListViewWithList<String> tableCountryList;

    @FXML
    private Tab pane;

    /**
     * Initialize the table
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
                                    setStyle("-fx-background-color: #ffc0cb;");
                                }
                                if (item.isAfter(utils.dateToLocalDate(fileOperator.getMaximumDate()))) {
                                    setDisable((true));
                                    setStyle("-fx-background-color: #ffc0cb;");
                                }
                            }
                        };
                    }
                };
        datePicker.setDayCellFactory(tableCellFactory);
        text.textProperty().addListener((observable, oldValue, newValue) -> {
            //String countryName = tableAText.getText();
            List<String> countryNames = fileOperator.searchCountry(newValue);
            tableCountryList.update(countryNames);
            text.requestFocus();
        });
        List<String> countryNames = fileOperator.getAllCountries();
        tableCountryList.update(countryNames);

    }

    @FXML
    void doConfirmTable(ActionEvent event) {
        switch (type) {
            case A:
                tableCountry.setCellValueFactory(new PropertyValueFactory<DailyStatistics, String>("country"));
                tableStat1.setCellValueFactory(new PropertyValueFactory<DailyStatistics, Integer>("cumulativeInfected"));
                tableStat2.setCellValueFactory(new PropertyValueFactory<DailyStatistics, Double>("infectedPerMillion"));
                break;
            case B:
                tableCountry.setCellValueFactory(new PropertyValueFactory<DailyStatistics, String>("country"));
                tableStat1.setCellValueFactory(new PropertyValueFactory<DailyStatistics, Integer>("cumulativeDeath"));
                tableStat2.setCellValueFactory(new PropertyValueFactory<DailyStatistics, Double>("deathPerMillion"));
                break;
            case C:
                tableCountry.setCellValueFactory(new PropertyValueFactory<DailyStatistics, String>("country"));
                tableStat1.setCellValueFactory(new PropertyValueFactory<DailyStatistics, Integer>("cumulativeVaccinated"));
                tableStat2.setCellValueFactory(new PropertyValueFactory<DailyStatistics, Double>("vaccinationRate"));
                break;
        }

        Boolean setterRes = TableSetter.update(fileOperator, datePicker, tableCountryList, table);
        if (!setterRes) return;
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

    public void setFileOperator(CSVFileOperator fileOperator) {
        this.fileOperator = fileOperator;
    }
}
