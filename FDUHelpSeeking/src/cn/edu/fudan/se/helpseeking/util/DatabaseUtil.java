package cn.edu.fudan.se.helpseeking.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.print.DocFlavor.STRING;
import javax.sql.DataSource;

import cn.edu.fudan.se.helpseeking.bean.Action;
import cn.edu.fudan.se.helpseeking.bean.Basic.CompileInfoType;
import cn.edu.fudan.se.helpseeking.bean.Basic.RuntimeInfoType;
import cn.edu.fudan.se.helpseeking.bean.Breakpoint;
import cn.edu.fudan.se.helpseeking.bean.ClassModel;
import cn.edu.fudan.se.helpseeking.bean.CompileInformation;
import cn.edu.fudan.se.helpseeking.bean.Cursor;
import cn.edu.fudan.se.helpseeking.bean.DebugCode;
import cn.edu.fudan.se.helpseeking.bean.EditCode;
import cn.edu.fudan.se.helpseeking.bean.EditorInfo;
import cn.edu.fudan.se.helpseeking.bean.ExplorerInfo;
import cn.edu.fudan.se.helpseeking.bean.ExplorerRelated;
import cn.edu.fudan.se.helpseeking.bean.IDEOutput;
import cn.edu.fudan.se.helpseeking.bean.Information;
import cn.edu.fudan.se.helpseeking.bean.Basic.Kind;
import cn.edu.fudan.se.helpseeking.bean.RuntimeInformation;
import cn.edu.fudan.se.helpseeking.bean.SyntacticBlock;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.InteractionEvent;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.views.BehaviorItem;

public class DatabaseUtil {
	//for oracle jdbc link
	//	public static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	//	public static final String URL = "jdbc:oracle:thin:@localhost:1521:MONITOR";
	//	public static final String USER = "MONITOR";
	//	public static final String PWD = "123456";

	//	for mysql jdbc link
	public static final String DRIVER = "com.mysql.jdbc.Driver";
	public static final String URL = "jdbc:mysql://localhost:3306/helpseeking";
	// for network DB SERVER URL : 	"jdbc:mysql://10.131.252.224:3309/helpseeking"
	public static final String USER = "root";
	public static final String PWD = "root";


	private static Connection con = null;
	private static ResultSet rs = null;
	public static DataSource source = null;
	private static InteractionEvent lastEvent = null;
	private static Information lastInformation=null;

	public static void init() {
		con = getCon();
	}

