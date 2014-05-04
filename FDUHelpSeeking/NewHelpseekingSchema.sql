CREATE DATABASE IF NOT EXISTS `helpseeking`;

use helpseeking;

CREATE TABLE IF NOT EXISTS `action` ( `id` int(11) NOT NULL AUTO_INCREMENT, `time` timestamp NULL DEFAULT NULL,  `endtime` timestamp NULL DEFAULT NULL,  `actionKind` varchar(45) DEFAULT NULL ,  `actionName` varchar(45) DEFAULT NULL,  `description` text,  `byuser` varchar(10) DEFAULT NULL ,  `user` varchar(45) DEFAULT NULL,  PRIMARY KEY (`id`),  KEY `ACTIONKIND` (`actionKind`),  KEY `ACTIONNAME` (`actionName`),  KEY `USER` (`user`)) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS  `breakpoint` (  `id` int(11) NOT NULL,  `type` varchar(45) DEFAULT NULL,  `MethodQualifiedName` text,  `lineNo` int(11) DEFAULT NULL,  PRIMARY KEY (`id`),  KEY `BREAKPOINTTYPE` (`type`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS  `classmodel` (  `id` int(11) NOT NULL,  `type` varchar(45) DEFAULT NULL,  `code` longtext,  `internalCaller` text,  `internalCallee` text ,  `upClass` text ,  `belowClass` text,  PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS  `compileinformation` (   `id` int(11) NOT NULL,  `type` varchar(45) DEFAULT NULL , `content` text, `relatedCode` longtext,  PRIMARY KEY (`id`),  KEY `COMPILEINFOTYPE` (`type`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE  IF NOT EXISTS `cursor` ( `id` int(11) NOT NULL, `lineNo` int(11) DEFAULT NULL,  `lineFrom` int(11) DEFAULT NULL,  `lineTo` int(11) DEFAULT NULL,  `MethodQualifiedName` text,  PRIMARY KEY (`id`),  KEY `CURSORLINENO` (`lineNo`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS  `debugcode` ( `id` int(11) NOT NULL, `SyntacticBlockID` int(11) DEFAULT NULL, `ClassModelID` int(11) DEFAULT NULL, `BreakpointID` int(11) DEFAULT NULL, PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS  `editcode` ( `id` int(11) NOT NULL, `SyntacticBlockID` int(11) DEFAULT NULL, `ClassModelID` int(11) DEFAULT NULL, `CursorID` int(11) DEFAULT NULL,  PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS  `editorinfo` ( `id` int(11) NOT NULL,  `size` int(11) DEFAULT NULL,  `classQualifiedNameList` text ,  PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `event` (  `id` int(11) NOT NULL AUTO_INCREMENT,  `user` varchar(100) DEFAULT NULL,  `time` timestamp(6) NULL DEFAULT NULL,  `endtime` timestamp(6) NULL DEFAULT NULL,  `kind` varchar(100) DEFAULT NULL,  `lineno` varchar(11) DEFAULT NULL,  `method` varchar(300) DEFAULT NULL,  `type` varchar(300) DEFAULT NULL,  `file` varchar(300) DEFAULT NULL,  `package` varchar(1000) DEFAULT NULL,  `project` varchar(300) DEFAULT NULL,  `originid` text,  `isbyuser` varchar(10) DEFAULT NULL,  `structurekind` varchar(1000) DEFAULT NULL,  `structurehandle` varchar(1000) DEFAULT NULL,  `delta` text,  PRIMARY KEY (`id`),  KEY `kind` (`kind`)) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `explorerinfo` ( `id` int(11) NOT NULL,  `size` int(11) DEFAULT NULL,  `selectObjectNameList` text,  PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `explorerrelated` ( `id` int(11) NOT NULL, `editorInfoID` int(11) DEFAULT NULL,  `explorerInfoID` int(11) DEFAULT NULL,  PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `ideoutput` ( `id` int(11) NOT NULL, `CompileInformationID` int(11) DEFAULT NULL, `RuntimeInformationID` int(11) DEFAULT NULL,  PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `information` ( `id` int(11) NOT NULL, `DebugCodeID` int(11) DEFAULT NULL, `EditCodeID` int(11) DEFAULT NULL, `IDEOutputID` int(11) DEFAULT NULL,  `ExplorerRelatedID` int(11) DEFAULT NULL,  `ActionID` int(11) DEFAULT NULL,  `type` varchar(45) DEFAULT NULL,  PRIMARY KEY (`id`),  KEY `INFOMATIONACTIONID` (`ActionID`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `keywords` ( `id` int(11) NOT NULL AUTO_INCREMENT,  `inforID` int(11) DEFAULT NULL,  `candidateKeyWords` text ,  `userKeyWords` text ,  `time` datetime DEFAULT NULL,  `costtime` BIGINT  DEFAULT NULL,  `searchID` varchar(45) DEFAULT NULL,  `isbyuser` VARCHAR(20) NULL ,  PRIMARY KEY (`id`)) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `runtimeinformation` ( `id` int(11) NOT NULL, `type` varchar(45) DEFAULT NULL ,  `content` text,  `relatedCode` longtext,  PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `searchresults` ( `id` int(11) NOT NULL AUTO_INCREMENT,  `searhID` varchar(45) DEFAULT NULL,  `title` text,  `link` text,  `contents` text,  `javaExceptionNames` text,  PRIMARY KEY (`id`)) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `syntacticblock` (`id` int(11) NOT NULL,`type` varchar(45) DEFAULT NULL, `code` longtext, `exceptionName` varchar(45) DEFAULT NULL, PRIMARY KEY (`id`), KEY `SYNTACTICTYPE` (`type`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `taskdecription` ( `id` int(11) NOT NULL AUTO_INCREMENT, `taskID` varchar(45) DEFAULT NULL, `taskName` varchar(255) DEFAULT NULL,  `content` longtext,  PRIMARY KEY (`id`)) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `usekeywords` (  `id` int(11) NOT NULL AUTO_INCREMENT,  `keywordname` text,  `searchid` varchar(45) DEFAULT NULL,  `positioninrecommandlist` int(11) DEFAULT NULL,  `positioninusestring` int(11) DEFAULT NULL,  `lastsearchid` varchar(45) DEFAULT NULL, `isrecommand` varchar(45) DEFAULT NULL, `times` timestamp NULL DEFAULT NULL, PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `usebrowserinformation` (  `id` int(11) NOT NULL AUTO_INCREMENT,  `tabName` text,  `urlRecord` text DEFAULT NULL,  `searchResultsListPostion` int(11) DEFAULT NULL,  `totallistnumber` int(11) DEFAULT NULL,  `useTimes` int(11) DEFAULT NULL,  `reOpenIndex` int(11) DEFAULT NULL,  `titleRecord` text,  `totalTime` bigint(20) DEFAULT NULL,  `starTimestamp` timestamp NULL DEFAULT NULL,  `endTimestamp` timestamp NULL DEFAULT NULL,  `solutionID` varchar(245) DEFAULT NULL,  `searchID` varchar(45) DEFAULT NULL,  `searchType` varchar(45) DEFAULT NULL,  `contentRecord` text,  PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `useresultsrecord` (  `id` int(11) NOT NULL AUTO_INCREMENT,  `searchID` varchar(45) DEFAULT NULL,  `solutionID` varchar(145) DEFAULT NULL,  `title` text,  `url` text,  `content` text,  `totallist` int(11) DEFAULT NULL,  `position` int(11) DEFAULT NULL,  `type` varchar(45) DEFAULT NULL,  `time` timestamp NULL DEFAULT NULL,  PRIMARY KEY (`id`)) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;