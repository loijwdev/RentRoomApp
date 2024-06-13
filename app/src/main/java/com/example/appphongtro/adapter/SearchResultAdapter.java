package com.example.appphongtro.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appphongtro.R;
import com.example.appphongtro.activity.DetailRoomActivity;
import com.example.appphongtro.model.Room;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder>{
    ArrayList<Room> lstRoom;

    Context context;
    int singleData;
    public SearchResultAdapter(Context context, ArrayList<Room> lstRoom, int singleData) {
        this.context = context;
        this.lstRoom = lstRoom;
        this.singleData = singleData;
    }
    @NonNull
    @Override
    public SearchResultAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.single_room, null);
        return new SearchResultAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultAdapter.ViewHolder holder, int position) {
        final Room room = lstRoom.get(position);
        byte[] image = room.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0 , image.length);
        holder.viewImgRoom.setImageBitmap(bitmap);
        holder.txt_name.setText(room.getName());
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedPrice = decimalFormat.format(room.getPrice() );
        holder.txt_price.setText(formattedPrice + " VND/1 tháng");
        holder.txt_address.setText(room.getAddress());
        holder.flowmenu.setVisibility(View.GONE);

        holder.viewImgRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int roomId= room.getId();
                Intent intent = new Intent(context, DetailRoomActivity.class);
                intent.putExtra("roomId", roomId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return  lstRoom.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView viewImgRoom;
        TextView txt_name, txt_price, txt_address;
        ImageButton flowmenu;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewImgRoom = itemView.findViewById(R.id.viewImgRoom);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_price = itemView.findViewById(R.id.txt_price);
            txt_address = itemView.findViewById(R.id.txt_address);
            flowmenu = itemView.findViewById(R.id.flowmenu);
        }
    }

}
