package cn.edu.fudan.se.helpseeking.preferences;

import java.util.LinkedList;

public class PreferenceConstants {
	
	//DB PREFERENCEPAGE VALUE
	//为文本框定义三个键值      
	public static final String URL_KEY = "$URL_KEY"; 
	public static final String USERNAME_KEY = "$USERNAME_KEY";    
	public static final String PASSWORD_KEY = "$PASSWORD_KEY";  
	public static final String DATABASE_KEY="$DATABASE_KEY";
	//为文本框值定义三个默认值      
	public static final String URL_DEFAULT = "jdbc:sqlite://Users/Grand/temp/test.db";
			//"jdbc:mysql://localhost:3306/";     
	public static final String USERNAME_DEFAULT = "root";     
	public static final String PASSWORD_DEFAULT = "root";
	public static final String DATABASE_DEFAULT="helpseeking";
	
	
//	TACTIC  PREFERENCEPAGE VALUE
    //	为策略定义键值
	public static final String  TACTICMODE_KEY="$TACTICMODE_KEY";
    //	默认值
	public static final  int TACTICMODE_DEFAULT= 1;
	public static final String SELECTTACTIC_DEFAULT="Simple Tactic";
	
	
	
	//CUSTOME SEARCH ENGINE SET VALUE
	public static final String CSE_KEY="$CSE_KEY";
	public static final String CSE_CX="$CSE_CX";
			//key=AIzaSyCr7g1tTLyy1MYOT8osYiBhuNQX4Od5JFM
			//cx=017576662512468239146:omuauf_lfve
	
	public static final String CSE_KEY_DEFAULT="AIzaSyCr7g1tTLyy1MYOT8osYiBhuNQX4Od5JFM";
	public static final String CSE_CX_DEFAULT="017576662512468239146:omuauf_lfve";
	
	
	
	

}
