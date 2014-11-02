package cn.edu.fudan.se.helpseeking.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import liuyang.nlp.lda.main.LdaGibbsSampling;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import cn.edu.fudan.se.helpseeking.FDUHelpSeekingPlugin;
import cn.edu.fudan.se.helpseeking.bean.Basic;
import cn.edu.fudan.se.helpseeking.bean.CxKeyPair;
import cn.edu.fudan.se.helpseeking.preprocessing.TokenExtractor;

public class CommUtil {
	private static final String SPLITE_STRING = "[; ]";

	public static boolean stringNullOrZero(String str) {
		return str == null || str.trim().length() == 0;
	}

	public static int randomInt(int scope, int base) {
		// 为获得 10<= temp <= 15 则为： scope 5 base 10
		int Temp = (int) Math.round(Math.random() * scope + base);
		return Temp;
	}
	
	public static String getTokensfromCodeStr(String name) {
		String tempstr;
		TokenExtractor mytoExtractor=new TokenExtractor();
		List<String> mystrlist=mytoExtractor.analysis(name);
		tempstr="";
		for (int j = 0; j < mystrlist.size(); j++) {
			if (tempstr.equals("")) {
				tempstr=mystrlist.get(j).toString();
			}else {
				tempstr=tempstr+" "+mystrlist.get(j).toString();
			}

		}
		return tempstr;
	}
	
	
	//给定一个字串，特别但是包结构带类名，或者是方法的全名带参数时，为了再foamtree上显示更明显，预处理一下，去除一些特殊字符，得到分词结果
	public static String getSimpleWords(String tempstr) {
		String resultstr="";
		tempstr=tempstr.replace(';', ' ');
    	if (tempstr.contains("(") && tempstr.contains(")")) {
						
			int firstpartlastIndex=tempstr.indexOf('(');
			String firstPart=tempstr.substring(0, firstpartlastIndex);
			System.out.println("firstpart: "+firstPart);
			List<String> namePart=CommUtil.stringToList(firstPart, "[.]");
			String name=namePart.get(namePart.size()-1);
			
			name = getTokensfromCodeStr(name);
						
			
			int lastpartIndex=tempstr.lastIndexOf(')');
			String secondPart=tempstr.substring(firstpartlastIndex+1,lastpartIndex);
			System.out.println("secondpart: "+ secondPart);
			List<String> secondkeywordparts=new ArrayList<String>();
			secondkeywordparts=CommUtil.stringToList(secondPart, "[,]");
			
			String parameterList="";
			
			for (int i = 0; i < secondkeywordparts.size(); i++) {
				if (secondkeywordparts.get(i).trim().equals("I")  ) {
					continue;
				}

				if (secondkeywordparts.get(i).trim().equals("Z")  ) {
					continue;
				}

				
				if (secondkeywordparts.get(i).trim().contains(" ")) {
					List<String> para=new ArrayList<String>();
					para=CommUtil.stringToList(secondkeywordparts.get(i), "[ ]");
					
					if (parameterList.equals("")) {
						parameterList=getTokensfromCodeStr(para.get(0));
					}else
					{parameterList=parameterList+" , "+getTokensfromCodeStr(para.get(0));}
					
					
				}
				else
				{
				List<String> para=new ArrayList<String>();
				para=CommUtil.stringToList(secondkeywordparts.get(i), "[.]");

				if (parameterList.equals("")) {
					parameterList=getTokensfromCodeStr(para.get(para.size()-1));
				}else
				{
					parameterList=parameterList+" , "+getTokensfromCodeStr(para.get(para.size()-1));}
				}
			}
			
			if (parameterList.equals("")) {
				resultstr=name;
			}else
			resultstr=name +" ("+parameterList+")";
 
			}
			else 
			{
				List<String> packageClassName=new ArrayList<String>();
				packageClassName=CommUtil.stringToList(tempstr, "[.]");
					resultstr=getTokensfromCodeStr(packageClassName.get(packageClassName.size()-1));
				
				
			}
		
     //   System.out.println("last formal: result" + resultstr);
        return resultstr;
	}

