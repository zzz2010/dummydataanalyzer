import java.io.File;
import java.util.HashMap;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import weka.core.Instances;


public class DDAmain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Options options = new Options();
		options.addOption("i", true, "data directory");
		CommandLineParser parser = new GnuParser();
		CommandLine cmd;
		String[] fileList=null;
		String inputdir="";
		HashMap<String,Instances> TablePool=new HashMap<String, Instances>();
		try {
			cmd = parser.parse( options, args);
			if(cmd.hasOption("i"))
			{
				 inputdir=cmd.getOptionValue("i")+"/";
				File dir = new File(inputdir);
				fileList = dir.list();
			}
			else
			{
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp( "DDAmain", options );
				return;
			}
			
		if(fileList==null)
			return;
		//loading data files
		for (int i = 0; i < fileList.length; i++) {
			String filename=inputdir+fileList[i];
			//tab separate file
			if(filename.endsWith("txt"))
			{
				Instances dataset=Tab2Arff.getInstanceNconvert(filename);
				TablePool.put(fileList[i], dataset);
			}
		}
		
		//apply single Feature Dummy
		
		
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
