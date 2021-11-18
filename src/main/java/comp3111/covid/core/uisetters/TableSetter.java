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

public class TableSetter {
    /**
     * set the TableView with the specified country list on the date of interest
     * @param fileOperator The instance of CSVFileOperator
     * @param datePicker The DatePicker instance from the desired tab
     * @param countryList The CheckListView instance from the desired  tab
     * @param table The TableView instance from the desired  tab
     * @return Boolean to indicate whether the table is set successfully
     */
    static public Boolean setter(CSVFileOperator fileOperator, DatePicker datePicker, CheckListView countryList, TableView table) {

        Date pickedDate = utils.localDateToDate(datePicker.getValue());
        if (!legalDateChecker(fileOperator, pickedDate)) return false;

        List<String> countryNames = countryList.getItems();
        ArrayList<String> pickedCountryList = new ArrayList<String>();
        for (int i = 0; i < countryNames.size(); i ++) {
            if (countryList.getItemBooleanProperty(i).get()) {
                pickedCountryList.add(countryNames.get(i));
            }
        }
        if (!legalCountryListChecker(pickedCountryList)) return false;

        ArrayList<DailyStatistics> countryData = fileOperator.getCountryDataSetOn(pickedDate, pickedCountryList);
        table.setItems(FXCollections.observableArrayList(countryData));

        return true;
    }

    static public Boolean update(CSVFileOperator fileOperator, DatePicker datePicker, CheckListViewWithList countryList, TableView table) {

        Date pickedDate = utils.localDateToDate(datePicker.getValue());
        if (!legalDateChecker(fileOperator, pickedDate)) return false;
        countryList.saveState();

        List<String> pickedCountryList = countryList.getCheckedItems();
        if (!legalCountryListChecker(pickedCountryList)) return false;

        ArrayList<DailyStatistics> countryData = fileOperator.getCountryDataSetOn(pickedDate, pickedCountryList);
        table.setItems(FXCollections.observableArrayList(countryData));

        return true;
    }

    private static Boolean legalDateChecker(CSVFileOperator fileOperator, Date date) {
        if (date == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Please select a date of interest", ButtonType.YES);
            alert.show();
            return false;
        }
        Date maxDate = fileOperator.getMaximumDate();
        Date minDate = fileOperator.getMinimumDate();
        if (date.before(minDate) || date.after(maxDate)) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"The date is beyond the legal range", ButtonType.YES);
            alert.show();
            return false;
        }
        return true;
    }

    private static Boolean legalCountryListChecker(List<String> countryList) {
        System.out.println(countryList);
        if (countryList.size() <= 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Please select at least one country", ButtonType.YES);
            alert.show();
            return false;
        }
        return true;
    }
}
