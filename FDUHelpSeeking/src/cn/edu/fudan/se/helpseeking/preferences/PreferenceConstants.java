package cn.edu.fudan.se.helpseeking.preferences;

import java.util.LinkedList;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;

import cn.edu.fudan.se.helpseeking.FDUHelpSeekingPlugin;
import cn.edu.fudan.se.helpseeking.util.CommUtil;

public class PreferenceConstants {
	

	
//	TACTIC  PREFERENCEPAGE VALUE
    //	为策略定义键值
	public static final String  TACTICMODE_KEY="$TACTICMODE_KEY";
    //	默认值
	public static final  int TACTICMODE_DEFAULT= 1;
	public static final String SELECTTACTIC_DEFAULT="Simple Tactic";

	
	
	//=======/FDUHelpSeeking/src/cn/edu/fudan/se/helpseeking/preferences/DBPreferencePage.java
	
	//DB PREFERENCEPAGE VALUE
	//为文本框定义三个键值      
	public static final String URL_KEY = "$URL_KEY"; 
	public static final String USERNAME_KEY = "$USERNAME_KEY";    
	public static final String PASSWORD_KEY = "$PASSWORD_KEY";  
	public static final String DATABASE_KEY="$DATABASE_KEY";
	
	private static String databasePath=CommUtil.getFDUHelpseekingPluginWorkingPath();
	
	//为文本框值定义三个默认值      
	public static final String URL_DEFAULT = "jdbc:sqlite:"+databasePath+"/helpseeking.db";
			//"jdbc:mysql://localhost:3306/";     
	public static final String USERNAME_DEFAULT = "root";     
	public static final String PASSWORD_DEFAULT = "root";
	public static final String DATABASE_DEFAULT="helpseeking";
	
	
	
	//=======/FDUHelpSeeking/src/cn/edu/fudan/se/helpseeking/preferences/SearchEngineSetPreferencePage.java
	
	//CUSTOME SEARCH ENGINE SET VALUE
	public static final String CSE_KEY="$CSE_KEY";
	public static final String CSE_CX="$CSE_CX";
			//key=AIzaSyCr7g1tTLyy1MYOT8osYiBhuNQX4Od5JFM
			//cx=017576662512468239146:omuauf_lfve
	
	public static final String CSE_KEY_DEFAULT="AIzaSyCr7g1tTLyy1MYOT8osYiBhuNQX4Od5JFM";
	public static final String CSE_CX_DEFAULT="017576662512468239146:omuauf_lfve";
	
	
	
	//==========/FDUHelpSeeking/src/cn/edu/fudan/se/helpseeking/preferences/AlgorithmParameterPreferencePage.java
	
	// 新算法权重
	
	// 对于action来说有两个权重设置： weightOne 和 weightTwo 他们分别用于描述 interest level 中 action
	// 的级别 以及 API 来源的级别
	// DOI -model (Degree of Interestes)
	// interest level action : select 1 < reveal 2 < save or debug 3 < execute 4
	// interest level api : normal 1 < has-compile_error 2 < cause_exception 3
	//为文本框定义键值      
	public static final String ACTION_SELECT_KEY = "$ACTION_SELECT_KEY"; 
	public static final String ACTION_REVEAL_KEY = "$ACTION_REVEAL_KEY";    
	public static final String ACTION_SAVEORDEBUG_KEY = "$ACTION_SAVEORDEBUG_KEY";  
	public static final String ACTION_EXECUTE_KEY="$ACTION_EXECUTE_KEY";
	public static final String ACTION_SELECTFOCUS_KEY = "$ACTION_SELECTFOCUS_KEY"; 


	public static final String API_NORMAL_KEY = "$API_NORMAL_KEY"; 
	public static final String API_HASCOMPILEERROR_KEY = "$API_HASCOMPILEERROR_KEY";    
	public static final String API_CAUSEEXCEPTION_KEY = "$API_CAUSEEXCEPTION_KEY"; 
	
