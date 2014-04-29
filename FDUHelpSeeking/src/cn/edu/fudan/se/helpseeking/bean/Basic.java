package cn.edu.fudan.se.helpseeking.bean;

import java.util.List;

import cn.edu.fudan.se.helpseeking.util.CommUtil;
import cn.edu.fudan.se.helpseeking.util.Resource;

public class Basic {

	/**
	 * Determines the type of interaction that took place, either initiated by
	 * the user or done on behalf of the user.
	 */
	public enum Kind {
		/**
		 * User selection of elements, issued by the Eclipse post-selection
		 * mechanism.
		 */
		SELECTION,

		/**
		 * Edit events that are created by text selections in an editor.
		 */
		EDIT, DEBUG,
		/**
		 * Commands and actions invoked via buttons, menus, and keyboard
		 * shortcuts.
		 */
		COMMAND,

		/**
		 * Workbench preference changes, sometimes made by the user, sometimes
		 * automatically on behalf of the user.
		 */
		PREFERENCE,

		/**
		 * Candidates for future interaction.
		 */
		PREDICTION,

		/**
		 * Indirect user interaction with elements (e.g. parent gets implicitly
		 * selected when element is selected).
		 */
		PROPAGATION,

		/**
		 * Direct manipulation of interest via actions such as
		 * "Mark as Landmark" and "Mark Less Interesting".
		 */
		MANIPULATION,

		/**
		 * Capture interaction with tasks, the workbench, and lifecycle events
		 * that define where the user's attention is directed.
		 */
		ATTENTION, RESOURCE, REVISION, TEST, JOB;

		/**
		 * TODO: add PREFERENCE?
		 */
		public boolean isUserEvent() {
			return this == SELECTION || this == EDIT || this == COMMAND
					|| this == PREFERENCE;
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

		/**
		 * @return The corresponding event based on the string provided, or null
		 *         if no such STring.
		 */
		public static Kind fromString(String string) {
			if (string == null) {
				return null;
			}
			if (string.equals("selection")) { //$NON-NLS-1$
				return SELECTION;
			}
			if (string.equals("edit")) { //$NON-NLS-1$
				return EDIT;
			}
			if (string.equals("debug")) { //$NON-NLS-1$
				return DEBUG;
			}
			if (string.equals("command")) { //$NON-NLS-1$
				return COMMAND;
			}
			if (string.equals("preference")) { //$NON-NLS-1$
				return PREFERENCE;
			}
			if (string.equals("prediction")) { //$NON-NLS-1$
				return PREDICTION;
			}
			if (string.equals("propagation")) { //$NON-NLS-1$
				return PROPAGATION;
			}
			if (string.equals("manipulation")) { //$NON-NLS-1$
				return MANIPULATION;
			}
			if (string.equals("attention")) { //$NON-NLS-1$
				return ATTENTION;
			}
			if (string.equals("resource")) { //$NON-NLS-1$
				return RESOURCE;
			}
			if (string.equals("revision")) { //$NON-NLS-1$
				return REVISION;
			}
			if (string.equals("test")) { //$NON-NLS-1$
				return TEST;
			}
			if (string.equals("job")) { //$NON-NLS-1$
				return JOB;
			}
			return null;
		}
	}
	
	
	
	
	public enum DebugAction {
		
		AddLineBreakpoint, AddMethodBreakpoint,
		DeleteLineBreakpoint,DeleteMethodBreakpoint,
		ChangeLineBreakpoint,ChangeMethodBreakpoint,
		Suspend,Resume,Terminate,
		StepInto,StepReturn,StepEnd,StepOver ;
		
	
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

		public  boolean isChooseAction() {
			return this == AddLineBreakpoint || this == AddMethodBreakpoint 
					|| this == DeleteLineBreakpoint || this == DeleteMethodBreakpoint
					|| this == ChangeLineBreakpoint || this == ChangeMethodBreakpoint
					|| this == Suspend || this == Resume  || this == Terminate
					|| this == StepInto || this == StepReturn || this == StepEnd || this == StepOver;

		}
		/**
		 * @return The corresponding event based on the string provided, or null
		 *         if no such STring.
		 */
		public static DebugAction fromString(String string) {
			if (string == null) {
				return null;
			}
			if (string.equals("AddLineBreakpoint")) { //$NON-NLS-1$
				return AddLineBreakpoint;
			}
			if (string.equals("AddMethodBreakpoint")) { //$NON-NLS-1$
				return AddMethodBreakpoint;
			}
			if (string.equals("DeleteLineBreakpoint")) { //$NON-NLS-1$
				return DeleteLineBreakpoint;
			}
			if (string.equals("DeleteMethodBreakpoint")) { //$NON-NLS-1$
				return DeleteMethodBreakpoint;
			}
			if (string.equals("ChangeLineBreakpoint")) { //$NON-NLS-1$
				return ChangeLineBreakpoint;
			}
			if (string.equals("ChangeMethodBreakpoint")) { //$NON-NLS-1$
				return ChangeMethodBreakpoint;
			}
			if (string.equals("Suspend")) { //$NON-NLS-1$
				return Suspend;
			}
			if (string.equals("Resume")) { //$NON-NLS-1$
				return Resume;
			}
			if (string.equals("Terminate")) { //$NON-NLS-1$
				return Terminate;
			}
			if (string.equals("StepInto")) { //$NON-NLS-1$
				return StepInto;
			}
			if (string.equals("StepReturn")) { //$NON-NLS-1$
				return StepReturn;
			}
			if (string.equals("StepEnd")) { //$NON-NLS-1$
				return StepEnd;
			}
			if (string.equals("StepOver")) { //$NON-NLS-1$
				return StepOver;
			}			
			return null;
		}
	}

