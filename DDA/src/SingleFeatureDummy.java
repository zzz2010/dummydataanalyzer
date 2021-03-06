import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;
import org.apache.commons.math3.stat.inference.TTest;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.jet.random.StudentT;
import cern.jet.random.engine.DRand;
import cern.jet.random.engine.RandomEngine;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.instance.RemoveWithValues;


public class SingleFeatureDummy implements AbstractDummy{



	@Override
	public List<DummyFinding> DigKnowledge(List<Instances> datasets) {
		// TODO Auto-generated method stub
		 List<DummyFinding> findings=new ArrayList<DummyFinding>();
		 for (int i = 0; i < datasets.size(); i++) {
			 List<DummyFinding> temp = processSingleTable(datasets.get(i));
			 if(temp!=null)
			 findings.addAll(temp);
		}
		 
		 for (int i = 0; i < datasets.size()-1; i++) {
			 for (int j = i+1; j < datasets.size(); j++) {
				 List<DummyFinding> temp =processTwoTables(datasets.get(i),datasets.get(j));
				 if(temp!=null)
					 findings.addAll(temp);
			}	 
		 }
		return findings;
	}
	
	public List<DummyFinding> processTwoTables(Instances table1,Instances table2)
	{
		List<DummyFinding> result=new ArrayList<DummyFinding>();
		//get common id
		List<Instances> inputData=new ArrayList<Instances>();
		inputData.add(table1);
		inputData.add(table2);
		List<Instances> filterData=common.getCommonInstances(inputData);
		Instances first=filterData.get(0);
		Instances second=filterData.get(1);
		second.deleteAttributeAt(0);
		Instances mergedDataSet = Instances.mergeInstances(first, second);
		
		Remove remove = new Remove();
		remove.setInvertSelection(true);
      for (int i = 1; i < first.numAttributes(); i++) {		
			for (int j = first.numAttributes(); j < mergedDataSet.numAttributes(); j++) {			
				try {
					remove.setAttributeIndicesArray(new int[]{i,j});
					remove.setInputFormat(mergedDataSet);
					Instances newData = Filter.useFilter(mergedDataSet, remove);
					boolean contanedNominal=(newData.attribute(0).isNominal()|newData.attribute(1).isNominal());
					if(contanedNominal)
					{
						//go for classification 
						Evaluation eval=doClassification(newData);
						if(eval.areaUnderROC(0)>Criteria.AUC) //dummy, only look at classIndex=0
						{
							DummyFinding finding=new DummyFinding();
							finding.confidence=eval.areaUnderROC(0);
							finding.pvalue=0;
							finding.support=newData.numInstances();
							finding.FeatureName.add(mergedDataSet.attribute(i).name());
							finding.FeatureName.add(mergedDataSet.attribute(j).name());
							finding.DummyName=this.getClass().getName();
							finding.description="J48 Classification";
							finding.isCrossTable=true;
							result.add(finding);
						}
					}
					else
					{
						boolean bothNumerical=(newData.attribute(0).isNumeric()|newData.attribute(1).isNumeric());
						if(bothNumerical)
						{
							//go for correlation
							DummyFinding finding=doCorrelation( newData);
							finding.isCrossTable=true;
							if(finding.pvalue<Criteria.Pvalue&&finding.confidence>Criteria.Corr)
							{
								result.add(finding);
							}
						}
						else
						{
							continue; 
//							boolean bothString=(newData.attribute(0).isString()|newData.attribute(1).isString());
//							if(bothString)
//							{
//								//go for string comparison
//							}
						}
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // apply filter
			}
		}
		return result;
		 
		
	}
	
	

	public List<DummyFinding> processSingleTable(Instances table)
	{
		List<DummyFinding> result=new ArrayList<DummyFinding>();
		Remove remove = new Remove();
		remove.setInvertSelection(true);
		
		for (int i = 1; i < table.numAttributes()-1; i++) {
			
			for (int j = i+1; j < table.numAttributes(); j++) {
				remove.setAttributeIndicesArray(new int[]{i,j});
				try {
					remove.setInputFormat(table);
					Instances newData = Filter.useFilter(table, remove);
					boolean contanedNominal=(newData.attribute(0).isNominal()|newData.attribute(1).isNominal());
					if(contanedNominal)
					{
						//go for classification 
						Evaluation eval=doClassification(newData);
						if(eval.areaUnderROC(0)>Criteria.AUC) //dummy, only look at classIndex=0
						{
							DummyFinding finding=new DummyFinding();
							finding.confidence=eval.areaUnderROC(0);
							finding.pvalue=0;
							finding.support=newData.numInstances();
							finding.FeatureName.add(table.attribute(i).name());
							finding.FeatureName.add(table.attribute(j).name());
							finding.DummyName=this.getClass().getName();
							finding.description="J48 Classification";
							result.add(finding);
						}
					}
					else
					{
						boolean bothNumerical=(newData.attribute(0).isNumeric()|newData.attribute(1).isNumeric());
						if(bothNumerical)
						{
							//go for correlation
							DummyFinding finding=doCorrelation( newData);
							if(finding.pvalue<Criteria.Pvalue&&finding.confidence>Criteria.Corr)
							{
								result.add(finding);
							}
						}
						else
						{
							continue; 
//							boolean bothString=(newData.attribute(0).isString()|newData.attribute(1).isString());
//							if(bothString)
//							{
//								//go for string comparison
//							}
						}
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // apply filter
			}
		}
		return result;
	}
	
	static DummyFinding doCorrelation(Instances data)
	{
		DoubleMatrix1D vec1=new DenseDoubleMatrix1D(data.numInstances());
		DoubleMatrix1D vec2=new DenseDoubleMatrix1D(data.numInstances());
		for (int i = 0; i < data.numInstances(); i++) {
			double[] vals= data.instance(i).toDoubleArray();
			vec1.set(i, vals[0]);
			vec2.set(i, vals[1]);
		}
		DummyFinding finding=new DummyFinding();
		finding.confidence=getCorrelation(vec1,vec2);
		finding.support=data.numInstances();
		double t=finding.confidence*Math.sqrt((finding.support-2)/(1-finding.confidence*finding.confidence));
		StudentT ttest=new StudentT(finding.support-2, new DRand());
		finding.pvalue=1-ttest.cdf(t);
		finding.FeatureName.add(data.attribute(0).name());
		finding.FeatureName.add(data.attribute(1).name());
		finding.DummyName="SingleFeatureDummy";
		finding.description="Spearman Correlation";
		return finding;
		
	}
	//spearman correlation
	public static float getCorrelation(DoubleMatrix1D feature_signal,DoubleMatrix1D target_class)
	{
		if(feature_signal.zSum()==0)
			return 0;
		if(target_class.zSum()==0)
			return 0;
		
		SpearmansCorrelation corr=new SpearmansCorrelation();
		double spearman=corr.correlation(feature_signal.toArray(), target_class.toArray());
		if(Double.isInfinite(spearman)||Double.isNaN(spearman))
			return 0;
		return (float) Math.abs(spearman);
	}
	
	
	static Evaluation doClassification(Instances data)
	{
		Evaluation besteval =null;
		for (int i = 0; i < data.numAttributes(); i++) {
			if(data.attribute(i).isNominal())
			{
				data.setClassIndex(i);
				J48 tree=new J48();
				try {
					Evaluation eval=new Evaluation(data);
					tree.buildClassifier(data);
					eval.evaluateModel(tree, data);
					double tempauc=eval.areaUnderROC(0);
					if(besteval==null)
						besteval=eval;
					else if(besteval.areaUnderROC(0)<tempauc)
					{
						besteval=eval;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
		}
		
		return besteval;
	}

	
	
	
}
