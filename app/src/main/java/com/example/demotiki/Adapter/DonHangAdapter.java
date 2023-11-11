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
import com.example.demotiki.AnotherClass.Cart;
import com.example.demotiki.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.DonHangViewHolder>{
    private List<Cart> cartList;
    private Context context;

    public DonHangAdapter(List<Cart> cartList, Context context) {
        this.cartList = cartList;
        this.context = context;
    }

    @NonNull
    @Override
    public DonHangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dathang,parent,false);
        return new DonHangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonHangViewHolder holder, int position) {
        Cart cart = cartList.get(position);
        holder.textViewTenSP.setText(cart.getTenSP());
        double price = cart.getGiasp();
        // Định dạng số và thêm dấu phân cách hàng nghìn
        @SuppressLint("DefaultLocale") String formattedPrice = String.format("%,.0f", price);
        holder.textViewGiaSP.setText(formattedPrice+ " VNĐ");
        holder.soluongSP.setText(String.valueOf(cart.getSoluong()));
        Glide.with(context)
                .load(cart.getAnhSP())
                .into(holder.imageViewSP);
        DatabaseReference SPRef = FirebaseDatabase.getInstance().getReference().child("SanPham").child(cart.getId_sp());
        SPRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String iduser = (String) snapshot.child("idUser").getValue();
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(iduser);
                    DatabaseReference nhabanRef = FirebaseDatabase.getInstance().getReference().child("NhaBan").child("NB"+iduser);
                    userRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String photoUrl = (String) snapshot.child("profile").getValue();
                            Glide.with(getApplicationContext()).load(photoUrl).error(R.drawable.baseline_account_circle_24).into(holder.circleImageView);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        if(cartList == null){
            return 0;
        }
        return cartList.size();
    }

    public static class DonHangViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewSP;
        CircleImageView circleImageView;
        TextView tennhaban;
        TextView textViewTenSP;
        TextView textViewGiaSP;
        TextView soluongSP;
        public DonHangViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewSP = itemView.findViewById(R.id.imageSPdathang);
            textViewTenSP = itemView.findViewById(R.id.tv_tenspdathang);
            textViewGiaSP = itemView.findViewById(R.id.tv_giaspdathang);
            soluongSP = itemView.findViewById(R.id.tv_soluong_dathang);
            tennhaban = itemView.findViewById(R.id.tennhaban_dathang);
            circleImageView = itemView.findViewById(R.id.avatar_nhaban_dathang);
        }
    }
}
