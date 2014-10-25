package cn.edu.fudan.se.helpseeking.test;

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
    	
    	
    	for (int i = 0; i < 100; i++) {
    	int temp=CommUtil.randomInt(CommUtil.getKeyCxList().size()-1, 0);
    	
		
		String cse_key=CommUtil.getKeyCxList().get(temp).getKey();
		
		String cse_cx=CommUtil.getKeyCxList().get(temp).getCx();
    	
		if (temp==0) {
			System.out.println("\nhello zero!");
		}
		System.out.println("\ntemp:\t"+temp+"\nkey:\t"+cse_key+"\ncx:\t"+cse_cx);
		
//		
//    	
//			int Temp = (int) Math.round(Math.random() * 7 + 18);
//			System.out.println(Temp);

		}
	}
    
}  