	public enum CommandAction {
		
		HelpCommand, 
		InlineEdit;
	
		@Override
		public String toString() {
			switch (this) {
			case HelpCommand:
				return "HelpCommand"; //$NON-NLS-1$
			case InlineEdit:
				return "InlineEdit"; //$NON-NLS-1$
		
				
			default:
				return "null"; //$NON-NLS-1$
			}
		}

		public  boolean isChooseAction() {
			return this == HelpCommand || this == InlineEdit;

		}
		/**
		 * @return The corresponding event based on the string provided, or null
		 *         if no such STring.
		 */
		public static CommandAction fromString(String string) {
			if (string == null) {
				return null;
			}
			if (string.equals("HelpCommand")) { //$NON-NLS-1$
				return HelpCommand;
			}
			if (string.equals("InlineEdit")) { //$NON-NLS-1$
				return InlineEdit;
			}
			return null;
		}
	}

	
	
	public enum AttentionAction {
		
		BroughtToTop, 
		Open,Closed,
		Activated,Deactivated,
		ChangedTo;
	
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

		public  boolean isChooseAction() {
			return this == BroughtToTop || this == Open 
					|| this == Closed || this == Activated
					|| this == Deactivated || this == ChangedTo;

		}
		/**
		 * @return The corresponding event based on the string provided, or null
		 *         if no such STring.
		 */
		public static AttentionAction fromString(String string) {
			if (string == null) {
				return null;
			}
			if (string.equals("BroughtToTop")) { //$NON-NLS-1$
				return BroughtToTop;
			}
			if (string.equals("Open")) { //$NON-NLS-1$
				return Open;
			}
			if (string.equals("Closed")) { //$NON-NLS-1$
				return Closed;
			}
			if (string.equals("Activated")) { //$NON-NLS-1$
				return Activated;
			}
			if (string.equals("Deactivated")) { //$NON-NLS-1$
				return Deactivated;
			}
			if (string.equals("ChangedTo")) { //$NON-NLS-1$
				return ChangedTo;
			}
					
			return null;
		}
	}
	
	
	public enum EditAction {
		
		AddField,AddMethod, AddImportDeclaration,AddClass,
		DeleteField,DeleteMethod, DeleteImportDeclaration,DeleteClass,
		ChangeField,ChangeMethod, ChangeImportDeclaration,ChangeClass,
		ContentAssistSelected;
	
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

		public  boolean isChooseAction() {
			return this == AddField || this == AddMethod || this == AddImportDeclaration || this == AddClass 
					|| this == DeleteField || this == DeleteMethod || this == DeleteImportDeclaration || this == DeleteClass 
					|| this == ChangeField || this == ChangeMethod || this == ChangeImportDeclaration || this == ChangeClass 
					|| this == ContentAssistSelected;

		}
		/**
		 * @return The corresponding event based on the string provided, or null
		 *         if no such STring.
		 */
		public static EditAction fromString(String string) {
			if (string == null) {
				return null;
			}
			if (string.equals("AddField")) { //$NON-NLS-1$
				return AddField;
			}
			if (string.equals("AddMethod")) { //$NON-NLS-1$
				return AddMethod;
			}
			if (string.equals("AddImportDeclaration")) { //$NON-NLS-1$
				return AddImportDeclaration;
			}
			if (string.equals("AddClass")) { //$NON-NLS-1$
				return AddClass;
			}
			if (string.equals("DeleteField")) { //$NON-NLS-1$
				return DeleteField;
			}
			if (string.equals("DeleteMethod")) { //$NON-NLS-1$ 
				return DeleteMethod;
			}
			if (string.equals("DeleteImportDeclaration")) { //$NON-NLS-1$
				return DeleteImportDeclaration;
			}
			if (string.equals("DeleteClass")) { //$NON-NLS-1$
				return DeleteClass;
			}
			if (string.equals("ChangeField")) { //$NON-NLS-1$
				return ChangeField;
			}
			if (string.equals("ChangeMethod")) { //$NON-NLS-1$
				return ChangeMethod;
			}
			if (string.equals("ChangeImportDeclaration")) { //$NON-NLS-1$
				return ChangeImportDeclaration;
			}
			if (string.equals("ChangeClass")) { //$NON-NLS-1$
				return ChangeClass;
			}
			if (string.equals("ContentAssistSelected")) { //$NON-NLS-1$
				return ContentAssistSelected;
			}
			
					
			return null;
		}
	}


