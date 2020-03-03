package com.wintranx.client.demo.util;


import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;


public class RSAEncryptCoder {
	/**
	 * 加密方法名
	 */
	public static final String KEY_ALGORITHM = "RSA";

	public static final String SIGNATURE_ALGORITHM = "SHA1WithRSA";

	/**
	 * Map的Key常量
	 */
	private static String PUBLIC_KEY = "RSAPublicKey";
	private static String PRIVATE_KEY = "RSAPrivateKey";

	/**
	 * 用私钥对信息生成数字签名
	 *
	 * @param data
	 *            加密数据
	 * @param privateKey
	 *            私钥
	 * @return 加密后字符串
	 * @throws Exception
	 */
	public static String sign(String privateKey, String content, String charset) throws Exception {
		// 解密由base64编码的私钥
		byte[] keyBytes = Base64.decodeBase64(privateKey);

		// 构造PKCS8EncodedKeySpec对象
		PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(keyBytes);

		// KEY_ALGORITHM 指定的加密算法
		KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);

		// 取私钥匙对象
		PrivateKey myPriKey = factory.generatePrivate(priPKCS8);

		// 用私钥对信息生成数字签名
		Signature signet = Signature.getInstance(SIGNATURE_ALGORITHM);
		signet.initSign(myPriKey);
		signet.update(content.trim().getBytes(charset));

