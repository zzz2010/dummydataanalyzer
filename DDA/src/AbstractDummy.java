import java.util.List;

import weka.core.Instances;


public interface AbstractDummy {
	
	public  List<DummyFinding> DigKnowledge(List<Instances> datasets);
	
}
