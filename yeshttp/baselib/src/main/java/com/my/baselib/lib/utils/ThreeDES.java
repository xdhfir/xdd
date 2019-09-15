package com.my.baselib.lib.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 使用3DES加密与解密
 */
public class ThreeDES {
	private static final String Algorithm = "DESede";
	/*
	 * 根据字符串生成密钥字节数组
	 * 
	 * @param keyStr 密钥字符串
	 */
	public static byte[] build3DesKey(String keyStr)
			throws UnsupportedEncodingException {
		byte[] key = "000000000000000000000000".getBytes();
		byte[] temp = keyStr.getBytes("UTF-8"); // 将字符串转成字节数组
		/*
		 * 执行数组拷贝 System.arraycopy(源数组，从源数组哪里开始拷贝，目标数组，拷贝多少位)
		 */
		if (key.length > temp.length) {
			// 如果temp不够24位，则拷贝temp数组整个长度的内容到key数组中
			System.arraycopy(temp, 0, key, 0, temp.length);
		} else {
			// 如果temp大于24位，则拷贝temp数组24个长度的内容到key数组中
			System.arraycopy(temp, 0, key, 0, key.length);
		}
		return key;
	}

	/**
	 * 加密字符串
	 * @param key 密钥
	 * @param src 源数据的字节串
	 * @return
	 */
	public static String encryptString(String key, String src) {
		if(src == null) {
			return "";
		}
		byte[] temp = null;
		try {
			temp = encryptByte(key, src.getBytes("UTF8"));
			return new String(Base64.encode(temp, Base64.DEFAULT));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			temp = null;
		}
		return "";
	}

	/**
	 * 解密字符串
	 * @param key 密钥
	 * @param src 密文的字符串
	 * @return
	 */
	public static String decryptString(String key, String src) {
		if(src == null) {
			return "";
		}
		byte[] temp = null;
		byte[] res = null;
		try {
			temp = Base64.decode(src, Base64.DEFAULT);
			res = decryptByte(key, temp);
			if(res == null) {
				return "";
			} else {
				return new String(res, "UTF8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			temp = null;
			res = null;
		}
		return "";
	}

	/**
	 * 加密字节数组
	 * @param key 密钥
	 * @param src 源数据的字节数组
	 * @return
	 */
	public static byte[] encryptByte(String key, byte[] src) {
		try {
			SecretKey deskey = new SecretKeySpec(build3DesKey(key), Algorithm); // 生成密钥
			Cipher c1 = Cipher.getInstance(Algorithm); // 实例化负责加密/解密的Cipher工具类
			c1.init(Cipher.ENCRYPT_MODE, deskey); // 初始化为加密模式
			return c1.doFinal(src);
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密字节数组
	 * @param key 密钥
	 * @param src 密文的字节数组
	 * @return
	 */
	public static byte[] decryptByte(String key, byte[] src) {
		try {
			SecretKey deskey = new SecretKeySpec(build3DesKey(key), Algorithm);
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.DECRYPT_MODE, deskey); // 初始化为解密模式
			return c1.doFinal(src);
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

}