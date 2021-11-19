package comp3111.covid.core.data;

import com.opencsv.bean.CsvToBeanBuilder;
import comp3111.covid.core.SortPolicyE;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class CSVFileOperator {
    List<DailyStatistics> dailyStatisticsList;
    List<String> allCountryList;
    List<DailyStatistics>  lastDayDataSet;
    private Map<Date, Map<String, DailyStatistics>> dateNameMapMap;
    private Date maximumDate;
    private Date minimumDate;

    /**
     * Constructor, specify file name
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
     * Extract all data of a specific country in a List sorted by Date
     * @param countryName country name
     * @return List of DailyStatistics of that country
     */
    private List<DailyStatistics> getCountryTrend(String countryName) {
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
        List<DailyStatistics> result = new ArrayList<>();
        return dateNameMapMap.keySet().stream()
                .filter(date -> (date.compareTo(start) >= 0) && date.before(end))
                .sorted()
                .map(date -> dateNameMapMap.get(date).get(countryName))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        //        return dailyStatisticsList.stream().filter(dailyStatistics -> dailyStatistics.getCountry().equals(countryName))
//                .filter(dailyStatistics -> )
//                .sorted((o1, o2) -> o1.getDate().compareTo(o2.getDate())).collect(Collectors.toList());
    }

    /**
     * Get country data on a specific day
     * @param date Date
     * @param countryName countryName
     * @return DailyStatistics of a country on that day
     */
    public DailyStatistics getCountryDataOn(Date date, String countryName) {
        return dateNameMapMap.get(date).getOrDefault(countryName, null);
    }

    /**
     * get set of country data on a specific day
     * @param date date
     * @param countryNames list of name of countries
     * @return set of dailyStatistics
     */
    public ArrayList<DailyStatistics> getCountryDataSetOn(Date date, List<String> countryNames) {
        ArrayList<DailyStatistics> resultSet = new ArrayList<>();
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
        return minimumDate;
    }

    /**
     * Get the latest date within the file
     * @return the maximum date
     */
    public Date getMaximumDate() {
        return maximumDate;
    }

//    /**
//     * get multiple countries' trend
//     * @param countryList List of string of country names
//     * @return a set of country trends
//     */
//    public HashSet<List<DailyStatistics>> getCountryTrendSet(List<String> countryList) {
//        HashSet<List<DailyStatistics>> result = new HashSet<>();
//        for (String countryName: countryList) {
//            result.add(getCountryTrend(countryName));
//        }
//        return result;
//    }
//
//    /**
//     * get multiple countries' trend in a specific period
//     * @param countryList List of string of country names
//     * @return a set of country trends
//     */
//    public HashSet<List<DailyStatistics>> getCountryTrendSet(List<String> countryList, Date start, Date end) {
//        HashSet<List<DailyStatistics>> result = new HashSet<>();
//        for (String countryName: countryList) {
//            result.add(getCountryTrend(countryName, start, end));
//        }
//        return result;
//    }

    /**
     * Get a map of country list
     * @param countryList String list of countries
     * @param start start date
     * @param end end date
     * @return map of country trend list
     */
    public Map<String, List<DailyStatistics>> getCountryTrendMap(List<String> countryList, Date start, Date end) {
        HashMap<String, List<DailyStatistics>> result = new HashMap<>();
        for (String countryName: countryList) {
            result.put(countryName, getCountryTrend(countryName, start, end));
        }
        return result;
    }

    public Map<String, List<DailyStatistics>> getCountryTrendMap_chartC(List<String> countryList, Date start, Date end) {
        HashMap<String, List<DailyStatistics>> result = new HashMap<>();
        Calendar c1 = Calendar.getInstance();
        c1.set(2020, 12 - 1, 30);
        Date date = c1.getTime();
        if(start.before(date)) {
        	start = date;
        }
        for (String countryName: countryList) {
            result.put(countryName, getCountryTrend(countryName, start, end));
        }
        return result;
    }


    public List<String> getAllCountries() {
        if (allCountryList == null) {
            HashSet<String> result = new HashSet<>();
            for (DailyStatistics ds:
                    dailyStatisticsList) {
                result.add(ds.getCountry());
            }
            allCountryList = result.stream().sorted().collect(Collectors.toList());
        }
        return allCountryList;

    }

    public List<String> searchCountry(String countryName) {
        List<String> allCountries = getAllCountries();
        return allCountries.stream().filter(name -> name.toLowerCase().startsWith(countryName.toLowerCase().trim())).collect(Collectors.toList());
    }

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
                            else if (o1.getPopulation() == null)
                                return -1;
                            else
                                return 1;
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
        }
        return null;
    }

    public static void main(String[] args) throws FileNotFoundException {
        CSVFileOperator fileOperator = new CSVFileOperator("COVID_Dataset_v1.0.csv");
        List<DailyStatistics> countryTrend = fileOperator.getCountryTrend("United States");
        countryTrend.forEach(System.out::println);
    }
}
