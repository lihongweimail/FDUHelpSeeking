package cn.edu.fudan.se.helpseeking.preferences;

public class PreferenceConstants {
	
	//DB PREFERENCEPAGE VALUE
	//为文本框定义三个键值      
	public static final String URL_KEY = "$URL_KEY"; 
	public static final String USERNAME_KEY = "$USERNAME_KEY";    
	public static final String PASSWORD_KEY = "$PASSWORD_KEY";     
	//为文本框值定义三个默认值      
	public static final String URL_DEFAULT = "jdbc:mysql://localhost:3306/helpseeking";     
	public static final String USERNAME_DEFAULT = "root";     
	public static final String PASSWORD_DEFAULT = "root";     
	
	
//	TACTIC  PREFERENCEPAGE VALUE
    //	为策略定义键值
	public static final String  TACTICMODE_KEY="$TACTICMODE_KEY";
    //	默认值
	public static final  int TACTICMODE_DEFAULT= 1;
	public static final String SELECTTACTIC_DEFAULT="Simple Tactic";
	
	

}
