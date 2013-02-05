package com.belmen.anwei.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	
	public final static String getMD5String(byte[] input) {
		try {
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(input);
			byte[] md = mdInst.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < md.length; i++) {
				int val = ((int) md[i]) & 0xff;
				if (val < 16) sb.append("0");
				sb.append(Integer.toHexString(val));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
}
