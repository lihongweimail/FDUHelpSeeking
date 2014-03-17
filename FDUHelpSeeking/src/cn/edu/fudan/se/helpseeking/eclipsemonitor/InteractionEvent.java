package cn.edu.fudan.se.helpseeking.eclipsemonitor;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.core.runtime.Assert;

public class InteractionEvent {

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

	private Kind kind = null;

	private Date date = null;

	private Date endDate = null;

	private String originId = null;   //description

	private String structureKind = null;

	private String structureHandle = null;

	private String lineno = null;

	private String method = null;

	private String type = null;

	private String file = null;

	private String packages = null;

	private String project = null;

	private String navigation = null;

	private String delta = null;

	private boolean byuser = false;

	private float interestContribution = 0;

	public InteractionEvent() {

	}

	/**
	 * Use to specify an uknown identifier, e.g. for an originId.
	 */
	public static final String ID_UNKNOWN = "?"; //$NON-NLS-1$

	/**
	 * For parameter description see this class's getters.
	 */
	public InteractionEvent(Kind kind, String structureKind, String handle,
			String originId) {
		this(kind, structureKind, handle, originId, 1f);
	}

	/**
	 * For parameter description see this class's getters.
	 */
	public InteractionEvent(Kind kind, String structureKind, String handle,
			String originId, String navigatedRelation) {
		this(kind, structureKind, handle, originId, navigatedRelation,
				"null", 1f); //$NON-NLS-1$
	}

	/**
	 * For parameter description see this class's getters.
	 */
	public InteractionEvent(Kind kind, String structureKind, String handle,
			String originId, String navigatedRelation,
			float interestContribution) {
		this(kind, structureKind, handle, originId, navigatedRelation,
				"null", interestContribution); //$NON-NLS-1$
	}

