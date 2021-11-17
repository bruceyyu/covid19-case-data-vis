package comp3111.covid;

import comp3111.covid.core.*;
import comp3111.covid.ui.CheckListViewWithList;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.util.StringConverter;

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
    private TextField textfieldISO;

    @FXML
    private Button buttonConfirmedDeaths;

    @FXML
    private TextField textfieldDataset;

    @FXML
    private Button buttonRateOfVaccination;

    @FXML
    private Button buttonConfirmedCases;

    @FXML
    private Tab tabReport1;

    @FXML
    private Tab tabReport2;

    @FXML
    private Tab tabReport3;

    @FXML
    private Tab tabApp1;

    @FXML
    private Tab tabApp2;

    @FXML
    private Tab tabApp3;

    @FXML
    private TextArea textAreaConsole;

    @FXML
    private DatePicker tableADatePicker;

    @FXML
    private Label tableATitle;

    @FXML
    private TableView<DailyStatistics> tableA;

    @FXML
    private TableColumn<DailyStatistics, String> tableACountry;

    @FXML
    private TableColumn<DailyStatistics, Integer> tableATotalCases;

    @FXML
    private TableColumn<DailyStatistics, Double> tableATotalCasesPerM;

    @FXML
    private CheckListViewWithList<String> tableACountryList;

    @FXML
    private Button doConfirmTableA;
    
    @FXML
    private DatePicker tableBDatePicker;

    @FXML
    private Label tableBTitle;

    @FXML
    private TableView<DailyStatistics> tableB;

    @FXML
    private TableColumn<DailyStatistics, String> tableBCountry;

    @FXML
    private TableColumn<DailyStatistics, Integer> tableBTotalDeaths;

    @FXML
    private TableColumn<DailyStatistics, Double> tableBTotalDeathsPerM;

    @FXML
    private CheckListViewWithList<String> tableBCountryList;

    @FXML
    private Button doConfirmTableB;
    
    @FXML
    private DatePicker tableCDatePicker;

    @FXML
    private Label tableCTitle;

    @FXML
    private TableView<DailyStatistics> tableC;

    @FXML
    private TableColumn<DailyStatistics, String> tableCCountry;

    @FXML
    private TableColumn<DailyStatistics, Integer> tableCTotalVaccinated;

    @FXML
    private TableColumn<DailyStatistics, Double> tableCVaccinationRate;

    @FXML
    private CheckListViewWithList<String> tableCCountryList;

    @FXML
    private Button doConfirmTableC;
    
    @FXML
    private LineChart<Number, Number> chartA;

    @FXML
    private NumberAxis chartAX;

    @FXML
    private NumberAxis chartAY;

    @FXML
    private TextField chartAText;

    @FXML
    private CheckListViewWithList<String> chartACountryList;

    @FXML
    private Button chartAButton;

    @FXML
    private DatePicker chartAStartDatePicker;

    @FXML
    private DatePicker chartAEndDatePicker;


    @FXML
    private LineChart<Number, Number> chartB;

    @FXML
    private NumberAxis chartBX;

    @FXML
    private NumberAxis chartBY;

    @FXML
    private TextField chartBText;

    @FXML
    private CheckListViewWithList<String> chartBCountryList;

    @FXML
    private Button chartBButton;

    @FXML
    private DatePicker chartBStartDatePicker;

    @FXML
    private DatePicker chartBEndDatePicker;
    
    @FXML
    private LineChart<Number, Number> chartC;

    @FXML
    private NumberAxis chartCX;

    @FXML
    private NumberAxis chartCY;

    @FXML
    private TextField chartCText;

    @FXML
    private CheckListViewWithList<String> chartCCountryList;

    @FXML
    private Button chartCButton;

    @FXML
    private DatePicker chartCStartDatePicker;

    @FXML
    private DatePicker chartCEndDatePicker;
    
    
    
    @FXML
    private TextField tableAText;
    
    @FXML
    private TextField tableBText; 
    
    @FXML
    private TextField tableCText; 

    public void initialize() {
//        disable the text input of the date picker, but not make it gray
        tableADatePicker.getEditor().setDisable(true);
        tableADatePicker.getEditor().setOpacity(1);
        tableBDatePicker.getEditor().setDisable(true);
        tableBDatePicker.getEditor().setOpacity(1);
        tableCDatePicker.getEditor().setDisable(true);
        tableCDatePicker.getEditor().setOpacity(1);
        final Callback<DatePicker, DateCell> tableACellFactory =
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
       final Callback<DatePicker, DateCell> tableBCellFactory =
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
       final Callback<DatePicker, DateCell> tableCCellFactory =
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
        
        tableADatePicker.setDayCellFactory(tableACellFactory);
        tableBDatePicker.setDayCellFactory(tableBCellFactory);
        tableCDatePicker.setDayCellFactory(tableCCellFactory);
        
        tableAText.textProperty().addListener((observable, oldValue, newValue) -> {
            //String countryName = tableAText.getText();
            List<String> countryNames = fileOperator.searchCountry(newValue);
            tableACountryList.update(countryNames);
            tableAText.requestFocus();
        });
        tableBText.textProperty().addListener((observable, oldValue, newValue) -> {
            //String countryName = tableAText.getText();
            List<String> countryNames = fileOperator.searchCountry(newValue);
            tableBCountryList.update(countryNames);
            tableBText.requestFocus();
        });
        tableCText.textProperty().addListener((observable, oldValue, newValue) -> {
            //String countryName = tableAText.getText();
            List<String> countryNames = fileOperator.searchCountry(newValue);
            tableCCountryList.update(countryNames);
            tableCText.requestFocus();
        });
        
        List<String> countryNames = fileOperator.getAllCountries();
        tableACountryList.update(countryNames);
        tableBCountryList.update(countryNames);
        tableCCountryList.update(countryNames);
        
        //chartB.setAnimated(false);
		
		chartAStartDatePicker.getEditor().setDisable(true);
		chartAStartDatePicker.getEditor().setOpacity(1);
		chartAStartDatePicker.setValue(utils.dateToLocalDate(fileOperator.getMinimumDate())); 
		chartAEndDatePicker.getEditor().setDisable(true);
		chartAEndDatePicker.getEditor().setOpacity(1);
	 	chartAEndDatePicker.setValue(utils.dateToLocalDate(fileOperator.getMaximumDate()));
		 
        chartBStartDatePicker.getEditor().setDisable(true);
        chartBStartDatePicker.getEditor().setOpacity(1);
        chartBStartDatePicker.setValue(utils.dateToLocalDate(fileOperator.getMinimumDate()));
        chartBEndDatePicker.getEditor().setDisable(true);
        chartBEndDatePicker.getEditor().setOpacity(1);
        chartBEndDatePicker.setValue(utils.dateToLocalDate(fileOperator.getMaximumDate()));
        
        chartCStartDatePicker.getEditor().setDisable(true);
        chartCStartDatePicker.getEditor().setOpacity(1);
        chartCStartDatePicker.setValue(utils.dateToLocalDate(fileOperator.getMinimumDate()));
        chartCEndDatePicker.getEditor().setDisable(true);
        chartCEndDatePicker.getEditor().setOpacity(1);
        chartCEndDatePicker.setValue(utils.dateToLocalDate(fileOperator.getMaximumDate()));
        
        final Callback<DatePicker, DateCell> tableACellFactoryEnd =
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);

                                if (item.isBefore(
                                        chartAStartDatePicker.getValue().plusDays(1))
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
        final Callback<DatePicker, DateCell> tableACellFactoryStart =
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
                                if (item.isAfter(utils.dateToLocalDate(fileOperator.getMaximumDate()).minusDays(1))) {
                                    setDisable((true));
                                    setStyle("-fx-background-color: #ffc0cb;");
                                }
                            }
                        };
                    }
                };
        final Callback<DatePicker, DateCell> tableBCellFactoryEnd =
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);

                                if (item.isBefore(
                                        chartBStartDatePicker.getValue().plusDays(1))
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
        final Callback<DatePicker, DateCell> tableBCellFactoryStart =
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
                                if (item.isAfter(utils.dateToLocalDate(fileOperator.getMaximumDate()).minusDays(1))) {
                                    setDisable((true));
                                    setStyle("-fx-background-color: #ffc0cb;");
                                }
                            }
                        };
                    }
                };
                final Callback<DatePicker, DateCell> tableCCellFactoryEnd =
                        new Callback<DatePicker, DateCell>() {
                            @Override
                            public DateCell call(final DatePicker datePicker) {
                                return new DateCell() {
                                    @Override
                                    public void updateItem(LocalDate item, boolean empty) {
                                        super.updateItem(item, empty);

                                        if (item.isBefore(
                                                chartCStartDatePicker.getValue().plusDays(1))
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
                final Callback<DatePicker, DateCell> tableCCellFactoryStart =
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
                                        if (item.isAfter(utils.dateToLocalDate(fileOperator.getMaximumDate()).minusDays(1))) {
                                            setDisable((true));
                                            setStyle("-fx-background-color: #ffc0cb;");
                                        }
                                    }
                                };
                            }
                        };
                
        chartAStartDatePicker.setDayCellFactory(tableACellFactoryStart);
        chartAEndDatePicker.setDayCellFactory(tableACellFactoryEnd);
                
        chartBStartDatePicker.setDayCellFactory(tableBCellFactoryStart);
        chartBEndDatePicker.setDayCellFactory(tableBCellFactoryEnd);
        
        chartCStartDatePicker.setDayCellFactory(tableCCellFactoryStart);
        chartCEndDatePicker.setDayCellFactory(tableCCellFactoryEnd);
        
        chartAStartDatePicker.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.isAfter(chartAEndDatePicker.getValue()) || newValue.isEqual(chartAEndDatePicker.getValue()))
                chartAEndDatePicker.setValue(chartAStartDatePicker.getValue().plusDays(1));
        }));
        
        chartBStartDatePicker.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.isAfter(chartBEndDatePicker.getValue()) || newValue.isEqual(chartBEndDatePicker.getValue()))
                chartBEndDatePicker.setValue(chartBStartDatePicker.getValue().plusDays(1));
        }));
        
        chartCStartDatePicker.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.isAfter(chartCEndDatePicker.getValue()) || newValue.isEqual(chartCEndDatePicker.getValue()))
                chartCEndDatePicker.setValue(chartCStartDatePicker.getValue().plusDays(1));
        }));
        
        chartAText.textProperty().addListener((observable, oldValue, newValue) -> {
            //String countryName = tableAText.getText();
            List<String> countryNamesAdd = fileOperator.searchCountry(newValue);
            chartACountryList.update(countryNamesAdd);
            chartAText.requestFocus();
        });

        
        chartACountryList.update(countryNames);
        
        chartBText.textProperty().addListener((observable, oldValue, newValue) -> {
            //String countryName = tableAText.getText();
            List<String> countryNamesAdd = fileOperator.searchCountry(newValue);
            chartBCountryList.update(countryNamesAdd);
            chartBText.requestFocus();
        });

        
        chartBCountryList.update(countryNames);
        
        chartCText.textProperty().addListener((observable, oldValue, newValue) -> {
            //String countryName = tableAText.getText();
            List<String> countryNamesAdd = fileOperator.searchCountry(newValue);
            chartCCountryList.update(countryNamesAdd);
            chartCText.requestFocus();
        });

        
        chartCCountryList.update(countryNames);
    }

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

    /*
    @FXML
    void tableAFilter(ActionEvent event) {
        String countryName = tableAText.getText();
        List<String> countryNames = fileOperator.searchCountry(countryName);
        tableACountryList.update(countryNames);
    }*/


    @FXML
    void doConfirmTableA(ActionEvent event) {
        tableACountry.setCellValueFactory(new PropertyValueFactory<DailyStatistics, String>("country"));
        tableATotalCases.setCellValueFactory(new PropertyValueFactory<DailyStatistics, Integer>("cumulativeInfected"));
        tableATotalCasesPerM.setCellValueFactory(new PropertyValueFactory<DailyStatistics, Double>("infectedPerMillion"));
        Boolean setterRes = TableSetter.update(fileOperator, tableADatePicker, tableACountryList, tableA);
        if (!setterRes) return;
        String dateString = utils.localDateToString(tableADatePicker.getValue(), "MMMM dd, uuuu");
        tableATitle.setText("Number of Confirmed COVID-19 Cases as of " + dateString);
    }

    @FXML
    void doConfirmTableB(ActionEvent event) {
    	tableBCountry.setCellValueFactory(new PropertyValueFactory<DailyStatistics, String>("country"));
        tableBTotalDeaths.setCellValueFactory(new PropertyValueFactory<DailyStatistics, Integer>("cumulativeDeath"));
        tableBTotalDeathsPerM.setCellValueFactory(new PropertyValueFactory<DailyStatistics, Double>("deathPerMillion"));
        Boolean setterRes = TableSetter.update(fileOperator, tableBDatePicker, tableBCountryList, tableB);
        if (!setterRes) return;
        String dateString = utils.localDateToString(tableBDatePicker.getValue(), "MMMM dd, uuuu");
        tableBTitle.setText("Number of Confirmed COVID-19 Deaths as of " + dateString);
//    	tableACountry.setCellValueFactory(new PropertyValueFactory<DailyStatistics, String>("country"));
//        tableATotalCases.setCellValueFactory(new PropertyValueFactory<DailyStatistics, Integer>("cumulativeDeath"));
//        tableATotalCasesPerM.setCellValueFactory(new PropertyValueFactory<DailyStatistics, Double>("deathPerMillion"));
//        Boolean setterRes = TableSetter.update(fileOperator, tableADatePicker, tableACountryList, tableA);
//        if (!setterRes) return;
//        String dateString = utils.localDateToString(tableADatePicker.getValue(), "MMMM dd, uuuu");
//        tableATitle.setText("Number of Confirmed COVID-19 Cases as of " + dateString);
//    
        }
    
    @FXML
    void doConfirmTableC(ActionEvent event) {
    	tableCCountry.setCellValueFactory(new PropertyValueFactory<DailyStatistics, String>("country"));
    	tableCTotalVaccinated.setCellValueFactory(new PropertyValueFactory<DailyStatistics, Integer>("cumulativeVaccinated"));
        tableCVaccinationRate.setCellValueFactory(new PropertyValueFactory<DailyStatistics, Double>("vaccinationRate"));
        Boolean setterRes = TableSetter.update(fileOperator, tableCDatePicker, tableCCountryList, tableC);
        if (!setterRes) return;
        String dateString = utils.localDateToString(tableCDatePicker.getValue(), "MMMM dd, uuuu");
        tableCTitle.setText("Rate of Vaccination against COVID-19 as of " + dateString);
//    	tableACountry.setCellValueFactory(new PropertyValueFactory<DailyStatistics, String>("country"));
//        tableATotalCases.setCellValueFactory(new PropertyValueFactory<DailyStatistics, Integer>("cumulativeInfected"));
//        tableATotalCasesPerM.setCellValueFactory(new PropertyValueFactory<DailyStatistics, Double>("infectedPerMillion"));
//        Boolean setterRes = TableSetter.update(fileOperator, tableADatePicker, tableACountryList, tableA);
//        if (!setterRes) return;
//        String dateString = utils.localDateToString(tableADatePicker.getValue(), "MMMM dd, uuuu");
//        tableATitle.setText("Number of Confirmed COVID-19 Cases as of " + dateString);
    }
    
    @FXML
    void doConfirmChartA(ActionEvent event) {
        ChartSetter.setGraphPropeties(chartA, chartAStartDatePicker, chartAEndDatePicker);

        // save state of checked
        chartACountryList.saveState();
        List<String> countryNames = chartACountryList.getCheckedItems();
        Map<String, List<DailyStatistics>> countryTrendMap = fileOperator.getCountryTrendMap(countryNames,
                utils.localDateToDate(chartAStartDatePicker.getValue()), utils.localDateToDate(chartAEndDatePicker.getValue()));
        if (countryTrendMap.size() <= 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select at least one country", ButtonType.YES);
            alert.show();
            return;
        }
        ChartSetter.updateGraph_A(chartA, countryTrendMap);
    }


    @FXML
    void doConfirmChartB(ActionEvent event) {
        ChartSetter.setGraphPropeties(chartB, chartBStartDatePicker, chartBEndDatePicker);

        // save state of checked
        chartBCountryList.saveState();
        List<String> countryNames = chartBCountryList.getCheckedItems();
        Map<String, List<DailyStatistics>> countryTrendMap = fileOperator.getCountryTrendMap(countryNames,
                utils.localDateToDate(chartBStartDatePicker.getValue()), utils.localDateToDate(chartBEndDatePicker.getValue()));
        if (countryTrendMap.size() <= 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select at least one country", ButtonType.YES);
            alert.show();
            return;
        }
        ChartSetter.updateGraph_B(chartB, countryTrendMap);
    }
    
    @FXML
    void doConfirmChartC(ActionEvent event) {
        ChartSetter.setGraphPropeties_C(chartC, chartCStartDatePicker, chartCEndDatePicker);

        // save state of checked
        chartCCountryList.saveState();
        List<String> countryNames = chartCCountryList.getCheckedItems();
        Map<String, List<DailyStatistics>> countryTrendMap = fileOperator.getCountryTrendMap_chartC(countryNames,
                utils.localDateToDate(chartCStartDatePicker.getValue()), utils.localDateToDate(chartCEndDatePicker.getValue()));
        if (countryTrendMap.size() <= 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select at least one country", ButtonType.YES);
            alert.show();
            return;
        }
        ChartSetter.updateGraph_C(chartC, countryTrendMap);
    }

    private static Calendar start;

    static {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        try {
            startDate = formatter.parse("2020-01-20");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date endDate = null;


        start = Calendar.getInstance();
        start.setTime(startDate);

    }


    @FXML
    void chartBGo(ActionEvent event) throws InterruptedException, ParseException {
        ObservableList<XYChart.Series<Number, Number>> chartDataSeriesList = chartB.getData();
        for (XYChart.Series<Number, Number> series : chartDataSeriesList) {
            series.getData().clear();
        }
    }
    @FXML
    void chartCGo(ActionEvent event) throws InterruptedException, ParseException {
        ObservableList<XYChart.Series<Number, Number>> chartDataSeriesList = chartC.getData();
        for (XYChart.Series<Number, Number> series : chartDataSeriesList) {
            series.getData().clear();
        }
    }


    /**
     * Task Zero
     * To be triggered by the "Confirmed Cases" button on the Task Zero Tab
     */
    @FXML
    void doConfirmedCases(ActionEvent event) {
        String iDataset = textfieldDataset.getText();
        String iISO = textfieldISO.getText();
        String oReport = DataAnalysis.getConfirmedCases(iDataset, iISO);
        textAreaConsole.setText(oReport);
    }


    /**
     * Task Zero
     * To be triggered by the "Confirmed Deaths" button on the Task Zero Tab
     */
    @FXML
    void doConfirmedDeaths(ActionEvent event) {
        String iDataset = textfieldDataset.getText();
        String iISO = textfieldISO.getText();
        String oReport = DataAnalysis.getConfirmedDeaths(iDataset, iISO);
        textAreaConsole.setText(oReport);
    }


    /**
     * Task Zero
     * To be triggered by the "Rate of Vaccination" button on the Task Zero Tab
     */
    @FXML
    void doRateOfVaccination(ActionEvent event) {
        String iDataset = textfieldDataset.getText();
        String iISO = textfieldISO.getText();
        String oReport = DataAnalysis.getRateOfVaccination(iDataset, iISO);
        textAreaConsole.setText(oReport);
    }


}

