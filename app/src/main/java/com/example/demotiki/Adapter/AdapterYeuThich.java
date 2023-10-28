package com.example.demotiki.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.demotiki.AnotherClass.Cart;
import com.example.demotiki.AnotherClass.YeuThich;
import com.example.demotiki.ChiTietSanPham.ChiTietSPActivity;
import com.example.demotiki.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AdapterYeuThich extends RecyclerView.Adapter<AdapterYeuThich.YeuThichViewHolder>{

    private List<YeuThich> favList;
    private Context context;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public AdapterYeuThich(List<YeuThich> favList, Context context) {
        this.favList = favList;
        this.context = context;
    }

    @NonNull
    @Override
    public YeuThichViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_yeuthich,parent,false);
        return new YeuThichViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YeuThichViewHolder holder, @SuppressLint("RecyclerView") int position) {
        YeuThich yeuThich = favList.get(position);
        holder.tensp.setText(yeuThich.getTenSP());
        double price = yeuThich.getGiasp();
        // Định dạng số và thêm dấu phân cách hàng nghìn
        @SuppressLint("DefaultLocale") String formattedPrice = String.format("%,.0f", price);
        holder.giasp.setText(formattedPrice+ " VNĐ");
        Glide.with(context)
                .load(yeuThich.getAnhSP())
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

                Query query = ref.child("SanPham").child(favList.get(position).getIdsp());
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
                        intent.putExtra("idsp",favList.get(position).getIdsp());
                        intent.putExtra("iduser", userId);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(context);
                view = inflater.inflate(R.layout.dialog_xoayeuthich,null);
                AlertDialog.Builder b = new AlertDialog.Builder(context);
                AlertDialog dialog = b.setView(view).create();
                dialog.show();
                Button ok = dialog.findViewById(R.id.btn_xoayeuthich_item);
                Button huy = dialog.findViewById(R.id.btn_huy_xoa_yeuthichitem);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("YeuThich").child("yeu_thich_"+user.getUid());
                        ref.child("yeu_thich_"+ yeuThich.getIdsp()).removeValue();
                        dialog.dismiss();
                        Toast.makeText(context,"Xóa sản phẩm khỏi danh sách yêu thích thành công",Toast.LENGTH_SHORT).show();
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
        if(favList == null){
            return 0;
        }
        return favList.size();
    }

    public static class YeuThichViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,fav;
        TextView tensp,giasp;

        public YeuThichViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSPfav);
            fav = itemView.findViewById(R.id.img_favorited);
            tensp = itemView.findViewById(R.id.tv_tenspfv);
            giasp = itemView.findViewById(R.id.tv_giaspfav);
        }
    }
}
