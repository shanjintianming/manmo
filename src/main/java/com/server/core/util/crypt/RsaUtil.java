package com.server.core.util.crypt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.bouncycastle.asn1.ASN1Sequence;

import net.iharder.Base64;

class RsaUtil {

	private static RSAPublicKey publicKey;
	private static RSAPrivateKey privateKey;

	static {
		try {
			// 读取pem证书
			loadPublicKey("E:\\syt-java\\rsa\\rsa_public_key.pem");
			loadPrivateKey("E:\\syt-java\\rsa\\private_key.pem");
			
			// KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
			// keyPairGen.initialize(1024);
			// KeyPair keyPair = keyPairGen.generateKeyPair();
			// publicKey = (RSAPublicKey) keyPair.getPublic();
			// privateKey = (RSAPrivateKey) keyPair.getPrivate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private RsaUtil() {
	}

	/**
	 * 从文件中加载私钥
	 * 
	 * @param keyFileName
	 *            私钥文件名
	 * @return 是否成功
	 * @throws Exception
	 */
	private static String getKeyFromFile(String path) throws Exception {
		BufferedReader br = null;
		String key = new String();
		try {
			br = new BufferedReader(new FileReader(path));
			String readLine = null;
			while ((readLine = br.readLine()) != null) {
				if (readLine.charAt(0) == '-') {
					continue;
				} else {
					key += readLine;
					key += '\r';
				}
			}
		} catch (IOException e) {
			throw new Exception("私钥数据读取错误");
		} catch (NullPointerException e) {
			throw new Exception("私钥输入流为空");
		} finally {
			if (br != null) {
				br.close();
			}
		}

		return key;
	}

	private static void loadPublicKey(String path) throws Exception {
		try {
			String publicKeyStr = getKeyFromFile(path);

			byte[] keybyte = Base64.decode(publicKeyStr);
			KeyFactory kf = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keybyte);
			publicKey = (RSAPublicKey) kf.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("私钥非法");
		} catch (IOException e) {
			throw new Exception("私钥数据内容读取错误");
		} catch (NullPointerException e) {
			throw new Exception("私钥数据为空");
		}
	}

	private static void loadPrivateKey(String path) throws Exception {
		try {
			String privateKeyStr = getKeyFromFile(path);

			byte[] buffer = Base64.decode(privateKeyStr);
			
			// pkcs1
			org.bouncycastle.asn1.pkcs.RSAPrivateKey asn1PrivKey = 
					org.bouncycastle.asn1.pkcs.RSAPrivateKey.getInstance((ASN1Sequence) ASN1Sequence.fromByteArray(buffer)); 
			RSAPrivateKeySpec rsaPrivKeySpec = new RSAPrivateKeySpec(asn1PrivKey.getModulus(), asn1PrivKey.getPrivateExponent()); 
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			privateKey = (RSAPrivateKey) keyFactory.generatePrivate(rsaPrivKeySpec);
			
			// pkcs8
//			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
//			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//			privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);						
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("私钥非法");
		} catch (IOException e) {
			throw new Exception("私钥数据内容读取错误");
		} catch (NullPointerException e) {
			throw new Exception("私钥数据为空");
		}
	}

	/**
	 * 加密过程
	 * 
	 * @param publicKey
	 *            公钥
	 * @param plainTextData
	 *            明文数据
	 * @return
	 * @throws Exception
	 *             加密过程中的异常信息
	 */
	public static byte[] encrypt(byte[] plainTextData) throws Exception {
		if (publicKey == null) {
			throw new Exception("加密公钥为空, 请设置");
		}

		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("RSA");// , new BouncyCastleProvider());
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] output = cipher.doFinal(plainTextData);
			return output;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此加密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("加密公钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("明文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("明文数据已损坏");
		}
	}

	/**
	 * 解密过程
	 * 
	 * @param privateKey
	 *            私钥
	 * @param cipherData
	 *            密文数据
	 * @return 明文
	 * @throws Exception
	 *             解密过程中的异常信息
	 */
	public static byte[] decrypt(byte[] cipherData) throws Exception {
		if (privateKey == null) {
			throw new Exception("解密私钥为空, 请设置");
		}
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("RSA");// , new BouncyCastleProvider());
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] output = cipher.doFinal(cipherData);
			return output;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此解密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("解密私钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("密文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("密文数据已损坏");
		}
	}
}
