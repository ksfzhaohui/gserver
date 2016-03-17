package org.gserver.core.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SessionUtil {

	private static final int SESSION_ID_BYTES = 16;

	/**
	 * 生成sessionId
	 * 
	 * @return
	 */
	public static synchronized String generateSessionId() {
		Random random = new SecureRandom();
		byte bytes[] = new byte[SESSION_ID_BYTES];
		random.nextBytes(bytes);
		bytes = getDigest().digest(bytes);

		StringBuffer result = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			byte b1 = (byte) ((bytes[i] & 0xf0) >> 4);
			byte b2 = (byte) (bytes[i] & 0x0f);
			if (b1 < 10) {
				result.append((char) ('0' + b1));
			} else {
				result.append((char) ('A' + (b1 - 10)));
			}

			if (b2 < 10) {
				result.append((char) ('0' + b2));
			} else {
				result.append((char) ('A' + (b2 - 10)));
			}
		}

		return (result.toString());
	}

	private static MessageDigest getDigest() {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			return md;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		String sid = SessionUtil.generateSessionId();
		System.out.println("jsessionid=" + sid);
		System.out.println("sid.lengh=" + sid.length());
		String s = "CAB541D43210B989EE5696CDC5DEA456";
		System.out.println(s.length());
		Map<String, Integer> v = new HashMap<String, Integer>();
		for (int i = 0; i < 100000; i++) {
			String xx = SessionUtil.generateSessionId();
			if (v.containsKey(xx)) {
				System.err.println("xxxxxxxxxxxxxxxxxxx");
			} else {
				v.put(xx, 0);
			}
		}
	}

}
