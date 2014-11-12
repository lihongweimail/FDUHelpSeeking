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
    public static String genShortName(String labels) {
    	
  
    		int index;
    		int lastindex;
    		int indexbrack;
    		
    		    		
		if (labels.contains("(")) {

			if (labels.contains(".")) {
				index = labels.indexOf(".");
				lastindex = labels.lastIndexOf(".");

				indexbrack = labels.indexOf("(");

				if (indexbrack > lastindex) {
					String templeftString = labels.substring(0, indexbrack);
					String[] cmlist = templeftString.split("[.]");
					int size = cmlist.length;
					String classMethodname = cmlist[size - 2] + "."
							+ cmlist[size - 1];

					String otherpart = labels.substring(indexbrack);
					    
					if (otherpart.contains(",")) {
						
					
					       String[] otherpartlist=otherpart.split("[,]");
					       for (int i = 0; i < otherpartlist.length; i++) {
							otherpartlist[i]=genShortName(otherpartlist[i]);
						}
					       
					
					}    

					labels = classMethodname + otherpart;

				} else {
					
					
					
				}

			}

		} else {

		}
   
    return labels;
    
    }

    public static void main(String[] args) {
    	
    	String tempstr="java.io.string;java.lang.String.replace(CharSequence.target(I,wo), CharSequence.replacement());";
    	//io.java.iostream
    	//java.util.Calendar.setTimeInMillis(long millis)
//java.lang.String.replace(CharSequence target, CharSequence replacement)
    	//
    	
    	getSimpleWords(tempstr);
		
   // System.out.println(genShortName(tempstr));
    	
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
		tempstr=tempstr.replace(';', ' ');
    	if (tempstr.contains("(")) {
						
			int firstpartlastIndex=tempstr.indexOf('(');
			String firstPart=tempstr.substring(0, firstpartlastIndex);
			System.out.println("firstpart: "+firstPart);
			List<String> namePart=CommUtil.stringToList(firstPart, "[.]");
			
			String name=namePart.get(namePart.size()-1);
			if (namePart.size()>1) {
				name=namePart.get(namePart.size()-2)+"."+namePart.get(namePart.size()-1);
			}
			
			int lastpartIndex=tempstr.lastIndexOf(')');
			String secondPart=tempstr.substring(firstpartlastIndex+1,lastpartIndex);
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
		
        System.out.println("last formal: result : " + resultstr);
        return resultstr;
	}
    
}  