	private static int minLength = 2;

	public static List<String> removeDuplicateWithOrder(List<String> list) {
		Collections.sort(list);
		List<String> listTemp = new ArrayList<String>();
		Iterator<String> it = list.iterator();
		while (it.hasNext()) {
			String a = it.next();
			if (listTemp.contains(a)) {
				it.remove();
			} else {
				listTemp.add(a);
			}
		}
		return listTemp;
	}

	public static String removeDuplicateWords(List<String> list) {
		Collections.sort(list);
		List<String> listTemp = new ArrayList<String>();
		Iterator<String> it = list.iterator();
		while (it.hasNext()) {
			String a = it.next();
			if (listTemp.contains(a)) {
				it.remove();
			} else {
				listTemp.add(a);
			}
		}
		String result = "";
		for (int i = 0; i < listTemp.size(); i++) {
			result = result + listTemp.get(i) + ";";
		}

		return result;
	}

	public static List<String> removeStopWordsFromList(List<String> tokens) {
		List<String> result = new ArrayList<String>();
		List<String> keyWords = constructDefaultFilterString("StopResource",
				"javaStopList.txt", "userStopList.txt");

		for (String token : tokens) {

			if (token.length() >= minLength && (int) token.charAt(0) < 255) {
				String lowercasetoken =token.toLowerCase();
				boolean flage = false;
				for (String keyword : keyWords) {
					if (lowercasetoken.trim().equals(keyword.trim().toLowerCase())) {
						// System.out.println("not add keyword:"+ token);
						flage = true;
						break;
					}
				}

				if (!flage) {
					result.add(token);
				}

			}
		}

		return result;

	}

	public static List<String> removeStopWordsAsList(String tokens) {
		List<String> result = new ArrayList<String>();
		List<String> keyWords = constructDefaultFilterString("StopResource",
				"javaStopList.txt", "userStopList.txt");
		String SPLIT_STRING = "[&#$_.(){}!*%+-=><\\:;,?/\"\'\t\b\r\n\0 ]";

		for (String token : tokens.split(SPLIT_STRING)) {

			if (token.length() >= minLength && (int) token.charAt(0) < 255) {
				String lowercasetoken;
				lowercasetoken = token.toLowerCase();
				boolean flage = false;
				for (String keyword : keyWords) {
					if (lowercasetoken.trim().equals(keyword.trim().toLowerCase())) {
						// System.out.println("not add keyword:"+ token);
						flage = true;
						break;
					}
				}

				if (!flage) {
					result.add(token);
				}

			}
		}

		return result;

	}

	public static String removeStopWordsAsString(String tokens) {
		String result = "";
		if (tokens == null || tokens.equals("")) {
			return null;

		}
		List<String> keyWords = constructDefaultFilterString("StopResource",
				"javaStopList.txt", "userStopList.txt");

		for (String token : tokens.split(Basic.SPLIT_STRING)) {

			if (token.length() >= minLength && (int) token.charAt(0) < 255) {
				String lowercasetoken;
				lowercasetoken = token.toLowerCase();
				boolean flage = false;
				for (String keyword : keyWords) {
					if (lowercasetoken.trim().equals(keyword.trim().toLowerCase())) {
						// System.out.println("not add keyword:"+ token);
						flage = true;
						break;
					}
				}

				if (!flage) {
					if (result.equals("")) {
						result = token;
					} else {
						result = result + ";" + token;
					}
				}

			}
		}

		return result;

	}

