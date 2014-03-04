package cn.edu.fudan.se.helpseeking.eclipsemonitor.monitors;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.Job;

import cn.edu.fudan.se.helpseeking.eclipsemonitor.InteractionEvent;
import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;

public class JobChangeListener extends AbstractUserActivityMonitor implements
		IJobChangeListener {

	@Override
	public void aboutToRun(IJobChangeEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void awake(IJobChangeEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void done(IJobChangeEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void running(IJobChangeEvent event) {
		InteractionEvent e = new InteractionEvent();
		e.setKind(InteractionEvent.Kind.JOB);
		e.setOriginId(event.getJob().getName());
		e.setByuser(false);
		if (event.getJob().getPriority() > 30) {
			DatabaseUtil.addInteractionToDatabase(e);
		}
	}
	
	@Override
	public void scheduled(IJobChangeEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sleeping(IJobChangeEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void start() {
		Job.getJobManager().addJobChangeListener(this);
		setEnabled(true);
	}

	@Override
	public void stop() {
		Job.getJobManager().removeJobChangeListener(this);
		setEnabled(false);
	}

}
