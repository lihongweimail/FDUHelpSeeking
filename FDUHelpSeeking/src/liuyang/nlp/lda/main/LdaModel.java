package liuyang.nlp.lda.main;

/**Class for Lda model
 * @author yangliu
 * @blog http://blog.csdn.net/yangliuy
 * @mail yangliuyx@gmail.com
 */
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.edu.fudan.se.helpseeking.bean.Basic;
import cn.edu.fudan.se.helpseeking.bean.FudanTopicWithWordsListBean;
import cn.edu.fudan.se.helpseeking.bean.FudanTopicWordsBean;
import cn.edu.fudan.se.helpseeking.util.CommUtil;
import liuyang.nlp.lda.com.FileUtil;
import liuyang.nlp.lda.conf.ConstantConfig;
import liuyang.nlp.lda.conf.PathConfig;

public class LdaModel {
	
	int [][] doc;//word index array
	int V, K, M;//vocabulary size, topic number, document number
	int [][] z;//topic label array
	float alpha; //doc-topic dirichlet prior parameter 
	float beta; //topic-word dirichlet prior parameter
	int [][] nmk;//given document m, count times of topic k. M*K
	int [][] nkt;//given topic k, count times of term t. K*V
	int [] nmkSum;//Sum for each row in nmk
	int [] nktSum;//Sum for each row in nkt
	double [][] phi;//Parameters for topic-word distribution K*V
	double [][] theta;//Parameters for doc-topic distribution M*K
	int iterations;//Times of iterations
	int saveStep;//The number of iterations between two saving
	int beginSaveIters;//Begin save model at this iteration
	
	public LdaModel(LdaGibbsSampling.modelparameters modelparam) {
		// TODO Auto-generated constructor stub
		alpha = modelparam.alpha;
		beta = modelparam.beta;
		iterations = modelparam.iteration;
		K = modelparam.topicNum;
		saveStep = modelparam.saveStep;
		beginSaveIters = modelparam.beginSaveIters;
	}

	public void initializeModel(Documents docSet) {
		// TODO Auto-generated method stub
		M = docSet.docs.size();
		V = docSet.termToIndexMap.size();
		nmk = new int [M][K];
		nkt = new int[K][V];
		nmkSum = new int[M];
		nktSum = new int[K];
		phi = new double[K][V];
		theta = new double[M][K];
		
		//initialize documents index array
		doc = new int[M][];
		for(int m = 0; m < M; m++){
			//Notice the limit of memory
			int N = docSet.docs.get(m).docWords.length;
			doc[m] = new int[N];
			for(int n = 0; n < N; n++){
				doc[m][n] = docSet.docs.get(m).docWords[n];
			}
		}
		
		//initialize topic lable z for each word
		z = new int[M][];
		for(int m = 0; m < M; m++){
			int N = docSet.docs.get(m).docWords.length;
			z[m] = new int[N];
			for(int n = 0; n < N; n++){
				int initTopic = (int)(Math.random() * K);// From 0 to K - 1
				z[m][n] = initTopic;
				//number of words in doc m assigned to topic initTopic add 1
				nmk[m][initTopic]++;
				//number of terms doc[m][n] assigned to topic initTopic add 1
				nkt[initTopic][doc[m][n]]++;
				// total number of words assigned to topic initTopic add 1
				nktSum[initTopic]++;
			}
			 // total number of words in document m is N
			nmkSum[m] = N;
		}
	}

	
	//用于特定目的改造，指定存储路径  forFudan ==true 为定制； forFudan==false 为原始程序
	public void inferenceModel(Documents docSet ,Boolean forFudan) throws IOException {
		// TODO Auto-generated method stub
		if(iterations < saveStep + beginSaveIters){
			System.err.println("Error: the number of iterations should be larger than " + (saveStep + beginSaveIters));
			System.exit(0);
		}
		for(int i = 0; i < iterations; i++){
			System.out.println("Iteration " + i);
			if((i >= beginSaveIters) && (((i - beginSaveIters) % saveStep) == 0)){
				//Saving the model
				System.out.println("Saving model at iteration " + i +" ... ");
				//Firstly update parameters
				updateEstimatedParameters();
				//Secondly print model variables
				
				//customize for fudan helpseeking 
				if (forFudan) {
					saveIteratedModelforFudanTopic(i,docSet);
				}else {
					saveIteratedModel(i, docSet);

				}
				
								
			}
			
			//Use Gibbs Sampling to update z[][]
			for(int m = 0; m < M; m++){
				int N = docSet.docs.get(m).docWords.length;
				for(int n = 0; n < N; n++){
					// Sample from p(z_i|z_-i, w)
					int newTopic = sampleTopicZ(m, n);
					z[m][n] = newTopic;
				}
			}
		}
	}
	
