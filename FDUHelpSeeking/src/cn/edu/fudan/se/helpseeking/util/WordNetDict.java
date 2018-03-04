package cn.edu.fudan.se.helpseeking.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.morph.WordnetStemmer;

/*
 * use wordnet dictionary for find semantics words  and stem words
 * modified by grand - lihongwei 2011-11-3
 * the original files from ztm 
 *  
 */




public class WordNetDict 
{
	private IDictionary wordNetDict = null;
	private static WordNetDict instance = null;

	private WordNetDict()
	{
		initial();
		wordNetDict.open();
	}
	
	
public static void main(String[] args) {
		
		String tempString = WordNetDict.getInstance().getNounsOrVerbs("horse");
		
		System.out.println("the horse string synomset is : "+ tempString);
}
	
	
	public static WordNetDict getInstance()
	{
		if (instance == null) instance = new WordNetDict();
		return instance;
	}

	public void closeDictionary()
	{
		wordNetDict.close();
	}

	public void initial()
	{

		String wnhome = System.getenv("WNHOME");
		String path = wnhome + File.separator + "dict";
		URL url = null;
		try
		{
			url = new URL("file", null, path);
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		wordNetDict = new Dictionary(url);

	}

	//get a word just as nouns or verbs   add by 2011-11-28
	
	public String getNounsOrVerbs(String word)
	{
		String resultword=null;
		IIndexWord idxWord = wordNetDict.getIndexWord(word, POS.NOUN);
		if (idxWord == null)
		{
			idxWord = wordNetDict.getIndexWord(word, POS.VERB);
		}
		if (idxWord != null)
		{
			IWordID wordID = (IWordID) idxWord.getWordIDs().get(0);
			IWord iWord = wordNetDict.getWord(wordID);
		    resultword=iWord.getLemma().toString();
		 //   System.out.println("the word is : <"+ word + "> and it's nouns or verbs are : " + resultword);
		}
		return resultword;
	}
	
	// get a stem of a word 
	
	public String getStem(String word)
	{
		String resultword;
		//wordNetDict.open();
		WordnetStemmer wnStemmer = new WordnetStemmer(wordNetDict);

		if (wnStemmer.findStems(word).size() > 0) 
		{		
			resultword= wnStemmer.findStems(word).get(0);
			System.out.println("This is : "+word +"  stem=>"+ resultword );
			return resultword;
		}
		return null;

	}

	// find related words of synset
	public void getRelatedWords(boolean isDeep, ArrayList<String> words,
			HashSet<String> syntaxRelatedWords,
			HashSet<String> semanticRelatedWords)
	{

		for (String word : words)
		{
			IIndexWord idxWord = wordNetDict.getIndexWord(word, POS.NOUN);
			if (idxWord == null)
			{
				idxWord = wordNetDict.getIndexWord(word, POS.VERB);
				if (idxWord == null)
				{
					idxWord = wordNetDict.getIndexWord(word, POS.ADJECTIVE);
					if (idxWord == null)
					{
						idxWord = wordNetDict.getIndexWord(word, POS.ADVERB);
					}
				}
			}
			if (idxWord != null)
			{
				IWordID wordID = (IWordID) idxWord.getWordIDs().get(0);
				IWord iWord = wordNetDict.getWord(wordID);

				List<IWordID> wl = iWord.getRelatedWords();
				for (IWordID iw : wl)
				{
					IWord wd = wordNetDict.getWord(iw);
					syntaxRelatedWords.add(wd.getLemma());
				}

				for (IWord iw : iWord.getSynset().getWords())
				{
					semanticRelatedWords.add(iw.getLemma());
				}

				if (isDeep)
				{
					List<ISynsetID> ssl = iWord.getSynset().getRelatedSynsets();
					for (ISynsetID ssid : ssl)
					{
						ISynset ss = wordNetDict.getSynset(ssid);
						for (IWord iw : ss.getWords())
						{
							semanticRelatedWords.add(iw.getLemma());
						}
					}
				}

			}

		}
	}
	
	

}
