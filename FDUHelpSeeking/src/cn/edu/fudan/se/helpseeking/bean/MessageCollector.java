package cn.edu.fudan.se.helpseeking.bean;

public class MessageCollector {
	
	String message;
	String methodQualifiedName;
	String currentLineCode;
	String methodCode;
	String messageType; // "exception error" from console view?  "compile error" from Problems view?   
	
	private static MessageCollector instance;
	private MessageCollector(){
		
	}
	public static synchronized MessageCollector getInstance(){
		if(instance==null)
		{
			instance = new MessageCollector();
		}
		return instance;
	}
	
	public  void SetValues(String message, String methodQualifiedName,
			String currentLineCode, String methodCode, String messageType) {
		
		this.message = message;
		this.methodQualifiedName = methodQualifiedName;
		this.currentLineCode = currentLineCode;
		this.methodCode = methodCode;
		this.messageType = messageType;
	}
	
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getMethodQualifiedName() {
		return methodQualifiedName;
	}
	public void setMethodQualifiedName(String methodQualifiedName) {
		this.methodQualifiedName = methodQualifiedName;
	}
	public String getCurrentLineCode() {
		return currentLineCode;
	}
	public void setCurrentLineCode(String currentLineCode) {
		this.currentLineCode = currentLineCode;
	}
	public String getMethodCode() {
		return methodCode;
	}
	public void setMethodCode(String methodCode) {
		this.methodCode = methodCode;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	
}


//单例实现的范例
//public class LazySingleton {

// /**
//  * 此时静态变量不能声明为final，因为需要在工厂方法中对它进行实例化
//  */
// private static LazySingleton instance;
// /**
//  * 私有构造子，确保无法在类外实例化该类
//  */
// private LazySingleton() {
// }
// /**
//  * synchronized关键字解决多个线程的同步问题
//  */
// public static synchronized LazySingleton getInstance() {
//     if (instance == null) {
//         instance = new LazySingleton();
//     }
//     return instance;
// }
//}

