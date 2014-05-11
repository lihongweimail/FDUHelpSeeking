package cn.edu.fudan.se.helpseeking.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import cn.edu.fudan.se.helpseeking.util.CommUtil;
import cn.edu.fudan.se.helpseeking.util.INIHelper;
import cn.edu.fudan.se.helpseeking.util.Resource;

public class ExpirmentTest {

	

	
	public static void main(String[] args) {
		
		String good="we.are.the live";
		int indexs=good.lastIndexOf('.');
		String result=good.substring(0, indexs);
		System.out.println(result);
	
//		INIHelper ini=new INIHelper("/cse.ini",false);
//		ini.getCxKeyPairs();
		

//		System.out.println(re.getResource("/stopresource/userstoplist.txt"));

		
/*		List<TaskDescription> myTaskList=new ArrayList<TaskDescription>(); 	
		DatabaseUtil.init();
		
						myTaskList=DatabaseUtil.getTaskDescriptionRecords();
						TaskDescription tDescription=myTaskList.get(0);
						TaskDescription tDescription2=myTaskList.get(1);
						
						String test=tDescription.getContent();
						TokenExtractor tokenExtractor=new TokenExtractor();
                        List<String> hh=removeDuplicateWithOrder((tokenExtractor.getIdentifierOccurenceOfString(test)));
                        		Collections.sort(hh);
                        		
                        		String test2=tDescription2.getContent();
                        		List<String>hh2=removeDuplicateWithOrder(tokenExtractor.getIdentifierOccurenceOfString(test2));
                        		Collections.sort(hh2);
                        		
                        		Collection<String> aa= CollectionUtils.intersection(hh, hh2);
                        		
                        		Collections.sort( (List<String>) aa);
                        		System.out.println(aa.toString());

						String tokens=CommUtil.ListToString(hh);
						
					
						System.out.println(tokens);
						
		
		DatabaseUtil.closeAll();*/
		
		
//		System.out.println(Basic.KeyWordsLevel.Level_Five.getLevelNumber());
		
		
//		 Stu stu1=new Stu("foyd",22.1);  
//         Stu stu2=new Stu("teddy",19.1);  
//         Stu stu3=new Stu("dean",26.1);  
//         Stu stu4=new Stu("lucas",19.1);  
//         Stu stu5=new Stu("tina",26.1);  
//           
//         List<Stu> list = new ArrayList<Stu>();  
//         list.add(stu1);  
//         list.add(stu2);  
//         list.add(stu3);  
//         list.add(stu4);  
//         list.add(stu5);  
//           
//         Comparator<Stu> comparator = new Comparator<Stu>() {  
//             public int compare(Stu p1, Stu p2) {//return必须是int，而str.age是double,所以不能直接return (p1.age-p2.age)  
//                 if((p1.age-p2.age)<0)   
//                     return -1;  
//                 else if((p1.age-p2.age)>0)  
//                     return 1;  
//                 else return 0;  
//             }  
//         };  
//         //jdk 7sort有可能报错，  
//         //加上这句话:System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");  
//         //表示，使用以前版本的sort来排序  
//         Collections.sort(list,comparator);  
//           
//         for(int i=0;i<list.size();i++)  
//         {  
//             System.out.println(list.get(i).age+"  "+list.get(i).name);  
//         }  
//    
		
//         Display display = new Display();
// 		Shell shell = new Shell(display);
// 		Link link = new Link(shell, SWT.BORDER);
// 		link.setText("This a very simple <A>link</A> widget.");
// 		Rectangle clientArea = shell.getClientArea();
// 		link.setBounds(clientArea.x, clientArea.y, 140, 40);
// 		link.addListener (SWT.Selection, new Listener () {
//			@Override
//			public void handleEvent(Event event) {
//				System.out.println("Selection: " + event.text);
//			}
//		});
// 		
// 
// 		
// 		shell.pack ();
// 		shell.open();
// 		while (!shell.isDisposed()) {
// 			if (!display.readAndDispatch())
// 				display.sleep();
// 		}
// 		display.dispose();	
		
		
	}

	public static void campareString() {
		List<String> last=CommUtil.stringToList("ambient probramming good ", "[ ]");
		List<String> now=CommUtil.stringToList("probramming good  ambient", "[ ]");
		

	}
	
	public static List<String> removeDuplicateWithOrder(List<String> list) {
		
		List<String> listTemp= new ArrayList<String>();  
		 Iterator<String> it=list.iterator();  
		 while(it.hasNext()){  
		  String a=it.next();  
		  if(listTemp.contains(a)){  
		   it.remove();  
		  }  
		  else{  
		   listTemp.add(a);  
		  }  
		 }  
		        return listTemp;
		}


	
public   String  getResource( String resourcePath) {    
		
	//编译阶段将文件放入到BIN目录，  生成JAR包时 记得将文件打包到JAR包的根目录下； 使用相对路径
	// “/a/b.txt”  和 “a/b.txt”不同一个是从根出发， 一个是从当前调用这个方法的类所在的相对路径出发。  通常选前面的格式
		String content="";
        //返回读取指定资源的输入流    
		try{
        InputStream is=this.getClass().getResourceAsStream(resourcePath);     //"/resource/res.txt"
        BufferedReader in=new BufferedReader(new InputStreamReader(is));  
        
    	StringBuilder buffer = new StringBuilder();
		String line = null;

		while (null != (line = in.readLine()))
		{
			buffer.append("\t" + line);
			buffer.append("\n");

		}

		content = buffer.toString();
		in.close();

	}
	catch (IOException e)
	{
		e.printStackTrace();
	}
	return content;
    }    


}
