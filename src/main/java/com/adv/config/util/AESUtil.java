package com.adv.config.util;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {
	private static String sKey = "androidlink/astore%#$abc";

	public static String Encrypt(String sSrc) throws Exception {

		if (sKey == null) {
			System.out.println("key is null");
			return null;
		}
		// check key size
		if (sKey.length() < 24) {
			System.out.println(" key size must be length greater then 24");
			return null;
		}
		byte[] raw = sKey.getBytes();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] result = cipher.doFinal(sSrc.getBytes());
		return Base64.encodeBase64String(result);
	}

	public static String Decrypt(String sSrc) throws Exception {

		if (sKey == null) {
			System.out.println("key is null");
			return null;
		}
		// check key size
		if (sKey.length() < 24) {
			System.out.println(" key size must be length greater then 24");
			return null;
		}
		byte[] raw = sKey.getBytes();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] result = cipher.doFinal(Base64.decodeBase64(sSrc));
		return new String(result);
	}
}
