package com.example.demotiki.Slider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.demotiki.Adapter.SanPhamAdapter;
import com.example.demotiki.Fragment.HomeFragment;

import java.util.List;

public class SliderAdapter extends FragmentStateAdapter {
    private List<SliderTin> mListSlide;

    public SliderAdapter(@NonNull HomeFragment fragmentActivity, List<SliderTin> mListSlide) {
        super(fragmentActivity);
        this.mListSlide = mListSlide;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        SliderTin sliderItem = mListSlide.get(position);

        //Xét bundle vào Fragment
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_slide",sliderItem);
        SliderFragment sliderFragment = new SliderFragment();
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
