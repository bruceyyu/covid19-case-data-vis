package comp3111.covid.Core;

import com.opencsv.bean.CsvToBeanBuilder;

import javax.sound.sampled.DataLine;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CSVFileOperator {
    List<DailyStatistics> dailyStatisticsList;

    /**
     * Constructor, specify file name
     * @param fileName fileName
     * @throws FileNotFoundException if the file cannot be found
     */
    public CSVFileOperator(String fileName) throws FileNotFoundException {
        dailyStatisticsList = new CsvToBeanBuilder(new FileReader(fileName))
                .withType(DailyStatistics.class).build().parse();
    }

    /**
     * Extract all data of a specific country in a List sorted by Date
     * @param countryName country name
     * @return List of DailyStatistics of that country
     */
    public List<DailyStatistics> getCountryTrend(String countryName) {
        return dailyStatisticsList.stream().filter(dailyStatistics -> dailyStatistics.getCountry().equals(countryName))
                .sorted((o1, o2) -> o1.getDate().compareTo(o2.getDate())).collect(Collectors.toList());
    }

    /**
     * get country trend of a country within a specific period
     * @param countryName string country name
     * @param start start date, inclusive
     * @param end end date, not inclusive
     * @return list of DailyStatistics within a specific period
     */
    public List<DailyStatistics> getCountryTrend(String countryName, Date start, Date end) {
        return dailyStatisticsList.stream().filter(dailyStatistics -> dailyStatistics.getCountry().equals(countryName))
                .filter(dailyStatistics -> (dailyStatistics.getDate().compareTo(start) >= 0) && dailyStatistics.getDate().before(end))
                .sorted((o1, o2) -> o1.getDate().compareTo(o2.getDate())).collect(Collectors.toList());
    }

    /**
     * Get country data on a specific day
     * @param date Date
     * @param countryName countryName
     * @return DailyStatistics of a country on that day
     */
    public DailyStatistics getCountryDataOn(Date date, String countryName) {
        List<DailyStatistics> countryTrend = getCountryTrend(countryName);
        if (countryTrend.size() == 0) {
            return null;
        }
        for (DailyStatistics ds: countryTrend
             ) {
            if (ds.getDate().equals(date))
                return ds;
        }
        return null;
    }

    /**
     * get set of country data on a specific day
     * @param date date
     * @param countryNames list of name of countries
     * @return set of dailyStatistics
     */
    public Set<DailyStatistics> getCountryDataSetOn(Date date, List<String> countryNames) {
        Set<DailyStatistics> resultSet = new HashSet<>();
        for (String name: countryNames) {
            DailyStatistics ds = getCountryDataOn(date, name);
            if (ds != null) {
                resultSet.add(ds);
            }
        }
        return resultSet;
    }

    /**
     * Get the minimum date within the file
     * @return the minimum date
     */
    public Date getMinimumDate() {
        if (dailyStatisticsList.size() == 0) { // list not initialized
            return null;
        }

        Date earliest = dailyStatisticsList.get(0).getDate();
        for (DailyStatistics dailyStat: dailyStatisticsList
             ) {
            if (dailyStat.getDate().before(earliest))
                earliest = dailyStat.getDate();
        }
        return earliest;
    }

    /**
     * Get the latest date within the file
     * @return the maximum date
     */
    public Date getMaximumDate() {
        if (dailyStatisticsList.size() == 0) { // list not initialized
            return null;
        }

        Date latest = dailyStatisticsList.get(0).getDate();
        for (DailyStatistics dailyStat: dailyStatisticsList
        ) {
            if (dailyStat.getDate().after(latest)) {
                latest = dailyStat.getDate();
            }

        }
        return latest;
    }

    /**
     * get multiple countries' trend
     * @param countryList List of string of country names
     * @return a set of country trends
     */
    public HashSet<List<DailyStatistics>> getCountryTrendSet(List<String> countryList) {
        HashSet<List<DailyStatistics>> result = new HashSet<>();
        for (String countryName: countryList) {
            result.add(getCountryTrend(countryName));
        }
        return result;
    }

    /**
     * get multiple countries' trend in a specific period
     * @param countryList List of string of country names
     * @return a set of country trends
     */
    public HashSet<List<DailyStatistics>> getCountryTrendSet(List<String> countryList, Date start, Date end) {
        HashSet<List<DailyStatistics>> result = new HashSet<>();
        for (String countryName: countryList) {
            result.add(getCountryTrend(countryName, start, end));
        }
        return result;
    }






    /**
     * check whether a date is valid for a specific country
     * @param date date
     * @param countryName country name
     * @return if the date is valid or not
     */
    public boolean checkValidDate(Date date, String countryName) {
        List<DailyStatistics> countryTrend = getCountryTrend(countryName);
        for (DailyStatistics d: countryTrend
             ) {
            if (d.getDate().equals(date)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getAllCountries() {
        HashSet<String> result = new HashSet<>();
        for (DailyStatistics ds:
             dailyStatisticsList) {
            result.add(ds.getCountry());
        }
        return result.stream().sorted().collect(Collectors.toList());
    }

    public static void main(String[] args) throws FileNotFoundException {
        CSVFileOperator fileOperator = new CSVFileOperator("COVID_Dataset_v1.0.csv");
        List<DailyStatistics> countryTrend = fileOperator.getCountryTrend("United States");
        countryTrend.forEach(System.out::println);
    }
}
