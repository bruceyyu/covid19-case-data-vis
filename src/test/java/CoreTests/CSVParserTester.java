package CoreTests;

import static org.junit.Assert.*;

import comp3111.covid.core.CSVFileOperator;
import comp3111.covid.core.DailyStatistics;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    public void getTrendValidCountryName() throws ParseException {
        List<DailyStatistics> countryTrendChina = csvFileOperator.getCountryTrend("China");
        assertEquals(countryTrendChina.size(), 546);
        SimpleDateFormat a = new SimpleDateFormat("MM/dd/yyyy");
        Date firstDate = a.parse("1/22/2020");
        assertEquals(countryTrendChina.get(0).getDate(), firstDate);
        assertEquals(countryTrendChina.get(0).getCountry(), "China");
    }

    @Test
    public void getTrendInvalidCountryName() {
        List<DailyStatistics> countryTrend = csvFileOperator.getCountryTrend("AAA");
        assertEquals(countryTrend.size(), 0);
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
        assertEquals(82964, stat5_11.getCumulativeInfected());
        assertEquals(4633, stat5_11.getCumulativeDeath());
    }

    @Test
    public void getSingleData() throws ParseException {
        SimpleDateFormat a = new SimpleDateFormat("MM/dd/yyyy");
        Date start = a.parse("5/11/2021");
        DailyStatistics day = csvFileOperator.getCountryDataOn(start, "Hong Kong");
        assertEquals("Hong Kong", day.getCountry());
        assertEquals(11812, day.getCumulativeInfected());
        assertEquals(210, day.getCumulativeDeath());
        assertEquals(1575.566, day.getInfectedPerMillion(), 0.001);
        assertEquals(28.011, day.getDeathPerMillion(), 0.001);
        assertEquals(704635, day.getCumulativeVaccinated());
        assertEquals(9.4, day.getVaccinationRate(), 0.1);
    }

    @Test
    public void getCountries() {
        List<String> a = csvFileOperator.getAllCountries();
        for (String s:
             a) {
            System.out.println(s);
        }
    }

}


