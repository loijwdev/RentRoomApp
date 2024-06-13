package com.example.appphongtro.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appphongtro.R;
import com.example.appphongtro.model.PhotoSlide;

public class SlideFragment extends Fragment {
    private View mView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_slide, container, false);

        Bundle bundle = getArguments();
        PhotoSlide photoSlide = (PhotoSlide) bundle.get("object_slide");

        ImageView img_slide = mView.findViewById(R.id.img_slide);
        img_slide.setImageResource(photoSlide.getRsourceId());
        return mView;
    }
}
