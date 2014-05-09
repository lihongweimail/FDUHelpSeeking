package cn.edu.fudan.se.helpseeking.googleAPIcall;

import java.util.List;

public class MYpromotions {

	class BODYLINE{
		String	htmlTitle;
		String	link;		
		String	title;

		String	url;

		public String getHtmlTitle() {
			return htmlTitle;
		}

		public String getLink() {
			return link;
		}

		public String getTitle() {
			return title;
		}

		public String getUrl() {
			return url;
		}

		public void setHtmlTitle(String htmlTitle) {
			this.htmlTitle = htmlTitle;
		}

		public void setLink(String link) {
			this.link = link;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public void setUrl(String url) {
			this.url = url;
		}




	}
	class Image{
		Integer	height;
		String	source;
		Integer	width;
		public Integer getHeight() {
			return height;
		}
		public String getSource() {
			return source;
		}
		public Integer getWidth() {
			return width;
		}
		public void setHeight(Integer height) {
			this.height = height;
		}
		public void setSource(String source) {
			this.source = source;
		}
		public void setWidth(Integer width) {
			this.width = width;
		}


	}			
	List<BODYLINE> bodyLines;			

	String	displayLink;

	String	htmlTitle;


	Image	image;

	String	link;

	String	title;
	
	public String toString() {
		String toString="\n[mypromotions]";
		toString=toString+"\n[title:]\n"+title;
		toString=toString+"\n[link:]\n"+link;
		return toString;
	}

	public List<BODYLINE> getBodyLines() {
		return bodyLines;
	}

	public String getDisplayLink() {
		return displayLink;
	}

	public String getHtmlTitle() {
		return htmlTitle;
	}

	public Image getImage() {
		return image;
	}

	public String getLink() {
		return link;
	}

	public String getTitle() {
		return title;
	}

	public void setBodyLines(List<BODYLINE> bodyLines) {
		this.bodyLines = bodyLines;
	}

	public void setDisplayLink(String displayLink) {
		this.displayLink = displayLink;
	}

	public void setHtmlTitle(String htmlTitle) {
		this.htmlTitle = htmlTitle;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public void setTitle(String title) {
		this.title = title;
	}




}
