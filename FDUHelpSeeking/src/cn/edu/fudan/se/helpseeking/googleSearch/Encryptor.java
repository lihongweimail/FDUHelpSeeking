package cn.edu.fudan.se.helpseeking.googleSearch;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Encryptor {
	public static String encryptSHA(String password) {
		MessageDigest md = null;
		String salt = "<^오^>";
		try {
			md = MessageDigest.getInstance("SHA-1");

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		byte[] bytes = (password + salt).getBytes();
		md.update(bytes);
		byte[] digest = md.digest();
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < digest.length; i++) {
			buffer.append(Integer.toHexString(0xFF & digest[i]));
		}
		return buffer.toString();
	}

	public static String getRandomString() {
		StringBuffer buffer = new StringBuffer();
		Random random = new Random();

		String chars[] = "a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z"
				.split(",");

		for (int i = 0; i < 6; i++) {
			buffer.append(chars[random.nextInt(chars.length)]);
		}
		return buffer.toString();
	}
}