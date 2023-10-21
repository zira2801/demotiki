package com.example.demotiki.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.demotiki.AnotherClass.SanPham;
import com.example.demotiki.ChiTietSanPham.ChiTietSPActivity;
import com.example.demotiki.R;

import java.util.ArrayList;
import java.util.List;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.SanPhamViewHolder> {
        private List<SanPham> phamList;
        private Context context;


    public SanPhamAdapter(List<SanPham> phamList, Context context) {
        this.phamList = phamList;
        this.context = context;
    }

    @NonNull
    @Override
    public SanPhamAdapter.SanPhamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_product_cart_view,parent,false);
        return new SanPhamViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "ResourceType"})
    @Override
    public void onBindViewHolder(@NonNull SanPhamViewHolder holder, @SuppressLint("RecyclerView") int position) {
        SanPham sanPham =phamList.get(position);
        holder.textViewCart.setText(sanPham.getTensp());
        holder.ratingBar.setIsIndicator(true);
        double price = sanPham.getGiaban();
        // Định dạng số và thêm dấu phân cách hàng nghìn
        @SuppressLint("DefaultLocale") String formattedPrice = String.format("%,.0f", price);
        holder.gia.setText(formattedPrice +" VND");
        String rateString = sanPham.getDanhgia();
        String[] rateParts = rateString.split("/");
        if (rateParts.length > 0) {
            try {
                float rating = Float.parseFloat(rateParts[0]);
                holder.ratingBar.setRating(rating);
            } catch (NumberFormatException e) {
                // Xử lý nếu chuỗi không thể chuyển đổi thành số
            }
        }
      ArrayList<String> dsAnh = sanPham.getDsAnh();
        if(dsAnh != null && dsAnh.size() > 0) {
            String anhDauTien = dsAnh.get(0);
            // load ảnh vào ImageView
            Glide.with(context)
                    .load(anhDauTien)
                    .into(holder.ProductCartView);
        }

        holder.ProductCartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChiTietSPActivity.class);
                intent.putExtra("danhmucsp",phamList.get(position).getDanhMucSP());
                intent.putExtra("motasp",phamList.get(position).getMoTa());
                String[] arrayAnh = dsAnh.toArray(new String[0]);
                intent.putExtra("arrayAnh", arrayAnh);
                intent.putExtra("tensp",phamList.get(position).getTensp());
                intent.putExtra("giasp",phamList.get(position).getGiaban());
                intent.putExtra("xuatxusp",phamList.get(position).getXuatXu());
                intent.putExtra("thuonghieu",phamList.get(position).getThuongHieu());
                intent.putExtra("danhgiasp",phamList.get(position).getDanhgia());
                intent.putExtra("baohanh",phamList.get(position).getCheckBaoHanh());
                intent.putExtra("tgbaohanh",phamList.get(position).getThoiGianBaoHanh());
                intent.putExtra("idsp",phamList.get(position).getItemId());
                intent.putExtra("iduser",phamList.get(position).getIdUser());

                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        if(phamList == null){
            return 0;
        }
        return phamList.size();
    }

    public static class SanPhamViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout item_cart;
        ImageView ProductCartView;
        TextView textViewCart,gia;
        RatingBar ratingBar;
        public SanPhamViewHolder(@NonNull View itemView) {
            super(itemView);
            ProductCartView = itemView.findViewById(R.id.imageViewProductCart);
            textViewCart = itemView.findViewById(R.id.textviewProductCart);
            gia = itemView.findViewById(R.id.textview_gia);
            ratingBar = itemView.findViewById(R.id.rating_sanpham);
            item_cart = itemView.findViewById(R.id.item_cart_product);
        }
    }
}
