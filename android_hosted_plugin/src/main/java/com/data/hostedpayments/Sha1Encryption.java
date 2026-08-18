package com.data.hostedpayments;



import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class Sha1Encryption {
    public byte[] hash(String text)throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md;
        md =  MessageDigest.getInstance("SHA-1");
        md =  MessageDigest.getInstance("SHA-1");
        md.update(text.getBytes("utf-8"));
        byte[] md5 = md.digest();
        return md5;
    }

    public String SHA384(String text)throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md;
        md = MessageDigest.getInstance("SHA-384");
        md.update(text.getBytes("utf-8"));
        byte[] sha384 = md.digest();
        return String.valueOf(Hex.encodeHex(sha384));
    }

    public String SHA256(String text)throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md;
        md = MessageDigest.getInstance("SHA-256");
        md.update(text.getBytes("utf-8"));
        byte[] sha384 = md.digest();
        return String.valueOf(Hex.encodeHex(sha384));
    }

//    public String SHA256decrypt(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException, DecoderException {
//        MessageDigest md;
//        md = MessageDigest.getInstance("SHA-256");
//        md.update(text.getBytes("utf-8"));
//        byte[] sha384 = md.digest();
////        return String.valueOf(Hex.decodeHex(sha384));
//        char[] chars = text.toCharArray();
//        String respMsqBytes = new String(Hex.decodeHex(sha384), "Cp1047");
//        System.out.println(respMsqBytes);
////        return respMsqBytes;
//    }


//    public String SHA256decrypt(String text) throws UnsupportedEncodingException {
//        byte[] data = text.getBytes("UTF-8");
//        String base64 = android.util.Base64.encodeToString(data,android.util.Base64.DEFAULT);
//
//        return base64;
//    }

    public String decode(String strdecode) throws UnsupportedEncodingException {
        // Receiving side

        byte[] data = android.util.Base64.decode(strdecode,  android.util.Base64.DEFAULT);
        String text = new String(data, "UTF-8");
        return text;
    }

//
//    String respMsqBytes = new String(Hex.decodeHex(hex), "Cp1047");
//    System.out.println(respMsqBytes);
}
