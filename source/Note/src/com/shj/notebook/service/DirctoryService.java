package com.shj.notebook.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.shj.notebook.dao.DirctoryDao;
import com.shj.notebook.dao.impl.DirctoryDaoImpl;
import com.shj.notebook.entity.DataType;
import com.shj.notebook.entity.Dirctory;
import com.shj.notebook.entity.DirctoryProperty;


public class DirctoryService {

	private DirctoryDao dirctoryDao = new DirctoryDaoImpl();

	public Optional<Dirctory> addDirctory(String name, DirctoryProperty dirctory) {
		int count = dirctoryDao.childCount(dirctory.getId());
		Dirctory dir = new Dirctory(name,count+1 ,dirctory.getId(), DataType.DIRCTORY);
		Dirctory d = dirctoryDao.save(dir);
		dirctoryDao.closeDB();
		return Optional.of(d);
	}

	public Optional<Dirctory> insertNote(String name, DirctoryProperty dirctory) {
		int count = dirctoryDao.childCount(dirctory.getId());
		Dirctory dir = new Dirctory(name, count+1,dirctory.getId(), DataType.TEXT);
		Dirctory d = dirctoryDao.save(dir);
		dirctoryDao.closeDB();
		return Optional.of(d);
	}

	public List<DirctoryProperty> getAllDirctoryById(Integer id) {
		List<Dirctory> dirs = dirctoryDao.findAll(id);
		List<DirctoryProperty> list = new ArrayList<DirctoryProperty>();
		if(dirs != null) {
			dirs.forEach((dir)->{
				int c = dirctoryDao.childCount(dir.getId());
				DirctoryProperty dp = new DirctoryProperty(dir.getId(), dir.getName(), dir.getDirOrder(),dir.getParentId(),dir.getType());
				dp.setChild_count(c);
				list.add(dp);
			});
		}
		dirctoryDao.closeDB();
		return list;
	}
	/***
	 * 根据父目录获取下面所有TXT类型的文件
	 * @return
	 */
	public List<DirctoryProperty> getTextDirctoryByDirId(Integer id) {
		List<Dirctory> dirs = dirctoryDao.findTextDir(id);
		List<DirctoryProperty> list = new ArrayList<DirctoryProperty>();
		if(dirs != null) {
			dirs.forEach((dir)->{
				list.add(new DirctoryProperty(dir.getId(), dir.getName(), dir.getDirOrder(),dir.getParentId(),dir.getType()));
			});
		}
		dirctoryDao.closeDB();
		return list;
	}

	/***
	 * 成功返回true
	 * @param dir
	 * @param is 是否判断包含子目录,有内容时不允许删除
	 * @return
	 */
	public boolean deleteDirctory(DirctoryProperty dir,boolean is) {
		int childCount = 0;
		if(is) {
			childCount = dirctoryDao.findAll(dir.getId()).size();
		}
		if(childCount == 0) {
			dirctoryDao.deleteById(dir.getId());
			//删除后对目录进行排序
			orderByParentId(dir.getParentId());
			dirctoryDao.closeDB();
			return true;
		}
		return false;
	}

	public void updateName(int id,String name) {
		dirctoryDao.updateName(id, name);
		dirctoryDao.closeDB();
	}
	/***
	 * 
	 * @param id
	 * @param parentId
	 * @param newOrder
	 * @param oldOrder
	 */
	public void updateOrder(Integer id,Integer parentId,Integer newOrder,Integer oldOrder) {
		int upc = dirctoryDao.updateOrder(parentId, oldOrder,newOrder);
		if(upc==1) {
			dirctoryDao.updateOrderById(id,newOrder);
		}
		dirctoryDao.closeDB();
	}

	public void updateParentId(Integer targetId, Integer sourceId) {
		int count = dirctoryDao.childCount(targetId);
		dirctoryDao.updateParentId(targetId, sourceId);
		dirctoryDao.updateOrderById(sourceId, count+1);
		dirctoryDao.closeDB();
	}
	/***
	 * 根据父节点id,对子节点排序下标从新整理排序
	 * @param id
	 */
	public void orderByParentId(Integer id) {
		List<Dirctory> dirs = dirctoryDao.findAll(id);
		for (int i = 0,j = dirs.size(); i < j; i++) {
			Dirctory dir = dirs.get(i);
			dirctoryDao.updateOrderById(dir.getId(),i+1);
		}
		dirctoryDao.closeDB();
	}
}
