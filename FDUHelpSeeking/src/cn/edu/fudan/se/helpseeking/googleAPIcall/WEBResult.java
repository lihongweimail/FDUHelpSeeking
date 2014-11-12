package cn.edu.fudan.se.helpseeking.googleAPIcall;

public class WEBResult {

  	   private String  summary;
  	   private String unescapedUrl;
  	   private String url;
         private String title;
       private String titleNoFormatting;
       private String location;
        private String publisher;
      private String publishedDate;
      private String signedRedirectUrl;
     private String language;
      
      public String getSummary() {
			return summary;
		}
		public void setSummary(String content) {
			this.summary = content;
		}
		public String getLanguage() {
			return language;
		}
		public void setLanguage(String language) {
			this.language = language;
		}
		public String getUnescapedUrl() {
			return unescapedUrl;
		}
		public void setUnescapedUrl(String unescapedUrl) {
			this.unescapedUrl = unescapedUrl;
		}
		public String getTitleNoFormatting() {
			return titleNoFormatting;
		}
		public void setTitleNoFormatting(String titleNoFormatting) {
			this.titleNoFormatting = titleNoFormatting;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public String getPublisher() {
			return publisher;
		}
		public void setPublisher(String publisher) {
			this.publisher = publisher;
		}
		public String getPublishedDate() {
			return publishedDate;
		}
		public void setPublishedDate(String publishedDate) {
			this.publishedDate = publishedDate;
		}
		public String getSignedRedirectUrl() {
			return signedRedirectUrl;
		}
		public void setSignedRedirectUrl(String signedRedirectUrl) {
			this.signedRedirectUrl = signedRedirectUrl;
		}

		public String getUrl() { return url; }
      public String getTitle() { return title; }
      public void setUrl(String url) { this.url = url; }
      public void setTitle(String title) { this.title = title; }
      public String toString() { return "\n[\nurl:\t\t\t\t" + url +"\ntitle(no formatting):\t" + title + "\n]\n"; }
  

}
