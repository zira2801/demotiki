package com.example.demotiki.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.demotiki.AnotherClass.Cart;
import com.example.demotiki.AnotherClass.DonHang;
import com.example.demotiki.AnotherClass.SanPhamDonHang;
import com.example.demotiki.R;
import com.example.demotiki.ThongTinDonHang.ThongTinDonHangActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DonHangTrongListAdapter extends RecyclerView.Adapter<DonHangTrongListAdapter.DonHangTrongListViewHolder>{
    private ArrayList<DonHang> donHangList;
    private Context context;

    public DonHangTrongListAdapter(ArrayList<DonHang> donHangList, Context context) {
        this.donHangList = donHangList;
        this.context = context;
    }

    @NonNull
    @Override
    public DonHangTrongListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_donhang,parent,false);
        return new DonHangTrongListAdapter.DonHangTrongListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonHangTrongListViewHolder holder, int position) {
        DonHang donHang = donHangList.get(position);
        holder.tensp.setText(donHang.getId_donhang());
        DatabaseReference donhangRef = FirebaseDatabase.getInstance().getReference("DonHang");
        donhangRef.orderByChild("id_nguoimua").equalTo(donHang.getId_nguoimua()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> idList = new ArrayList<>();
                Map<String, SanPhamDonHang> sanPhamList = donHang.getId_sanpham();
                int tongsoluong = 0;
                for (SanPhamDonHang sanPham : sanPhamList.values()) {
                    tongsoluong+= sanPham.getQuantity();
                }
                for (DataSnapshot ds : snapshot.getChildren()) {
                    DonHang donHang = ds.getValue(DonHang.class);
                    for (SanPhamDonHang sp : donHang.getId_sanpham().values()) {
                        idList.add(sp.getId());
                    }
                    String firstProductId = idList.get(0);
                    DatabaseReference spRef = FirebaseDatabase.getInstance().getReference("SanPham");
                    spRef.child(firstProductId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String name = (String) snapshot.child("tensp").getValue();
                            holder.tensp.setText(name);
                            ArrayList<String> dsAnh = new ArrayList<>();
                            for(DataSnapshot urlSnapshot : snapshot.child("dsAnh").getChildren()) {
                                String url = urlSnapshot.getValue(String.class);
                                dsAnh.add(url);
                            }
                            String firstImageUrl = dsAnh.get(0);
                            Glide.with(context)
                                    .load(firstImageUrl)
                                    .into(holder.anhsp);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                double price = donHang.getGiaTongCong();
                // Định dạng số và thêm dấu phân cách hàng nghìn
                @SuppressLint("DefaultLocale") String formattedPrice = String.format("%,.0f", price);
                holder.giasp.setText(formattedPrice+" VNĐ");
                holder.soluong.setText(String.valueOf(tongsoluong));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.xemchitiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ThongTinDonHangActivity.class);
                i.putExtra("id_donhang",donHang.getId_donhang());
                i.putExtra("id_nguoimua",donHang.getId_nguoimua());
                i.putExtra("diachigiao",donHang.getDiachigiaohang());
                i.putExtra("tennguoimua",donHang.getTennguoimua());
                i.putExtra("sodienthoai",donHang.getSodienthoainguoimua());
                i.putExtra("ptthanhtoan",donHang.getPTThanhToan());
                i.putExtra("trangthaidh",donHang.getTrangthaidonhang());
                i.putExtra("ngaydathang",donHang.getNgaydathang());
                i.putExtra("tonggiatien",donHang.getGiaTongCong());
                context.startActivity(i);
            }
        });
    }
    @Override
    public int getItemCount() {
        if(donHangList == null){
            return 0;
        }
        return donHangList.size();
    }

    public static class DonHangTrongListViewHolder extends RecyclerView.ViewHolder {
        TextView tensp, giasp,soluong;
        ImageView anhsp;
        LinearLayout xemchitiet;
        public DonHangTrongListViewHolder(@NonNull View itemView) {
            super(itemView);
            tensp = itemView.findViewById(R.id.tv_tenspdonhang);
            giasp = itemView.findViewById(R.id.tv_giaspdonhang);
            soluong = itemView.findViewById(R.id.tv_soluong_donhang);
            anhsp = itemView.findViewById(R.id.imageSPdonhang);
            xemchitiet = itemView.findViewById(R.id.btn_xemchitietdonhang);
        }
    }
}
