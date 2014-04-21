package cn.edu.fudan.se.helpseeking.bean;

import java.util.ArrayList;

import org.eclipse.core.resources.IMarker;

public class ProblemInformationList {
	private ArrayList<ProblemInformation> errorList;
	private ArrayList<ProblemInformation> warningList;
	private ArrayList<ProblemInformation> informationList;
	
	private ProblemInformationList(){
		errorList = new ArrayList<>();
		warningList = new ArrayList<>();
		informationList = new ArrayList<>();
	}
	
	public void addProblemInformation(ProblemInformation information) {
		boolean found = false;
		switch(information.getSeverity()){
			case IMarker.SEVERITY_ERROR:
				for(ProblemInformation problemInformation : errorList){
					if(problemInformation.equals(information)){
						//errorList.remove(problemInformation);
						found = true;
						break;
					}
				}
				if(!found)
					errorList.add(information);
				break;
			case IMarker.SEVERITY_WARNING:
				for(ProblemInformation problemInformation : warningList){
					if(problemInformation.equals(information)){
						//warningList.remove(problemInformation);
						found = true;
						break;
					}
				}
				if(!found)
					warningList.add(information);
				break;
			case IMarker.SEVERITY_INFO:
				informationList.add(information);
				break;
		}
	}
	
	public void clearProblemInformation(String path) {
		for(ProblemInformation problemInformation : errorList){
			if(problemInformation.getPath().equals(path))
				errorList.remove(problemInformation);
		}
		for(ProblemInformation problemInformation : warningList){
			if(problemInformation.getPath().equals(path))
				warningList.remove(problemInformation);
		}
		informationList.clear();
	}
	
	public ArrayList<ProblemInformation> getErrorList() {
		return errorList;
	}

	public ArrayList<ProblemInformation> getWarningList() {
		return warningList;
	}

	public ArrayList<ProblemInformation> getInformationList() {
		return informationList;
	}
	
	public void prettyPrint() {
		System.out.println("======Errors======");
		for(ProblemInformation problemInformation : errorList){
			System.out.println(problemInformation.getPath() + "\t"
					+ problemInformation.getDescription());
		}
		System.out.println("======Warnings======");		
		for(ProblemInformation problemInformation : warningList){
			System.out.println(problemInformation.getPath() + "\t"
					+ problemInformation.getDescription());
		}
	}

	public static ProblemInformationList getInstance() {
		return Singleton.problemInformationList;
	}
	
	private static class Singleton {
		 static ProblemInformationList problemInformationList = new ProblemInformationList();
	}

}
