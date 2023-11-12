package com.example.demotiki.FragmentDonHang;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.demotiki.Adapter.DonHangTrongListAdapter;
import com.example.demotiki.AnotherClass.DonHang;
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


public class DangXuLyFragment extends Fragment {


    public DangXuLyFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    RecyclerView recyclerView;
    DonHangTrongListAdapter donhangAdapter;
    ArrayList<DonHang> donHangArrayList;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_dang_xu_ly, container, false);
        recyclerView = view.findViewById(R.id.recycle_donhangdanxuly);
        String trangthaiName = "Đang xử lý";
        getList(trangthaiName);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        donhangAdapter = new DonHangTrongListAdapter(donHangArrayList,getContext());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(donhangAdapter);
        return view;
    }
    private void getList(String trangthai){
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("DonHang");
        donHangArrayList = new ArrayList<>();
        productsRef.orderByChild("trangthaidonhang").equalTo(trangthai)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot productSnap : dataSnapshot.getChildren()){
                            String donhangId = productSnap.child("id_donhang").getValue(String.class);

                            String tennguoimua = productSnap.child("tennguoimua").getValue(String.class);
                            String userId = productSnap.child("id_nguoimua").getValue(String.class);

                            String sodienthoai = productSnap.child("sodienthoainguoimua").getValue(String.class);

                            String phuongthucthanhtoan = productSnap.child("ptthanhToan").getValue(String.class);

                            String ngaydathang = productSnap.child("ngaydathang").getValue(String.class);

                            String trangthaidonhang = productSnap.child("trangthaidonhang").getValue(String.class);

                            double giatong = productSnap.child("giaTongCong").getValue(Double.class);

                            String diachigiaohang = productSnap.child("diachigiaohang").getValue(String.class);

                            Map<String, SanPhamDonHang> sanPhamMap = new HashMap<>();

                            for(DataSnapshot spSnapshot : productSnap.child("id_sanpham").getChildren()) {
                                String id = spSnapshot.child("id").getValue(String.class);
                                int quanty = spSnapshot.child("quantity").getValue(Integer.class);
                                SanPhamDonHang sanPham = new SanPhamDonHang();
                                sanPham.setId(id);
                                sanPham.setQuantity(quanty);
                                sanPhamMap.put("SP"+id, sanPham);
                            }

                            DonHang donHang = new DonHang(donhangId,userId,sanPhamMap,diachigiaohang,tennguoimua,sodienthoai,phuongthucthanhtoan,giatong,trangthaidonhang,ngaydathang);
                            donHangArrayList.add(donHang);
                        }

                        donhangAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Lỗi
                    }
                });
    }
}