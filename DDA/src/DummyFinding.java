import java.util.HashSet;


public class DummyFinding implements Comparable<DummyFinding>{
	
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

	@Override
	public int compareTo(DummyFinding o) {
		// TODO Auto-generated method stub
		if(this.confidence==o.confidence)
		{
			if(this.support==o.support)
			{
				if(pvalue==o.pvalue)
				{
					if(isCrossTable==true&&o.isCrossTable==false)
					{
						return 1;
					}
					else if (isCrossTable==false&&o.isCrossTable==true)
					{
						return -1;
					}
					else
					{
						return 0;
					}
						
				}
				else
					Double.compare(o.pvalue, pvalue);//smaller the better
			}
			else
				Double.compare(support, o.support);
		}
		return Double.compare(this.confidence,o.confidence);
	}

	
	
	
}
