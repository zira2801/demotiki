package com.example.demotiki.GioHang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demotiki.Adapter.AdapterCart;
import com.example.demotiki.AnotherClass.Cart;
import com.example.demotiki.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;

public class GioHangActivity extends AppCompatActivity {
    RecyclerView recyclerViewGioHang;
    ArrayList<Cart> carts;
    AdapterCart adapterCart;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    TextView tongtien;
    ImageView xoatatca,back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        recyclerViewGioHang = findViewById(R.id.recyclevview_cart);
        tongtien = findViewById(R.id.tongtien_tv);
        xoatatca = findViewById(R.id.xoatatca_cart);
        back = findViewById(R.id.back_giohang);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getListGioHang();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewGioHang.setLayoutManager(layoutManager);
        recyclerViewGioHang.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        adapterCart = new AdapterCart(carts,GioHangActivity.this);
        recyclerViewGioHang.setAdapter(adapterCart);
        LoadTongTien();
        xoatatca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.dialog_xoatatcagiohang,null);
                AlertDialog.Builder b = new AlertDialog.Builder(GioHangActivity.this);
                AlertDialog dialog = b.setView(view).create();
                dialog.show();
                Button ok = dialog.findViewById(R.id.btn_xoacart);
                Button huy = dialog.findViewById(R.id.btn_huy_xoa_cart);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("GioHang").child("gio_hang_"+user.getUid());
                        ref.removeValue();
                        carts.clear();
                        dialog.dismiss();
                        Toast.makeText(GioHangActivity.this,"Xóa tất cả sản phẩm khỏi giỏ hàng thành công",Toast.LENGTH_SHORT).show();
                    }
                });

                huy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    private void getListGioHang(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("GioHang").child("gio_hang_"+user.getUid());
        carts = new ArrayList<>();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                carts.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    String spgh = child.child("id_sp").getValue(String.class);
                    String idnguoidung = child.child("id_nguoidung").getValue(String.class);
                    String tenSP = child.child("tenSP").getValue(String.class);
                    Double giaSP = child.child("giasp").getValue(Double.class);
                    String anhsanpham = child.child("anhSP").getValue(String.class);
                    int soluong = child.child("soluong").getValue(Integer.class);
                    Cart cart = new Cart(spgh, idnguoidung, tenSP,giaSP,anhsanpham,soluong);
                    carts.add(cart);
                }
                adapterCart.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void LoadTongTien(){
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMaximumFractionDigits(0);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("GioHang").child("gio_hang_"+user.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double total = 0;
                for (DataSnapshot child : snapshot.getChildren()) {
                    Double giaSP = child.child("giasp").getValue(Double.class);
                    int soluong = child.child("soluong").getValue(Integer.class);
                    double thanhtien = giaSP * soluong; // tính thành tiền
                    total += thanhtien; // cộng dồn vào tổng
                }
                String formattedTotal = formatter.format(total) + " VNĐ";
                tongtien.setText(formattedTotal);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}