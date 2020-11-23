package com.shj.notebook;


import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.shj.notebook.main.ApplicationCode;


public class NotebookApplication {
	public static String dbName;
	public static void main(String[] args) throws IOException {
		//读取自定义数据库
		
		if(args.length > 0) {
			dbName = args[0];
		}else {
			dbName = "notebook.db";
			//检查数据库
			checkDB();
		}
		//启动可视化面板
		new ApplicationCode().appRun(args);
		
	}
	/**
	 * 检查数据库是否存在，不存在就创建
	 * @throws IOException
	 */
	public static void checkDB() throws IOException{
		String path = System.getProperty("user.dir")+"\\"+dbName;

		if(!Files.exists(Paths.get(path))){
			System.out.println("创建文件:"+path);
			DataInputStream in = new DataInputStream(NotebookApplication.class.getClassLoader().getResourceAsStream("notebook.db"));
			File file = new File(path);
			file.createNewFile();
			FileOutputStream out = new FileOutputStream(file);
			for (int i = in.read(); i!= -1; i = in.read()) {
				out.write(i);

			}
		    out.flush();
		    out.close();
		}
	}
}
