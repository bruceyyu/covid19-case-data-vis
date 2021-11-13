package comp3111.covid.core;

import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;
import org.apache.xpath.operations.Bool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ChartSetter {
    /**
     * Update a chart
     * @param chart chart
     * @param newCountryTrendMap new data map
     */
    public static void updateGraph(LineChart<Number, Number> chart, Map<String, List<DailyStatistics>> newCountryTrendMap) {
        ObservableList<XYChart.Series<Number, Number>> chartDataSeriesList = chart.getData();
        Map<String, Boolean> existMap = new HashMap<>();
        for (String countryName: newCountryTrendMap.keySet()) {
            existMap.put(countryName, true);
        }
        
        int graphInternalSize = chartDataSeriesList.size();
        List<XYChart.Series<Number, Number>> toRemoveList = new ArrayList<>();
        for (int i = 0; i < graphInternalSize; i++) {
            XYChart.Series<Number, Number> series = chartDataSeriesList.get(i);
            if (existMap.getOrDefault(series.getName(), false)) {
                // exist, update data
                series.getData().clear();

                    for (DailyStatistics ds : newCountryTrendMap.get(series.getName())
                    ) {
                        series.getData().add(new XYChart.Data(ds.getDate().getTime(), ds.getDeathPerMillion()));
                    }
                    existMap.put(series.getName(), false); // mark as processed


            } else {
                toRemoveList.add(series);

            }
        }
        chartDataSeriesList.removeAll(toRemoveList);
        for (String countryName: existMap.keySet()) {
            if (existMap.get(countryName)) {
                XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
                series1.setName(countryName);
                for (DailyStatistics dailyStatistics : newCountryTrendMap.get(countryName)) {
                    series1.getData().add(new XYChart.Data<>(dailyStatistics.getDate().getTime(), dailyStatistics.getDeathPerMillion()));
                }
                if (chartDataSeriesList.size() == 0) {
                    chartDataSeriesList.add(series1);
                } else {
                    for (int i = 0; i <= chartDataSeriesList.size(); i++) {
                        if (i == chartDataSeriesList.size()) {
                            chartDataSeriesList.add(i, series1);
                            break;
                        }
                        if (countryName.compareTo(chartDataSeriesList.get(i).getName()) < 0) {
                            chartDataSeriesList.add(i, series1);
                            break;
                        }

                    }
                }

            }
        }
    }

    /**
     * Set some default properties of a line chart
     * @param chart line chart
     * @param chartStartDatePicker start date
     * @param chartEndDatePicker end date
     */
    public static void setGraphPropeties(LineChart<Number, Number> chart, DatePicker chartStartDatePicker, DatePicker chartEndDatePicker) {
        NumberAxis chartX = (NumberAxis) chart.getXAxis();
        NumberAxis chartY = (NumberAxis) chart.getYAxis();
        chartX.setTickLabelFormatter(
                new StringConverter<Number>() {
                    @Override
                    public String toString(Number object) {
                        SimpleDateFormat a = new SimpleDateFormat("yy/MM/dd");
                        return a.format(new Date(object.longValue()));
                    }

                    @Override
                    public Number fromString(String string) {
                        return 0;
                    }
                }
        );
        chartX.setAutoRanging(false); // manually set X-axis range and tick width
        SimpleDateFormat a = new SimpleDateFormat("yyyy/MM/dd");
        try {
            chartX.setLowerBound(utils.localDateToDate(chartStartDatePicker.getValue()).getTime());
            chartX.setUpperBound(utils.localDateToDate(chartEndDatePicker.getValue()).getTime());
            chartX.setTickUnit(a.parse("1970/02/01").getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        chart.setCreateSymbols(true); // show the symbols
    }


}
