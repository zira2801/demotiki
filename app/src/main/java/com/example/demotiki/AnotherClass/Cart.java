package com.example.demotiki.AnotherClass;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Cart implements Parcelable {
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

    protected Cart(Parcel in) {
        id_sp = in.readString();
        id_nguoidung = in.readString();
        tenSP = in.readString();
        if (in.readByte() == 0) {
            giasp = null;
        } else {
            giasp = in.readDouble();
        }
        anhSP = in.readString();
        soluong = in.readInt();
    }

    public static final Creator<Cart> CREATOR = new Creator<Cart>() {
        @Override
        public Cart createFromParcel(Parcel in) {
            return new Cart(in);
        }

        @Override
        public Cart[] newArray(int size) {
            return new Cart[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id_sp);
        parcel.writeString(id_nguoidung);
        parcel.writeString(tenSP);
        if (giasp == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(giasp);
        }
        parcel.writeString(anhSP);
        parcel.writeInt(soluong);
    }
}
