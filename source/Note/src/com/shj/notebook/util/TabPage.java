package com.shj.notebook.util;

import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import com.shj.notebook.controller.AppController;
import com.shj.notebook.entity.DataType;
import com.shj.notebook.entity.DirctoryProperty;
import com.shj.notebook.entity.NoteBook;
import com.shj.notebook.main.ApplicationCode;
import com.shj.notebook.service.NoteBookService;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

/***
 * 代表一个tab页
 * 
 * @author Administrator
 *
 */
public class TabPage {
	private DirctoryProperty clickItemName;
	private TabPane tabPane;
	private HTMLEditor dit;
	private Tab tab;
	private NoteBook note;
	public TabPage(AppController controller,NoteBook note) {
		super();
		this.note = note;
		this.clickItemName = controller.clickItemName;
		this.noteBookService = controller.noteBookService;
		this.tabPane = controller.tabPane;
		createTab("");
		setScrollTextHeight();
		onKeyPressed();
	}
	public TabPage(DirctoryProperty clickItemName,NoteBookService noteBookService,TabPane tabPane) {
		super();
		this.clickItemName = clickItemName;
		this.noteBookService = noteBookService;
		this.tabPane = tabPane;
		createTab("");
		setScrollTextHeight();
		onKeyPressed();
	}
	/***
	 * 工具栏可见性
	 */
	private NoteBookService noteBookService;
	private boolean visible = false;
	public void setHtmlText(String htmlText) {
		dit.setHtmlText(htmlText);
	}
	public String getHtmlText() {
		return dit.getHtmlText();
	}
	public void setTitleText(String value) {
		tab.setText(value);
	}
	public void setId(NoteBook note) {
		tab.setId(note.getDirectoryId().toString());
		dit.setId(note.getId().toString());
	}
	public String getId() {
		return dit.getId();
	}
	AnchorPane pane;
	/***
	 * 创建一个tab选项卡
	 * 
	 * @param name
	 */
	private void createTab(String name) {
		tab = new Tab(name);
		dit = new HTMLEditor();
		// 隐藏工具栏
		dit.setStyle(AppController.FONTFAMILY);
    	Node[] node = dit.lookupAll(".tool-bar").toArray(new Node[0]);
    	for(int i = 0;i<node.length;i++){
    		node[i].setVisible(false);
    		node[i].setManaged(false);
    	}
		AnchorPane.setBottomAnchor(dit, 0.0);
		AnchorPane.setTopAnchor(dit, 0.0);
		AnchorPane.setLeftAnchor(dit, 0.0);
		AnchorPane.setRightAnchor(dit, 0.0);
		pane = new AnchorPane(dit);
		pane.setStyle(AppController.FONTFAMILY);
		tab.setContent(pane);
		addTHMLContextMenu(dit);
		tabPane.getTabs().add(tab);
	}
	private void importImageToHtml(String path,String format) {
		String htmlText = dit.getHtmlText();
		StringBuilder sb = new StringBuilder();
		sb.append(htmlText.substring(0, htmlText.length()-14))
		.append("<p><img style=\"-webkit-user-drag:none;\" src=\"data:image/").append(format)
		.append(";base64,")
		.append(ImageBase64Converter.convertFileToBase64(path))
		.append("\"/></p></body></html>");
		dit.setHtmlText(sb.toString());
	}
	/***
	 * 添加编辑器的右键菜单
	 */
	private void addTHMLContextMenu(HTMLEditor edit) {
		ContextMenu textContext = new ContextMenu();
		MenuItem tool = new MenuItem("工具栏 [ Ctrl + T ]");
		MenuItem save = new MenuItem("保存   [ Ctrl + S ]");
		MenuItem importHtml = new MenuItem("导入文件/图片");
		MenuItem exportHtml = new MenuItem("导出");
		MenuItem asWebSite = new MenuItem("作为站点");
		asWebSite.setOnAction((ActionEvent event) -> {
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("作为站点");
			dialog.setHeaderText(" ");
			dialog.setContentText("网站地址");
			Optional<String> result = dialog.showAndWait();
			result.ifPresent((String str) -> {
				String name = str.trim();
				if (name.length() > 0) {
					String js = "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"utf-8\" /></head><body><noscript>无法运行脚本</noscript><script>window.open('"+name+"');</script></body></html>";
					String id = dit.getId();
					if (id != null && id.length() > 0) {
						note.setType("WEBSITE");
						noteBookService.updateNoteBook(Integer.valueOf(id), js,note.getType());
						dit.setHtmlText(js);
					}
				}
			});
		});
		tool.setOnAction((ActionEvent event) -> {
			visible = !visible;
			Node[] node = edit.lookupAll(".tool-bar").toArray(new Node[0]);
			for (int i = 0; i < node.length; i++) {
				node[i].setVisible(visible);
				node[i].setManaged(visible);
			}
		});
		save.setOnAction((ActionEvent event) -> {
			String id = edit.getId();
			if (id != null && id.length() > 0) {
				if(note.getType().equals("HTML")) {					
					noteBookService.updateNoteBook(Integer.valueOf(id), edit.getHtmlText(),note.getType());
				}
			}
		});
		importHtml.setOnAction((ActionEvent event) -> {
			FileChooser choose = new FileChooser();
			choose.setTitle("选择文件");
			File f = choose.showOpenDialog(ApplicationCode.getPrimaryStage());

			if (f != null && clickItemName.getType() == DataType.TEXT) {
				try {
					Path filePath = f.toPath();
					String strPath = filePath.toString();
					if(strPath.endsWith(".png")) {
						importImageToHtml(strPath, "png");
					}else if(strPath.endsWith(".gif")) {
						importImageToHtml(strPath, "gif");
					}else if(strPath.endsWith(".jpg")){
						importImageToHtml(strPath, "jpg");
					}else {
						byte[] by = Files.readAllBytes(filePath);
						String newStr = new String(by, "UTF-8");
						edit.setHtmlText(newStr);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		});
		exportHtml.setOnAction((ActionEvent event) -> {
			DirectoryChooser dirChooser = new DirectoryChooser();
			dirChooser.setTitle("选择保存目录");
			File dir = dirChooser.showDialog(ApplicationCode.getPrimaryStage());
			if (dir != null && clickItemName.getType() == DataType.TEXT) {
				try {
					String str = dir.toString() + "\\" + clickItemName.getName() + ".html";
					Files.write(Paths.get(str), edit.getHtmlText().getBytes());
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("");
					alert.setHeaderText("");
					alert.setContentText("导出完成");
					alert.showAndWait();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		textContext.getItems().add(tool);
		textContext.getItems().add(save);
		textContext.getItems().add(importHtml);
		textContext.getItems().add(exportHtml);
		textContext.getItems().add(asWebSite);
		textContext.setStyle(AppController.FONTFAMILY);
		edit.setContextMenu(textContext);
	}
	/***
	 * 自动调整输入区的高度
	 */
	private void setScrollTextHeight() {
		WebView m = (WebView) dit.lookupAll(".web-view").toArray(new Node[0])[0];
		m.setContextMenuEnabled(false);// 关闭右键
		double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		m.prefHeightProperty().set(height);
//		if (visible) {
//			m.prefHeightProperty().set(height - 25 - 70);
//		} else {
//			m.prefHeightProperty().set(height - 25);
//		}
	}
	/***
	 * 按下键盘时的事件
	 */
	private void onKeyPressed(){
		dit.addEventHandler(KeyEvent.KEY_PRESSED, evt -> {
			if (evt.isControlDown()) {
				if (evt.getCode() == KeyCode.S) {
					String id = dit.getId();
					if (id != null && id.length() > 0) {
						if(note.getType().equals("HTML")) {							
							noteBookService.updateNoteBook(Integer.valueOf(id), dit.getHtmlText(),note.getType());
						}
					}
				} else if (evt.getCode() == KeyCode.T) {
					// ctrl + t 工具栏
					visible = !visible;
					Node[] node = dit.lookupAll(".tool-bar").toArray(new Node[0]);
					for (int i = 0; i < node.length; i++) {
						node[i].setVisible(visible);
						node[i].setManaged(visible);
					}
					//tab之间的切换
				}else if(evt.getCode() == KeyCode.TAB){
					int size = tabPane.getTabs().size();
					int index = tabPane.getSelectionModel().getSelectedIndex();
					if(index+1==size) {
						tabPane.getSelectionModel().selectFirst();
					}else {
						tabPane.getSelectionModel().selectNext();
					}
					tabPane.requestFocus();
					tabPane.getSelectionModel().getSelectedItem().getContent().requestFocus();
				}

			}else {				
				if(evt.getCode() == KeyCode.ESCAPE) {
					if(!isFull) {
						ApplicationCode.getPrimaryStage().setFullScreen(true);
						isFull = true;
					}else {
						isFull = false;
					}
				}
			}
		});
	}
	private static boolean isFull = false;
	/***
	 * 选中当前页
	 */
	public void select() {
		tabPane.getSelectionModel().select(tab);
	}
	/***
	 * 删除当前页
	 */
	public void remove() {
		tabPane.getTabs().remove(tab);
	}
}
