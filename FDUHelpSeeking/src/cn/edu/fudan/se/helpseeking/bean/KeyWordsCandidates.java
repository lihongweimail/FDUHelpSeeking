package cn.edu.fudan.se.helpseeking.bean;

import java.security.cert.CertPathValidatorException.BasicReason;
import java.util.ArrayList;
import java.util.List;

public class KeyWordsCandidates {
	List<KeyWord> keyWords=new ArrayList<>();
	
	Basic.KeyWordsLevel level=Basic.KeyWordsLevel.Other;  
/**
 *    level 记录这组词汇的 权重级别： 共分 7级 
 *    1级是console view即时输出   基本权重：第一次出现  其中exception名字词 w1=8  w2=3  普通词 w1=7  w2=2  (原来的处理是第二次后  这组值为： exception w1=6   w2=1  普通词为 w1=6  w2=1)  
 *    2级 是problem view输出    基本权重：第一次出现  Error词 w1=7  w2=2  warning  w1=6  w2=1  (原来的处理是第二次后  这组值为： exception w1=6   w2=1  普通词为 w1=6  w2=1) 
 *    3级 是class model 记录的API   基本权重：  其中exception名字词 w1=6  w2=2  普通词 w1=6  w2=1
 *    4级  是 console 历史  老策略：：   基本权重：其中exception名字词 w1=5  w2=2  普通词 w1=5  w2=1  （新方法打算不使用它们）
 *    5级  是 problem 历史  老策略：：   基本权重：ERROR: 其中exception名字词 w1=4  w2=2  普通词 w1=4  w2=1 Warning: 其中exception名字词 w1=2  w2=2  普通词 w1=1  w2=1  （新方法打算不使用它们）
 *    6级 是code (debug or edit)  基本权重：  其中exception名字词 w1=2  w2=2  普通词 w1=2  w2=1
 *    7级是explorer相关 在 project explorer，或者在 editor 的tab上的动作： 基本权重：   w1=1  w2=1
 *    
 *     */
	

	int index=0;//the words list index 
	
	public int getIndex() {
	return index;
}

public void setIndex(int index) {
	this.index = index;
}

	int actionID=0;//记录这组词汇的来源， 用作移除的动作时，移除对应的关键词
	
	int frequency=0;//该动作所属类别的频率值
	
	int oldStep=0;  //老化值
	
	int distance=0;//距离值 （0开始） 一直老化下去
	
	/**
	 * 老化策略：
	 * 
	 * 高级别老化结束后，就开始对权重值减其score的10%； 直到小于8
	 * level=1   oldstep=3
	 * level=2   oldstep=5
	 * level=3   oldstep=8
	 * level=4   oldstep=12
	 * 
	 * 低级别老化结束后，就开始对权重值score开始增加10% 直到大于8
	 * level=7   oldstep=3
	 * level=6   oldstep=5
	 * level=5   oldstep=8
	 * 
	 * 所有变化达到中值 8 后，如果还调整 小于8则增加10%，大于8 则减少10%
	 * 
	 * 对窗口内的动作相同的信息加权
	 *    加上   频率值*3%
	 * 
	 * 
	 * 另外，依据距离最新动作的距离，即 在动作窗口中 list的索引  index值的差用作距离
	 * 
	 *     减去  距离*1%
	 * 
	 * 
	 * 
	 */
	
	
	

	public List<KeyWord> getKeyWords() {
		return keyWords;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public void setKeyWords(List<KeyWord> keyWords) {
		this.keyWords = keyWords;
	}



	public Basic.KeyWordsLevel getLevel() {
		return level;
	}

	public void setLevel(Basic.KeyWordsLevel level) {
		this.level = level;
	}

	public int getActionID() {
		return actionID;
	}

	public void setActionID(int actionID) {
		this.actionID = actionID;
	}

	public int getOldStep() {
		return oldStep;
	}

	public void setOldStep(int oldStep) {
		this.oldStep = oldStep;
	}
	

	
	

}
