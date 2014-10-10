package cn.edu.fudan.se.helpseeking.test;

import java.util.List;

import org.carrot2.core.Cluster;
import org.carrot2.core.Document;

import cn.edu.fudan.se.helpseeking.googleAPIcall.WEBResult;
import cn.edu.fudan.se.helpseeking.util.CarrotTopic;

public class CarrotTopicTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<WEBResult> results = SampleWebResults.WEB_RESULTS;
		List<Cluster> clusters = CarrotTopic.fromWebResults(results);
		
		CarrotTopic.printCluster(clusters, 0);
	}

}
