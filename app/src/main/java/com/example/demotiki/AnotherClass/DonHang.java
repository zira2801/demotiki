package com.example.demotiki.AnotherClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DonHang {
    private String id_donhang;
    private String id_nguoimua;
    private Map<String, SanPhamDonHang> danhsachsanpham;

    private String diachigiaohang;

    private String tennguoimua;
    private String sodienthoainguoimua;

    private String PTThanhToan;

    private Double GiaTongCong;

    private String Trangthaidonhang;
    private String ngaydathang;

    public DonHang(String id_donhang, String id_nguoimua, Map<String, SanPhamDonHang> danhsachsanpham, String diachigiaohang, String tennguoimua, String sodienthoainguoimua, String PTThanhToan, Double giaTongCong, String trangthaidonhang,String ngaydathang) {
        this.id_donhang = id_donhang;
        this.id_nguoimua = id_nguoimua;
        this.danhsachsanpham = danhsachsanpham;
        this.diachigiaohang = diachigiaohang;
        this.tennguoimua = tennguoimua;
        this.sodienthoainguoimua = sodienthoainguoimua;
        this.PTThanhToan = PTThanhToan;
        GiaTongCong = giaTongCong;
        Trangthaidonhang = trangthaidonhang;
        this.ngaydathang = ngaydathang;
    }

    public DonHang() {
    }

    public String getNgaydathang() {
        return ngaydathang;
    }

    public void setNgaydathang(String ngaydathang) {
        this.ngaydathang = ngaydathang;
    }

    public String getId_donhang() {
        return id_donhang;
    }

    public void setId_donhang(String id_donhang) {
        this.id_donhang = id_donhang;
    }

    public String getId_nguoimua() {
        return id_nguoimua;
    }

    public void setId_nguoimua(String id_nguoimua) {
        this.id_nguoimua = id_nguoimua;
    }


    public Map<String, SanPhamDonHang> getId_sanpham() {
        return danhsachsanpham;
    }

    public void setId_sanpham(Map<String, SanPhamDonHang> danhsachsanpham) {
        this.danhsachsanpham = danhsachsanpham;
    }

    public String getDiachigiaohang() {
        return diachigiaohang;
    }

    public void setDiachigiaohang(String diachigiaohang) {
        this.diachigiaohang = diachigiaohang;
    }

    public String getTennguoimua() {
        return tennguoimua;
    }

    public void setTennguoimua(String tennguoimua) {
        this.tennguoimua = tennguoimua;
    }

    public String getSodienthoainguoimua() {
        return sodienthoainguoimua;
    }

    public void setSodienthoainguoimua(String sodienthoainguoimua) {
        this.sodienthoainguoimua = sodienthoainguoimua;
    }

    public String getPTThanhToan() {
        return PTThanhToan;
    }

    public void setPTThanhToan(String PTThanhToan) {
        this.PTThanhToan = PTThanhToan;
    }

    public Double getGiaTongCong() {
        return GiaTongCong;
    }

    public void setGiaTongCong(Double giaTongCong) {
        GiaTongCong = giaTongCong;
    }

    public String getTrangthaidonhang() {
        return Trangthaidonhang;
    }

    public void setTrangthaidonhang(String trangthaidonhang) {
        Trangthaidonhang = trangthaidonhang;
    }
}
