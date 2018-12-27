package com.diverapp.diverapp.Diver;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.diverapp.diverapp.MainActivity;
import com.diverapp.diverapp.R;
import com.diverapp.diverapp.Trip;
import com.diverapp.diverapp.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopFragment extends Fragment {


    public List<Trip> storeList= new ArrayList<>();
    public List<User> users;
    public MainActivity mainActivity;
    public ShopFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        System.out.println("SHOPPING FRAGMENT onCreateView"+ storeList.size());
        View rootView =  inflater.inflate(R.layout.fragment_shop, container, false);
        final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.itemsRecyclerView);
        final ShoppingItemsAdapter[] shoppingItemsAdapter = {new ShoppingItemsAdapter(storeList, getContext(),
                this, users, mainActivity)};

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(shoppingItemsAdapter[0]);
        FirebaseDatabase.getInstance().getReference().child("Trips").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                     Trip trip = new Trip();
                     trip=dataSnapshot.getValue(Trip.class);
                    if ((trip.getActivion() == 1) &&(trip.getTicketNumber() > 0 )){
                        storeList.add(0,trip);
                        shoppingItemsAdapter[0].notifyDataSetChanged();
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

//                    Trip trip = dataSnapshot.getValue(Trip.class);
//
//                    for (int i=0;i<storeList.size();i++){
//                        Trip oldTrip = storeList.get(i);
//                        if(trip.getUid().equals(oldTrip.getUid())){
//                            storeList.add(storeList.indexOf(oldTrip),trip);
//                            storeList.remove(oldTrip);
//                        }
//                    }
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


        SearchView searchView=rootView.findViewById(R.id.edit_search_text);
        // perform set on query text listener event
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
// do something on text submit
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
// do something when text changes
                List<Trip> searchtrip=new ArrayList<>();//create new list to get search tag
                int i=0;
                for (i=0;i<storeList.size();i++)

                    try {
                        if (storeList.get(i).getLocationName().contains(newText)) {
                            searchtrip.add(storeList.get(i));
                            shoppingItemsAdapter[0] = new ShoppingItemsAdapter(searchtrip, getContext(), ShopFragment.this
                                    , users, mainActivity);
                            recyclerView.setAdapter(shoppingItemsAdapter[0]);
                        }
                    }catch (Exception exc){}
                try {
                    if (storeList.get(i).getLocationName().contains(newText)
                            || storeList.get(i).getTripClass().contains(newText)
                            || storeList.get(i).getProvider_name().contains(newText)) {
                        searchtrip.add(storeList.get(i));
                        shoppingItemsAdapter[0] = new ShoppingItemsAdapter(searchtrip, getContext(), ShopFragment.this
                                , users, mainActivity);
                        recyclerView.setAdapter(shoppingItemsAdapter[0]);
                    }
                }catch (Exception exc){}
                try {
                    if (storeList.get(i).getLocationName().contains(newText)
                            || storeList.get(i).getTripClass().contains(newText)
                            || storeList.get(i).getProvider_name().contains(newText)) {
                        searchtrip.add(storeList.get(i));
                        shoppingItemsAdapter[0] = new ShoppingItemsAdapter(searchtrip, getContext(), ShopFragment.this
                                , users, mainActivity);
                        recyclerView.setAdapter(shoppingItemsAdapter[0]);
                    }
                }catch (Exception exc){}
                return  false;
            }});
        return  rootView;
    }

}
