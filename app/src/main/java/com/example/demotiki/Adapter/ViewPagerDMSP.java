package com.example.demotiki.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.demotiki.AnotherClass.DanhMucSP;
import com.example.demotiki.Fragment.CategoryFragment;
import com.example.demotiki.Fragment.DanhMucFragment;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerDMSP extends FragmentStateAdapter {
    private ArrayList<DanhMucSP> categories;
    public ViewPagerDMSP(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, ArrayList<DanhMucSP> categories) {
        super(fragmentManager,lifecycle);
        this.categories = categories;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return CategoryFragment.newInstance(categories.get(position).getTendanhmuc());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