	/**
	 * For parameter description see this class's getters.
	 */
	public static InteractionEvent makeCommand(String originId, String delta) {
		return new InteractionEvent(InteractionEvent.Kind.COMMAND,
				"null", "null", originId, "null", delta, 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/**
	 * For parameter description see this class's getters.
	 */
	public static InteractionEvent makeCopy(InteractionEvent originalEvent,
			float newInterestContribution) {
		return new InteractionEvent(originalEvent.getKind(),
				originalEvent.getStructureKind(),
				originalEvent.getStructureHandle(),
				originalEvent.getOriginId(), originalEvent.getNavigation(),
				originalEvent.getDelta(), newInterestContribution,
				originalEvent.getDate(), originalEvent.getEndDate());
	}

	/**
	 * For parameter description see this class's getters.
	 */
	public static InteractionEvent makePreference(String originId, String delta) {
		return new InteractionEvent(InteractionEvent.Kind.PREFERENCE,
				"null", "null", originId, "null", delta, 1); // default //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		// contribution
	}

	/**
	 * For parameter description see this class's getters.
	 */
	public InteractionEvent(Kind kind, String structureKind, String handle,
			String originId, float interestContribution) {
		this(kind, structureKind, handle, originId,
				"null", "null", interestContribution); // default //$NON-NLS-1$ //$NON-NLS-2$
		// contribution
	}

	/**
	 * For parameter description see this class's getters.
	 */
	public InteractionEvent(Kind kind, String structureKind, String handle,
			String originId, String navigatedRelation, String delta,
			float interestContribution) {
		this(kind, structureKind, handle, originId, navigatedRelation, delta,
				interestContribution, Calendar.getInstance().getTime());
	}

	private InteractionEvent(Kind kind, String structureKind, String handle,
			String originId, String navigatedRelation, String delta,
			float interestContribution, Date startDate) {
		this(kind, structureKind, handle, originId, navigatedRelation, delta,
				interestContribution, startDate, startDate);
	}

	/**
	 * For parameter description see this class's getters.
	 */
	public InteractionEvent(Kind kind, String structureKind, String handle,
			String originId, String navigatedRelation, String delta,
			float interestContribution, Date startDate, Date endDate) {
		Assert.isNotNull(kind);
		Assert.isNotNull(originId);
		Assert.isNotNull(startDate);
		Assert.isNotNull(endDate);
		this.kind = kind;
		this.structureKind = (structureKind != null) ? structureKind.intern()
				: null;
		this.structureHandle = (handle != null) ? handle.intern() : null;
		this.originId = originId.intern();
		this.navigation = (navigatedRelation != null) ? navigatedRelation
				.intern() : null;
		this.delta = (delta != null) ? delta.intern() : null;
		this.interestContribution = interestContribution;
		this.date = startDate;
		this.endDate = endDate;
	}

	@Override
	public boolean equals(Object object) {
		if (object == null || !(object instanceof InteractionEvent)) {
			return false;
		}
		InteractionEvent event = (InteractionEvent) object;
		return (kind == null ? event.kind == null : kind.equals(event.kind))
				&& (structureKind == null ? event.structureKind == null
						: structureKind.equals(event.structureKind))
				&& (structureHandle == null ? event.structureHandle == null
						: structureHandle.equals(event.structureHandle))
				&& (originId == null ? event.originId == null : originId
						.equals(event.originId))
				&& (navigation == null ? event.navigation == null : navigation
						.equals(event.navigation))
				&& (delta == null ? event.delta == null : delta
						.equals(event.delta));

	}

	@Override
	public int hashCode() {
		int hashCode = 0;
		if (date != null) {
			hashCode += date.hashCode();
		}
		if (endDate != null) {
			hashCode += endDate.hashCode();
		}
		if (kind != null) {
			hashCode += kind.hashCode();
		}
		if (structureKind != null) {
			hashCode += structureKind.hashCode();
		}
		if (structureHandle != null) {
			hashCode += structureHandle.hashCode();
		}
		if (originId != null) {
			hashCode += originId.hashCode();
		}
		if (navigation != null) {
			hashCode += navigation.hashCode();
		}
		if (delta != null) {
			hashCode += delta.hashCode();
		}
		// TODO: could this lose precision?
		hashCode += new Float(interestContribution).hashCode();
		return hashCode;
	}

	@Override
	public String toString() {
		return "(date: " + date + ", kind: " + kind + ", sourceHandle: " + structureHandle + ", origin: " + originId //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				+ ", delta: " + delta + ")"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	public boolean isValidStructureHandle() {
		return structureHandle != null
				&& !structureHandle.equals("null") && !structureHandle.trim().equals(ID_UNKNOWN); //$NON-NLS-1$
	}

	// TODO 4.0 change to getHandleIdentifier()
	public String getStructureHandle() {
		return structureHandle;
	}

	/**
	 * @return The content type of the element being interacted with.
	 */
	public String getStructureKind() {
		return structureKind;
	}

	/**
	 * @return Time stamp for the occurrence of the event.
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Can be used for extensibility, e.g. by adding an XML-encoded String.
	 * 
	 * @return Additional information relevant to interaction monitoring.
	 */
	public String getDelta() {
		return delta;
	}

	/**
	 * @return Defines the kind of interaction that took place.
	 */
	public Kind getKind() {
		return kind;
	}

	/**
	 * @return The UI affordance that the event was issued from.
	 */
	public String getOriginId() {
		return originId;
	}

	/**
	 * @return If an aggregate event, amount of interest of all contained
	 *         events.
	 */
	// TODO: consider refactoring in order to de-couple events from interest.
	public float getInterestContribution() {
		return interestContribution;
	}

	/**
	 * @return If an aggregate event, time stamp of the last occurrence.
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @return An identifier for the kind of relation that corresponds to the
	 *         navigation to this element.
	 */
	public String getNavigation() {
		return navigation;
	}

	public void setKind(Kind kind) {
		this.kind = kind;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setOriginId(String originId) {
		this.originId = originId;
	}

	public void setStructureKind(String structureKind) {
		this.structureKind = structureKind;
	}

	public void setStructureHandle(String structureHandle) {
		this.structureHandle = structureHandle;
	}

	public void setNavigation(String navigation) {
		this.navigation = navigation;
	}

	public void setDelta(String delta) {
		this.delta = delta;
	}

	public void setInterestContribution(float interestContribution) {
		this.interestContribution = interestContribution;
	}

	public String getLineno() {
		return lineno;
	}

	public void setLineno(String lineno) {
		this.lineno = lineno;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPackages() {
		return packages;
	}

	public void setPackages(String packages) {
		this.packages = packages;
	}

	public boolean isByuser() {
		return byuser;
	}

	public void setByuser(boolean isbyuser) {
		this.byuser = isbyuser;
	}
}
