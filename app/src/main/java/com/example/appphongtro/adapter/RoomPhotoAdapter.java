package com.example.appphongtro.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appphongtro.R;
import com.example.appphongtro.model.PhotoRoom;

import java.util.ArrayList;

public class RoomPhotoAdapter extends RecyclerView.Adapter<RoomPhotoAdapter.ViewHolder>{

    private ArrayList<PhotoRoom> photoRooms;

    public RoomPhotoAdapter(ArrayList<PhotoRoom> photoRooms) {
        this.photoRooms = photoRooms;
    }

    @NonNull
    @Override
    public RoomPhotoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.single_photo_review, parent, false);
        return new RoomPhotoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomPhotoAdapter.ViewHolder holder, int position) {
        Uri imageRoomUri = photoRooms.get(position).getImageRoomUri();
        holder.img_photo_review.setImageURI(imageRoomUri);
    }

    @Override
    public int getItemCount() {
        return photoRooms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_photo_review;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_photo_review = itemView.findViewById(R.id.img_photo_review);

        }
    }
}
