import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import cern.colt.matrix.DoubleMatrix2D;

import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.RemoveWithValues;


public class common {

	static String outputDir="output/";
	
	
	//get instance with common IDs
	public static List<Instances> getCommonInstances(List<Instances> datasets)
	{
		
		List<HashSet<String>> IdSets=new ArrayList<HashSet<String>>();
		for (int i = 0; i < datasets.size(); i++) {
			HashSet<String> idset = new HashSet<String>();
			for (int j = 0; j < datasets.get(i).numInstances(); j++) {
				String id = datasets.get(i).instance(j).stringValue(0);
				idset.add(id);
			}
			IdSets.add(idset);
		}
		HashSet<String> commonIDs=new HashSet<String>(IdSets.get(0));
		for (int i = 1; i < IdSets.size(); i++) {
			commonIDs.retainAll(IdSets.get(i));
		}
		List<Instances> filterData=new ArrayList<Instances>();
		RemoveWithValues remove=new RemoveWithValues();
		for (int i = 0; i < datasets.size(); i++) {
			try {
				remove.setInputFormat(datasets.get(i));
				remove.setAttributeIndex("1");
				ArrayList<Integer> uniqueIdList=new ArrayList<Integer>();
				for (int j = 0; j < datasets.get(i).numInstances(); j++) {
					String id=datasets.get(i).instance(j).stringValue(0);
					if(!commonIDs.contains(id))
					{
						uniqueIdList.add((int) datasets.get(i).instance(j).value(0));
					}
				}
				Instances filteredInts=null ;
				if(uniqueIdList.size()>0)
				{
					int[] filterIds=new int[uniqueIdList.size()];
					for (int j = 0; j < filterIds.length; j++) {
						filterIds[j]=uniqueIdList.get(j);
					}
					
					remove.setNominalIndicesArr(filterIds);
					 filteredInts = Filter.useFilter(datasets.get(i), remove);
				}
				else//no need to filter
					filteredInts=new Instances(datasets.get(i));
				filterData.add(filteredInts);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		return filterData;
	}
	
	
	public static List<Instances> ShuffleValues(List<Instances> datasets)
	{
		List<Instances> shuffled=new ArrayList<Instances>(datasets.size());
		//initialize
		for (Instances instances : datasets) {
			ArrayList<ArrayList<Double>> matrix=new ArrayList<ArrayList<Double>>(instances.numAttributes());
			for (int i = 0; i < instances.numAttributes(); i++) {
				//the first one is id column, never assign value
				matrix.add(new ArrayList<Double>(instances.numInstances()));
			}
			
			//assign value
			for (int i = 0; i < instances.numInstances(); i++) {
				Instance inst = instances.instance(i);
				for (int j = 1; j <instances.numAttributes(); j++) {
					matrix.get(j).add(inst.value(j));
				}
			}
			//shuffle values
			for (int i = 0; i < instances.numAttributes()-1; i++) {
				Collections.shuffle(matrix.get(i),new Random());
			}
			
			//create new instances
			Instances shuffledInts=new Instances(instances);
			for (int i = 0; i < instances.numInstances(); i++) {
				Instance inst = shuffledInts.instance(i);
				for (int j = 1; j <instances.numAttributes(); j++) {
					inst.setValue(j, matrix.get(j).get(i));
				}
			}
			shuffled.add(shuffledInts);
		}
		return shuffled;
	}
	
	
}
