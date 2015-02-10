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

public class AmAssitBrowser implements ControlListener {

	// 定义浏览器的标题
	public static  String APP_TITLE = "Simple SWT Browser";
	// 定义主页的url
	public static final String HOME_URL = "about:blank";
	
	
	// test for js
	public String testforinteractewithhtmljs="this is test for js from html";
	public void setTestforinteractewithhtmljs(String testforinteractewithhtmljs) {
		this.testforinteractewithhtmljs = testforinteractewithhtmljs;
	}
	public String getTestforinteractewithhtmljs() {
		return testforinteractewithhtmljs;
	}	
	//end of test for js
	


	public Browser getBrowser() {
		return browser;
	}

	public void setBrowser(Browser browser) {
		this.browser = browser;
	}

	public static String getAppTitle() {
		return APP_TITLE;
	}
	public void setAppTitle(String titleString) {
		APP_TITLE=titleString;
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
	private ProgressBar progressBar = null;// 装载页面时的进度条
	private Button refreshButton = null;// 刷新按钮
	private Composite myComposite = null;
	private Display display=null;
	

	private boolean resized = false;
	
	
	
	public Display getDisplay() {
		return display;
	}

	public void setDisplay(Display display) {
		this.display = display;
	}

	public AmAssitBrowser(Composite composite) {
		setMyComposite(composite);
	}

	public AmAssitBrowser() {
		super();
	}

	public Composite getMyComposite() {
		return myComposite;
	}

	public void setMyComposite(Composite myComposite) {
		this.myComposite = myComposite;
		GridData gPanelData = new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL, GridData.FILL_BOTH); 
		myComposite.setLayoutData(gPanelData); 
		//为Panel也设置一个布局对象。文本框和按键将按这个布局对象来显示。 
		GridLayout gPanelLay = new GridLayout(); 
		myComposite.setLayout(gPanelLay); 
	}

	// 初始化浏览器
	private void createBrowser(Composite composite) {
		GridData gridData3 = new GridData();
		// 创建浏览器对象
		browser = new Browser(composite, SWT.BORDER);

		gridData3.horizontalSpan = 7;
		gridData3.horizontalAlignment = SWT.FILL;
		gridData3.verticalAlignment = SWT.FILL;
		gridData3.grabExcessVerticalSpace = true;
		// 设置浏览器布局
		browser.setLayoutData(gridData3);
		// 为浏览器注册标题改变事件
		browser.addTitleListener(new TitleListener() {
			public void changed(TitleEvent e) {
				// sShell.setText(APP_TITLE + " - " + e.title);
				System.out.println(e.title);

                //监听到后，处理HTML生成问题，判断title，最好title用一个全局变量保存，新HTMLtitle要相应变更
				  //另外，browser 中设置一个属性，用来判断浏览器是否专用于foamtree展示，从而以触发特定的HTML生成事件
				

			}
		});
		
		
		// 为浏览器注册地址改变事件
		browser.addLocationListener(new LocationListener() {
			public void changing(LocationEvent e) {
//				locationText.setText(e.location);
			}

			public void changed(LocationEvent e) {
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
				
//				if(!resized){
//					resized = true;
//					String text = browser.getText();
//					String resize = "<script type=\"text/javascript\">"
//							+ "document.body.style.zoom=\"100%\""
//							+ "</script>";
//					
//					Document doc = Jsoup.parse(text); 
//					//System.out.println(doc.html());
//					Elements children = doc.select("html > *");
//					for(Element child : children){
//						child.prepend(resize);
//					}
//					//System.out.println(doc.html());
//					
//					browser.setText(doc.html());				
//				}
			}
		});
		// 注册浏览器状态改变事件
		browser.addStatusTextListener(new StatusTextListener() {
			public void changed(StatusTextEvent e) {
				statusText.setText(e.text);
			}
		});

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
		

	}

	public void setNewUrl(String Url) {
		resetResized();
		browser.setUrl(Url);
		//System.out.println(browser.getText());
	}
	
	public void resetResized(){
		resized = false;
	}

	public void refreshBrowser() {
		
		browser.refresh();
		
      //getMyComposite().pack();
	}

