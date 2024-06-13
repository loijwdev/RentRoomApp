package com.example.appphongtro.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.appphongtro.fragments.SlideFragment;
import com.example.appphongtro.model.PhotoSlide;

import java.util.List;

public class SlideAdapter extends FragmentStateAdapter {
    private List<PhotoSlide> lstSlide;
    public SlideAdapter(@NonNull FragmentActivity fragmentActivity, List<PhotoSlide> lst) {
        super(fragmentActivity);
        this.lstSlide = lst;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        PhotoSlide photoSlide = lstSlide.get(position);

        Bundle bundle = new Bundle();
        bundle.putSerializable("object_slide", photoSlide);

        SlideFragment slideFragment = new SlideFragment();
        slideFragment.setArguments(bundle);
        return slideFragment;
    }

    @Override
    public int getItemCount() {
        if(lstSlide != null) {
            return lstSlide.size();
        }
        return 0;
    }
}
