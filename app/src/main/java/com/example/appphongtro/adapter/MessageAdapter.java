package com.example.appphongtro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import static com.example.appphongtro.activity.ChatActivity.nameUserSend;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appphongtro.R;
import com.example.appphongtro.model.Message;
import com.example.appphongtro.session.SessionManagement;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter{
    Context context;
    ArrayList<Message> messagesAdpterArrayList;
    int ITEM_SEND=1;
    int ITEM_RECIVE=2;

    private SessionManagement sessionManagement;
    private int user_id;
    public MessageAdapter(Context context, ArrayList<Message> messagesAdpterArrayList) {
        this.context = context;
        this.messagesAdpterArrayList = messagesAdpterArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SEND){
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout, parent, false);
            return new senderVierwHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_layout, parent, false);
            return new reciverViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message messages = messagesAdpterArrayList.get(position);
        if (holder.getClass() == senderVierwHolder.class) {
            senderVierwHolder viewHolder = (senderVierwHolder) holder;
            viewHolder.msgsendertyp.setText(messages.getMessage());
            viewHolder.senderName.setText(nameUserSend);
        } else {
            reciverViewHolder viewHolder = (reciverViewHolder) holder;
            viewHolder.msgtxt.setText(messages.getMessage());
            viewHolder.receiverName.setText(messages.getUserName());
        }

    }
    @Override
    public int getItemCount() {
        return messagesAdpterArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message messages = messagesAdpterArrayList.get(position);
        sessionManagement = new SessionManagement(context);
        user_id = sessionManagement.getUserId();
        if (user_id == messages.getSenderId()) {
            return ITEM_SEND;
        } else {
            return ITEM_RECIVE;
        }
    }
     class senderVierwHolder extends RecyclerView.ViewHolder {
        TextView msgsendertyp, senderName;
        public senderVierwHolder(@NonNull View itemView) {
            super(itemView);
            senderName = itemView.findViewById(R.id.senderName);
            msgsendertyp = itemView.findViewById(R.id.msgsendertyp);

        }
    }
    class reciverViewHolder extends RecyclerView.ViewHolder {
        TextView msgtxt, receiverName;
        public reciverViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverName = itemView.findViewById(R.id.receiverName);
            msgtxt = itemView.findViewById(R.id.recivertextset);
        }
    }
}
