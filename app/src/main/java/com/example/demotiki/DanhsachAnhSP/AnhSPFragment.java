package com.example.demotiki.DanhsachAnhSP;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.demotiki.R;
import com.makeramen.roundedimageview.RoundedImageView;

public class AnhSPFragment extends Fragment {
    private View mView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.img_danhsach_anh_sp,container,false);

        Bundle bundle = getArguments();
        AnhSP sliderItem = bundle.getParcelable("resourceId");
        Uri imageUri = sliderItem.getResourceId();
       ImageView imageSlide = (ImageView) mView.findViewById(R.id.img_anhsp_danhsach);
        Glide.with(this)
                .load(imageUri)
                .into(imageSlide);
        return mView;
    }
}
