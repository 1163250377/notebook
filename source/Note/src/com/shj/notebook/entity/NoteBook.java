package com.shj.notebook.entity;

public class NoteBook{

	private Integer id;
	private String title;
	private String createTime;
	private String updateTime;
	private String content;
	private String type;
	private Integer directoryId;

	public NoteBook() {
		super();
	}

	public NoteBook(Integer id, String title, String createTime, String updateTime, String content, String type,
			Integer directoryId) {
		super();
		this.id = id;
		this.title = title;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.content = content;
		this.type = type;
		this.directoryId = directoryId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getDirectoryId() {
		return directoryId;
	}

	public void setDirectoryId(Integer directoryId) {
		this.directoryId = directoryId;
	}
}
