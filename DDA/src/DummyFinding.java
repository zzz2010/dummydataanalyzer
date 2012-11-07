import java.util.HashSet;


public class DummyFinding {
	
	int support;
	double confidence;
	double pvalue;
	HashSet<String> FeatureName;
	String description;
	public DummyFinding() {
		FeatureName=new HashSet<String>();
		support=0;
		confidence=0;
		pvalue=0;
		description="";
	}

	
	
}
