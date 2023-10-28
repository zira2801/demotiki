package com.example.demotiki.DanhsachAnhSP;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.demotiki.ChiTietSanPham.ChiTietSPActivity;
import com.example.demotiki.Fragment.HomeFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DSAnhSPAdapter extends FragmentStateAdapter {
    private ArrayList<AnhSP> mListSlide;

    public DSAnhSPAdapter(@NonNull ChiTietSPActivity fragmentActivity, ArrayList<AnhSP> mListSlide) {
        super(fragmentActivity);
        this.mListSlide = mListSlide;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        AnhSP sliderItem = mListSlide.get(position);

        //Xét bundle vào Fragment
        Bundle bundle = new Bundle();
        bundle.putParcelable("resourceId",sliderItem);
        AnhSPFragment sliderFragment = new AnhSPFragment();
        sliderFragment.setArguments(bundle);
        return sliderFragment;
    }

    @Override
    public int getItemCount() {
        if(mListSlide != null){
            return mListSlide.size();
        }
        return 0;
    }

}
