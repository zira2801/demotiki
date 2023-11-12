package com.example.demotiki.DanhSachDonHang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.demotiki.Adapter.DanhSachDonHangAdapter;
import com.example.demotiki.FragmentCaNhan.ViewPagerAdapter;
import com.example.demotiki.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class DanhSachDonHangActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    DanhSachDonHangAdapter donHangAdapter;
    ImageView back;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_don_hang);
        tabLayout = findViewById(R.id.tab_layout_danhsachdonhang);
        viewPager2 = findViewById(R.id.view_pager_danhsachdonhang);
        back = findViewById(R.id.thoat_danhsachdonhang);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        donHangAdapter = new DanhSachDonHangAdapter(this);
        viewPager2.setAdapter(donHangAdapter);
        new TabLayoutMediator(tabLayout,viewPager2, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("Chờ thanh toán");
                    break;
                case 1:
                    tab.setText("Đang xử lý");
                    break;
                case 2:
                    tab.setText("Đang giao");
                    break;
                case 3:
                    tab.setText("Giao thành công");
                    break;
            }
        }).attach();
    }
}