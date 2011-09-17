package org.mobilization.schedule.model;

import java.util.Date;

public class Event {

	private String title;

	private String speaker;

	private String description;

	private Date from;

	private Date to;

	private int hallId;

	public Event() {
	}

	public Event(String title, String speaker) {
		super();
		this.title = title;
		this.speaker = speaker;
		this.description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ut lectus et lacus convallis ornare. Maecenas et mollis risus. Morbi quam massa, rutrum a rhoncus non, interdum nec metus. Pellentesque sit amet libero nisi, non sodales magna. Cras eros magna, gravida in tristique lobortis, pretium eu diam. Vivamus viverra nibh in augue consectetur at pulvinar lorem commodo. Nullam eget neque ante, ac euismod sapien. Sed dui metus, vehicula in egestas vel, sagittis et sapien. Quisque ac turpis id ligula ultricies hendrerit ac faucibus augue. Cras sollicitudin cursus erat ut porttitor. Morbi id augue ipsum. Etiam tincidunt congue ligula, a pretium risus sagittis nec. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; In hendrerit elementum elementum. Morbi nec dui sit amet massa aliquam adipiscing. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Pellentesque magna purus, tristique sit amet dictum sed, vulputate eu eros. Pellentesque ac quam eros.";
		this.from = new Date();
		this.to = new Date();
	}

	public Event(String title, String speaker, String description, Date from, Date to) {
		super();
		this.title = title;
		this.speaker = speaker;
		this.description = description;
		this.from = from;
		this.to = to;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSpeaker() {
		return speaker;
	}

	public void setSpeaker(String speaker) {
		this.speaker = speaker;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public int getHallId() {
		return hallId;
	}

	public void setHallId(int hallId) {
		this.hallId = hallId;
	}
}
