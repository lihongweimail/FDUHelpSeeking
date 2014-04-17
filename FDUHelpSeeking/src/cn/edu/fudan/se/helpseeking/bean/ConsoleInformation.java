package cn.edu.fudan.se.helpseeking.bean;

import java.util.ArrayList;
import java.util.HashMap;

public class ConsoleInformation {
	private String exceptionName;
	private String description;
	private HashMap<String, ArrayList<Integer>> locations;
	
	public ConsoleInformation(){
		locations = new HashMap<>();
	}
	
	public String getExceptionName() {
		return exceptionName;
	}
	
	public void setExceptionName(String exceptionName) {
		this.exceptionName = exceptionName;
	}	

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public HashMap<String, ArrayList<Integer>> getLocations() {
		return locations;
	}
	
	public void addLocation(String fileName, int lineNumber) {
		if(locations.containsKey(fileName)){
			if(!locations.get(fileName).contains(lineNumber))
				locations.get(fileName).add(lineNumber);
		}else{
			ArrayList<Integer> list = new ArrayList<>();
			list.add(lineNumber);
			locations.put(fileName, list);
		}
	}
}
