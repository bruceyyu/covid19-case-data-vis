package coretests;

import static org.junit.Assert.*;

import com.sun.javafx.application.PlatformImpl;
import comp3111.covid.core.data.CSVFileOperator;
import comp3111.covid.core.uisetters.TableSetter;
import comp3111.covid.core.utils;
import comp3111.covid.ui.CheckListViewWithList;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TableSetterTester {
    CSVFileOperator csvFileOperator;

    @Before
    public void setUp() throws Exception {
        csvFileOperator = new CSVFileOperator("src" + File.separator + "main" + File.separator
                + "resources" + File.separator + "dataset" + File.separator + "COVID_Dataset_v1.0.csv");
    }

    @Test
    public  void updateTest() throws  ParseException {
        PlatformImpl.startup(() -> {});
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date legalDate = null;
        try {
            legalDate = df.parse("2020-10-01");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(utils.dateToLocalDate(legalDate));
        ArrayList<String> countries = new ArrayList<>();
        countries.add("Hong Kong");
        CheckListViewWithList countryList = new CheckListViewWithList();
        countryList.init(countries);
        countryList.getCheckModel().checkAll();
        TableView table = new TableView();

        String res = TableSetter.update(csvFileOperator, datePicker, countryList, table);
        System.out.println(res);
    }

    @Test
    public void legalDateTest() throws ParseException {
        PlatformImpl.startup(() -> {});
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date legalDate = null;
        try {
            legalDate = df.parse("2020-10-01");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(utils.dateToLocalDate(legalDate));
        ArrayList<String> countries = new ArrayList<>();
        countries.add("Hong Kong");
        CheckListViewWithList countryList = new CheckListViewWithList();
        countryList.init(countries);
        countryList.getCheckModel().checkAll();
        TableView table = new TableView();

        String res = TableSetter.update(csvFileOperator, datePicker, countryList, table);
        assertEquals("success",res);
    }

    @Test
    public void illegalDateTest() throws ParseException {
        PlatformImpl.startup(() -> {});
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date smallDate = null;
        Date largeDate = null;
        try {
            smallDate = df.parse("2000-01-01");
            largeDate = df.parse("2030-01-01");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DatePicker smallDatePicker = new DatePicker();
        DatePicker largeDatePicker = new DatePicker();
        DatePicker nullDatePicker = new DatePicker();
        smallDatePicker.setValue(utils.dateToLocalDate(smallDate));
        largeDatePicker.setValue(utils.dateToLocalDate(largeDate));
        ArrayList<String> countries = new ArrayList<>();
        countries.add("Hong Kong");
        CheckListViewWithList countryList = new CheckListViewWithList();
        countryList.init(countries);
        countryList.getCheckModel().checkAll();
        TableView table = new TableView();

        String smallRes = TableSetter.update(csvFileOperator, smallDatePicker, countryList, table);
        String largeRes = TableSetter.update(csvFileOperator, largeDatePicker, countryList, table);
        String nullRes = TableSetter.update(csvFileOperator, nullDatePicker, countryList, table);
        assertEquals("The date is beyond the legal range",smallRes);
        assertEquals("The date is beyond the legal range",largeRes);
        assertEquals("Please select a date of interest",nullRes);
    }

    @Test
    public void illegalCountryListTest() throws ParseException {
        PlatformImpl.startup(() -> {});
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date legalDate = null;
        try {
            legalDate = df.parse("2020-10-01");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(utils.dateToLocalDate(legalDate));
        ArrayList<String> countries = new ArrayList<>();
        countries.add("Hong Kong");
        CheckListViewWithList countryList = new CheckListViewWithList();
        countryList.init(countries);
        countryList.update(countries);
        countryList.clear();
        TableView table = new TableView();

        String res = TableSetter.update(csvFileOperator, datePicker, countryList, table);

        assertEquals("Please select at least one country",res);
    }
}


