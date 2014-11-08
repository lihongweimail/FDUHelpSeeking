// 正常可用的 LdaGibbsSampling.java

package liuyang.nlp.lda.main;

import java.beans.beancontext.BeanContext;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.fnlp.nlp.cn.CNFactory;
import org.fnlp.nlp.cn.ner.stringPreHandlingModule;
import org.fnlp.util.exception.LoadModelException;
import org.hamcrest.core.Is;
import org.htmlparser.util.ParserException;

import cn.edu.fudan.se.helpseeking.bean.Basic;
import cn.edu.fudan.se.helpseeking.bean.FudanTopicWithWordsListBean;
import cn.edu.fudan.se.helpseeking.bean.TopicWEBPagesBean;
import cn.edu.fudan.se.helpseeking.bean.WEBPageBean;
import cn.edu.fudan.se.helpseeking.util.CommUtil;
import cn.edu.fudan.se.helpseeking.util.FileHelper;
import cn.edu.fudan.se.helpseeking.util.Resource;
import liuyang.nlp.lda.com.FileUtil;
import liuyang.nlp.lda.conf.ConstantConfig;
import liuyang.nlp.lda.conf.PathConfig;
import liuyang.nlp.lda.conf.UrlConfig;
import liuyang.nlp.lda.main.Documents.Document;
import urlWordExtract.GetT;
import urlWordExtract.NewWordExtract;
import urlWordExtract.UrlWordExtract;

/**Liu Yang's implementation of Gibbs Sampling of LDA
 * @author yangliu
 * @blog http://blog.csdn.net/yangliuy
 * @mail yangliuyx@gmail.com
 */

public class LdaGibbsSampling {
	
	public static class modelparameters {
		float alpha = 0.5f; //usual value is 50 / K
		float beta = 0.1f;//usual value is 0.1
		int topicNum = 100;
		int iteration = 100;
		int saveStep = 10;
		int beginSaveIters = 50;
	}
	
	/**Get parameters from configuring file. If the 
	 * configuring file has value in it, use the value.
	 * Else the default value in program will be used
	 * @param ldaparameters
	 * @param parameterFile
	 * @return void
	 */
	private static void getParametersFromFile(modelparameters ldaparameters,
			String parameterFile) {
		// TODO Auto-generated method stub
		ArrayList<String> paramLines = new ArrayList<String>();
		FileUtil.readLines(parameterFile, paramLines);
		for(String line : paramLines){
			String[] lineParts = line.split("\t");
			switch(parameters.valueOf(lineParts[0])){
			case alpha:
				ldaparameters.alpha = Float.valueOf(lineParts[1]);
				break;
			case beta:
				ldaparameters.beta = Float.valueOf(lineParts[1]);
				break;
			case topicNum:
				ldaparameters.topicNum = Integer.valueOf(lineParts[1]);
				break;
			case iteration:
				ldaparameters.iteration = Integer.valueOf(lineParts[1]);
				break;
			case saveStep:
				ldaparameters.saveStep = Integer.valueOf(lineParts[1]);
				break;
			case beginSaveIters:
				ldaparameters.beginSaveIters = Integer.valueOf(lineParts[1]);
				break;
			}
		}
	}
	
