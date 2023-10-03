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
    private Context mcontext;
    CountImage countImage;
    public RecycleAdapter(ArrayList<Uri> arrayList, Context context,CountImage countImage){
        this.uris = arrayList;
        this.mcontext = context;
        this.countImage = countImage;
    }
    @NonNull
    @Override
    public RecycleAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_image,parent,false);
        return new RecyclerViewHolder(view,countImage);
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

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView,delete;
        CountImage countImage;
        public RecyclerViewHolder(@NonNull View itemView, CountImage countImage) {
            super(itemView);
            this.countImage = countImage;
            imageView = itemView.findViewById(R.id.image_select);
            delete = itemView.findViewById(R.id.delete_image);
        }
    }

    public interface CountImage{
        void clicked(int getSize);
    }
}
