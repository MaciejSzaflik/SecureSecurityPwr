package noob.pwr;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class CipherAES {
	public SecretKey key;
	public Cipher cipherEncypt;
	public Cipher cipherDecrypt;
	
	public CipherAES(SecretKey key)
	{
		this.key = key;
		try {
			cipherEncypt = Cipher.getInstance("AES");
			cipherDecrypt = Cipher.getInstance("AES");
			cipherEncypt.init(Cipher.ENCRYPT_MODE, key);
			cipherDecrypt.init(Cipher.DECRYPT_MODE,key);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String Decrypt(String toDecrypt)
	{
		byte[] byteDecryptedText;
		try {
			byteDecryptedText = cipherDecrypt.doFinal(new BASE64Decoder().decodeBuffer(toDecrypt));
	        return new String(byteDecryptedText);
		} catch (IllegalBlockSizeException | BadPaddingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
       return "";
	}
	
	public String Encrypt(String toEncrypt)
	{
		byte[] byteCipherText;
		try {
			byteCipherText = cipherEncypt.doFinal(toEncrypt.getBytes());
			return new BASE64Encoder().encode(byteCipherText);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
       return "";
	}
}
