package com.adv.config.util;

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
public class EncryptUtil {
    private static final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDA0npULgTlY5RezMAmGLcSaw4dVIZTVuGbSRnai8GHTNTzT4/8yHnIfAhWpwAENEKZWP8iItvQT+KuxpbmpKBEPYIVf2D2wdkr13bFHTgwpICJAjMopNhCmYiKrfbHnpcZJ+rxDfaOpq11cg5LTeGog4PGyPBX47xe+TNU4CPevQIDAQAB";
    private static final String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMDSelQuBOVjlF7MwCYYtxJrDh1UhlNW4ZtJGdqLwYdM1PNPj/zIech8CFanAAQ0QplY/yIi29BP4q7GluakoEQ9ghV/YPbB2SvXdsUdODCkgIkCMyik2EKZiIqt9seelxkn6vEN9o6mrXVyDktN4aiDg8bI8FfjvF75M1TgI969AgMBAAECgYB4njFvnyno5JXXh3VbX3KFzvoRVuKAF4UhQmUt1ObG4EUFvutvUWj9BLmimuzjtF1E1+shS3T9pjtC4W9b7/dgIZY6yco6BWxL7lIYhr89QxJ/0qX+TdAYX9sxbPduSDW2YmsKiNFKOKK1GR4AuATmO5kAOwiIfdyCqZjaXRyNAQJBAPL0ZKNSNOtqPPejThxCUqHiJ/54oNgk7y4UlkvULCK9v+E4wKooVAXul8Z+cvzYznWrBG7JRXpmdtGdECo1/3ECQQDLLPk4i4YRPf4em7IySyRorjJsYBQ6Nti6VhkM1G5GmIobgAbzwVJFJTCsXecEF5MjaFWFyNUs4efRF04j3UYNAkBS6P4TEjHHCNlHdS5pzGRgpEobuFTDjk8lPZdQx1ZMgM6jcHenWd3arGKVNX/OM7q5QyRfHkacj60KuXvTu9uxAkEAloHGTKDuu68UpE/p/V+0pOcF60pxMX6XpdFJ1Ts4RYuSIzT/8WnwK9CU8drN2zYrhTswv146rHjcZQPCmsa7sQJAFGtvZlxCgM0l3Jo2j6XlmY83aKuof6tGLhsNvqSBuz9LR3DFpMvTWl3mjfreYXmOxMaqPr9iTU9gu95mk71XsQ==";
    private static final int MAX_ENCRYPT_BLOCK = 117;
    private static final int MAX_DECRYPT_BLOCK = 128;
    //公钥加密
    public static String encrypt(String json){
        try{
            byte[] rsaPublicKey = Base64.decodeBase64(publicKey);
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
    public static String decrypt(String json){
        try{
            byte[] rsaPrivateKey = Base64.decodeBase64(privateKey);
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(rsaPrivateKey);
            KeyFactory keyFactory=KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Cipher cipher=Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] data = Base64.decodeBase64(json);
            int inputLen = data.length;
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                int offSet = 0;
                byte[] cache;
                int i = 0;
                // 对数据分段加密
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

    public static void jdkRSA(){
        //初始化秘钥
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(1024);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey rSAPublicKey = (RSAPublicKey)keyPair.getPublic();
            RSAPrivateKey private1 = (RSAPrivateKey)keyPair.getPrivate();
            System.out.println("puk  :"+ Base64.encodeBase64String(rSAPublicKey.getEncoded()));
            System.out.println("pvk  :"+Base64.encodeBase64String(private1.getEncoded()));
        } catch (Exception e) {
            e.printStackTrace();
        }




    }
}
