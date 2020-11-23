package com.shj.notebook.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shj.notebook.dao.DirctoryDao;
import com.shj.notebook.entity.Dirctory;

public class DirctoryDaoImpl extends BaseDao implements DirctoryDao{

	@Override
	public void updateName(Integer id, String name) {
		executeUpdate("UPDATE dirctory SET name = ? WHERE id = ?",name,id.toString());
	}

	@Override
	public Dirctory save(Dirctory dir) {

		try {
			String type = dir.getType().toString();
			Integer id =dir.getId();
			if(id == null){
				executeUpdate("INSERT INTO dirctory(name,dir_order,parent_id,type) VALUES(?,?,?,?)",
						dir.getName(),dir.getDirOrder(),dir.getParentId(),type);
				
				ResultSet rs = executeSql("SELECT id,name,dir_order,parent_id,type FROM dirctory WHERE name = ? ORDER BY id desc", dir.getName());
				if(rs.next()){
					return new Dirctory(rs.getInt(1), rs.getString(2),rs.getInt(3), rs.getInt(4), rs.getString(5));
				}
				
			}else{
				ResultSet rs1= executeSql("SELECT id FROM dirctory WHERE id = ?", id);
				if(rs1.next()){
					executeUpdate("UPDATE dirctory SET name = ?,parent_id = ?,type = ? WHERE id = ?",
							dir.getName(),dir.getParentId(),type,dir.getId());
					return new Dirctory(dir.getId(), dir.getName(), dir.getDirOrder(),dir.getParentId(), type);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Dirctory> findAll(Integer id) {// WHERE type = 'DIRCTORY'
		List<Dirctory> list = new ArrayList<Dirctory>();
		ResultSet rs = executeSql("SELECT id,name,dir_order,parent_id,type FROM dirctory WHERE parent_id = ? ORDER BY dir_order ASC",id);
		try {
			if(rs!=null){
				while(rs.next()){
					list.add(new Dirctory(rs.getInt(1), rs.getString(2),rs.getInt(3), rs.getInt(4), rs.getString(5)));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@Override
	public List<Dirctory> findTextDir(Integer id) {
		List<Dirctory> list = new ArrayList<Dirctory>();
		ResultSet rs = executeSql("SELECT id,name,dir_order,parent_id,type FROM dirctory WHERE type = 'TEXT' AND parent_id = ? ORDER BY dir_order ASC",id.toString());
		try {
			while(rs.next()){
				list.add(new Dirctory(rs.getInt(1), rs.getString(2), rs.getInt(3),rs.getInt(4), rs.getString(5)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	@Override
	public void deleteById(Integer id) {
		executeUpdate("DELETE FROM dirctory WHERE id = ?", id.toString());
	}

	@Override
	public void closeDB() {
		close();
	}

	@Override
	public int childCount(Integer id) {
		ResultSet rs = executeSql("SELECT COUNT(1) FROM dirctory WHERE parent_id = ?", id);
		try {
			if(rs!=null){
				while(rs.next()){
					return rs.getInt(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int updateOrder(Integer parentId, Integer newOrder,Integer oldOrder) {
		return executeUpdate("UPDATE dirctory SET dir_order = ? WHERE parent_id = ? AND dir_order = ?",newOrder,parentId,oldOrder);
	}

	@Override
	public void updateOrderById(Integer id, Integer newOrder) {
		executeUpdate("UPDATE dirctory SET dir_order = ? WHERE id = ?",newOrder,id);
	}
	@Override
	public void updateParentId(Integer parentId, Integer id) {
		executeUpdate("UPDATE dirctory SET parent_id = ? WHERE id = ?",parentId,id);
	}
}
