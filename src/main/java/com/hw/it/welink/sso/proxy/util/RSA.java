package com.hw.it.welink.sso.proxy.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import it.sauronsoftware.base64.Base64;

public class RSA {


	private static final String ENCODING = "UTF-8";
	//公共公私钥
	private static final String pubkey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCuSFOb7cwjUsTejDW5es8UJqoC0iHvv1BWB3yoRDZ64k+QwE7Vq6/nph5746k4ovXwzpeCaBft7v9Lc5n2bQJZrD2bO62Dlv8cYHmXqltraxHmYMi/f3mhEPvQ4jM73HsXUB5SDxK1S9ohScj+MII5crdmg55y5GqCgOOD894gnQIDAQAB";
	private static final String prikey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAK5IU5vtzCNSxN6MNbl6zxQmqgLSIe+/UFYHfKhENnriT5DATtWrr+emHnvjqTii9fDOl4JoF+3u/0tzmfZtAlmsPZs7rYOW/xxgeZeqW2trEeZgyL9/eaEQ+9DiMzvcexdQHlIPErVL2iFJyP4wgjlyt2aDnnLkaoKA44Pz3iCdAgMBAAECgYAPyygggk00ikSi8kvQEiv/5H0VoltG8axjD2yhhwT039tb/8FlJZjcQjaS/Y+RPVtSRjZ+r9YzTqw99yzlov1KYjc/N4AhcqO2nv3fmP+TYgCUF8eHN7C0NY5iAj3mJnEvMukDiQ2VxvWBdlAuKBnx2qjFNjsVbth9wJ5BOQXQwQJBAPoskCwkOag80H7h+SsF1LzqWGAw13i+WmTXK4VHkGdOnjxBpytiH3mUp7ZtxDw2yK2atjQc5WxCbqhlmGRSdzECQQCyV1QiZriMu0JTG/gIJE2iu77IGWTDMIaV9jKXPthmwyPa6UcvVWz6QrKyiV8ZOzEfAIDwMDpoxXiItFNNYL0tAkBZ73tE4yD3IiWx93qjxMzdvUFYEzbi6UuFxPSYTUnUWab48oEuT3ARPSetpXhOTUOHZX7q7Rhs+tTdJOjL5/mBAkACoECP5R1QjS1T1fSYAhjWjZcokR8ntJ42tRJXochD8xmjDo5KxyI8qSDCej1ZPMLjZvl/D+a8RjQZJzaWzBbdAkAthEc/wq02dB83YR+LSwULujERR2TphDMwmlVw7Oy+w0Rfljw3wjkwISviBvVCfItWmnxhrhWcGnc9CiRK15KY";
	//动态公私钥，前期先保持所有用户的动态公私钥一致
	private static final String dynamicPublicRsaKey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCRBD1wjArlqhsFr76TKkRHV8LZA5G50iDVCd23L56kcMUUH8dQoo9OpeZZhAufc1fIfDOKauKLQMidNF6g2hzA4rATJo5EMovTURs7c6TBUfLvO3KNaasnJKEVP5SQZzAJNEDV34goYjxaag56yKZtG2Wwp28o1gNmGRneXduJdQIDAQAB";
	private static final String dynamicPrivateRsaKey="MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAJEEPXCMCuWqGwWvvpMqREdXwtkDkbnSINUJ3bcvnqRwxRQfx1Cij06l5lmEC59zV8h8M4pq4otAyJ00XqDaHMDisBMmjkQyi9NRGztzpMFR8u87co1pqyckoRU/lJBnMAk0QNXfiChiPFpqDnrIpm0bZbCnbyjWA2YZGd5d24l1AgMBAAECgYB5NRsJS/P7u+80FI1sQeKp4r+YVXRi5S/OAJ4Rc7rcZOlEhb85NXYkXWOoJEb2shiIGM+XfPj3PWGwi8ogFc37YuYtAVAgVm/lvP8JgS4xLP9sl5HLqz7HnDy+xNWoBDyzE7Zb65medmdThdFpNAkJ4HtRYS2uxGweMdOO62Qv4QJBANJtgXVPCPqOG2ChZG8Y6Y9PzfHGWWnZ7Au6hJWo5cUbugXXRqSZfM8udHPrZf1TuU5bAaIma4XBnOVMHT4XKakCQQCwbDhA4HuHLXxIXxO/sW4YrGd3CoglNmdAmuC5AS8ak84w4L6iNslfXc5Ye0/hDu/lHS6YajAeDgnDJBWZBTjtAkEApoUNy11OB6jpNWDCb8BRI53NpaSHIWLYjrd9MEnF+mNkpDalqp/jyrhCY3FG9l+I5t7RvhWVRPXgSPwgr//4cQJBAKulrxmYDzeXDquPLbS9GYQSAVx1t76U7uKRjYESYNT3543wrClBKq2JpCC8YRx0Hv2IZfROpGApgmAFNKo7xYUCQQCq23jEqcYfLYUuisDKpDQO5AiNEPi4YlHaoEmj8dO7KTEzSgHoZqA35aWxbqRr3DsX+coFGJ9rHlsfK8Bqy4Li";
	
	private static final Logger logger = LoggerFactory.getLogger(RSA.class);
	private KeyPair keyPair = null;

	private KeyPair generateKey() throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
		keyPairGen.initialize(1024);//, new SecureRandom());

