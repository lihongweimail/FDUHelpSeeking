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
	

}
