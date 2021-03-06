package com.geurimsoft.socket.client;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES 
{

	public static byte[] hexToByteArray(String hex) 
	{
	
		if (hex == null || hex.length() == 0)
			return null;
		
		byte[] ba = new byte[hex.length() / 2];
		for (int i = 0; i < ba.length; i++) 
		{
			ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		
		return ba;
		
	}

	public static String byteArrayToHex(byte[] ba) 
	{
	
		if (ba == null || ba.length == 0)
			return null;
		
		StringBuffer sb = new StringBuffer(ba.length * 2);
		String hexNumber;
		for (int x = 0; x < ba.length; x++) 
		{
			hexNumber = "0" + Integer.toHexString(0xff & ba[x]);
			sb.append(hexNumber.substring(hexNumber.length() - 2));
		}
		
		return sb.toString();
		
	}

	public static String makeKey(String keyStr) 
	{
	
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 16; i++) 
		{
			if (keyStr.length() > i)
				sb.append(keyStr.substring(i,i+1));
			else
				sb.append(" ");
		}
		
		return sb.toString();
	}

	public static String encrypt(String message, String keyStr) throws Exception 
	{
	
		String key = makeKey(keyStr);
		SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(message.getBytes());
		
		return byteArrayToHex(encrypted);
		
	}


	public static String decrypt(String encrypted, String keyStr) 
	{
	
		String key = makeKey(keyStr);
		String originalString;
		
		try 
		{
			
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(),"AES");
			Cipher cipher;
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			byte[] original = cipher.doFinal(hexToByteArray(encrypted));
			
			originalString = new String(original);
			
		}
		catch (Exception e) 
		{
			originalString = "ERR";
		}
		
		return originalString;
		
	}
	
}
