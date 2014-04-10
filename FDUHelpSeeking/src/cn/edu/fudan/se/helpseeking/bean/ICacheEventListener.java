package cn.edu.fudan.se.helpseeking.bean;

import java.util.EventListener;

public interface ICacheEventListener extends EventListener {
	public void cacheChangedEvent();
	
}
