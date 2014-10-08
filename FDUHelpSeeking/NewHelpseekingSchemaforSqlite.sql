CREATE TABLE IF NOT EXISTS  action (  
	id integer PRIMARY KEY AUTOINCREMENT,  
	time timestamp NULL DEFAULT NULL,  
	endtime timestamp NULL DEFAULT NULL,  
	actionKind varchar(245) DEFAULT NULL,  
	actionName varchar(245) DEFAULT NULL,  
	description text,  byuser varchar(10) DEFAULT NULL,  
	user varchar(45) DEFAULT NULL);
	
	create index IF NOT EXISTS action_id on action(id);
	create index IF NOT EXISTS action_actionkind on action(actionKind);
	create index IF NOT EXISTS action_actionname on action(actionName);
	create index IF NOT EXISTS action_user on action(user);

CREATE TABLE IF NOT EXISTS  breakpoint (  
	id int(11) NOT NULL PRIMARY KEY,  
	type varchar(245) DEFAULT NULL,  
	MethodQualifiedName text,  
	lineNo int(11) DEFAULT NULL);
	
	create index IF NOT EXISTS breakpoint_id on breakpoint(id);
	create index IF NOT EXISTS breakpoint_type on breakpoint(type);

	

CREATE TABLE IF NOT EXISTS  classmodel (  
	id int(11) NOT NULL PRIMARY KEY,  
	type varchar(245) DEFAULT NULL,  
	code longtext,  
	internalCaller text,  
	internalCallee text,  
	upClass text,  
	belowClass text);
	
		create index IF NOT EXISTS classmodel_id on classmodel(id);


CREATE TABLE IF NOT EXISTS  compileinformation (  
	id int(11) NOT NULL PRIMARY KEY,  
	type varchar(245) DEFAULT NULL,  
	content text,  
	relatedCode longtext);
	
	create index IF NOT EXISTS compileinformation_id on compileinformation(id);
	create index IF NOT EXISTS compileinformation_type on compileinformation(type);

	
CREATE TABLE IF NOT EXISTS  cursor (  
	id int(11) NOT NULL PRIMARY KEY,  
	lineNo int(11) DEFAULT NULL,  
	lineFrom int(11) DEFAULT NULL,  
	lineTo int(11) DEFAULT NULL,  
	MethodQualifiedName text);
	
	create index IF NOT EXISTS cursor_id on cursor(id);
	create index IF NOT EXISTS cursor_lineno on cursor(lineNo);

	 
CREATE TABLE IF NOT EXISTS  debugcode (  
	id int(11) NOT NULL PRIMARY KEY,  
	SyntacticBlockID int(11) DEFAULT NULL,  
	ClassModelID int(11) DEFAULT NULL,  
	BreakpointID int(11) DEFAULT NULL) ;
	
	create index IF NOT EXISTS debugcode_id on debugcode(id);
	
	

CREATE TABLE IF NOT EXISTS  editcode ( 
	id int(11) NOT NULL PRIMARY KEY,  
	SyntacticBlockID int(11) DEFAULT NULL,  
	ClassModelID int(11) DEFAULT NULL,  
	CursorID int(11) DEFAULT NULL);
	
	create index IF NOT EXISTS editcode_id on editcode(id);

CREATE TABLE IF NOT EXISTS  editorinfo (  
	id int(11) NOT NULL PRIMARY KEY,  
	size int(11) DEFAULT NULL,  
	classQualifiedNameList text);
	
	create index IF NOT EXISTS editorinfo_id on editorinfo(id);

CREATE TABLE IF NOT EXISTS event (  
	id integer PRIMARY KEY AUTOINCREMENT,  
	user varchar(100) DEFAULT NULL,  
	time timestamp(6) NULL DEFAULT NULL,   
	endtime timestamp(6) NULL DEFAULT NULL,  
	kind varchar(100) DEFAULT NULL,  
	lineno varchar(11) DEFAULT NULL,  
	method varchar(300) DEFAULT NULL,  
	type varchar(300) DEFAULT NULL,  
	file varchar(300) DEFAULT NULL,  
	package varchar(1000) DEFAULT NULL,  
	project varchar(300) DEFAULT NULL,  
	originid text,  
	isbyuser varchar(10) DEFAULT NULL,  
	structurekind varchar(1000) DEFAULT NULL,  
	structurehandle varchar(1000) DEFAULT NULL,  
	delta text);
	
	create index IF NOT EXISTS event_id on event(id);
	create index IF NOT EXISTS event_kind on event(kind);
	

CREATE TABLE IF NOT EXISTS explorerinfo (  
	id int(11) PRIMARY KEY NOT NULL,  
	size int(11) DEFAULT NULL,  
	selectObjectNameList text);
	
	create index IF NOT EXISTS explorerinfo_id on explorerinfo(id);

CREATE TABLE IF NOT EXISTS explorerrelated (  
	id int(11) PRIMARY KEY NOT NULL,  
	editorInfoID int(11) DEFAULT NULL,  
	explorerInfoID int(11) DEFAULT NULL);
	
	create index IF NOT EXISTS explorerrelated_id on explorerrelated(id);
	

