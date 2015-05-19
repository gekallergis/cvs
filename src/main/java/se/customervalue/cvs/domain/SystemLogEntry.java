package se.customervalue.cvs.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class SystemLogEntry {
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private int logEntryId;

	@Lob
	private String text;

	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;

	private String title;

	@Enumerated(EnumType.STRING)
	private SystemLogEntryType type;

	public SystemLogEntry() {}

	public SystemLogEntry(String text, String title, SystemLogEntryType type) {
		this.text = text;
		this.title = title;
		this.type = type;
	}

	@PrePersist
	protected void onCreate() {
		timestamp = new Date();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public SystemLogEntryType getType() {
		return type;
	}

	public void setType(SystemLogEntryType type) {
		this.type = type;
	}

	public int getLogEntryId() {
		return logEntryId;
	}

	public void setLogEntryId(int logEntryId) {
		this.logEntryId = logEntryId;
	}
}