	/**
	 * @param stop_list_path
	 *            "StopResource"
	 * @param stopfileName
	 *            "javaStopList.txt"
	 * @param userStopFileName
	 *            "userStopList.txt"
	 * @return
	 */
	public static List<String> constructDefaultFilterString(
			String stop_list_path, String stopfileName, String userStopFileName) {
		List<String> keyWords = new ArrayList<String>();
		String SPLIT_STRING = "[&#$_.(){}!*%+-=><\\:;,?/\"\'\t\b\r\n\0 ]";
		stopfileName = "/" + stop_list_path + "/" + stopfileName;
		userStopFileName = "/" + stop_list_path + "/" + userStopFileName;

		Resource myResource = new Resource();
		String tempKeyWords1 = myResource.getResource(stopfileName)
				.toLowerCase();
		String tempKeyWords2 = myResource.getResource(userStopFileName)
				.toLowerCase();

		keyWords = CommUtil.arrayToList((tempKeyWords1 + "," + tempKeyWords2)
				.split(SPLIT_STRING));
		for (int i = 0; i < keyWords.size() - 1; i++) {
			for (int j = i + 1; j < keyWords.size(); j++) {
				if ((keyWords.get(i).trim()).equals(keyWords.get(j).trim())) {

					// System.out.println("remove word: "+keyWords.get(j)+" j="+j);
					keyWords.remove(j);
					j = j - 1;
				}
			}

		}

		return keyWords;
	}

	public static void main(String[] args) {
		System.out.println(getProjectNameFromProjectPath("Buddi"));
	}

	// TODO PATH BUG
	public static String getPluginCurrentPath() {
		String result = "";
		result = Platform.getInstanceLocation().getURL().getPath();

		return result;

	}

	public static String getMyPluginCurrentPathbyFilename(String filename) {
		String result = "";

		// URL url = Activator.getDefault().getBundle().getResource(filename);
		// // "META-INF/MANIFEST.MF"
		//
		// result=FileLocator.toFileURL(url);
		//

		return result;

	}

