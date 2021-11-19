package CoreTests;

import static org.junit.Assert.*;

import comp3111.covid.core.data.CSVFileOperator;
import comp3111.covid.core.data.DailyStatistics;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class CSVParserTester {
    CSVFileOperator csvFileOperator;

    @Before
    public void setUp() throws Exception {
        csvFileOperator = new CSVFileOperator("src" + File.separator + "main" + File.separator
                + "resources" + File.separator + "dataset" + File.separator + "COVID_Dataset_v1.0.csv");
    }

    @Test
    public void successfulInitialization() {
        assertNotNull(csvFileOperator);
    }

    @Test(expected = FileNotFoundException.class)
    public void unsuccessfulInitialization() throws FileNotFoundException {
        CSVFileOperator invalidOP = new CSVFileOperator("invaliddir.csv");
    }

    @Test
    public void getCountryTrendByPeriod() throws ParseException {
        SimpleDateFormat a = new SimpleDateFormat("MM/dd/yyyy");
        Date start = a.parse("5/11/2020");
        Date end = a.parse("5/13/2020");
        List<DailyStatistics> dailyStatisticsList = csvFileOperator.getCountryTrend("China", start, end);
        assertEquals(2, dailyStatisticsList.size());
        assertEquals(start, dailyStatisticsList.get(0).getDate());
    }

    @Test
    public void trendDataCheck() throws ParseException {
        SimpleDateFormat a = new SimpleDateFormat("MM/dd/yyyy");
        Date start = a.parse("5/11/2020");
        Date end = a.parse("5/13/2020");
        List<DailyStatistics> chinaTrend = csvFileOperator.getCountryTrend("China", start, end);
        DailyStatistics stat5_11 = chinaTrend.get(0);
       assertEquals("82964", stat5_11.getCumulativeInfected().toString());
        assertEquals("4633", stat5_11.getCumulativeDeath().toString());
    }

    @Test
    public void getSingleData() throws ParseException {
        SimpleDateFormat a = new SimpleDateFormat("MM/dd/yyyy");
        Date start = a.parse("5/11/2021");
        DailyStatistics day = csvFileOperator.getCountryDataOn(start, "Hong Kong");
        assertEquals("Hong Kong", day.getCountry());
        assertEquals("11812", day.getCumulativeInfected().toString());
        assertEquals("210", day.getCumulativeDeath().toString());
        assertEquals(1575.566, day.getInfectedPerMillion(), 0.001);
        assertEquals(28.011, day.getDeathPerMillion(), 0.001);
        assertEquals("704635", day.getCumulativeVaccinated().toString());
        assertEquals(9.4, day.getVaccinationRate(), 0.1);
    }
    
    @Test
    public void getDataSet() throws ParseException {
    	SimpleDateFormat a = new SimpleDateFormat("MM/dd/yyyy");
    	List<String> list = new ArrayList<String>();
        list.add("Hong Kong");
        Date start = a.parse("5/11/2021");
        ArrayList<DailyStatistics> day_1 = csvFileOperator.getCountryDataSetOn(start, list);
        assertEquals("Hong Kong", day_1.get(0).getCountry());
        assertEquals("11812", day_1.get(0).getCumulativeInfected().toString());
        assertEquals("210", day_1.get(0).getCumulativeDeath().toString());
        assertEquals(1575.566, day_1.get(0).getInfectedPerMillion(), 0.001);
        assertEquals(28.011, day_1.get(0).getDeathPerMillion(), 0.001);
        assertEquals("704635", day_1.get(0).getCumulativeVaccinated().toString());
        assertEquals(9.4, day_1.get(0).getVaccinationRate(), 0.1);
    }

    @Test
    public void getCountries() {
        List<String> a = csvFileOperator.getAllCountries();
        for (String s:
             a) {
            System.out.println(s);
        }
    }
    
    @Test 
    public void getCountries_Map() throws ParseException {
    	SimpleDateFormat a = new SimpleDateFormat("MM/dd/yyyy");
        Date start = a.parse("5/11/2020");
        Date end = a.parse("5/13/2020");
        List<String> list = new ArrayList<String>();
        list.add("Hong Kong");
        Map<String, List<DailyStatistics>> countryTrendMap = csvFileOperator.getCountryTrendMap(list, start, end);
        assertTrue(countryTrendMap.size() > 0);
        countryTrendMap =  csvFileOperator.getCountryTrendMap_chartC(list, start, end);
        assertTrue(countryTrendMap.size() > 0);
    }
    @Test 
    public void getCountries_Set() throws ParseException {
    	SimpleDateFormat a = new SimpleDateFormat("MM/dd/yyyy");
        Date start = a.parse("5/11/2020");
        Date end = a.parse("5/13/2020");
        List<String> list = new ArrayList<String>();
        list.add("Hong Kong");
        HashSet<List<DailyStatistics>> countryTrendSet = csvFileOperator.getCountryTrendSet(list, start, end);
        assertTrue(countryTrendSet.size() > 0);
        countryTrendSet = csvFileOperator.getCountryTrendSet(list);
        assertTrue(countryTrendSet.size() > 0);
    }
    @Test
    public void getDate_Mim() throws ParseException, IOException {
    	Date date = csvFileOperator.getMinimumDate();
    	DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
    	String strDate = dateFormat.format(date);
    	assertEquals("2020-00-01", strDate);
    	
    }
    @Test
    public void getDate_Max() throws ParseException, IOException {
    	Date date = csvFileOperator.getMaximumDate();
    	DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
    	String strDate = dateFormat.format(date);
    	assertEquals("2021-00-20", strDate);
    	
    }

    @Test
    public void countryListTest() {
        List<DailyStatistics>  lastDayDataSet = csvFileOperator.getCountryDataSetOn(csvFileOperator.getMaximumDate(),
                csvFileOperator.getAllCountries());
        lastDayDataSet.forEach(System.out::println);
    }

}


