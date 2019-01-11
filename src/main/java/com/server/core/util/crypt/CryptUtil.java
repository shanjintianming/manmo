package com.server.core.util.crypt;

public class CryptUtil {
	
public static class Bcrypt {
		
		/**
		 * 加密过程
		 * @param plainTextData
		 *            明文数据
		 * @return
		 * @throws Exception
		 *             加密过程中的异常信息
		 */
		public static String encrpty(String plainTextData) {
			return BcryptUtil.encrpty(plainTextData);
		}
		
		/**
		 * 验证过程
		 * @param plaintext
		 *            明文数据
		 * @param cipherData
		 *            密文数据
		 * @return 密码是否正确
		 * @throws Exception
		 *             解密过程中的异常信息
		 */
		public static boolean checkpw(String plaintext, String cipherData) {
			return BcryptUtil.checkpw(plaintext, cipherData);
		}
	}
	
	public static class Rsa {
		
		/**
		 * 加密过程
		 * @param plainTextData
		 *            明文数据
		 * @return
		 * @throws Exception
		 *             加密过程中的异常信息
		 */
		public static byte[] encrypt(byte[] plainTextData) throws Exception {
			return RsaUtil.encrypt(plainTextData);
		}
		
		/**
		 * 解密过程
		 * @param cipherData
		 *            密文数据
		 * @return 明文
		 * @throws Exception
		 *             解密过程中的异常信息
		 */
		public static byte[] decrypt(byte[] cipherData) throws Exception {
			return RsaUtil.decrypt(cipherData);
		}
	}
}
