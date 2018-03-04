package cn.edu.fudan.se.helpseeking.googleSearch;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ExtendedStorage {
	public static ArrayList<String> nickNameList = null;
	public static ArrayList<String> imgUrlList = null;
//	public static ArrayList<SiteInfo> siteInfoList = null;

	public static String realName = "null";
	public static String realBirthday = "null";
	public static String realEmail = "null";

	private String clientId;
	private String clientEmail;

//	Search[] webSite = new Search[11];

	public ExtendedStorage(String clientEmail) {
		this.clientEmail = clientEmail;
		clientId = this.clientEmail.split("@")[0];
		init();
	}

	public void init() {
		nickNameList = null;
		nickNameList = new ArrayList<String>();
		imgUrlList = null;
		imgUrlList = new ArrayList<String>();

//		siteInfoList = null;
//		siteInfoList = new ArrayList<SiteInfo>();

	}

	class staticThread implements Runnable {
		int i;

		public staticThread(int i) {
			this.i = i;
		}

		@Override
		public void run() {
//			webSite[i].searchMaterials(getClientId());
			return;
		}
	}

	public void execute() {
//		webSite[0] = new NaverBlog();
//		webSite[1] = new Twitter();
//		webSite[2] = new NaverMe2day();
//		webSite[3] = new DaumBlog();
//		webSite[4] = new Egloos();
//		webSite[5] = new Gallog();
//		webSite[6] = new NaverKin();
//		webSite[7] = new TodayHumor();
//		webSite[8] = new Dreamwiz();
//		webSite[9] = new CyworldBlog();
//		webSite[10] = new Cyworld();

		System.out.println("======Static Search 시작======");
//		ExecutorService service = Executors.newFixedThreadPool(webSite.length);
//		for (int i = 0; i < webSite.length - 1; i++) {
//			service.execute(new staticThread(i));
//		}
//
//		service.shutdown();
//		while (true) {
//			if (service.isTerminated()) {
//				System.out.println("======Static Search 종료======");
//				break;
//			}
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}

//		webSite[webSite.length - 1].searchMaterials(getClientEmail()); // Cyworld
//
		checkDupNick();

//		ImageStorage.getImgUrlList().addAll(imgUrlList);

		System.out.println("");
		System.out.println(getClientId() + "님의 닉네임");
		for (int i = 0; i < nickNameList.size(); i++) {
			System.out.println(nickNameList.get(i));
		}
		System.out.println("");
		System.out.println(getClientId() + "님의 프로필이미지Url");
		for (int i = 0; i < imgUrlList.size(); i++) {
			System.out.println(imgUrlList.get(i));
		}
		System.out.println("");
		System.out.println(getClientId() + "님의 주요 신상정보 노출 웹");
//		for (int i = 0; i < siteInfoList.size(); i++) {
//			System.out.println(siteInfoList.get(i).getUrl());
//		}

		System.out.println("");
		System.out.println(getClientId() + "님의 그 외 주요 신상정보");
		System.out.println("실명 : " + realName);
		System.out.println("생년월일 : " + realBirthday);
		System.out.println("사용 이메일 : " + realEmail);

	}

	public void checkDupNick() {
		int currentSize = nickNameList.size();

		for (int i = 0; i < currentSize - 1; i++) {
			for (int j = i + 1; j < currentSize; j++) {
				if (nickNameList.get(i).equals(nickNameList.get(j))) {
					nickNameList.remove(j);
					j--;
					currentSize--;
				}
			}
		}
	}

	public String getClientId() {
		return clientId;
	}

	public String getClientEmail() {
		return clientEmail;
	}

}