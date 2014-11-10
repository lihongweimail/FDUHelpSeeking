package cn.edu.fudan.se.helpseeking.bean;

import java.util.ArrayList;
import java.util.List;

import cn.edu.fudan.se.helpseeking.util.CommUtil;
import cn.edu.fudan.se.helpseeking.util.INIHelper;
import cn.edu.fudan.se.helpseeking.util.Resource;
import cn.edu.fudan.se.helpseeking.bean.CxKeyPair;


public class Basic {

	public enum AttentionAction {
		
		Activated, 
		BroughtToTop,ChangedTo,
		Closed,Deactivated,
		Open;
	//("Problem View Changed");
		
		/**
		 * @return The corresponding event based on the string provided, or null
		 *         if no such STring.
		 */
		public static AttentionAction fromString(String string) {
			if (string == null) {
				return null;
			}
			if (string.toLowerCase().equals("BroughtToTop".toLowerCase())) { //$NON-NLS-1$
				return BroughtToTop;
			}
			if (string.toLowerCase().equals("Open".toLowerCase())) { //$NON-NLS-1$
				return Open;
			}
			if (string.toLowerCase().equals("Closed".toLowerCase())) { //$NON-NLS-1$
				return Closed;
			}
			if (string.toLowerCase().equals("Activated".toLowerCase())) { //$NON-NLS-1$
				return Activated;
			}
			if (string.toLowerCase().equals("Deactivated".toLowerCase())) { //$NON-NLS-1$
				return Deactivated;
			}
			if (string.toLowerCase().equals("ChangedTo".toLowerCase())) { //$NON-NLS-1$
				return ChangedTo;
			}
					
			return null;
		}

		public  boolean isChooseAction() {
			return this == BroughtToTop || this == Open 
					|| this == Closed || this == Activated
					|| this == Deactivated || this == ChangedTo;

		}
		@Override
		public String toString() {
			switch (this) {
			case BroughtToTop:
				return "BroughtToTop"; //$NON-NLS-1$
			case Open:
				return "Open"; //$NON-NLS-1$
			case Closed:
				return "Closed"; //$NON-NLS-1$
			case Activated:
				return "Activated"; //$NON-NLS-1$
			case Deactivated:
				return "Deactivated"; //$NON-NLS-1$
			case ChangedTo:
				return "ChangedTo"; //$NON-NLS-1$
		
			default:
				return "null"; //$NON-NLS-1$
			}
		}
	}
	
	
// SelectionAction:
	
//	ac.setActionKind(Kind.SELECTION);
//	ac.setActionName("PartOpen");
	
	public enum CommandAction {
		
		Save,HelpCommand, 
		InlineEdit;
	
		/**
		 * @return The corresponding event based on the string provided, or null
		 *         if no such STring.
		 */
		public static CommandAction fromString(String string) {
			if (string == null) {
				return null;
			}
			if (string.toLowerCase().equals("Save".toLowerCase())) { //$NON-NLS-1$
				return Save;
			}
			if (string.toLowerCase().equals("HelpCommand".toLowerCase())) { //$NON-NLS-1$
				return HelpCommand;
			}
			if (string.toLowerCase().equals("InlineEdit".toLowerCase())) { //$NON-NLS-1$
				return InlineEdit;
			}
			return null;
		}

		public  boolean isChooseAction() {
			return this==Save ||this == HelpCommand || this == InlineEdit;

		}
		@Override
		public String toString() {
			switch (this) {
			case Save:
				return "Save";  //$NON-NLS-1$
			case HelpCommand:
				return "HelpCommand"; //$NON-NLS-1$
			case InlineEdit:
				return "InlineEdit"; //$NON-NLS-1$
		
				
			default:
				return "null"; //$NON-NLS-1$
			}
		}
	}

