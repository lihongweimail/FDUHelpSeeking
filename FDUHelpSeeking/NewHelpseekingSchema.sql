CREATE DATABASE `helpseeking` /*!40100 DEFAULT CHARACTER SET utf8 */;

use helpseeking;

CREATE TABLE `action` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `time` timestamp NULL DEFAULT NULL,
  `endtime` timestamp NULL DEFAULT NULL,
  `actionKind` varchar(45) DEFAULT NULL COMMENT 'see KIND type in basic.java of the bean package. 5-10 kinds to use',
  `actionName` varchar(45) DEFAULT NULL,
  `description` text,
  `byuser` varchar(10) DEFAULT NULL COMMENT 'yes or no from the programe name as 1 or 0.',
  `user` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ACTIONKIND` (`actionKind`),
  KEY `ACTIONNAME` (`actionName`),
  KEY `USER` (`user`)
) ENGINE=InnoDB AUTO_INCREMENT=985 DEFAULT CHARSET=utf8;


CREATE TABLE `breakpoint` (
  `id` int(11) NOT NULL,
  `type` varchar(45) DEFAULT NULL,
  `MethodQualifiedName` text,
  `lineNo` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `BREAKPOINTTYPE` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `classmodel` (
  `id` int(11) NOT NULL,
  `type` varchar(45) DEFAULT NULL,
  `code` longtext,
  `internalCaller` text COMMENT 'please use ";" to split items',
  `internalCallee` text COMMENT 'please use ";" to split items',
  `upClass` text COMMENT 'please use ";" to split items',
  `belowClass` text COMMENT 'please use ";" to split items',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `compileinformation` (
  `id` int(11) NOT NULL,
  `type` varchar(45) DEFAULT NULL COMMENT 'error  or warning',
  `content` text,
  `relatedCode` longtext,
  PRIMARY KEY (`id`),
  KEY `COMPILEINFOTYPE` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `cursor` (
  `id` int(11) NOT NULL,
  `lineNo` int(11) DEFAULT NULL,
  `lineFrom` int(11) DEFAULT NULL,
  `lineTo` int(11) DEFAULT NULL,
  `MethodQualifiedName` text,
  PRIMARY KEY (`id`),
  KEY `CURSORLINENO` (`lineNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `debugcode` (
  `id` int(11) NOT NULL,
  `SyntacticBlockID` int(11) DEFAULT NULL,
  `ClassModelID` int(11) DEFAULT NULL,
  `BreakpointID` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `editcode` (
  `id` int(11) NOT NULL,
  `SyntacticBlockID` int(11) DEFAULT NULL,
  `ClassModelID` int(11) DEFAULT NULL,
  `CursorID` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `editorinfo` (
  `id` int(11) NOT NULL,
  `size` int(11) DEFAULT NULL,
  `classQualifiedNameList` text COMMENT 'split character ";"',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `event` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user` varchar(100) DEFAULT NULL,
  `time` timestamp(6) NULL DEFAULT NULL,
  `endtime` timestamp(6) NULL DEFAULT NULL,
  `kind` varchar(100) DEFAULT NULL,
  `lineno` varchar(11) DEFAULT NULL,
  `method` varchar(300) DEFAULT NULL,
  `type` varchar(300) DEFAULT NULL,
  `file` varchar(300) DEFAULT NULL,
  `package` varchar(1000) DEFAULT NULL,
  `project` varchar(300) DEFAULT NULL,
  `originid` text,
  `isbyuser` varchar(10) DEFAULT NULL,
  `structurekind` varchar(1000) DEFAULT NULL,
  `structurehandle` varchar(1000) DEFAULT NULL,
  `delta` text,
  PRIMARY KEY (`id`),
  KEY `kind` (`kind`)
) ENGINE=InnoDB AUTO_INCREMENT=11676 DEFAULT CHARSET=utf8;


CREATE TABLE `explorerinfo` (
  `id` int(11) NOT NULL,
  `size` int(11) DEFAULT NULL,
  `selectObjectNameList` text COMMENT 'split character ";"',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `explorerrelated` (
  `id` int(11) NOT NULL,
  `editorInfoID` int(11) DEFAULT NULL,
  `explorerInfoID` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `ideoutput` (
  `id` int(11) NOT NULL,
  `CompileInformationID` int(11) DEFAULT NULL,
  `RuntimeInformationID` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `information` (
  `id` int(11) NOT NULL,
  `DebugCodeID` int(11) DEFAULT NULL,
  `EditCodeID` int(11) DEFAULT NULL,
  `IDEOutputID` int(11) DEFAULT NULL,
  `ExplorerRelatedID` int(11) DEFAULT NULL,
  `ActionID` int(11) DEFAULT NULL,
  `type` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `INFOMATIONACTIONID` (`ActionID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `keywords` (
  `inforID` int(11) DEFAULT NULL,
  `candidateKeyWords` text,
  `userKeyWords` text,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1282 DEFAULT CHARSET=utf8 COMMENT='保存程序员通过agent检索的关键词，以及前100个候选关键词';


CREATE TABLE `runtimeinformation` (
  `id` int(11) NOT NULL,
  `type` varchar(45) DEFAULT NULL COMMENT 'framework message , program output  or exceptional message',
  `content` text,
  `relatedCode` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `syntacticblock` (
  `id` int(11) NOT NULL,
  `type` varchar(45) DEFAULT NULL,
  `code` longtext,
  `exceptionName` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `SYNTACTICTYPE` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




