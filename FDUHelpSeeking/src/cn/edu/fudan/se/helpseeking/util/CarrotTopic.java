package cn.edu.fudan.se.helpseeking.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.carrot2.clustering.lingo.LingoClusteringAlgorithm;
import org.carrot2.core.Cluster;
import org.carrot2.core.Controller;
import org.carrot2.core.ControllerFactory;
import org.carrot2.core.Document;
import org.carrot2.core.ProcessingResult;

import cn.edu.fudan.se.helpseeking.googleAPIcall.WEBResult;

/**
 * Topic Clustering by Carrot2
 * @author LingFeng
 *
 */
public class CarrotTopic {
	
	public final static int DESIRED_CLUSTER_COUNT = 30;
	
	/**
	 * Cluster: 
	 * 		-label
	 * 		-document list
	 * @param results
	 * @return List<Cluster>
	 */
	public static List<Cluster> fromWebResults(List<WEBResult> results)
	{
		List<Document> docs = toCarrotDocument(results);
		
		final Controller controller = ControllerFactory.createSimple();
		 
        /*
         * Perform clustering by topic using the Lingo algorithm. Lingo can 
         * take advantage of the original query, so we provide it along with the documents.
         */
		LingoClusteringAlgorithm al = new LingoClusteringAlgorithm();
        al.desiredClusterCountBase = DESIRED_CLUSTER_COUNT;
        
        //Map<String, Object> attributes = new HashMap<String, Object>(); 
        //attributes.put("LingoClusteringAlgorithm.desiredClusterCountBase", 30); 
        //controller.init(attributes);
        
        final ProcessingResult byTopicClusters = controller.process(docs, "*",LingoClusteringAlgorithm.class);
        
                
        final List<Cluster> clustersByTopic = byTopicClusters.getClusters();
        
        return clustersByTopic;
	}
	
	public static List<Document> toCarrotDocument(List<WEBResult> results)
	{
		List<Document> docs = new ArrayList<Document>();
		
		for(WEBResult r : results)
		{
			docs.add(new Document(r.getTitle(), r.getContent(),r.getUrl()));
		}
		
		return docs;
	}
	
	public static void printCluster(List<Cluster> clusters, int level)
	 {
		 for(int i=0; i<clusters.size(); i++)
	     {
	    	 Cluster c = clusters.get(i);
	    	 for(int k=0; k<level; k++)
	    	 {
	    		 System.out.print("+");
	    	 }
	    	 
	    	 System.out.println(c.getLabel());
	    	 
	    	 
	    	 for(int j=0; j<c.getAllDocuments().size(); j++)
	    	 {		
	    		 Document doc = c.getAllDocuments().get(j);
	    		 System.out.println("No. "+j);
	    		 System.out.println("  --title  :"+doc.getTitle());
	    		 System.out.println("  --url    :"+doc.getContentUrl());
	    		 System.out.println("  --summary:"+doc.getSummary());
	    		 
	    		 
	    	 }
	    	 
	    	 printCluster(c.getSubclusters(), level+1);
	     }
		 
	 }
}