	//	编译信息
//	策略是监听problem view当有change事件时,更新内存单例，在每个动作时候都去获取信息：
//	如果变更则取出所有信息，并按照先error后warning信息；
//	如果有单击或双击选择事件，则取该项目信息。
	public enum CompileInfoType {
		ERROR,
		WARNING;
		
		/**
		 * @return The corresponding event based on the string provided, or null
		 *         if no such STring.
		 */
		public static CompileInfoType fromString(String string) {
			if (string == null) {
				return null;
			}
			if (string.toUpperCase().equals("ERROR")) { //$NON-NLS-1$
				return ERROR;
			}
			if (string.toUpperCase().equals("WARNING")) { //$NON-NLS-1$
				return WARNING;
			}
			return null;
		}

		@Override
		public String toString() {
			switch (this) {
			case ERROR:
				return "ERROR"; //$NON-NLS-1$
			case WARNING:
				return "WARNING"; //$NON-NLS-1$	
			default:
				return "null"; //$NON-NLS-1$
			}
		}
	}

	
	
	public enum DebugAction {
		
		AddLineBreakpoint, AddMethodBreakpoint,
		ChangeLineBreakpoint,ChangeMethodBreakpoint,
		DeleteLineBreakpoint,DeleteMethodBreakpoint,
		Resume,StepEnd,StepInto,
		StepOver,StepReturn,Suspend,Terminate ;
		
	
		/**
		 * @return The corresponding event based on the string provided, or null
		 *         if no such STring.
		 */
		public static DebugAction fromString(String string) {
			if (string == null) {
				return null;
			}
			if (string.toLowerCase().equals("AddLineBreakpoint".toLowerCase())) { //$NON-NLS-1$
				return AddLineBreakpoint;
			}
			if (string.toLowerCase().equals("AddMethodBreakpoint".toLowerCase())) { //$NON-NLS-1$
				return AddMethodBreakpoint;
			}
			if (string.toLowerCase().equals("DeleteLineBreakpoint".toLowerCase())) { //$NON-NLS-1$
				return DeleteLineBreakpoint;
			}
			if (string.toLowerCase().equals("DeleteMethodBreakpoint".toLowerCase())) { //$NON-NLS-1$
				return DeleteMethodBreakpoint;
			}
			if (string.toLowerCase().equals("ChangeLineBreakpoint".toLowerCase())) { //$NON-NLS-1$
				return ChangeLineBreakpoint;
			}
			if (string.toLowerCase().equals("ChangeMethodBreakpoint".toLowerCase())) { //$NON-NLS-1$
				return ChangeMethodBreakpoint;
			}
			if (string.toLowerCase().equals("Suspend".toLowerCase())) { //$NON-NLS-1$
				return Suspend;
			}
			if (string.toLowerCase().equals("Resume".toLowerCase())) { //$NON-NLS-1$
				return Resume;
			}
			if (string.toLowerCase().equals("Terminate".toLowerCase())) { //$NON-NLS-1$
				return Terminate;
			}
			if (string.toLowerCase().equals("StepInto".toLowerCase())) { //$NON-NLS-1$
				return StepInto;
			}
			if (string.toLowerCase().equals("StepReturn".toLowerCase())) { //$NON-NLS-1$
				return StepReturn;
			}
			if (string.toLowerCase().equals("StepEnd".toLowerCase())) { //$NON-NLS-1$
				return StepEnd;
			}
			if (string.toLowerCase().equals("StepOver".toLowerCase())) { //$NON-NLS-1$
				return StepOver;
			}			
			return null;
		}

