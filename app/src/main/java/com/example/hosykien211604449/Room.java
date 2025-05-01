package com.example.hosykien211604449;

/**
 * Model class representing a Room (Phòng) in the hospital.
 */
public class Room {
    private String maPhong;
    private String maKhoa;
    private String tenPhong;
    private String viTri;

    public Room(String maPhong, String maKhoa, String tenPhong, String viTri) {
        this.maPhong = maPhong;
        this.maKhoa = maKhoa;
        this.tenPhong = tenPhong;
        this.viTri = viTri;
    }

    public String getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }

    public String getMaKhoa() {
        return maKhoa;
    }

    public void setMaKhoa(String maKhoa) {
        this.maKhoa = maKhoa;
    }

    public String getTenPhong() {
        return tenPhong;
    }

    public void setTenPhong(String tenPhong) {
        this.tenPhong = tenPhong;
    }

    public String getViTri() {
        return viTri;
    }

    public void setViTri(String viTri) {
        this.viTri = viTri;
    }
}