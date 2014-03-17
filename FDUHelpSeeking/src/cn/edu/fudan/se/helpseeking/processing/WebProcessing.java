package cn.edu.fudan.se.helpseeking.processing;

import cn.edu.fudan.se.helpseeking.web.CrawlWebPages;

public class WebProcessing extends Thread {

	static Object obj = new Object();

	int i = 0;

	public void run() {
		CrawlWebPages cwp = new CrawlWebPages();
		synchronized (obj) {
			// 同步块中！防止 出错！

			cwp.haha();

		}
	}

}
