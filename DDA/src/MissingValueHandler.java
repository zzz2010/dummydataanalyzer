import java.util.ArrayList;
import java.util.List;

import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;


public class MissingValueHandler extends DummyWrapper {

	public MissingValueHandler(AbstractDummy workerDummy) {
		super(workerDummy);
		// TODO Auto-generated constructor stub
	}

	public static Instances fillMissingValueMeanMode(Instances dataset)
	{
		Instances filled=null;
		weka.filters.unsupervised.attribute.ReplaceMissingValues replaceFilter=new ReplaceMissingValues();
		try {
			replaceFilter.setInputFormat(dataset);
			 filled = Filter.useFilter(dataset, replaceFilter);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return filled;
	}

	@Override
	public List<DummyFinding> DigKnowledge(List<Instances> datasets) {
		// TODO Auto-generated method stub
		List<Instances> filledMissData=new ArrayList<Instances>();
		for(int i=0;i<datasets.size();i++)
		{
			filledMissData.add(fillMissingValueMeanMode(datasets.get(i)));
		}
		
		return workerDummy.DigKnowledge(filledMissData);
	}
}
