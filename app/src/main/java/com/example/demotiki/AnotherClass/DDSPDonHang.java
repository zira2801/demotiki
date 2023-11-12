package com.example.demotiki.AnotherClass;

import java.util.Map;

public class DDSPDonHang {
    private String id_donhang;
    private Map<String, SanPhamDonHang> danhsachsanpham;

    public DDSPDonHang(String id_donhang, Map<String, SanPhamDonHang> danhsachsanpham) {
        this.id_donhang = id_donhang;
        this.danhsachsanpham = danhsachsanpham;
    }

    public String getId_donhang() {
        return id_donhang;
    }

    public void setId_donhang(String id_donhang) {
        this.id_donhang = id_donhang;
    }

    public Map<String, SanPhamDonHang> getDanhsachsanpham() {
        return danhsachsanpham;
    }

    public void setDanhsachsanpham(Map<String, SanPhamDonHang> danhsachsanpham) {
        this.danhsachsanpham = danhsachsanpham;
    }
}
