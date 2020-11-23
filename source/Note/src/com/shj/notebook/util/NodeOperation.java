package com.shj.notebook.util;

import java.util.HashMap;

import com.shj.notebook.entity.DirctoryProperty;
import javafx.scene.control.TreeItem;

/***
 * 执行节点操作   添加  删除   修改
 * @author shj
 *
 */
public class NodeOperation {
	/***
	 * 保存所有的节点
	 */
	public static HashMap<Integer,TreeItem<DirctoryProperty>> map = new HashMap<Integer,TreeItem<DirctoryProperty>>();
	
	/***
	 * 为节点添加节点
	 * @param newNode 新的节点,可能需要添加到node中去的
	 * @param clickItemName 右键的节点
	 * @return
	 */
	public static void addTreeNode(TreeItem<DirctoryProperty> newNode,DirctoryProperty clickItemName){
		
		map.get(clickItemName.getId()).getChildren().add(newNode);
	}
	/***
	 * 删除某一个节点
	 * @param nodeId
	 */
	public static void deleteTreeNode(Integer nodeId) {
		TreeItem<DirctoryProperty> item = map.get(nodeId);
		item.getParent().getChildren().remove(item);
	}
	/***
	 * 修改某一个节点的名字
	 * @param id
	 * @param name
	 */
	public static void updateTreeNode(Integer id,String name) {
		TreeItem<DirctoryProperty> item = map.get(id);
		item.getValue().setName(name);
	}
	
}
