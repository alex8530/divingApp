package com.diverapp.diverapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import Modle.Notifi;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private Context mCtx;
    private List<Notifi> notifis;

    public NotificationAdapter(Context mCtx, List<Notifi> notifs) {
        this.mCtx = mCtx;
        this.notifis = notifs;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(mCtx);
        View view = layoutInflater.inflate(R.layout.notification_cell,null);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder notificationViewHolder, int i) {
        Notifi notifi = notifis.get(i);
        notificationViewHolder.contentTV.setText("Your trip  ("+notifi.getTitle()+") is "+ notifi.getBody());
    }
    @Override
    public int getItemCount() {
        return notifis.size();
    }

    //    private List<Report> reports;
    public class NotificationViewHolder extends RecyclerView.ViewHolder{

        TextView contentTV;
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            contentTV = itemView.findViewById(R.id.notificationTV);
        }
    }
}