		public  boolean isChooseAction() {
			return this == AddLineBreakpoint || this == AddMethodBreakpoint 
					|| this == DeleteLineBreakpoint || this == DeleteMethodBreakpoint
					|| this == ChangeLineBreakpoint || this == ChangeMethodBreakpoint
					|| this == Suspend || this == Resume  || this == Terminate
					|| this == StepInto || this == StepReturn || this == StepEnd || this == StepOver;

		}
		/**
		 * @return Simple string representation of the enum or "null" if
		 *         no such kind.
		 */
		@Override
		public String toString() {
			switch (this) {
			case AddLineBreakpoint:
				return "AddLineBreakpoint"; //$NON-NLS-1$
			case AddMethodBreakpoint:
				return "AddMethodBreakpoint"; //$NON-NLS-1$
			case DeleteLineBreakpoint:
				return "DeleteLineBreakpoint"; //$NON-NLS-1$
			case DeleteMethodBreakpoint:
				return "DeleteMethodBreakpoint"; //$NON-NLS-1$
			case ChangeLineBreakpoint:
				return "ChangeLineBreakpoint"; //$NON-NLS-1$
			case ChangeMethodBreakpoint:
				return "ChangeMethodBreakpoint"; //$NON-NLS-1$
			case Suspend:
				return "Suspend"; //$NON-NLS-1$
			case Resume:
				return "Resume"; //$NON-NLS-1$
			case Terminate:
				return "Terminate"; //$NON-NLS-1$
			case StepInto:
				return "StepInto"; //$NON-NLS-1$
			case StepReturn:
				return "StepReturn"; //$NON-NLS-1$
			case StepEnd:
				return "StepEnd"; //$NON-NLS-1$
			case StepOver:
				return "StepOver"; //$NON-NLS-1$
			default:
				return "null"; //$NON-NLS-1$
			}
		}
	}
	
	
	public enum EditAction {
		
		AddClass,AddField, AddImportDeclaration,AddMethod,
		ChangeClass,ChangeField, ChangeImportDeclaration,ChangeMethod,
		ContentAssistSelected,DeleteClass, DeleteField,DeleteImportDeclaration,
		DeleteMethod;
	
		/**
		 * @return The corresponding event based on the string provided, or null
		 *         if no such STring.
		 */
		public static EditAction fromString(String string) {
			if (string == null) {
				return null;
			}
			if (string.toLowerCase().equals("AddField".toLowerCase())) { //$NON-NLS-1$
				return AddField;
			}
			if (string.toLowerCase().equals("AddMethod".toLowerCase())) { //$NON-NLS-1$
				return AddMethod;
			}
			if (string.toLowerCase().equals("AddImportDeclaration".toLowerCase())) { //$NON-NLS-1$
				return AddImportDeclaration;
			}
			if (string.toLowerCase().equals("AddClass".toLowerCase())) { //$NON-NLS-1$
				return AddClass;
			}
			if (string.toLowerCase().equals("DeleteField".toLowerCase())) { //$NON-NLS-1$
				return DeleteField;
			}
			if (string.toLowerCase().equals("DeleteMethod".toLowerCase())) { //$NON-NLS-1$ 
				return DeleteMethod;
			}
			if (string.toLowerCase().equals("DeleteImportDeclaration".toLowerCase())) { //$NON-NLS-1$
				return DeleteImportDeclaration;
			}
			if (string.toLowerCase().equals("DeleteClass".toLowerCase())) { //$NON-NLS-1$
				return DeleteClass;
			}
			if (string.toLowerCase().equals("ChangeField".toLowerCase())) { //$NON-NLS-1$
				return ChangeField;
			}
			if (string.toLowerCase().equals("ChangeMethod".toLowerCase())) { //$NON-NLS-1$
				return ChangeMethod;
			}
			if (string.toLowerCase().equals("ChangeImportDeclaration".toLowerCase())) { //$NON-NLS-1$
				return ChangeImportDeclaration;
			}
			if (string.toLowerCase().equals("ChangeClass".toLowerCase())) { //$NON-NLS-1$
				return ChangeClass;
			}
			if (string.toLowerCase().equals("ContentAssistSelected".toLowerCase())) { //$NON-NLS-1$
				return ContentAssistSelected;
			}
			
					
			return null;
		}