CREATE TABLE IF NOT EXISTS ideoutput ( 
	id int(11) PRIMARY KEY NOT NULL,  
	CompileInformationID int(11) DEFAULT NULL,  
	RuntimeInformationID int(11) DEFAULT NULL);
	
	create index IF NOT EXISTS ideoutput_id on ideoutput(id);
	

CREATE TABLE IF NOT EXISTS information (  
	id int(11) PRIMARY KEY NOT NULL,  
	DebugCodeID int(11) DEFAULT NULL,  
	EditCodeID int(11) DEFAULT NULL,  
	IDEOutputID int(11) DEFAULT NULL,  
	ExplorerRelatedID int(11) DEFAULT NULL,  
	ActionID int(11) DEFAULT NULL,  
	type varchar(245) DEFAULT NULL) ;
	
	create index IF NOT EXISTS information_id on information(id);
	create index IF NOT EXISTS information_actionID on information(ActionID);

CREATE TABLE IF NOT EXISTS keywords (  
	id integer PRIMARY KEY AUTOINCREMENT,  
	inforID int(11) DEFAULT NULL,  
	candidateKeyWords text,  
	userKeyWords text,  
	time datetime DEFAULT NULL,  
	costtime bigint(20) DEFAULT NULL,  
	searchID varchar(45) DEFAULT NULL,  
	isbyuser varchar(20) DEFAULT NULL);
	
	create index IF NOT EXISTS keywords_id on keywords(id);
	

CREATE TABLE IF NOT EXISTS runtimeinformation (  
	id int(11) PRIMARY KEY NOT NULL,  
	type varchar(245) DEFAULT NULL,  
	content text, 
	relatedCode longtext) ;
	
	create index IF NOT EXISTS runtimeinformation_id on runtimeinformation(id);

CREATE TABLE IF NOT EXISTS searchresults (  
	id integer PRIMARY KEY AUTOINCREMENT, 
	searhID varchar(45) DEFAULT NULL,  
	title text, 
	link text, 
	contents text, 
	javaExceptionNames text) ;

	create index IF NOT EXISTS searchresults_id on searchresults(id);
	
CREATE TABLE IF NOT EXISTS syntacticblock (  
	id int(11) PRIMARY KEY NOT NULL, 
	type varchar(245) DEFAULT NULL,  
	code longtext,  
	exceptionName text) ;
	
	create index IF NOT EXISTS syntacticblock_id on syntacticblock(id);

CREATE TABLE IF NOT EXISTS taskdecription (  
	id integer PRIMARY KEY AUTOINCREMENT,  
	taskID varchar(45) DEFAULT NULL,  
	taskName varchar(255) DEFAULT NULL,  
	content longtext) ;
	
	create index IF NOT EXISTS taskdecription_id on taskdecription(id);
	

CREATE TABLE IF NOT EXISTS usebrowserinformation (  
	id integer PRIMARY KEY AUTOINCREMENT,  
	tabName text,  
	urlRecord text,  
	searchResultsListPostion int(11) DEFAULT NULL,  
	totallistnumber int(11) DEFAULT NULL,  
	useTimes int(11) DEFAULT NULL,  
	reOpenIndex int(11) DEFAULT NULL,  
	titleRecord text,  
	totalTime bigint(20) DEFAULT NULL,  
	starTimestamp timestamp NULL DEFAULT NULL,  
	endTimestamp timestamp NULL DEFAULT NULL,  
	solutionID varchar(245) DEFAULT NULL,  
	searchID varchar(45) DEFAULT NULL,  
	searchType varchar(245) DEFAULT NULL,  
	contentRecord text) ;
	
	create index IF NOT EXISTS usebrowserinformation_id on usebrowserinformation(id);
	

CREATE TABLE IF NOT EXISTS usekeywords (  
	id integer PRIMARY KEY AUTOINCREMENT,  
	keywordname text,  
	searchid varchar(45) DEFAULT NULL,  
	positioninrecommandlist int(11) DEFAULT NULL,  
	positioninusestring int(11) DEFAULT NULL,  
	lastsearchid varchar(45) DEFAULT NULL,  
	isrecommand varchar(45) DEFAULT NULL,  
	times timestamp NULL DEFAULT NULL);
	
	create index IF NOT EXISTS usekeywords_id on usekeywords(id);


CREATE TABLE IF NOT EXISTS useresultsrecord (  
	id integer PRIMARY KEY AUTOINCREMENT,  
	searchID varchar(45) DEFAULT NULL,  
	solutionID varchar(145) DEFAULT NULL,  
	title text,  url text,  
	content text,  
	totallist int(11) DEFAULT NULL,  
	position int(11) DEFAULT NULL,  
	type varchar(245) DEFAULT NULL,  
	time timestamp NULL DEFAULT NULL);
	
	create index IF NOT EXISTS useresultsrecord_id on useresultsrecord(id);
	
	
