package se.customervalue.cvs.api.representation.domain;

import se.customervalue.cvs.domain.SystemLogEntry;
import se.customervalue.cvs.domain.SystemLogEntryType;

public class SystemLogEntryRepresentation {
	private int logEntryId;

	private SystemLogEntryType type;

	private long timestamp;

	private String title;

	private String text;

	public SystemLogEntryRepresentation() {}

	public SystemLogEntryRepresentation(SystemLogEntry systemLogEntry) {
		this.logEntryId = systemLogEntry.getLogEntryId();
		this.type = systemLogEntry.getType();
		this.timestamp = systemLogEntry.getTimestamp().getTime() / 1000;
		this.title = systemLogEntry.getTitle();
		this.text = systemLogEntry.getText();
	}

	public SystemLogEntryRepresentation(int logEntryId, SystemLogEntryType type, long timestamp, String title, String text) {
		this.logEntryId = logEntryId;
		this.type = type;
		this.timestamp = timestamp;
		this.title = title;
		this.text = text;
	}

	public int getLogEntryId() {
		return logEntryId;
	}

	public void setLogEntryId(int logEntryId) {
		this.logEntryId = logEntryId;
	}

	public SystemLogEntryType getType() {
		return type;
	}

	public void setType(SystemLogEntryType type) {
		this.type = type;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
