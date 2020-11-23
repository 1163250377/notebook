package com.shj.notebook.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shj.notebook.dao.NoteBookDao;
import com.shj.notebook.entity.NoteBook;

public class NoteBookDaoImpl extends BaseDao implements NoteBookDao{

	@Override
	public void updateNoteBook(Integer id, String content,String type) {
		executeUpdate("UPDATE notelist SET content = ?,type = ? WHERE id = ?",content,type,id);
	}
	@Override
	public void updateTitle(Integer id, String name) {
		executeUpdate("UPDATE notelist SET title = ? WHERE directory_id = ?",name,id);
	}
	@Override
	public NoteBook findOne(NoteBook of) {
		ResultSet rs = executeSql("SELECT id,title,create_time,update_time,content,type,directory_id FROM notelist WHERE directory_id = ?", of.getDirectoryId().toString());
		try {
			if(rs.next()){
				return new NoteBook(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5),rs.getString(6),rs.getInt(7));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public NoteBook save(NoteBook note) {

		try {
			if(note.getId() == null){
				executeUpdate("INSERT INTO notelist(title,create_time,update_time,content,type,directory_id) VALUES(?,?,?,?,?,?)",
						note.getTitle(),note.getCreateTime(),note.getUpdateTime(),note.getContent(),note.getType(),note.getDirectoryId().toString()
						);
			}else{
				ResultSet rs1= executeSql("SELECT id FROM notelist WHERE id = ?", note.getId().toString());
				if(rs1.next()){
					executeUpdate("UPDATE notelist SET title=?,create_time=?,update_time=?,content=?,type=?,directory_id=? WHERE id = ?",
							note.getTitle(),note.getCreateTime(),note.getUpdateTime(),note.getContent(),note.getType(),note.getDirectoryId().toString(),note.getId().toString());
				}
			}
			ResultSet rs = executeSql("SELECT id,title,create_time,update_time,content,type,directory_id FROM notelist WHERE directory_id = ?", note.getDirectoryId().toString());
			try {
				if(rs.next()){
					return new NoteBook(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
							rs.getString(5),rs.getString(6),rs.getInt(7));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void deleteById(Integer id) {
		executeUpdate("DELETE FROM notelist WHERE directory_id = ?", id.toString());
	}

	@Override
	public void closeDB() {
		close();
	}
	@Override
	public List<NoteBook> getAllNoteBook() {
		List<NoteBook> list = new ArrayList<NoteBook>();
		ResultSet rs = executeSql("SELECT id,title,create_time,update_time,content,type,directory_id FROM notelist");
		try {
			
			while(rs.next()){
				list.add(new NoteBook(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5),rs.getString(6),rs.getInt(7)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

}
