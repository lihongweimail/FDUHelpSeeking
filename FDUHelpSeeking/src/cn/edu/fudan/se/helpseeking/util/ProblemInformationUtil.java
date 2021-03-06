package cn.edu.fudan.se.helpseeking.util;

import org.eclipse.core.resources.IMarker;

import cn.edu.fudan.se.helpseeking.bean.Basic.CompileInfoType;
import cn.edu.fudan.se.helpseeking.bean.Breakpoint;
import cn.edu.fudan.se.helpseeking.bean.CompileInformation;
import cn.edu.fudan.se.helpseeking.bean.Cursor;
import cn.edu.fudan.se.helpseeking.bean.ProblemInformation;
import cn.edu.fudan.se.helpseeking.bean.ProblemInformationList;

public class ProblemInformationUtil {
	
	public static CompileInformation SelectProblemInformationByBreakpont(Breakpoint bp){
		CompileInformation compileInformation = null;		
		ProblemInformationList list = ProblemInformationList.getInstance();		
		boolean found = false;
		
		for(ProblemInformation problemInformation : list.getErrorList()){
			if(!found){
				if (problemInformation.getPath()!=null && bp.getFileName()!=null) {
				if(problemInformation.getPath().contains(bp.getFileName())){
					int differ = problemInformation.getLineNumber() - bp.getLineNo();
					if(differ > -5 && differ < 5){
						compileInformation = 
								generateCompileInformationbyProblemInformation(
										problemInformation);
						found = true;
					}
				}
				}
			}			
		}
		for(ProblemInformation problemInformation : list.getWarningList()){
			if(!found){
				if (problemInformation.getPath()!=null && bp.getFileName()!=null) {
				if(problemInformation.getPath().contains(bp.getFileName())){
					int differ = problemInformation.getLineNumber() - bp.getLineNo();
					if(differ > -5 && differ < 5){
						compileInformation = 
								generateCompileInformationbyProblemInformation(
										problemInformation);
						found = true;
					}
				}
				}
			}			
		}
		if(!found){
			if(list.getErrorList().size() > 0){
				ProblemInformation problemInformation = list.getErrorList().get(0);
				compileInformation = 
						generateCompileInformationbyProblemInformation(
								problemInformation);
				found = true;
			}
		}
		if(!found){
			if(list.getWarningList().size() > 0){
				ProblemInformation problemInformation = list.getWarningList().get(0);
				compileInformation = 
						generateCompileInformationbyProblemInformation(
								problemInformation);
				found = true;
			}
		}
		
		return compileInformation;
	}
	
	public static CompileInformation SelectProblemInformationByCursor(Cursor c){
		CompileInformation compileInformation = null;		
		ProblemInformationList list = ProblemInformationList.getInstance();		
		boolean found = false;
		
		for(ProblemInformation problemInformation : list.getErrorList()){
			if(!found){
				if (problemInformation.getPath()!=null && c.getFileName()!=null) {
					
				
				if(problemInformation.getPath().contains(c.getFileName())){
					int differ = problemInformation.getLineNumber() - c.getLineNo();
					if(differ > -5 && differ < 5){
						compileInformation = 
								generateCompileInformationbyProblemInformation(
										problemInformation);
						found = true;
					}
				
				}
				}
			}			
		}
		for(ProblemInformation problemInformation : list.getWarningList()){
			if(!found){
				if (problemInformation.getPath()!=null && c.getFileName()!=null) {
				
			if(problemInformation.getPath().contains(c.getFileName())){
					int differ = problemInformation.getLineNumber() - c.getLineNo();
					if(differ > -5 && differ < 5){
						compileInformation = 
								generateCompileInformationbyProblemInformation(
										problemInformation);
						found = true;
					}
				}
			}	
		 }		
		}
		if(!found){
			if(list.getErrorList().size() > 0){
				ProblemInformation problemInformation = list.getErrorList().get(0);
				compileInformation = 
						generateCompileInformationbyProblemInformation(
								problemInformation);
				found = true;
			}
		}
		if(!found){
			if(list.getWarningList().size() > 0){
				ProblemInformation problemInformation = list.getWarningList().get(0);
				compileInformation = 
						generateCompileInformationbyProblemInformation(
								problemInformation);
				found = true;
			}
		}
		
		return compileInformation;
	}
	
	public static CompileInformation generateCompileInformationbyProblemInformation(
			ProblemInformation problemInformation){
		CompileInformation compileInformation = new CompileInformation();
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
		return compileInformation;
	}

}
