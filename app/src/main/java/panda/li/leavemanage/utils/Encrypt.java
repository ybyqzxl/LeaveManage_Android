package panda.li.leavemanage.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.jivesoftware.smack.util.Base64;

/**
 * AES加密，解密,SHA单向散列
 *
 * @author Administrator
 */
public class Encrypt implements Serializable {

    private static final long serialVersionUID = -6526243391606302956L;
    public static final Boolean IS_DEVELOPING = Boolean.TRUE;
    public static final String ENCODING = "UTF-8";
    private static final String ALGORITHM_SHA_256 = "SHA-256";
    private static final String ALGORITHM_AES_128 = "AES";
    private static final String ALOGRITHM_AES_PARAM = "AES/ECB/PKCS5Padding";
    public static final Long SECURITY_RAND = 518L;

    /**
     * 使用SHA-256对指定字符串做单向散列
     *
     * @param str 待加密字符串
     * @return
     */
    public static String encryptBySHA256(String str) {
        MessageDigest md = algorithemFactory(ALGORITHM_SHA_256);
        md.update(str.getBytes());
        String strEncrypted = byte2hex(md.digest());
        return strEncrypted;
    }

    /**
     * 使用SHA-256对指定文件做单向散列校验
     *
     * @param file 待校验文件
     * @return
     */
    public static String fileChecking(File file) {
        MessageDigest md = algorithemFactory(ALGORITHM_SHA_256);
        FileInputStream is = null;
        byte[] buffer = new byte[1024];
        int readed = 0;
        try {
            is = new FileInputStream(file);
            while ((readed = is.read(buffer)) != -1)
                md.update(buffer, 0, readed);
            String strEncrypted = byte2hex(md.digest());
            return strEncrypted;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (is != null)
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
        }
    }

