package com.cong.mm;

public class ShareContent {
	private String title;
	private String urlImage;
	private String content;
	private String targetUrl;
	private int imageId;

	public String getUrlImage() {
		return urlImage;
	}

	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	@Override
	public String toString() {
		return "ShareContent{" +
				"title='" + title + '\'' +
				", urlImage='" + urlImage + '\'' +
				", content='" + content + '\'' +
				", targetUrl='" + targetUrl + '\'' +
				", imageId=" + imageId +
				'}';
	}
}
