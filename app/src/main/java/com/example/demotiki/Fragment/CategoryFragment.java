package com.example.demotiki.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.demotiki.Adapter.SanPhamAdapter;
import com.example.demotiki.AnotherClass.DanhMucSP;
import com.example.demotiki.AnotherClass.SanPham;
import com.example.demotiki.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoryFragment extends Fragment {

    public CategoryFragment() {
        // Required empty public constructor
    }
    public static CategoryFragment newInstance(String category) {

        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putString("CATEGORY_ID", category);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    RecyclerView recyclerView;
    SanPhamAdapter sanPhamAdapter;
    ArrayList<SanPham> sanPhamArrayList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        recyclerView = view.findViewById(R.id.recycle_sanpham_theodanhmuc);
        String categoryName = getArguments().getString("CATEGORY_ID");
        ArrayList<SanPham> danhsach = getList(categoryName);
        sanPhamAdapter = new SanPhamAdapter(danhsach,getContext());
        recyclerView.setAdapter(sanPhamAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        return view;
    }

    private ArrayList<SanPham> getList(String name){
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("SanPham");
        sanPhamArrayList = new ArrayList<>();
        productsRef.orderByChild("danhMucSP").equalTo(name)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        sanPhamArrayList.clear();
                        for (DataSnapshot productSnap : dataSnapshot.getChildren()){
                            String productId = productSnap.getKey();

                            String name = productSnap.child("tenSP").getValue(String.class);
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
                            sanPhamArrayList.add(sanPham);
                        }

                        sanPhamAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Lỗi
                    }
                });
        return sanPhamArrayList;
    }
}