	private void updateEstimatedParameters() {
		// TODO Auto-generated method stub
		for(int k = 0; k < K; k++){
			for(int t = 0; t < V; t++){
				phi[k][t] = (nkt[k][t] + beta) / (nktSum[k] + V * beta);
			}
		}
		
		for(int m = 0; m < M; m++){
			for(int k = 0; k < K; k++){
				theta[m][k] = (nmk[m][k] + alpha) / (nmkSum[m] + K * alpha);
			}
		}
	}

	private int sampleTopicZ(int m, int n) {
		// TODO Auto-generated method stub
		// Sample from p(z_i|z_-i, w) using Gibbs upde rule
		
		//Remove topic label for w_{m,n}
		int oldTopic = z[m][n];
		nmk[m][oldTopic]--;
		nkt[oldTopic][doc[m][n]]--;
		nmkSum[m]--;
		nktSum[oldTopic]--;
		
		//Compute p(z_i = k|z_-i, w)
		double [] p = new double[K];
		for(int k = 0; k < K; k++){
			p[k] = (nkt[k][doc[m][n]] + beta) / (nktSum[k] + V * beta) * (nmk[m][k] + alpha) / (nmkSum[m] + K * alpha);
		}
		
		//Sample a new topic label for w_{m, n} like roulette
		//Compute cumulated probability for p
		for(int k = 1; k < K; k++){
			p[k] += p[k - 1];
		}
		double u = Math.random() * p[K - 1]; //p[] is unnormalised
		int newTopic;
		for(newTopic = 0; newTopic < K; newTopic++){
			if(u < p[newTopic]){
				break;
			}
		}
		
		//Add new topic label for w_{m, n}
		nmk[m][newTopic]++;
		nkt[newTopic][doc[m][n]]++;
		nmkSum[m]++;
		nktSum[newTopic]++;
		return newTopic;
	}

