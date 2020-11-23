package com.shj.notebook.entity;

import com.shj.notebook.controller.AppController;
import com.shj.notebook.service.DirctoryService;
import com.shj.notebook.util.NodeOperation;

import javafx.scene.control.TreeItem;

/***
 * 剪切
 * @author Administrator
 *
 */
public class CutBean {
	/***
	 * 是否开启了剪切功能
	 */
	private boolean isCut;
	/***
	 * 剪切的内容
	 */
	private DirctoryProperty source;
	/**
	 * 目标位置
	 */
	private DirctoryProperty target;
	public boolean isCut() {
		return isCut;
	}
	public void setCut(boolean isCut) {
		this.isCut = isCut;
	}
	public DirctoryProperty getSource() {
		return source;
	}
	public void setSource(DirctoryProperty source) {
		this.source = source;
	}
	public DirctoryProperty getTarget() {
		return target;
	}
	public void setTarget(DirctoryProperty target) {
		this.target = target;
	}
	/***
	 * 开始剪切
	 * @param dirctoryService
	 */
	public void work(DirctoryService dirctoryService,AppController app) {
		//将源文件 修改到 目标文件中
		dirctoryService.updateParentId(target.getId(), source.getId());
		//更新显示  更新源
		TreeItem<DirctoryProperty> item = NodeOperation.map.get(source.getId());
		DirctoryProperty dir = item.getParent().getValue();
		dir.setLoadNode(false);
		app.loadCurrentNodeChild(dir);
		//更新目标
		target.setLoadNode(false);
		app.loadCurrentNodeChild(target);
		//更新 源的排序下标
		dirctoryService.orderByParentId(dir.getId());
		
	}
	
}