	public static final String GAMA_KEY = "$GAMA_KEY"; 
		

	// 设置默认值
	public static  final int al_action_select_default = 1;
	public static  final int al_action_reveal_default = 2;
	public static  final int al_action_saveOrDebug_default = 3;
	public static  final int al_action_execute_default = 4;
	public static  final int al_action_selectfocus_default = 5;

	public static  final int al_api_normal_default = 1;
	public static  final int al_api_hasCompileError_default = 2;
	public static  final int al_api_causeException_default = 3;

	public static  final double al_gama_default = 2.0; // 新算法权重值

	// 光标所在行附近的代码： 目前是设置为前后2行；共计5行；为了更加关注，设置为1行，就自己
//  以上均需取走
	
	
	//========/FDUHelpSeeking/src/cn/edu/fudan/se/helpseeking/preferences/GoogleSearchParametersPreferencePage.java
	
	// google search setting 
	//为文本框定义键值      
	public static final String WAIT_GOOGLE_TIME_KEY = "$WAIT_GOOGLE_TIME_KEY"; 
	public static final String GOOGLE_RESULT_NUMBERS_KEY = "$ACTION_REVEAL_KEY";    
	public static final String GOOGLE_SEARCH_ROUND_KEY = "$GOOGLE_SEARCH_ROUND_KEY";  

	//设置默认值
	
	public final static int WAIT_GOOGLE_TIME_DEFAULT = 6000;
	
	public final static int GOOGLE_RESULT_NUMBERS_DEFAULT = 10; 
	// default 10 to get 10
    // urls ; values as
    // 1--10  用于取结果数量的，即一次检索后，从Google获得多少个URL
    // 一般为4  配合GOOGLE_SERCH_ROUND ， 即它们相乘为最后获得的URL数量
	
	public final static int GOOGLE_SEARCH_ROUND_DEFAULT = 3; 
	// default 10 for get 100  max is 10
    // url ; test as 1 get 10 urls
    // 用于设置获取url数量的参数，功能是，就一个查询的结果集中去取的次数，每次取的数量为GOOGLE_RESULT_NUMBERS个

	
	//=== /FDUHelpSeeking/src/cn/edu/fudan/se/helpseeking/preferences/FoamtreeSetPreferencePage.java
	//为文本框定义键值      
	public static final String FOAMTREE_K_KEYWORDS_KEY = "$FOAMTREE_K_KEYWORDS_KEY"; 
	public static final String DIFFERENTPOSITION_COUNT_KEY = "$DIFFERENTPOSITION_COUNT_KEY";    
	public static final String RATIO_OF_NEW_KEYWORDS_KEY = "$RATIO_OF_NEW_KEYWORDS_KEY";  

	
	
	public final static int FOAMTREE_K_KEYWORDS_DEFAULT = 8; // 或者提供给第一个foamtree1   
	public final static int DIFFERENTPOSITION_COUNT_DEFAULT = 4;
// 相同词按分值排序后位置不同  
//另一个参数，就是生成的新候选词集合和现在在foamtree中的词集合中词的rank值得变化有N个不同时更新foamtree
	public final static double RATIO_OF_NEW_KEYWORDS_DEFAULT = 0.24; 
//一个参数，就是生成的新候选词集合和现在在foamtree中的词集合中词的不同词的的比率情况，当大于这个值时更新foamtree
//调节foamtree的变化速率的参数值越大速率越慢，取值范围0<x<1。  给定一个值，两个串中的不相同的词大于这个值，则容许刷新新的一组关键词
// 这个比例要按照系统已有的词的数量来动态调整，如果系统候选词多可以放大，如果系统词少例如就10个词的情况，就小一点
//词的集合变化太少
// 变化率RATIOOFNEWSEARCHSTRING设置为0.1
// （原来设置0.6 或0.38）

	
	

}
