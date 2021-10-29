package comp3111.covid.Core;

import com.opencsv.bean.CsvToBeanBuilder;

import javax.sound.sampled.DataLine;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.List;
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

    public HashSet<List<DailyStatistics>> getCountryTrendSet(List<String> countryList) {
        HashSet<List<DailyStatistics>> result = new HashSet<>();
        for (String countryName: countryList) {
            result.add(getCountryTrend(countryName));
        }
        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        CSVFileOperator fileOperator = new CSVFileOperator("COVID_Dataset_v1.0.csv");
        List<DailyStatistics> countryTrend = fileOperator.getCountryTrend("United States");
        countryTrend.forEach(System.out::println);
    }
}