		return keyPairGen.generateKeyPair();
	}

	public KeyPair getKeyPair() {
		if (keyPair == null) {
			try {
				keyPair = generateKey();
			} catch (NoSuchAlgorithmException e) {
				//logger.error(e.getMessage(), e);
			}
		}
		return keyPair;
	}
  
	public void setKeyPair(KeyPair keyPair) {
		this.keyPair = keyPair;
	}

	public String getPrivateKeyToString() {
		byte[] b1 = getKeyPair().getPrivate().getEncoded();
		// byte[] b2 = GZip.gzipCompress(b1);
		try {
			return new String(Base64.encode(b1), ENCODING);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	

	/**
	 * 通过字符串生成 PrivateKey 对象
	 * 
	 * @param key
	 *            私钥字串
	 * @return PrivateKey 私钥对象
	 * @throws NoSuchAlgorithmException
	 * @throws RuntimeException
	 * @throws UnsupportedEncodingException
	 * @throws InvalidKeySpecException
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKeyByString(String key) throws NoSuchAlgorithmException, UnsupportedEncodingException, RuntimeException, InvalidKeySpecException {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA"); // , new
																// BouncyCastleProvider());
		byte[] b1 = Base64.decode(key.getBytes(ENCODING));
		// byte[] b2 = GZip.gzipDecompress2Byte(b1);
		PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(b1);
		PrivateKey privateKey = keyFactory.generatePrivate(priPKCS8);
		return privateKey;
	}

	/**
	 * 取公钥字串
	 * 
	 * @return
	 */
	public String getPublicKeyToString() {
		byte[] b1 = getKeyPair().getPublic().getEncoded();
		try {
			return new String(Base64.encode(b1), ENCODING);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/**
	 * 通知字符串生成公钥对象
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static PublicKey getPublicKeyByString(String key) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA"); // , new
																// BouncyCastleProvider());
		byte[] b1 = Base64.decode(key.getBytes(ENCODING));
		X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(b1);
		PublicKey publicKey = keyFactory.generatePublic(bobPubKeySpec);
		return publicKey;
	}

	/**
	 * Encrypt String.
	 * 
	 * @return byte[]
	 */
	public static byte[] encrypt(PublicKey publicKey, byte[] data) {
		if (publicKey != null) {
			try {
				Cipher cipher = Cipher.getInstance("RSA"); // , new // BouncyCastleProvider());
				cipher.init(Cipher.ENCRYPT_MODE, publicKey);
				return cipher.doFinal(data);
			} catch (Exception e) {
				//logger.error(e.getMessage(), e);
			}
		}
		return null;
	}

	/**
	 * 加密字串
	 * 
	 * @param publicKey
	 *            公钥字串
	 * @param data
	 *            需要加密的字串
	 * @return 加密后的字串
	 * @throws Exception
	 */
	public static String encrypt(String publicKey, String data) throws Exception {
		PublicKey key = getPublicKeyByString(publicKey);
		byte[] e = data.getBytes(ENCODING);
		byte[] en = encrypt(key, e);
		byte[] b = Base64.encode(en);
		return (new String(b, ENCODING));
	}

	/**
	 * Basic decrypt method
	 * 
	 * @return byte[]
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 */
	public static byte[] decrypt(PrivateKey privateKey, byte[] raw) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

		if (privateKey != null) {
			//Cipher cipher = Cipher.getInstance("RSA");
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return cipher.doFinal(raw);
		}

		return null;
	}

	/**
	 * 解密
	 * 
	 * @param privateKey
	 *            私钥字串
	 * @param hieroglyph
	 *            需解密的字串
	 * @return 原文
	 * @throws RuntimeException
	 * @throws InvalidKeySpecException
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 * @throws Exception
	 */
	public static String decrypt(String privateKey, String hieroglyph) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException,
			UnsupportedEncodingException, InvalidKeySpecException {

		PrivateKey key;
		key = getPrivateKeyByString(privateKey);
		byte[] e = Base64.decode(hieroglyph.getBytes(ENCODING));
		byte[] de = decrypt(key, e);
		return (new String(de, ENCODING));
	}
/*
 * 解密成功返回解密后的信息，返回""表示解密失败
 */
	
public static String decryptInfo(String key,String encryptInfo){
		
		String result = "";
		
		
		
		try {

			//动态私钥解密
			if(key.equals("1")){
				result=RSA.decrypt(dynamicPrivateRsaKey,encryptInfo.trim());
			}else{
				result=RSA.decrypt(prikey,encryptInfo.trim());
			}
			
			logger.info(encryptInfo+" 经解密后："+result);
		
			
		} catch (Exception e) {
			logger.error("sso login result:" + result);
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		return result;
	}
	
//	public static void main(String ...arg) throws Exception{
//
////
////				RSA rsa = new RSA();
////				rsa.setKeyPair(rsa.getKeyPair());
////				String pubkey=rsa.getPublicKeyToString();
////				String prikey=rsa.getPrivateKeyToString();
////				logger.error("pubkey:" + pubkey);
////				logger.error("prikey:" + prikey);
//
//		
//		    String username="test1";
//		    String password="Huawei#123";
//		
//			String eusername=encrypt(dynamicPublicRsaKey,username);
//			String epassword=encrypt(dynamicPublicRsaKey,password);
//			logger.info(username+" 加密后："+eusername);
//			logger.info(password+" 加密后："+epassword);
//			logger.info(decrypt(dynamicPrivateRsaKey,eusername));
//			logger.info(decrypt(dynamicPrivateRsaKey,epassword));
//		    
//		
//	}
}

