package com.shj.notebook.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.shj.notebook.controller.AppController;
import com.shj.notebook.dao.NoteBookDao;
import com.shj.notebook.dao.impl.NoteBookDaoImpl;
import com.shj.notebook.entity.NoteBook;

public class NoteBookService {
	private NoteBookDao notebookDao = new NoteBookDaoImpl();

	public Optional<NoteBook> getNoteBookByDirctoryId(Integer id) {
		NoteBook note = new NoteBook();
		note.setDirectoryId(id);
		NoteBook n = notebookDao.findOne(note);
		notebookDao.closeDB();
		return Optional.ofNullable(n);
	}
	public void updateNoteBook(Integer id,String content,String type) {
		notebookDao.updateNoteBook(id,content,type);
		notebookDao.closeDB();
	}
	//插入一条笔记信息
	public Optional<NoteBook> insertNote(String name, int id) {
		NoteBook note = new NoteBook();
		note.setTitle(name);
		String time = LocalDateTime.now().toString();
		note.setCreateTime(time);
		note.setUpdateTime(time);
		note.setDirectoryId(id);
		note.setType("HTML");
		//note.setRemark("<html dir=\"ltr\"><head></head><body contenteditable=\"true\"></body></html>");
		note.setContent("<html dir=\"ltr\"><head></head><body contenteditable=\"true\" style=\"background-color:"+AppController.GLOBAL_COLOR+"\"><p><font face=\"Consolas\">"+name+"</font></p></body></html>");
		NoteBook book = notebookDao.save(note);
		notebookDao.closeDB();
		return Optional.of(book);
	}
	public void deleteNoteById(Integer id) {
		notebookDao.deleteById(id);
		notebookDao.closeDB();
	}
	public void updateName(Integer id, String name) {
		notebookDao.updateTitle(id, name);
		notebookDao.closeDB();
	}
	public List<NoteBook> getAllNoteBook() {
		List<NoteBook> list = notebookDao.getAllNoteBook();
		notebookDao.closeDB();
		return list;
	}
}
