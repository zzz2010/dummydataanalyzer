import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

import weka.core.converters.CSVLoader;


public class Tab2Arff {

	public static void convert(String tabFile, String arffFile)
	{


 
        String line;
        String csvfile=tabFile+".csv";
        PrintWriter pw;
		try {

	        /**
	         * Creating a buffered reader to read the file
	         */
	        BufferedReader bReader = new BufferedReader(
	                new FileReader(tabFile));
			pw = new PrintWriter(new File(csvfile));
			
			  String headerline=bReader.readLine();
		        String catLine=bReader.readLine();
		        /**
		         * Looping the read block until all lines in the file are read.
		         */
		       
		        while ((line = bReader.readLine()) != null) {
		 
		        	pw.println(line.trim().replace("\t", ","));
		        }
		        bReader.close();
		        pw.close();
				 CSVLoader loader = new CSVLoader();
				
				 loader.setSource(new File(csvfile));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
      
		 
	}
}
