package cn.edu.fudan.se.helpseeking.preprocessing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import cn.edu.fudan.se.bean.TokenInFile;
import cn.edu.fudan.se.bean.ast.CLass;
import cn.edu.fudan.se.bean.ast.Method;
import cn.edu.fudan.se.dal.DBHelper;
import cn.edu.fudan.se.dal.TokenDAL;
import cn.edu.fudan.se.util.ASTUtil;
import cn.edu.fudan.se.util.CommUtil;
import cn.edu.fudan.se.util.FileHelper;
import cn.edu.fudan.se.util.INIHelper;
import cn.edu.fudan.se.util.MicroLog;
import cn.edu.fudan.se.util.WordNetDict;

public class TokenExtractor
{

	List<TokenInFile> tokenList;
	

	private boolean isAcceptReduplication = false;

	boolean isAcceptAlphabet = true;
	boolean isAcceptDigit = false;

	public void analysis(List<String> fileList, String projectName)
	{
		tokenList = new ArrayList<TokenInFile>();
		List<String> fileMapList = new ArrayList<String>();
		INIHelper iniHelper = new INIHelper(CommUtil.getCurrentProjectPath()
				+ "\\conf.ini");
		String fileMapName = iniHelper.getValue("IDENTIFIEREXTRACTOR",
				"fileMapName", "");
		fileMapName = CommUtil.getCurrentProjectPath() + "\\" + fileMapName;
		TokenExtractor tokenExtractor = new TokenExtractor();
		tokenExtractor.isAcceptAlphabet(true);
		tokenExtractor.isAcceptDigit(false);
		for (String fileName : fileList)
		{
			TokenInFile newTokens = new TokenInFile();
			newTokens.setFileName(fileName);
			newTokens.setProjectName(projectName);
			newTokens.setTokens(tokenExtractor
					.getIdentifierOccurenceOfDocument(fileName));
			newTokens.setContent(FileHelper.getContent(fileName));
			tokenList.add(newTokens);
			fileMapList.add(fileName);

		}
		writeToFileMap(fileMapName, fileMapList);
	}


	// add function for only keep noun and verb words 2011-11-28

	public void processTokenOnlyNounsVerbs()
	{

		for (TokenInFile currentTokenInFile : tokenList)
		{
			List<String> tokens = currentTokenInFile.getTokens();
			List<String> newTokens = new ArrayList<>();
			String word;
			for (int i = 0; i < tokens.size(); i++)
			{
				word = tokens.get(i);
				String tempString = WordNetDict.getInstance().getNounsOrVerbs(
						word);
				if (tempString != null)
				{
					newTokens.add(tempString);
				}
			}
			currentTokenInFile.setTokens(newTokens);
		}

	}
	    
	public void addClassAndMethodIdentification(List<CLass> classList)
	{
		for (TokenInFile curTokenInFile : tokenList)
		{
			CLass oClass = ASTUtil.getClassByFileName(curTokenInFile.getFileName(), classList);
			if(oClass == null)
			{
				MicroLog.log(Level.INFO, "can't find the class by className " + curTokenInFile.getFileName());
				continue;
			}
			List <String> tokens= curTokenInFile.getTokens();
			List<Method> methodsList= oClass.getMethods();
			int countOfMethod=methodsList.size();
			
			for (int i=0; i< countOfMethod;i++)
			{
				tokens.add(methodsList.get(i).getName().toString());
				tokens.add(oClass.getName().toString());		
			}		
			
		}
	}

	
	
	public List<String> getTokensOfString(String tokenString)
	{
		List<String> content = new ArrayList<String>();
		FudanIdentifierNameTokeniserFactory factory = new FudanIdentifierNameTokeniserFactory();
		FudanIdentifierNameTokeniser tokeniser = new FudanIdentifierNameTokeniser(
				factory.create());
		tokeniser.setMinTokenLength(2);
		List<String> tokens;

		tokens = tokeniser.tokeniseOnly(tokenString);
		for (String token : tokens)
		{
			if (isStringAccepted(content, token))
				content.add(token);
		}

		return content;
	}

	
	public List<String> getIdentifierOccurenceOfString(String tokenString)
	{
		List<String> content = new ArrayList<String>();
		FudanIdentifierNameTokeniserFactory factory = new FudanIdentifierNameTokeniserFactory();
		FudanIdentifierNameTokeniser tokeniser = new FudanIdentifierNameTokeniser(
				factory.create());
		tokeniser.setMinTokenLength(2);
		List<String> tokens;

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

	public List<TokenInFile> getTokens()
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


	public void setTokens(List<TokenInFile> tokenList)
	{
		this.tokenList = tokenList;
	}

	public void writeToFile(List<TokenInFile> list, String fileName)
	{
		FileHelper.createFile(fileName);
		PrintWriter outputStream = null;
		try
		{
			outputStream = new PrintWriter(fileName);
			outputStream.println(tokenList.size());
			for (TokenInFile tokens : tokenList)
			{
				outputStream.println(CommUtil.ListToString(tokens.getTokens()));
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

	// TODO lihongwei add
	private void writeToFileMap(String fileMapName, List<String> fileMapList)
	{
		FileHelper.createFile(fileMapName);
		PrintWriter outputStream = null;
		try
		{
			outputStream = new PrintWriter(fileMapName);
			for (String tempString : fileMapList)
			{
				outputStream.println(tempString);

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

}