	public void saveIteratedModel(int iters, Documents docSet) throws IOException {
		// TODO Auto-generated method stub
		//lda.params lda.phi lda.theta lda.tassign lda.twords
		//lda.params
		String resPath = PathConfig.LdaResultsPath;
		String modelName = "lda_" + iters;//迭代次数每次传进来，用作保存时对文件的命名
		ArrayList<String> lines = new ArrayList<String>();
		lines.add("alpha = " + alpha);
		lines.add("beta = " + beta);
		lines.add("topicNum = " + K);
		lines.add("docNum = " + M);
		lines.add("termNum = " + V);
		lines.add("iterations = " + iterations);
		lines.add("saveStep = " + saveStep);
		lines.add("beginSaveIters = " + beginSaveIters);
		FileUtil.writeLines(resPath + modelName + ".params", lines);
		
		//lda.phi K*V
		BufferedWriter writer = new BufferedWriter(new FileWriter(resPath + modelName + ".phi"));		
		for (int i = 0; i < K; i++){
			for (int j = 0; j < V; j++){
				writer.write(phi[i][j] + "\t");
			}
			writer.write("\n");
		}
		writer.close();
		
		//lda.theta M*K
		writer = new BufferedWriter(new FileWriter(resPath + modelName + ".theta"));
		for(int i = 0; i < M; i++){
			for(int j = 0; j < K; j++){
				writer.write(theta[i][j] + "\t");
			}
			writer.write("\n");
		}
		writer.close();
		
		//lda.tassign
		writer = new BufferedWriter(new FileWriter(resPath + modelName + ".tassign"));
		for(int m = 0; m < M; m++){
			for(int n = 0; n < doc[m].length; n++){
				writer.write(doc[m][n] + ":" + z[m][n] + "\t");
			}
			writer.write("\n");
		}
		writer.close();
		
		//lda.twords phi[][] K*V   最后提取出来的关键字
		writer = new BufferedWriter(new FileWriter(resPath + modelName + ".twords"));
		int topNum = 20; //Find the top 20 topic words in each topic
		ArrayList<String> LdaOutcome = new ArrayList<String>();
		for(int i = 0; i < K; i++){
			List<Integer> tWordsIndexArray = new ArrayList<Integer>(); 
			for(int j = 0; j < V; j++){
				tWordsIndexArray.add(new Integer(j));
			}
			Collections.sort(tWordsIndexArray, new LdaModel.TwordsComparable(phi[i]));
			writer.write("topic " + i + "\t:\t");
			for(int t = 0; t < topNum; t++){
				System.out.println(docSet.indexToTermMap.get(tWordsIndexArray.get(t)));
				writer.write(docSet.indexToTermMap.get(tWordsIndexArray.get(t)) + " " + phi[i][tWordsIndexArray.get(t)] + "\t");
				//????  在这里将topic的词汇提出来  但是topic叫什么名字呢？取前两个前三个？例如前5五个标题，所有20个词汇作为下一组
				
				//LdaOutcome.add(word);
			}
			writer.write("\n");
		}
		writer.close();
	}

	
	
