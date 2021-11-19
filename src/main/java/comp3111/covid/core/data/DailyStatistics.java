package comp3111.covid.core.data;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;

import java.math.BigInteger;
import java.util.Date;

/**
 * This class is an internal representation of an entry in the Excel sheet.
 */
public class DailyStatistics {
    @CsvBindByName(column = "location", required = true)
    private String country;

    @CsvCustomBindByName(column = "date", required = true, converter= CustomDateConverter.class)
    private Date date;

    @CsvBindByName(column = "total_deaths_per_million")
    private double deathPerMillion;

    @CsvBindByName(column = "total_deaths")
    private BigInteger cumulativeDeath;

    @CsvBindByName(column = "total_cases_per_million")
    private double infectedPerMillion;

    @CsvBindByName(column = "total_cases")
    private BigInteger cumulativeInfected;

    @CsvBindByName(column = "people_fully_vaccinated_per_hundred\n")
    private double vaccinationRate;

    @CsvBindByName(column = "people_fully_vaccinated")
    private BigInteger cumulativeVaccinated;

    @CsvBindByName(column = "population")
    private BigInteger population;

    @CsvBindByName(column = "population_density")
    private Double populationDensity;

    @CsvBindByName(column = "median_age")
    private Double medianAge;

    @CsvBindByName(column = "gdp_per_capita")
    private Double gdp;

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
    public DailyStatistics(String country, Date date, double deathPerMillion, BigInteger cumulativeDeath,
                           double infectedPerMillion, BigInteger cumulativeInfected, double vaccinationRate,
                           BigInteger cumulativeVaccinated, BigInteger population, Double populationDensity) {
        this.country = country;
        this.date = date;
        this.deathPerMillion = deathPerMillion;
        this.cumulativeDeath = cumulativeDeath;
        this.infectedPerMillion = infectedPerMillion;
        this.cumulativeInfected = cumulativeInfected;
        this.vaccinationRate = vaccinationRate;
        this.cumulativeVaccinated = cumulativeVaccinated;
        this.population = population;
        this.populationDensity = populationDensity;
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

    public BigInteger getCumulativeDeath() {
        return cumulativeDeath;
    }

    public BigInteger getCumulativeInfected() {
        return cumulativeInfected;
    }

    public BigInteger getCumulativeVaccinated() {
        return cumulativeVaccinated;
    }

    public String getCountry() {
        return country;
    }

    public BigInteger getPopulation() {
        return population;
    }

    public Double getPopulationDensity() {
        return populationDensity;
    }

    public Double getMedianAge() {
        return medianAge;
    }

    public Double getGdp() {
        return gdp;
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
                ", population=" + population +
                ", populationDensity=" + populationDensity +
                ", medianAge=" + medianAge +
                ", gdp=" + gdp +
                '}';
    }
}
