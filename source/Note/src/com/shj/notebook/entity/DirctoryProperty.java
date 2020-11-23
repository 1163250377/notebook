package com.shj.notebook.entity;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/***
 * 目录类的数据同步类
 * @author shj
 *
 */
public class DirctoryProperty {
	private IntegerProperty id;
	private StringProperty name;
	private IntegerProperty dirOrder;
	private IntegerProperty parentId;
	private IntegerProperty type;
	private int child_count;
	/**
	 * 是否加载过了子节点
	 */
	private boolean loadNode;
	public DirctoryProperty() {
		super();
	}
	
	public DirctoryProperty(Integer id, String name, Integer order,Integer parentId, DataType type) {
		super();
		this.id = new SimpleIntegerProperty(id);
		this.name = new SimpleStringProperty(name);
		this.dirOrder = new SimpleIntegerProperty(order);
		this.parentId = new SimpleIntegerProperty(parentId);
		this.type = new SimpleIntegerProperty(type.ordinal());
		
	}

	public Integer getId() {
		return id.get();
	}
	public void setId(Integer id) {
		this.id.set(id);
	}
	public String getName() {
		return name.get();
	}
	public void setName(String name) {
		this.name.set(name);
	}
	
	public Integer getDirOrder() {
		return dirOrder.get();
	}

	public void setDirOrder(Integer dirOrder) {
		this.dirOrder.set(dirOrder);
	}

	public Integer getParentId() {
		return parentId.get();
	}
	public void setParentId(Integer parentId) {
		this.parentId.set(parentId);
	}

	public DataType getType() {
		return DataType.class.getEnumConstants()[type.get()];
	}

	public void setType(DataType type) {	
		this.type.set(type.ordinal());
	}

	public int getChild_count() {
		return child_count;
	}

	public void setChild_count(int child_count) {
		this.child_count = child_count;
	}
	
	public boolean isLoadNode() {
		return loadNode;
	}

	public void setLoadNode(boolean loadNode) {
		this.loadNode = loadNode;
	}

	@Override
	public String toString() {
		return name.get();
	}
	
}
