package com.example.demotiki.Adapter;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.demotiki.AnotherClass.DDSPDonHang;
import com.example.demotiki.AnotherClass.DonHang;
import com.example.demotiki.AnotherClass.SanPhamDonHang;
import com.example.demotiki.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class DSSPDonHangAdapter extends RecyclerView.Adapter<DSSPDonHangAdapter.DDSSPDonHangAdapterViewHolder>{
    private ArrayList<DDSPDonHang> donHangList;
    private Context context;


    public DSSPDonHangAdapter(ArrayList<DDSPDonHang> donHangList, Context context) {
        this.donHangList = donHangList;
        this.context = context;
    }

    @NonNull
    @Override
    public DDSSPDonHangAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dsspdonhang,parent,false);
        return new DSSPDonHangAdapter.DDSSPDonHangAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DDSSPDonHangAdapterViewHolder holder, int position) {
        DDSPDonHang ddspDonHang = donHangList.get(position);
        String idDonHang = ddspDonHang.getId_donhang();
        Map<String, SanPhamDonHang> sanPhamMap = ddspDonHang.getDanhsachsanpham();
        Set<String> sanPhamKeys = sanPhamMap.keySet();

        // Duyệt qua danh sách key
        for(String key : sanPhamKeys) {
            SanPhamDonHang sanPham = sanPhamMap.get(key);

            // Lấy thông tin sản phẩm
            String id = sanPham.getId();
            int quantity = sanPham.getQuantity();
            holder.soluong.setText(String.valueOf(quantity));
            DatabaseReference donhangRef = FirebaseDatabase.getInstance().getReference("SanPham").child(id);
            donhangRef.addValueEventListener(new ValueEventListener() {
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
                    Double price = (Double) snapshot.child("giaban").getValue(Double.class);
                    @SuppressLint("DefaultLocale") String formattedPrice = String.format("%,.0f", price);
                    holder.giasp.setText(formattedPrice+" VNĐ");

                    String iduser = (String) snapshot.child("idUser").getValue();
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(iduser);
                    userRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String photoUrl = (String) snapshot.child("profile").getValue();
                            Glide.with(getApplicationContext()).load(photoUrl).error(R.drawable.baseline_account_circle_24).into(holder.anhnhaban);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    DatabaseReference nhabanRef = FirebaseDatabase.getInstance().getReference().child("NhaBan").child("NB"+iduser);
                    nhabanRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String name = (String) snapshot.child("hoten").getValue();
                            if (name == null) {
                                holder.tennhaban.setText("");
                            } else {
                                holder.tennhaban.setText(name);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if(donHangList == null){
            return 0;
        }
        return donHangList.size();
    }

    public static class DDSSPDonHangAdapterViewHolder extends RecyclerView.ViewHolder {
        ImageView anhsp;
        TextView tensp,giasp,soluong,tennhaban;
        CircleImageView anhnhaban;
        public DDSSPDonHangAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            anhnhaban = itemView.findViewById(R.id.avatar_nhaban_thongtindonhang);
            anhsp = itemView.findViewById(R.id.imageSPthongtindonhang);
            tensp = itemView.findViewById(R.id.tv_tenspthongtindonhang);
            giasp = itemView.findViewById(R.id.tv_giaspthongtindonhang);
            soluong = itemView.findViewById(R.id.tv_soluong_thongtindonhang);
            tennhaban = itemView.findViewById(R.id.tennhaban_thongtindonhang);
        }
    }
}
