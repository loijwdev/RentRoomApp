package com.example.appphongtro.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appphongtro.R;
import com.example.appphongtro.activity.DetailRoomActivity;
import com.example.appphongtro.activity.MainActivity;
import com.example.appphongtro.database.RoomTbl;
import com.example.appphongtro.model.Room;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ManageRoomAdapter extends RecyclerView.Adapter<ManageRoomAdapter.ViewHolder>{

    Context context;
    ArrayList<Room> lstRoom;
    int singleData;
    private RoomTbl roomTbl;


    public ManageRoomAdapter(Context context, ArrayList<Room> lstRoom, int singleData) {
        this.context = context;
        this.lstRoom = lstRoom;
        this.singleData = singleData;
    }

    @NonNull
    @Override
    public ManageRoomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.single_room, null);
        return new ManageRoomAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageRoomAdapter.ViewHolder holder, int position) {
        final Room room = lstRoom.get(position);
        byte[] image = room.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0 , image.length);
        holder.viewImgRoom.setImageBitmap(bitmap);
        holder.txt_name.setText(room.getName());
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedPrice = decimalFormat.format(room.getPrice());
        holder.txt_price.setText(formattedPrice + " VND/1 tháng");
        holder.txt_address.setText(room.getAddress());
        holder.flowmenu.setVisibility(View.VISIBLE);
        holder.viewImgRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int roomId= room.getId();
                Intent intent = new Intent(context, DetailRoomActivity.class);
                intent.putExtra("roomId", roomId);
                context.startActivity(intent);
            }
        });
        holder.flowmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, holder.flowmenu);
                popupMenu.inflate(R.menu.flow_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int itemId = menuItem.getItemId();
                        if (itemId == R.id.edit_menu) {
                            Bundle bundle = new Bundle();
                            bundle.putInt("id", room.getId());
                            bundle.putString("name", room.getName());
                            //bundle.putByteArray("img", room.getImage());
                            bundle.putString("address", room.getAddress());
                            bundle.putDouble("price", room.getPrice());
                            bundle.putDouble("are", room.getAre());
                            bundle.putString("amenities", room.getAmenities());
                            bundle.putString("description", room.getDescription());
                            bundle.putString("rules", room.getRules());
                            bundle.putDouble("deposit_money", room.getDepositMoney());
                            bundle.putDouble("electricity_money", room.getElectricity_money());
                            bundle.putDouble("water_money", room.getWater_money());
                            bundle.putDouble("internet_money", room.getInternet_money());
                            bundle.putString("parking_fee", room.getParking_fee());
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("dataRoom", bundle);
                            context.startActivity(intent);
                            // Handle edit menu click
                        } else if (itemId == R.id.delete_menu) {
                            roomTbl = new RoomTbl(context);
                            boolean deleteRoom = roomTbl.deleteRoom(room.getId());
                            if(deleteRoom == true) {
                                Toast.makeText(context, "Xóa phòng thành công", Toast.LENGTH_SHORT).show();
                                lstRoom.remove(position);
                                notifyDataSetChanged();
                            }
                        } else {
                            return false;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstRoom.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton flowmenu;
        ImageView viewImgRoom;
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
