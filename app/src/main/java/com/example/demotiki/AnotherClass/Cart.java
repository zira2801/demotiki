package com.example.demotiki.AnotherClass;

import android.net.Uri;

public class Cart {
    private String id_sp;
    private String id_nguoidung;
    private String tenSP;
    private Double giasp;
    private String anhSP;
    private int soluong;

    public Cart(String id_sp, String id_nguoidung, String tenSP, Double giasp, String anhSP,int soluong) {
        this.id_sp = id_sp;
        this.id_nguoidung = id_nguoidung;
        this.tenSP = tenSP;
        this.giasp = giasp;
        this.soluong = soluong;
        this.anhSP = anhSP;
    }

    public Cart() {
    }

    public String getAnhSP() {
        return anhSP;
    }

    public void setAnhSP(String anhSP) {
        this.anhSP = anhSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public Double getGiasp() {
        return giasp;
    }

    public void setGiasp(Double giasp) {
        this.giasp = giasp;
    }

    public String getId_sp() {
        return id_sp;
    }

    public void setId_sp(String id_sp) {
        this.id_sp = id_sp;
    }

    public String getId_nguoidung() {
        return id_nguoidung;
    }

    public void setId_nguoidung(String id_nguoidung) {
        this.id_nguoidung = id_nguoidung;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }
}