	public static String getFDUPluginCurrentPath() {
		String result = "";
		Platform.getInstanceLocation().getURL().getPath();
		try {
			result = FileLocator
					.resolve(
							FDUHelpSeekingPlugin.getDefault().getBundle()
									.getEntry("/")).getFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;

	}

	public static String getFDUPluginWorkingPath() {
		String workingPath = System.getProperties().getProperty("user.home");
		
		String ldaDataPath="/data/";
		String ldaDocsPath = "/data/LdaOriginalDocs/";
		
		String LdaResultsPath = "/data/LdaResults/";
		String LdaParameterPath = "/data/LdaParameter/";

		String osName = System.getProperties().getProperty("os.name");
		if (osName.contains("Windows")) {

			
			workingPath = workingPath + "\\HelpSeekingData";
			
			ldaDataPath=workingPath+"\\data";
			ldaDocsPath=workingPath+"\\data\\LdaOriginalDocs";
			LdaResultsPath=workingPath+"\\data\\LdaResults";
			LdaParameterPath = workingPath+"\\data\\LdaParameter";
			ldaDataPath=workingPath+"\\data";

		} else
		
		if (osName.contains("Mac")) {
			workingPath = workingPath + "/HelpSeekingData";
			ldaDataPath=workingPath+"/data";
			ldaDocsPath = workingPath+"/data/LdaOriginalDocs";
			LdaResultsPath =workingPath+ "/data/LdaResults";
			LdaParameterPath = workingPath+"/data/LdaParameter";
			
			

		}else {
			//有待设置处理不同的操作系统
			workingPath = workingPath + "/HelpSeekingData";
			
			ldaDataPath=workingPath+"/data";
		    ldaDocsPath = workingPath+"/data/LdaOriginalDocs";
			LdaResultsPath =workingPath+ "/data/LdaResults";
			LdaParameterPath = workingPath+"/data/LdaParameter";


		}
		
		
		File file = new File(workingPath);
		if (!file.exists())
			FileHelper.createDirectory(workingPath);
		
		File fileldaDataPath = new File(ldaDataPath);
		if (!fileldaDataPath.exists())
			FileHelper.createDirectory(ldaDataPath);

		
		File fileldaDocsPath = new File(ldaDocsPath);
		if (!fileldaDocsPath.exists())
			FileHelper.createDirectory(ldaDocsPath);

		File fileLdaResultsPath = new File(LdaResultsPath);
		if (!fileLdaResultsPath.exists())
			FileHelper.createDirectory(LdaResultsPath);
		File fileLdaParameterPath = new File(LdaParameterPath);
		
		if (!fileLdaParameterPath.exists())
			FileHelper.createDirectory(LdaParameterPath);
	

		return workingPath;

	}

	public static String getCurrentProjectPath() {
		String result = new File("").getAbsolutePath();
		if (result.endsWith("target\\classes"))
			result = result.substring(0, result.length() - 15);
		return result;
	}

	public static String getProjectNameFromProjectPath(String projectPath) {
		return new File(projectPath).getName();
	}

	public static String ListToString(List<String> list) {
		String result = "";

		if (list != null) {

			for (String object : list) {
				if (object != null)
					result = result + object.toString() + ";";
			}
			result = result.trim();
		} else {
			result = null;
		}

		return result;
	}

	public static String tokenListToString(List<String> list) {
		String result = "";
		int count = 0;
		for (String object : list) {
			count = count + object.length();
			if (object != null || count < 65530)
				result = result + object + " ";
		}
		return result.trim();
	}

	public static List<String> arrayToList(String[] strContent) {
		List<String> result = new ArrayList<String>();
		for (String str : strContent) {
			if (str.trim().length() > 0)
				result.add(str);
		}
		return result;
	}

	public static List<String> stringToList(String strContent) {
		List<String> result = new ArrayList<String>();
		for (String str : strContent.split(SPLITE_STRING)) {
			if (str.trim().length() > 0)
				result.add(str);
		}
		return result;
	}

	public static List<String> stringToList(String strContent,
			String mySpliteString) {
		List<String> result = new ArrayList<String>();
		for (String str : strContent.split(mySpliteString)) {
			if (str.trim().length() > 0)
				result.add(str);
		}
		return result;
	}

	public static String getDateTime() {
		String result;
		GregorianCalendar calendar = new GregorianCalendar();
		result = calendar.get(Calendar.YEAR) + ""
				+ (calendar.get(Calendar.MONTH) + 1) + ""
				+ calendar.get(Calendar.DATE);
		return result;

	}

	public static int CompareStr(String str1, String str2) {

		if (str1 == null)
			str1 = "";
		if (str2 == null)
			str2 = "";

		return str1.compareTo(str2);
	}

	public static boolean compareString(String searchwords,
			String lastsearchwords) {
		boolean result = true;

		List<String> last = CommUtil.stringToList(lastsearchwords, "[ ]");
		List<String> now = CommUtil.stringToList(searchwords, "[ ]");

		Collections.sort(last);
		Collections.sort(now);
		if (last.size() != now.size()) {
			result = false;

		} else {

			for (int i = 0; i < last.size(); i++) {
				if (!last.get(i).equals(now.get(i))) {
					result = false;
				}
			}
		}

		return result;

	}

	public static String trimOnlySign(String fullMethodName) {
		String trimMethodName = fullMethodName;
		int indexP = fullMethodName.indexOf("(", 0);
		trimMethodName = fullMethodName.substring(0, indexP);

		// System.out.println(trimMethodName);

		return trimMethodName;

	}

	public static String trimMethodAndSign(String fullMethodName) {
		String trimMethodName = fullMethodName;
		int indexP = fullMethodName.indexOf("(", 0);
		trimMethodName = fullMethodName.substring(0, indexP);
		indexP = trimMethodName.lastIndexOf(".");
		trimMethodName = trimMethodName.substring(0, indexP);

		// System.out.println(trimMethodName);

		return trimMethodName;

	}

	public static List<String> stringToList(String tempString,
			String splitString, int miniLength) {
		List<String> result = new ArrayList<String>();
		for (String str : tempString.split(splitString)) {
			if (str.trim().length() > miniLength)
				result.add(str);
		}
		return result;
	}

	public static List<CxKeyPair> getKeyCxList() {

		List<CxKeyPair> allCxKeyPair = new ArrayList<CxKeyPair>();

		CxKeyPair ckp1 = new CxKeyPair(
				"AIzaSyAoB0pBvZ6veuzDQbR21auME8HJUwmCaos",
				"005635559766885752621:ly8_ifzrwps");
		CxKeyPair ckp2 = new CxKeyPair(
				"AIzaSyCr7g1tTLyy1MYOT8osYiBhuNQX4Od5JFM",
				"005635559766885752621:va1etsiak-a");
		CxKeyPair ckp3 = new CxKeyPair(
				"AIzaSyCDvxfQpMizImxGbx3yQv6z4bfrvSjHJTY",
				"005635559766885752621:q0yjnkh3lgi");
		CxKeyPair ckp4 = new CxKeyPair(
				"AIzaSyCXTStjSSEk4WH2ravVosalWS6EtGN5s9Q",
				"005635559766885752621:ys-az1pvb2o");
		CxKeyPair ckp5 = new CxKeyPair(
				"AIzaSyAIjU5Hmz0KFKo6m466Gxg1f6CpXE-ILhM",
				"005635559766885752621:7lpnvltcihi");
		CxKeyPair ckp6 = new CxKeyPair(
				"AIzaSyBaCiuRptGo8d091SDza2MjUv9Ls0iuB5E",
				"005635559766885752621:ktb_q5howhq");
		CxKeyPair ckp7 = new CxKeyPair(
				"AIzaSyAC54SHQwECRqHGFg8k3n6OEA9FkM6280E",
				"005635559766885752621:k6dpbo7ovf8");
		CxKeyPair ckp8 = new CxKeyPair(
				"AIzaSyCqstdHwuC2BcFhqdLDUSu_suWQHBhPulE",
				"005635559766885752621:kn50cjge5t4");
		CxKeyPair ckp9 = new CxKeyPair(
				"AIzaSyCbO9ltRiZm8gvmjcphzi2Lmts9y9VlDfE",
				"005635559766885752621:rapzyt3z6f4");
		CxKeyPair ckp10 = new CxKeyPair(
				"AIzaSyDHgWn_MeUvBp0ha41XIXUGsT0LQuaylSs",
				"005635559766885752621:nnlxix8wrxo");
		CxKeyPair ckp11 = new CxKeyPair(
				"AIzaSyCEw3qtyVRYJ9QlhKgQN2FMwRa4N6zxt78",
				"005635559766885752621:fqacawephkk");
		CxKeyPair ckp12 = new CxKeyPair(
				"AIzaSyARXdH3_gBhG3zg3mmCp6NH-RLCqs9w_h8",
				"005635559766885752621:rjqlrd92ema");
		CxKeyPair ckp13 = new CxKeyPair(
				"AIzaSyA2aCykyQf1nZP2ZlA54Nrswliy0kThJ5w",
				"005635559766885752621:mkhzdvvqmdc");
		CxKeyPair ckp14 = new CxKeyPair(
				"AIzaSyAlOeQI4kAlSHvxGpCA7oj9r4ZCzCAsXho",
				"005635559766885752621:qurwj2b9mrw");
		allCxKeyPair.add(ckp1);
		allCxKeyPair.add(ckp2);
		allCxKeyPair.add(ckp3);
		allCxKeyPair.add(ckp4);
		allCxKeyPair.add(ckp5);
		allCxKeyPair.add(ckp6);
		allCxKeyPair.add(ckp7);
		allCxKeyPair.add(ckp8);
		allCxKeyPair.add(ckp9);
		allCxKeyPair.add(ckp10);
		allCxKeyPair.add(ckp11);
		allCxKeyPair.add(ckp12);
		allCxKeyPair.add(ckp13);
		allCxKeyPair.add(ckp14);
		return allCxKeyPair;
	}

	public static boolean compareHistoryString(String searchText,
			String lastsearchwords, int historyquerywindowcount) {
		// TODO Auto-generated method stub
		return false;
	}
}
