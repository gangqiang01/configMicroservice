package com.adv.config.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.net.util.Base64;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @date ：Created in 7/11/19 1:24 PM
 * @description：encrypt json
 */
@Slf4j
public class RASUtil {
    public static final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDMIt/Eu8oXDvgmVEOi47TvqSHEjf2y3xw+ZninCFN+G3uPkp1Ve6DEQd1ZXBCBZkgWKQuLFdgrKZj3Oq1yvdbaqYBdcQjoEoYMG8gE4pPNPFUaRy5tVjcVuqyaMzjkUs5GR2RJR7rgYObDnAHWRbTWqLkyMjB0QAy71i/Vqotx/wIDAQAB";
    public static final String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMwi38S7yhcO+CZUQ6LjtO+pIcSN/bLfHD5meKcIU34be4+SnVV7oMRB3VlcEIFmSBYpC4sV2CspmPc6rXK91tqpgF1xCOgShgwbyATik808VRpHLm1WNxW6rJozOORSzkZHZElHuuBg5sOcAdZFtNaouTIyMHRADLvWL9Wqi3H/AgMBAAECgYEAir8pWI5KzWFfSK39Srl6QWqziOT5ipoamE8Nsnn2RcBoOy1rwq5ElyAtUo9AqLLcTDOZpO8z/iJH1cIMIfDmFoJcTz1FSTWIY3DbG3HPXgRycoPB5kUVf5Vdyev9KAF3N/HDEl/TW6X++SB986qOcH88zxzvOgLdUGS46cki8AECQQDltlEmK6HAuSc5rR/QxkpmSEFJC6j+1v4ubUcJgBsW+BqDRajt+4MkRpVD5ujPMkpX0xDa5I9maiWHWckeY+UFAkEA439Hu0xRYkA+q08N0qkBnmpTKfj2UvxjElTYn8AHCOYGYZLVHZOevwwj3U5OVWT3o8PXQxN8eGXJbDU64WoqMwJAIF91SD/V5+cwE/zGAxIu4gNE0BqIkoGSCND1te0L5EzKz709dWyAqtgXqR4dqTWZLI9eTLrxSqmi6FRK97F0UQJANORRxB0hSsuPBnsxxomeo7sqrLaCZpbhXdC2MKYPvKTmVOczf1Xj8Z0b9YcQNVBtNvkAYkr0hHV92BekIeZx+wJBAIAKyQC9WcNFyXTOV4wXdjlX80LQmrajNsUNK6L9PZyCHw38N/lB8sDEzQcToZAv86J3ERswLUPsQzgNmnMxIAU=";
    private static final int MAX_ENCRYPT_BLOCK = 117;
    private static final int MAX_DECRYPT_BLOCK = 128;
    //公钥加密
    public static String encrypt(String json, String pubKey){
        try{
            log.info("encryotJson:"+json);
            byte[] rsaPublicKey = Base64.decodeBase64(pubKey);
            X509EncodedKeySpec x509EncodedKeySpec =new X509EncodedKeySpec(rsaPublicKey);
            KeyFactory keyFactory=KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            Cipher cipher=Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            int inputLen = json.getBytes().length;
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                int offSet = 0;
                byte[] cache;
                int i = 0;
                // 对数据分段加密
                while (inputLen - offSet > 0) {
                    if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                        cache = cipher.doFinal(json.getBytes(), offSet, MAX_ENCRYPT_BLOCK);
                    } else {
                        cache = cipher.doFinal(json.getBytes(), offSet, inputLen - offSet);
                    }
                    out.write(cache, 0, cache.length);
                    i++;
                    offSet = i * MAX_ENCRYPT_BLOCK;
                }
                return Base64.encodeBase64String(out.toByteArray());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;

    }
//    私钥解密
    public static String decrypt(String json, String privKey){
        try{
            byte[] rsaPrivateKey = Base64.decodeBase64(privKey);
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(rsaPrivateKey);
            KeyFactory keyFactory=KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Cipher cipher=Cipher.getInstance("RSA");//java版
//            Cipher cipher=Cipher.getInstance("RSA/ECB/PKCS1Padding");//android版
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] data = Base64.decodeBase64(json);
            int inputLen = data.length;
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                int offSet = 0;
                byte[] cache;
                int i = 0;
                // 对数据分段解密
                while (inputLen - offSet > 0) {
                    if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                        cache = cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
                    } else {
                        cache = cipher.doFinal(data, offSet, inputLen - offSet);
                    }
                    out.write(cache, 0, cache.length);
                    i++;
                    offSet = i * MAX_DECRYPT_BLOCK;
                }
                return new String(out.toByteArray());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;

    }

    public static JSONObject getKeyPair(){
        //初始化秘钥
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(1024);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();
            JSONObject keyPairJson = new JSONObject();
            keyPairJson.put("publicKey", Base64.encodeBase64String(publicKey.getEncoded()));
            keyPairJson.put("privateKey", Base64.encodeBase64String(privateKey.getEncoded()));
            return keyPairJson;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
