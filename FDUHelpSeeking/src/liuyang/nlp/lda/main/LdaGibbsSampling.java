package liuyang.nlp.lda.main;

import java.beans.beancontext.BeanContext;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.htmlparser.util.ParserException;

import cn.edu.fudan.se.helpseeking.util.CommUtil;
import cn.edu.fudan.se.helpseeking.util.FileHelper;
import cn.edu.fudan.se.helpseeking.util.Resource;
import liuyang.nlp.lda.com.FileUtil;
import liuyang.nlp.lda.conf.ConstantConfig;
import liuyang.nlp.lda.conf.PathConfig;
import liuyang.nlp.lda.conf.UrlConfig;
import liuyang.nlp.lda.main.Documents.Document;
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
			UrlContent += UrlWordExtract.getText(CurrentUrl);
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


	public static List<FudanTopicWithWordsListBean> fduTopicURLfilter(ArrayList<String> UrlList) throws IOException, ParserException {
		//write LdaParameters file
		
		
		
		// TODO Auto-generated method stub
		String originalDocsPath = CommUtil.getFDUPluginWorkingPath()+"/"+PathConfig.ldaDocsPath;
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
		for(String CurrentUrl: UrlList){
			System.out.println(CurrentUrl);
			UrlContent += " "+ UrlWordExtract.getText(CurrentUrl);
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
		boolean forFudan=true;
		model.inferenceModel(docSet,forFudan);	
		System.out.println("3 Output the final model ...");
		//model.saveIteratedModel(ldaparameters.iteration, docSet);
		
		System.out.println("Done!");
		return model.saveIteratedModelforFudanTopic(ldaparameters.iteration,docSet);
		
		
		
	}



}
