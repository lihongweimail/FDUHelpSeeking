package cn.edu.fudan.se.helpseeking.test;
import java.sql.*;
import java.util.Calendar;

import cn.edu.fudan.se.helpseeking.bean.Basic.Kind;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.InteractionEvent;
import cn.edu.fudan.se.helpseeking.util.CommUtil;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;
import cn.edu.fudan.se.helpseeking.util.FileHelper;




public class testSQLite {

	/**
	  * @param args
	  */
	 public static void main(String[] args) {
	  // TODO Auto-generated method stub
	  try {
	   long start = System.currentTimeMillis();
	   // 连接SQLite的JDBC
	   Class.forName("org.sqlite.JDBC");

	   // 建立一个数据库名zieckey.db的连接，如果不存在就在当前目录下创建之
	   //红色部分路径要求全小写，大写会报错
	   
	   String databasePath=CommUtil.getPluginCurrentPath();
		String pathStr= "jdbc:sqlite:"+databasePath+"/helpseeking.db";
	   
	   Connection conn = DriverManager
	     .getConnection(pathStr);
	   long end = System.currentTimeMillis();
	   System.out.println("创建数据库文件并连接耗费时间：" + (end - start));
	   
	   conn.close();
	   
	   start = System.currentTimeMillis();
	  
	   
	   //conn = DriverManager.getConnection("jdbc:sqlite://Users/Grand/temp/test.db"); 
	   conn = DriverManager.getConnection(pathStr); 
		  
	   end = System.currentTimeMillis();
	   System.out.println("数据库连接耗费时间：" + (end - start));
	   
	   start = System.currentTimeMillis();
	   Statement stat = conn.createStatement();
	   // 创建一个表，两列
	   
//	   String sqlString="CREATE TABLE IF NOT EXISTS  breakpoint (  id int(11) NOT NULL PRIMARY KEY,	type varchar(245) DEFAULT NULL,	MethodQualifiedName text,	lineNo int(11) DEFAULT NULL);";
			   
			   
	   //String sqlString=FileHelper.getContent("//Users/Grand/temp/main.sql");
	   String sqlString=FileHelper.getContent(CommUtil.getCurrentProjectPath() + "/NewHelpseekingSchemaforSqlite.sql");
	   
	   stat.executeUpdate(sqlString);   
	   
	   end = System.currentTimeMillis();
	   System.out.println("创建表耗费时间：" + (end - start));
//	   
//	   // 插入数据
//	   start = System.currentTimeMillis();
//	   stat.executeUpdate("insert into tbl1 values('ZhangSan',8000);");
//	   stat.executeUpdate("insert into tbl1 values('LiSi',7800);");
//	   stat.executeUpdate("insert into tbl1 values('WangWu',5800);");
//	   stat.executeUpdate("insert into tbl1 values('ZhaoLiu',9100);");
//	   end = System.currentTimeMillis();
//	   System.out.println("插入四行数据耗费时间：" + (end - start));
//	   
//	   start = System.currentTimeMillis();
//	   ResultSet rs = stat.executeQuery("select * from tbl1;"); // 查询数据
//	   while (rs.next()) { // 将查询到的数据打印出来
//	    System.out.print("name = " + rs.getString("name") + " "); // 列属性一
//	    System.out.println("salary = " + rs.getString("salary")); // 列属性二
//	   }
//	   rs.close();
//	   end = System.currentTimeMillis();
//	   System.out.println("查询数据耗费时间：" + (end - start));   
//	   
	   
	   InteractionEvent event =new InteractionEvent();
	   event.setActionName("test1");
	   event.setByuser(true);
	   event.setDate(Calendar.getInstance().getTime());
	   event.setDelta("hahah");
	   event.setEndDate(Calendar.getInstance().getTime());
	   event.setFile("test.file");
	   event.setInterestContribution(0.2f);
	   event.setKind(Kind.DEBUG);
	   event.setLineno("15");
	   event.setMethod("Hello");
	   event.setNavigation("project method");
	   event.setOriginId("this is test oprate");
	   event.setPackages("org.test.package");
	   event.setProject("testHello");
	   event.setStructureHandle("p->m->o");
	   event.setStructureKind("method");
	   event.setType("test");
	   DatabaseUtil.setCon(conn);
	   DatabaseUtil.addInteractionEventToDatabase(event);
	   
	   conn.close(); // 结束数据库的连接

	  } catch (Exception e) {
	   e.printStackTrace();
	  }
	 }
	
	 
//	 public String getRelatedPath()
//	 {
//		 Location installLoc = LocationManager.getInstallLocation();
//		 AaaaPlugin.getDefault().getStateLocation().makeAbsolute().toFile().getAbsolutePath());
//
//	    String path = null;
//	    String installPath = null;
//	    if (installLoc != null)
//	    {
//	        URL installURL = installLoc.getURL();
//	        // assume install URL is file: based
//	        path = installURL.getPath();
//	    }
//	    installPath = path.substring(1, path.length());
//}

}