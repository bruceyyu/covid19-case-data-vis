package comp3111.covid.core.uisetters;

import comp3111.covid.core.data.CSVFileOperator;
import comp3111.covid.core.data.DailyStatistics;
import comp3111.covid.core.utils;
import comp3111.covid.ui.CheckListViewWithList;
import javafx.collections.FXCollections;
import org.controlsfx.control.CheckListView;

import javafx.scene.control.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * Setter classes the tables for Task A1, B1, C1
 */
public class TableSetter {
    /**
     * set the TableView with the specified country list on the date of interest
     * @param fileOperator The instance of CSVFileOperator
     * @param datePicker The DatePicker instance from the desired tab
     * @param countryList The CheckListView instance from the desired  tab
     * @param table The TableView instance from the desired  tab
     * @return Boolean to indicate whether the table is set successfully
     */
    static public String update(CSVFileOperator fileOperator, DatePicker datePicker, CheckListViewWithList countryList, TableView table) {

        Date pickedDate = utils.localDateToDate(datePicker.getValue());
        String legalDateCheckerRes = legalDateChecker(fileOperator, pickedDate);
        if (legalDateCheckerRes != "success") {
            return legalDateCheckerRes;
        }
        countryList.saveState();

        List<String> pickedCountryList = countryList.getCheckedItems();
        String legalCountryListCheckerRes = legalCountryListChecker(pickedCountryList);
        if (legalCountryListCheckerRes != "success") {
            return legalCountryListCheckerRes;
        }

        ArrayList<DailyStatistics> countryData = fileOperator.getCountryDataSetOn(pickedDate, pickedCountryList);
        table.setItems(FXCollections.observableArrayList(countryData));

        return "success";
    }

    /**
     * set the TableView for Table C specifically with the specified country list on the date of interest
     * @param fileOperator The instance of CSVFileOperator
     * @param datePicker The DatePicker instance from the desired tab
     * @param countryList The CheckListView instance from the desired  tab
     * @param table The TableView instance from the desired  tab
     * @return Boolean to indicate whether the table is set successfully
     */
    static public String updateTableC(CSVFileOperator fileOperator, DatePicker datePicker, CheckListViewWithList countryList, TableView table) {

        Date pickedDate = utils.localDateToDate(datePicker.getValue());
        String legalDateCheckerRes = legalDateChecker(fileOperator, pickedDate);
        if (legalDateCheckerRes != "success") {
            return legalDateCheckerRes;
        }
        countryList.saveState();

        List<String> pickedCountryList = countryList.getCheckedItems();
        String legalCountryListCheckerRes = legalCountryListChecker(pickedCountryList);
        if (legalCountryListCheckerRes != "success") {
            return legalCountryListCheckerRes;
        }

        ArrayList<DailyStatistics> countryData = fileOperator.getCountryDataSetOn(pickedDate, pickedCountryList);
        Map<String, List<DailyStatistics>> countryDataTrend = fileOperator.getCountryTrendMap(pickedCountryList, fileOperator.getMinimumDate(), pickedDate);
//        if the data on that day is empty, need to backtrace the day with valid data
        for (int i = 0; i < countryData.size(); i ++) {
            System.out.println(countryData.get(i).getCumulativeVaccinated());
            if (countryData.get(i).getCumulativeVaccinated() == null) {
                List<DailyStatistics> historyTrend = countryDataTrend.get(countryData.get(i).getCountry());
                for (int j = historyTrend.size() - 1; j >= 0; j--) {
                    if (historyTrend.get(j).getCumulativeVaccinated() != null) {
                        countryData.set(i, historyTrend.get(j));
                        break;
                    }
                }
            }
        }
        table.setItems(FXCollections.observableArrayList(countryData));

        return "success";
    }

    /**
     * check whether the input date is legal
     * @param fileOperator The instance of CSVFileOperator
     * @param date The picked date
     * @return String to indicate whether it's legal, or its error message
     */
    public static String legalDateChecker(CSVFileOperator fileOperator, Date date) {
        if (date == null) {
            return "Please select a date of interest";
        }
        Date maxDate = fileOperator.getMaximumDate();
        Date minDate = fileOperator.getMinimumDate();
        if (date.before(minDate) || date.after(maxDate)) {
            return "The date is beyond the legal range";
        }
        return "success";
    }
    /**
     * check whether the input countryList is legal
     * @param countryList The picked country List
     * @return String to indicate whether it's legal, or its error message
     */
    public static String legalCountryListChecker(List<String> countryList) {
        if (countryList.size() <= 0) {
            return "Please select at least one country";
        }
        return "success";
    }
}
