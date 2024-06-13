package com.example.appphongtro.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.appphongtro.activity.ManageAccountActivity;
import com.example.appphongtro.database.UserTbl;
import com.example.appphongtro.model.User;

import java.util.List;

public class UserAccountAdapter extends RecyclerView.Adapter<UserAccountAdapter.ViewHolder>{

    Context context;
    List<User> lstUser;
    int singleData;
    UserTbl userTbl;

    public UserAccountAdapter(Context context, List<User> lstUser, int singleData) {
        this.context = context;
        this.lstUser = lstUser;
        this.singleData = singleData;
    }

    @NonNull
    @Override
    public UserAccountAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.single_room, null);
        return new UserAccountAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAccountAdapter.ViewHolder holder, int position) {
        final User user = lstUser.get(position);
        if (user.getAvatar() != null) {
            byte[] image = user.getAvatar();
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            holder.viewImgRoom.setImageBitmap(bitmap);
            ;
        } else {
            holder.viewImgRoom.setImageResource(R.drawable.profile);
        }
        holder.txt_name.setText(user.getFullName());
        holder.txt_price.setText(user.getPhoneNumber());
        holder.txt_address.setText(user.getAddress());
        holder.flowmenu.setVisibility(View.VISIBLE);
        holder.txt_role.setVisibility(View.VISIBLE);
        if(user.getRole() == 1) {
            holder.txt_role.setText("Chủ nhà");
        } else {
            holder.txt_role.setText("Người thuê");
        }
        holder.viewImgRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ManageAccountActivity.class);
                intent.putExtra("user_id", user.getId());
                context.startActivity(intent);
            }
        });

        holder.flowmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, holder.flowmenu);
                popupMenu.inflate(R.menu.menu_user);

                // Get the menu item for unlock_user
                MenuItem unlockUserMenuItem = popupMenu.getMenu().findItem(R.id.unlock_user);

                // Set the visibility based on the user's lock status
               // unlockUserMenuItem.setVisible(user.getIsLocked() == 1);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int itemId = menuItem.getItemId();
                        if (itemId == R.id.block_user) {
                            userTbl = new UserTbl(context);
                            userTbl.updateUserIsLocked(user.getId(), 1);
                            Toast.makeText(context, "Đã khóa người dùng", Toast.LENGTH_LONG).show();
                        } else if (itemId == R.id.unlock_user) {
                            userTbl = new UserTbl(context);
                            userTbl.updateUserIsLocked(user.getId(), 0);
                            Toast.makeText(context, "Đã mở khóa người dùng", Toast.LENGTH_LONG).show();
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
        return lstUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView viewImgRoom;
        ImageButton flowmenu;

        TextView txt_name, txt_price, txt_address, txt_role;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            flowmenu = itemView.findViewById(R.id.flowmenu);
            viewImgRoom = itemView.findViewById(R.id.viewImgRoom);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_price = itemView.findViewById(R.id.txt_price);
            txt_address = itemView.findViewById(R.id.txt_address);
            txt_role = itemView.findViewById(R.id.txt_role);
        }
    }
}
