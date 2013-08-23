/*
 * SHA512CryptLinuxShadow HASH Function like PHP crypt for MD5, SHA-256 AND SHA-512
 *
 * Copyright (C) 2010  Demitrius Belai
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Date:    14/07/2010 11:22:43
 *
 * Author:  Demitrius Belai
 */

package br.com.alphadev.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidParameterException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 *
 * @author demitrius
 * @since 14/07/2010 11:22:43
 */
public class SHA512CryptLinuxShadow {
    
    private static final String BASE64_TABLE = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private static final String SALT_TABLE = BASE64_TABLE;

    private static final byte[][] BASE64_MD5_SEQ = {
        { 0,  6, 12},
        { 1,  7, 13},
        { 2,  8, 14},
        { 3,  9, 15},
        { 4, 10,  5},
        {-1, -1, 11},
    };

    private static final byte[][] BASE64_SHA256_SEQ = {
        { 0, 10, 20},
        {21,  1, 11},
        {12, 22,  2},
        { 3, 13, 23},
        {24,  4, 14},
        {15, 25,  5},
        { 6, 16, 26},
        {27,  7, 17},
        {18, 28,  8},
        { 9, 19, 29},
        {-1, 31, 30},
    };

    private static final byte[][] BASE64_SHA512_SEQ = {
        { 0, 21, 42},
        {22, 43,  1},
        {44,  2, 23},
        { 3, 24, 45},
        {25, 46,  4},
        {47,  5, 26},
        { 6, 27, 48},
        {28, 49,  7},
        {50,  8, 29},
        { 9, 30, 51},
        {31, 52, 10},
        {53, 11, 32},
        {12, 33, 54},
        {34, 55, 13},
        {56, 14, 35},
        {15, 36, 57},
        {37, 58, 16},
        {59, 17, 38},
        {18, 39, 60},
        {40, 61, 19},
        {62, 20, 41},
        {-1, -1, 63},
    };

    private static final String MAGIC = "\\$\\d\\$";

    private static final Pattern SALT_TEST_SHA = Pattern.compile("^[./0-9A-Za-z]{8,16}$");
    private static final Pattern SALT_TEST_MD5 = Pattern.compile("^[./0-9A-Za-z]{4,8}$");

