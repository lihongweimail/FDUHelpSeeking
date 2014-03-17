package cn.edu.fudan.se.helpseeking.googleAPIcall;

import java.util.List;

public class GoogleAPICallResults {
 
	    private ResponseData responseData;
	    public ResponseData getResponseData() { return responseData; }
	    public void setResponseData(ResponseData responseData) { this.responseData = responseData; }
	    public String toString() { return "ResponseData[" + responseData + "]"; }
	 
	    static class ResponseData {
	        private List<WEBResult> results;
	        private int estimatedResultCount;
	        
	        
	        public int getEstimatedResultCount() {
				return estimatedResultCount;
			}
			public void setEstimatedResultCount(int estimatedResultCount) {
				this.estimatedResultCount = estimatedResultCount;
			}
			public List<WEBResult> getResults() { return results; }
	        public void setResults(List<WEBResult> results) { this.results = results; }
	        public String toString() { return "Results[" + results + "]"; }
	    }
	 
	  
}
