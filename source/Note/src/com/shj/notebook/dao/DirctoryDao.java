package com.shj.notebook.dao;

import java.util.List;

import com.shj.notebook.entity.Dirctory;

public interface DirctoryDao{
	//@Query(value="UPDATE Dirctory n SET n.name = :name WHERE n.id = :id")
	void updateName(Integer id,String name);

	Dirctory save(Dirctory dir);

	List<Dirctory> findAll(Integer id);

	void deleteById(Integer id);

	List<Dirctory> findTextDir(Integer id);
	
	void closeDB();

	int childCount(Integer id);

	int updateOrder(Integer parentId,Integer newOrder,Integer oldOrder);

	void updateOrderById(Integer id, Integer newOrder);
	
	void updateParentId(Integer parentId, Integer id);
}
