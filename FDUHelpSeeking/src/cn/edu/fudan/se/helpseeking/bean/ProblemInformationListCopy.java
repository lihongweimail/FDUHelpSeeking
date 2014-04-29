package cn.edu.fudan.se.helpseeking.bean;

import java.util.ArrayList;

import org.eclipse.core.resources.IMarker;

public class ProblemInformationListCopy {
	private ArrayList<ProblemInformation> errorList= new ArrayList<>();
	private ArrayList<ProblemInformation> warningList=new ArrayList<>();
	

	
	public void setErrorList(ArrayList<ProblemInformation> errorList) {
		this.errorList = errorList;
	}

	public void setWarningList(ArrayList<ProblemInformation> warningList) {
		this.warningList = warningList;
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
	}
	
	public ArrayList<ProblemInformation> getErrorList() {
		return errorList;
	}

	public ArrayList<ProblemInformation> getWarningList() {
		return warningList;
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


}
