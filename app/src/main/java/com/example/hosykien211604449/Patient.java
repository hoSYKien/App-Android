package com.example.hosykien211604449;

public class Patient {
    private final String maPhieu;
    private final String maBN;
    private final String tenBN;

    public Patient(String maPhieu, String maBN, String tenBN) {
        this.maPhieu = maPhieu;
        this.maBN     = maBN;
        this.tenBN    = tenBN;
    }

    public String getMaPhieu() { return maPhieu; }
    public String getMaBN()    { return maBN; }
    public String getTenBN()   { return tenBN; }
}
