package com.example.demotiki.AnotherClass;

import java.util.ArrayList;

public class SanPham {
    private String itemId;
    private String idUser;
    private String tensp;
    private String DanhMucSP;
    private String thuongHieu;

    private Double giaban;
    private String thoiGianBaoHanh;
    private String xuatXu;
    private String moTa;
    private String checkBaoHanh;
    private ArrayList<String> dsAnh;

    private String danhgia;

    public SanPham(){

    }

    public SanPham(String itemId, String idUser, String tensp, String danhMucSP, String thuongHieu, Double giaban, String thoiGianBaoHanh, String xuatXu, String moTa, String checkBaoHanh, ArrayList<String> dsAnh, String danhgia) {
        this.itemId = itemId;
        this.idUser = idUser;
        this.tensp = tensp;
        DanhMucSP = danhMucSP;
        this.thuongHieu = thuongHieu;
        this.giaban = giaban;
        this.thoiGianBaoHanh = thoiGianBaoHanh;
        this.xuatXu = xuatXu;
        this.moTa = moTa;
        this.checkBaoHanh = checkBaoHanh;
        this.dsAnh = dsAnh;
        this.danhgia = danhgia;
    }

    public Double getGiaban() {
        return giaban;
    }

    public void setGiaban(Double giaban) {
        this.giaban = giaban;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getDanhMucSP() {
        return DanhMucSP;
    }

    public void setDanhMucSP(String danhMucSP) {
        DanhMucSP = danhMucSP;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getCheckBaoHanh() {
        return checkBaoHanh;
    }

    public void setCheckBaoHanh(String checkBaoHanh) {
        this.checkBaoHanh = checkBaoHanh;
    }

    public String getThuongHieu() {
        return thuongHieu;
    }

    public void setThuongHieu(String thuongHieu) {
        this.thuongHieu = thuongHieu;
    }

    public String getThoiGianBaoHanh() {
        return thoiGianBaoHanh;
    }

    public void setThoiGianBaoHanh(String thoiGianBaoHanh) {
        this.thoiGianBaoHanh = thoiGianBaoHanh;
    }

    public String getXuatXu() {
        return xuatXu;
    }

    public void setXuatXu(String xuatXu) {
        this.xuatXu = xuatXu;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public ArrayList<String> getDsAnh() {
        return dsAnh;
    }

    public void setDsAnh(ArrayList<String> dsAnh) {
        this.dsAnh = dsAnh;
    }

    public String getDanhgia() {
        return danhgia;
    }

    public void setDanhgia(String danhgia) {
        this.danhgia = danhgia;
    }
}