	public static void main(String[] args) {
		 Display display = new Display();  
	      Shell shell = new Shell(display); 
		//Shell sShell = null;
		//Composite composite = new Composite(sShell, SWT.NONE);
	      shell.setLayout(new FillLayout());  
		Composite composite = new Composite(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.NO_BACKGROUND | SWT.NO_FOCUS | SWT.NO_MERGE_PAINTS | SWT.NO_REDRAW_RESIZE | SWT.NO_RADIO_GROUP | SWT.EMBEDDED);
		AmAssitBrowser thisClass = new AmAssitBrowser(composite);
		//正常浏览器
		thisClass.createShow();
		//精简浏览器
		//thisClass.createSimpleShow();
		
		thisClass.getBrowser().setUrl("http://stackoverflow.com/questions/17018857/how-to-call-jframe-from-another-java-class");
		thisClass.getBrowser().refresh();
		thisClass.setDisplay(display);
		thisClass.myComposite.pack();

		shell.open();
		
		while (!thisClass.myComposite.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	
	// 初始化浏览器
		private void createSimpleBrowser(Composite composite) {
			// 创建浏览器对象
			browser = new Browser(composite, SWT.BORDER);

			
			browser.addTitleListener(new TitleListener() {
				public void changed(TitleEvent e) {
					// sShell.setText(APP_TITLE + " - " + e.title);
                System.out.println("select group label is in amAssistBrowser class : "+ e.title);
				}
			});
			
			
			
		}

	// 创建简单窗 只有浏览器
	public void createSimpleShow() {
		// sShell = new Shell();
		
	
		int x = myComposite.getParent().getBounds().x;
		int y = myComposite.getParent().getBounds().y;

		myComposite.setSize(new Point(0, 0));
		
			createSimpleBrowser(myComposite);
		
	
		myComposite.addControlListener(new ControlListener() {

			@Override
			public void controlResized(ControlEvent e) {
			}

			@Override
			public void controlMoved(ControlEvent e) {
				myComposite.pack();
			}
		});

	}

	// 创建窗口和窗口的控件
	public void createShow() {
		// sShell = new Shell();
		GridLayout gridLayout1 = new GridLayout();
		GridData gridData2 = new GridData();
		GridData gridData4 = new GridData();
		GridData gridData5 = new GridData();
		GridData gridData6 = new GridData();
		GridData gridData7 = new GridData();
		GridData gridData8 = new GridData();
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
		refreshButton.setText("Refresh");
		refreshButton.setToolTipText("Refresh the current page");

		int x = myComposite.getParent().getBounds().x;
		int y = myComposite.getParent().getBounds().y;

		myComposite.setSize(new Point(x, y));
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
//				// TODO Auto-generated method stub
//				// System.out.println("mycomposite brower reciver resized");
//				int width = myComposite.getParent().getBounds().width;
//				int height = myComposite.getParent().getBounds().height;
//				int x = myComposite.getParent().getBounds().x;
//				int y = myComposite.getParent().getBounds().y;
//				// System.out.println("width:"+width+"height"+height+"x"+x+"y"+y);
//				// myComposite.setBounds(x, y, width, height);
//				myComposite.setSize(width, height);
//				myComposite.redraw();
//////				browser.setBounds(x, y, width - 15, height);
////
////				 myComposite.pack();
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
                         browser.refresh();// 重新载入
						//getMyComposite().pack();
						
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
		 myComposite.pack();
	}

	@Override
	public void controlResized(ControlEvent e) {
		 System.out.println("resized");
		 int width=myComposite.getParent().getBounds().width;
		 int height=myComposite.getParent().getBounds().height;
		 int x=myComposite.getParent().getBounds().x;
		 int y=myComposite.getParent().getBounds().y;
//		 System.out.println("width:"+width+"height"+height+"x"+x+"y"+y);
		 // myComposite.setBounds(x, y, width, height);
		 myComposite.setSize(width, height);
		 myComposite.redraw();
		  myComposite.pack();

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

		statusText.setVisible(state);
		progressBar.setVisible(false);
	}
	
	public void setRefreshOnly(boolean state)
	{
		setDisableButton(false);
		refreshButton.setVisible(true);
	}
	public String getId() {
		// TODO Auto-generated method stub
		return String.valueOf(this.mybrowserid);
	}
	
	long mybrowserid=0l;
	public void mysetId(long currentBrowserID) {
		// TODO Auto-generated method stub
		this.mybrowserid=currentBrowserID;
		
	}
}
