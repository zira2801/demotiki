package com.example.demotiki.AnotherClass;

public class YeuThich {
    private String idsp;
    private String idnguoidung;

    public YeuThich(String idsp, String idnguoidung) {
        this.idsp = idsp;
        this.idnguoidung = idnguoidung;
    }

    public YeuThich() {
    }

    public String getIdnguoidung() {
        return idnguoidung;
    }

    public void setIdnguoidung(String idnguoidung) {
        this.idnguoidung = idnguoidung;
    }

    public String getIdsp() {
        return idsp;
    }

    public void setIdsp(String idsp) {
        this.idsp = idsp;
    }

}
