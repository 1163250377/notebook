package com.shj.notebook.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import com.shj.notebook.entity.AlertType;
import com.shj.notebook.entity.CutBean;
import com.shj.notebook.entity.DataType;
import com.shj.notebook.entity.Dirctory;
import com.shj.notebook.entity.DirctoryProperty;
import com.shj.notebook.entity.NoteBook;
import com.shj.notebook.service.DirctoryService;
import com.shj.notebook.service.NoteBookService;
import com.shj.notebook.util.MyCell;
import com.shj.notebook.util.NodeOperation;
import com.shj.notebook.util.TabPage;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class AppController {
	/***
	 * 左侧树
	 */
	@FXML
	public TreeView<DirctoryProperty> treeView;
	/***
	 * 左侧树的菜单
	 */
	@FXML
	public ContextMenu contextMenu;
	/***
	 * tab面板
	 */
	@FXML
	public TabPane tabPane;
	@FXML
	public SplitPane splitPane;
	@FXML
	public AnchorPane leftAnchorPane;
	/***
	 * 当前点击的节点
	 */
	public DirctoryProperty clickItemName;
	/***
	 * 负责剪切功能的对象
	 */
	private CutBean cut = new CutBean();
	/***
	 * tab面板的子项tab页集合
	 */
	public HashMap<Integer, TabPage> pages = new HashMap<Integer, TabPage>();
	/***
	 * 右键菜单的子项集合
	 */
	private Map<String,MenuItem> menuItem = new HashMap<String,MenuItem>();
	public DirctoryService dirctoryService = new DirctoryService();
	public NoteBookService noteBookService = new NoteBookService();
	/***
	 * 控制树的显示隐藏
	 */
	public boolean treeVisible = true;
	public static String GLOBAL_COLOR = "#B3E6B3";
	public static String FONTFAMILY = "-fx-font:12 Consolas;-fx-background-color:"+GLOBAL_COLOR;
	@FXML
	public void initialize() {

		addDeleteListener();
		treeInitData();
		loadCurrentNodeChild(clickItemName);
		addtabPaneContextMenu();
		contextMenuInit();
		treeView.setStyle(FONTFAMILY);
		tabPane.setStyle(FONTFAMILY);
		contextMenu.setStyle(FONTFAMILY);
		// 在工具栏中添加按钮
		// ToolBar n = (ToolBar)edit.lookup(".top-toolbar");
		// n.getItems().add(new Button("添加图片"));
	}

	/***
	 * tab页添加右键菜单
	 */
	private void addtabPaneContextMenu() {
		ContextMenu textContext = new ContextMenu();
		MenuItem closeAll = new MenuItem("关闭所有");
		MenuItem closeLeftTree = new MenuItem("隐藏左侧树");
		closeAll.setOnAction((ActionEvent event) -> {
			tabPane.getTabs().clear();
			pages.clear();
		});
		closeLeftTree.setOnAction((ActionEvent event) -> {
			visible(closeLeftTree);
		});
		textContext.getItems().add(closeAll);
		textContext.getItems().add(closeLeftTree);
		textContext.setStyle(FONTFAMILY);
		tabPane.setContextMenu(textContext);
	}

	/***
	 * 树的显示与隐藏
	 */
	public void visible(MenuItem closeLeftTree) {
		if (treeVisible) {
			splitPane.getItems().remove(leftAnchorPane);
			treeVisible = false;
			closeLeftTree.setText("显示左侧树");
		} else {
			splitPane.setDividerPosition(0, 0.2);
			splitPane.getItems().add(0, leftAnchorPane);
			treeVisible = true;
			closeLeftTree.setText("隐藏左侧树");
		}
	}

	/**
	 * 添加删除tab页时的监听操作
	 */
	public void addDeleteListener() {
		// tab上显示删除标签
		tabPane.setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
		// tab个数改变时 移除对应的tab实例
		tabPane.getTabs().addListener(new ListChangeListener<Tab>() {
			@Override
			public void onChanged(Change<? extends Tab> c) {
				if (c.next() && c.wasRemoved()) {
					List<? extends Tab> li = c.getRemoved();
					Tab tab = li.get(0);
					pages.remove(Integer.valueOf(tab.getId()));
				}

			}
		});
	}

	/**
	 * 初始化根节点
	 */
	public void treeInitData() {
		treeView.setCellFactory(new MyCell(this));
		DirctoryProperty d = new DirctoryProperty(0, "存储库", 1,0, DataType.DB);
		d.setId(0);
		clickItemName = d;
		TreeItem<DirctoryProperty> rootItem = new TreeItem<DirctoryProperty>(d);
		rootItem.setExpanded(true);
		// 创建TreeView
		ObjectProperty<TreeItem<DirctoryProperty>> rootItemProPerty = new SimpleObjectProperty<TreeItem<DirctoryProperty>>(
				rootItem);
		treeView.rootProperty().bind(rootItemProPerty);
		NodeOperation.map.put(d.getId(), rootItem);
		// tab页切换
		treeView.addEventHandler(KeyEvent.KEY_PRESSED, evt -> {
			if (evt.isControlDown()) {
				if (evt.getCode() == KeyCode.TAB) {
					int size = tabPane.getTabs().size();
					int index = tabPane.getSelectionModel().getSelectedIndex();
					if (index + 1 == size) {
						tabPane.getSelectionModel().selectFirst();
					} else {
						tabPane.getSelectionModel().selectNext();
					}
				}

			}else if(evt.getCode() == KeyCode.SPACE||evt.getCode() == KeyCode.ENTER) {
				TreeItem<DirctoryProperty> item = treeView.getSelectionModel().getSelectedItem();
				if(item!=null) {					
					clickItemName = item.getValue();
					loadCurrentNodeChild(item.getValue());
					item.setExpanded(!item.isExpanded());
					if(clickItemName.getType()==DataType.TEXT) {
						getNoteBook();
					}
				}
			}
		});
	}

	/**
	 * 根据当前节点加载子节点
	 * 
	 * @param nodeid
	 */
	public void loadCurrentNodeChild(DirctoryProperty clickItemName) {
		if (!clickItemName.isLoadNode() && clickItemName.getType() != DataType.TEXT) {
			List<DirctoryProperty> dirList = dirctoryService.getAllDirctoryById(clickItemName.getId());
			getTreeNode(dirList, NodeOperation.map.get(clickItemName.getId()), clickItemName);
			clickItemName.setLoadNode(true);
		}
	}

	/***
	 * 将list转化为树节点
	 *
	 * @param dirList
	 * @param parentItem
	 * @param parent
	 * @return
	 */
	public TreeItem<DirctoryProperty> getTreeNode(List<DirctoryProperty> dirList, TreeItem<DirctoryProperty> parentItem,
			DirctoryProperty parent) {
		ObservableList<TreeItem<DirctoryProperty>> obser = parentItem.getChildren();
		obser.clear();
		for (int i = 0; i < dirList.size(); i++) {
			DirctoryProperty dir = dirList.get(i);
			TreeItem<DirctoryProperty> item = new TreeItem<DirctoryProperty>(dir);
			if (dir.getType() != DataType.TEXT) {
				// 如果目录中存在子目录，就加入一条数据,保证三角箭头的出现
				if (dir.getChild_count() > 0) {
					item.getChildren().add(new TreeItem<DirctoryProperty>());
				}
			}
			obser.add(item);
			NodeOperation.map.put(dir.getId(), item);
		}
		return parentItem;
	}

	/***
	 * 添加树
	 * 
	 * @param dir
	 */
	public void treeDataAdd(DirctoryProperty dir) {
		TreeItem<DirctoryProperty> node = new TreeItem<DirctoryProperty>(dir);
		NodeOperation.map.put(dir.getId(), node);
		NodeOperation.addTreeNode(node, clickItemName);
		treeView.refresh();
	}

	/**
	 * 删除树
	 * 
	 * @param nodeId
	 */
	public void treeDataDelete(Integer nodeId) {
		NodeOperation.deleteTreeNode(nodeId);
		treeView.refresh();
	}

	/***
	 * 更新树
	 * 
	 * @param id
	 * @param name
	 */
	public void treeDataUpdate(Integer id, String name) {
		NodeOperation.updateTreeNode(id, name);
		treeView.refresh();
	}
	/***
	 * 右键菜单触发时调用
	 */
	public void treeContextMenu() {
		switch (clickItemName.getType()) {
		case DIRCTORY:
			showContextMenu("新建目录", "新建文件", "删除目录", "重命名目录", "上移", "下移", "粘贴", "剪切");
			break;
		case DB:
			showContextMenu("新建目录");
			break;
		case TEXT:
			showContextMenu("删除", "重命名", "上移", "下移", "粘贴", "剪切");
			break;
		default:
			break;
	   }
	}

	/***
	 * 初始化右键菜单
	 */
	public void contextMenuInit() {
		MenuItem xinjian = addMenuItem("新建目录", AlertType.INFO, (name) -> {
			Optional<Dirctory> opt = dirctoryService.addDirctory(name, clickItemName);
			opt.ifPresent((dir) -> {
				treeDataAdd(new DirctoryProperty(dir.getId(), dir.getName(), dir.getDirOrder(),dir.getParentId(),
						DataType.DIRCTORY));
			});
			return "";
		});
		MenuItem xinjian2 = addMenuItem("新建文件", AlertType.INFO, (name) -> {
			Optional<Dirctory> dir = dirctoryService.insertNote(name, clickItemName);
			dir.ifPresent((d) -> {
				noteBookService.insertNote(name, d.getId());
				treeDataAdd(new DirctoryProperty(d.getId(), d.getName(), d.getDirOrder(),d.getParentId(), DataType.TEXT));
			});
			return "";
		});
		MenuItem del = addMenuItem("删除", AlertType.WARNING, (name) -> {
			if(dirctoryService.deleteDirctory(clickItemName,true)) {				
				treeDataDelete(clickItemName.getId());
			}
			return "";
		});
		MenuItem del2 = addMenuItem("删除", AlertType.WARNING, (name) -> {
			dirctoryService.deleteDirctory(clickItemName,false);
			noteBookService.deleteNoteById(clickItemName.getId());
			TabPage tabPage = pages.get(clickItemName.getId());
			if(tabPage!=null) {				
				tabPage.remove();
			}
			treeDataDelete(clickItemName.getId());
			return "";
		});
		MenuItem remame = addMenuItem("重命名", AlertType.INFO, (name) -> {
			dirctoryService.updateName(clickItemName.getId(), name);
			treeDataUpdate(clickItemName.getId(), name);
			return "";
		});
		
		MenuItem remame2 = addMenuItem("重命名", AlertType.INFO, (name) -> {
			dirctoryService.updateName(clickItemName.getId(), name);
			noteBookService.updateName(clickItemName.getId(), name);
			treeDataUpdate(clickItemName.getId(), name);
			pages.get(clickItemName.getId()).setTitleText(name);
			return "";
		});

		MenuItem shangyi = new MenuItem("上移");
		shangyi.setOnAction((ActionEvent event) -> {
			moveup();
		});
		MenuItem xiayi = new MenuItem("下移");
		xiayi.setOnAction((ActionEvent event) -> {
			movedown();
		});
		MenuItem zhantie = new MenuItem("粘贴");
		zhantie.setOnAction((ActionEvent event) -> {
			cut.setCut(false);
			cut.setTarget(clickItemName);
			cut.work(dirctoryService,this);
		});
		
		MenuItem jianqie = new MenuItem("剪切");
		jianqie.setOnAction((ActionEvent event) -> {
			cut.setCut(true);
			cut.setSource(clickItemName);
		});
		menuItem.put("新建目录", xinjian);
		menuItem.put("新建文件", xinjian2);
		menuItem.put("删除目录", del);
		menuItem.put("删除", del2);
		menuItem.put("重命名目录", remame);
		menuItem.put("重命名", remame2);
		menuItem.put("上移", shangyi);
		menuItem.put("下移", xiayi);
		menuItem.put("粘贴",zhantie);
		menuItem.put("剪切",jianqie);
		contextMenu.setAutoHide(true);
	
	}
	
	// 右键菜单项调用的方法
	public MenuItem addMenuItem(String menuName, AlertType alertType,Function<String, String> fun) {
		MenuItem item = new MenuItem(menuName);
		item.setOnAction((ActionEvent event) -> {
			Optional<String> result = TextInputDialog(menuName, alertType, clickItemName.getName());
			result.ifPresent((String str) -> {
				String name = str.trim();
				if (name.length() > 0) {
					fun.apply(name);
				}
			});
		});
		return item;
	}

	public Optional<String> TextInputDialog(String title, AlertType alertType, String defaultValue) {
		if (alertType == AlertType.INFO) {
			TextInputDialog dialog = new TextInputDialog(defaultValue);
			dialog.setTitle(title);
			dialog.setHeaderText(" ");
			dialog.setContentText("名称");

			return dialog.showAndWait();
		} else {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle(title);
			alert.setHeaderText("");
			alert.setContentText("删除?");
			Optional<ButtonType> bt = alert.showAndWait();
			if (bt.isPresent()) {
				return Optional.of(bt.get().getText());
			}
			return Optional.empty();
		}

	}
	public void moveup() {
		int order = clickItemName.getDirOrder();
		dirctoryService.updateOrder(clickItemName.getId(),clickItemName.getParentId(),order-1,order);
		TreeItem<DirctoryProperty> item = NodeOperation.map.get(clickItemName.getId());
		DirctoryProperty dir = item.getParent().getValue();
		dir.setLoadNode(false);
		loadCurrentNodeChild(dir);
	}
	public void movedown() {
		int order = clickItemName.getDirOrder();
		dirctoryService.updateOrder(clickItemName.getId(),clickItemName.getParentId(),order+1,order);
		TreeItem<DirctoryProperty> item = NodeOperation.map.get(clickItemName.getId());
		DirctoryProperty dir = item.getParent().getValue();
		dir.setLoadNode(false);
		loadCurrentNodeChild(dir);
	}
	/***
	 * 双击节点树时 加载数据
	 */
	public void onMouseClicked(MouseEvent obj) {
		TreeItem<DirctoryProperty> item = treeView.getSelectionModel().getSelectedItem();
		if (Objects.nonNull(item)) {
			clickItemName = item.getValue();
			if(clickItemName.getType()==DataType.TEXT && obj.getClickCount()==2) {
				getNoteBook();
			}
			loadCurrentNodeChild(item.getValue());
		}
	}
	public void getNoteBook() {
		Optional<NoteBook> note = noteBookService.getNoteBookByDirctoryId(clickItemName.getId());
		note.ifPresent((notebook) -> {
			String txt = notebook.getContent();
			TabPage page = receicePage(notebook);
			page.setHtmlText(txt);
			page.select();
		});
	}

	/***
	 * 获取一个tab页
	 * 
	 * @param note
	 * @return
	 */
	private TabPage receicePage(NoteBook note) {
		Integer dirId = note.getDirectoryId();
		TabPage page = pages.get(dirId);
		if (page == null) {
			page = new TabPage(this, note);
			pages.put(dirId, page);
		}
		page.setTitleText(note.getTitle());
		page.setId(note);

		return page;
	}
	/**
	 * "新建目录",
		"新建文件",
		"删除目录",
		"删除",
		"重命名目录",
		"重命名",
		"上移",
		"下移",
		"粘贴",
		"剪切"
	 * @param items
	 */
	public void showContextMenu(String... items) {
		if(cut.isCut() && clickItemName.getType()==DataType.DIRCTORY && clickItemName.getId() != cut.getSource().getParentId()) {
			//点击了剪切功能后 启用粘贴功能
			menuItem.get("粘贴").setDisable(false);
		}else {
			menuItem.get("粘贴").setDisable(true);
		}
		contextMenu.getItems().clear();
		for (String string : items) {
			contextMenu.getItems().add(menuItem.get(string));
		}
		contextMenu.setAutoHide(true);
	}
}
