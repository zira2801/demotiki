package com.example.demotiki.DangSanPham;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demotiki.R;

import java.util.ArrayList;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.RecyclerViewHolder> {

    private final ArrayList<Uri> uris;
    private final Context mcontext;
    CountImage countImage;
    private final itemClickListener itemClickListener1;
    public RecycleAdapter(ArrayList<Uri> arrayList, Context context,CountImage countImage,itemClickListener itemClickListener){
        this.uris = arrayList;
        this.mcontext = context;
        this.countImage = countImage;
        this.itemClickListener1 = itemClickListener;
    }
    @NonNull
    @Override
    public RecycleAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_image,parent,false);
        return new RecyclerViewHolder(view,countImage,itemClickListener1);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.imageView.setImageURI(uris.get(position));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uris.remove(uris.get(position));
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,getItemCount());
                countImage.clicked(uris.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return uris.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageView,delete;
        CountImage countImage;
        itemClickListener itemClickListener;
        public RecyclerViewHolder(@NonNull View itemView, CountImage countImage,itemClickListener itemClickListener) {
            super(itemView);
            this.countImage = countImage;
            imageView = itemView.findViewById(R.id.image_select);
            delete = itemView.findViewById(R.id.delete_image);
            this.itemClickListener = itemClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(itemClickListener != null){
                itemClickListener.itemClick(getAdapterPosition());
            }
        }
    }

    //Interface số lượng ảnh
    public interface CountImage{
        void clicked(int getSize);
    }

    //Interface zoom ảnh
    public interface itemClickListener{
        void itemClick(int position);
    }
}
