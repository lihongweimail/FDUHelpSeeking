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
		switch(information.getSeverity()){
			case IMarker.SEVERITY_ERROR:
				errorList.add(information);
				break;
			case IMarker.SEVERITY_WARNING:
				warningList.add(information);
				break;
			case IMarker.SEVERITY_INFO:
				informationList.add(information);
				break;
		}
	}
	
	public void clearProblemInformation() {
		errorList.clear();
		warningList.clear();
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

	public static ProblemInformationList getInstance() {
		return Singleton.problemInformationList;
	}
	
	private static  class Singleton {
		 static ProblemInformationList problemInformationList = new ProblemInformationList();
	}

}
