package com.example.demotiki.AnotherClass;

public class SanPhamDonHang {
    String id;
    int quantity;

    public SanPhamDonHang(String id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public SanPhamDonHang() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
