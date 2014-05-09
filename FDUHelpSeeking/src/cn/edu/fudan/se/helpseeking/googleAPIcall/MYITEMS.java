package cn.edu.fudan.se.helpseeking.googleAPIcall;

import java.util.List;

public class MYITEMS {
	 
	class Image{
	Integer	byteSize;
	String	contextLink;
	Integer	height;
	Integer	thumbnailHeight;
	String	thumbnailLink;
	Integer	thumbnailWidth;
	Integer	width;
	public Integer getByteSize() {
		return byteSize;
	}
	public String getContextLink() {
		return contextLink;
	}
	public Integer getHeight() {
		return height;
	}
	public Integer getThumbnailHeight() {
		return thumbnailHeight;
	}
	public String getThumbnailLink() {
		return thumbnailLink;
	}
	public Integer getThumbnailWidth() {
		return thumbnailWidth;
	}
	public Integer getWidth() {
		return width;
	}
	public void setByteSize(Integer byteSize) {
		this.byteSize = byteSize;
	}
	public void setContextLink(String contextLink) {
		this.contextLink = contextLink;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public void setThumbnailHeight(Integer thumbnailHeight) {
		this.thumbnailHeight = thumbnailHeight;
	}
	public void setThumbnailLink(String thumbnailLink) {
		this.thumbnailLink = thumbnailLink;
	}
	public void setThumbnailWidth(Integer thumbnailWidth) {
		this.thumbnailWidth = thumbnailWidth;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	
	
	}
	class LABELES{
		String	displayName;
		String	label_with_op;
		String	name;
		public String getDisplayName() {
			return displayName;
		}
		public String getLabel_with_op() {
			return label_with_op;
		}
		public String getName() {
			return name;
		}
		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}
		public void setLabel_with_op(String label_with_op) {
			this.label_with_op = label_with_op;
		}
		public void setName(String name) {
			this.name = name;
		}
		
		}
	class PAGEMAP{
	List <String>	key;

	public List<String> getKey() {
		return key;
	}

	public void setKey(List<String> key) {
		this.key = key;
	}
	
	
	}
	String	cacheId;
	String	displayLink;
	String	fileFormat;
	String	formattedUrl;
	String	htmlFormattedUrl;
	String	htmlSnippet;
	String	htmlTitle;
	Image	image;
    String	kind;
	List<LABELES>	labels;
	String	link;
	String	mime;
	PAGEMAP	pagemap;
	String	snippet;
	String	title;
	
	public String toString() {
		String toString="\n[MYITEMS]";
		toString=toString+"\n[title:]\n"+title;
		toString=toString+"\n[link:]\n"+link;
		toString=toString+"\n[htmlformattedURL:]\n"+htmlFormattedUrl;
		toString=toString+"\n[formattedUrl:]\n"+formattedUrl;
		toString=toString+"\n[snippet(content or abstract):]\n"+snippet;
		return toString;
		
	}
	
	public String getCacheId() {
		return cacheId;
	}
	public String getDisplayLink() {
		return displayLink;
	}
	public String getFileFormat() {
		return fileFormat;
	}
	public String getFormattedUrl() {
		return formattedUrl;
	}
	public String getHtmlFormattedUrl() {
		return htmlFormattedUrl;
	}
	public String getHtmlSnippet() {
		return htmlSnippet;
	}
	public String getHtmlTitle() {
		return htmlTitle;
	}
	public Image getImage() {
		return image;
	}
	public String getKind() {
		return kind;
	}
	public List<LABELES> getLabels() {
		return labels;
	}
	public String getLink() {
		return link;
	}
	public String getMime() {
		return mime;
	}
	public PAGEMAP getPagemap() {
		return pagemap;
	}
	public String getSnippet() {
		return snippet;
	}
	public String getTitle() {
		return title;
	}
	public void setCacheId(String cacheId) {
		this.cacheId = cacheId;
	}
	public void setDisplayLink(String displayLink) {
		this.displayLink = displayLink;
	}
	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}
	public void setFormattedUrl(String formattedUrl) {
		this.formattedUrl = formattedUrl;
	}
	public void setHtmlFormattedUrl(String htmlFormattedUrl) {
		this.htmlFormattedUrl = htmlFormattedUrl;
	}
	public void setHtmlSnippet(String htmlSnippet) {
		this.htmlSnippet = htmlSnippet;
	}
	public void setHtmlTitle(String htmlTitle) {
		this.htmlTitle = htmlTitle;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}

	
	public void setLabels(List<LABELES> labels) {
		this.labels = labels;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public void setMime(String mime) {
		this.mime = mime;
	}
	public void setPagemap(PAGEMAP pagemap) {
		this.pagemap = pagemap;
	}
	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}
	public void setTitle(String title) {
		this.title = title;
	}




}
