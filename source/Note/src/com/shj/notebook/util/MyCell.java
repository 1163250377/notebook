package com.shj.notebook.util;

import com.shj.notebook.NotebookApplication;
import com.shj.notebook.controller.AppController;
import com.shj.notebook.entity.DirctoryProperty;

import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
/***
 * 代表一个tree节点
 * @author Administrator
 *
 */
public class MyCell implements Callback<TreeView<DirctoryProperty>, TreeCell<DirctoryProperty>> {
	AppController controller;
	public MyCell(AppController personOverviewController) {
		this.controller = personOverviewController;
	}
	private Integer index = 0;
	@Override
	public TreeCell<DirctoryProperty> call(TreeView<DirctoryProperty> param) {
		
		TreeCell<DirctoryProperty> t = new TreeCell<DirctoryProperty>() {
			//这个后执行 将除了选中节点的其他节点恢复样式
			@Override
			public void updateIndex(int i) {
				super.updateIndex(i);
				if(index!=i) {
					setStyle(AppController.FONTFAMILY);
				}
			}
			//这个先执行 获取选中的节点下标 并为选中的节点添加选中样式
			@Override
			public void updateSelected(boolean selected) {
				super.updateSelected(selected);
				if(selected==true) {
					setStyle("-fx-font:12 Consolas;-fx-background-color:#99CC99;");
					index = getIndex();
				}
			}

			@Override
			protected void updateItem(DirctoryProperty item, boolean empty) {

				super.updateItem(item, empty);
				if (item == null || empty) {
					setText("");
					setGraphic(null);
					setStyle(AppController.FONTFAMILY);
					return;
				}
				/*
				 * 拦截左侧树三角图标的事件
				 */
				getDisclosureNode().addEventFilter(MouseEvent.ANY, ev -> {
					if(ev.getEventType() != MouseEvent.MOUSE_CLICKED) {						
						ev.consume();
					}
				});
				//设置箭头的图片
//				setDisclosureNode(
//						new ImageView(new Image(NotebookApplication.class.getResourceAsStream("/static/image/dir.png"),
//								16.2, 21.6, true, true)));
				//设置节点的值
				setText(item.getName());
				//设置节点的图片
				switch (item.getType()) {
				case DIRCTORY:
					setGraphic(new ImageView(
							new Image(NotebookApplication.class.getResourceAsStream("/static/image/dir.png"), 18, 24, true, true)));
					break;
				case TEXT:
					setGraphic(new ImageView(
							new Image(NotebookApplication.class.getResourceAsStream("/static/image/text.png"), 18, 24, true, true)));
					break;
				case DB:
					setGraphic(new ImageView(
							new Image(NotebookApplication.class.getResourceAsStream("/static/image/db.png"), 18, 24, true, true)));
					break;
				default:
					break;
				}
			}
		};
		return t;
	}

}
