package com.server.core.util.crypt;

import org.mindrot.jbcrypt.BCrypt;

class BcryptUtil {
	
	// 加密
	public static String encrpty(String password) {
		String cipherData = BCrypt.hashpw(password, BCrypt.gensalt(12));
		return cipherData;
	}
	
	// 密码是否正确
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
