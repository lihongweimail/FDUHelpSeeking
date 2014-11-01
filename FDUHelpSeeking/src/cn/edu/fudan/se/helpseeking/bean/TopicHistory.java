package cn.edu.fudan.se.helpseeking.bean;

import java.util.List;

public class TopicHistory {
	int carrotTopicID;
	String carrotTopicStr;
	List<LDAURLbean> carrotURLStr;
	double weight;
	public int getCarrotTopicID() {
		return carrotTopicID;
	}
	public void setCarrotTopicID(int carrotTopicID) {
		this.carrotTopicID = carrotTopicID;
	}
	public String getCarrotTopicStr() {
		return carrotTopicStr;
	}
	public void setCarrotTopicStr(String carrotTopicStr) {
		this.carrotTopicStr = carrotTopicStr;
	}
	public List<LDAURLbean> getCarrotURLStr() {
		return carrotURLStr;
	}
	public void setCarrotURLStr(List<LDAURLbean> carrotURLStr) {
		this.carrotURLStr = carrotURLStr;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	
	

}
