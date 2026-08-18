package com.data.hostedpayments;

import static android.content.ContentValues.TAG;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import android.util.Base64;
import android.util.Log;

import java.nio.charset.StandardCharsets;

public class DecryptionUtil {

    public static String decodeAndDecryptV2(String encryptedResponse, String merKey) throws Exception {
        SecretKey secretKey = new SecretKeySpec(hexStringToByteArray(merKey), "AES");
        String decodedAndDecrypted = decodeAndDecrypt(encryptedResponse, secretKey);

        return decodedAndDecrypted;
    }

    private static String decodeAndDecrypt(String encodedText, SecretKey secretKey) throws Exception {
        // Use Android's Base64 class for decoding
        byte[] decodedBytes = Base64.decode(encodedText, Base64.DEFAULT);

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); // Ensure to use AES/ECB/PKCS5Padding
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] decryptedBytes = cipher.doFinal(decodedBytes);

        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }


    public static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }

}