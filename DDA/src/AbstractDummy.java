import java.util.List;

import weka.core.Instances;


public abstract class AbstractDummy {
	List<Instances> datasets;

	public AbstractDummy(List<Instances> datasets) {
		super();
		this.datasets = datasets;
	}
	
	public abstract List<DummyFinding> DigKnowledge();
	
}
