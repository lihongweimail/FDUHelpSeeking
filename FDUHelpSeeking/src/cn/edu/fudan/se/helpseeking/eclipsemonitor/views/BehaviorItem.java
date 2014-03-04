package cn.edu.fudan.se.helpseeking.eclipsemonitor.views;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.edu.fudan.se.helpseeking.util.DatabaseUtil;

public class BehaviorItem {

	public String id;
	public String programmer;
	public String time;
	public String kind;
	public String description;
	public String lineno;
	public String method;
	public String type;
	public String file;
	public String pack;
	public String project;
	public String isbyuser;
	public String handler;

	public BehaviorItem() {
		super();
	}

	public BehaviorItem(String id, String programmer, String time, String kind,
			String description, String lineno, String method, String type,
			String file, String pack, String project, String isbyuser,
			String handler) {
		super();
		this.id = id;
		this.programmer = programmer;
		this.time = time;
		this.kind = kind;
		this.description = description;
		this.lineno = lineno;
		this.method = method;
		this.type = type;
		this.file = file;
		this.pack = pack;
		this.project = project;
		this.isbyuser = isbyuser;
		this.handler = handler;
	}

}
