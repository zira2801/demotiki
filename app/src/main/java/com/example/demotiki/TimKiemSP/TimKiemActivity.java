package com.example.demotiki.TimKiemSP;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.demotiki.Adapter.SanPhamAdapter;
import com.example.demotiki.AnotherClass.SanPham;
import com.example.demotiki.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TimKiemActivity extends AppCompatActivity {

    EditText search;
    RecyclerView recyclerView;
    SanPhamAdapter sanPhamAdapter;
    ArrayList<SanPham> sanPhamArrayList;
    TextView khontimthaysp;
    ArrayList<SanPham> filteredProducts;
    ArrayList<SanPham> allProduct;
    String query;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tim_kiem);
        search = findViewById(R.id.edt_search);
        recyclerView = findViewById(R.id.recycle_timkiem);
        khontimthaysp = findViewById(R.id.khongtimthay);
        back = findViewById(R.id.back_timkiemsp);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        query = getIntent().getStringExtra("search_text");
        search.setText(query);
        query = search.getText().toString().trim().toLowerCase();
        sanPhamArrayList = new ArrayList<>();
        sanPhamAdapter = new SanPhamAdapter(sanPhamArrayList, TimKiemActivity.this);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                query = charSequence.toString().trim().toLowerCase();
                filterProducts(query);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        recyclerView.setAdapter(sanPhamAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        loadProductData();
    }

    private void loadProductData() {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("SanPham");
        allProduct = new ArrayList<>();
        productsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allProduct.clear();
                for (DataSnapshot productSnap : dataSnapshot.getChildren()) {
                    // Parse product data here
                    String productId = productSnap.getKey();

                    String name = productSnap.child("tensp").getValue(String.class);
                    String userId = productSnap.child("idUser").getValue(String.class);

                    String category = productSnap.child("danhMucSP").getValue(String.class);

                    String description = productSnap.child("moTa").getValue(String.class);

                    String brand = productSnap.child("thuongHieu").getValue(String.class);

                    String origin = productSnap.child("xuatXu").getValue(String.class);

                    double price = productSnap.child("giaban").getValue(Double.class);

                    String warranty = productSnap.child("checkBaoHanh").getValue(String.class);

                    String warrantyPeriod = productSnap.child("thoiGianBaoHanh").getValue(String.class);

                    String rating = productSnap.child("danhgia").getValue(String.class);

                    ArrayList<String> imageList = new ArrayList<>();
                    for(DataSnapshot imageSnapshot : productSnap.child("dsAnh").getChildren()) {
                        String imageUrl = imageSnapshot.getValue(String.class);
                        imageList.add(imageUrl);
                    }

                    SanPham sanPham = new SanPham(productId,userId,name,category,brand,price,warrantyPeriod,origin,description,warranty,imageList,rating);

                    allProduct.add(sanPham);
                }
                filterProducts(query);
                sanPhamAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }

    private void filterProducts(String query) {
        ArrayList<SanPham> filteredList = new ArrayList<>();
        for (SanPham product : allProduct) {
            if (product.getTensp().toLowerCase().contains(query)) {
                filteredList.add(product);
            }
        }

        if (query.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            khontimthaysp.setVisibility(View.VISIBLE);
        } else if (filteredList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            khontimthaysp.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            khontimthaysp.setVisibility(View.GONE);
        }

        sanPhamAdapter.updateData(filteredList);
    }


}