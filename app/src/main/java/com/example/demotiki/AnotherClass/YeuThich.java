package com.example.demotiki.AnotherClass;

public class YeuThich {
    private String idsp;
    private String idnguoidung;
    private String tenSP;
    private Double giasp;
    private String anhSP;

    public YeuThich(String idsp, String idnguoidung, String tenSP, Double giasp, String anhSP) {
        this.idsp = idsp;
        this.idnguoidung = idnguoidung;
        this.tenSP = tenSP;
        this.giasp = giasp;
        this.anhSP = anhSP;
    }

    public YeuThich() {
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

    public String getAnhSP() {
        return anhSP;
    }

    public void setAnhSP(String anhSP) {
        this.anhSP = anhSP;
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
