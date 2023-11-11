package com.example.demotiki.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.demotiki.FragmentCaNhan.DonHangFragment;
import com.example.demotiki.FragmentCaNhan.SanPhamFragment;
import com.example.demotiki.FragmentDonHang.ChoThanhToanFragment;
import com.example.demotiki.FragmentDonHang.DangGiaoFragment;
import com.example.demotiki.FragmentDonHang.DangXuLyFragment;
import com.example.demotiki.FragmentDonHang.GiaoThanhCongFragment;

public class DanhSachDonHangAdapter extends FragmentStateAdapter {
    final int PAGE_COUNT = 4;
    private String tabTitles[] = new String[] { "Chờ thanh toán", "Đang xử lý", "Đang giao","Giao thành công" };


    public DanhSachDonHangAdapter(@NonNull FragmentActivity fm) {
        super(fm);
    }



    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new ChoThanhToanFragment();
            case 1:
                return new DangXuLyFragment();
            case 2:
                return new DangGiaoFragment();
            case 3:
                return new GiaoThanhCongFragment();
            default:
                return new ChoThanhToanFragment();
        }
    }

    @Override
    public int getItemCount() {
        return PAGE_COUNT;
    }
}
