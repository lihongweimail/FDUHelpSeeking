package cn.edu.fudan.se.helpseeking.bean;



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
		
		Add, 
		Delete,Change,
		ContentAssistSelected;
	
		@Override
		public String toString() {
			switch (this) {
			case Add:
				return "Add"; //$NON-NLS-1$
			case Delete:
				return "Delete"; //$NON-NLS-1$
			case Change:
				return "Change"; //$NON-NLS-1$
			case ContentAssistSelected:
				return "ContentAssistSelected"; //$NON-NLS-1$

			default:
				return "null"; //$NON-NLS-1$
			}
		}

		public  boolean isChooseAction() {
			return this == Add || this == Delete 
					|| this == Change || this == ContentAssistSelected;

		}
		/**
		 * @return The corresponding event based on the string provided, or null
		 *         if no such STring.
		 */
		public static EditAction fromString(String string) {
			if (string == null) {
				return null;
			}
			if (string.equals("Add")) { //$NON-NLS-1$
				return Add;
			}
			if (string.equals("Delete")) { //$NON-NLS-1$
				return Delete;
			}
			if (string.equals("Change")) { //$NON-NLS-1$
				return Change;
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

	
	
}
