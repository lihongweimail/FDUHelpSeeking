package cn.edu.fudan.se.helpseeking.abandoncodefragment;

import java.util.ArrayList;


import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.edu.fudan.se.helpseeking.bean.KeyWord;

public class testRun {

	public static void main(String[] args) {
	List<KeyWord> he=new ArrayList<KeyWord>();
		
		KeyWord ni0=new KeyWord();
		 ni0.setKeywordName("he");
		 ni0.setScore(1.2);
			KeyWord ni1=new KeyWord();
			 ni1.setKeywordName("she");
			 ni1.setScore(1.8);
				KeyWord ni2=new KeyWord();
				 ni2.setKeywordName("me");
				 ni2.setScore(2.5);
					KeyWord ni3=new KeyWord();
					 ni3.setKeywordName("whe");
					 ni3.setScore(0.2);
						KeyWord ni4=new KeyWord();
						 ni4.setKeywordName("iehe");
						 ni4.setScore(8.2);
							KeyWord ni5=new KeyWord();
							 ni5.setKeywordName("owhe");
							 ni5.setScore(11.4);
							 
							 he.add(ni0);
							 he.add(ni1);
							 he.add(ni2);
							 he.add(ni3);
							 he.add(ni4);
							 he.add(ni5);
							 for (KeyWord keyWord : he) {
									 System.out.println("before sort:" +keyWord.getKeywordName()+":"+keyWord.getScore());
							}
						
								Collections.sort(he, new Comparator<KeyWord>() {
									public int compare(KeyWord arg0, KeyWord arg1)
									{
										return (int)arg1.getScore()-(int)arg0.getScore();
									}
								});
								
								 for (KeyWord keyWord : he) {
									 System.out.println("after sort:" +keyWord.getKeywordName()+":"+keyWord.getScore());
							}
	
		
		
		
	}
}
