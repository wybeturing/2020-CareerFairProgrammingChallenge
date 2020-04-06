package covid19;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Set;


/**
 *  This class contains information about each country, in the context of the Covid-19, giving us a picture of the number of
 * confirmed cases daily, among others. 
 * @author wybeturing
 *
 */

public class Country {
	/**
	 * This class contains each recorded case of covid-19. 
	 * @author wybeturing
	 *
	 */
	public static class CovidReport {
		
		private String date;
		private int numCases;
		
		/**
		 * Constructor for creating a single record of cases in a day.
		 * @param date The date on which the new cases have been confirmed.
		 * @param numCases The number of new cases confirmed on a given day.
		 */
		public CovidReport(String date, int numCases){
			this.date = date;
			this.numCases = numCases;
		}
		
		/**
		 * Gets the date on which the covid report was made.
		 * @return Returns the date on which the observation was made.
		 */
		public String getDate() {
			return this.date;
		}
		
		/**
		 * Gets the number of cases identified in a covid report
		 * @return Returns the number of cases as an integer. 
		 */
		public int getNumCases() {
			return this.numCases;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((date == null) ? 0 : date.hashCode());
			result = prime * result + numCases;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CovidReport other = (CovidReport) obj;
			if (date == null) {
				if (other.date != null)
					return false;
			} else if (!date.equals(other.date))
				return false;
			if (numCases != other.numCases)
				return false;
			return true;
		}
		
		
		
	}
	
	private String countryName; 
	private int population;
	private ArrayList <CovidReport> covidCase;
	private int numCovidCases;
	private double infectionRate;
	private int numDeath;
	private double deathRate;
	private CovidReport peak;
	
	/**
	 * Overloaded constructor. This constructor sets most of the attributes to zero as that is what they are supposed to be 
	 * in the initial stage. 
	 * @param name The name of the country, as a string. 
	 * @param population The population of the country, as an integer. 
	 */
	public Country(String name,int population) {
		this.countryName = name;
		this.population = population;
		this.covidCase = new ArrayList<>();
		this.numCovidCases = 0;
		this.numDeath = 0;
		this.infectionRate = 0.0;
		this.deathRate = 0.0;
	}
	
	public CovidReport getPeak() {
		return peak;
	}

	
	/**
	 * Gets the name of the country.
	 * @return The name of the country. 
	 */
	public String getCountryName() {
		return countryName;
	}

	/**
	 * Sets the name of the country to a desired name.
	 * @param countryName The name to be set, as a string. 
	 */
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	/**
	 * Gets the population of the country. 
	 * @return Returns the country's population as an integer. 
	 */
	public int getPopulation() {
		return population;
	}

	
	/***
	 * Sets the population of a country to a desired number.
	 * @param Returns the population as an integer. 
	 */
	public void setPopulation(int population) {
		this.population = population;
	}

	/**
	 * Gets the array list of all reports of covid-19 for the country in question.  
	 * @return An array list of CovidReports. 
	 */
	public ArrayList<CovidReport> getCovidCase() {
		return covidCase;
	}

	/**
	 * Gets the number of covid-19 cases reported in a given country. 
	 * @return An integer count of the covid-19 cases. 
	 */
	public int getNumCovidCases() {
		return numCovidCases;
	}

	/**
	 * Sets the number of covid-19 cases to a desired number.
	 * @param numCovidCases An integer representing the count of covid-19 cases up to this point. 
	 */
	public void setNumCovidCases(int numCovidCases) {
		this.numCovidCases = numCovidCases;
	}
	
	/**
	 * Gets the infection rate of the country in question (The number of cases, divided by the population).
	 * @return The infection rate as a real number. 
	 */
	public double getInfectionRate() {
		return this.infectionRate;
	}
	
	/**
	 * Gets the death rate of the country in question (The number of deaths divided by the number of infections.)
	 * @return The death rate as a real number.
	 */
	public double getDeathRate() {
		return this.deathRate;
	}
	
	/**
	 * Gets the number of deaths recorded from covid-19 in a country. 
	 * @return The number of deaths, as an integer. 
	 */
	public int getNumDeath() {
		return numDeath;
	}
	
	/**
	 * Permits us to add a new Covid-19 case , known in this context as a CovidReport.
	 * It is important to note that each time a new covid case is being added, we recalculate the infection rate and change the number
	 * of infections appropriately. 
	 * @param date The date on which the new cases are being observed, date is entered as a string, in the format dd/mm/yy.
	 * @param numCases The number of cases recorded on that day, as an integer.
	 */
	public void addCovidCase(String date, int numCases) {
		this.numCovidCases += numCases;
		this.infectionRate = (double)this.getNumCovidCases()/(double)this.getPopulation();
		this.getCovidCase().add(new CovidReport(date, numCases));
		if(this.peak == null) {
			this.peak = new CovidReport(date, numCases);
		}
		else {
			if(this.peak.getNumCases() < numCases) {
				this.peak = new CovidReport(date, numCases);
			}
		}
	}
	
	/**
	 * Permits the addition or recording of new deaths from Covid-19. Important to note that each time a new deat is recorded, there
	 * is a new calculation for the death rate and a proper change in the number of deaths.
	 * @param death
	 */
	public void addNumDeaths(int death) {
		this.numDeath += death;
		this.deathRate = (double)this.getNumDeath()/(double)this.getNumCovidCases();
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((countryName == null) ? 0 : countryName.hashCode());
		result = prime * result + ((covidCase == null) ? 0 : covidCase.hashCode());
		long temp;
		temp = Double.doubleToLongBits(deathRate);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(infectionRate);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + numCovidCases;
		result = prime * result + numDeath;
		result = prime * result + ((peak == null) ? 0 : peak.hashCode());
		result = prime * result + population;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Country other = (Country) obj;
		if (countryName == null) {
			if (other.countryName != null)
				return false;
		} else if (!countryName.equals(other.countryName))
			return false;
		if (covidCase == null) {
			if (other.covidCase != null)
				return false;
		} else if (!covidCase.equals(other.covidCase))
			return false;
		if (Double.doubleToLongBits(deathRate) != Double.doubleToLongBits(other.deathRate))
			return false;
		if (Double.doubleToLongBits(infectionRate) != Double.doubleToLongBits(other.infectionRate))
			return false;
		if (numCovidCases != other.numCovidCases)
			return false;
		if (numDeath != other.numDeath)
			return false;
		if (peak == null) {
			if (other.peak != null)
				return false;
		} else if (!peak.equals(other.peak))
			return false;
		if (population != other.population)
			return false;
		return true;
	}
	
	
	
}
