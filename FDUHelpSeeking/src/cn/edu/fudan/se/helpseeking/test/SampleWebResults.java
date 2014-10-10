package cn.edu.fudan.se.helpseeking.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



import cn.edu.fudan.se.helpseeking.googleAPIcall.WEBResult;


public class SampleWebResults {
	 public final static List<WEBResult> WEB_RESULTS;
	 static
	 {
	        final String [][] data = new String [] []
    		{
        		{
        			"http://stackoverflow.com/questions/15581687/class-getresource-returns-null",
        			"java - Class.getResource() returns null - Stack Overflow",
        			"I am trying to display pictures on the JPanel but I keep getting the error : java.lang"
        			+ ".IllegalArgumentException: input == null! I don't understand ..."
        		},
        		{
        			"http://www.coderanch.com/t/480048/java/java/getResource-properties-file-package-put",
        			"getResource() properties file in package How to put contents in text ...",
        			"Jan 24, 2010 ... The moose likes Java in General and the fly likes getResource() ... "
        			+"IllegalArgumentException: could not load [properties.properties] as a ..."
        		},
        		{
        			"http://stackoverflow.com/questions/9867273/getresources-returns-null",
        			"java - getResources() returns null - Stack Overflow",
        			"getResource(\"/res/bitmaps/image.png\"));. and it throws the exception: Exception" 
        			+"in thread \"main\" java.lang.IllegalArgumentException: input ..."

        		},
        		{
        			"http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/7u40-b43/java/net/URLClassLoader.java",
        			"GC: URLClassLoader - java.net.URLClassLoader (.java ...",
        			"getResource(String) : URL · getResourceAsStream(String) : InputStream · "
        			+"getResources(String) : Enumeration<URL> · getSystemClassLoader() : ClassLoader."
        		},
        		{
        			"http://stackoverflow.com/questions/14544759/java-imageio-throws-illegalargumentexception",
        			"Java - ImageIO throws IllegalArgumentException",
        			"getResource(IMG_DIRECTORY)); } catch (IOException ioEx) { ioEx. ... "
        			+"IllegalArgumentException: input == null! at javax.imageio."
        		},
        		{
        			"http://docs.oracle.com/javase/7/docs/api/java/beans/beancontext/BeanContext.html",
        			"BeanContext (Java Platform SE 7 )",
        			"getResource() , this method allows a BeanContext implementation to interpose "
        			+"behavior ... name, BeanContextChild bcc) throws IllegalArgumentException."
        		},
        		{
        			"http://stackoverflow.com/questions/15424834/java-lang-illegalargumentexception-input-null-when-using-imageio-read-to-lo",
        			"java.lang.IllegalArgumentException: input == null! when using ...",
        			"getProperty(\"user.dir\") points to the correct location. I have also tried using "
        			+"getResource() instead of getResourceAsStream(), but it still does not ..."
        		},
        		{
        			"https://issues.apache.org/bugzilla/show_bug.cgi?id=53257",
        			"Bug 53257 – getLastModified() of compilation context returns ...",
        			"May 18, 2012 ... ... it returns -1 every time: java.lang.IllegalArgumentException: Negative time java"
        			+".io. ... getResource() and the paths returned by ServletContext."
        		},
        		{
        			"http://slick.ninjacave.com/forum/viewtopic.php?f=18&t=6082",
        			"Slick Forums • View topic - IllegalArgumentException: url is null",
        			"Here is my code: Code: themeManager = ThemeManager.createThemeManager"
        			+"(MainMenu.class.getResource(\"data/lesson2.xml\"), renderer);."
        		},
        		{
        			"https://bugs.openjdk.java.net/browse/JDK-6502392",
        			"[#JDK-6502392] Invalid relative names for Filer.createResource and ...",
        			"Dec 8, 2006 ... getResource(). The spec for getResource() and createResource() states that" 
        			+"IllegalArgumentException is thrown if relativeName is not relative,"
        		}
    		};
	        
	        final ArrayList<WEBResult> documents = new ArrayList<WEBResult>();
	        for (String [] row : data)
	        {
	        	WEBResult result = new WEBResult();
	        	result.setUrl(row[0]);
	        	result.setTitle(row[1]);
	        	result.setContent(row[2]);
	        	documents.add(result);
	         //   documents.add(new Document(row[1], row[2], row[0]));
	        }

	        WEB_RESULTS = Collections.unmodifiableList(documents);
	 }
}