	public enum parameters{
		alpha, beta, topicNum, iteration, saveStep, beginSaveIters;
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, ParserException {
		// TODO Auto-generated method stub
		String originalDocsPath = PathConfig.ldaDocsPath;
		String resultPath = PathConfig.LdaResultsPath;
		String parameterFile= ConstantConfig.LDAPARAMETERFILE;
		//String Url1= UrlConfig.Url1;
		//String Url2= UrlConfig.Url2;
		
		modelparameters ldaparameters = new modelparameters();
		getParametersFromFile(ldaparameters, parameterFile); //参数一个来自本身设置，一个来自文件

		ArrayList<String> UrlList = new ArrayList<String>();
		UrlConfig.UrlReader(UrlList);
		System.out.println(UrlList);
		
		
		
		//读待采样数据
		String UrlContent = new String();
		for(String CurrentUrl: UrlList){
			System.out.println(CurrentUrl);
			try {
				UrlContent += UrlWordExtract.getText(CurrentUrl);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Documents docSet = new Documents();
		docSet.readDocs(UrlContent);//读待采样数据	
		
		
		//System.out.println(UrlWordExtract.getText(Url2) );		
		System.out.println("wordMap size " + docSet.termToIndexMap.size());
		FileUtil.mkdir(new File(resultPath));//检测结果存放文件夹是否存在，存在继续，不存在创造一个
		LdaModel model = new LdaModel(ldaparameters);
		System.out.println("1 Initialize the model ...");
		model.initializeModel(docSet);
		System.out.println("2 Learning and Saving the model ...");
		model.inferenceModel(docSet,false);	
		System.out.println("3 Output the final model ...");
		model.saveIteratedModel(ldaparameters.iteration, docSet);
		System.out.println("Done!");
	}


	public static List<FudanTopicWithWordsListBean> fduTopicURLfilter(String topicName, List<TopicWEBPagesBean> allWebPages)  
 {
		//write LdaParameters file
		
		
		
		// TODO Auto-generated method stub
		//String originalDocsPath = CommUtil.getFDUPluginWorkingPath()+"/"+PathConfig.ldaDocsPath;
		String resultPath = CommUtil.getFDUPluginWorkingPath()+"/"+PathConfig.LdaResultsPath;
		String parameterFile= CommUtil.getFDUPluginWorkingPath()+"/"+ConstantConfig.LDAPARAMETERFILE;
		//String Url1= UrlConfig.Url1;
		//String Url2= UrlConfig.Url2;
		
		Resource ldaParameters = new Resource();
		String ldaParametersFileContent;

		ldaParametersFileContent = ldaParameters.getResource(
				"/ldaData/LdaParameter/LdaParameters.txt", true);
		FileHelper.writeNewFile(parameterFile, ldaParametersFileContent);

		
		modelparameters ldaparameters = new modelparameters();
		getParametersFromFile(ldaparameters, parameterFile); //参数一个来自本身设置，一个来自文件

//		ArrayList<String> UrlList = new ArrayList<String>();
//		UrlConfig.UrlReader(UrlList);
//		System.out.println(UrlList);
		
		
		
		
		
		
		//读待采样数据
		String UrlContent = new String();
		int currentTopicindex=0;
		//定位 topic
		for (int i = 0; i < allWebPages.size(); i++) {
			if (topicName.trim().equals(allWebPages.get(i).getTopicName().trim())) {
				currentTopicindex=i;
			}
		}
	

		Documents docSet = new Documents();

		
		
		
	
		//替换方案X 线程池		start	
		ExecutorService pool=Executors.newSingleThreadExecutor();
		List<Future<String>> callablFutures=new ArrayList<Future<String>>();
		try {
	
	    for (int j = 0; j < allWebPages.get(currentTopicindex).getPages().size(); j++) {	
			WEBPageBean CurrentUrl=new WEBPageBean();
					CurrentUrl.setUrl(allWebPages.get(currentTopicindex).getPages().get(j).getUrl());
					CurrentUrl.setTitle(allWebPages.get(currentTopicindex).getPages().get(j).getUrl());
					CurrentUrl.setSummary(allWebPages.get(currentTopicindex).getPages().get(j).getUrl());
	
			System.out.println("claw web page: "+CurrentUrl.getUrl());
			String pageContent="";
	
			Callable<String> c1=new GetT(CurrentUrl.getUrl());
			Future<String> f1=pool.submit(c1);
		    callablFutures.add(f1);
			
		}
	    
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < callablFutures.size(); i++) {
			
			String tempString="";
			try {
				try {
					tempString=callablFutures.get(i).get(Basic.CLAWLIMITEDTIME,TimeUnit.MILLISECONDS);
				} catch (TimeoutException e) {
					// TODO Auto-generated catch block
					tempString="";
					callablFutures.get(i).cancel(true);
					e.printStackTrace();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				tempString="";
				callablFutures.get(i).cancel(true);

				e.printStackTrace();
			} catch (ExecutionException e) {
				tempString="";
				callablFutures.get(i).cancel(true);

				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String summaryText=allWebPages.get(currentTopicindex).getPages().get(i).getSummary();
			
			if (tempString.trim().equals("")) {
				tempString=summaryText;
			}else
			{
				tempString=tempString+" "+summaryText;
			}
			
			

			allWebPages.get(currentTopicindex).getPages().get(i).setContent(tempString);
			
			UrlContent += " \n"+tempString;	

			tempString=CommUtil.fudanSplitWords(tempString);

			
			docSet.readDocs(tempString);//读待采样数据	
		}
		
		pool.shutdown();
		//替换方案X 线程池		 end		
		
		
		
		
/*		
		//替换方案二 ：  htmlparser GBK 编码  开始
		try {
	
	    for (int j = 0; j < allWebPages.get(currentTopicindex).getPages().size(); j++) {	
			WEBPageBean CurrentUrl=new WEBPageBean();
					CurrentUrl.setUrl(allWebPages.get(currentTopicindex).getPages().get(j).getUrl());
					CurrentUrl.setTitle(allWebPages.get(currentTopicindex).getPages().get(j).getUrl());
					CurrentUrl.setSummary(allWebPages.get(currentTopicindex).getPages().get(j).getUrl());
	
			System.out.println("claw web page: "+CurrentUrl.getUrl());
			String pageContent="";

			try {
			
			
			pageContent =CurrentUrl.getSummary()+" "+NewWordExtract.testHtml(CurrentUrl.getUrl());

		} catch (Exception e1) {
							// TODO Auto-generated catch block
			pageContent=CurrentUrl.getSummary();
		    System.out.println("claw web exception , use webpage summary!");
			//e1.printStackTrace();
		}
			
			pageContent = pageContent.replaceAll("&quot;", "\"").replaceAll("&nbsp;", "\"").replaceAll("&#39;", "\'").replaceAll("<b>", " ").replaceAll("</", " ").replaceAll(">", " ").replaceAll("b>", " ").replaceAll(";", " ").replaceAll("&gt", " ").replaceAll("&lt", " ").replaceAll("�", " ");
			
			
			pageContent=CommUtil.fudanSplitWords(pageContent);
//			System.out.println(pageContent);
			allWebPages.get(currentTopicindex).getPages().get(j).setContent(pageContent);
			
			docSet.readDocs(pageContent);//读待采样数据	

			
		}
	    
		} catch (Exception e) {
			e.printStackTrace();
		}
		
*/
		
		
		
		
/*  	//替换方案： 一 雪娇提供

		
		try {
	
	    for (int j = 0; j < allWebPages.get(currentTopicindex).getPages().size(); j++) {	
			WEBPageBean CurrentUrl=new WEBPageBean();
					CurrentUrl.setUrl(allWebPages.get(currentTopicindex).getPages().get(j).getUrl());
					CurrentUrl.setTitle(allWebPages.get(currentTopicindex).getPages().get(j).getUrl());
					CurrentUrl.setSummary(allWebPages.get(currentTopicindex).getPages().get(j).getUrl());
	
			System.out.println("claw web page: "+CurrentUrl.getUrl());
			String pageContent="";
		
					
			try {
				
				pageContent = UrlWordExtract.getText(CurrentUrl.getUrl());

			} catch (Exception e1) {
								// TODO Auto-generated catch block
				pageContent=CurrentUrl.getSummary();
			    System.out.println("claw web exception , use webpage summary!");
				//e1.printStackTrace();
			}
			pageContent = pageContent.replaceAll("&quot;", "\"").replaceAll("&nbsp;", "\"").replaceAll("&#39;", "\'").replaceAll("<b>", " ").replaceAll("</", " ").replaceAll(">", " ").replaceAll("b>", " ").replaceAll(";", " ").replaceAll("&gt", " ").replaceAll("&lt", " ").replaceAll("�", " ");

			pageContent=CommUtil.fudanSplitWords(pageContent);
			System.out.println(pageContent);
			allWebPages.get(currentTopicindex).getPages().get(j).setContent(pageContent);
//			
//			UrlContent += " \n"+tempString;	

			
			docSet.readDocs(pageContent);//读待采样数据	

			
		}
	    
		} catch (Exception e) {
			e.printStackTrace();
		}
*/		
			
		
//		System.out.println("all webpages: \n"+UrlContent);
		
		//System.out.println(UrlWordExtract.getText(Url2) );		
		System.out.println("wordMap size " + docSet.termToIndexMap.size());
		FileUtil.mkdir(new File(resultPath));//检测结果存放文件夹是否存在，存在继续，不存在创造一个
		LdaModel model = new LdaModel(ldaparameters);
		System.out.println("1 Initialize the model ...");
		model.initializeModel(docSet);
		System.out.println("2 Learning and Saving the model ...");
		boolean forFudan=true;
		try {
			model.inferenceModel(docSet,forFudan);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		System.out.println("3 Output the final model ...");
		//model.saveIteratedModel(ldaparameters.iteration, docSet);
	
		
		
		 List<FudanTopicWithWordsListBean> resultBeans=new ArrayList<FudanTopicWithWordsListBean>();		
		
		
		try {
			resultBeans= model.saveIteratedModelforFudanTopic(ldaparameters.iteration,docSet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			resultBeans=null;
			e.printStackTrace();
		}
		
		
		System.out.println("Done!");
		
		return resultBeans;
		
	}



}
