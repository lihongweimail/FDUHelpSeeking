package cn.edu.fudan.se.helpseeking.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    	
    	
    	
    	Calendar c = Calendar.getInstance();
    	  c.setTimeInMillis(new Date().getTime());
 //   	  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	  System.out.println(c.getTime().getHours()+" "+c.getTime().getMinutes()+" "+c.getTime().getSeconds()+" "+c.getTimeInMillis());
//    	  System.out.println(dateFormat.format(c.getTime()));
    	
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
    
}  