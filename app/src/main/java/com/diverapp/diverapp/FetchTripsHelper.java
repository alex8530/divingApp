package com.diverapp.diverapp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Modle.Notifi;
import Modle.Request;

import static android.support.constraint.Constraints.TAG;

public class FetchTripsHelper {

    User user;
    FirebaseDatabase database;
    public DatabaseReference myRef;

    public FetchTripsHelper(){
        database = FirebaseDatabase.getInstance();

    }
    public FetchTripsHelper(User user){
        this.user = user;
    }
    List<Notifi> notifis = new ArrayList<>();

    List<Trip> tripList = new ArrayList<>();
    List<Trip> storeList = new ArrayList<>();
    final List<User> users = new ArrayList<>();
    final List<Request> requests = new ArrayList<>();


    DatabaseReference tripsRef;
    DatabaseReference requestsRef;
    public void fetchTheUsers() {

        tripsRef = database.getReference().child("Trips");
        requestsRef = database.getReference().child("Requests");
//        Query query = tripsRef.child("startDate").orderByKey();
        tripsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                HashMap<String, Object> object = (HashMap<String, Object>) dataSnapshot.getValue();

//                for (String key : object.keySet()) {
//                    Map<String, Object> opbb = (Map<String, Object>) object.get(key);
                    Trip trip = dataSnapshot.getValue(Trip.class);

                    tripList.add(trip);
//                    Log.e(TAG, "onChildAdded: "+  trip.getProviderId());
                    if ((trip.activion == 1) &&(trip.getTicketNumber() > 0 )){
                        storeList.add(0,trip);
                    }
////                    Log.e(TAG, "onChildAdded: "+trip.getTicketNumber()+" "+trip.getActivion());
//                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // Here add the trip to store after feved it
//                Log.e(MainActivity.TAG, "onChildChanged: dataSnapshot.getKey() "+ dataSnapshot.getKey() );
//                Log.e(MainActivity.TAG, "onChildChanged: dataSnapshot.getValue() "+ dataSnapshot.getValue() );
                Log.e(TAG, "onChildChanged: Query ----321" );
                HashMap<String,Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
//                for (String key : map.keySet()) {
//                    Map<String, Object> opbb = (Map<String, Object>) map.get(key);
//                    Trip trip = new Trip(dataSnapshot.getKey(), opbb);
//                    trip.setProviderId(key);
//                    for (int i=0;i<storeList.size();i++){
//                        Trip oldTrip = storeList.get(i);
//                        if(trip.getUid().equals(oldTrip.getUid())){
//                            storeList.add(storeList.indexOf(oldTrip),trip);
//                            storeList.remove(oldTrip);
//                        }
//                    }
//                }
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
        // fetch the requests
        requestsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Map<String,Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                List<Map<Key,Object>> orders = (List<Map<Key, Object>>) map.get("tickets");
                for (int i=0;i<orders.size();i++){
//                    Order order = orders.get(i);
                }
                Request request = new Request(map);
                requests.add(request);
//                Log.e(TAG, "onChildAdded: "+ requests.size() + " "+ request.getUserUid() );
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
        myRef = database.getReference().child("Users");
        // fetch the users
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Map<String,Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
//                User user1 = new User(dataSnapshot.getKey(), (HashMap<String, Object>) map);
//                Log.e(TAG, "onChildAdded: "+ dataSnapshot.getKey() );
//                completedUser(user1);
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

    }
    public void fetchTheTripsAndRequests(){

    }
    public void completedUser(User user1){
        for (int i= 0;i<requests.size(); i++){
            Request request = requests.get(i);
//            if (user1.getUid().equals(request.getUserUid())){
//                user1.requests.add(request);
//            }
//            Log.e(TAG, "requests: "+ user1.getUid() );
        }
        for (int i =0;i<tripList.size();i++){
//            Log.e(MainActivity.TAG, "tripList: "+ user1.getUid() );
            Trip trip = tripList.get(i);
//            Log.e(TAG, "onChildAdded: trrrip"+ trip.getName() );
            if (trip.activion == 1 && trip.ticketNumber > 0){
//                storeList.add(trip);
//                Log.e(TAG, "onChildAdded:--------------------------- "+storeList.size()+" "+ tripList.size() );
            }
//            if (trip.getProviderId().equals(user1.getUid())){
//                user1.trips.add(trip);
//            }
        }
        users.add(user1);
//        addTripsToUsers();
    }
    public void addTripsToUsers(){

        tripsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Map<String,Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                Trip trip = dataSnapshot.getValue(Trip.class);
                for (int i=0;i<users.size();i++){
                    User user = users.get(i);
//                    if (user.getUid().equals(trip.getProviderId())){
//                        user.getTrips().add(trip);
//
//                    }
                }
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
    }
}
