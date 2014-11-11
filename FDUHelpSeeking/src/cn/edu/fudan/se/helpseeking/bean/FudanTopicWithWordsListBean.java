package cn.edu.fudan.se.helpseeking.bean;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.util.WordlistLoader;

import cn.edu.fudan.se.helpseeking.util.CommUtil;

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

			//String labels = wordsList.get(i).getWordName().replace(".", " ");
			//引入切词工具 处置
			//不切词更好：
			//String labels = CommUtil.getSimpleWords(wordsList.get(i).getWordName().replace(".", " "));
			String labels = wordsList.get(i).getWordName();
			labelWeight = labelWeight + "{ label: \"" + labels 
					+ "\""
					+", "
				+" weight: " + wordsList.get(i).getWordWeight() 
					+ ",type: \"leaf\" },";
		}
		
//    two level group with topic name
//		String groupString = "{ label: \"" + getTopicName().replace(".", " ") + "\" , groups: ["
//				+ labelWeight + "]" + " ,type: \"node\"} ";


//     two level group with no topic name  为了显示需要不遮住第二层，这里设置组的标签label为空格，或者使用1个词的topic
		String groupString = "{ label: \" " + "" + "\" , groups: ["
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
