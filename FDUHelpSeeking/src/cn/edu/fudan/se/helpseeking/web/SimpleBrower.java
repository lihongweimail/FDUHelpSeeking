package cn.edu.fudan.se.helpseeking.web;

//import java.awt.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SimpleBrower implements ControlListener {

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
	private ProgressBar progressBar = null;// 装载页面时的进度条
	private Button refreshButton = null;// 刷新按钮
	private Composite myComposite = null;
	
	private boolean resized = false;

	public SimpleBrower(Composite composite) {
		setMyComposite(composite);
	}

	public SimpleBrower() {
		super();
	}

	public Composite getMyComposite() {
		return myComposite;
	}

	public void setMyComposite(Composite myComposite) {
		this.myComposite = myComposite;
	}

	// 初始化浏览器
	private void createBrowser(org.eclipse.swt.widgets.Composite composite) {
		org.eclipse.swt.layout.GridData gridData3 = new org.eclipse.swt.layout.GridData();
		// 创建浏览器对象
		browser = new Browser(composite, SWT.BORDER);

		gridData3.horizontalSpan = 7;
		gridData3.horizontalAlignment = SWT.FILL;
		gridData3.verticalAlignment = SWT.FILL;
		gridData3.grabExcessVerticalSpace = true;
		// 设置浏览器布局
		browser.setLayoutData(gridData3);
		// 为浏览器注册标题改变事件
		browser.addTitleListener(new org.eclipse.swt.browser.TitleListener() {
			public void changed(org.eclipse.swt.browser.TitleEvent e) {
				// sShell.setText(APP_TITLE + " - " + e.title);

			}
		});
		// 为浏览器注册地址改变事件
		browser.addLocationListener(new org.eclipse.swt.browser.LocationListener() {
			public void changing(org.eclipse.swt.browser.LocationEvent e) {
				locationText.setText(e.location);
			}

			public void changed(org.eclipse.swt.browser.LocationEvent e) {

			}
		});
		// 为浏览器注册装载网页事件
		browser.addProgressListener(new org.eclipse.swt.browser.ProgressListener() {
			// 当装载时，设置装载的进度，并且设置停止按钮可用
			public void changed(org.eclipse.swt.browser.ProgressEvent e) {
				if (!stopButton.isEnabled() && e.total != e.current) {
					stopButton.setEnabled(true);
				}
				progressBar.setMaximum(e.total);
				progressBar.setSelection(e.current);
			}

			// 装载完成后设置停止按钮，后退按钮，前进按钮和进度条的状态
			public void completed(org.eclipse.swt.browser.ProgressEvent e) {
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
		browser.addStatusTextListener(new org.eclipse.swt.browser.StatusTextListener() {
			public void changed(org.eclipse.swt.browser.StatusTextEvent e) {
				statusText.setText(e.text);
			}
		});
		// 初始状态打开主页的url
		// browser.setUrl(HOME_URL);
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
		getMyComposite().pack();
		browser.refresh();

	}

	public static void main(String[] args) {
		org.eclipse.swt.widgets.Display display = org.eclipse.swt.widgets.Display
				.getDefault();
		//org.eclipse.swt.widgets.Shell sShell = null;
		//Composite composite = new Composite(sShell, SWT.NONE);
		SimpleBrower thisClass = new SimpleBrower();
		thisClass.createShow();
		thisClass.myComposite.pack();

		while (!thisClass.myComposite.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	// 创建窗口和窗口的控件
	public void createShow() {
		// sShell = new org.eclipse.swt.widgets.Shell();
		org.eclipse.swt.layout.GridLayout gridLayout1 = new GridLayout();
		org.eclipse.swt.layout.GridData gridData2 = new org.eclipse.swt.layout.GridData();
		org.eclipse.swt.layout.GridData gridData4 = new org.eclipse.swt.layout.GridData();
		org.eclipse.swt.layout.GridData gridData5 = new org.eclipse.swt.layout.GridData();
		org.eclipse.swt.layout.GridData gridData6 = new org.eclipse.swt.layout.GridData();
		org.eclipse.swt.layout.GridData gridData7 = new org.eclipse.swt.layout.GridData();
		org.eclipse.swt.layout.GridData gridData8 = new org.eclipse.swt.layout.GridData();
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
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
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
		gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData5.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData6.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData6.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData7.horizontalSpan = 1;
		gridData7.grabExcessHorizontalSpace = true;
		gridData7.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData7.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData8.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		gridData8.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		refreshButton.setText("Refresh");
		refreshButton.setToolTipText("Refresh the current page");

		int x = myComposite.getParent().getBounds().x;
		int y = myComposite.getParent().getBounds().y;

		myComposite.setSize(new org.eclipse.swt.graphics.Point(0, 0));
		// 注册显示地址的文本框事件
		locationText
				.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
					public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
						locationText.selectAll();
					}
				});
		locationText.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
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
				browser.setBounds(x, y, width - 15, height);

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
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						browser.setUrl(locationText.getText());// 设置浏览器的指向的url
					}
				});

		refreshButton
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {

						getMyComposite().pack();
						browser.refresh();// 重新载入
					}
				});

		stopButton
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						browser.stop();// 停止装载网页
					}
				});
		backButton
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						browser.back();// 后退
					}
				});
		forwardButton
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						browser.forward();// 前进
					}
				});
		homeButton
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						browser.setUrl(HOME_URL);// 设置主页
					}
				});
		goButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
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
