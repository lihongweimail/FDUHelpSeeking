package cn.edu.fudan.se.helpseeking.bean;

import org.eclipse.core.runtime.Preferences.PropertyChangeEvent;



public class MyPropertyChangeEvent extends PropertyChangeEvent{

	public MyPropertyChangeEvent(Object source, String property,
			Object oldValue, Object newValue) {
		super(source, property, oldValue, newValue);
		// TODO Auto-generated constructor stub
	}
}
