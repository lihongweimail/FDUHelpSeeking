package cn.edu.fudan.se.helpseeking.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.edu.fudan.se.helpseeking.bean.CxKeyPair;

public class INIHelper
{
	private FileReader FReadFile;
	private BufferedReader BReadFile;

	private FileWriter FWriteFile;
	private BufferedWriter BWriteFile;

	private String FileName; // 操作的文件名
	private LinkedList<String> FileStringList = new LinkedList<String>(); // 存放配置文件内容
	private int SectionStarNo = -1, SectionEndNo = -1, IdentNo = -1; // 存放该段落的起始位置
	
	
	/**
	 * 专门定制函数，获取cse.ini文件中的cx,key对
	 * @return
	 */
	public List<CxKeyPair> getCxKeyPairs()
	{
		List<CxKeyPair> myCxKeyPairs=new ArrayList<CxKeyPair>();
		String sectionName="cse";
		for (int i = 0; i < (FileStringList.size()/6) ; i++) {
			CxKeyPair cxkeypair=new CxKeyPair();
			cxkeypair.setCx(getValue(sectionName+i, "cx", "005635559766885752621:va1etsiak-a"));
			cxkeypair.setKey(getValue(sectionName+i, "key", "AIzaSyCr7g1tTLyy1MYOT8osYiBhuNQX4Od5JFM"));
			cxkeypair.setProjectID(getValue(sectionName+i, "projectid", "cnedufdusehelpseeking"));
			cxkeypair.setCseName(getValue(sectionName+i, "csename", "helpseeking"));

			myCxKeyPairs.add(cxkeypair);
//			System.out.println(sectionName+i +cxkeypair.toString());
		}
		
		
		return myCxKeyPairs;
	}
	

	private static  Resource myResource=new Resource();

	/**
	 * @param filename  please use relatively file path "/a.txt" means under the plugin jar package root file or you application working folder; 
	 * 
	 * @param pluginmode  true means load file in packageJAR , fase means load file from your application working folder
	 */
	public INIHelper(String filename, boolean pluginmode)
	{
		//使用reseource 从插件中获得资源文件的内容串
		// pluginmode 表示是否为插件资源包内读取 true    和    false则使用基本的读取方式，绝对路径定位到应用程序所在路径读取
		if (pluginmode) {
			FileStringList=myResource.getResourcetoLinkedList(filename);
			
		}
		else {
			normalInihelper(filename);
		}
		
		
		
	}
	
	
	public void normalInihelper(String FileNameSend) {
		FileName = FileNameSend;
		File f = new File(  FileName);
		if(!f.exists())
			f = new File(CommUtil.getCurrentProjectPath() + "\\" + FileName);
		try
		{

			String strFile;
			// 把该文件下的内容存到LIST中
			FReadFile = new FileReader(f);
			BReadFile = new BufferedReader(FReadFile);
			while (true)
			{
				strFile = BReadFile.readLine();				
				if (strFile == null)
					break;
				FileStringList.add(strFile);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		finally
		{
			try
			{
				if (BReadFile != null)
					BReadFile.close();
				if (FReadFile != null)
					FReadFile.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

	}
	
	
	

	// 初始化 FileName文件名
	public INIHelper(String FileNameSend)
	{

		FileName = FileNameSend;
		File f = new File(  FileName);
		if(!f.exists())
			f = new File(CommUtil.getCurrentProjectPath() + "\\" + FileName);
		try
		{

			String strFile;
			// 把该文件下的内容存到LIST中
			FReadFile = new FileReader(f);
			BReadFile = new BufferedReader(FReadFile);
			while (true)
			{
				strFile = BReadFile.readLine();				
				if (strFile == null)
					break;
				FileStringList.add(strFile);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		finally
		{
			try
			{
				if (BReadFile != null)
					BReadFile.close();
				if (FReadFile != null)
					FReadFile.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

	}

	// 设置段落和节点的起始位置
	private void SectionPlace(String SectionName_, String IdentName_)
	{
		String strTmp = null;
		IdentName_ = IdentName_.trim();
		// 查找段落是否存在
		for (int i = 0; i < FileStringList.size(); i++)
		{
			strTmp = (String) FileStringList.get(i);
			if (SectionName_.equals(strTmp))
			{
				SectionStarNo = i;
				continue;
			}
			if (SectionStarNo != -1)
			{

				if ((strTmp.charAt(0) == '[')
						& (strTmp.charAt(strTmp.length() - 1) == ']'))
				{
					SectionEndNo = i;
					break;
				}
				strTmp = strTmp.substring(0, strTmp.indexOf("=")).trim();
				if (strTmp.equals(IdentName_))
				{
					IdentNo = i;
				}
			}
		}
	}

	// 读取文件 SectionName部份名,IdentName节点名,DefaultValue默认值
	public String getValue(String SectionName_, String IdentName_,
			String DefaultValue_)
	{
		DefaultValue_ = this.MyReadFileINI(SectionName_, IdentName_,
				DefaultValue_);
		return DefaultValue_;
	}

	// 反回所取得的值,如果为空反回DefaultValue
	private String MyReadFileINI(String SectionName_, String IdentName_,
			String DefaultValue_)
	{
		SectionName_ = "[" + SectionName_ + "]";
		this.SectionPlace(SectionName_, IdentName_);
		// 如果节点存在
		if (IdentNo != -1)
		{
			String strtmp = new String();
			strtmp = FileStringList.get(IdentNo);
			strtmp = strtmp.substring(IdentName_.length() + 1);
			DefaultValue_ = strtmp;
		}
		this.ClearPlace();
		return DefaultValue_;

	}

	// 写入字符串
	public boolean setValue(String SectionName_, String IdentName_,
			String DefaultValue_)
	{
		boolean boolFile = true;
		boolFile = this.MyWriteFileINI(SectionName_, IdentName_, DefaultValue_);
		return boolFile;
	}

	// 写入文件
	private boolean MyWriteFileINI(String SectionName_, String IdentName_,
			String DefaultValue_)
	{
		boolean boolFile = true;
		SectionName_ = "[" + SectionName_ + "]";
		this.SectionPlace(SectionName_, IdentName_);
		String strtmp = new String();
		strtmp = IdentName_ + "=" + DefaultValue_;
		// 如果节点存在
		if (IdentNo != -1)
		{
			FileStringList.set(IdentNo, strtmp);
		}
		else
		{
			// 段落不存在
			if (SectionStarNo == -1)
			{
				FileStringList.add(SectionName_);
				FileStringList.add(strtmp);
			}
			else
			{
				// 后面没有段落了
				if (SectionEndNo == -1)
				{
					FileStringList.add(strtmp);
				}
				else
				{
					FileStringList.add(SectionEndNo, strtmp);
				}
			}
		}
		try
		{
			// 写入文件
			FWriteFile = new FileWriter(FileName, false);
			BWriteFile = new BufferedWriter(FWriteFile);
			for (int i = 0; i < FileStringList.size(); i++)
			{
				strtmp =  FileStringList.get(i) + "\r\n";
				BWriteFile.write(strtmp);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (BWriteFile != null)
					BWriteFile.close();
				if (FWriteFile != null)
					FWriteFile.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		this.ClearPlace();
		return boolFile;

	}

	private void ClearPlace()
	{
		SectionStarNo = -1;
		SectionEndNo = -1;
		IdentNo = -1;
	}
}