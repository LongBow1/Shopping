package com.myqq.service.youza.util;

import java.security.MessageDigest;
import java.util.Arrays;

public class CheckUntil {
    private static final String token = "cobra";


    static class SHA1 {

        private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5',
                '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        /**
         * Takes the raw bytes from the digest and formats them correct.
         *
         * @param bytes the raw bytes from the digest.
         * @return the formatted bytes.
         */
        private static String getFormattedText(byte[] bytes) {
            int len = bytes.length;
            StringBuilder buf = new StringBuilder(len * 2);
            // 把密文转换成十六进制的字符串形式
            for (int j = 0; j < len; j++) {
                buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
                buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
            }
            return buf.toString();
        }

        public static String encode(String str) {
            if (str == null) {
                return null;
            }
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
                messageDigest.update(str.getBytes());
                return getFormattedText(messageDigest.digest());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static boolean checkSignatures(String signature, String timestamp, String nonce) {
        String[] strings = new String[]{nonce, token, timestamp};
        Arrays.sort(strings);
        StringBuffer stringBuffer = new StringBuffer();
        for (String string : strings) {
            stringBuffer.append(string);
        }
        if (SHA1.encode(stringBuffer.toString()).equals(signature)) {
            return true;
        }
        return false;
    }



}