    /**
     * 获取指定加密算法
     *
     * @param algorithemName 算法名称
     * @return
     */
    private static MessageDigest algorithemFactory(String algorithemName) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithemName);
            return md;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 从byte[]转换为16进制字符串
     *
     * @param b
     * @return
     */
    public static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder("");
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append("0").append(stmp);
            else
                hs.append(stmp);
        }
        return hs.toString().toUpperCase();
    }

    /**
     * 从16进制字符串转byte[]
     *
     * @param hex
     * @return
     */
    public static byte[] hex2byte(String hex) {
        if (hex.length() < 1)
            return null;
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length() / 2; i++) {
            int high = Integer.parseInt(hex.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hex.substring(i * 2 + 1, i * 2 + 2), 16);
            bytes[i] = (byte) (high * 16 + low);
        }
        return bytes;
    }

    /**
     * 使用AES算法加密明文
     *
     * @param key 采用Encrypt.aesKeyGenerator()方法生成的密钥
     * @param str 明文
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     */
    public static String encryptByAES(Key key, String str)
            throws UnsupportedEncodingException, NoSuchAlgorithmException,
            NoSuchPaddingException, IllegalBlockSizeException,
            BadPaddingException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance(ALOGRITHM_AES_PARAM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] cipherText = cipher.doFinal(str.getBytes(ENCODING));
        return byte2hex(cipherText);
    }

    /**
     * 使用AES算法解密密文
     *
     * @param key 采用Encrypt.aesKeyGenerator()方法生成的密钥
     * @param str 密文
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws UnsupportedEncodingException
     */
    public static String decryptByAES(Key key, String str)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException, UnsupportedEncodingException {
        Cipher cipher = Cipher.getInstance(ALOGRITHM_AES_PARAM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] valueByteArr = cipher.doFinal(hex2byte(str));
        return new String(valueByteArr, ENCODING);
    }

    /**
     * 密钥生成器
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static Key aesKeyGenerator() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM_AES_128);
        keyGen.init(128);
        return keyGen.generateKey();
    }

    /**
     * 密钥生成器
     *
     * @param keyByteArr 指定密钥byte[]
     * @return
     */
    public static Key aesKeyGenerator(byte[] keyByteArr) {
        byte[] keyBytes = new byte[16];
        for (int i = 0; i < keyByteArr.length && i < keyBytes.length; i++) {
            keyBytes[i] = keyByteArr[i];
        }
        return new SecretKeySpec(keyBytes, ALGORITHM_AES_128);
    }

    /**
     * 密钥生成器
     *
     * @param key 指定密钥
     * @return
     * @throws UnsupportedEncodingException
     */
    public static Key aesKeyGenerator(String key)
            throws UnsupportedEncodingException {
        return aesKeyGenerator(key.getBytes(ENCODING));
    }

    /**
     * 密钥对象转换16进制字符串
     *
     * @param key
     * @return
     */
    public static String key2hex(Key key) {
        return byte2hex(key.getEncoded());
    }

    /**
     * 16进制字符串转换密钥对象
     *
     * @param hex
     * @return
     */
    public static Key hex2key(String hex) {
        return new SecretKeySpec(hex2byte(hex), ALGORITHM_AES_128);
    }

    // ///////////////////////////////////////////////////////////////
    // /BASE64 ///////////////////////////////////////////////////////
    // ///////////////////////////////////////////////////////////////

    /**
     * 对给定的字符串做BASE64编码,并采用默认的UTF-8编码将给定的字符串转换为byte[]
     *
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String encryptByBASE64(String str)
            throws UnsupportedEncodingException {
        return encryptByBASE64(str, ENCODING);
    }

    /**
     * 对给定的字符串做BASE64编码
     *
     * @param str
     * @param charsetName 转换字符串为byte[]时采用的编码
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String encryptByBASE64(String str, String charsetName)
            throws UnsupportedEncodingException {
        return Base64.encodeBytes(str.getBytes(charsetName));
    }

    /**
     * 采用BASE64算法对给定的字符串进行解码，并采用默认的UTF-8编码进行字符串的重建
     *
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static String decryptByBASE64(String str)
            throws UnsupportedEncodingException, IOException {
        return decryptByBASE64(str, ENCODING);
    }

    /**
     * 采用BASE64算法对给定的字符串进行解码
     *
     * @param str
     * @param charsetName 重建字符串时采用的编码
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static String decryptByBASE64(String str, String charsetName)
            throws UnsupportedEncodingException, IOException {
        byte[] bytes = Base64.decode(str);
        return new String(bytes, charsetName);
    }

    /**
     * RSA
     *
     * @author ZhaoWenhao
     */
    public static class RSA {

        /**
         * 加密算法RSA
         */
        public static final String KEY_ALGORITHM = "RSA";

        /**
         * 签名算法
         */
        public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

        /**
         * 获取公钥的key
         */
        private static final String PUBLIC_KEY = "RSAPublicKey";

        /**
         * 获取私钥的key
         */
        private static final String PRIVATE_KEY = "RSAPrivateKey";

        /**
         * RSA最大加密明文大小
         */
        private static final int MAX_ENCRYPT_BLOCK = 117;

        /**
         * RSA最大解密密文大小
         */
        private static final int MAX_DECRYPT_BLOCK = 128;

        /**
         * 生成密钥对(公钥和私钥)
         *
         * @return
         * @throws Exception
         */
        public static Map<String, Object> genKeyPair() {
            try {
                KeyPairGenerator keyPairGen = KeyPairGenerator
                        .getInstance(KEY_ALGORITHM);
                keyPairGen.initialize(1024);
                KeyPair keyPair = keyPairGen.generateKeyPair();
                RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
                RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
                Map<String, Object> keyMap = new HashMap<String, Object>(2);
                keyMap.put(PUBLIC_KEY, publicKey);
                keyMap.put(PRIVATE_KEY, privateKey);
                return keyMap;
            } catch (Throwable e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        /**
         * 用私钥对信息生成数字签名
         *
         * @param data       已加密数据
         * @param privateKey 私钥(BASE64编码)
         * @return
         * @throws Exception
         */
        public static String sign(byte[] data, String privateKey) {
            try {
                byte[] keyBytes = Base64.decode(privateKey);
                PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(
                        keyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
                PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
                Signature signature = Signature
                        .getInstance(SIGNATURE_ALGORITHM);
                signature.initSign(privateK);
                signature.update(data);
                return Base64.encodeBytes(signature.sign());
            } catch (Throwable e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        /**
         * 校验数字签名
         *
         * @param data      已加密数据
         * @param publicKey 公钥(BASE64编码)
         * @param sign      数字签名
         * @return
         * @throws Exception
         */
        public static boolean verify(byte[] data, String publicKey, String sign) {
            try {
                byte[] keyBytes = Base64.decode(publicKey);
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
                PublicKey publicK = keyFactory.generatePublic(keySpec);
                Signature signature = Signature
                        .getInstance(SIGNATURE_ALGORITHM);
                signature.initVerify(publicK);
                signature.update(data);
                return signature.verify(Base64.decode(sign));
            } catch (Throwable e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        /**
         * 私钥解密
         *
         * @param encryptedData 已加密数据
         * @param privateKey    私钥(BASE64编码)
         * @return
         * @throws Exception
         */
        public static byte[] decryptByPrivateKey(byte[] encryptedData,
                                                 String privateKey) {
            try {
                byte[] keyBytes = Base64.decode(privateKey);
                PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(
                        keyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
                Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
                Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
                cipher.init(Cipher.DECRYPT_MODE, privateK);
                int inputLen = encryptedData.length;
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                int offSet = 0;
                byte[] cache;
                int i = 0;
                /** 对数据分段解密 */
                while (inputLen - offSet > 0) {
                    if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                        cache = cipher.doFinal(encryptedData, offSet,
                                MAX_DECRYPT_BLOCK);
                    } else {
                        cache = cipher.doFinal(encryptedData, offSet, inputLen
                                - offSet);
                    }
                    out.write(cache, 0, cache.length);
                    i++;
                    offSet = i * MAX_DECRYPT_BLOCK;
                }
                byte[] decryptedData = out.toByteArray();
                out.close();
                return decryptedData;
            } catch (Throwable e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        /**
         * 公钥解密
         *
         * @param encryptedData 已加密数据
         * @param publicKey     公钥(BASE64编码)
         * @return
         * @throws Exception
         */
        public static byte[] decryptByPublicKey(byte[] encryptedData,
                                                String publicKey) {
            try {
                byte[] keyBytes = Base64.decode(publicKey);
                X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(
                        keyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
                Key publicK = keyFactory.generatePublic(x509KeySpec);
                Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
                cipher.init(Cipher.DECRYPT_MODE, publicK);
                int inputLen = encryptedData.length;
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                int offSet = 0;
                byte[] cache;
                int i = 0;
                /** 对数据分段解密 */
                while (inputLen - offSet > 0) {
                    if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                        cache = cipher.doFinal(encryptedData, offSet,
                                MAX_DECRYPT_BLOCK);
                    } else {
                        cache = cipher.doFinal(encryptedData, offSet, inputLen
                                - offSet);
                    }
                    out.write(cache, 0, cache.length);
                    i++;
                    offSet = i * MAX_DECRYPT_BLOCK;
                }
                byte[] decryptedData = out.toByteArray();
                out.close();
                return decryptedData;
            } catch (Throwable e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        /**
         * 公钥加密
         *
         * @param data      源数据
         * @param publicKey 公钥(BASE64编码)
         * @return
         * @throws Exception
         */
        public static byte[] encryptByPublicKey(byte[] data, String publicKey) {
            try {
                byte[] keyBytes = Base64.decode(publicKey);
                X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(
                        keyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
                Key publicK = keyFactory.generatePublic(x509KeySpec);
                Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
                cipher.init(Cipher.ENCRYPT_MODE, publicK);
                int inputLen = data.length;
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                int offSet = 0;
                byte[] cache;
                int i = 0;
                /** 对数据分段解密 */
                while (inputLen - offSet > 0) {
                    if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                        cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                    } else {
                        cache = cipher.doFinal(data, offSet, inputLen - offSet);
                    }
                    out.write(cache, 0, cache.length);
                    i++;
                    offSet = i * MAX_ENCRYPT_BLOCK;
                }
                byte[] encryptedData = out.toByteArray();
                out.close();
                return encryptedData;
            } catch (Throwable e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        /**
         * 私钥加密
         *
         * @param data       源数据
         * @param privateKey 私钥(BASE64编码)
         * @return
         * @throws Exception
         */
        public static byte[] encryptByPrivateKey(byte[] data, String privateKey) {
            try {
                byte[] keyBytes = Base64.decode(privateKey);
                PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(
                        keyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
                Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
                Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
                cipher.init(Cipher.ENCRYPT_MODE, privateK);
                int inputLen = data.length;
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                int offSet = 0;
                byte[] cache;
                int i = 0;
                /** 对数据分段解密 */
                while (inputLen - offSet > 0) {
                    if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                        cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                    } else {
                        cache = cipher.doFinal(data, offSet, inputLen - offSet);
                    }
                    out.write(cache, 0, cache.length);
                    i++;
                    offSet = i * MAX_ENCRYPT_BLOCK;
                }
                byte[] encryptedData = out.toByteArray();
                out.close();
                return encryptedData;
            } catch (Throwable e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        public static String getPrivateKey(Map<String, Object> keyMap) {
            try {
                Key key = (Key) keyMap.get(PRIVATE_KEY);
                return Base64.encodeBytes(key.getEncoded());
            } catch (Throwable e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        public static String getPublicKey(Map<String, Object> keyMap) {
            try {
                Key key = (Key) keyMap.get(PUBLIC_KEY);
                return Base64.encodeBytes(key.getEncoded());
            } catch (Throwable e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        public static PublicKey parserPublicKey(byte[] pubKey) {
            try {
                KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pubKey);
                PublicKey publicKey = keyFactory.generatePublic(keySpec);
                return publicKey;
            } catch (Exception e) {
                e.printStackTrace();
                throw new IllegalArgumentException(e.getMessage(), e);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        public static PrivateKey parserPrivateKey(byte[] pubKey) {
            try {
                KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pubKey);
                PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
                return privateKey;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }

}
