package cn.edu.fudan.se.helpseeking.web;

//import java.awt.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class SimpleBrowser implements ControlListener {

	// 定义浏览器的标题
	public static final String APP_TITLE = "Simple SWT Browser";
	// 定义主页的url
	public static final String HOME_URL = "http://about:blank";

	public Browser getBrowser() {
		return browser;
	}

	public void setBrowser(Browser browser) {
		this.browser = browser;
	}

	public static String getAppTitle() {
		return APP_TITLE;
	}

	public static String getHomeUrl() {
		return HOME_URL;
	}

	// 声明主窗口和其它控件
	//
	private Button backButton = null;// 后退按钮
	private Button forwardButton = null;// 前进按钮
	private Button stopButton = null;// 停止按钮
	private Text locationText = null;// 显示url的文本框
	private Button goButton = null;// 转向按钮
	private Browser browser = null;// 浏览器对象
	private Button homeButton = null;// 主页按钮
	private Label statusText = null;// 显示浏览器状态的文本框
//	private Label status;
	private ProgressBar progressBar = null;// 装载页面时的进度条
	private Button refreshButton = null;// 刷新按钮
	private Composite myComposite = null;
	
	private Display display=null;
	
	private boolean resized = false;
	
//	final static String PREAMBLE = "SNIPPET327LINK";
	
	

	public SimpleBrowser(Composite composite) {
		setMyComposite(composite);
	}

	public Display getDisplay() {
		return display;
	}

	public void setDisplay(Display display) {
		this.display = display;
	}

	public SimpleBrowser() {
		super();
	}

	public Composite getMyComposite() {
		return myComposite;
	}

	public void setMyComposite(Composite myComposite) {
		this.myComposite = myComposite;
	}

	// 初始化浏览器
	private void createBrowser(Composite composite) {
		GridData gridData3 = new GridData();
		// 创建浏览器对象
		 
		 browser = new Browser(composite, SWT.BORDER);
		
//		System.setProperty("org.eclipse.swt.browser.XULRunnerPath", "d:/xulrunner"); 
//		browser = new Browser(composite, SWT.MOZILLA);
		 
//		 browser.getMenu();
		 

		gridData3.horizontalSpan = 7;
		gridData3.horizontalAlignment = SWT.FILL;
		gridData3.verticalAlignment = SWT.FILL;
		gridData3.grabExcessVerticalSpace = true;
		// 设置浏览器布局
		browser.setLayoutData(gridData3);
		
	
		
		
		// 为浏览器中点击链接打开新URL
		browser.addOpenWindowListener(new OpenWindowListener() {
			
			@Override
			public void open(WindowEvent event) {
				 
//				   Browser browsertest = (Browser)event.widget; 
//	                String urlStr = browsertest.get;  
//	                locationText.setText(urlStr);
	               System.out.println("window open func: "+getBrowser().getUrl());
//	                browser.setUrl(urlStr);
	                event.browser = getBrowser();
	                event.browser.setUrl(statusText.getText());
	                locationText.setText(statusText.getText());
				
				//今后在这里添加代码，识别是否为doc, pdf等文件，来使用ole activex 实现文档的打开功能。
	
	             
				
				
			}
		});
		
		
//		// 为浏览器添加菜单？
//		browser.addMenuDetectListener(new MenuDetectListener() {
//			
//			@Override
//			public void menuDetected(MenuDetectEvent e) {
//				// TODO Auto-generated method stub
//				browser.getMenu();
//			}
//		});
		
		// 为浏览器注册标题改变事件
		browser.addTitleListener(new TitleListener() {
			public void changed(TitleEvent e) {
				 browser.getShell().setText(APP_TITLE + " - " + e.title);

			}
		});
		// 为浏览器注册地址改变事件

		browser.addLocationListener(new LocationListener() {

					
			public void changing(LocationEvent e) {
//				System.out.println("changing :"+ e.location);
//				locationText.setText(e.location);
//				locationText.setText("loading "+e.location);
			}

			public void changed(LocationEvent e) {
				
//				System.out.println("changed :  "+e.location);
//				locationText.setText(e.location);
//				setNewUrl(e.location);
				locationText.setText(e.location);
				
			}
		});
		// 为浏览器注册装载网页事件
		browser.addProgressListener(new ProgressListener() {
			// 当装载时，设置装载的进度，并且设置停止按钮可用
			public void changed(ProgressEvent e) {
				if (!stopButton.isEnabled() && e.total != e.current) {
					stopButton.setEnabled(true);
				}
				progressBar.setMaximum(e.total);
				progressBar.setSelection(e.current);
			}

			// 装载完成后设置停止按钮，后退按钮，前进按钮和进度条的状态
			public void completed(ProgressEvent e) {
				stopButton.setEnabled(false);
				backButton.setEnabled(browser.isBackEnabled());
				forwardButton.setEnabled(browser.isForwardEnabled());
				progressBar.setSelection(0);
				
				if(!resized){
					resized = true;
					String text = browser.getText();
					String resize = "<script type=\"text/javascript\">"
							+ "document.body.style.zoom=\"100%\""
							+ "</script>";
					
					Document doc = Jsoup.parse(text); 
					//System.out.println(doc.html());
					Elements children = doc.select("html > *");
					for(Element child : children){
						child.prepend(resize);
					}
					//System.out.println(doc.html());
					
					browser.setText(doc.html());				
				}

			}
		});
		// 注册浏览器状态改变事件
		browser.addStatusTextListener(new StatusTextListener() {
			public void changed(StatusTextEvent e) {
				statusText.setText(e.text);
//				status.setText(e.text);
			}
		});

		 
	}

	public void setNewUrl(String Url) {
		resetResized();
		browser.setUrl(Url);
		System.out.println("the new url: "+ browser.getText());
	}
	
	public void resetResized(){
		resized = false;
	}

	public void refreshBrowser() {
		getMyComposite().pack();
		browser.refresh();

	}

	public static void main(String[] args) {
		 Display display = new Display();  
	      Shell shell = new Shell(display); 
		//org.eclipse.swt.widgets.Shell sShell = null;
		//Composite composite = new Composite(sShell, SWT.NONE);
	      shell.setLayout(new FillLayout());  
		Composite composite = new Composite(shell, SWT.DEFAULT);
		SimpleBrowser thisClass = new SimpleBrowser(composite);
		thisClass.createShow();
		thisClass.setDisplay(display);
		thisClass.myComposite.pack();

		shell.open();
		while (!thisClass.myComposite.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	// 创建窗口和窗口的控件
	public void createShow() {
		// sShell = new org.eclipse.swt.widgets.Shell();
		GridLayout gridLayout1 = new GridLayout();
		GridData gridData2 = new GridData();
		GridData gridData4 = new GridData();
		GridData gridData5 = new GridData();
		GridData gridData6 = new GridData();
		GridData gridData7 = new GridData();
		GridData gridData8 = new GridData();
		
//		GridData gridDataStatus=new GridData();
		
		
		backButton = new Button(myComposite, SWT.ARROW | SWT.LEFT);
		forwardButton = new Button(myComposite, SWT.ARROW | SWT.RIGHT);
		stopButton = new Button(myComposite, SWT.NONE);
		refreshButton = new Button(myComposite, SWT.NONE);
		homeButton = new Button(myComposite, SWT.NONE);
		locationText = new Text(myComposite, SWT.BORDER);
		goButton = new Button(myComposite, SWT.NONE);
		createBrowser(myComposite);
		// createBrowser();
		progressBar = new ProgressBar(myComposite, SWT.BORDER);
		statusText = new Label(myComposite, SWT.NONE);
		
//        status = new Label(myComposite, SWT.NONE);
		
		
		// myComposite.setText(APP_TITLE);
		myComposite.setLayout(gridLayout1);
		gridLayout1.numColumns = 7;
		backButton.setEnabled(false);
		backButton.setToolTipText("Navigate back to the previous page");
		backButton.setLayoutData(gridData6);
		forwardButton.setEnabled(false);
		forwardButton.setToolTipText("Navigate forward to the next page");
		forwardButton.setLayoutData(gridData5);
		stopButton.setText("Stop");
		stopButton.setEnabled(false);
		stopButton.setToolTipText("Stop the loading of the current page");
		goButton.setText("Go!");
		goButton.setLayoutData(gridData8);
		goButton.setToolTipText("Navigate to the selected web address");
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.horizontalAlignment = GridData.FILL;
		gridData2.verticalAlignment = GridData.CENTER;
		locationText.setLayoutData(gridData2);
		locationText.setText(HOME_URL);
		locationText.setToolTipText("Enter a web address");
		homeButton.setText("Home");
		homeButton.setToolTipText("Return to home page");
		statusText.setText("Done");
		statusText.setLayoutData(gridData7);
		gridData4.horizontalSpan = 5;
		progressBar.setLayoutData(gridData4);
		progressBar.setEnabled(false);
		progressBar.setSelection(0);
		gridData5.horizontalAlignment = GridData.FILL;
		gridData5.verticalAlignment = GridData.FILL;
		gridData6.horizontalAlignment = GridData.FILL;
		gridData6.verticalAlignment = GridData.FILL;
		gridData7.horizontalSpan = 1;
		gridData7.grabExcessHorizontalSpace = true;
		gridData7.horizontalAlignment = GridData.FILL;
		gridData7.verticalAlignment = GridData.CENTER;
		gridData8.horizontalAlignment = GridData.END;
		gridData8.verticalAlignment = GridData.CENTER;
		
//		gridDataStatus.horizontalSpan=7;
//		gridDataStatus.grabExcessHorizontalSpace = true;
//		gridDataStatus.horizontalAlignment = GridData.FILL;
//		gridDataStatus.verticalAlignment = GridData.FILL;
//		status.setLayoutData(gridDataStatus);
		
		refreshButton.setText("Refresh");
		refreshButton.setToolTipText("Refresh the current page");

		int x = myComposite.getParent().getBounds().x;
		int y = myComposite.getParent().getBounds().y;

		myComposite.setSize(new Point(0, 0));
		// 注册显示地址的文本框事件
		locationText
				.addMouseListener(new MouseAdapter() {
					public void mouseUp(MouseEvent e) {
						locationText.selectAll();
					}
				});
		
		locationText.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				// Handle the press of the Enter key in the locationText.
				// This will browse to the entered text.
				if (e.character == SWT.LF || e.character == SWT.CR) {
					e.doit = false;
					browser.setUrl(locationText.getText());
				}
			}
		});

		// new add 2014.1.5
		// myComposite.addPaintListener(new PaintListener() {
		//
		// @Override
		// public void paintControl(PaintEvent e) {
		// myComposite.pack();
		//
		// }
		// });

		myComposite.addControlListener(new ControlListener() {

			@Override
			public void controlResized(ControlEvent e) {
				// TODO Auto-generated method stub
				// System.out.println("mycomposite brower reciver resized");
				int width = myComposite.getParent().getBounds().width;
				int height = myComposite.getParent().getBounds().height;
				int x = myComposite.getParent().getBounds().x;
				int y = myComposite.getParent().getBounds().y;
				// System.out.println("width:"+width+"height"+height+"x"+x+"y"+y);
				// myComposite.setBounds(x, y, width, height);
				myComposite.setSize(width, height);
				myComposite.redraw();
				browser.setBounds(x, y, width, height);

				// myComposite.pack();
			}

			@Override
			public void controlMoved(ControlEvent e) {
				// TODO Auto-generated method stub
				// System.out.println("moved");
				myComposite.pack();
			}
		});

		locationText
				.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(
							SelectionEvent e) {
						browser.setUrl(locationText.getText());// 设置浏览器的指向的url
					}
				});

		refreshButton
				.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(
							SelectionEvent e) {

						getMyComposite().pack();
						browser.refresh();// 重新载入
					}
				});

		stopButton
				.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(
							SelectionEvent e) {
						browser.stop();// 停止装载网页
					}
				});
		backButton
				.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(
							SelectionEvent e) {
						browser.back();// 后退
					}
				});
		forwardButton
				.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(
							SelectionEvent e) {
						browser.forward();// 前进
					}
				});
		homeButton
				.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(
							SelectionEvent e) {
						browser.setUrl(HOME_URL);// 设置主页
					}
				});
		goButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				browser.setUrl(locationText.getText());// 转向地址的网页
			}
		});
	}

	@Override
	public void controlMoved(ControlEvent e) {

		// System.out.println("moved");
		int width = myComposite.getParent().getBounds().width;
		int height = myComposite.getParent().getBounds().height;
		int x = myComposite.getParent().getBounds().x;
		int y = myComposite.getParent().getBounds().y;
		// System.out.println("width:"+width+"height"+height+"x"+x+"y"+y);
		// myComposite.setBounds(x, y, width, height);
		myComposite.setSize(width, height);
		myComposite.redraw();
		// myComposite.pack();
	}

	@Override
	public void controlResized(ControlEvent e) {
		// System.out.println("resized");
		// int width=myComposite.getParent().getBounds().width;
		// int height=myComposite.getParent().getBounds().height;
		// int x=myComposite.getParent().getBounds().x;
		// int y=myComposite.getParent().getBounds().y;
		// System.out.println("width:"+width+"height"+height+"x"+x+"y"+y);
		// // myComposite.setBounds(x, y, width, height);
		// myComposite.setSize(width, height);
		// myComposite.redraw();
		// // myComposite.pack();

	}

	public void setDisableButton(boolean state) {
		backButton.setVisible(state);// 后退按钮
		forwardButton.setVisible(state);
		// 前进按钮
		stopButton.setVisible(state);
		// 停止按钮
		locationText.setVisible(state);// 显示url的文本框
		goButton.setVisible(state);// 转向按钮

		homeButton.setVisible(state);// 主页按钮

		refreshButton.setVisible(state);// 刷新按钮

	}
}
