package cn.edu.fudan.se.helpseeking.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

class ExtensionFileFilter implements FileFilter
{
	String acceptedExtension;

	public ExtensionFileFilter(String acceptedExtension)
	{
		this.acceptedExtension = acceptedExtension;
	}

	public boolean accept(File pathname)
	{
		return pathname.getName().endsWith(acceptedExtension)
				|| pathname.isDirectory();
	}

}

public class FileHelper
{
	public static String NewLine = System.getProperty("line.separator");
	private static String PNG = "png"; 
	
	public static void appendContentToFile(String fileName, String content)
	{
		
		try
		{			
			FileWriter fw = new FileWriter(fileName, true);
			fw.append(content);
			fw.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	


	public static void createFile(String fileName) {
		File file = new File(fileName);
		try {
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			MicroLog.log(Level.INFO, "create file error");
		}
		
	}


	public static void createFile(String fileName, String content)
	{
		
		PrintWriter pw;
		try
		{
			pw = new PrintWriter(fileName);
			pw.write(content);
			
			pw.close();
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	

//	public static void createFileWithTokenize(String fileName, String content)
//	{
//		TokenExtractor extractor = new TokenExtractor();
//		extractor.isAcceptDigit(false);
//		extractor.isAcceptReduplication(true);
//		content = CommUtil.ListToString(extractor.getIdentifierOccurenceOfString(content));
//		createFile(fileName, content);
//		
//	}
	
	
	public static void deleteFile(File relationFile)
	{
		relationFile.delete();
		
	}
	
	public static void deleteFile(String filename)
	{
		File f = new File(filename);
		while(f.exists())
			f.delete();
	}
	
	public  static String  getResource(Object obj, String resourcePath) {    
		
		String content="";
        //返回读取指定资源的输入流    
		try{
        InputStream is=obj.getClass().getResourceAsStream(resourcePath);     //"/resource/res.txt"
        BufferedReader in=new BufferedReader(new InputStreamReader(is));  
        
    	StringBuilder buffer = new StringBuilder();
		String line = null;

		while (null != (line = in.readLine()))
		{
			buffer.append("\t" + line);
			buffer.append("\n");

		}

		content = buffer.toString();
		in.close();

	}
	catch (IOException e)
	{
		e.printStackTrace();
	}
	return content;
    }    
	
	
	public static String getContent(String path)
	{
		String content = "";
		try
		{
			File file = new File(path);
			BufferedReader in = new BufferedReader(new FileReader(file));
			StringBuilder buffer = new StringBuilder();
			String line = null;

			while (null != (line = in.readLine()))
			{
				buffer.append("\t" + line);
				buffer.append("\n");

			}

			content = buffer.toString();
			in.close();

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return content;
	}

	
	public static String getPatternFileContent(String path)
	{
		String content = null;
		try
		{
			File file = new File(path);
			if (file.isFile())
			{
			BufferedReader in = new BufferedReader(new FileReader(file));
			StringBuilder buffer = new StringBuilder();
			String line = null;
			in.readLine();

			while (null != (line = in.readLine()))
			{
				buffer.append(line);
				buffer.append("\n");

			}

			content = buffer.toString();
			
			
			in.close();
		}

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return content;
	}
	
	public static String[] getContentArray(String path)
	{
		List<String> list = new ArrayList<String>();
		File file = new File(path);
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line = null;
			while (null != (line = in.readLine()))
			{
				list.add(line);
			}
			in.close();
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] result;
		result = list.toArray(new String[0]);
		return result;

	}
	
	public static List<String> getContentAsList(String instanceFile)
	{
		List<String> result = new ArrayList<String>();
		try
		{
			File file = new File(instanceFile);
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line = null;
			while(null != (line = in.readLine()))
			{
				result.add(line);
			}
		}
		catch (IOException e)
		{
			// TODO: handle exception
		}
		return result;
	}
	
	public static Image getScaledImage(Image srcImg, int w, int h)
	{
		
		BufferedImage resizedImg = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = resizedImg.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(srcImg, 0, 0, w, h, null);
		g2.dispose();
		
		return resizedImg;

	}
	
	public static ImageIcon getScaledImage(ImageIcon icon, int w, int h)
	{
		Image image = icon.getImage();                         
		Image smallImage = image.getScaledInstance(w,h,Image.SCALE_FAST);
		ImageIcon result = new ImageIcon(smallImage);
		return result;
	}
	
	public static int getSqrt(int value)
    {
    	int result;
    	result = (int) Math.sqrt(value);
    	if(result > 0)
    		result --;
    	return result;
    }
	
    /**
	 * 鍦ㄦ枃浠跺す涓紝鑾峰彇鎵�湁鎸囧畾鍚庣紑鍚庣殑鏂囦欢
	 * @param path
	 * @param extension
	 * @return
	 */
	public static List<String> getSubFile(String path, String extension)
	{		 
		FileFilter fileFilter = new ExtensionFileFilter(extension);
		FileHelper fileHelper = new FileHelper();
		return fileHelper.getSubFile(path, fileFilter);

	}
	
	public static void main(String[] args)
	{
		vacuumFile("C:\\temp.txt");
	}



	public static void readByteStreamtoPNGFile(String pngFileName, byte[] imageByteStream)
	{
		
		DataOutputStream out;
		try
		{
			out = new DataOutputStream(new FileOutputStream(pngFileName));
			out.write(imageByteStream);
			out.close();
			
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

		
		//TODO LIHONGWEI
	}

	public static void scaleImageFile(String imageSrc, int w)
	{
		 try
		{
			BufferedImage bufferedImage = ImageIO.read( new File(imageSrc));
			int h = (int) (w * 1.0 * bufferedImage.getHeight() / bufferedImage.getWidth());
			Image scaledImage = bufferedImage.getScaledInstance(w, h, BufferedImage.TYPE_INT_RGB);
			bufferedImage.getGraphics().drawImage(scaledImage, 0, 0, w,h,null);
			ImageIO.write(bufferedImage, PNG, new File(imageSrc));
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void scaleImageFile(String imageSrc, int w, int h)
	{
		 try
		{
			BufferedImage bufferedImage = ImageIO.read( new File(imageSrc));
			Image scaledImage = bufferedImage.getScaledInstance(w, h, BufferedImage.TYPE_INT_RGB);
			bufferedImage.getGraphics().drawImage(scaledImage, 0, 0, w,h,null);
			ImageIO.write(bufferedImage, PNG, new File(imageSrc));
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}




	public static byte[] transforPNGFiletoByteStream(String pngFileName) 
	{

		File pngfile=new File(pngFileName);
		
		
		int len=(int) pngfile.length();
		byte[] pngfileBytes=new byte[len];
		
		FileInputStream filestream;
		try
		{
			filestream = new FileInputStream(pngfile);
			filestream.read(pngfileBytes);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
        return pngfileBytes;
	
		//TODO  LIHONGWEI
	}




	public static void vacuumFile(String relationfileName)
	{
		File file = new File(relationfileName);
		if(file.exists())
		{
			FileWriter fw;
			try
			{				
				fw = new FileWriter(relationfileName, false);
				fw.close();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
	}
	
	private  List<String> getSubFile(String path, FileFilter filter)
	{
		List<String> result = new ArrayList<String>();
		File file = new File(path);
		if( file.isDirectory())
		{			
			for(File subFile :file.listFiles(filter))
			{
				result.addAll(getSubFile(subFile.getAbsolutePath(), filter));
			}
		}
		else 
		{
			result.add(file.getAbsolutePath());
		}
		return result;
	
	}











}
