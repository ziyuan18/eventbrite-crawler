package ai.preferred.crawler.eventbrite.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class EBEvent {
	
	private String url;
	private String title;
	private String address;
	private String datetime; 
	private String description; 
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getDateTime() {
		return datetime; 
	}
	
	public void setDateTime(String datetime) {
		this.datetime = datetime; 
	}
	
	public String getDescription() {
		return description; 
	}
	
	public void setDescription(String description) {
		this.description = description; 
	}
}