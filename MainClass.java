package covid19;

import java.time.Instant;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;




public class MainClass {


		/**
		 * This method enables us to get the two countries with the highest number of cases. 
		 * This is done using a linear search approach.
		 * @param countries An array list of country objects.
		 * @return Returns An array list of countries with the country with the highest number of cases in first position and the second in second position.
		 */
		public static ArrayList<Country> getHighestTwoCases(ArrayList<Country> countries){
			ArrayList<Country> temp = new ArrayList<Country>();
			Country max = countries.get(0);
			for(Country c:countries) {
				if((c.getNumCovidCases() > max.getNumCovidCases()) && (!c.getCountryName().equals("Cases on an international conveyance Japan"))) {
					max = c;
				}
			}
			Country secondMax = countries.get(0);
			if(secondMax.equals(max)){
				secondMax = countries.get(1);
			}
			for(Country c:countries) {
				if((c.getNumCovidCases() > secondMax.getNumCovidCases()) && !c.equals(max) && (!c.getCountryName().equals("Cases on an international conveyance Japan"))) {
					secondMax = c;
				}
			}
			temp.add(max);
			temp.add(secondMax);
			return temp;
		}
		
		
		/**
		 * This method finds the country with the highest infection rate, by using a linear search approach.
		 * @param c An array list of country objects
		 * @return The country with the highest infection rate. 
		 */
		public static Country getHighestInfectionRate(ArrayList<Country> c) {
			if(c == null) {
				return null;
			}
			Country currentMax = c.get(0);
			if(currentMax.getCountryName().equals("Cases on an international conveyance Japan")) {
				currentMax = c.get(1);
			}
			for(Country country:c) {
				if((country.getInfectionRate() > currentMax.getInfectionRate()) && (!country.getCountryName().equals("Cases on an international conveyance Japan"))) {
					currentMax = country;
				}
			}
			return currentMax;
		}
		

		/**
		 * This method helps us obtain the country with the highest death rate. This is done using a linear search approach.
		 * @param c An arrayList of countries
		 * @return The country with the highest death rate.
		 */
		public static Country getHighestDeathRate(ArrayList<Country> c) {
			if(c == null) {
				return null;
			}
			Country currentMax = c.get(0);
			if((currentMax.getCountryName().equals("Cases on an international conveyance Japan"))) {
				currentMax = c.get(1);
			}
			for(Country country:c) {
				if((country.getDeathRate() > currentMax.getDeathRate()) && (!country.getCountryName().equals("Cases on an international conveyance Japan"))) {
					currentMax = country;
				}
			}
			return currentMax;
		}
		
		/**
		 * This method helps us to calculate the correlation coefficient for the past week of data for a country, to determine
		 * if the trend in new cases reported is increasing or decreasing. 
		 * @param c A country object
		 * @return A real number that represents the correlation coefficient. 
		 */
		public static float correlationCoefficient(Country c) {
			int n = 0;
			int[] X = null;
			int[] Y = null;
			if(c.getCovidCase().size() >= 7) {
				n = 7;
            	int[] X1 = {1,2,3,4,5,6,7};
            	int[] Y1 = new int[7];
            	for(int i = 6; i >= 0; i--) {
            		Y1[i] = c.getCovidCase().get(i).getNumCases();
            	}
            	X = X1;
            	Y = Y1;
        	}
        	else if(c.getCovidCase().size() > 0) {
        		n = c.getCovidCase().size();
        		int[] X1 = new int[c.getCovidCase().size()];
            	int[] Y1 = new int[c.getCovidCase().size()];
            	for(int i = c.getCovidCase().size() - 1; i >= 0; i--) {
            		X1[i] = c.getCovidCase().get(i).getNumCases();
            		Y1[i] = c.getCovidCase().size() - i;
            	}
            	X = X1;
            	Y = Y1;
            	
        	}
			
				int sumof_X = 0, sumof_Y = 0, sumof_XY = 0; 
				int squareSumof_X = 0, squareSumof_Y = 0; 
				
				for (int i = 0; i < n; i++) 
				{ 
					sumof_X = sumof_X + X[i];  
					sumof_Y = sumof_Y + Y[i]; 
					sumof_XY = sumof_XY + X[i] * Y[i]; 
					squareSumof_X = squareSumof_X + X[i] * X[i]; 
					squareSumof_Y = squareSumof_Y + Y[i] * Y[i]; 
				} 
					
				// use formula for calculating correlation coefficient. 
				// 
				float corrCoeff = (float)(n * sumof_XY - sumof_X * sumof_Y)/ (float)(Math.sqrt((n * squareSumof_X - sumof_X * sumof_X) * (n * squareSumof_Y -  sumof_Y * sumof_Y))); 
				return corrCoeff; 
				} 

		
		/**
		 * This method allows us to determine the country with the highest rate of increase of the new cases per day.
		 * @param countryList A list of countries. For efficiency, this list is restricted to the list of countries with increasing new cases per day.
		 * @return The country object that represents the country with the steepest increase for the cases of corona virus
		 */
		public static Country getFastestIncreasingCountry(ArrayList<Country> countryList) {
			if (countryList.size() == 0) {
				return null;
			}
			
			Country temp = countryList.get(0);
			float t = correlationCoefficient(temp);
			for(Country c:countryList) {
				if(correlationCoefficient(c) < t) {
					temp = c;
					t = correlationCoefficient(c);
				}
			}
			return temp;
			
		}
		
