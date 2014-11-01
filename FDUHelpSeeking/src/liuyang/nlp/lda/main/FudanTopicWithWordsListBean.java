package liuyang.nlp.lda.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.util.WordlistLoader;

public class FudanTopicWithWordsListBean {
	
	
    int topicNO;
	String topicName;
	double topicWeight;
	List<FudanTopicWordsBean> wordsList = new ArrayList<FudanTopicWordsBean>();

	public String genFoamTreeGroupString() {
		// dataObject: {
		// groups: [
		// { label:"Group 1", groups: [
		// { label:"Group 1.1" },
		// { label:"Group 1.2" },
		// { label:"Group 1.3" }
		// ]},
		// { label:"Group 2", groups: [
		// { label:"Group 2.1" },
		// { label:"Group 2.2" }
		// ]},
		// { label:"Group 3" }
		// ]
		// }
		String labelWeight = "";
		for (int i = 0; i < wordsList.size(); i++) 
		{

			String labels = wordsList.get(i).getWordName().replace(".", " ");

			labelWeight = labelWeight + "{ label: \"" + labels 
					+ "\""
					+", "
				+" weight: " + wordsList.get(i).getWordWeight() 
					+ ",type: \"leaf\" },";
		}
		
		String groupString = "{ label: \"" + getTopicName().replace(".", " ") + "\" , groups: ["
				+ labelWeight + "]" + " ,type: \"node\"} ";

		return groupString;

	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public double getTopicWeight() {
		return topicWeight;
	}

	public void setTopicWeight(double topicWeight) {
		this.topicWeight = topicWeight;
	}

	public List<FudanTopicWordsBean> getWordsList() {
		return wordsList;
	}

	public void setWordsList(List<FudanTopicWordsBean> wordsList) {
		this.wordsList = wordsList;
	}

	public int getTopicNO() {
		return topicNO;
	}

	public void setTopicNO(int topicNO) {
		this.topicNO = topicNO;
	}

	
	
}
