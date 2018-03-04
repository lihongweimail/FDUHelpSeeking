package cn.edu.fudan.se.helpseeking.googleSearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class KeywordCounting {
	RankingCount rankingCount;
	HashMap<String, String> userMap = new HashMap<String, String>();

	public RankingCount countingProcess(StringBuffer document) {
		userMap.clear();
		userMap = ExtendedInfo.getKeywordMap();

		rankingCount = new RankingCount();
		String doc = document.toString();

		rankingCount.setIdCount(doc.split(userMap.get("id")).length - 1);

		rankingCount.setEmailCount(doc.split(userMap.get("email")).length - 1);

		if (!(userMap.get("cellphone").equals("null"))) {
			rankingCount
					.setCellphoneCount(doc.split(userMap.get("cellphone")).length - 1);
		}
		if (!(userMap.get("homephone").equals("null"))) {
			rankingCount
					.setHomephoneCount(doc.split(userMap.get("homephone")).length - 1);
		}

		if (!(userMap.get("name").equals("null"))) {
			rankingCount
					.setNameCount(doc.split(userMap.get("name")).length - 1);
		}
		if (!(userMap.get("address").equals("null"))) {
			rankingCount
					.setAddressCount(doc.split(userMap.get("address")).length - 1);
		}
		if (!(userMap.get("workplace").equals("null"))) {
			rankingCount
					.setWorkplaceCount(doc.split(userMap.get("workplace")).length - 1);
		}
		if (!(userMap.get("birthday").equals("null"))) {
			rankingCount
					.setBirthdayCount(doc.split(userMap.get("birthday")).length - 1);
		}
		if (!(userMap.get("school").equals("null"))) {
			rankingCount
					.setSchoolCount(doc.split(userMap.get("school")).length - 1);
		}
		if (!(userMap.get("occupation").equals("null"))) {
			rankingCount
					.setOccupationCount(doc.split(userMap.get("occupation")).length - 1);
		}
		if (!(userMap.get("nickname1").equals("null"))) {
			rankingCount
					.setNicknameCount(doc.split(userMap.get("nickname1")).length - 1);
		}
		if (!(userMap.get("nickname2").equals("null"))) {
			rankingCount
					.addNicknameCount(doc.split(userMap.get("nickname2")).length - 1);
		}
		if (!(userMap.get("nickname3").equals("null"))) {
			rankingCount
					.addNicknameCount(doc.split(userMap.get("nickname3")).length - 1);
		}
		if (!(userMap.get("nickname4").equals("null"))) {
			rankingCount
					.addNicknameCount(doc.split(userMap.get("nickname4")).length - 1);
		}
		if (!(userMap.get("nickname5").equals("null"))) {
			rankingCount
					.addNicknameCount(doc.split(userMap.get("nickname5")).length - 1);
		}

		return rankingCount;
	}
}