		public  boolean isChooseAction() {
			return this == AddField || this == AddMethod || this == AddImportDeclaration || this == AddClass 
					|| this == DeleteField || this == DeleteMethod || this == DeleteImportDeclaration || this == DeleteClass 
					|| this == ChangeField || this == ChangeMethod || this == ChangeImportDeclaration || this == ChangeClass 
					|| this == ContentAssistSelected;

		}
		@Override
		public String toString() {
			switch (this) {
			case AddField:
				return "AddField"; //$NON-NLS-1$
			case AddMethod:
				return "AddMethod"; //$NON-NLS-1$
			case AddImportDeclaration:
				return "AddImportDeclaration"; //$NON-NLS-1$
			case AddClass:
				return "AddClass"; //$NON-NLS-1$
			case DeleteField:
				return "DeleteField"; //$NON-NLS-1$
			case DeleteMethod:
				return "DeleteMethod"; //$NON-NLS-1$
			case DeleteImportDeclaration:
				return "DeleteImportDeclaration"; //$NON-NLS-1$
			case DeleteClass:
				return "DeleteClass"; //$NON-NLS-1$
			case ChangeField:
				return "ChangeField"; //$NON-NLS-1$
			case ChangeMethod:
				return "ChangeMethod"; //$NON-NLS-1$
			case ChangeImportDeclaration:
				return "ChangeImportDeclaration"; //$NON-NLS-1$
			case ChangeClass:
				return "ChangeClass"; //$NON-NLS-1$
			case ContentAssistSelected:
				return "ContentAssistSelected"; //$NON-NLS-1$

			default:
				return "null"; //$NON-NLS-1$
			}
		}
	}


	public enum KeyWordsLevel {
			Level_Five,
			Level_Four,
			Level_One,
			Level_Seven,
			Level_Six,
			Level_Three,
			Level_Two,
			Other;
  
			public int getLevelNumber() {
				switch (this) {
				case Level_One:
					return 1; //$NON-NLS-1$
				case Level_Two:
					return 2; //$NON-NLS-1$
				case Level_Three:
					return 3; //$NON-NLS-1$
				case Level_Four:
					return 4; //$NON-NLS-1$
				case Level_Five:
					return 5; //$NON-NLS-1$
				case Level_Six:
					return 6; //$NON-NLS-1$
				case Level_Seven:
					return 7; //$NON-NLS-1$
				case Other:
					return 0; //$NON-NLS-1$			
				default:
					return -1; //$NON-NLS-1$
				}
			}

		}

	
/**
	 * Determines the type of interaction that took place, either initiated by
	 * the user or done on behalf of the user.
	 */
	public enum Kind {
		/**
		 * Capture interaction with tasks, the workbench, and lifecycle events
		 * that define where the user's attention is directed.
		 */
		ATTENTION,

		/**
		 * Commands and actions invoked via buttons, menus, and keyboard
		 * shortcuts.
		 */
		COMMAND, DEBUG,
		/**
		 * Edit events that are created by text selections in an editor.
		 */
		EDIT,

		JOB,

		/**
		 * Direct manipulation of interest via actions such as
		 * "Mark as Landmark" and "Mark Less Interesting".
		 */
		MANIPULATION,

		/**
		 * Candidates for future interaction.
		 */
		PREDICTION,

		/**
		 * Workbench preference changes, sometimes made by the user, sometimes
		 * automatically on behalf of the user.
		 */
		PREFERENCE,

		/**
		 * Indirect user interaction with elements (e.g. parent gets implicitly
		 * selected when element is selected).
		 */
		PROPAGATION, RESOURCE, REVISION, /**
		 * User selection of elements, issued by the Eclipse post-selection
		 * mechanism.
		 */
		SELECTION, TEST,SELECTIONFOCUS;

