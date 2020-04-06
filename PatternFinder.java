package covid19;
import java.time.Instant;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.regex.*;

import covid19.Country.CovidReport;


public class PatternFinder {
	
	/**
	 * @author hussein
	 * @param countries
	 * @param patternToFind
	 * @return
	 * The checkForPattern check for the pattern of the time series
	 */
	public static String checkForPattern(Hashtable<String,Country>countries , String patternToFind) {
		
		String timeSeries = "";
		String information = "";
		Pattern p = Pattern.compile(patternToFind);
	/**
	 * This for loop concatenates the  time series of a country from the beginning to the end.
	 * And uses the Regex to find if there is a match. if yes, it returns the country and start date.
	 */
       for(String E : countries.keySet()) {
    	  for(CovidReport A: countries.get(E).getCovidCase()) {
    		  timeSeries+=A.getNumCases();
    		  timeSeries+="-";
    	  }
    	 Matcher matcher = p.matcher(timeSeries);
    	  if(matcher.find()) {
    		  information = countries.get(E).getCountryName();
    		  information += checkIndex(countries.get(E),timeSeries, patternToFind);    		
    	  }
    	  timeSeries = "";
       }
    
       return information;
	}
	
	
	/**
	 * 
	 * @param country
	 * @param timeSeries
	 * @param pattern
	 * @return
	 * The checkIndex return date of the starting date by finding a the portion of the 
	 * time series where it is located.
	 * This method locates the start of the pattern in the main time series.
	 */
	public static String checkIndex(Country country,String timeSeries, String pattern) {

		String [] time = timeSeries.split("-");
		String [] patt = pattern.split("-");
		String information = "";
	
		int begin = 0;
		while(begin<time.length) {
			if(ispattern(time, patt, begin)) {
				information+="\n"+country.getCovidCase().get(begin+patt.length-1).getDate();
				break;
			}
			begin++;
		}
		return information;
	}
	
	/**
	 * 
	 * @param time
	 * @param patt
	 * @param start
	 * @return boolean
	 * 
	 * The ispattern method finds exactly where the patt in located in the main 
	 * time series of a country.
	 * It returns true once it is found.
	 */
	public static boolean ispattern(String [] time,String[] patt, int start) {
		boolean pattern = false;
		for(int i = 0;i<patt.length;i++) {
			if(patt[i].equals(time[start+i])) {
				pattern = true;
			}else
				return false;
		}
		return pattern;
	}
	
	

// 		This is the main method

	public static void main(String[] args) {
		if(args.length < 2) {
			System.out.println("Please you are supposed to enter two files.");
			System.exit(0);

		}
		String fileToCovid = args[0];
		String pathToSeries = args[1];
		
		String outPutPath = pathToSeries.substring(0, pathToSeries.length()-4);
		outPutPath =  "task2_solution-" + outPutPath + ".txt";
		Long start = Instant.now().toEpochMilli();
		

		String patternToFind = "";
		
		// This Hastable hold the information of the countries with conoro virus case 
        Hashtable<String,Country>countries = new Hashtable<>();
        
        // The code below reads from the two data file
        // Inserting the cases into the given countries
            try{
                Scanner scan = new Scanner(new BufferedReader(new FileReader(fileToCovid)));
                scan.nextLine();
                while(scan.hasNextLine()){
                    String[] data = scan.nextLine().split(",");
                    
                    if(!countries.containsKey(data[4])) {
                    	countries.put(data[4],new Country(data[1],10));
                    	Country temp = countries.get(data[data.length - 1]);
                        temp.addCovidCase(data[0], Integer.parseInt(data[2]));
                        temp.addNumDeaths(Integer.parseInt(data[3]));
                    }else {
                    	Country temp = countries.get(data[data.length - 1]);
                        temp.addCovidCase(data[0], Integer.parseInt(data[2]));
                        temp.addNumDeaths(Integer.parseInt(data[3]));
                    }	
                }   
                scan.close();
            }catch(FileNotFoundException e){
               System.out.println("Please make sure that the name of the files are properly typed.");
               System.exit(0);
            }
            
//          loading the time series
            try{
                Scanner scanner = new Scanner(new BufferedReader(new FileReader(pathToSeries)));
                
                while(scanner.hasNextLine()){
                	String word = scanner.nextLine();
                	if(Pattern.matches("\\d*", word)) {
                		patternToFind += word;
                		if(scanner.hasNextLine())
                			patternToFind+="-";
                	}else {
                		for(int i = 0;i<word.length();i++) {
                			if(Pattern.matches("\\d*", word.subSequence(i, i+1))) {
                				patternToFind += word.subSequence(i, i+1);
                				if(scanner.hasNextLine())
                        			patternToFind+="-";
                			}               			
                		}
                	}	
                }
                scanner.close();
            }catch(FileNotFoundException e){
            	System.out.println("Please make sure that the name of the files are properly typed.");
            	System.exit(0);
            }
//            checkForPattern(countries, patternToFind);
           //Writing into the file
            try{    
                FileWriter fileWrite = new FileWriter(outPutPath);    
                fileWrite.write(checkForPattern(countries, patternToFind));    
                fileWrite.close();    
               }catch(Exception e){
            	   e.printStackTrace();
            }    
   
               Long end = Instant.now().toEpochMilli();
               System.out.println(end-start);
	}

}
