package com.example.demotiki.AnotherClass;

public class DanhMucSP {
    private String iddanhmuc;
    private String tendanhmuc;

    public DanhMucSP(String id, String tendanhmuc) {
        this.iddanhmuc = id;
        this.tendanhmuc = tendanhmuc;
    }

    public DanhMucSP() {
    }

    public String getIddanhmuc() {
        return iddanhmuc;
    }

    public void setIddanhmuc(String iddanhmuc) {
        this.iddanhmuc = iddanhmuc;
    }

    public String getTendanhmuc() {
        return tendanhmuc;
    }

    public void setTendanhmuc(String tendanhmuc) {
        this.tendanhmuc = tendanhmuc;
    }
}
