package com.diverapp.diverapp.Admin;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.diverapp.diverapp.MainActivity;
import com.diverapp.diverapp.R;
import com.diverapp.diverapp.Trip;
import com.diverapp.diverapp.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TripsFragment extends Fragment {


    public List<Trip> tripList;
    public List<User> users;
    RecyclerView recyclerView;

    public TripsFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_trips, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.tripsList);
        final TripCellAdapter tripCellAdapter = new TripCellAdapter(tripList,getContext(),this,users);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(tripCellAdapter);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Trips");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                HashMap<String, Object> object = (HashMap<String, Object>) dataSnapshot.getValue();
//                Trip trip = new Trip(dataSnapshot.getKey(), object);
//                tripList.add(trip);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                HashMap<String, Object> object = (HashMap<String, Object>) dataSnapshot.getValue();
                Trip trip = dataSnapshot.getValue(Trip.class);
                for (int i=0;i<tripList.size();i++){
                    Trip oldTrip = tripList.get(i);
                    if (trip.getUid().equals(oldTrip.getUid())){
                        tripList.remove(i);
                        tripList.add(i,trip);
                        tripCellAdapter.notifyDataSetChanged();
                    }
                }
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
        Log.e(MainActivity.TAG, "onCreateView: The Root"+ tripList.size() );
        return rootView;
    }

}