	public static List<BehaviorItem> getInteractionEventRecords(int num, String isbyuser) {
		PreparedStatement ps = null;
		List<BehaviorItem> items = new ArrayList<BehaviorItem>();

		init();
		try {
			StringBuffer sqlBuffer = new StringBuffer();
			sqlBuffer.append("select * from helpseeking.event");
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



	public static int addInteractionEventToDatabase(InteractionEvent event) {
		int resu = 0;
		PreparedStatement ps = null;
		if (event.equals(lastEvent)) {
			return 0;
		}

	System.out.println("DatabaseUtil: addinteractionEventToDatabase\n"+" action kind :"+event.getKind()+"\n action name "+event.getActionName()+"\n action original:"+event.getOriginId());
		
		try {
			String sql = "insert into helpseeking.event(user,time,endtime,kind,lineno,method,type,file,package,project,originid,isbyuser,structurekind,structurehandle,delta)  values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; //for mysql the first field set as auto increment filed , you can neglect assignment value or use 'null' value, it can auto increment 
			//			String sql = "insert into \"helpseeking\".\"event\" values(id_seq.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; //in oracle db the first field with sql process code 
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


//尝试使用synchronized用于插入数据时的同步
	public static synchronized int addInformationToDatabase(Information information)  {
		int resu = 0;

		if (information.equals(lastInformation)) {
			return 0;
		}

		int DebugCodeID=-1;
		int EditCodeID=-1;
		int IDEOutputID=-1;
		int ExplorerRelatedID=-1;
		int ActionID=-1;

		//insert debugCode and get debugcodeID with synchronized
		if (information.getDebugCode()!=null) {
			DebugCodeID=addDebugCodeAndgetID(information.getDebugCode());
		}



		//insert editCode and get editCodeID with synchronized
		if (information.getEditCode()!=null) {
			EditCodeID=addEditCodeAndgetID(information.getEditCode());
		}


		//insert IDEOutput and get IDEOutputID with synchronized
		if (information.getIdeOutput()!=null) {
			IDEOutputID=addIDEOutputAndgetID(information.getIdeOutput());
		}

		//insert explorerRelated and get explorerRelatedID with synchronized
		if (information.getExplorerRelated()!=null) {
			ExplorerRelatedID=addExplorerRelatedAndgetID(information.getExplorerRelated());
		}

		//insert action and get actionID with synchronized
		if (information.getAction()!=null) {
			ActionID=addActionAndgetID(information.getAction());
		}


		// then insert information

		PreparedStatement ps = null;

		try {
			String sql = "insert into helpseeking.information(DebugCodeID,EditCodeID,IDEOutputID,ExplorerRelatedID,ActionID, type)  values(?,?,?,?,?,?)"; 
			//for mysql the first field set as auto increment filed , you can neglect assignment value or use 'null' value, it can auto increment 
			//			String sql = "insert into \"helpseeking\".\"event\" values(id_seq.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; //in oracle db the first field with sql process code 

			ps = con.prepareStatement(sql);

			ps.setInt(1, DebugCodeID);
			ps.setInt(2, EditCodeID);
			ps.setInt(3, IDEOutputID);
			ps.setInt(4, ExplorerRelatedID);
			ps.setInt(5, ActionID);
			ps.setString(6, information.getType());

			//id field is an auto increment field , it need null value to let dbms increase ....


			resu = ps.executeUpdate();
			if (resu == 1) {
				lastInformation = information;
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




	//insert action and get actionID with synchronized	

	private static int addActionAndgetID(Action action) {
		int actionID=-1;
		int addresult=0;
		
		
			addresult=addActionTODataBase(action);
		
		if (addresult==1) {
			actionID=findlastActionID();
		}
		
		return actionID;
	}


	private static int addActionTODataBase(Action action) {
		int result = 0;

		if (action == null) {
			return -1;
		}

		PreparedStatement ps = null;
		// if (debugCode.equals(lastdebugCode)) {
		// return 0;
		// }
		try {
			String sql = "insert into helpseeking.action(time,endtime,actionKind,actionName,description,byuser)  values(?,?,?,?,?,?)";
			// for mysql the first field set as auto increment filed , you can
			// neglect assignment value or use 'null' value, it can auto
			// increment
			// String sql =
			// "insert into \"helpseeking\".\"event\" values(id_seq.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			// //in oracle db the first field with sql process code

			ps = con.prepareStatement(sql);

			ps.setTimestamp(1, (Timestamp)action.getTime());
			ps.setTimestamp(2, (Timestamp)action.getEndtime());
			ps.setString(3, action.getActionKind().toString());
			ps.setString(4, action.getActionName());
			ps.setString(5, action.getDescription());
			ps.setBoolean(6, action.isByuser());

			// id field is an auto increment field , it need null value to let
			// dbms increase ....

			result = ps.executeUpdate();
			// if (result == 1) {
			// lastdebugCode= debugCode;
			// }
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

		return result;
	}

	private static int findlastActionID() {
		int ActionID=-1;
		ActionID=findlastID("select max(id) from helpseeking.action ");
		return ActionID;

	}

	//insert explorerRelated and get explorerRelatedID with synchronized

	private static int addExplorerRelatedAndgetID(
			ExplorerRelated explorerRelated) {
		int explorerRelatedID=-1;
		int addresult=0;
		addresult=addExplorerRelatedTODataBase(explorerRelated);
		if (addresult==1) {
			explorerRelatedID=findlastExplorerRelatedID();
		}

		return explorerRelatedID;
	}

	private static int addExplorerRelatedTODataBase(
			ExplorerRelated explorerRelated) {
		int result=0;

		int editorInfoID=-1;
		int explorerInfoID=-1;


		//insert editorInfo and get editorInfoID with synchronized
		if (explorerRelated.getEditorInfo()!=null) {
			editorInfoID=addEditorInfoAndgetID(explorerRelated.getEditorInfo());
		}

		//insert explorerInfo and get explorerInfoID with synchronized
		if (explorerRelated.getExplorerInfo()!=null) {
			explorerInfoID=addExplorerInfoAndgetID(explorerRelated.getExplorerInfo());
		}


		PreparedStatement ps = null;
		//		if (debugCode.equals(lastdebugCode)) {
		//			return 0;
		//		}

		try {
			String sql = "insert into helpseeking.explorerrelated(editorInfoID,explorerInfoID)  values(?,?)"; 
			//for mysql the first field set as auto increment filed , you can neglect assignment value or use 'null' value, it can auto increment 
			//String sql = "insert into \"helpseeking\".\"event\" values(id_seq.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; //in oracle db the first field with sql process code 

			ps = con.prepareStatement(sql);

			ps.setInt(1, editorInfoID);
			ps.setInt(2, explorerInfoID);

			//id field is an auto increment field , it need null value to let dbms increase ....
			result = ps.executeUpdate();
			//			if (result == 1) {
			//				lastdebugCode= debugCode;
			//			}
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

		return result;
	}

	private static int addExplorerInfoAndgetID(ExplorerInfo explorerInfo) {
		int explorerInfoID=-1;
		int addresult=0;
		addresult=addExplorerInfoTODataBase(explorerInfo);
		if (addresult==1) {
			explorerInfoID=findlastExplorerInfoID();
		}


		return explorerInfoID;
	}

	private static int findlastExplorerInfoID() {


		int explorerInfoID=-1;
		explorerInfoID=findlastID("select max(id) from helpseeking.explorerinfo ");
		return explorerInfoID;
	}

	private static int addExplorerInfoTODataBase(ExplorerInfo explorerInfo) {
		int result = 0;

		if (explorerInfo == null) {
			return -1;
		}

		PreparedStatement ps = null;
		// if (debugCode.equals(lastdebugCode)) {
		// return 0;
		// }
		try {
			String sql = "insert into helpseeking.explorerinfo(size,selectObjectNameList)  values(?,?)";

			ps = con.prepareStatement(sql);

			ps.setInt(1, explorerInfo.getSize());
			String selectObjectName=connectListwithSemicolonToString(explorerInfo.getSelectObjectNameList());
			ps.setString(2, selectObjectName);

			// id field is an auto increment field , it need null value to let
			// dbms increase ....

			result = ps.executeUpdate();
			// if (result == 1) {
			// lastdebugCode= debugCode;
			// }
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

		return result;
	}

	private static int addEditorInfoAndgetID(EditorInfo editorInfo) {
		int editorInfoID=-1;
		int addresult=0;
		addresult=addEditorInfoTODataBase(editorInfo);
		if (addresult==1) {
			editorInfoID=findlastEditorInfoID();
		}


		return editorInfoID;
	}

	private static int findlastEditorInfoID() {
		int editorInfoID=-1;
		editorInfoID=findlastID("select max(id) from helpseeking.editorinfo ");
		return editorInfoID;
	}

	private static int addEditorInfoTODataBase(EditorInfo editorInfo) {
		int result = 0;

		if (editorInfo == null) {
			return -1;
		}

		PreparedStatement ps = null;
		// if (debugCode.equals(lastdebugCode)) {
		// return 0;
		// }
		try {
			String sql = "insert into helpseeking.editorinfo(size,classQualifiedNameList)  values(?,?)";

			ps = con.prepareStatement(sql);

			ps.setInt(1, editorInfo.getSize());
			String classQualifiedName=connectListwithSemicolonToString(editorInfo.getClassQualifiedNameList());
			ps.setString(2, classQualifiedName);

			// id field is an auto increment field , it need null value to let
			// dbms increase ....

			result = ps.executeUpdate();
			// if (result == 1) {
			// lastdebugCode= debugCode;
			// }
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

		return result;
	}

	private static int findlastExplorerRelatedID() {

		int explorerRelatedID=-1;
		explorerRelatedID=findlastID("select max(id) from helpseeking.explorerrelated ");
		return explorerRelatedID;

	}


	//insert IDEOutput and get IDEOutputID with synchronized


	private static int addIDEOutputAndgetID(IDEOutput ideOutput) {
		int ideOutputID=-1;
		int addresult=0;
		addresult=addIDEOutputTODataBase(ideOutput);
		if (addresult==1) {
			ideOutputID=findlastIDEOutputID();
		}

		return ideOutputID;
	}

	private static int addIDEOutputTODataBase(IDEOutput ideOutput) {
		int result=0;

		int compileInformationID=-1;
		int runtimeInformationID=-1;


		//insert compileInformation and get compileInformationID with synchronized
		if (ideOutput.getCompileInformation()!=null) {
			compileInformationID=addCompileInformationAndgetID(ideOutput.getCompileInformation());
		}

		//insert runtimeInformation and get runtimeInformationID with synchronized
		if (ideOutput.getRuntimeInformation()!=null) {
			runtimeInformationID=addRuntimeInformationAndgetID(ideOutput.getRuntimeInformation());
		}


		PreparedStatement ps = null;
		//		if (debugCode.equals(lastdebugCode)) {
		//			return 0;
		//		}

		try {
			String sql = "insert into helpseeking.ideoutput(CompileInformationID,RuntimeInformationID)  values(?,?)"; 
			//for mysql the first field set as auto increment filed , you can neglect assignment value or use 'null' value, it can auto increment 
			//String sql = "insert into \"helpseeking\".\"event\" values(id_seq.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; //in oracle db the first field with sql process code 

			ps = con.prepareStatement(sql);

			ps.setInt(1, compileInformationID);
			ps.setInt(2, runtimeInformationID);

			//id field is an auto increment field , it need null value to let dbms increase ....
			result = ps.executeUpdate();
			//			if (result == 1) {
			//				lastdebugCode= debugCode;
			//			}
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

		return result;
	}

	private static int addRuntimeInformationAndgetID(
			RuntimeInformation runtimeInformation) {
		int runtimeInformationID=-1;
		int addresult=0;
		addresult=addRuntimeInformationTODataBase(runtimeInformation);
		if (addresult==1) {
			runtimeInformationID=findlastRuntimeInformationID();
		}

		return runtimeInformationID;
	}

	private static int findlastRuntimeInformationID() {


		int runtimeInformationID=-1;
		runtimeInformationID=findlastID("select max(id) from helpseeking.runtimeinformation ");
		return runtimeInformationID;
	}

	private static int addRuntimeInformationTODataBase(
			RuntimeInformation runtimeInformation) {
		int result = 0;

		if (runtimeInformation == null) {
			return -1;
		}

		PreparedStatement ps = null;
		// if (debugCode.equals(lastdebugCode)) {
		// return 0;
		// }
		try {
			String sql = "insert into helpseeking.runtimeinformation(type,content,relatedCode)  values(?,?,?)";
			// for mysql the first field set as auto increment filed , you can
			// neglect assignment value or use 'null' value, it can auto
			// increment
			// String sql =
			// "insert into \"helpseeking\".\"event\" values(id_seq.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			// //in oracle db the first field with sql process code

			ps = con.prepareStatement(sql);

			ps.setString(1, runtimeInformation.getType().toString());
			ps.setString(2, runtimeInformation.getContent());
			ps.setString(3, runtimeInformation.getRelatedCode());

			// id field is an auto increment field , it need null value to let
			// dbms increase ....

			result = ps.executeUpdate();
			// if (result == 1) {
			// lastdebugCode= debugCode;
			// }
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

		return result;
	}

	private static int addCompileInformationAndgetID(CompileInformation compileInformation) {
		int compileInformationID=-1;
		int addresult=0;
		addresult=addCompileInformationTODataBase(compileInformation);
		if (addresult==1) {
			compileInformationID=findlastCompileInformationID();
		}

		return compileInformationID;
	}



	private static int findlastCompileInformationID() {
		int compileInformationID=-1;
		compileInformationID=findlastID("select max(id) from helpseeking.compileinformation ");
		return compileInformationID;


	}

	private static int addCompileInformationTODataBase(
			CompileInformation compileInformation) {
		int result = 0;

		if (compileInformation == null) {
			return -1;
		}

		PreparedStatement ps = null;
		// if (debugCode.equals(lastdebugCode)) {
		// return 0;
		// }
		try {
			String sql = "insert into helpseeking.compileinformation(type,content,relatedCode)  values(?,?,?)";
			// for mysql the first field set as auto increment filed , you can
			// neglect assignment value or use 'null' value, it can auto
			// increment
			// String sql =
			// "insert into \"helpseeking\".\"event\" values(id_seq.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			// //in oracle db the first field with sql process code

			ps = con.prepareStatement(sql);

			ps.setString(1, compileInformation.getType().toString());
			ps.setString(2, compileInformation.getContent());
			ps.setString(3, compileInformation.getRelatedCode());

			// id field is an auto increment field , it need null value to let
			// dbms increase ....

			result = ps.executeUpdate();
			// if (result == 1) {
			// lastdebugCode= debugCode;
			// }
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

		return result;
	}

	private static int findlastIDEOutputID() {


		int ideOutputID=-1;
		ideOutputID=findlastID("select max(id) from helpseeking.ideoutput ");
		return ideOutputID;
	}



	//insert editCode and get editCodeID with synchronized


	private static int addEditCodeAndgetID(EditCode editCode) {
		int editCodeID=-1;
		int addresult=0;
		addresult=addEditCodeTODataBase(editCode);
		if (addresult==1) {
			editCodeID=findlastEditCodeID();
		}

		return editCodeID;
	}

	private static int findlastEditCodeID() {
		int EditCodeID=-1;
		EditCodeID=findlastID("select max(id) from helpseeking.editcode ");
		return EditCodeID;
	}	

	private static int addEditCodeTODataBase(EditCode editCode) {
		int result=0;

		int SyntacticBlockID=-1;
		int ClassModelID=-1;
		int CursorID=-1;

		//insert syntacticblock and get syntacticblockID with synchronized
		if (editCode.getSyntacticBlock()!=null) {
			SyntacticBlockID=addSyntacticBlockAndgetID(editCode.getSyntacticBlock());
		}

		//insert ClassModel and get ClassModelID with synchronized
		if (editCode.getClassModel()!=null) {
			ClassModelID=addClassModelAndgetID(editCode.getClassModel());
		}

		//insert Cursor and get CursorID with synchronized
		if (editCode.getCursor()!=null) {
			CursorID=addCursorAndgetID(editCode.getCursor());
		}



		PreparedStatement ps = null;
		//		if (debugCode.equals(lastdebugCode)) {
		//			return 0;
		//		}

		try {
			String sql = "insert into helpseeking.editcode(SyntacticBlockID,ClassModelID,CursorID)  values(?,?,?)"; 
			//for mysql the first field set as auto increment filed , you can neglect assignment value or use 'null' value, it can auto increment 
			//String sql = "insert into \"helpseeking\".\"event\" values(id_seq.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; //in oracle db the first field with sql process code 

			ps = con.prepareStatement(sql);

			ps.setInt(1, SyntacticBlockID);
			ps.setInt(2, ClassModelID);
			ps.setInt(3, CursorID);
			//id field is an auto increment field , it need null value to let dbms increase ....


			result = ps.executeUpdate();
			//			if (result == 1) {
			//				lastdebugCode= debugCode;
			//			}
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

		return result;
	}

	private static int addCursorAndgetID(Cursor cursor) {
		int cursorID=-1;
		int addresult=0;
		addresult=addCursorTODataBase(cursor);
		if (addresult==1) {
			cursorID=findlastCursorID();
		}


		return cursorID;
	}




	private static int findlastCursorID() {
		int cursorID=-1;
		cursorID=findlastID("select max(id) from helpseeking.cursor ");
		return cursorID;

	}

	private static int addCursorTODataBase(Cursor cursor) {
		int result = 0;

		if (cursor == null) {
			return -1;
		}

		PreparedStatement ps = null;
		// if (debugCode.equals(lastdebugCode)) {
		// return 0;
		// }
		try {
			String sql = "insert into helpseeking.cursor(lineNo,lineFrom,lineTo,MethodQualifiedName)  values(?,?,?,?)";
			// for mysql the first field set as auto increment filed , you can
			// neglect assignment value or use 'null' value, it can auto
			// increment
			// String sql =
			// "insert into \"helpseeking\".\"event\" values(id_seq.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			// //in oracle db the first field with sql process code

			ps = con.prepareStatement(sql);

			ps.setInt(1, cursor.getLineNo());
			ps.setInt(2, cursor.getLineFrom());
			ps.setInt(3, cursor.getLineTo());
			ps.setString(4, cursor.getMethodQualifiedName());


			// id field is an auto increment field , it need null value to let
			// dbms increase ....

			result = ps.executeUpdate();
			// if (result == 1) {
			// lastdebugCode= debugCode;
			// }
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

		return result;
	}

	//insert debugCode and get debugcodeID with synchronized	
	private static int addDebugCodeAndgetID(DebugCode debugCode){
		int debugCodeID=-1;
		int addresult=0;
		addresult=addDebugCodeTODataBase(debugCode);
		if (addresult==1) {
			debugCodeID=findlastDebugCodeID();
		}

		return debugCodeID;
	}
	private static int addSyntacticBlockAndgetID(SyntacticBlock syntacticBlock) {
		int syntacticBlockID=-1;
		int addresult=0;
		addresult=addSyntacticBlockTODataBase(syntacticBlock);
		if (addresult==1) {
			syntacticBlockID=findlastSyntacticBlockID();
		}


		return syntacticBlockID;
	}


	// return 0 insert failed , return 1 insert succed , -1 no value to insert
	private static int addSyntacticBlockTODataBase(SyntacticBlock syntacticBlock) {
		int result = 0;

		if (syntacticBlock == null) {
			return -1;
		}

		PreparedStatement ps = null;
		// if (debugCode.equals(lastdebugCode)) {
		// return 0;
		// }
		try {
			String sql = "insert into helpseeking.syntacticblock(type,code,exceptionName)  values(?,?,?)";
			// for mysql the first field set as auto increment filed , you can
			// neglect assignment value or use 'null' value, it can auto
			// increment
			// String sql =
			// "insert into \"helpseeking\".\"event\" values(id_seq.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			// //in oracle db the first field with sql process code

			ps = con.prepareStatement(sql);

			ps.setString(1, syntacticBlock.getType());
			ps.setString(2, syntacticBlock.getCode());
			ps.setString(3, syntacticBlock.getExceptionName());
			// id field is an auto increment field , it need null value to let
			// dbms increase ....

			result = ps.executeUpdate();
			// if (result == 1) {
			// lastdebugCode= debugCode;
			// }
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

		return result;
	}

	private static  int addDebugCodeTODataBase(DebugCode debugCode) {
		int result=0;

		int SyntacticBlockID=-1;
		int ClassModelID=-1;
		int BreakpointID=-1;

		//insert syntacticblock and get syntacticblockID with synchronized
		if (debugCode.getSyntacticBlock()!=null) {
			SyntacticBlockID=addSyntacticBlockAndgetID(debugCode.getSyntacticBlock());
		}

		//insert ClassModel and get ClassModelID with synchronized
		if (debugCode.getClassModel()!=null) {
			ClassModelID=addClassModelAndgetID(debugCode.getClassModel());
		}

		//insert Breakpoint and get BreakpointID with synchronized
		if (debugCode.getBreakpoint()!=null) {
			BreakpointID=addBreakpointAndgetID(debugCode.getBreakpoint());
		}



		PreparedStatement ps = null;
		//		if (debugCode.equals(lastdebugCode)) {
		//			return 0;
		//		}

		try {
			String sql = "insert into helpseeking.debugcode(SyntacticBlockID,ClassModelID,BreakpointID)  values(?,?,?)"; 
			//for mysql the first field set as auto increment filed , you can neglect assignment value or use 'null' value, it can auto increment 
			//String sql = "insert into \"helpseeking\".\"event\" values(id_seq.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; //in oracle db the first field with sql process code 

			ps = con.prepareStatement(sql);

			ps.setInt(1, SyntacticBlockID);
			ps.setInt(2, ClassModelID);
			ps.setInt(3, BreakpointID);
			//id field is an auto increment field , it need null value to let dbms increase ....


			result = ps.executeUpdate();
			//			if (result == 1) {
			//				lastdebugCode= debugCode;
			//			}
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

		return result;
	}


	private static int addBreakpointAndgetID(Breakpoint breakpoint) {
		int breakpointID=-1;
		int addresult=0;
		addresult=addBreakpointTODataBase(breakpoint);
		if (addresult==1) {
			breakpointID=findlastBreakpointID();
		}


		return breakpointID;
	}

	private static int findlastBreakpointID() {

		int BreakpointID=-1;
		BreakpointID=findlastID("select max(id) from helpseeking.breakpoint ");
		return BreakpointID;
	}

	private static int addBreakpointTODataBase(Breakpoint breakpoint) {
		int result = 0;

		if (breakpoint == null) {
			return -1;
		}

		PreparedStatement ps = null;
		// if (debugCode.equals(lastdebugCode)) {
		// return 0;
		// }
		try {
			String sql = "insert into helpseeking.breakpoint(type,MethodQualifiedName,lineNo)  values(?,?,?)";
			// for mysql the first field set as auto increment filed , you can
			// neglect assignment value or use 'null' value, it can auto
			// increment
			// String sql =
			// "insert into \"helpseeking\".\"event\" values(id_seq.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			// //in oracle db the first field with sql process code

			ps = con.prepareStatement(sql);

			ps.setString(1, breakpoint.getType());
			ps.setString(2, breakpoint.getMethodQualifiedName()); 
			ps.setInt(3, breakpoint.getLineNo());

			// id field is an auto increment field , it need null value to let
			// dbms increase ....

			result = ps.executeUpdate();
			// if (result == 1) {
			// lastdebugCode= debugCode;
			// }
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

		return result;
	}

	private static int addClassModelAndgetID(ClassModel classModel) {
		int classModelID=-1;
		int addresult=0;
		addresult=addClassModelTODataBase(classModel);
		if (addresult==1) {
			classModelID=findlastClassModelID();
		}


		return classModelID;
	}

	private static int addClassModelTODataBase(ClassModel classModel) {
		int result = 0;

		if (classModel == null) {
			return -1;
		}

		PreparedStatement ps = null;
		// if (debugCode.equals(lastdebugCode)) {
		// return 0;
		// }
		try {
			String sql = "insert into helpseeking.classmodel(type,code,internalCaller,internalCallee,upClass,belowClass)  values(?,?,?,?,?,?)";
			// for mysql the first field set as auto increment filed , you can
			// neglect assignment value or use 'null' value, it can auto
			// increment
			// String sql =
			// "insert into \"helpseeking\".\"event\" values(id_seq.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			// //in oracle db the first field with sql process code

			ps = con.prepareStatement(sql);

			ps.setString(1, classModel.getType());
			ps.setString(2, classModel.getCode());

			String internalCaller=connectListwithSemicolonToString(classModel.getInternalCaller());
			String internalCallee=connectListwithSemicolonToString(classModel.getInternalCallee());
			String upClass=connectListwithSemicolonToString(classModel.getUpClass());
			String belowClass=connectListwithSemicolonToString(classModel.getBelowClass());

			ps.setString(3, internalCaller);
			ps.setString(4, internalCallee);
			ps.setString(5, upClass);
			ps.setString(6, belowClass);
			// id field is an auto increment field , it need null value to let
			// dbms increase ....

			result = ps.executeUpdate();
			// if (result == 1) {
			// lastdebugCode= debugCode;
			// }
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

		return result;
	}

	public static String connectListwithSemicolonToString(List<String> strList) {
		if (strList==null) {
			return null;
		}
		
		String resultString="";
		List<String > tempList=strList;

		if(tempList.size()==1)
		{
			resultString=tempList.get(0);
		}
		if (tempList.size()>1) {
			resultString=tempList.get(0);
			for (int i=1;i<tempList.size();i++) {
				resultString=resultString+";"+tempList.get(i); //??
			}		
		}
		return resultString;
	}

	private static int findlastClassModelID() {
		int ClassModelID=-1;
		ClassModelID=findlastID("select max(id) from helpseeking.classmodel ");
		return ClassModelID;
	}

	private static int findlastSyntacticBlockID() {


		int SyntacticBlockID=-1;
		SyntacticBlockID=findlastID("select max(id) from helpseeking.syntacticblock ");
		return SyntacticBlockID;
	}

	private static int findlastID(String sqlString) {
		PreparedStatement ps = null;
		int  ID=-1;
		try {
			StringBuffer sqlBuffer = new StringBuffer();
			sqlBuffer.append(sqlString);
			//			"select max(id) from helpseeking.syntacticblock "
			ps = con.prepareStatement(sqlBuffer.toString());
			rs = ps.executeQuery();
			List<Integer> items=new ArrayList<Integer>();
			try {
				while (rs.next()) {
					int item=-1;
					item=rs.getInt(1);
					items.add(item);
				}
				//debug 
				ID = items.get(0);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return ID;

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

		return -1;
	}

	private static int findlastDebugCodeID() {
		int DebugCodeID=-1;
		DebugCodeID=findlastID("select max(id) from helpseeking.debugcode ");
		return DebugCodeID;

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
		//		testAddInteractionEvent();
		testAddInformation();
		destroy();
	}

	public static void testAddInformation()
	{
		Information information=new Information();
		DebugCode debugCode=new DebugCode();
		EditCode editCode=new EditCode();
		IDEOutput ideOutput=new IDEOutput();
		ExplorerRelated explorerRelated=new ExplorerRelated();
		Action action=new Action();
		String type="the first  information node for  testing";

		debugCode=testinitDebugCode();
		editCode=testinitEditCode();
		ideOutput=testinitIDEOutput();
		explorerRelated=testinitExplorerRelated();
		action=testinitAction();

		information.setDebugCode(debugCode);
		information.setEditCode(editCode);
		information.setIdeOutput(ideOutput);
		information.setExplorerRelated(explorerRelated);
		information.setAction(action);

		addInformationToDatabase(information);

	}

	private static Action testinitAction() {
		Action action=new Action();
		action.setTime(new  Timestamp(System.currentTimeMillis()));
		action.setEndtime(new  Timestamp(System.currentTimeMillis()+2000));
		action.setActionKind(Kind.ATTENTION);
		action.setActionName("bring up to top");
		action.setDescription("bring up to top : window @1445");
		action.setByuser(false);

		return action;
	}

	private static ExplorerRelated testinitExplorerRelated() {
		ExplorerRelated explorerRelated=new ExplorerRelated();

		EditorInfo editorInfo=new EditorInfo();
		editorInfo.setSize(5);
		List<String > classQualifiedNameList=new ArrayList<String>();
		classQualifiedNameList.add("cn.edu.fudan.se.helpseeking.eclipsemonitor.InteractionEvent");
		classQualifiedNameList.add("cn.edu.fudan.se.helpseeking.bean.ClassModel");
		classQualifiedNameList.add("cn.edu.fudan.se.helpseeking.bean.Breakpoint");
		classQualifiedNameList.add("cn.edu.fudan.se.helpseeking.bean.Cursor");
		editorInfo.setClassQualifiedNameList(classQualifiedNameList);

		ExplorerInfo explorerInfo=new ExplorerInfo();
		explorerInfo.setSize(4);
		List<String > selectObjectNameList=new ArrayList<String>();
		selectObjectNameList.add("cn.edu.fudan.se.helpseeking.eclipsemonitor.InteractionEvent");
		selectObjectNameList.add("cn.edu.fudan.se.helpseeking.bean.ClassModel");
		selectObjectNameList.add("cn.edu.fudan.se.helpseeking.bean.Breakpoint");
		selectObjectNameList.add("cn.edu.fudan.se.helpseeking.bean.Cursor");
		explorerInfo.setSelectObjectNameList(selectObjectNameList);

		explorerRelated.setEditorInfo(editorInfo);
		explorerRelated.setExplorerInfo(explorerInfo);
		return explorerRelated;
	}

	private static IDEOutput testinitIDEOutput() {
		IDEOutput ideOutput=new IDEOutput();
		CompileInformation compileInformation=new CompileInformation();
		compileInformation.setType(CompileInfoType.ERROR);
		compileInformation.setContent("this is compile error testing information");
		compileInformation.setRelatedCode(" for(int i; i<a.size();i++)  { "); 

		RuntimeInformation runtimeInformation=new RuntimeInformation();
		runtimeInformation.setType(RuntimeInfoType.ExceptionalMessage);
		runtimeInformation.setContent("nullPorinterException at the line 10   test.java ");
		runtimeInformation.setRelatedCode("ok=test.getstatus()");

		ideOutput.setCompileInformation(compileInformation);
		ideOutput.setRuntimeInformation(runtimeInformation);



		return ideOutput;
	}

	private static EditCode testinitEditCode() {
		EditCode editCode=new EditCode();
		Cursor cursor=new Cursor();
		cursor.setLineNo(90);
		cursor.setLineFrom(90);
		cursor.setLineTo(90);

		cursor.setMethodQualifiedName("cn.edu.fudan.se.helpseeking.util.DatabaseUtil.initEditCode()");


		ClassModel edclassModel=new ClassModel();
		edclassModel.setType("line");
		edclassModel.setCode("editor testing code line ");

		List<String> edinteralcaller=new ArrayList<String>();
		List<String> edinteralcallee=new ArrayList<String>();
		List<String> edupClass=new ArrayList<String>();
		List<String> edbelowClass=new ArrayList<String>();

		edinteralcaller.add("cn.edu.fudan.se.helpseeking.util.DatabaseUtil.testAddInformation()");

		edinteralcallee.add("cn.edu.fudan.se.helpseeking.bean.ClassModel.setCode(String)");
		edinteralcallee.add("cn.edu.fudan.se.helpseeking.bean.Breakpoint.setLineNo(int)");
		edinteralcallee.add("cn.edu.fudan.se.helpseeking.bean.Cursor.setLineFrom(int)");

		edupClass.add(	"cn.edu.fudan.se.helpseeking.eclipsemonitor.InteractionEvent");

		edbelowClass.add("cn.edu.fudan.se.helpseeking.bean.ClassModel");
		edbelowClass.add("cn.edu.fudan.se.helpseeking.bean.Breakpoint");
		edbelowClass.add("cn.edu.fudan.se.helpseeking.bean.Cursor");

		edclassModel.setInternalCaller(edinteralcaller);
		edclassModel.setInternalCallee(edinteralcallee);
		edclassModel.setUpClass(edupClass);
		edclassModel.setBelowClass(edbelowClass);

		SyntacticBlock edsyntacticBlock=new SyntacticBlock();
		edsyntacticBlock.setType("WHILESTATEMENT");
		edsyntacticBlock.setCode( "while (k>1)  { k=k+1; }" );
		edsyntacticBlock.setExceptionName(null);

		editCode.setSyntacticBlock(edsyntacticBlock);
		editCode.setClassModel(edclassModel);
		editCode.setCursor(cursor);

		return editCode;
	}

	private static DebugCode testinitDebugCode() {
		DebugCode debugCode=new DebugCode();

		Breakpoint breakpoint=new Breakpoint();
		breakpoint.setLineNo(110);
		breakpoint.setMethodQualifiedName("cn.edu.fudan.se.helpseeking.util.DatabaseUtil.testAddInformation()");
		breakpoint.setType("line");

		ClassModel dbugclassModel=new ClassModel();
		dbugclassModel.setType("line");
		dbugclassModel.setCode("testing code line ");

		List<String> dbuginteralcaller=new ArrayList<String>();
		List<String> dbuginteralcallee=new ArrayList<String>();
		List<String> dbugupClass=new ArrayList<String>();
		List<String> dbugbelowClass=new ArrayList<String>();

		dbuginteralcaller.add("cn.edu.fudan.se.helpseeking.util.DatabaseUtil.main(String[])");
		dbuginteralcallee.add("cn.edu.fudan.se.helpseeking.bean.ClassModel.setCode(String)");
		dbuginteralcallee.add("cn.edu.fudan.se.helpseeking.bean.Breakpoint.setLineNo(int)");
		dbugupClass=null;
		dbugbelowClass.add("cn.edu.fudan.se.helpseeking.bean.ClassModel");
		dbugbelowClass.add("cn.edu.fudan.se.helpseeking.bean.Breakpoint");

		dbugclassModel.setInternalCaller(dbuginteralcaller);
		dbugclassModel.setInternalCallee(dbuginteralcallee);
		dbugclassModel.setUpClass(dbugupClass);
		dbugclassModel.setBelowClass(dbugbelowClass);

		SyntacticBlock dbugsyntacticBlock=new SyntacticBlock();
		dbugsyntacticBlock.setType("IFSTATEMENT");
		dbugsyntacticBlock.setCode( "if(i>0) {i++;}" );
		dbugsyntacticBlock.setExceptionName(null);


		debugCode.setBreakpoint(breakpoint);
		debugCode.setClassModel(dbugclassModel);
		debugCode.setSyntacticBlock(dbugsyntacticBlock);

		return debugCode;
	}

	public static void testAddInteractionEvent() {
		InteractionEvent event = new InteractionEvent();

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


		int resu = addInteractionEventToDatabase(event);
		System.out.println(resu);
	}

}

