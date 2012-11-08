import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;

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
			
			HashMap<String,Integer> MissingValueStr=new HashMap<String,Integer>();
			MissingValueStr.put("?",0);
			MissingValueStr.put("NA",0);
			MissingValueStr.put("na",0);
			MissingValueStr.put("N/A",0);
			MissingValueStr.put("n/a",0);
			MissingValueStr.put("Missing",0);
			MissingValueStr.put("missing",0);
			
			  String headerline=bReader.readLine();
			 	pw.println(headerline.trim().replace('\t', ','));//
		        String catLine=bReader.readLine().trim();
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
		        	String[] toks=line.trim().split("\t");
		        	for(int i=0;i<toks.length;i++)
		        	{
		        		if(MissingValueStr.containsKey(toks[i]))
		        			MissingValueStr.put(toks[i], MissingValueStr.get(toks[i])+1);
		        	}
		        }
		        bReader.close();
		        pw.close();
				 CSVLoader loader = new CSVLoader();
				File csv=new File(csvfile);
				 loader.setSource(csv);
				 loader.setNominalAttributes(C_str);
				 loader.setStringAttributes(S_str);
				 
				 //missing values, find the largest occurrence as missing value placeholder
				 String placehoder="";
				 int misscount=0;
				 for(String temp : MissingValueStr.keySet())
				 {
					 int tmp=MissingValueStr.get(temp);
					 if(tmp>misscount)
					 {
						 misscount=tmp;
						 placehoder=temp;
					 }
				 }
				 if(placehoder!="")
					 loader.setMissingValue(placehoder);
				 
				 //get dataset
				  inst=loader.getDataSet();
				  
				  //delete tmp file
				  csv.delete();
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
	
	public static Instances getInstanceNconvert(String tabFile)
	{

		 Instances dataSet = getInstances( tabFile);
		 BufferedWriter writer;
		try {
			
			writer = new BufferedWriter(new FileWriter(common.outputDir+(new File(tabFile)).getName()+".arff"));
			 writer.write(dataSet.toString());
			 writer.flush();
			 writer.close();  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
		return dataSet;
		 
	}
}
