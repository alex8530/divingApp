package com.diverapp.diverapp;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Modle.Notifi;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {


//    List<Trip> tripList;
    public List<Notifi> notifiList = new ArrayList<>();
    RecyclerView recyclerView;
    User user;
    public NotificationFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final NotificationAdapter notificationAdapter = new NotificationAdapter(getContext(),notifiList);
        DatabaseReference dRef=FirebaseDatabase.getInstance().getReference().child("Notification").child(user.getUid());
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);
        recyclerView = (RecyclerView ) rootView.findViewById(R.id.notiRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(notificationAdapter);


        dRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Map<String,Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                Notifi noti = new Notifi(map);
                notifiList.add(noti);
                notificationAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return rootView;
    }
}