		/**
		 * @return The corresponding event based on the string provided, or null
		 *         if no such STring.
		 */
		public static Kind fromString(String string) {
			if (string == null) {
				return null;
			}
			if (string.toLowerCase().equals("selection".toLowerCase())) { //$NON-NLS-1$
				return SELECTION;
			}
			if (string.toLowerCase().equals("selectionfocus".toLowerCase())) { //$NON-NLS-1$
				return SELECTIONFOCUS;
			}
			if (string.toLowerCase().equals("edit".toLowerCase())) { //$NON-NLS-1$
				return EDIT;
			}
			if (string.toLowerCase().equals("debug".toLowerCase())) { //$NON-NLS-1$
				return DEBUG;
			}
			if (string.toLowerCase().equals("command".toLowerCase())) { //$NON-NLS-1$
				return COMMAND;
			}
			if (string.toLowerCase().equals("preference".toLowerCase())) { //$NON-NLS-1$
				return PREFERENCE;
			}
			if (string.toLowerCase().equals("prediction".toLowerCase())) { //$NON-NLS-1$
				return PREDICTION;
			}
			if (string.toLowerCase().equals("propagation".toLowerCase())) { //$NON-NLS-1$
				return PROPAGATION;
			}
			if (string.toLowerCase().equals("manipulation".toLowerCase())) { //$NON-NLS-1$
				return MANIPULATION;
			}
			if (string.toLowerCase().equals("attention".toLowerCase())) { //$NON-NLS-1$
				return ATTENTION;
			}
			if (string.toLowerCase().equals("resource".toLowerCase())) { //$NON-NLS-1$
				return RESOURCE;
			}
			if (string.toLowerCase().equals("revision".toLowerCase())) { //$NON-NLS-1$
				return REVISION;
			}
			if (string.toLowerCase().equals("test".toLowerCase())) { //$NON-NLS-1$
				return TEST;
			}
			if (string.toLowerCase().equals("job".toLowerCase())) { //$NON-NLS-1$
				return JOB;
			}
			return null;
		}

		/**
		 * TODO: add PREFERENCE?
		 */
		public boolean isUserEvent() {
			return this == SELECTION || this==SELECTIONFOCUS|| this == EDIT || this == COMMAND
					|| this == PREFERENCE || this==DEBUG || this == TEST ;
		}

		/**
		 * @return Simple string representation of the event kind or "null" if
		 *         no such kind.
		 */
		@Override
		public String toString() {
			switch (this) {
			case SELECTION:
				return "selection"; //$NON-NLS-1$
			case SELECTIONFOCUS:
				return "selectionfocus";
			case EDIT:
				return "edit"; //$NON-NLS-1$
			case DEBUG:
				return "debug"; //$NON-NLS-1$
			case COMMAND:
				return "command"; //$NON-NLS-1$
			case PREFERENCE:
				return "preference"; //$NON-NLS-1$
			case PREDICTION:
				return "prediction"; //$NON-NLS-1$
			case PROPAGATION:
				return "propagation"; //$NON-NLS-1$
			case MANIPULATION:
				return "manipulation"; //$NON-NLS-1$
			case ATTENTION:
				return "attention"; //$NON-NLS-1$
			case RESOURCE:
				return "resource"; //$NON-NLS-1$
			case REVISION:
				return "revision"; //$NON-NLS-1$
			case TEST:
				return "test"; //$NON-NLS-1$
			case JOB:
				return "job"; //$NON-NLS-1$
			default:
				return "null"; //$NON-NLS-1$
			}
		}
	}

	
public enum QueryLevel {
		High,
		Middle,
		Other;
		
		/**
		 * @return The corresponding event based on the string provided, or null
		 *         if no such STring.
		 */
		public static QueryLevel fromString(String string) {
			if (string == null) {
				return null;
			}
			if (string.toLowerCase().equals("High".toLowerCase())) { //$NON-NLS-1$
				return High;
			}
			if (string.toLowerCase().equals("Middle".toLowerCase())) { //$NON-NLS-1$
				return Middle;
			}
			if (string.toLowerCase().equals("Other".toLowerCase())) { //$NON-NLS-1$
				return Other;
			}
			return null;
		}

