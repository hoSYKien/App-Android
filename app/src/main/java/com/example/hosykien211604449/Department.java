package com.example.hosykien211604449;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Model class representing a Department (Khoa) in the hospital.
 */
public class Department {
    private String maKhoa;
    private String tenKhoa;
    private String vitri;
    private String pass;

    public Department(String maKhoa, String tenKhoa, String vitri, String pass) {
        this.maKhoa = maKhoa;
        this.tenKhoa = tenKhoa;
        this.vitri = vitri;
        this.pass = pass;
    }

    public String getMaKhoa() {
        return maKhoa;
    }

    public void setMaKhoa(String maKhoa) {
        this.maKhoa = maKhoa;
    }

    public String getTenKhoa() {
        return tenKhoa;
    }

    public void setTenKhoa(String tenKhoa) {
        this.tenKhoa = tenKhoa;
    }

    public String getVitri() {
        return vitri;
    }

    public void setVitri(String vitri) {
        this.vitri = vitri;
    }

    public String getPass() throws Exception {
        return decrypt(pass);
    }

    // Key và IV của bạn, đã encode Base64
    private static final String KEY = "T28S1wHcHXmnbMyqNHnUgA==";
    private static final String IV  = "Olvi9pHXNYCYee/ZWEgsug==";

    /**
     * Mã hóa plainText → trả Base64-encoded cipherText
     */
    public static String encrypt(String plainText) throws Exception {
        byte[] keyBytes = Base64.decode(KEY, Base64.DEFAULT);
        byte[] ivBytes  = Base64.decode(IV,  Base64.DEFAULT);

        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec  = new IvParameterSpec(ivBytes);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));
        return Base64.encodeToString(encrypted, Base64.NO_WRAP);
    }

    /**
     * Giải mã Base64-encoded cipherText → trả plainText
     */
    public static String decrypt(String cipherText) throws Exception {
        byte[] keyBytes    = Base64.decode(KEY, Base64.DEFAULT);
        byte[] ivBytes     = Base64.decode(IV,  Base64.DEFAULT);
        byte[] cipherBytes = Base64.decode(cipherText, Base64.DEFAULT);

        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        byte[] decrypted = cipher.doFinal(cipherBytes);
        return new String(decrypted, "UTF-8");
    }
    public boolean validatePassword(String input) {
        if (input == null) return false;
        try {
            String real = getPass();
            return input.equals(real);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
