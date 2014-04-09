package cn.edu.fudan.se.helpseeking.processing;

import java.nio.file.AccessMode;
import java.util.ArrayList;
import java.util.List;

import cn.edu.fudan.se.helpseeking.bean.ActionCache;
import cn.edu.fudan.se.helpseeking.bean.ActionInformation;
import cn.edu.fudan.se.helpseeking.bean.Cache;


public class CacheProcessing extends Thread {
	
	//获取单例
	Cache currentCache=Cache.getInstance();
	
	static Object obj = new Object();

	public void run() {
	
		synchronized (obj) {
			// 同步块中！防止 出错！
			
			actionInformations=new ArrayList<ActionInformation>();	
//     从滑动窗口中获得动作给actionFrequencies赋值  并统计频率;最后按照频率值对actionfrequencies的元素降序排序
			constructAndcountActionFrequency();
			//统计资源			
//TODO ??统计资源和信息	

//			信息切词
			
			
//			抽取关键词

			

		}
	}



//	一个动作就有其对应的一组资源； 这些资源可能相同，可能不同；目前系统实现采用一个动作actionid，其相关的资源的ID都相同	
//  获得一个动作使用的资源数量	
	
	
	List <ActionInformation> actionInformations;	
//	获得动作窗口内相同动作的频率fi  : 出现次数除以总次数
//	对资源信息的权重最简单的安排： 就是频率高的动作的资源权重高  

	public void constructAndcountActionFrequency() {
            
		//遍历动作滑动窗口并统计
		for (ActionCache  ac: (currentCache.getActionSlideWindow().getActionList())) 
		{
				boolean flage=false;
			  for (ActionInformation af : actionInformations)
			  {
				if (af.getActionKind()==ac.getAction().getActionKind()  && (af.getActionName()==ac.getAction().getActionName())) 
				{
					int actionID=ac.getActionID();
					af.setCount(af.getCount()+1);//计数
					af.getActionIDList().add(actionID);
					//TODO:  20140409   策略太简单，先试试；  简单地添加信息到串中
//					从currentcache中获得actionID相同的资源加入inforString相应的串中
//				    几类信息的字符串 
//				    0 对应Debugcode
//				    1对应Editcode
//				    2对应ideOutput
//				    3对应explorerRelated
//				    4对应当前类模型CruuentClassModel
				    String[] inforString=new String[5];
	    
				    inforString[0]=inforString[0]+" "+currentCache.findDebugCodeString(actionID);
				    inforString[1]=inforString[1]+" "+currentCache.findEditCodeString(actionID);
				    inforString[2]=inforString[2]+" "+currentCache.findIDEOutputString(actionID);
				    inforString[3]=inforString[3]+" "+currentCache.findExplorerRelatedString(actionID);
				    inforString[4]=inforString[4]+" "+currentCache.findCurrentClassModel();
				    
					af.setInforString(inforString);
					
					
					flage=true;
					break;					
				}								
			}
			  //没有找到相同的动作则添加
			if (!flage)   
			{
					ActionInformation tempAF=new ActionInformation();
					tempAF.setActionKind(ac.getAction().getActionKind());
					tempAF.setActionName(ac.getAction().getActionName());
					tempAF.setCount(1);
					tempAF.setFrequency((float)0.0);
					tempAF.getActionIDList().add(ac.getActionID());
					//TODO:  20140409   策略太简单，先试试；  简单地添加信息到串中
					
					
				    actionInformations.add(tempAF);
					
				}
		}
		
//		计算频率比值
//		求和
		int sumCount=0;
		 for (ActionInformation af : actionInformations) {
		 sumCount=sumCount+af.getCount();
			
		}
		 
		 ActionInformation[] farray=new  ActionInformation[actionInformations.size()];
		 
//		 求频率， 复制
		 for (int i=0;i<actionInformations.size();i++) {
			 actionInformations.get(i).setFrequency( actionInformations.get(i).getCount()*(float)1.0/sumCount);
	         farray[i]=actionInformations.get(i);
			}
		 
		 //清理
		 actionInformations.clear();
		 int bigestIndex=0;
		 float bigestvalue=0;
		 //排序
		 for(int j=0;j<farray.length-1;j++)
		 {    bigestIndex=j;
		       bigestvalue=farray[j].getFrequency();
//		       检查是否越界
			 for (int i=j+1;i<farray.length;i++) 
			 {
				 System.out.println("j="+j+"\t i="+i+"\n");
				 if (farray[i].getFrequency()>bigestvalue) {
					 bigestIndex=i;
				 }
			 }
//			 交换
			 if (bigestIndex!=j) {
				 ActionInformation tempA=farray[j];
				 farray[j]=farray[bigestIndex];
				 farray[bigestIndex]=tempA;
			}
       }
//		 复原actionFrequencies
		 for (int i = 0; i < farray.length; i++) {
			actionInformations.add(farray[i]);
		}
		 
	}

	
	
	

}
