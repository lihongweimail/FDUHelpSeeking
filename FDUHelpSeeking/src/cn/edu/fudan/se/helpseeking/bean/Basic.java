package cn.edu.fudan.se.helpseeking.bean;

import java.util.ArrayList;
import java.util.List;

import cn.edu.fudan.se.helpseeking.util.CommUtil;
import cn.edu.fudan.se.helpseeking.util.INIHelper;
import cn.edu.fudan.se.helpseeking.util.Resource;
import cn.edu.fudan.se.helpseeking.bean.CxKeyPair;

public class Basic {

	public enum AttentionAction {

		Activated, BroughtToTop, ChangedTo, Closed, Deactivated, Open;
		// ("Problem View Changed");

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

		public boolean isChooseAction() {
			return this == BroughtToTop || this == Open || this == Closed
					|| this == Activated || this == Deactivated
					|| this == ChangedTo;

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

	// ac.setActionKind(Kind.SELECTION);
	// ac.setActionName("PartOpen");

	public enum CommandAction {

		Save, HelpCommand, InlineEdit;

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

		public boolean isChooseAction() {
			return this == Save || this == HelpCommand || this == InlineEdit;

		}

		@Override
		public String toString() {
			switch (this) {
			case Save:
				return "Save"; //$NON-NLS-1$
			case HelpCommand:
				return "HelpCommand"; //$NON-NLS-1$
			case InlineEdit:
				return "InlineEdit"; //$NON-NLS-1$

			default:
				return "null"; //$NON-NLS-1$
			}
		}
	}

	// 编译信息
	// 策略是监听problem view当有change事件时,更新内存单例，在每个动作时候都去获取信息：
	// 如果变更则取出所有信息，并按照先error后warning信息；
	// 如果有单击或双击选择事件，则取该项目信息。
	public enum CompileInfoType {
		ERROR, WARNING;

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

		AddLineBreakpoint, AddMethodBreakpoint, ChangeLineBreakpoint, ChangeMethodBreakpoint, DeleteLineBreakpoint, DeleteMethodBreakpoint, Resume, StepEnd, StepInto, StepOver, StepReturn, Suspend, Terminate;

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
			if (string.toLowerCase()
					.equals("AddMethodBreakpoint".toLowerCase())) { //$NON-NLS-1$
				return AddMethodBreakpoint;
			}
			if (string.toLowerCase().equals(
					"DeleteLineBreakpoint".toLowerCase())) { //$NON-NLS-1$
				return DeleteLineBreakpoint;
			}
			if (string.toLowerCase().equals(
					"DeleteMethodBreakpoint".toLowerCase())) { //$NON-NLS-1$
				return DeleteMethodBreakpoint;
			}
			if (string.toLowerCase().equals(
					"ChangeLineBreakpoint".toLowerCase())) { //$NON-NLS-1$
				return ChangeLineBreakpoint;
			}
			if (string.toLowerCase().equals(
					"ChangeMethodBreakpoint".toLowerCase())) { //$NON-NLS-1$
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

		public boolean isChooseAction() {
			return this == AddLineBreakpoint || this == AddMethodBreakpoint
					|| this == DeleteLineBreakpoint
					|| this == DeleteMethodBreakpoint
					|| this == ChangeLineBreakpoint
					|| this == ChangeMethodBreakpoint || this == Suspend
					|| this == Resume || this == Terminate || this == StepInto
					|| this == StepReturn || this == StepEnd
					|| this == StepOver;

		}

		/**
		 * @return Simple string representation of the enum or "null" if no such
		 *         kind.
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

		AddClass, AddField, AddImportDeclaration, AddMethod, ChangeClass, ChangeField, ChangeImportDeclaration, ChangeMethod, ContentAssistSelected, DeleteClass, DeleteField, DeleteImportDeclaration, DeleteMethod;

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
			if (string.toLowerCase().equals(
					"AddImportDeclaration".toLowerCase())) { //$NON-NLS-1$
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
			if (string.toLowerCase().equals(
					"DeleteImportDeclaration".toLowerCase())) { //$NON-NLS-1$
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
			if (string.toLowerCase().equals(
					"ChangeImportDeclaration".toLowerCase())) { //$NON-NLS-1$
				return ChangeImportDeclaration;
			}
			if (string.toLowerCase().equals("ChangeClass".toLowerCase())) { //$NON-NLS-1$
				return ChangeClass;
			}
			if (string.toLowerCase().equals(
					"ContentAssistSelected".toLowerCase())) { //$NON-NLS-1$
				return ContentAssistSelected;
			}

			return null;
		}

		public boolean isChooseAction() {
			return this == AddField || this == AddMethod
					|| this == AddImportDeclaration || this == AddClass
					|| this == DeleteField || this == DeleteMethod
					|| this == DeleteImportDeclaration || this == DeleteClass
					|| this == ChangeField || this == ChangeMethod
					|| this == ChangeImportDeclaration || this == ChangeClass
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
		Level_Five, Level_Four, Level_One, Level_Seven, Level_Six, Level_Three, Level_Two, Other;

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
		 * User selection of elements, issued
		 * by the Eclipse post-selection mechanism.
		 */
		SELECTION, TEST, SELECTIONFOCUS;

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
			return this == SELECTION || this == SELECTIONFOCUS || this == EDIT
					|| this == COMMAND || this == PREFERENCE || this == DEBUG
					|| this == TEST;
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
		High, Middle, Other;

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

	// 运行时信息：
	// 需要考虑如何从console中的输出中区分type：
	// 1 framework message
	// 2 program output
	// 3 exceptional message（*这个信息更重要）
	public enum RuntimeInfoType {
		ExceptionalMessage, FrameWorkMessage, ProgramOutput;

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

		public boolean isChooseAction() {
			return this == SelectPart;

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

	// private static final String SPLIT_STRING =
	// "[&#$_.(){}!*%+-=><\\:;,?/\"\'\t\b\r\n\0 ]";

	
	
	public static final long Auto_Search_Timer = 2 * 60 * 1000;// 自动检索间隔时间
	public static final int Auto_Search_Try = 10;// 自动检索次数

	public final static double DISTANCE_RATIO = 0.01;

	public final static double FREQUENCY_RATIO = 0.01;
	public static final int History_SearchList_Size = 5;
	// 另外，有必要建立一个异常列表文件，记录各种异常名称，如果以上信息中出现了该异常词汇，则该异常词汇权重为基本权重两倍(weightTwo)！
	public static final String javaExceptionalFileName = "/StopResource/javaExceptionalName.txt";

		public static final String SPLIT_STRING = "[\\[\\]&#$_.()@|{}!*%+-=><\\:;,?/\"\'\t\b\r\n\0 ]";
	public static final Resource myResource = new Resource();
	public static final String javaExceptionalName = myResource
			.getResource(javaExceptionalFileName);
	public static final List<String> javaExceptionalNameList = CommUtil
			.arrayToList((javaExceptionalName).split(SPLIT_STRING));
	// 保存到数据库中最大的候选检索词保留数量
	public final static int MAX_CANDIDATE_KEYWORDS = 100;
	
	public static final int MAX_DEEP_PACKAGE_COUNT = 10;
	
	// 权重 距离 老化 频率
	public static final long MINI_Action_Time = 3000; // 最小动作间隔时间3秒？

	public static final int Mini_Actions = 5; // 最小的动作范围

	public final static double OLD_RATIO = 0.1; // 老化比率

	// 动作的滑动窗口大小： 经验上 15分钟的动作在300个左右
	public final static int SLIDE_WINDOW_SIZE = 30; 
	// 保留的最大数量的历史窗口数量，工具在这个窗口内可以使用countwindow作为焦点框住的内容进行分析
	// 2015.2.11 新算法这个窗口值实际上是无限大的  后面适当将这个值 调大到1000
	
	public static final int CountWindowSize = 10;// 处置窗口的范围； 要小于SLID_WINDOW_SIZE  
	                                            // 目前2015.2.11新算法实际上这个窗口也是无限大，不妨设置为SLIDE_WINDOW_SIZE-1的值
													// ！！！编译受控版本时 设置这个值就好了。 

	// 从候选检索词中取词数量
//	// 取走到配置
//	public final static int WAIT_GOOGLE_TIME = 6000;
//	// 取走到配置
//	public final static int GOOGLE_RESULT_NUMBERS = 10; // default 10 to get 10
//														// urls ; values as
//														// 1--10  用于取结果数量的，即一次检索后，从Google获得多少个URL
// 														// 一般为4  配合GOOGLE_SERCH_ROUND ， 即它们相乘为最后获得的URL数量
//	// 取走到配置
//	public final static int GOOGLE_SEARCH_ROUND = 3; // default 10 for get 100  max is 10
//														// url ; test as 1 get 10 urls
//                                                        // 用于设置获取url数量的参数，功能是，就一个查询的结果集中去取的次数，每次取的数量为GOOGLE_RESULT_NUMBERS个

	public static INIHelper mycseini = new INIHelper("/cse.ini", true);  
//	指定一些配置数据定制引擎的信息，可以给出的相对当前项目的路径文件名：  文件内容格式：
//	     例如这里有两个检索引擎：
//	        [cse0]
//			cx=005635559766885752621:ly8_ifzrwps
//			projectid=oval-crawler-574
//			key=AIzaSyAoB0pBvZ6veuzDQbR21auME8HJUwmCaos
//			csename=FDUHelpseekingOnStackoverflow
//			[cse1]
//			cx=005635559766885752621:va1etsiak-a
//			projectid=cnedufdusehelpseeking
//			key=AIzaSyCr7g1tTLyy1MYOT8osYiBhuNQX4Od5JFM
//			csename=helpseeking
//	        
	
	public final static List<CxKeyPair> ALL_CX_KEY_PAIRS = mycseini.getCxKeyPairs(); //从配置文件中获得定制检索引擎的参数
	// CommUtil.getKeyCx();
	public static final int historyQueryWindowCount = 3; 

	public final static int FOAMTREETOPICCOUNT = 6; // 提供给foamtree2   ICSE2015tooldemo 后取消了
													// （右下）的topic的数量
	public final static int FOAMTREETOPICWORDSLIMITIED = 6; // 每个topic 中提取出前六个词
	public static final int DISPLAYTOPICFOAMTREEPLAN = 2; // 值为1 或其他值   用于定制foamtree的格式的，目前新算法后都统一为一层默认为2
															// 则为典型的两层显示，topic一层，topic的词一层；
															// 值为2时，是将所有topic的词综合起来一层显示
	public static int SELECTTOPICWORDNUMBER = 1; // 从topic中选择前N个词作为topic的标题名字  
	public static boolean isProcessingQuilifedName = false; // ???? LDA前 选择是否开启
															// 处理 包和类 方法 签名串
//	// 取走到配置
//	public final static int TEMP_K_KEYWORDS = 8; // 或者提供给第一个foamtree1   
//	// 取走到配置
//	public static int DifferentPostion = 4;
//// 相同词按分值排序后位置不同  
////另一个参数，就是生成的新候选词集合和现在在foamtree中的词集合中词的rank值得变化有N个不同时更新foamtree
//	public static double RATIOOFNEWSEARCHSTRING = 0.24; 
////一个参数，就是生成的新候选词集合和现在在foamtree中的词集合中词的不同词的的比率情况，当大于这个值时更新foamtree
////调节foamtree的变化速率的参数值越大速率越慢，取值范围0<x<1。  给定一个值，两个串中的不相同的词大于这个值，则容许刷新新的一组关键词
//// 这个比例要按照系统已有的词的数量来动态调整，如果系统候选词多可以放大，如果系统词少例如就10个词的情况，就小一点
////词的集合变化太少
//// 变化率RATIOOFNEWSEARCHSTRING设置为0.1
//// （原来设置0.6 或0.38）
	
// 光标所在行附近的代码： 目前是设置为前后2行；共计5行；为了更加关注，设置为1行，就自己
public static int linelimited = 1; // 3 发现新算法的词太少，所以这个代码行数量 还是设置为3 同时
											// 词的集合变化太少

	public static final long CLAWLIMITEDTIME = 5000;// 爬取网络页面时，等待的时间
													// 毫秒，超时后，就放弃，以改善交互
	public static int ALGORITHMSELECTION = 2; // 算法设置，1为5月份的算法的关键词数据初值设置
												// 2为11月新算法的关键词初值设置

//	// 新算法权重
//	// 取走到配置
//	// 对于action来说有两个权重设置： weightOne 和 weightTwo 他们分别用于描述 interest level 中 action
//	// 的级别 以及 API 来源的级别
//	// DOI -model (Degree of Interestes)
//	// interest level action : select 1 < reveal 2 < save or debug 3 < execute 4
//	// interest level api : normal 1 < has-compile_error 2 < cause_exception 3
//
//	public final static int action_select = 1;
//	public final static int action_reveal = 2;
//	public final static int action_saveOrDebug = 3;
//	public final static int action_execute = 4;
//	public final static int action_selectfocus = 5;
//
//	public final static int api_normal = 1;
//	public final static int api_hasCompileError = 2;
//	public final static int api_causeException = 3;
//
//	public final static double gama = 2.0; // 新算法权重值
//
//
//	//  以上均需取走
	// public static int mockup=2;

	public static int CLAWMODE = 1; // 爬取网页的技术 1：使用线程池 2：使用GBK编码获取网页，非线程 
	// 3: 雪娇提供的stringbean方案
	

	//颜色， 颜色16制，反向颜色
	public final static int cssColorNumber=140;//请设置为：cssColor数组元素个数值-1  
	public final static String[][] cssColor = {
			{ "Black", "#000000", "White" }, 
			{ "Navy", "#000080", "Ivory" },
			{ "DarkBlue", "#00008B", "LightYellow" },
			{ "MediumBlue", "#0000CD", "Yellow" },
			{ "Blue", "#0000FF", "Snow" },
			{ "DarkGreen", "#006400", "FloralWhite" },
			{ "Green", "#008000", "LemonChiffon" },
			{ "Teal", "#008080", "Cornsilk" },
			{ "DarkCyan", "#008B8B", "SeaShell" },
			{ "DeepSkyBlue", "#00BFFF", "LavenderBlush" },
			{ "DarkTurquoise", "#00CED1", "PapayaWhip" },
			{ "MediumSpringGreen", "#00FA9A", "BlanchedAlmond" },
			{ "Lime", "#00FF00", "MistyRose" },
			{ "SpringGreen", "#00FF7F", "Bisque" },
			{ "Aqua", "#00FFFF", "Moccasin" },
			{ "Cyan", "#00FFFF", "NavajoWhite" },
			{ "MidnightBlue", "#191970", "PeachPuff" },
			{ "DodgerBlue", "#1E90FF", "Gold" },
			{ "LightSeaGreen", "#20B2AA", "Pink" },
			{ "ForestGreen", "#228B22", "LightPink" },
			{ "SeaGreen", "#2E8B57", "Orange" },
			{ "DarkSlateGray", "#2F4F4F", "LightSalmon" },
			{ "LimeGreen", "#32CD32", "Darkorange" },
			{ "MediumSeaGreen", "#3CB371", "Coral" },
			{ "Turquoise", "#40E0D0", "HotPink" },
			{ "RoyalBlue", "#4169E1", "Tomato" },
			{ "SteelBlue", "#4682B4", "OrangeRed" },
			{ "DarkSlateBlue", "#483D8B", "DeepPink" },
			{ "MediumTurquoise", "#48D1CC", "Fuchsia" },
			{ "Indigo", "#4B0082", "Magenta" },
			{ "DarkOliveGreen", "#556B2F", "Red" },
			{ "CadetBlue", "#5F9EA0", "OldLace" },
			{ "CornflowerBlue", "#6495ED", "LightGoldenRodYellow" },
			{ "MediumAquaMarine", "#66CDAA", "Linen" },
			{ "DimGray", "#696969", "AntiqueWhite" },
			{ "DimGrey", "#696969", "Salmon" },
			{ "SlateBlue", "#6A5ACD", "GhostWhite" },
			{ "OliveDrab", "#6B8E23", "MintCream" },
			{ "SlateGray", "#708090", "WhiteSmoke" },
			{ "LightSlateGray", "#778899", "Beige" },
			{ "MediumSlateBlue", "#7B68EE", "Wheat" },
			{ "LawnGreen", "#7CFC00", "SandyBrown" },
			{ "Chartreuse", "#7FFF00", "Azure" },
			{ "Aquamarine", "#7FFFD4", "HoneyDew" },
			{ "Maroon", "#800000", "AliceBlue" },
			{ "Purple", "#800080", "Khaki" },
			{ "Olive", "#808000", "LightCoral" },
			{ "Gray", "#808080", "PaleGoldenRod" },
			{ "SkyBlue", "#87CEEB", "Violet" },
			{ "LightSkyBlue", "#87CEFA", "DarkSalmon" },
			{ "BlueViolet", "#8A2BE2", "Lavender" },
			{ "DarkRed", "#8B0000", "LightCyan" },
			{ "DarkMagenta", "#8B008B", "BurlyWood" },
			{ "SaddleBrown", "#8B4513", "Plum" },
			{ "DarkSeaGreen", "#8FBC8F", "Gainsboro" },
			{ "LightGreen", "#90EE90", "Crimson" },
			{ "MediumPurple", "#9370DB", "PaleVioletRed" },
			{ "DarkViolet", "#9400D3", "GoldenRod" },
			{ "PaleGreen", "#98FB98", "Orchid" },
			{ "DarkOrchid", "#9932CC", "Thistle" },
			{ "YellowGreen", "#9ACD32", "LightGray" },
			{ "Sienna", "#A0522D", "White" },
			{ "Brown", "#A52A2A", "Chocolate" },
			{ "DarkGray", "#A9A9A9", "Peru" },
			{ "LightBlue", "#ADD8E6", "IndianRed" },
			{ "GreenYellow", "#ADFF2F", "MediumVioletRed" },
			{ "PaleTurquoise", "#AFEEEE", "Silver" },
			{ "LightSteelBlue", "#B0C4DE", "DarkKhaki" },
			{ "PowderBlue", "#B0E0E6", "RosyBrown" },
			{ "FireBrick", "#B22222", "MediumOrchid" },
			{ "DarkGoldenRod", "#B8860B", "DarkGoldenRod" },
			{ "MediumOrchid", "#BA55D3", "FireBrick" },
			{ "RosyBrown", "#BC8F8F", "PowderBlue" },
			{ "DarkKhaki", "#BDB76B", "LightSteelBlue" },
			{ "Silver", "#C0C0C0", "PaleTurquoise" },
			{ "MediumVioletRed", "#C71585", "GreenYellow" },
			{ "IndianRed", "#CD5C5C", "LightBlue" },
			{ "Peru", "#CD853F", "DarkGray" },
			{ "Chocolate", "#D2691E", "Brown" },
			{ "Tan", "#D2B48C", "White" },
			{ "LightGray", "#D3D3D3", "YellowGreen" },
			{ "Thistle", "#D8BFD8", "DarkOrchid" },
			{ "Orchid", "#DA70D6", "PaleGreen" },
			{ "GoldenRod", "#DAA520", "DarkViolet" },
			{ "PaleVioletRed", "#DB7093", "MediumPurple" },
			{ "Crimson", "#DC143C", "LightGreen" },
			{ "Gainsboro", "#DCDCDC", "DarkSeaGreen" },
			{ "Plum", "#DDA0DD", "SaddleBrown" },
			{ "BurlyWood", "#DEB887", "DarkMagenta" },
			{ "LightCyan", "#E0FFFF", "DarkRed" },
			{ "Lavender", "#E6E6FA", "BlueViolet" },
			{ "DarkSalmon", "#E9967A", "LightSkyBlue" },
			{ "Violet", "#EE82EE", "SkyBlue" },
			{ "PaleGoldenRod", "#EEE8AA", "Gray" },
			{ "LightCoral", "#F08080", "Olive" },
			{ "Khaki", "#F0E68C", "Purple" },
			{ "AliceBlue", "#F0F8FF", "Maroon" },
			{ "HoneyDew", "#F0FFF0", "Aquamarine" },
			{ "Azure", "#F0FFFF", "Chartreuse" },
			{ "SandyBrown", "#F4A460", "LawnGreen" },
			{ "Wheat", "#F5DEB3", "MediumSlateBlue" },
			{ "Beige", "#F5F5DC", "LightSlateGray" },
			{ "WhiteSmoke", "#F5F5F5", "SlateGray" },
			{ "MintCream", "#F5FFFA", "OliveDrab" },
			{ "GhostWhite", "#F8F8FF", "SlateBlue" },
			{ "Salmon", "#FA8072", "DimGray" },
			{ "AntiqueWhite", "#FAEBD7", "DimGrey" },
			{ "Linen", "#FAF0E6", "MediumAquaMarine" },
			{ "LightGoldenRodYellow", "#FAFAD2", "CornflowerBlue" },
			{ "OldLace", "#FDF5E6", "CadetBlue" },
			{ "Red", "#FF0000", "DarkOliveGreen" },
			{ "Fuchsia", "#FF00FF", "Indigo" },
			{ "Magenta", "#FF00FF", "MediumTurquoise" },
			{ "DeepPink", "#FF1493", "DarkSlateBlue" },
			{ "OrangeRed", "#FF4500", "SteelBlue" },
			{ "Tomato", "#FF6347", "RoyalBlue" },
			{ "HotPink", "#FF69B4", "Turquoise" },
			{ "Coral", "#FF7F50", "MediumSeaGreen" },
			{ "Darkorange", "#FF8C00", "LimeGreen" },
			{ "LightSalmon", "#FFA07A", "DarkSlateGray" },
			{ "Orange", "#FFA500", "SeaGreen" },
			{ "LightPink", "#FFB6C1", "ForestGreen" },
			{ "Pink", "#FFC0CB", "LightSeaGreen" },
			{ "Gold", "#FFD700", "DodgerBlue" },
			{ "PeachPuff", "#FFDAB9", "MidnightBlue" },
			{ "NavajoWhite", "#FFDEAD", "Aqua" },
			{ "Moccasin", "#FFE4B5", "Cyan" },
			{ "Bisque", "#FFE4C4", "SpringGreen" },
			{ "MistyRose", "#FFE4E1", "Lime" },
			{ "BlanchedAlmond", "#FFEBCD", "MediumSpringGreen" },
			{ "PapayaWhip", "#FFEFD5", "DarkTurquoise" },
			{ "LavenderBlush", "#FFF0F5", "DeepSkyBlue" },
			{ "SeaShell", "#FFF5EE", "DarkCyan" },
			{ "Cornsilk", "#FFF8DC", "Teal" },
			{ "LemonChiffon", "#FFFACD", "Green" },
			{ "FloralWhite", "#FFFAF0", "DarkGreen" },
			{ "Snow", "#FFFAFA", "Blue" },
			{ "Yellow", "#FFFF00", "MediumBlue" },
			{ "LightYellow", "#FFFFE0", "DarkBlue" },
			{ "Ivory", "#FFFFF0", "Navy" }, 
			{ "White", "#FFFFFF", "Black " } };
	
	// CSS color
	// 颜色名 十六进制颜色值 颜色
	// AliceBlue #F0F8FF
	// AntiqueWhite #FAEBD7
	// Aqua #00FFFF
	// Aquamarine #7FFFD4
	// Azure #F0FFFF
	// Beige #F5F5DC
	// Bisque #FFE4C4
	// Black #000000
	// BlanchedAlmond #FFEBCD
	// Blue #0000FF
	// BlueViolet #8A2BE2
	// Brown #A52A2A
	// BurlyWood #DEB887
	// CadetBlue #5F9EA0
	// Chartreuse #7FFF00
	// Chocolate #D2691E
	// Coral #FF7F50
	// CornflowerBlue #6495ED
	// Cornsilk #FFF8DC
	// Crimson #DC143C
	// Cyan #00FFFF
	// DarkBlue #00008B
	// DarkCyan #008B8B
	// DarkGoldenRod #B8860B
	// DarkGray #A9A9A9
	// DarkGreen #006400
	// DarkKhaki #BDB76B
	// DarkMagenta #8B008B
	// DarkOliveGreen #556B2F
	// Darkorange #FF8C00
	// DarkOrchid #9932CC
	// DarkRed #8B0000
	// DarkSalmon #E9967A
	// DarkSeaGreen #8FBC8F
	// DarkSlateBlue #483D8B
	// DarkSlateGray #2F4F4F
	// DarkTurquoise #00CED1
	// DarkViolet #9400D3
	// DeepPink #FF1493
	// DeepSkyBlue #00BFFF
	// DimGray #696969
	// DimGrey #696969
	// DodgerBlue #1E90FF
	// FireBrick #B22222
	// FloralWhite #FFFAF0
	// ForestGreen #228B22
	// Fuchsia #FF00FF
	// Gainsboro #DCDCDC
	// GhostWhite #F8F8FF
	// Gold #FFD700
	// GoldenRod #DAA520
	// Gray #808080
	// Green #008000
	// GreenYellow #ADFF2F
	// HoneyDew #F0FFF0
	// HotPink #FF69B4
	// IndianRed #CD5C5C
	// Indigo #4B0082
	// Ivory #FFFFF0
	// Khaki #F0E68C
	// Lavender #E6E6FA
	// LavenderBlush #FFF0F5
	// LawnGreen #7CFC00
	// LemonChiffon #FFFACD
	// LightBlue #ADD8E6
	// LightCoral #F08080
	// LightCyan #E0FFFF
	// LightGoldenRodYellow #FAFAD2
	// LightGray #D3D3D3
	// LightGreen #90EE90
	// LightPink #FFB6C1
	// LightSalmon #FFA07A
	// LightSeaGreen #20B2AA
	// LightSkyBlue #87CEFA
	// LightSlateGray #778899
	// LightSteelBlue #B0C4DE
	// LightYellow #FFFFE0
	// Lime #00FF00
	// LimeGreen #32CD32
	// Linen #FAF0E6
	// Magenta #FF00FF
	// Maroon #800000
	// MediumAquaMarine #66CDAA
	// MediumBlue #0000CD
	// MediumOrchid #BA55D3
	// MediumPurple #9370DB
	// MediumSeaGreen #3CB371
	// MediumSlateBlue #7B68EE
	// MediumSpringGreen #00FA9A
	// MediumTurquoise #48D1CC
	// MediumVioletRed #C71585
	// MidnightBlue #191970
	// MintCream #F5FFFA
	// MistyRose #FFE4E1
	// Moccasin #FFE4B5
	// NavajoWhite #FFDEAD
	// Navy #000080
	// OldLace #FDF5E6
	// Olive #808000
	// OliveDrab #6B8E23
	// Orange #FFA500
	// OrangeRed #FF4500
	// Orchid #DA70D6
	// PaleGoldenRod #EEE8AA
	// PaleGreen #98FB98
	// PaleTurquoise #AFEEEE
	// PaleVioletRed #DB7093
	// PapayaWhip #FFEFD5
	// PeachPuff #FFDAB9
	// Peru #CD853F
	// Pink #FFC0CB
	// Plum #DDA0DD
	// PowderBlue #B0E0E6
	// Purple #800080
	// Red #FF0000
	// RosyBrown #BC8F8F
	// RoyalBlue #4169E1
	// SaddleBrown #8B4513
	// Salmon #FA8072
	// SandyBrown #F4A460
	// SeaGreen #2E8B57
	// SeaShell #FFF5EE
	// Sienna #A0522D
	// Silver #C0C0C0
	// SkyBlue #87CEEB
	// SlateBlue #6A5ACD
	// SlateGray #708090
	// Snow #FFFAFA
	// SpringGreen #00FF7F
	// SteelBlue #4682B4
	// Tan #D2B48C
	// Teal #008080
	// Thistle #D8BFD8
	// Tomato #FF6347
	// Turquoise #40E0D0
	// Violet #EE82EE
	// Wheat #F5DEB3
	// White #FFFFFF
	// WhiteSmoke #F5F5F5
	// Yellow #FFFF00
	// YellowGreen #9ACD32

}
