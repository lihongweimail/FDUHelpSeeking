package cn.edu.fudan.se.helpseeking.util;

import org.eclipse.core.resources.IMarker;

import cn.edu.fudan.se.helpseeking.bean.Basic.CompileInfoType;
import cn.edu.fudan.se.helpseeking.bean.Breakpoint;
import cn.edu.fudan.se.helpseeking.bean.CompileInformation;
import cn.edu.fudan.se.helpseeking.bean.ProblemInformation;
import cn.edu.fudan.se.helpseeking.bean.ProblemInformationList;

public class ProblemInformationUtil {
	
	public static CompileInformation SelectProblemInformationByBreakpont(Breakpoint bp){
		CompileInformation compileInformation = new CompileInformation();		
		ProblemInformationList list = ProblemInformationList.getInstance();		
		boolean found = false;
		
		for(ProblemInformation problemInformation : list.getErrorList()){
			if(!found){
				if(problemInformation.getPath().contains(bp.getFileName())){
					int differ = problemInformation.getLineNumber() - bp.getLineNo();
					if(differ > -5 && differ < 5){
						generateCompileInformationbyProblemInformation(compileInformation, 
								problemInformation);
						found = true;
					}
				}
			}			
		}
		for(ProblemInformation problemInformation : list.getWarningList()){
			if(!found){
				if(problemInformation.getPath().contains(bp.getFileName())){
					int differ = problemInformation.getLineNumber() - bp.getLineNo();
					if(differ > -5 && differ < 5){
						generateCompileInformationbyProblemInformation(compileInformation, 
								problemInformation);
						found = true;
					}
				}
			}			
		}
		if(!found){
			if(list.getErrorList().size() > 0){
				ProblemInformation problemInformation = list.getErrorList().get(0);
				generateCompileInformationbyProblemInformation(compileInformation, 
						problemInformation);
				found = true;
			}
		}
		if(!found){
			if(list.getWarningList().size() > 0){
				ProblemInformation problemInformation = list.getWarningList().get(0);
				generateCompileInformationbyProblemInformation(compileInformation, 
						problemInformation);
				found = true;
			}
		}
		
		return compileInformation;
	}
	
	private static void generateCompileInformationbyProblemInformation(
			CompileInformation compileInformation, ProblemInformation problemInformation){
		compileInformation.setContent(problemInformation.getDescription());
		compileInformation.setRelatedCode(problemInformation.getSource());
		switch(problemInformation.getSeverity()){
			case IMarker.SEVERITY_ERROR:
				compileInformation.setType(CompileInfoType.ERROR);
				break;
			case IMarker.SEVERITY_WARNING:
				compileInformation.setType(CompileInfoType.WARNING);
				break;
		}
	}

}