	public List<FudanTopicWithWordsListBean> saveIteratedModelforFudanTopic( int iters, Documents docSet) throws IOException {
		// TODO Auto-generated method stub
		//lda.params lda.phi lda.theta lda.tassign lda.twords
		//lda.params
		String resPath = CommUtil.getFDUPluginWorkingPath()+"/"+PathConfig.LdaResultsPath;
		
		
		String modelName = "lda_" + iters;//迭代次数每次传进来，用作保存时对文件的命名
		ArrayList<String> lines = new ArrayList<String>();
		lines.add("alpha = " + alpha);
		lines.add("beta = " + beta);
		lines.add("topicNum = " + K);
		lines.add("docNum = " + M);
		lines.add("termNum = " + V);
		lines.add("iterations = " + iterations);
		lines.add("saveStep = " + saveStep);
		lines.add("beginSaveIters = " + beginSaveIters);
		FileUtil.writeLines(resPath + modelName + ".params", lines);
		
		//lda.phi K*V
		BufferedWriter writer = new BufferedWriter(new FileWriter(resPath + modelName + ".phi"));		
		for (int i = 0; i < K; i++){
			for (int j = 0; j < V; j++){
				writer.write(phi[i][j] + "\t");
			}
			writer.write("\n");
		}
		writer.close();
		
		//lda.theta M*K
		writer = new BufferedWriter(new FileWriter(resPath + modelName + ".theta"));
		for(int i = 0; i < M; i++){
			for(int j = 0; j < K; j++){
				writer.write(theta[i][j] + "\t");
			}
			writer.write("\n");
		}
		writer.close();
		
		//lda.tassign
		writer = new BufferedWriter(new FileWriter(resPath + modelName + ".tassign"));
		for(int m = 0; m < M; m++){
			for(int n = 0; n < doc[m].length; n++){
				writer.write(doc[m][n] + ":" + z[m][n] + "\t");
			}
			writer.write("\n");
		}
		writer.close();
		
	
		//????
		//为FDU helpseeking 输出数据：
		List<FudanTopicWithWordsListBean> fudanTopicWithWordsList=new ArrayList<FudanTopicWithWordsListBean>();
		//lda.twords phi[][] K*V   最后提取出来的关键字
		writer = new BufferedWriter(new FileWriter(resPath + modelName + ".twords"));
		int topNum = Basic.FOAMTREETOPICWORDSLIMITIED < docSet.indexToTermMap.size()? Basic.FOAMTREETOPICWORDSLIMITIED: docSet.indexToTermMap.size(); //从一个topic中只取部分词；Find the top 20 topic words in each topic
		
		
		ArrayList<String> LdaOutcome = new ArrayList<String>();
		int getTopicNumber=Basic.FOAMTREETOPICCOUNT < K ? Basic.FOAMTREETOPICCOUNT: K;  //只取部分topic 使用
		for(int i = 0; i < getTopicNumber; i++){
			List<Integer> tWordsIndexArray = new ArrayList<Integer>(); 
			for(int j = 0; j < V; j++){
				tWordsIndexArray.add(new Integer(j));
			}
			Collections.sort(tWordsIndexArray, new LdaModel.TwordsComparable(phi[i]));
			writer.write("topic " + i + "\t:\t");
			
			
			
			//收集数据到 bean 中 便于处理
			FudanTopicWithWordsListBean ftwwlb=new FudanTopicWithWordsListBean();
			ftwwlb.setTopicNO(i);
			System.out.println("topic No."+ i);
			int selectTopicWordCount=Basic.SELECTTOPICWORDNUMBER;
			String wordsasTopicTitle="";
			
			for(int t = 0; t < topNum; t++){
				System.out.println(docSet.indexToTermMap.get(tWordsIndexArray.get(t)));
				writer.write(docSet.indexToTermMap.get(tWordsIndexArray.get(t)) + " " + phi[i][tWordsIndexArray.get(t)] + "\t");
				//????  在这里将topic的词汇提出来  但是topic叫什么名字呢？取前两个前三个？例如前5五个标题，所有20个词汇作为下一组

                //如果不是特殊的串或者空串
				
				String isnullstr=docSet.indexToTermMap.get(tWordsIndexArray.get(t)).trim();
				
				
				if (!isnullstr.equals("")) {
					String simpString=CommUtil.getSimpleWords(isnullstr);
					if (!simpString.equals("")) {
						
				//一个词以及词权 并收集到 一个topic中去
				FudanTopicWordsBean ftwbBean=new FudanTopicWordsBean();
				ftwbBean.setWordName(docSet.indexToTermMap.get(tWordsIndexArray.get(t)));
				ftwbBean.setWordWeight(phi[i][tWordsIndexArray.get(t)]);
				
				
				
				if (selectTopicWordCount>0) {
					wordsasTopicTitle=wordsasTopicTitle+" "+docSet.indexToTermMap.get(tWordsIndexArray.get(t));
					selectTopicWordCount=selectTopicWordCount-1;
				}

				
				
				ftwwlb.getWordsList().add(ftwbBean);
				}
				
			}
				//LdaOutcome.add(word);
			}
			writer.write("\n");
		//收集一个topic到topic list中
			ftwwlb.setTopicName(wordsasTopicTitle);
			fudanTopicWithWordsList.add(ftwwlb);
			
		}
		
		
		
		
		writer.close();
		
		return fudanTopicWithWordsList;
		
		//????
		
	}

	public class TwordsComparable implements Comparator<Integer> {  //比较topic中的关键字
		
		public double [] sortProb; // Store probability of each word in topic k
		
		public TwordsComparable (double[] sortProb){
			this.sortProb = sortProb;
		}

		@Override
		public int compare(Integer o1, Integer o2) {
			// TODO Auto-generated method stub
			//Sort topic word index according to the probability of each word in topic k
			if(sortProb[o1] > sortProb[o2]) return -1;
			else if(sortProb[o1] < sortProb[o2]) return 1;
			else return 0;
		}
	}
}
