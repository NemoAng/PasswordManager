package com.nemowang.passwordmanager;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class EncryptDecrypt {
    public static final String MD5 = "MD5";
    public static final String BASE64 = "BASE64";
    public static final String DES = "DES";

    private static final String DES_KEY = "PasswordManager";

    public EncryptDecrypt() throws InvalidKeySpecException {
    }

    public static String MD5(String plainText) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] cipherData = md5.digest(plainText.getBytes());
            StringBuilder builder = new StringBuilder();
            for(byte cipher : cipherData) {
                String toHexStr = Integer.toHexString(cipher & 0xff);
                builder.append(toHexStr.length() == 1 ? "0" + toHexStr : toHexStr);
            }
            return builder.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String BASE64(String plainText) {
        byte[] cipherText = plainText.getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(cipherText);
    }

    public static String BASE64_Decrypt(String cipherText) {
        byte[] decoded = Base64.getDecoder().decode(cipherText);
        return (new String(decoded, StandardCharsets.UTF_8));
    }

    public static String DES(String plainText){
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec keySpec = new DESKeySpec(DES_KEY.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(keySpec);


            Cipher cipher = Cipher.getInstance("des");

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, random);
            byte[] cipherData = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(cipherData);

        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
                | BadPaddingException | IllegalBlockSizeException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String DES_Decrypt(String cipherText){
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec keySpec = new DESKeySpec(DES_KEY.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(keySpec);

            Cipher cipher = Cipher.getInstance("des");


            cipher.init(Cipher.DECRYPT_MODE, secretKey, random);
            byte[] cipherData = Base64.getDecoder().decode(cipherText);

            byte[] plainData  = cipher.doFinal(cipherData);
            return new String(plainData);

        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
                | BadPaddingException | IllegalBlockSizeException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return "";
    }
}
