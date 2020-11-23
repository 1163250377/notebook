package com.shj.notebook.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.shj.notebook.NotebookApplication;

/***
 * 数据库连接基类
 * @author ShuHuajian
 *
 */
public abstract class BaseDao {
	protected Connection con ;//连接对象
	protected PreparedStatement pstm;//操作对象
	protected ResultSet rs;//结果集对象
	//private static String driver;
	//private static String url;

	/***
	 * 获取连接
	 * @return 数据库连接对象
	 */
	public static Connection getConnection(){
		try {
			Class.forName("org.sqlite.JDBC");
			return DriverManager.getConnection("jdbc:sqlite:"+NotebookApplication.dbName);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	/***
	 * 获取结果集
	 * @param sql 要执行的sql语句
	 * @param param 传入的参数
	 */
	public ResultSet executeSql(String sql,Object... param){
		try {
			if(con == null || con.isClosed()){
				con = getConnection();
			}
			pstm = con.prepareStatement(sql);
			for(int i = 0; i< param.length;i++){
				pstm.setObject(i+1, param[i]);
			}
			rs = pstm.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	public int executeUpdate(String sql,Object... param){
		try {
			if(con == null || con.isClosed()){
				con = getConnection();
			}
			pstm = con.prepareStatement(sql);
			for(int i = 0; i< param.length;i++){
				pstm.setObject(i+1, param[i]);
			}
			return pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	/***
	 * 关闭资源及数据库连接
	 */
	public void close(){
		try {
			if(rs != null){
				rs.close();
			}
			if(pstm != null){
				pstm.close();
			}
			if(con != null){
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
