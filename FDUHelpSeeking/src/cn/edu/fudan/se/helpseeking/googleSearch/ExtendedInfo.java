package cn.edu.fudan.se.helpseeking.googleSearch;

import java.util.HashMap;

public class ExtendedInfo {
	String memberEmail = "";
	ExtendedStorage storage;
	
	public ExtendedInfo(String memberEmail){
		this.memberEmail = memberEmail;
	}
	
	private static HashMap<String, String> keywordMap;

	public static HashMap<String, String> getKeywordMap() {

		if (keywordMap == null) {
			keywordMap = new HashMap<String, String>();
		}

		return keywordMap;
	}
	
	public void makeKeywordMap() {
		keywordMap = new HashMap<String, String>();
		LogonDataBean dataBean;
		String memberId = memberEmail.split("@")[0];


		storage = new ExtendedStorage(memberEmail);
		storage.execute(); // start the extending algorithm

		int nickNameCount = storage.nickNameList.size();

		try {
			dataBean = new LogonDataBean();
			dataBean = LogonDBBean.getInstance().getMember(memberEmail);

			keywordMap.put("email", dataBean.getEmail());
			keywordMap.put("id", memberId);
			keywordMap.put("name", dataBean.getName());
			keywordMap.put("cellphone", dataBean.getCellphone());
			keywordMap.put("homephone", dataBean.getHomephone());
			keywordMap.put("birthday", dataBean.getBirthday());
			keywordMap.put("address", dataBean.getAddress());
			keywordMap.put("school", dataBean.getSchool());
			keywordMap.put("workplace", dataBean.getWorkplace());
			keywordMap.put("occupation", dataBean.getOccupation());

			if (!keywordMap.containsKey("name")) {
				keywordMap.put("name", storage.realName);
			}
			keywordMap.put("email2", storage.realEmail);
			keywordMap.put("birthday2", storage.realBirthday);

			
			for (int i = 0; i < nickNameCount; i++) {
				switch (i) {
				
				case 0:
					keywordMap.put("nickname1", storage.nickNameList.get(i));
					
					break;
				case 1:
					keywordMap.put("nickname2", storage.nickNameList.get(i));

					break;
				case 2:
					keywordMap.put("nickname3", storage.nickNameList.get(i));

					break;
				case 3:
					keywordMap.put("nickname4", storage.nickNameList.get(i));

					break;
				case 4:
					keywordMap.put("nickname5", storage.nickNameList.get(i));

					break;
				}
			}
			
			if(nickNameCount < 5) {
				for(int i=0; i<5-nickNameCount; i++){
					keywordMap.put("nickname" + (nickNameCount+i+1), "null");
					
				}
			}
			
			System.out.println(keywordMap.get("nickname1") + "   " + keywordMap.get("nickname2") + "   " + keywordMap.get("nickname3") + "   " + keywordMap.get("nickname4") + "   " + keywordMap.get("nickname5"));
		

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}