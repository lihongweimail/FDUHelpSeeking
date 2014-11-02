package cn.edu.fudan.se.helpseeking.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.edu.fudan.se.helpseeking.util.CommUtil;

public class Stu {  
    public double age;  
    public String name;  
    public Stu(String name,double age)  
    {  
        this.name = name;  
        this.age = age;  
    }  
    
    public static void main(String[] args) {
    	
    	String tempstr="java.lang.String.replace(CharSequence.target, CharSequence.replacement)";
    	//io.java.iostream
    	//java.util.Calendar.setTimeInMillis(long millis)
//java.lang.String.replace(CharSequence target, CharSequence replacement)
    	//
    	
    	getSimpleWords(tempstr);
		
    	
    	
//    	Calendar c = Calendar.getInstance();
//    	  c.setTimeInMillis(new Date().getTime());
// //   	  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    	  System.out.println(c.getTime().getHours()+" "+c.getTime().getMinutes()+" "+c.getTime().getSeconds()+" "+c.getTimeInMillis());
////    	  System.out.println(dateFormat.format(c.getTime()));
    	
//    	for (int i = 0; i < 100; i++) {
//    	int temp=CommUtil.randomInt(CommUtil.getKeyCxList().size()-1, 0);
//    	
//		
//		String cse_key=CommUtil.getKeyCxList().get(temp).getKey();
//		
//		String cse_cx=CommUtil.getKeyCxList().get(temp).getCx();
//    	
//		if (temp==0) {
//			System.out.println("\nhello zero!");
//		}
//		System.out.println("\ntemp:\t"+temp+"\nkey:\t"+cse_key+"\ncx:\t"+cse_cx);
//		
//		
//    	
//			int Temp = (int) Math.round(Math.random() * 7 + 18);
//			System.out.println(Temp);

//		}
	}

	private static String getSimpleWords(String tempstr) {
		String resultstr="";
    	if (tempstr.contains("(")) {
						
			int firstpartlastIndex=tempstr.indexOf('(');
			String firstPart=tempstr.substring(0, firstpartlastIndex);
			System.out.println("firstpart: "+firstPart);
			List<String> namePart=CommUtil.stringToList(firstPart, "[.]");
			String name=namePart.get(namePart.size()-1);
			
			String secondPart=tempstr.substring(firstpartlastIndex+1,tempstr.length()-1);
			System.out.println("secondpart: "+ secondPart);
			List<String> secondkeywordparts=new ArrayList<String>();
			secondkeywordparts=CommUtil.stringToList(secondPart, "[,]");
			
			String parameterList="";
			
			for (int i = 0; i < secondkeywordparts.size(); i++) {
				if (secondkeywordparts.get(i).trim().contains(" ")) {
					List<String> para=new ArrayList<String>();
					para=CommUtil.stringToList(secondkeywordparts.get(i), "[ ]");
					
					if (parameterList.equals("")) {
						parameterList=para.get(0);
					}else
					{parameterList=parameterList+" , "+para.get(0);}
					
				}
				else
				{
				List<String> para=new ArrayList<String>();
				para=CommUtil.stringToList(secondkeywordparts.get(i), "[.]");

				if (parameterList.equals("")) {
					parameterList=para.get(para.size()-1);
				}else
				{parameterList=parameterList+" , "+para.get(para.size()-1);}
				}
			}
			
			resultstr=name +"("+parameterList+")";
 
			}
			else 
			{
				List<String> packageClassName=new ArrayList<String>();
				packageClassName=CommUtil.stringToList(tempstr, "[.]");
					resultstr=packageClassName.get(packageClassName.size()-1);
				
				
			}
		
        System.out.println("last formal: result" + resultstr);
        return resultstr;
	}
    
}  