		@Override
		public String toString() {
			switch (this) {
			case High:
				return "High"; //$NON-NLS-1$
			case Middle:
				return "Middle"; //$NON-NLS-1$
			case Other:
				return "Other"; //$NON-NLS-1$			
			default:
				return "null"; //$NON-NLS-1$
			}
		}
	}

	
	
	//	运行时信息：
//	需要考虑如何从console中的输出中区分type： 
//	1 framework message 
//  2 program output
//  3 exceptional message（*这个信息更重要）
	public enum RuntimeInfoType {
		ExceptionalMessage,
		FrameWorkMessage,
		ProgramOutput;
		
		/**
		 * @return The corresponding event based on the string provided, or null
		 *         if no such STring.
		 */
		public static RuntimeInfoType fromString(String string) {
			if (string == null) {
				return null;
			}
			if (string.toLowerCase().equals("FrameWorkMessage".toLowerCase())) { //$NON-NLS-1$
				return FrameWorkMessage;
			}
			if (string.toLowerCase().equals("ProgramOutput".toLowerCase())) { //$NON-NLS-1$
				return ProgramOutput;
			}
			if (string.toLowerCase().equals("ExceptionalMessage".toLowerCase())) { //$NON-NLS-1$
				return ExceptionalMessage;
			}
			return null;
		}

		@Override
		public String toString() {
			switch (this) {
			case FrameWorkMessage:
				return "FrameWorkMessage"; //$NON-NLS-1$
			case ProgramOutput:
				return "ProgramOutput"; //$NON-NLS-1$
			case ExceptionalMessage:
				return "ExceptionalMessage"; //$NON-NLS-1$			
			default:
				return "null"; //$NON-NLS-1$
			}
		}
	}


	public enum SelectionAction {
		
		SelectPart;
	
		/**
		 * @return The corresponding event based on the string provided, or null
		 *         if no such STring.
		 */
		public static SelectionAction fromString(String string) {
			if (string == null) {
				return null;
			}
			if (string.toLowerCase().equals("SelectPart".toLowerCase())) { //$NON-NLS-1$
				return SelectPart;
			}
				
			return null;
		}

		public  boolean isChooseAction() {
			return this == SelectPart ;

		}
		@Override
		public String toString() {
			switch (this) {
			case SelectPart:
				return "SelectPart"; //$NON-NLS-1$
			

			default:
				return "null"; //$NON-NLS-1$
			}
		}
	}
	
//	private static final String SPLIT_STRING =  "[&#$_.(){}!*%+-=><\\:;,?/\"\'\t\b\r\n\0 ]";

	public static final long Auto_Search_Timer = 2*60*1000;//自动检索间隔时间
	public static final int Auto_Search_Try = 10;//自动检索次数

	public final static double DISTANCE_RATIO=0.01;
	
	
	
	
	
	public final static double FREQUENCY_RATIO=0.01;
	public static final int History_SearchList_Size = 5;
	//		另外，有必要建立一个异常列表文件，记录各种异常名称，如果以上信息中出现了该异常词汇，则该异常词汇权重为基本权重两倍(weightTwo)！
	public static final  String javaExceptionalFileName ="/StopResource/javaExceptionalName.txt";
	
	//权重  距离   老化   频率
	public static final String SPLIT_STRING =  "[\\[\\]&#$_.()@|{}!*%+-=><\\:;,?/\"\'\t\b\r\n\0 ]";
	public static final  Resource myResource=new Resource();
	public static final String javaExceptionalName = myResource.getResource(javaExceptionalFileName );
	public static final List<String> javaExceptionalNameList=CommUtil.arrayToList((javaExceptionalName).split(SPLIT_STRING));
	//保存到数据库中最大的候选检索词保留数量
	public final static int MAX_CANDIDATE_KEYWORDS=100;
	public static final int MAX_DEEP_PACKAGE_COUNT = 10;

