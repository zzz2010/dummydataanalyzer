import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import bioweka.core.converters.sequence.FastaSequenceLoader;

import weka.clusterers.HierarchicalClusterer;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.RemoveUseless;


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
		ArrayList<String> TableNames=new ArrayList<String>();
		ArrayList<Instances> TableData=new ArrayList<Instances>();
	
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
		(new File(common.outputDir)).mkdirs();
		if(fileList==null)
			return;
		//loading data files
		for (int i = 0; i < fileList.length; i++) {
			String filename=inputdir+fileList[i];
			//tab separate file
			if(filename.endsWith("txt"))
			{
				Instances dataset=Tab2Arff.getInstanceNconvert(filename);
//				dataset.setClassIndex(0);//so skip remove
				RemoveUseless_ignoreID remove=new RemoveUseless_ignoreID();
				remove.setMaximumVariancePercentageAllowed(95);
				remove.setInputFormat(dataset);
				TableData.add(Filter.useFilter(dataset,remove));
				TableNames.add(fileList[i]);
			}
			if(filename.endsWith("fasta")||filename.endsWith("fa"))
			{
				FastaSequenceLoader loader=new FastaSequenceLoader();
				loader.setFile(new File(filename));
				  Instances inst = loader.getDataSet();
				  TableData.add(inst);
				  TableNames.add(fileList[i]);
			}
		}
		
		//apply single Feature Dummy
//		MissingValueHandler mvHandler=new MissingValueHandler(new SingleFeatureDummy());
		MissingValueHandler mvHandler=new MissingValueHandler(new HierarchicalClusteringDummy());
		//apply shuffleVerifier
		ShuffleVerifier verifier=new ShuffleVerifier(mvHandler);
		List<DummyFinding> Findings = verifier.DigKnowledge(TableData);
		for (DummyFinding dummyFinding : Findings) {
			if(dummyFinding.isCrossTable)
			System.out.println(dummyFinding.toString());
		}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
