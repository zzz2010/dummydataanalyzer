import java.util.HashSet;


public class DummyFinding {
	
	int support;
	double confidence;
	double pvalue;
	boolean isCrossTable;
	HashSet<String> FeatureName;
	String description;
	String DummyName;
	String PredictTarget;
	
	public DummyFinding() {
		FeatureName=new HashSet<String>();
		support=0;
		confidence=0;
		pvalue=0;
		description="";
		DummyName="";
		isCrossTable=false;
	}
	@Override
	public String toString() {
		String ret=DummyName+" find out:"+FeatureName.toString()+" "+description+"\n";
		ret+="Support:"+support+"\tConfidence:"+confidence+"\tpvalue:"+pvalue;
		return ret;
	}

	
	
}
