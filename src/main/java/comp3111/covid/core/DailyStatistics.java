package comp3111.covid.core;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import com.opencsv.bean.CsvDate;

import java.util.Date;

/**
 * This class is an internal representation of an entry in the Excel sheet.
 */
public class DailyStatistics {
    @CsvBindByName(column = "location", required = true)
    private String country;

    @CsvCustomBindByName(column = "date", required = true, converter=CustomDateConverter.class)
    private Date date;

    @CsvBindByName(column = "total_deaths_per_million")
    private double deathPerMillion;

    @CsvBindByName(column = "total_deaths")
    private int cumulativeDeath;

    @CsvBindByName(column = "total_cases_per_million")
    private double infectedPerMillion;

    @CsvBindByName(column = "total_cases")
    private int cumulativeInfected;

    @CsvBindByName(column = "people_fully_vaccinated_per_hundred\n")
    private double vaccinationRate;

    @CsvBindByName(column = "people_fully_vaccinated")
    private int cumulativeVaccinated;


    public DailyStatistics() {
    }

    /**
     * Default constructor
     *
     * @param country              country name
     * @param date                 date
     * @param deathPerMillion      death per million
     * @param cumulativeDeath      cumulative death
     * @param infectedPerMillion   infected per million
     * @param cumulativeInfected   cumulative infected
     * @param vaccinationRate      vaccination rate
     * @param cumulativeVaccinated cumulative vaccinated
     */
    public DailyStatistics(String country, Date date, double deathPerMillion, int cumulativeDeath,
                           double infectedPerMillion, int cumulativeInfected, double vaccinationRate,
                           int cumulativeVaccinated) {
        this.country = country;
        this.date = date;
        this.deathPerMillion = deathPerMillion;
        this.cumulativeDeath = cumulativeDeath;
        this.infectedPerMillion = infectedPerMillion;
        this.cumulativeInfected = cumulativeInfected;
        this.vaccinationRate = vaccinationRate;
        this.cumulativeVaccinated = cumulativeVaccinated;
    }

    public Date getDate() {
        return date;
    }

    public double getDeathPerMillion() {
        return deathPerMillion;
    }

    public double getInfectedPerMillion() {
        return infectedPerMillion;
    }

    public double getVaccinationRate() {
        return vaccinationRate;
    }

    public int getCumulativeDeath() {
        return cumulativeDeath;
    }

    public int getCumulativeInfected() {
        return cumulativeInfected;
    }

    public int getCumulativeVaccinated() {
        return cumulativeVaccinated;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return "DailyStatistics{" +
                "country='" + country + '\'' +
                ", date=" + date +
                ", deathPerMillion=" + deathPerMillion +
                ", cumulativeDeath=" + cumulativeDeath +
                ", infectedPerMillion=" + infectedPerMillion +
                ", cumulativeInfected=" + cumulativeInfected +
                ", vaccinationRate=" + vaccinationRate +
                ", cumulativeVaccinated=" + cumulativeVaccinated +
                '}';
    }
}
