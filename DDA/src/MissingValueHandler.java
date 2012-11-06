import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;


public class MissingValueHandler {

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
}
