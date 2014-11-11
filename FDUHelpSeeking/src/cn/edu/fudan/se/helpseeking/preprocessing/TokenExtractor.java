package cn.edu.fudan.se.helpseeking.preprocessing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.NotActiveException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import cn.edu.fudan.se.helpseeking.util.FileHelper;
import cn.edu.fudan.se.helpseeking.util.WordNetDict;


public class TokenExtractor
{

	List<String> tokenList=new ArrayList<String>();
	

	private boolean isAcceptReduplication = false;

	boolean isAcceptAlphabet = true;
	boolean isAcceptDigit = false;
	boolean nousestopwords=false;

	
	
	
	
	public boolean isNousestopwords() {
		return nousestopwords;
	}

	public void setNousestopwords(boolean nousestopwords) {
		this.nousestopwords = nousestopwords;
	}

	//给出一个串分析得到字符
	public List<String>  analysis(String tokenString)
	{
		List<String> tokens=new ArrayList<String>();
		TokenExtractor tokenExtractor = new TokenExtractor();
		tokenExtractor.isAcceptAlphabet(true);
		tokenExtractor.isAcceptDigit(false);
		tokens=tokenExtractor.getIdentifierOccurenceOfString(tokenString);		
		setTokens(tokens);
		return  tokens;
		
	}

	//给出一个串分析得到字符
		public List<String>  analysis(String tokenString, boolean notusestopwords)
		{
			List<String> tokens=new ArrayList<String>();
			TokenExtractor tokenExtractor = new TokenExtractor();
			tokenExtractor.isAcceptAlphabet(true);
			tokenExtractor.isAcceptDigit(false);
			tokenExtractor.setNousestopwords(notusestopwords);
			tokens=tokenExtractor.getIdentifierOccurenceOfString(tokenString);		
			setTokens(tokens);
			return  tokens;
			
		}
	

	public  List <String> processTokenOnlyNounsVerbs()
	{
	    List <String> newTokens=new ArrayList<String>();

		for (String currentString : tokenList)
		{
			String tempString = WordNetDict.getInstance().getNounsOrVerbs(currentString);
				if (tempString != null)
				{
					newTokens.add(tempString);
				}
		}

		return newTokens;
	}
	    
	
	public List<String> getIdentifierOccurenceOfString(String tokenString)
	{
		List<String> content = new ArrayList<String>();
		FudanIdentifierNameTokeniserFactory factory = new FudanIdentifierNameTokeniserFactory();
		FudanIdentifierNameTokeniser tokeniser = new FudanIdentifierNameTokeniser(
				factory.create());
		tokeniser.setMinTokenLength(2);
		
		List<String> tokens;

		if (nousestopwords) {
			tokens = tokeniser.tokeniseOnly(tokenString);

		}else
		tokens = tokeniser.tokenise(tokenString);
		
		
		for (String token : tokens)
		{
			if (isStringAccepted(content, token))
				content.add(token);
		}

		return content;
	}

	public List<String> getIdentifierOccurenceOfDocument(String fileName)
	{
		List<String> content = new ArrayList<String>();
		FudanIdentifierNameTokeniserFactory factory = new FudanIdentifierNameTokeniserFactory();
		FudanIdentifierNameTokeniser tokeniser = new FudanIdentifierNameTokeniser(
				factory.create());
		tokeniser.setMinTokenLength(2);
		try
		{
			BufferedReader inputFile = new BufferedReader(new FileReader(
					fileName));
			String line;
			List<String> tokens;
			while (true)
			{
				line = inputFile.readLine();
				if (line == null)
				{
					break;
				}
				if (isAcceptAlphabet) {
					
				}
				
				if (nousestopwords) {
					tokens = tokeniser.tokeniseOnly(line);

				}else
				tokens = tokeniser.tokenise(line);

				
				for (String token : tokens)
				{
					if (isStringAccepted(content, token)) content.add(token);
				}
			}
			inputFile.close();
		}
		catch (IOException ioEx)
		{
			System.err.println(ioEx.getMessage());
		}
		return content;
	}

	
	
	public List<String> getTokens()
	{
		return tokenList;
	}

	public void isAcceptAlphabet(boolean isAcceptAlphabet)
	{
		this.isAcceptAlphabet = isAcceptAlphabet;
	}

	public void isAcceptDigit(boolean isAcceptDigit)
	{
		this.isAcceptDigit = isAcceptDigit;
	}

	public void isAcceptReduplication(boolean isAcceptReduplication)
	{
		this.isAcceptReduplication = isAcceptReduplication;
	}

	private boolean isStringAccepted(List<String> content, String str)
	{
		str = str.trim();
		boolean accpeted = false;
		if (isAcceptAlphabet) return str.charAt(0) >= 'a'
				&& str.charAt(0) <= 'z' || str.charAt(0) >= 'A'
				&& str.charAt(0) <= 'Z';
		if (isAcceptDigit) return str.charAt(0) >= '0' && str.charAt(0) <= '9';

		if (isAcceptReduplication == false) { return content.indexOf(str) == -1; }
		return accpeted;
	}


	public void setTokens(List<String> tokenList)
	{
		this.tokenList = tokenList;
	}

	public void writeToFile(List<String> list, String fileName)
	{
		FileHelper.createFile(fileName);
		PrintWriter outputStream = null;
		try
		{
			outputStream = new PrintWriter(fileName);
			outputStream.println(tokenList.size());
			for (String tokens : tokenList)
			{
				outputStream.println(tokens);
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		finally
		{
			outputStream.close();
		}
	}

	public void writeToFile(String fileName)
	{
		writeToFile(tokenList, fileName);
	}

	
	public static void main(String[] args) {
		TokenExtractor mytoExtractor=new TokenExtractor();
		List<String>mystr=mytoExtractor.getIdentifierOccurenceOfString("import;IOException;TasteException");
		for (int i = 0; i < mystr.size();i++)
		{
			System.out.println(mystr.get(i));
		}
	}

}