		/**
		 * This method allows us to determine the country with the highest rate of increase of the new cases per day.
		 * @param countryList A list of countries. For efficiency, this list is restricted to the list of countries with increasing new cases per day.
		 * @return The country object that represents the country with the steepest increase for the cases of corona virus
		 */
		public static Country getFastestDecreasingCountry(ArrayList<Country> countryList) {
			if (countryList.size() == 0) {
				return null;
			}
			
			Country temp = countryList.get(0);
			float t = correlationCoefficient(temp);
			for(Country c:countryList) {
				if(correlationCoefficient(c) > t) {
					temp = c;
					t = correlationCoefficient(c);
				}
			}
			return temp;
		}
	
		/**
		 * This method allows us to determine the country that peaked the earliest
		 * @param An array list of countries
		 * @return A country object that represents the country with the highest peak. 
		 */
		public static Country getFirstPeak(ArrayList<Country> c) {
			// Obtaining the peak days
			String[] peakDays = new String[c.size()];
            for(int i = 0; i < c.size(); i++) {
            	peakDays[i] = c.get(i).getPeak().getDate();
            }
            // Determining the smallest peak
            int currentPeak = 0;
            String[] cPeakDate = peakDays[currentPeak].replace("\"", "").split("/");
            for(int i = 0; i < peakDays.length; i ++) {
            	String tempPeak = peakDays[i];
            	String[] temp = tempPeak.replace("\"", "").split("/");
            	if(Integer.parseInt(temp[2]) < Integer.parseInt(cPeakDate[2])) {
            		cPeakDate = temp;
            		currentPeak = i;
            	}
            	else if (Integer.parseInt(temp[1]) < Integer.parseInt(cPeakDate[1])) {
            		cPeakDate = temp;
            		currentPeak = i;
            	}
            	else if (Integer.parseInt(temp[0]) < Integer.parseInt(cPeakDate[0])) {
            		cPeakDate = temp;
            		currentPeak = i;
            	}
            	
            }
            return c.get(currentPeak);
		}
		
