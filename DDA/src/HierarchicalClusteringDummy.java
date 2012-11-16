import java.util.ArrayList;
import java.util.List;
import weka.clusterers.FilteredClusterer;
import weka.core.ChebyshevDistance;
import weka.core.DistanceFunction;
import weka.core.EuclideanDistance;
import weka.core.Instances;
import weka.core.ManhattanDistance;
import weka.core.SelectedTag;
import weka.filters.unsupervised.attribute.Remove;


public class HierarchicalClusteringDummy implements AbstractDummy {

	@Override
	public List<DummyFinding> DigKnowledge(List<Instances> datasets) {
		// TODO Auto-generated method stub
		 List<DummyFinding> findings=new ArrayList<DummyFinding>();
		for (int i = 0; i < datasets.size()-1; i++) {
			for (int j = i+1; j < datasets.size(); j++)
			{
				List<DummyFinding> result=processTwoTables(datasets.get(i),datasets.get(j));
				findings.addAll(result);
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

		ArrayList<DistanceFunction> distFuns=new ArrayList<DistanceFunction>();
		distFuns.add(new ChebyshevDistance());
		distFuns.add(new EuclideanDistance());
		distFuns.add(new  ManhattanDistance());
		
		
		ArrayList<SelectedTag> LinkTypes=new ArrayList<SelectedTag>();
		LinkTypes.add(new SelectedTag(0, HierarchicalClusterer.TAGS_LINK_TYPE));
		LinkTypes.add(new SelectedTag(1, HierarchicalClusterer.TAGS_LINK_TYPE));
		LinkTypes.add(new SelectedTag(2, HierarchicalClusterer.TAGS_LINK_TYPE));
		LinkTypes.add(new SelectedTag(3, HierarchicalClusterer.TAGS_LINK_TYPE));
		LinkTypes.add(new SelectedTag(4, HierarchicalClusterer.TAGS_LINK_TYPE));
		LinkTypes.add(new SelectedTag(5, HierarchicalClusterer.TAGS_LINK_TYPE));
		LinkTypes.add(new SelectedTag(6, HierarchicalClusterer.TAGS_LINK_TYPE));
		HierarchicalClusterer clustering= new HierarchicalClusterer();
		
		Remove filter=new Remove();
		filter.setAttributeIndicesArray(new int[]{0});
		clustering.setPrintNewick(true);
		FilteredClusterer filteredCluster=new FilteredClusterer();
		
		filteredCluster.setFilter(filter);
		
		
		//build cluster
		try {
			for (int i = 0; i < distFuns.size(); i++) {
				clustering.setDistanceFunction(distFuns.get(i));
				for (int j = 0; j<LinkTypes.size(); j++) {
					clustering.setLinkType(LinkTypes.get(j));
					filteredCluster.setClusterer(clustering);
					filteredCluster.buildClusterer(first);
					String clusterTree=clustering.graph();
					System.out.println(clusterTree);
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public List<DummyFinding> ClusteringPredict()
	{
		return null;
	}
}
