package com.example.hosykien211604449;

/**
 * Model class cho dữ liệu phiếu chuẩn đoán (SearchPCD / slPKP result)
 */
public class PCDRecord {
    private String maPCD;
    private String maBS;
    private String tenBS;
    private String maBN;
    private String tenBN;
    private String ngayCD;
    private String tongTien;
    private String trangThai;
    private int stt;

    public PCDRecord() {}

    public PCDRecord(String maPCD, String maBS, String tenBS,
                     String maBN, String tenBN,
                     String ngayCD, String tongTien,
                     String trangThai, int stt) {
        this.maPCD = maPCD;
        this.maBS = maBS;
        this.tenBS = tenBS;
        this.maBN = maBN;
        this.tenBN = tenBN;
        this.ngayCD = ngayCD;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
        this.stt = stt;
    }

    public String getMaPCD() {
        return maPCD;
    }

    public void setMaPCD(String maPCD) {
        this.maPCD = maPCD;
    }

    public String getMaBS() {
        return maBS;
    }

    public void setMaBS(String maBS) {
        this.maBS = maBS;
    }

    public String getTenBS() {
        return tenBS;
    }

    public void setTenBS(String tenBS) {
        this.tenBS = tenBS;
    }

    public String getMaBN() {
        return maBN;
    }

    public void setMaBN(String maBN) {
        this.maBN = maBN;
    }

    public String getTenBN() {
        return tenBN;
    }

    public void setTenBN(String tenBN) {
        this.tenBN = tenBN;
    }

    public String getNgayCD() {
        return ngayCD;
    }

    public void setNgayCD(String ngayCD) {
        this.ngayCD = ngayCD;
    }

    public String getTongTien() {
        return tongTien;
    }

    public void setTongTien(String tongTien) {
        this.tongTien = tongTien;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }
}
