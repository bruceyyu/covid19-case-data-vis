package comp3111.covid.core.data;

import com.opencsv.bean.CsvToBeanBuilder;
import comp3111.covid.core.SortPolicyE;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * CSV File Operator class. An instance parses the CSV and contains
 * many useful operations related to the CSV.
 */
public class CSVFileOperator {
    /**
     * A list of all of DailyStatistics parsed form the CSV file.
     */
    List<DailyStatistics> dailyStatisticsList;
    List<String> allCountryList;
    List<DailyStatistics> lastDayDataSet;
    private Map<Date, Map<String, DailyStatistics>> dateNameMapMap;
    private Date maximumDate;
    private Date minimumDate;

    /**
     * Constructor, specify file name
     *
     * @param fileName fileName
     * @throws FileNotFoundException if the file cannot be found
     */
    public CSVFileOperator(String fileName) throws FileNotFoundException {
        dailyStatisticsList = new CsvToBeanBuilder(new FileReader(fileName))
                .withType(DailyStatistics.class).build().parse();
        dateNameMapMap = new HashMap<>();
        maximumDate = new Date(0);
        minimumDate = new Date();
        dailyStatisticsList.forEach(dailyStatistics -> {
            dateNameMapMap.putIfAbsent(dailyStatistics.getDate(), new HashMap<>());
            dateNameMapMap.get(dailyStatistics.getDate()).putIfAbsent(dailyStatistics.getCountry(), dailyStatistics);
            Date current = dailyStatistics.getDate();
            if (current.before(minimumDate))
                minimumDate = current;
            if (current.after(maximumDate))
                maximumDate = current;
        });
        getAllCountries();
        lastDayDataSet = new ArrayList<>(this.dateNameMapMap.get(getMaximumDate()).values());
    }