		return Base64.encodeBase64String(signet.sign());
	}

	/**
	 * 生成16进制签名
	 *
	 * @param privateKey
	 * @param content
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	public static String sign(PrivateKey privateKey, String content, String charset) throws Exception {
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initSign(privateKey);
		signature.update(content.trim().getBytes(charset));

		return ByteUtils.toHexAscii(signature.sign());
	}

	/**
	 * 转成16进制签名
	 *
	 * @param privateKey
	 * @param content
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	public static String signToHexAscii(String privateKey, String content, String charset) throws Exception {
		return HexUtil.DecimalToHex(sign(privateKey, content, charset));
	}

	/**
	 * 校验数字签名
	 *
	 * @param data
	 *            加密数据
	 * @param publicKey
	 *            公钥
	 * @param sign
	 *            数字签名
	 * @return 校验成功返回true 失败返回false
	 * @throws Exception
	 */
	public static boolean verify(String sign, String publicKey, String content, String charset) throws Exception {
		// 解密由base64编码的公钥
		byte[] keyBytes = Base64.decodeBase64(publicKey);

		// 构造X509EncodedKeySpec对象
		X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(keyBytes);

		// KEY_ALGORITHM 指定的加密算法
		KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);

		// 取公钥匙对象
		PublicKey pubKey = factory.generatePublic(bobPubKeySpec);

		// 用公钥解密签名信息
		Signature signetcheck = Signature.getInstance(SIGNATURE_ALGORITHM);
		signetcheck.initVerify(pubKey);
		signetcheck.update(content.trim().getBytes(charset));
		byte[] signByte = Base64.decodeBase64(sign);
		return signetcheck.verify(signByte);
	}

	/**
	 * 验证16进制签名
	 *
	 * @param sign
	 * @param publicKey
	 * @param content
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	public static boolean verify(String sign, PublicKey publicKey, String content, String charset) throws Exception {
		Signature verifier = Signature.getInstance(SIGNATURE_ALGORITHM);
		verifier.initVerify(publicKey);
		verifier.update(content.trim().getBytes(charset));

		return verifier.verify(ByteUtils.fromHexAscii(sign));
	}

	/**
	 * Verify Sign By Hexadecimal
	 *
	 * @param sign
	 * @param publicKey
	 * @param content
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	public static boolean verifyFromHexAscii(String sign, String publicKey, String content, String charset)
			throws Exception {
		String decSign = new String(HexUtil.HexToDecimal(sign), charset);
		System.out.println(decSign);
		return verify(decSign, publicKey, content, charset);
	}

	/**
	 * 解密<br>
	 * 用私钥解密
	 *
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPrivateKey(byte[] data, String key) throws Exception {
		// 对密钥解密
		byte[] keyBytes = Base64.decodeBase64(key);

		// 取得私钥
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

		// 对数据解密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);

		return cipher.doFinal(data);
	}

	/**
	 * 解密<br>
	 * 用公钥解密
	 *
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPublicKey(byte[] data, String key) throws Exception {
		// 对密钥解密
		byte[] keyBytes = Base64.decodeBase64(key);

		// 取得公钥
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicKey = keyFactory.generatePublic(x509KeySpec);

		// 对数据解密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicKey);

		return cipher.doFinal(data);
	}

	/**
	 * 加密<br>
	 * 用公钥加密
	 *
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPublicKey(byte[] data, String key) throws Exception {
		// 对公钥解密
		byte[] keyBytes = Base64.decodeBase64(key);

		// 取得公钥
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicKey = keyFactory.generatePublic(x509KeySpec);

		// 对数据加密
		// Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);

		return cipher.doFinal(data);
	}

	/**
	 * 解密
	 *
	 * @param content
	 *            密文
	 * @param privateKey
	 *            商户私钥
	 * @param inputCharset
	 *            编码格式
	 * @return 解密后的字符串
	 * @throws Exception
	 */
	public static String decryptByPrivateKey(String content, String privateKey, String inputCharset) throws Exception {
		PrivateKey prikey = getPrivateKey(privateKey);

		Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, prikey);

		InputStream ins = new ByteArrayInputStream(Base64.decodeBase64(content));
		ByteArrayOutputStream writer = new ByteArrayOutputStream();
		// rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
		byte[] buf = new byte[128];
		int bufl;

		while ((bufl = ins.read(buf)) != -1) {
			byte[] block = null;

			if (buf.length == bufl) {
				block = buf;
			} else {
				block = new byte[bufl];
				for (int i = 0; i < bufl; i++) {
					block[i] = buf[i];
				}
			}

			writer.write(cipher.doFinal(block));
		}

		return new String(writer.toByteArray(), inputCharset);
	}

	/**
	 * 加密<br>
	 * 用私钥加密
	 *
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPrivateKey(byte[] data, String key) throws Exception {
		// 对密钥解密
		byte[] keyBytes = Base64.decodeBase64(key);

		// 取得私钥
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

		// 对数据加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);

		return cipher.doFinal(data);
	}

	/**
	 * 取得私钥
	 *
	 * @param keyMap
	 * @return
	 * @throws Exception
	 */
	public static String getPrivateKey(Map keyMap) throws Exception {
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		return Base64.encodeBase64String(key.getEncoded());
	}

	/**
	 * 得到私钥
	 *
	 * @param key
	 *            密钥字符串（经过base64编码）
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey(String key) throws Exception {
		byte[] keyBytes;
		keyBytes = Base64.decodeBase64(key);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}

	/**
	 *
	 * 功能:根据模指来获取私钥 作者:Xinwen.wen 创建时间:2015-3-13 下午3:23:44
	 *
	 * @param modulus
	 *            模
	 * @param exponent
	 *            指
	 * @return
	 * @throws Exception
	 */

	public static PrivateKey getPrivateKey(String modulus, String exponent) throws Exception {

		BigInteger mod = new BigInteger(modulus, 16);
		BigInteger exp = new BigInteger(exponent, 16);
		RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(mod, exp);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

		return privateKey;
	}

	/**
	 * 取得公钥
	 *
	 * @param keyMap
	 * @return
	 * @throws Exception
	 */
	public static String getPublicKey(Map keyMap) throws Exception {
		Key key = (Key) keyMap.get(PUBLIC_KEY);
		return Base64.encodeBase64String(key.getEncoded());
	}

	/**
	 *
	 * 功能:根据模指来获取公钥 作者:Xinwen.wen 创建时间:2015-3-13 下午3:23:44
	 *
	 * @param modulus
	 *            模
	 * @param exponent
	 *            指
	 * @return
	 * @throws Exception
	 */

	public static PublicKey getPublicKey(String modulus, String exponent) throws Exception {

		BigInteger mod = new BigInteger(modulus, 16);
		BigInteger exp = new BigInteger(exponent, 16);
		RSAPublicKeySpec keySpec = new RSAPublicKeySpec(mod, exp);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PublicKey publicKey = keyFactory.generatePublic(keySpec);

		return publicKey;
	}

	/**
	 * 生成密钥密钥
	 *
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> generateKey(String seed) throws Exception {
		KeyPairGenerator keygen = KeyPairGenerator.getInstance(KEY_ALGORITHM);

		SecureRandom secrand = new SecureRandom();
		secrand.setSeed(seed.getBytes()); // 随机产生器
		keygen.initialize(1024, secrand);

		KeyPair keys = keygen.genKeyPair();

		PublicKey publicKey = keys.getPublic();
		System.out.println("pmod:" + ((RSAPublicKey) publicKey).getModulus().toString(16));
		System.out.println("pexp:" + ((RSAPublicKey) publicKey).getPublicExponent().toString(16));

		PrivateKey privateKey = keys.getPrivate();

		System.out.println("primod:" + ((RSAPrivateKey) privateKey).getModulus().toString(16));
		System.out.println("priexp:" + ((RSAPrivateKey) privateKey).getPrivateExponent().toString(16));

		Map<String, Object> map = new HashMap<String, Object>();
		map.put(PUBLIC_KEY, publicKey);
		map.put(PRIVATE_KEY, privateKey);

		return map;
	}

	/**
	 * 公钥加密
	 *
	 * @param data
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static String encryptByPublicKey(String data, PublicKey publicKey) throws Exception {
		Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		// 模长
		int key_len = ((RSAPublicKey) publicKey).getModulus().bitLength() / 8;
		// 加密数据长度 <= 模长-11
		String[] datas = splitString(data, key_len - 11);
		String mi = "";
		// 如果明文长度大于模长-11则要分组加密
		for (String s : datas) {
			mi += bcd2Str(cipher.doFinal(s.getBytes()));
		}
		return mi;
	}

	/**
	 * 私钥解密
	 *
	 * @param data
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static String decryptByPrivateKey(String data, PrivateKey privateKey) throws Exception {
		Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		// 模长
		int key_len = ((RSAPrivateKey) privateKey).getModulus().bitLength() / 8;
		byte[] bytes = data.getBytes();
		byte[] bcd = ASCII_To_BCD(bytes, bytes.length);
		System.err.println(bcd.length);
		// 如果密文长度大于模长则要分组解密
		String ming = "";
		byte[][] arrays = splitArray(bcd, key_len);
		for (byte[] arr : arrays) {
			ming += new String(cipher.doFinal(arr));
		}
		return ming;
	}

	/**
	 * ASCII码转BCD码
	 *
	 */
	public static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {
		byte[] bcd = new byte[asc_len / 2];
		int j = 0;
		for (int i = 0; i < (asc_len + 1) / 2; i++) {
			bcd[i] = asc_to_bcd(ascii[j++]);
			bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));
		}
		return bcd;
	}

	public static byte asc_to_bcd(byte asc) {
		byte bcd;

		if ((asc >= '0') && (asc <= '9'))
			bcd = (byte) (asc - '0');
		else if ((asc >= 'A') && (asc <= 'F'))
			bcd = (byte) (asc - 'A' + 10);
		else if ((asc >= 'a') && (asc <= 'f'))
			bcd = (byte) (asc - 'a' + 10);
		else
			bcd = (byte) (asc - 48);
		return bcd;
	}

	/**
	 * BCD转字符串
	 */
	public static String bcd2Str(byte[] bytes) {
		char temp[] = new char[bytes.length * 2], val;

		for (int i = 0; i < bytes.length; i++) {
			val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
			temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

			val = (char) (bytes[i] & 0x0f);
			temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
		}
		return new String(temp);
	}

	/**
	 * 拆分字符串
	 */
	public static String[] splitString(String string, int len) {
		int x = string.length() / len;
		int y = string.length() % len;
		int z = 0;
		if (y != 0) {
			z = 1;
		}
		String[] strings = new String[x + z];
		String str = "";
		for (int i = 0; i < x + z; i++) {
			if (i == x + z - 1 && y != 0) {
				str = string.substring(i * len, i * len + y);
			} else {
				str = string.substring(i * len, i * len + len);
			}
			strings[i] = str;
		}
		return strings;
	}

	/**
	 * 拆分数组
	 */
	public static byte[][] splitArray(byte[] data, int len) {
		int x = data.length / len;
		int y = data.length % len;
		int z = 0;
		if (y != 0) {
			z = 1;
		}
		byte[][] arrays = new byte[x + z][];
		byte[] arr;
		for (int i = 0; i < x + z; i++) {
			arr = new byte[len];
			if (i == x + z - 1 && y != 0) {
				System.arraycopy(data, i * len, arr, 0, y);
			} else {
				System.arraycopy(data, i * len, arr, 0, len);
			}
			arrays[i] = arr;
		}
		return arrays;
	}

	/**
	 *
	 * AES加密
	 */

	public static String encryptToBase64(String data, String key) {
		try {
			byte[] valueByte = encrypt(data.getBytes("UTF-8"), key.getBytes("UTF-8"));
			return Base64.encodeBase64String(valueByte);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("encrypt fail!", e);
		}
	}

	public static byte[] encrypt(byte[] data, byte[] key) {

		if (key.length != 16) {
			throw new RuntimeException("Invalid AES key length (must be 16 bytes)");
		}
		try {
			SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec seckey = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");// 创建密码器
			cipher.init(Cipher.ENCRYPT_MODE, seckey);// 初始化
			byte[] result = cipher.doFinal(data);
			return result;
		} catch (Exception e) {
			throw new RuntimeException("encrypt fail!", e);
		}
	}

	/**
	 * @throws Exception
	 *
	 *
	 */
	public static String decryptFromBase64(String data, String key) throws Exception {
		try {
			byte[] originalData = Base64.decodeBase64(data);
			byte[] valueByte = decrypt(originalData, key.getBytes("UTF-8"));
			return new String(valueByte, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("decrypt fail!", e);
		}
	}

	public static byte[] decrypt(byte[] data, byte[] key) {
		if (key.length != 16) {
			throw new RuntimeException("Invalid AES key length (must be 16 bytes)");
		}
		try {
			SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec seckey = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, seckey);// 初始化
			byte[] result = cipher.doFinal(data);
			return result; // 加密
		} catch (Exception e) {
			throw new RuntimeException("decrypt fail!", e);
		}
	}

	public static byte[] generateDesKey(int length) throws Exception {
		//实例化
		KeyGenerator kgen = null;
		kgen = KeyGenerator.getInstance("AES");
		//设置密钥长度
		kgen.init(length);
		//生成密钥
		SecretKey skey = kgen.generateKey();
		//返回密钥的二进制编码
		return skey.getEncoded();
	}

	/**
	 * 解码PublicKey
	 * @param key
	 * @return
	 */
	public static PublicKey getPublicKey(String key) {
		try {
			byte[] byteKey = Base64.decodeBase64(key);
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(byteKey);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			return keyFactory.generatePublic(x509EncodedKeySpec);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}




	//public static void main(String[] args) throws Exception {
	//	//		String PW_KEY_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDmwwFxxkClvsBzZUtE0CN4S7P0QWZxnpxn2De0zlqbjY6Put/8738SXYkGsuBIb5QZU3tDb/0hmON3zQ84BLexksP2iNqY1q1VSeY2NkV/QxrCUefUedTFsDU+ZcIB5JJ02m4fqpYtzYowtf5JrgjYHcyrO1IaX3NVITm9EPOMHQIDAQAB";
	//	//		String PW_KEY_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAObDAXHGQKW+wHNlS0TQI3hLs/RBZnGenGfYN7TOWpuNjo+63/zvfxJdiQay4EhvlBlTe0Nv/SGY43fNDzgEt7GSw/aI2pjWrVVJ5jY2RX9DGsJR59R51MWwNT5lwgHkknTabh+qli3NijC1/kmuCNgdzKs7Uhpfc1UhOb0Q84wdAgMBAAECgYBwAYL3+GR0jqvW1vm7wtMUlpoYGo5g473C4b3YMrjj+8eQmAIUQSMKhP/3kmcugYn+6PhReVACOLVf9tZGOHP3/uMztwy/T6U0+xuEulagzb6VzUNmwUgx6zd9bXFfjjVm3l61UL5bDoaeKq0/Tw0s6KNIUyn8O9cEUH99VU2MwQJBAPjiDWJEHETQnvRsyilOE41KlXTRa/HVnQpE+n4JgvnJyk0WGdX4NQE5w5GyiW7QfXr3iZ7JiJDiwJD3adnTwWUCQQDtXEvtXUexUyeMLdrOLs//qiEEvMGAhU5AjqwyaezeiyPqH0GZCK/k6SruJ7aOFaLf3Yqhpj0QrOAF/+6k0xBZAkAPFYRCJGaiNRKtNbq67iR5N7//KIImUhAGg0+7O9BYW0aLjX9bXIO540pbFjsGIFS6ky4gFY7VWSjaoGQl85ORAkEA60y72CrIj3knk0dByXOOrpww70lPaeC8mjLVcYSgVF0K1WT22oZKGFstO7oStver5tDwRR9PGeDGlCwKn0q8wQJBAJ9G8E758G3jIBiJ1idouwj59dyXn1eHzoL7GR6ms5bVdeHDZEA+g7GLVsGOm4R6ENmIUldTSHE3NK7H8KnyCEI=";
	//
	//	//加密内容
	//	String data="1234567894545";
	//	//生成秘钥对随机变量
//	//	String seed="wintranx-test";
	//	String seed="0";
	//
	//	Map<String,Object> keyMap = RSAEncryptCoder.generateKey(seed);
	//	String PW_KEY_PRIVATE =RSAEncryptCoder.getPrivateKey(keyMap);
	//	String PW_KEY_PUBLIC = RSAEncryptCoder.getPublicKey(keyMap);
	//	//生成私钥
	//	System.out.println(PW_KEY_PRIVATE);
	//	//生成公钥
	//	System.out.println(PW_KEY_PUBLIC);
	//
	//	String sign =RSAEncryptCoder.sign(PW_KEY_PRIVATE, data, "utf-8");
	//	//生成签名
	//	System.out.println(sign);
	//	boolean serverSign = RSAEncryptCoder.verify(sign, PW_KEY_PUBLIC, data, "utf-8");
	//	//验证签名
	//	System.out.println(serverSign);
	//	String encode = RSAEncryptCoder.encryptByPublicKey(data, getPublicKey(PW_KEY_PUBLIC));
	//	//ras 加密
	//	System.out.println("加密内容"+encode);
	//	String decode = RSAEncryptCoder.decryptByPrivateKey(encode, getPrivateKey(PW_KEY_PRIVATE));
	//	//rsa 解密
	//	System.out.println(decode);
	//
	//}
}
