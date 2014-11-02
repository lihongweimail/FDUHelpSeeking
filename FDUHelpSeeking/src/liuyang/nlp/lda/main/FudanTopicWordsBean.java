package liuyang.nlp.lda.main;

public class FudanTopicWordsBean {
String wordName;
double wordWeight;
public String getWordName() {
	return wordName;
}
public void setWordName(String wordName) {
	this.wordName = wordName;
}
public double getWordWeight() {
	return wordWeight;
}
public void setWordWeight(double wordWeight) {
	this.wordWeight = wordWeight;
}

public String genFoamTreeObjectString ()
{
	String labelWeight = "";
	

		String labels = getWordName().replace(".", " ");

		labelWeight = labelWeight + "{ label: \"" + labels 
				+ "\""
				+", "
			+" weight: " + getWordWeight() 
				+ ",type: \"leaf\" } ";
	
	
return  labelWeight;
}

}
