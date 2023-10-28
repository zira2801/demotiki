package com.example.demotiki.YeuThich;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.demotiki.Adapter.AdapterCart;
import com.example.demotiki.Adapter.AdapterYeuThich;
import com.example.demotiki.AnotherClass.Cart;
import com.example.demotiki.AnotherClass.YeuThich;
import com.example.demotiki.GioHang.GioHangActivity;
import com.example.demotiki.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class YeuThichActivity extends AppCompatActivity {

    RecyclerView recyclerViewYeuthich;
    AdapterYeuThich adapterYeuThich;
    ArrayList<YeuThich> yeuThiches;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ImageView xoatatca,back;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yeu_thich);
        recyclerViewYeuthich = findViewById(R.id.recyclevview_yeuthich);
        xoatatca = findViewById(R.id.xoatatca_yeuthich);
        back = findViewById(R.id.back_yeuthich);
        getListYeuThich();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewYeuthich.setLayoutManager(layoutManager);
        recyclerViewYeuthich.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        adapterYeuThich = new AdapterYeuThich(yeuThiches, YeuThichActivity.this);
        recyclerViewYeuthich.setAdapter(adapterYeuThich);

        xoatatca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.dialog_xoatatcayeuthich,null);
                AlertDialog.Builder b = new AlertDialog.Builder(YeuThichActivity.this);
                AlertDialog dialog = b.setView(view).create();
                dialog.show();
                Button ok = dialog.findViewById(R.id.btn_xoatatcayeuthich_item);
                Button huy = dialog.findViewById(R.id.btn_huy_xoa_tatcayeuthichitem);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("YeuThich").child("yeu_thich_"+user.getUid());
                        ref.removeValue();
                        yeuThiches.clear();
                        dialog.dismiss();
                        Toast.makeText(YeuThichActivity.this,"Xóa tất cả sản phẩm khỏi yêu thích thành công",Toast.LENGTH_SHORT).show();
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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getListYeuThich(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("YeuThich").child("yeu_thich_"+user.getUid());
        yeuThiches = new ArrayList<>();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                yeuThiches.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    String spyt = child.child("idsp").getValue(String.class);
                    String idnguoidung = child.child("idnguoidung").getValue(String.class);
                    String tenSP = child.child("tenSP").getValue(String.class);
                    Double giaSP = child.child("giasp").getValue(Double.class);
                    String anhsanpham = child.child("anhSP").getValue(String.class);
                    YeuThich yeuThich = new YeuThich(spyt,idnguoidung,tenSP,giaSP,anhsanpham);
                    yeuThiches.add(yeuThich);
                }
                adapterYeuThich.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}