	public static final long MINI_Action_Time = 3000; //最小动作间隔时间3秒？

	public static final int Mini_Actions = 5; //最小的动作范围

	public final static double OLD_RATIO=0.1; //老化比率

	//动作的滑动窗口大小： 经验上 15分钟的动作在300个左右
	public final static int SLIDE_WINDOW_SIZE=30;  //保留的最大数量的历史窗口数量，工具在这个窗口内可以使用countwindow作为焦点框住的内容进行分析
	public static final int CountWindowSize = 10;//处置窗口的范围； 要小于SLID_WINDOW_SIZE  编译受控版本时 设置这个值就好了。


	//从候选检索词中取词数量
	public final static int TEMP_K_KEYWORDS =8;  //或者提供给第一个foamtree1 （右上）的词的个数，目前认为8比较合适
	
	
	public final static int WAIT_GOOGLE_TIME=6000;
	public final static int GOOGLE_RESULT_NUMBERS=10; //default 10  to get 10 urls ; values as 1--10
	public final static int GOOGLE_SEARCH_ROUND=2; // default 10 for get 100 url  ; test as 1 for 10 url 
	

	public static INIHelper mycseini=new INIHelper("/cse.ini",true);
	public final static List<CxKeyPair> ALL_CX_KEY_PAIRS = mycseini.getCxKeyPairs();
	                                                     //CommUtil.getKeyCx();
	public static final int historyQueryWindowCount = 3;
	
	public final static int FOAMTREETOPICCOUNT=6; //提供给foamtree2 （右下）的topic的数量
	public final static int FOAMTREETOPICWORDSLIMITIED=6; //每个topic 中提取出前六个词
	public static final int DISPLAYTOPICFOAMTREEPLAN = 2;  //值为1 或其他值 则为典型的两层显示，topic一层，topic的词一层；  值为2时，是将所有topic的词综合起来一层显示
	public static int SELECTTOPICWORDNUMBER=1; //从topic中选择前三个词作为topic的标题名字
	public static boolean isProcessingQuilifedName=false; 	//???? LDA前 选择是否开启 处理 包和类 方法 签名串  
	
	public final static double  RATIOOFNEWSEARCHSTRING=0.1;  //给定一个值，两个串中的不相同的词大于这个值，则容许刷新新的一组关键词
	public static final long CLAWLIMITEDTIME = 5000;//爬取网络页面时，等待的时间 毫秒，超时后，就放弃，以改善交互
	public  static int ALGORITHMSELECTION=2; //算法设置，1为5月份的算法的关键词数据初值设置， 2 为11月新算法的关键词初值设置
	
	//新算法权重
	//对于action来说有两个权重设置：  weightOne  和 weightTwo  他们分别用于描述  interest level  中 action 的级别 以及 API 来源的级别
	//  DOI -model  (Degree  of Interestes)
	// interest level action :     select 1 <  reveal 2 < save or debug 3 < execute 4
	// interest level api   :       normal 1 < has-compile_error 2 < cause_exception 3
	
		public final static int action_select=1;
		public final static int action_reveal=2;
		public final static int action_saveOrDebug=3;
		public final static int action_execute=4;
		public final static int action_selectfocus=5;
		
		public final static int api_normal=1;
		public final static int api_hasCompileError=2;
		public final static int api_causeException=3;
		
		public final static double gama=3.0;  //新算法权重值
		public static  int CLAWMODE = 1;  //爬取网页的技术  1：使用线程池  2：使用GBK编码获取网页，非线程  3： 雪娇提供的stringbean方案
	
	
		//光标所在行附近的代码：  目前是设置为前后2行；共计5行；为了更加关注，设置为1行，就自己
		public static int linelimited=3;   //3  发现新算法的词太少，所以这个代码行数量 还是设置为3 同时 词的集合变化太少 变化率RATIOOFNEWSEARCHSTRING设置为0.1 （原来设置0.6 或0.38）
}