	public enum SelectionAction {
		
		SelectPart;
	
		@Override
		public String toString() {
			switch (this) {
			case SelectPart:
				return "SelectPart"; //$NON-NLS-1$
			

			default:
				return "null"; //$NON-NLS-1$
			}
		}

		public  boolean isChooseAction() {
			return this == SelectPart ;

		}
		/**
		 * @return The corresponding event based on the string provided, or null
		 *         if no such STring.
		 */
		public static SelectionAction fromString(String string) {
			if (string == null) {
				return null;
			}
			if (string.equals("SelectPart")) { //$NON-NLS-1$
				return SelectPart;
			}
				
			return null;
		}
	}

	
//	编译信息
//	策略是监听problem view当有change事件时,更新内存单例，在每个动作时候都去获取信息：
//	如果变更则取出所有信息，并按照先error后warning信息；
//	如果有单击或双击选择事件，则取该项目信息。
	public enum CompileInfoType {
		ERROR,
		WARNING;
		
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

		/**
		 * @return The corresponding event based on the string provided, or null
		 *         if no such STring.
		 */
		public static CompileInfoType fromString(String string) {
			if (string == null) {
				return null;
			}
			if (string.equals("ERROR")) { //$NON-NLS-1$
				return ERROR;
			}
			if (string.equals("WARNING")) { //$NON-NLS-1$
				return WARNING;
			}
			return null;
		}
	}

	
//	运行时信息：
//	需要考虑如何从console中的输出中区分type： 
//	1 framework message 
//  2 program output
//  3 exceptional message（*这个信息更重要）
	public enum RuntimeInfoType {
		FrameWorkMessage,
		ProgramOutput,
		ExceptionalMessage;
		
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

		/**
		 * @return The corresponding event based on the string provided, or null
		 *         if no such STring.
		 */
		public static RuntimeInfoType fromString(String string) {
			if (string == null) {
				return null;
			}
			if (string.equals("FrameWorkMessage")) { //$NON-NLS-1$
				return FrameWorkMessage;
			}
			if (string.equals("ProgramOutput")) { //$NON-NLS-1$
				return ProgramOutput;
			}
			if (string.equals("ExceptionalMessage")) { //$NON-NLS-1$
				return ExceptionalMessage;
			}
			return null;
		}
	}

	
	
	public enum QueryLevel {
		High,
		Middle,
		Other;
		
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

		/**
		 * @return The corresponding event based on the string provided, or null
		 *         if no such STring.
		 */
		public static QueryLevel fromString(String string) {
			if (string == null) {
				return null;
			}
			if (string.equals("High")) { //$NON-NLS-1$
				return High;
			}
			if (string.equals("Middle")) { //$NON-NLS-1$
				return Middle;
			}
			if (string.equals("Other")) { //$NON-NLS-1$
				return Other;
			}
			return null;
		}
	}


	public static final String SPLIT_STRING =  "[\\[\\]&#$_.(){}!*%+-=><\\:;,?/\"\'\t\b\r\n\0 ]";
	
//	private static final String SPLIT_STRING =  "[&#$_.(){}!*%+-=><\\:;,?/\"\'\t\b\r\n\0 ]";

	//		另外，有必要建立一个异常列表文件，记录各种异常名称，如果以上信息中出现了该异常词汇，则该异常词汇权重为基本权重两倍(weightTwo)！
	public static final  String javaExceptionalFileName ="/StopResource/javaExceptionalName.txt";
	public static final  Resource myResource=new Resource();
	public static final String javaExceptionalName = myResource.getResource(javaExceptionalFileName );

	public static final List<String> javaExceptionalNameList=CommUtil.arrayToList((javaExceptionalName).split(SPLIT_STRING));
	
	
	
	
	
	//从候选检索词中取词数量
	public final static int TEMP_K_KEYWORDS =5;
	//爆出到数据库中最大的候选检索词保留数量
	public final static int MAX_CANDIDATE_KEYWORDS=100;
	//动作的滑动窗口大小： 经验上 15分钟的动作在300个左右
	public final static int SLIDE_WINDOW_SIZE=20;
	
	//权重  距离   老化   频率
	
	public final static double DISTANCE_RATIO=0.01;
	public final static double OLD_RATIO=0.1;
	public final static double FREQUENCY_RATIO=0.01;
	
	
	
	
	
	public enum KeyWordsLevel {
			Level_One,
			Level_Two,
			Level_Three,
			Level_Four,
			Level_Five,
			Level_Six,
			Level_Seven,
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

	  

	
}
