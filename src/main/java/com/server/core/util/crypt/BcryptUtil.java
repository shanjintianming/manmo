package com.server.core.util.crypt;

import org.mindrot.jbcrypt.BCrypt;

class BcryptUtil {
	public static String encrpty(String password) {
		String cipherData = BCrypt.hashpw(password, BCrypt.gensalt(12));
		return cipherData;
	}
	/**
	 * 
	 * @param plaintext 密码
	 * @param hashed 
	 * @return
	 */
	public static boolean checkpw(String plaintext, String hashed) {
		boolean result = BCrypt.checkpw(plaintext, hashed);
		return result;
	}
}
