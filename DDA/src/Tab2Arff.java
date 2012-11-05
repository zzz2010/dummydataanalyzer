import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import weka.core.Instances;
import weka.core.converters.CSVLoader;


public class Tab2Arff {
	
	public static  Instances getInstances(String tabFile)
	{

		 Instances inst=null;

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
		       String[] comps=catLine.split("\t");
		       String C_str="1";
		       String N_str="";
		       String S_str="";
		       for(int i=1;i<comps.length;i++)
		       {
		    	   if(comps[i].equalsIgnoreCase("C"))
		    		   C_str+=","+(i+1);
		    	   else if(comps[i].equalsIgnoreCase("N"))
		    	   {
		    		   if(N_str=="")
		    			   N_str=String.valueOf(i+1);
		    		   else
		    			   N_str+=","+(i+1);
		    	   }
		    	   else
		    	   {
		    		   if(S_str=="")
		    			   S_str=String.valueOf(i+1);
		    		   else
		    			   S_str+=","+(i+1);   
		    	   }
		    	   
		       }
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
				 loader.setNominalAttributes(N_str);
				 loader.setStringAttributes(S_str);
				  inst=loader.getDataSet();
				 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

			return inst;	
	}

	public static void convert(String tabFile, String arffFile)
	{

		 Instances dataSet = getInstances( tabFile);
		 BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(arffFile));
			 writer.write(dataSet.toString());
			 writer.flush();
			 writer.close();  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
		 
	}
}
