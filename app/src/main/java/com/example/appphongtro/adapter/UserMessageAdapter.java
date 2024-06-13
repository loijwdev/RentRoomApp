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
import com.example.appphongtro.activity.ChatActivity;
import com.example.appphongtro.model.Room;
import com.example.appphongtro.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserMessageAdapter extends RecyclerView.Adapter<UserMessageAdapter.ViewHolder> {
    Context context;
    List<User> lstUser;
    int singleData;

    public UserMessageAdapter(Context context, List<User> lstUser, int singleData) {
        this.context = context;
        this.lstUser = lstUser;
        this.singleData = singleData;
    }

    @NonNull
    @Override
    public UserMessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.single_room, null);
        return new UserMessageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserMessageAdapter.ViewHolder holder, int position) {
        final User user = lstUser.get(position);
        if(user.getAvatar() != null) {
            byte [] image = user.getAvatar();
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0 , image.length);
            holder.viewImgRoom.setImageBitmap(bitmap);;
        } else {
            holder.viewImgRoom.setImageResource(R.drawable.profile);
        }
        holder.txt_name.setText(user.getFullName());
        holder.txt_price.setText(user.getPhoneNumber());
        holder.txt_address.setText(user.getAddress());
        holder.flowmenu.setVisibility(View.GONE);

        holder.viewImgRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("receiver_id", user.getId());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return lstUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView viewImgRoom;
        ImageButton flowmenu;

        TextView txt_name, txt_price, txt_address;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            flowmenu = itemView.findViewById(R.id.flowmenu);
            viewImgRoom = itemView.findViewById(R.id.viewImgRoom);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_price = itemView.findViewById(R.id.txt_price);
            txt_address = itemView.findViewById(R.id.txt_address);
        }
    }
}
