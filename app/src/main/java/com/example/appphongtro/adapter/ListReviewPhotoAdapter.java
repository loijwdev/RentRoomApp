package com.example.appphongtro.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appphongtro.R;

import java.util.ArrayList;

public class ListReviewPhotoAdapter extends RecyclerView.Adapter<ListReviewPhotoAdapter.ViewHolder> {
    private ArrayList<byte[]> imageList;

    public ListReviewPhotoAdapter(ArrayList<byte[]> imageList) {
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public ListReviewPhotoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_photo_review, parent, false);
        return new ListReviewPhotoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListReviewPhotoAdapter.ViewHolder holder, int position) {
        byte[] image = imageList.get(position);
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        holder.img_photo_review.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_photo_review;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_photo_review = itemView.findViewById(R.id.img_photo_review);
        }
    }
}
