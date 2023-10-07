package com.example.demotiki.AnotherClass;

public class NhaBan {
    private String userId;
    private String Email;
    private String Hoten;
    private String QuocGia;
    private String SoDT;
    private String NganhHang;

    public NhaBan(String userId, String email, String hoten, String quocGia, String soDT, String nganhHang) {
        this.userId = userId;
        Email = email;
        Hoten = hoten;
        QuocGia = quocGia;
        SoDT = soDT;
        NganhHang = nganhHang;
    }

    public NhaBan() {
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setHoten(String hoten) {
        Hoten = hoten;
    }

    public void setQuocGia(String quocGia) {
        QuocGia = quocGia;
    }

    public void setSoDT(String soDT) {
        SoDT = soDT;
    }

    public void setNganhHang(String nganhHang) {
        NganhHang = nganhHang;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return Email;
    }

    public String getHoten() {
        return Hoten;
    }

    public String getQuocGia() {
        return QuocGia;
    }

    public String getSoDT() {
        return SoDT;
    }

    public String getNganhHang() {
        return NganhHang;
    }
}
