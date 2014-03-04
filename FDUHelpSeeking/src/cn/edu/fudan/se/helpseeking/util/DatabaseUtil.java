package cn.edu.fudan.se.helpseeking.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import cn.edu.fudan.se.helpseeking.eclipsemonitor.InteractionEvent;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.InteractionEvent.Kind;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.views.BehaviorItem;

public class DatabaseUtil {
	//for oracle jdbc link
//	public static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
//	public static final String URL = "jdbc:oracle:thin:@localhost:1521:MONITOR";
//	public static final String USER = "MONITOR";
//	public static final String PWD = "123456";
	
//	for mysql jdbc link
	public static final String DRIVER = "com.mysql.jdbc.Driver";
	public static final String URL = "jdbc:mysql://localhost:3306/stackoverflow";
// for network DB SERVER URL : 	"jdbc:mysql://10.131.252.224:3309/stackoverflow"
	public static final String USER = "root";
	public static final String PWD = "root";
	
	
	private static Connection con = null;
	private static ResultSet rs = null;
	public static DataSource source = null;
	private static InteractionEvent lastEvent = null;

	public static void init() {
		con = getCon();
	}

	public static List<BehaviorItem> getRecords(int num, String isbyuser) {
		PreparedStatement ps = null;
		List<BehaviorItem> items = new ArrayList<BehaviorItem>();

		init();
		try {
			StringBuffer sqlBuffer = new StringBuffer();
			sqlBuffer.append("select * from stackoverflow.event");
			if (isbyuser.equals("1")) {
				sqlBuffer.append(" where event.isbyuser='1'");
			}
			sqlBuffer.append(" order by event.id desc");
			ps = con.prepareStatement(sqlBuffer.toString());
			rs = ps.executeQuery();

			try {
				while (rs.next()) {
					BehaviorItem item = new BehaviorItem();
					item.id = rs.getString(1);
					item.programmer = rs.getString(2);
					item.time = rs.getString(3);
					item.kind = rs.getString(5);
					item.lineno = rs.getString(6);
					item.method = rs.getString(7);
					item.type = rs.getString(8);
					item.file = rs.getString(9);
					item.pack = rs.getString(10);
					item.project = rs.getString(11);
					item.description = rs.getString(12);
					item.isbyuser = rs.getString(13);
					item.handler = rs.getString(15);
					items.add(item);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return items;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		closeAll();
		return null;
	}

	

	public static int addInteractionToDatabase(InteractionEvent event) {
		int resu = 0;
		PreparedStatement ps = null;
		if (event.equals(lastEvent)) {
			return 0;
		}

		try {
			String sql = "insert into stackoverflow.event(user,time,endtime,kind,lineno,method,type,file,package,project,originid,isbyuser,structurekind,structurehandle,delta)  values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; //for mysql the first field set as auto increment filed , you can neglect assignment value or use 'null' value, it can auto increment 
//			String sql = "insert into \"stackoverflow\".\"event\" values(id_seq.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; //in oracle db the first field with sql process code 
			String username = System.getProperties().getProperty("user.name");
			ps = con.prepareStatement(sql);
		
			ps.setString(1, username);
			ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
			ps.setString(4, event.getKind().toString());
			ps.setString(5, event.getLineno() == null ? ContextUtil.getLineno()
					: event.getLineno());
			ps.setString(6, event.getMethod() == null ? ContextUtil.getMethod()
					: event.getMethod());
			ps.setString(7, event.getType() == null ? ContextUtil.getType()
					: event.getType());
			ps.setString(8, event.getFile() == null ? ContextUtil.getFile()
					: event.getFile());
			ps.setString(9,
					event.getPackages() == null ? ContextUtil.getPackages()
							: event.getPackages());
			ps.setString(10,
					event.getProject() == null ? ContextUtil.getProject()
							: event.getProject());
			if (event.getOriginId() != null
					&& event.getOriginId().length() > 200) {
				event.setOriginId(event.getOriginId().substring(0, 199));
			}
			ps.setString(11, event.getOriginId());
			ps.setBoolean(12, event.isByuser());
			ps.setString(
					13,
					event.getStructureKind() == null ? "java" : event
							.getStructureHandle());
			ps.setString(
					14,
					event.getStructureHandle() == null ? ContextUtil
							.getHandleIdentifier() : event.getStructureHandle());
			ps.setString(15, event.getDelta());
			
			//id field is an auto increment field , it need null value to let dbms increase ....

			
			resu = ps.executeUpdate();
			if (resu == 1) {
				lastEvent = event;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return resu;
	}

	public static void destroy() {
		closeAll();
	}



	public static Connection getCon() {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			con = DriverManager.getConnection(URL, USER, PWD);
			if (con!=null) {
				System.out.println("数据库连接成功");
			}
//			con.setAutoCommit(false);//设置为false时可能批处理提交容易,并不是自动提交，这样不 需要频繁验证。处理回滚事务。

			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return con;
	}

	public static void closeAll() {

		if (con != null)
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}
	
	public static void main(String args[]) {
		init();
		InteractionEvent event = new InteractionEvent();
//		Kind kind = null;
//		Date date = null;
//		Date endDate = null;
//		String originId = null;
//		String structureKind = null;
//		String structureHandle = null;
//		String lineno = null;
//		String method = null;
//		private String type = null;
//		String file = null;
//		private String packages = null;
//		String project = null;
//		private String navigation = null;
//		String delta = null;
//		private boolean byuser = false;
//		private float interestContribution = 0;

		
		
		event.setDate(Calendar.getInstance().getTime());
		event.setEndDate(Calendar.getInstance().getTime());
		event.setDelta(null);
		event.setKind(Kind.EDIT);
		event.setStructureHandle("abcde");
		event.setStructureKind("java");
		event.setOriginId("23fe");
		event.setLineno("0");
		event.setMethod("234");
		event.setFile("324");
		event.setProject("fsd");
		
		event.setType("testmysql");
		event.setPackages("cn.edu.fudan.se.helpseeking");
		event.setNavigation("main");
		
		
		int resu = addInteractionToDatabase(event);
		System.out.println(resu);
		destroy();
	}

}
