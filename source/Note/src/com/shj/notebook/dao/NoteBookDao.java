package com.shj.notebook.dao;


import java.util.List;

import com.shj.notebook.entity.NoteBook;

public interface NoteBookDao{
	//@Query(value="UPDATE NoteBook n SET n.remark = :content WHERE n.id = :id")
	void updateNoteBook(Integer id, String content,String type);

	NoteBook findOne(NoteBook of);

	NoteBook save(NoteBook note);

	void deleteById(Integer id);
	
	void updateTitle(Integer id, String name);
	
	void closeDB();

	List<NoteBook> getAllNoteBook();
}
