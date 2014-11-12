package cn.edu.fudan.se.helpseeking.bean;

public class ProblemInformation {
	private int severity;
	private String description;
	private String resource;
	private String path;
	private int lineNumber;
	private int charStart;
	private int charEnd;
	private String relatedMethod;
	private String cause;
	private String source;
	
	public int getSeverity() {
		return severity;
	}
	
	public void setSeverity(int severity) {
		this.severity = severity;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getResource() {
		return resource;
	}
	
	public void setResource(String resource) {
		this.resource = resource;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public int getLineNumber() {
		return lineNumber;
	}
	
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	
	public int getCharStart() {
		return charStart;
	}
	
	public void setCharStart(int charStart) {
		this.charStart = charStart;
	}
	
	public int getCharEnd() {
		return charEnd;
	}
	
	public void setCharEnd(int charEnd) {
		this.charEnd = charEnd;
	}
	
	public String getRelatedMethod() {
		return relatedMethod;
	}
	
	public void setRelatedMethod(String relatedMethod) {
		this.relatedMethod = relatedMethod;
	}
	
	public String getCause() {
		return cause;
	}
	
	public void setCause(String cause) {
		this.cause = cause;
	}
	
	public String getSource() {
		return source;
	}
	
	public void setSource(String source) {
		this.source = source;
	}

	@Override 
	public boolean equals(Object obj) {
		if(obj instanceof ProblemInformation){
			ProblemInformation problemInformation = (ProblemInformation)obj;
			if(problemInformation.getPath().equals(path) 
					&& problemInformation.getDescription().equals(description)){
				return true;
			}
		}
		return false;
	}

}
