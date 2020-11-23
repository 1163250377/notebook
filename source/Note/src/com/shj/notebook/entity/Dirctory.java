package com.shj.notebook.entity;

public class Dirctory {

	private Integer id;//主键
	private String name;//名字
	private Integer dirOrder;//排序
	private Integer parentId;//父节点
	/***
	 * 节点类型
	 */
	private DataType type;//文件类型
	public Dirctory() {
		super();
	}


	public Dirctory(Integer id, String name,Integer order, Integer parentId, String type) {
		super();
		this.id = id;
		this.name = name;
		this.dirOrder = order;
		this.parentId = parentId;
		this.type = DataType.valueOf(type);
	}


	public Dirctory(String name,Integer order, Integer parentId, DataType type) {
		super();
		this.name = name;
		this.dirOrder = order;
		this.parentId = parentId;
		this.type = type;
	}
	
	


	public Integer getDirOrder() {
		return dirOrder;
	}


	public void setDirOrder(Integer dirOrder) {
		this.dirOrder = dirOrder;
	}


	public DataType getType() {
		return type;
	}

	public void setType(DataType type) {
		this.type = type;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	@Override
	public String toString() {
		return name;
	}

}