		/**
		 * Writes a solution to the output text file
		 * @param sol The solution to the written to a text file.
		 * @param fileName The file to which the solutions have to be written. 
		 */
		public static void writeSolution(String sol, String fileName) {
			try{    
                FileWriter fileWrite = new FileWriter(fileName, true);    
                fileWrite.write(sol + "\n");    
                fileWrite.close();    
               }catch(Exception e){
            	   e.printStackTrace();
            }    
		}
	
		
	public static void main(String[] args) {
		
		if(args.length < 2) {
			System.out.println("Please you are supposed to enter two files.");
			System.exit(0);
		}
		
		String file1 = args[0];
		String file2 = args[1];
		String file3 = file1.substring(0, file1.length()-4);
		String outputFileName = "task1_solution-"+ file3 +".txt";
		
		
		
		//Clearing the text file
		try{    
            FileWriter fileWrite = new FileWriter(outputFileName);    
            fileWrite.write("");    
            fileWrite.close();    
           }catch(Exception e){
        	   e.printStackTrace();
        } 
		
		Long start = Instant.now().toEpochMilli();

		// Inserting the countries into a countries Hashmap
		
		String filename = file2;
        Hashtable<String,Country> countries = new Hashtable<>();
      
        
            try{
                Scanner scan = new Scanner(new BufferedReader(new FileReader(filename)));
                scan.nextLine();
                while(scan.hasNextLine()){
                    String[] data = scan.nextLine().split(",");
                    try {
                    countries.put(data[0],new Country(data[0].replace("\"", ""),Integer.parseInt(data[2])));
                    
                    }catch(NumberFormatException e) {
                    
                    }
                }   
                scan.close();
            }catch(FileNotFoundException e){
                System.out.println("Please ensure that the names of the files are properly typed.");
                System.exit(0);
	
            }
            
            
            
            // Inserting the international cases. The cases are treated as a country. We give this country a default population 
            // of 100000000.
            
            //countries.put("JPG11668", new Country("Cases on an international conveyance Japan", 100000000));
            countries.put("Cases on an international conveyance Japan", new Country("Cases on an international conveyance Japan", 100000000));

            // Inserting the cases into the given countries
            
            String newFilename = file1;
           
            
            try{
                Scanner scanner = new Scanner(new BufferedReader(new FileReader(newFilename)));
                scanner.nextLine();
                while(scanner.hasNextLine()){
                    String[] data = scanner.nextLine().split(",");
		    try{	
                    Country temp = countries.get(data[1]);            
                    temp.addCovidCase(data[0], Integer.parseInt(data[2]));
     
                    temp.addNumDeaths(Integer.parseInt(data[3]));
                    
		   } catch(NullPointerException e){
			}	
                    }
              
            }catch(FileNotFoundException e) {
            	System.out.println("Please ensure that the names of the files are properly typed.");
            	System.exit(0);
            }
       
	
            //  Answering the questions
            ArrayList<Country> countryList =  new ArrayList<Country>();
            for(String key:countries.keySet()) {
            	countryList.add(countries.get(key));
            }
     
           // Highest number of recorded cases
            ArrayList<Country> question1 = getHighestTwoCases(countryList);
            Country questionA = question1.get(0);
            Country questionB = question1.get(1);
            writeSolution("A. The country with the highest cases: " + questionA.getCountryName() + " Number of cases is:  " + questionA.getNumCovidCases(), outputFileName);
            writeSolution("B. The country with the second highest cases: " + questionB.getCountryName() + " Number of cases is:  " + questionB.getNumCovidCases(), outputFileName);
            
           // Highest infection rate
            Country questionC = getHighestInfectionRate(countryList);
            writeSolution("C. The country with the highest infection rate: " + questionC.getCountryName() + " Infection rate:  " + questionC.getInfectionRate(), outputFileName);
           
           
         // Total death rate from Covid19.
            int totalInfected = 0;
            int totalDead = 0;
            for(Country c:countryList) {
            	if(!(c.getCountryName().equals("Cases on an international conveyance Japan"))) {
	            	totalInfected += c.getNumCovidCases();
	            	totalDead += c.getNumDeath();
            	}
            }
            
          
            double worldDeathRate = (float) totalDead / (float) totalInfected;
            writeSolution("D. The death rate for the world is: " + worldDeathRate, outputFileName);
            
           // Highest death rate
            Country questionD = getHighestDeathRate(countryList);
            writeSolution("E. The country with the highest death rate: " + questionD.getCountryName() + " Death rate:  " + questionD.getDeathRate(), outputFileName);
            

            // Determining the countries whose infection rate is on the rise.
            ArrayList<Country> newInfectionsRising = new ArrayList<Country>();
            for(Country c:countryList) {
            	if((correlationCoefficient(c) < 0) && (!(c.getCountryName().equals("Cases on an international conveyance Japan")))) {
            		newInfectionsRising.add(c);
            	}
            }
            
            // Printing out the countries with rising cases
            StringBuilder temp = new StringBuilder();
            if(newInfectionsRising.size() > 0) {
	            
	            for(Country c:newInfectionsRising) {
	            	if(newInfectionsRising.indexOf(c) != newInfectionsRising.size() - 1) {
	            	temp.append(c.getCountryName());
	            	temp.append(" , ");
	            	}
	            	else
	            		temp.append(c.getCountryName());
	            }
            }
            else {
            	temp.append("There are no countries in which the new infections per day is increasing.");
            }
            writeSolution("F. " + temp, outputFileName);
            
         // Determining the country with the highest rate of increase of new cases per day
            Country highestIncrease = getFastestIncreasingCountry(newInfectionsRising);
            String temp1 = "";
            if(highestIncrease != null) {
            	temp1 = "The country with the highest rate of increase is " + highestIncrease.getCountryName();
            }
            else {
            	temp1 = "There were no countries with an increasing rate of new infections per week over the last week";
            }
            writeSolution("G. " + temp1, outputFileName);
            
            // Determining the countries whose infection rate is falling
            ArrayList<Country> newInfectionsDroping = new ArrayList<Country>();
            for(Country c:countryList) {
            	if((correlationCoefficient(c) > 0) && !(c.getCountryName().equals("Cases on an international conveyance Japan"))) {
            		newInfectionsDroping.add(c);
            	}

            }
            
            // Printing out the countries with falling cases
            StringBuilder temp2 = new StringBuilder();
            if(newInfectionsDroping.size() > 0) {
	            for(Country c:newInfectionsDroping) {
	            	if(newInfectionsDroping.indexOf(c) != newInfectionsDroping.size() - 1) {
	            		temp2.append(c.getCountryName());
	            		temp2.append(" , ");
	            	}
	            	else
	            		temp2.append(c.getCountryName());
	            }
            }
            else {
            	temp2.append("There are no countries in which the new infections per day is decreasing.");
            }
            writeSolution("H. " + temp2, outputFileName);
            
            
            // Determining the country with the highest rate of decrease of new cases per day
            Country highestDecrease = getFastestDecreasingCountry(newInfectionsDroping);
            temp1 = "";
            if(highestDecrease != null) {
            	temp1 = "The country with the highest rate of derease is " + highestDecrease.getCountryName();
            }
            else {
            	temp1 = "There were no countries with a decreasing rate of new infections per week over the last week";
            }
            writeSolution("I. " + temp1, outputFileName);

            // Determining the country to reach the peak the first
            Country earliestPeak = getFirstPeak(newInfectionsDroping);
            writeSolution("J. The country with the earliest peak is: " + earliestPeak.getCountryName() + " which occured on " + earliestPeak.getPeak().getDate(), outputFileName);
	
            Long end = Instant.now().toEpochMilli();
            System.out.println(end-start);
	}

}
