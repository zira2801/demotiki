package com.example.demotiki.ThongTinDonHang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.demotiki.Adapter.DSSPDonHangAdapter;
import com.example.demotiki.Adapter.DonHangAdapter;
import com.example.demotiki.AnotherClass.Cart;
import com.example.demotiki.AnotherClass.DDSPDonHang;
import com.example.demotiki.AnotherClass.SanPhamDonHang;
import com.example.demotiki.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ThongTinDonHangActivity extends AppCompatActivity {

    ImageView back;
    TextView tennguoimua,sodtnguoimua,diachinguoimua,ngaydat,hinhthucthanhtoan,tongcong,madonhang;
    RecyclerView recyclerView;

    ArrayList<DDSPDonHang> arrayList;
    DSSPDonHangAdapter donHangAdapter;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_don_hang);
        UI();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        String madh = getIntent().getStringExtra("id_donhang");
        String id_nm = getIntent().getStringExtra("id_nguoimua");
        String tenm = getIntent().getStringExtra("tennguoimua");
        String sodt = getIntent().getStringExtra("sodienthoai");
        String diachi = getIntent().getStringExtra("diachigiao");
        String pttt = getIntent().getStringExtra("ptthanhtoan");
        String trangthai = getIntent().getStringExtra("trangthaidh");
        String ngaydathang = getIntent().getStringExtra("ngaydathang");
        Double tonggia = getIntent().getDoubleExtra("tonggiatien",0.0);

        getListSPDonHang(id_nm,madh);
        tennguoimua.setText(tenm);
        sodtnguoimua.setText(sodt);
        diachinguoimua.setText(diachi);
        madonhang.setText(madh);
        hinhthucthanhtoan.setText(pttt);
        ngaydat.setText(ngaydathang);
        String formattedPrice = String.format("%,.0f",tonggia);
        tongcong.setText(formattedPrice+" VNĐ");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(layoutManager);
        donHangAdapter = new DSSPDonHangAdapter(arrayList,this);
        recyclerView.setAdapter(donHangAdapter);
    }
    private void UI(){
        back = findViewById(R.id.thoat_thongtindonhang);
        tennguoimua = findViewById(R.id.tennguoimua_thongtindonhang);
        sodtnguoimua = findViewById(R.id.sodtnguoimua_thongtindonhang);
        diachinguoimua = findViewById(R.id.diachigiaohangnguoimua_thongtindonhang);
        ngaydat = findViewById(R.id.ngaydathang_thongtindonhang);
        hinhthucthanhtoan = findViewById(R.id.hinhthucthanhtoan_thongtindonhang);
        tongcong = findViewById(R.id.tongtiendathang_thongtindonhang);
        recyclerView = findViewById(R.id.recycel_ds_thongtindonhang);
        madonhang = findViewById(R.id.madonhang_thongtindonhang);
    }


    private void getListSPDonHang(String id,String mad){
        DatabaseReference donhangRef = FirebaseDatabase.getInstance().getReference("DonHang");
        arrayList = new ArrayList<>();
        donhangRef.orderByChild("id_donhang").equalTo(mad).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for(DataSnapshot dsDonHang : snapshot.getChildren()){
                    String id = snapshot.getKey();
                    Map<String, Object> sanPhamMap = (Map<String, Object>) dsDonHang.child("id_sanpham").getValue();
                    Map<String, SanPhamDonHang> sanPhamList = new HashMap<>();
                    for(String key : sanPhamMap.keySet()) {
                        Map<String, Object> sanPhamData = (Map<String, Object>) sanPhamMap.get(key);

                        // Chuyển đổi sang đối tượng SanPhamDonHang
                        String sanPhamId = (String) sanPhamData.get("id");
                        int sl = ((Long) sanPhamData.get("quantity")).intValue();
                        SanPhamDonHang sanPham = new SanPhamDonHang(sanPhamId,sl);
                        // Thêm vào danh sách
                        sanPhamList.put(key, sanPham);
                    }

                    DDSPDonHang donHang = new DDSPDonHang(id,sanPhamList);
                    arrayList.add(donHang);
                }
                donHangAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}