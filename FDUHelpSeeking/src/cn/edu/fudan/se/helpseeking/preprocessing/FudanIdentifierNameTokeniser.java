package cn.edu.fudan.se.helpseeking.preprocessing;

import java.util.ArrayList;
import java.util.List;

import uk.ac.open.crc.intt.IdentifierNameTokeniser;
import cn.edu.fudan.se.helpseeking.util.CommUtil;
import cn.edu.fudan.se.helpseeking.util.FileHelper;
import cn.edu.fudan.se.helpseeking.util.INIHelper;


public class FudanIdentifierNameTokeniser 
{
	//TODO lihongwei add 2011-10-31
	public static final String JAVA_STOP_LIST_FILENAME;
	public static final String SPLIT_STRING = "[&#$_.(){}!*%+-=><\\:;,?/\"\'\t\b\r\n\0 ]";
	

	private static final String stop_list_path ;
	public static final String USER_STOP_LIST_FILENAME;
	
	
	static {
		INIHelper iniHelper = new INIHelper("conf.ini");
		stop_list_path = iniHelper.getValue("IDENTIFIEREXTRACTOR", "path", "StopResource");
		JAVA_STOP_LIST_FILENAME = iniHelper.getValue("IDENTIFIEREXTRACTOR", "javaStopList", "javaStopList.txt");
		USER_STOP_LIST_FILENAME= iniHelper.getValue("IDENTIFIEREXTRACTOR", "userStopList", "userStopList.txt");
		
	}

	private IdentifierNameTokeniser identifierNametokeniser;
	private boolean isIgnoreCase=true;
	private List<String> keyWords=new ArrayList<String>();
	private int minLength =2;
	
	
	public FudanIdentifierNameTokeniser()
	{
		super();
	}
	
	
	public FudanIdentifierNameTokeniser(IdentifierNameTokeniser tokeniser)
	{
		identifierNametokeniser = tokeniser;
		minLength = 1;	
		isIgnoreCase = true;
		constructDefaultFilterString(JAVA_STOP_LIST_FILENAME,USER_STOP_LIST_FILENAME);
		

		
	}
	
	private void constructDefaultFilterString(String stopfileName, String userStopFileName)
	{	
		stopfileName = CommUtil.getCurrentProjectPath() + "\\" + stop_list_path +  "\\" + stopfileName;
		userStopFileName=CommUtil.getCurrentProjectPath()+"\\"+ stop_list_path + "\\"+userStopFileName;
		
	     String tempKeyWords1 = FileHelper.getContent(stopfileName);
	     String tempKeyWords2 = FileHelper.getContent(userStopFileName);
	     
	     keyWords=CommUtil.arrayToList((tempKeyWords1+","+tempKeyWords2).split(SPLIT_STRING));
	     for (int i = 0; i < keyWords.size()-1; i++) {
			for (int j = i+1; j < keyWords.size(); j++) {
				if ((keyWords.get(i).trim()).equals(keyWords.get(j).trim())) {
					
//					System.out.println("remove word: "+keyWords.get(j)+" j="+j);
					keyWords.remove(j);
					j=j-1;
				}
			}
			
		}
	    	 
	    		
       //  System.out.println("stop list debug!");
	     /* for(int i=0;i<tempKeyWords1.length;i++)
		 {   
			 if (tempKeyWords1[i].length()==0)
				 tempKeyWords1[i]=" ";
			 keyWords[i]=tempKeyWords1[i].valueof;
			 		 
		 }
		 int j=tempKeyWords1.length;
		 for (int i=0;i<tempKeyWords2.length;i++)
		 {
			 if (tempKeyWords2[i].length()==0)
				 tempKeyWords2[i]=" ";

			 keyWords[i+j]=tempKeyWords2[i];
		 }
*/	
	  }

	
	public void isIgnoreCase(boolean isIgnoreCase)
	{
	
		this.isIgnoreCase = isIgnoreCase;
	}

	
	
	public void setMinTokenLength(int minLength)
	{
		this.minLength = minLength;
	}

	

	public List<String> tokeniseOnly(String line)
	{
		String[] tokens = identifierNametokeniser.tokenise(line);
		ArrayList<String> result = new ArrayList<String>();		
		for(String token : tokens)
		{
			token = token.trim();
			// add lihongwei 2011-10-28
			if (token.equals("") ) continue;
			
			//   add end 2011-10-28
			
			if(token.length() >= minLength && (int) token.charAt(0)< 255)
			{
				if( this.isIgnoreCase)
				{
					token = token.toLowerCase();		
				}	
                
				result.add(token);
			}
		}				
		return result;		

}
	
	
	
	public List<String> tokenise(String line)
	{
		String[] tokens = identifierNametokeniser.tokenise(line);
		ArrayList<String> result = new ArrayList<String>();		
		for(String token : tokens)
		{
			token = token.trim();
			// add lihongwei 2011-10-28
			if (token.equals("") ) continue;
			
			//   add end 2011-10-28
			
			if(token.length() >= minLength && (int) token.charAt(0)< 255)
			{
				if( this.isIgnoreCase)
				{
					token = token.toLowerCase();		
				}	
                boolean flage=false;
				for (String keyword : keyWords) {
					if (token.trim().equals(keyword.trim())) {
//						System.out.println("not add keyword:"+ token);
						flage=true;
						break;
					}
				}
				
				if (!flage) {
						result.add(token);
				}
			
			}
		}				
//		for(String keyword : keyWords)
//		{
//			
//			result.remove(keyword);
//		}
//			
		return result;		
	}	
	
	
	
	
}