    private static byte[] getBytes(String string) {
        try {
            return string.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SHA512CryptLinuxShadow.class.getName()).log(Level.SEVERE, null, ex);
        }
        return string.getBytes();
    }

    private static int unsignedByte(byte b) {
        return (int)(b & 0xFF);
    }

    private static void clear(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++)
            bytes[i] = 0;
    }

    public static String crypt(String password, String salt) {
        String ret;
        try {
            if (salt.startsWith("$1$")) {
                ret = crypt_md5(password, salt);
            } else if (salt.startsWith("$5$") || salt.startsWith("$6$")) {
                ret = crypt_sha(password, salt);
            } else {
                // Default
                ret = crypt_sha(password, "$6$" + salt);
            }
            return ret;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SHA512CryptLinuxShadow.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static String crypt_md5(String password, String salt) throws NoSuchAlgorithmException {
        byte[] b_password = getBytes(password);
        String salt_2 = salt.replaceFirst("^" + MAGIC, "");
        salt_2 = salt_2.replaceFirst("\\$.*", "");
        if (salt_2.length() > 8)
            salt_2 = salt_2.substring(0, 8);
        if (!SALT_TEST_MD5.matcher(salt_2).find())
            throw new InvalidParameterException("salt: " + salt);

        byte[] b_salt = getBytes(salt_2);
        MessageDigest md_a = MessageDigest.getInstance("md5");
        md_a.update(b_password);
        md_a.update(getBytes("$1$"));
        md_a.update(b_salt);

        MessageDigest md_b = MessageDigest.getInstance("md5");
        md_b.update(b_password);
        md_b.update(b_salt);
        md_b.update(b_password);
        byte[] md_b_digest = md_b.digest();

        for (int i = b_password.length; i > 0; i -= 16) {
            md_a.update(md_b_digest, 0, i > 16 ? 16 : i );
        }

        for (int i = b_password.length; i != 0; i >>>= 1) {
            if ((i & 1) != 0)
                md_a.update((byte)0);
            else
                md_a.update(b_password, 0, 1);
        }

        byte[] md_a_digest = md_a.digest();

        for (int i = 0; i < 1000; i++) {
            MessageDigest md_c = MessageDigest.getInstance("md5");
            if ((i & 1) != 0) {
                md_c.update(b_password);
            } else {
                md_c.update(md_a_digest);
            }
            if (i % 3 != 0) {
                md_c.update(b_salt);
            }
            if (i % 7 != 0) {
                md_c.update(b_password);
            }
            if ((i & 1) != 0) {
                md_c.update(md_a_digest);
            } else {
                md_c.update(b_password);
            }
            md_a_digest = md_c.digest();
        }
        String ret = "$1$" + salt_2 + "$" + base64(md_a_digest, BASE64_MD5_SEQ);
        // Clear
        clear(b_password);
        clear(b_salt);
        clear(md_a_digest);
        clear(md_b_digest);
        return ret;
    }

    private static String crypt_sha(String password, String salt) throws NoSuchAlgorithmException {
        /*
         * Steps in http://people.redhat.com/drepper/SHA-crypt.txt
         */
        byte[] b_password = getBytes(password);
        byte bytes = 64;
        if (salt.startsWith("$5$")) {
            bytes = 32;
        }
        String salt_2 = salt.replaceFirst("^" + MAGIC, "");
        int rounds = 5000;
        if (salt_2.startsWith("rounds=")) {
            rounds = Integer.parseInt(salt_2.replaceFirst("rounds=([0-9]+)\\$.*", "$1"));
            if (rounds < 1000)
                rounds = 1000;
            if (rounds > 999999999)
                rounds = 999999999;
            salt_2 = salt_2.replaceFirst("rounds=[0-9]+\\$", "");
        }
        salt_2 = salt_2.replaceFirst("\\$.*", "");
        if (salt_2.length() > 16)
            salt_2 = salt_2.substring(0, 16);
        if (!SALT_TEST_SHA.matcher(salt_2).find())
            throw new InvalidParameterException("salt: " + salt);

        byte[] b_salt = getBytes(salt_2);
        // 1
        MessageDigest md_a = MessageDigest.getInstance("SHA-512");
        // 2
        md_a.update(b_password);
        // 3
        md_a.update(b_salt);
        // 4
        MessageDigest md_b = MessageDigest.getInstance("SHA-512");
        // 5
        md_b.update(b_password);
        // 6
        md_b.update(b_salt);
        // 7
        md_b.update(b_password);
        // 8
        byte[] md_b_digest = md_b.digest();
        // 9, 10
        for (int i = b_password.length; i > 0; i -= bytes) {
            md_a.update(md_b_digest, 0, i > bytes ? bytes : i );
        }
        // 11
        for (int i = b_password.length; i != 0; i >>>= 1) {
            if ((i & 1) != 0)
                md_a.update(md_b_digest);
            else
                md_a.update(b_password);
        }
        // 12
        byte[] md_a_digest = md_a.digest();
        // 13
        MessageDigest md_dp = MessageDigest.getInstance("SHA-512");
        // 14
        for (int i = 0; i < b_password.length; i++) {
            md_dp.update(b_password);
        }
        // 15
        byte[] md_dp_digest = md_dp.digest();
        // 16
        byte[] p = new byte[b_password.length];
        int k = 0;
        for (int i = b_password.length; i > 0; i -= bytes) {
            for (int j = 0; j < (b_password.length < bytes ? b_password.length : bytes); j++) {
                p[k++] = md_dp_digest[j];
            }
        }
        // 17
        MessageDigest md_ds = MessageDigest.getInstance("SHA-512");
        // 18
        for (int i = 0; i < 16 + unsignedByte(md_a_digest[0]); i++) {
            md_ds.update(b_salt);
        }
        // 19
        byte[] md_ds_digest = md_ds.digest();
        // 20
        byte[] s = new byte[b_salt.length];
        k = 0;
        for (int i = b_salt.length; i > 0; i -= bytes) {
            for (int j = 0; j < (b_salt.length < bytes ? b_salt.length : bytes); j++) {
                s[k++] = md_ds_digest[j];
            }
        }
        // 21
        for (int i = 0; i < rounds; i++) {
            MessageDigest md_c = MessageDigest.getInstance("SHA-512");
            if ((i & 1) != 0) {
                md_c.update(p);
            } else {
                md_c.update(md_a_digest);
            }
            if (i % 3 != 0) {
                md_c.update(s);
            }
            if (i % 7 != 0) {
                md_c.update(p);
            }
            if ((i & 1) != 0) {
                md_c.update(md_a_digest);
            } else {
                md_c.update(p);
            }
            md_a_digest = md_c.digest();
        }
        // 22
        StringBuilder ret = new StringBuilder(128);
        if (bytes == 32) {
            ret.append("$5$");
            if (rounds != 5000) {
                ret.append("rounds=").append(rounds).append("$");
            }
            ret.append(salt_2).append("$");
            ret.append(base64(md_a_digest, BASE64_SHA256_SEQ));
        } else {
            ret.append("$6$");
            if (rounds != 5000) {
                ret.append("rounds=").append(rounds).append("$");
            }
            ret.append(salt_2).append("$");
            ret.append(base64(md_a_digest, BASE64_SHA512_SEQ));
        }
        // Clear
        clear(b_password);
        clear(b_salt);
        clear(md_a_digest);
        clear(md_b_digest);
        clear(md_dp_digest);
        clear(md_ds_digest);
        clear(p);
        clear(s);
        return ret.toString();
    }

    public static String crypt(String password) {
        StringBuilder salt = new StringBuilder(16);
        Random random;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SHA512CryptLinuxShadow.class.getName()).log(Level.SEVERE, null, ex);
            random = new Random(System.currentTimeMillis());
        }
        for (int i = 0; i < 16; i++) {
            salt.append(SALT_TABLE.charAt(random.nextInt(SALT_TABLE.length())));
        }
        return crypt(password, salt.toString());
    }

    private static String lto64(long l, int k) {
        StringBuilder s = new StringBuilder(3);
        for (int i = k; i > 0; i--) {
            s.append(BASE64_TABLE.charAt((int)(l & 0x3F)));
            l >>>= 6;
        }
        return s.toString();
    }

    public static String base64(byte[] bytes, byte[][] seq) {
        StringBuilder ret = new StringBuilder(seq.length * 4);
        for (int i = 0; i < seq.length; i++) {
            long l = 0;
            int k = 0;
            for (int j = 0; j < 3; j++) {
                if (seq[i][j] >= 0) {
                    k++;
                    l |= unsignedByte(bytes[seq[i][j]]) << (16 - 8 * j);
                }
            }
            if (k == 1)
                ret.append(lto64(l, 2));
            else if (k == 2)
                ret.append(lto64(l, 3));
            else
                ret.append(lto64(l, 4));
        }
        return ret.toString();
    }

}
