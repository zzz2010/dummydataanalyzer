import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import weka.core.Instances;


public class ShuffleVerifier extends DummyWrapper {

	public ShuffleVerifier(AbstractDummy workerDummy) {
		super(workerDummy);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<DummyFinding> DigKnowledge(List<Instances> datasets) {
		// TODO Auto-generated method stub
		List<DummyFinding> verifiedResult=new ArrayList<DummyFinding>();
		
		List<DummyFinding> posFindings=workerDummy.DigKnowledge(datasets);
		List<DummyFinding> negFindings=new ArrayList<DummyFinding>();
		int shuffleRound=10;
		if(posFindings.size()>0)// if no finding, then no false positive
		for (int i = 0; i < shuffleRound; i++) {
			List<Instances> shuffledData = common.ShuffleValues(datasets);
			List<DummyFinding> temp = workerDummy.DigKnowledge(shuffledData);
			if(temp!=null)
				negFindings.addAll(temp);
		}
			
		
		HashMap<Integer, DummyFinding> findingMap=new HashMap<Integer, DummyFinding>();
		for (DummyFinding dummyFinding : posFindings) {
			int code=dummyFinding.FeatureName.hashCode();
			findingMap.put(code, dummyFinding);
		}
		for (DummyFinding dummyFinding : negFindings) {
			int code=dummyFinding.FeatureName.hashCode();
			if(findingMap.containsKey(code))
			{
				DummyFinding posFD = findingMap.get(code);
				if(posFD.confidence<=dummyFinding.confidence)
				{
					findingMap.remove(code);
				}
			}
		}
		for(DummyFinding dummyFinding : findingMap.values())
		{
			verifiedResult.add(dummyFinding);
		}
		return verifiedResult;
	}

}
