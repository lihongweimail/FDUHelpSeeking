package cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.IFileBuffer;
import org.eclipse.core.filebuffers.IFileBufferListener;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;

import cn.edu.fudan.se.helpseeking.bean.Basic;
import cn.edu.fudan.se.helpseeking.eclipsemonitor.InteractionEvent;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;

public class FileBufferListener extends AbstractUserActivityMonitor implements
		IFileBufferListener {

	private boolean readyToCommit = false;
	protected static volatile boolean bufferToBeReplaced = false;

	@Override
	public void bufferCreated(IFileBuffer buffer) {

	}

	@Override
	public void bufferDisposed(IFileBuffer buffer) {
	}

	@Override
	public void bufferContentAboutToBeReplaced(IFileBuffer buffer) {
		bufferToBeReplaced = true;
	}

	@Override
	public void bufferContentReplaced(IFileBuffer buffer) {
		bufferToBeReplaced = false;
	}

	@Override
	public void stateChanging(IFileBuffer buffer) {
		if (buffer.isDirty()) {
			readyToCommit = true;
		}

	}

	@Override
	public void dirtyStateChanged(IFileBuffer buffer, boolean isDirty) {
		if (readyToCommit == true && !isDirty && buffer.isSynchronized()) {
			IResource resource = ResourcesPlugin.getWorkspace().getRoot()
					.findMember(buffer.getLocation());
			if (resource instanceof IFile && resource.exists()) {
				IFile file = (IFile) resource;
				if (file.getFileExtension().equals("java")) {
					InteractionEvent e = new InteractionEvent();
					e.setByuser(true);
					e.setKind(Basic.Kind.EDIT);
					e.setOriginId("Save File: " + buffer.getLocation());
					DatabaseUtil.addInteractionEventToDatabase(e);
				}
			}
			readyToCommit = false;
		}
	}

	@Override
	public void stateValidationChanged(IFileBuffer buffer,
			boolean isStateValidated) {
		// TODO Auto-generated method stub

	}

	@Override
	public void underlyingFileMoved(IFileBuffer buffer, IPath path) {
		// TODO Auto-generated method stub

	}

	@Override
	public void underlyingFileDeleted(IFileBuffer buffer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stateChangeFailed(IFileBuffer buffer) {
		readyToCommit = false;
	}

	@Override
	public void start() {
		FileBuffers.getTextFileBufferManager().addFileBufferListener(this);
		setEnabled(true);
	}

	@Override
	public void stop() {
		FileBuffers.getTextFileBufferManager().removeFileBufferListener(this);
		setEnabled(false);
	}
}
