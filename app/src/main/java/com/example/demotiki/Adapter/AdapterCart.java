package com.example.demotiki.Adapter;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.demotiki.AnotherClass.Cart;
import com.example.demotiki.AnotherClass.SanPham;
import com.example.demotiki.ChiTietSanPham.ChiTietSPActivity;
import com.example.demotiki.DangKyNhaBan.DangKyNhaBanActivity;
import com.example.demotiki.GioHang.GioHangActivity;
import com.example.demotiki.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class AdapterCart extends RecyclerView.Adapter<AdapterCart.CartViewHolder> {
    private List<Cart> cartList;
    private Context context;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public AdapterCart(List<Cart> cartList, Context context) {
        this.cartList = cartList;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterCart.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_cart,parent,false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCart.CartViewHolder holder, @SuppressLint("RecyclerView") int position) {
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
        holder.imageViewSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

                Query query = ref.child("SanPham").child(cartList.get(position).getId_sp());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String name = snapshot.child("tensp").getValue(String.class);
                        String userId = snapshot.child("idUser").getValue(String.class);

                        String category = snapshot.child("danhMucSP").getValue(String.class);

                        String description = snapshot.child("moTa").getValue(String.class);

                        String brand = snapshot.child("thuongHieu").getValue(String.class);

                        String origin = snapshot.child("xuatXu").getValue(String.class);

                        Double price = snapshot.child("giaban").getValue(Double.class);

                        String warranty = snapshot.child("checkBaoHanh").getValue(String.class);

                        String warrantyPeriod = snapshot.child("thoiGianBaoHanh").getValue(String.class);

                        String rating = snapshot.child("danhgia").getValue(String.class);

                        ArrayList<String> imageList = new ArrayList<>();
                        for(DataSnapshot imageSnapshot : snapshot.child("dsAnh").getChildren()) {
                            String imageUrl = imageSnapshot.getValue(String.class);
                            imageList.add(imageUrl);
                        }
                        Intent intent = new Intent(context, ChiTietSPActivity.class);
                        intent.putExtra("danhmucsp", category);
                        intent.putExtra("motasp", description);
                        String[] arrayAnh = imageList.toArray(new String[0]);
                        intent.putExtra("arrayAnh", arrayAnh);
                        intent.putExtra("tensp", name);
                        intent.putExtra("giasp", price);
                        intent.putExtra("xuatxusp", origin);
                        intent.putExtra("thuonghieu", brand);
                        intent.putExtra("danhgiasp", rating);
                        intent.putExtra("baohanh", warranty);
                        intent.putExtra("tgbaohanh", warrantyPeriod);
                        intent.putExtra("idsp",cartList.get(position).getId_sp());
                        intent.putExtra("iduser", userId);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        //Su kien cap nhat gio hang // lấy số lượng hiện tại
        holder.remove.setOnClickListener(new View.OnClickListener() {
            int currentQuantity = cart.getSoluong();
            @Override
            public void onClick(View view) {
                if(currentQuantity > 1) {
                    currentQuantity--; // giảm số lượng đi 1
                   cart.setSoluong(currentQuantity);
                    // Cập nhật lên Firebase
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("GioHang").child("gio_hang_"+user.getUid());
                    ref.child("gio_hang_"+ cart.getId_sp()).child("soluong").setValue(currentQuantity);
                    holder.soluongSP.setText(String.valueOf(currentQuantity));
                }
            }
        });
        holder.plus.setOnClickListener(new View.OnClickListener() {
            int currentQuantity = cart.getSoluong();
            @Override
            public void onClick(View view) {
                if(currentQuantity < 10) {
                    currentQuantity++; // giảm số lượng đi 1
                    cart.setSoluong(currentQuantity);

                    // Cập nhật lên Firebase
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("GioHang").child("gio_hang_"+user.getUid());
                    ref.child("gio_hang_"+ cart.getId_sp()).child("soluong").setValue(currentQuantity);
                    holder.soluongSP.setText(String.valueOf(currentQuantity));
                }
            }
        });

        holder.xoaSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(context);
                view = inflater.inflate(R.layout.dialog_xoa_sp_cart,null);
                AlertDialog.Builder b = new AlertDialog.Builder(context);
                AlertDialog dialog = b.setView(view).create();
                dialog.show();
                Button ok = dialog.findViewById(R.id.btn_xoacart_item);
                Button huy = dialog.findViewById(R.id.btn_huy_xoa_cartitem);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("GioHang").child("gio_hang_"+user.getUid());
                        ref.child("gio_hang_"+ cart.getId_sp()).removeValue();
                        dialog.dismiss();
                        Toast.makeText(context,"Xóa sản phẩm khỏi giỏ hàng thành công",Toast.LENGTH_SHORT).show();
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

    @Override
    public int getItemCount() {
        if(cartList == null){
            return 0;
        }
        return cartList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewSP,remove,plus;
        TextView textViewTenSP;
        TextView textViewGiaSP;
        TextView soluongSP;
        TextView xoaSP;
        CardView cardView;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewSP = itemView.findViewById(R.id.imageSPcart);
            textViewTenSP = itemView.findViewById(R.id.tv_tenspcart);
            textViewGiaSP = itemView.findViewById(R.id.tv_giaspcart);
            soluongSP = itemView.findViewById(R.id.tv_soluong_cart);
            xoaSP = itemView.findViewById(R.id.xoa_cart);
            cardView = itemView.findViewById(R.id.cart_item);
            remove = itemView.findViewById(R.id.removeitem);
            plus = itemView.findViewById(R.id.plusitem);
        }
    }
}
