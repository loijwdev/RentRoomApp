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

public class ListImageOfRoomAdapter extends RecyclerView.Adapter<ListImageOfRoomAdapter.ViewHolder>{
    private ArrayList<byte[]> imageList;

    public ListImageOfRoomAdapter(ArrayList<byte[]> imageList) {
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public ListImageOfRoomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_photo_room, parent, false);
        return new ListImageOfRoomAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListImageOfRoomAdapter.ViewHolder holder, int position) {
        byte[] image = imageList.get(position);
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        holder.img_photo_room_detail.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_photo_room_detail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_photo_room_detail = itemView.findViewById(R.id.img_photo_room_detail);
        }
    }
}
