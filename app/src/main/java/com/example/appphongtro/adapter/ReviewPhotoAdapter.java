package com.example.appphongtro.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appphongtro.R;
import com.example.appphongtro.model.PhotoReview;

import java.util.ArrayList;

public class ReviewPhotoAdapter extends RecyclerView.Adapter<ReviewPhotoAdapter.ViewHolder> {

    private ArrayList<PhotoReview> uriArrayList;

    public ReviewPhotoAdapter(ArrayList<PhotoReview> uriArrayList) {
        this.uriArrayList = uriArrayList;
    }

    @NonNull
    @Override
    public ReviewPhotoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.single_photo_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewPhotoAdapter.ViewHolder holder, int position) {
        Uri imageUri = uriArrayList.get(position).getImageUri();
        holder.img_photo_review.setImageURI(imageUri);
    }

    @Override
    public int getItemCount() {
        return uriArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView img_photo_review;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_photo_review = itemView.findViewById(R.id.img_photo_review);
        }
    }
}
