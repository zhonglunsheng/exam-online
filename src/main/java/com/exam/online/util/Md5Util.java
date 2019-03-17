package com.exam.online.util;

import com.exam.online.common.Consts;

import java.security.MessageDigest;

/**
 * @author zhonglunsheng
 * @Description MD5加密
 * @create 2019-02-18 16:43
 */
public class Md5Util {

    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return HEXDIGITS[d1] + HEXDIGITS[d2];
    }

    /**
     * 返回大写MD5
     *
     * @param origin
     * @param charsetname
     * @return
     */
    private static String md5Encode(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname)) {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            } else {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
            }
        } catch (Exception exception) {
        }
        return resultString.toUpperCase();
    }

    public static String md5Encodeutf8(String origin) {
        origin = origin + Consts.SALT;
        return md5Encode(origin, "utf-8");
    }


    private static final String[] HEXDIGITS = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public static void main(String[] args) {
        System.out.println(md5Encodeutf8("abc123"));
        // 60DAE9D237234EB6EB86A29E69FFB6E2 123456
        // 87F4D927D81AD1DFE1FF4B9B9CE35C90 abc123
    }

}
