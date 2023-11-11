package com.example.demotiki.TrangBill;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.demotiki.Adapter.DonHangAdapter;
import com.example.demotiki.AnotherClass.Cart;
import com.example.demotiki.MainActivity2;
import com.example.demotiki.R;

import java.util.ArrayList;

public class TrangBillActivity extends AppCompatActivity {

    TextView tv_madonhang,tv_tennguoimua,tv_sodt,tv_diachi,tv_hinhthucthanhtoan,tv_ngaydat,tv_tonggia;
    RecyclerView sanphamdonhang;
    DonHangAdapter donHangAdapter;

    LinearLayout btn_muasam;
    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_bill);
        UI();
        String madon = getIntent().getStringExtra("id_donhang");
        String tenmn = getIntent().getStringExtra("tenNguoiMua");
        String sodtnm = getIntent().getStringExtra("sdtNguoimua");
        String diachinm = getIntent().getStringExtra("diachiNguoiMua");
        String ngaydat = getIntent().getStringExtra("ngaydathang");
        String hinhthuctt = getIntent().getStringExtra("hinhthucthanhtoan");
        Double tonggia = getIntent().getDoubleExtra("tonggiadonhang",0.0);

        ArrayList<Cart> cartList = getIntent().getParcelableArrayListExtra("danhsachspgiohang");
        tv_madonhang.setText(madon);
        tv_tennguoimua.setText(tenmn);
        tv_sodt.setText(sodtnm);
        tv_diachi.setText(diachinm);
        tv_hinhthucthanhtoan.setText(hinhthuctt);
        tv_ngaydat.setText(ngaydat);
        tv_tonggia.setText(String.format("%,.0f VNĐ",tonggia));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        sanphamdonhang.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        sanphamdonhang.setLayoutManager(layoutManager);
        donHangAdapter = new DonHangAdapter(cartList,this);
        sanphamdonhang.setAdapter(donHangAdapter);

        btn_muasam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TrangBillActivity.this, MainActivity2.class));
                finish();
            }
        });
    }

    private void UI(){
        tv_madonhang = findViewById(R.id.madonhang_bill);
        tv_tennguoimua = findViewById(R.id.tennguoimua_bill);
        tv_sodt = findViewById(R.id.sodtnguoimua_bill);
        tv_diachi = findViewById(R.id.diachigiaohangnguoimua_bill);
        tv_hinhthucthanhtoan = findViewById(R.id.hinhthucthanhtoan_bill);
        tv_ngaydat = findViewById(R.id.ngaydathang_bill);
        tv_tonggia = findViewById(R.id.tongtiendathang_bill);
        sanphamdonhang = findViewById(R.id.recycel_bill);
        btn_muasam = findViewById(R.id.btn_tieptucmuasam);
    }
}