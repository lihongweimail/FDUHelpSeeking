package cn.edu.fudan.se.helpseeking.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class ConsoleInformationList {
	private ArrayList<ConsoleInformation> exceptionList;
	
	private ConsoleInformationList(){
		exceptionList = new ArrayList<>();
	}
	
	public ArrayList<ConsoleInformation> getExceptionList() {
		return exceptionList;
	}
	
	public void prettyPrint() {
		System.out.println("\nConsoleInformationList:");
		for(ConsoleInformation information : exceptionList){
			System.out.println(information.getExceptionName() + ":" + information.getDescription());
			HashMap<String, ArrayList<Integer>> locations = information.getLocations();
			if(locations != null){
				Set<Entry<String, ArrayList<Integer>>> entryset = locations.entrySet();
				for(Entry<String, ArrayList<Integer>> entry : entryset){
					String location = "\t" + entry.getKey();
					ArrayList<Integer> numbers = entry.getValue();
					for(Integer number : numbers){
						location += "\t" + number;
					}
					System.out.println(location);
				}
			}
		}
	}
	
	

	
	public void clearConsoleInformation() {
		exceptionList.clear();
	}
	
	public static ConsoleInformationList getInstance() {
		return Singleton.consoleInformationList;
	}
	
	private static class Singleton{
		static ConsoleInformationList consoleInformationList = new ConsoleInformationList();
	}
	
}