    /**
     * Get country trend of a country within a specific period
     *
     * @param countryName string country name
     * @param start       start date, inclusive
     * @param end         end date, not inclusive
     * @return list of DailyStatistics within a specific period
     */
    public List<DailyStatistics> getCountryTrend(String countryName, Date start, Date end) {
        return dateNameMapMap.keySet().stream()
                .filter(date -> (date.compareTo(start) >= 0) && date.before(end))
                .sorted()
                .map(date -> dateNameMapMap.get(date).get(countryName))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Get country data on a specific day
     *
     * @param date        Date
     * @param countryName countryName
     * @return DailyStatistics of a country on that day
     */
    public DailyStatistics getCountryDataOn(Date date, String countryName) {
        return dateNameMapMap.get(date).getOrDefault(countryName, null);
    }

    /**
     * get set of country data on a specific day
     *
     * @param date         date
     * @param countryNames list of name of countries
     * @return set of dailyStatistics
     */
    public ArrayList<DailyStatistics> getCountryDataSetOn(Date date, List<String> countryNames) {
        ArrayList<DailyStatistics> resultSet = new ArrayList<>();
        for (String name : countryNames) {
            DailyStatistics ds = getCountryDataOn(date, name);
            if (ds != null) {
                resultSet.add(ds);
            }
        }
        return resultSet;
    }

    /**
     * Get the minimum date within the file
     *
     * @return the minimum date
     */
    public Date getMinimumDate() {
        return minimumDate;
    }

    /**
     * Get the latest date within the file
     *
     * @return the maximum date
     */
    public Date getMaximumDate() {
        return maximumDate;
    }

    /**
     * Get a map of country list
     *
     * @param countryList String list of countries
     * @param start       start date
     * @param end         end date
     * @return map of country trend list
     */
    public Map<String, List<DailyStatistics>> getCountryTrendMap(List<String> countryList, Date start, Date end) {
        HashMap<String, List<DailyStatistics>> result = new HashMap<>();
        for (String countryName : countryList) {
            result.put(countryName, getCountryTrend(countryName, start, end));
        }
        return result;
    }

    public Map<String, List<DailyStatistics>> getCountryTrendMap_chartC(List<String> countryList, Date start, Date end) {
        HashMap<String, List<DailyStatistics>> result = new HashMap<>();
        Calendar c1 = Calendar.getInstance();
        c1.set(2020, 12 - 1, 30);
        Date date = c1.getTime();
        if (start.before(date)) {
            start = date;
        }
        for (String countryName : countryList) {
            result.put(countryName, getCountryTrend(countryName, start, end));
        }
        return result;
    }


    /**
     * Get all the countries available in the current file
     * @return List of all countries
     */
    public List<String> getAllCountries() {
        if (allCountryList == null) {
            HashSet<String> result = new HashSet<>();
            for (DailyStatistics ds :
                    dailyStatisticsList) {
                result.add(ds.getCountry());
            }
            allCountryList = result.stream().sorted().collect(Collectors.toList());
        }
        return allCountryList;
    }

    /**
     * Search for a specific country via name
     * @param countryName name of the country
     * @return list of country with the name
     */
    public List<String> searchCountry(String countryName) {
        List<String> allCountries = getAllCountries();
        return allCountries.stream().filter(name -> name.toLowerCase().startsWith(countryName.toLowerCase().trim())).collect(Collectors.toList());
    }

    /**
     * Search for a country name with a specific sorting policy. For all policies other than
     * "Alphabetical," we sort the available countries on the LAST day of the data file, and
     * return the result. Countries that do not have the policy data available will be thrown
     * to the end of the list in alphabetical order.
     * @param countryName name of country
     * @param policyE policy
     * @return list of country names
     */
    public List<String> searchCountry(String countryName, SortPolicyE policyE) {
        switch (policyE) {
            case NAME:
                return searchCountry(countryName);
            case POP:
                return lastDayDataSet.stream()
                        .filter(ds -> ds.getCountry().toLowerCase().startsWith(countryName.toLowerCase().trim()))
                        .sorted(Comparator.comparing(DailyStatistics::getCountry))
                        .sorted((o1, o2) -> {
                            if (o1.getPopulation() != null && o2.getPopulation() != null)
                                return o1.getPopulation().compareTo(o2.getPopulation());
                            else if (o1.getPopulation() == null && o2.getPopulation() == null)
                                return 0;
                            else if (o1.getPopulation() == null)
                                return 1;
                            else
                                return -1;
                        }).map(DailyStatistics::getCountry)
                        .collect(Collectors.toList());
            case POP_D:
                return lastDayDataSet.stream()
                        .filter(ds -> ds.getCountry().toLowerCase().startsWith(countryName.toLowerCase().trim()))
                        .sorted(Comparator.comparing(DailyStatistics::getCountry))
                        .sorted((o1, o2) -> {
                            if (o1.getPopulationDensity() != null && o2.getPopulationDensity() != null)
                                return o1.getPopulationDensity().compareTo(o2.getPopulationDensity());
                            else if (o1.getPopulationDensity() == null && o2.getPopulationDensity() == null)
                                return 0;
                            else if (o1.getPopulationDensity() == null)
                                return 1;
                            else
                                return -1;
                        }).map(DailyStatistics::getCountry)
                        .collect(Collectors.toList());
            case MED:
                return lastDayDataSet.stream()
                        .filter(ds -> ds.getCountry().toLowerCase().startsWith(countryName.toLowerCase().trim()))
                        .sorted(Comparator.comparing(DailyStatistics::getCountry))
                        .sorted((o1, o2) -> {
                            if (o1.getMedianAge() != null && o2.getMedianAge() != null)
                                return o1.getMedianAge().compareTo(o2.getMedianAge());
                            else if (o1.getMedianAge() == null && o2.getMedianAge() == null)
                                return 0;
                            else if (o1.getMedianAge() == null)
                                return 1;
                            else
                                return -1;
                        }).map(DailyStatistics::getCountry)
                        .collect(Collectors.toList());
            case GDP:
                return lastDayDataSet.stream()
                        .filter(ds -> ds.getCountry().toLowerCase().startsWith(countryName.toLowerCase().trim()))
                        .sorted(Comparator.comparing(DailyStatistics::getCountry))
                        .sorted((o1, o2) -> {
                            if (o1.getGdp() != null && o2.getGdp() != null)
                                return o1.getGdp().compareTo(o2.getGdp());
                            else if (o1.getGdp() == null && o2.getGdp() == null)
                                return 0;
                            else if (o1.getGdp() == null)
                                return 1;
                            else
                                return -1;
                        }).map(DailyStatistics::getCountry)
                        .collect(Collectors.toList());
        }
        return null;
    }
}
