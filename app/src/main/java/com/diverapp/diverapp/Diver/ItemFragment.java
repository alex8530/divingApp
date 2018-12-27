package com.diverapp.diverapp.Diver;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.diverapp.diverapp.Config.GlideApp;
import com.diverapp.diverapp.MainActivity;
import com.diverapp.diverapp.R;
import com.diverapp.diverapp.Trip;
import com.diverapp.diverapp.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Modle.Order;
import database.DBManager;

import static com.diverapp.diverapp.MainActivity.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemFragment extends Fragment implements View.OnClickListener {

    MainActivity mainActivity;
    TextView title,discription,count ,locationName,zeroHoure,numberOfSetes,name,lastTrips,rating,priceTV;
    ImageView imageView,therdImageItemFragment,secondImageItemFragment,firstImageItemFragment;
    Button addToCart,incementor,decrmotor;
    int counter = 1 ;
    Context mContex;

    FirebaseDatabase database;
    @SuppressLint("ValidFragment")

    public ItemFragment(Trip trip,MainActivity mainActivity) {
        this.trip = trip;
        this.mainActivity = mainActivity;
    }
    Trip trip ;
    User user ;
    public ItemFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        Log.e(MainActivity.TAG, "onCreateView: "+ trip.getTicketNumber());
        System.out.println(trip.getProviderId());
        System.out.println(trip.getPrice());
        View rootView = inflater.inflate(R.layout.fragment_item, container, false);
        database = FirebaseDatabase.getInstance();
        listenToTicketNumber();
        title = (TextView) rootView.findViewById(R.id.title);
        addToCart = (Button) rootView.findViewById(R.id.addToCart);
        addToCart.setOnClickListener(this);
        discription = (TextView) rootView.findViewById(R.id.description2);
        incementor = (Button) rootView.findViewById(R.id.incrmonter);
        decrmotor = (Button) rootView.findViewById(R.id.decrmontor);

        count = (TextView) rootView.findViewById(R.id.numberOfTicket);
        imageView = (ImageView) rootView.findViewById(R.id.imageView);
        therdImageItemFragment = (ImageView) rootView.findViewById(R.id.therdImageItemFragment);
        firstImageItemFragment = (ImageView) rootView.findViewById(R.id.firstImageItemFragment);
        secondImageItemFragment= (ImageView) rootView.findViewById(R.id.secondImageItemFragment);

        locationName = (TextView) rootView.findViewById(R.id.infoLocation);
        zeroHoure = (TextView) rootView.findViewById(R.id.infoZeroHoure);
        numberOfSetes = (TextView) rootView.findViewById(R.id.infoNumberOfTeckets);
        priceTV = (TextView) rootView.findViewById(R.id.priceTV);
        setupTripAndPDiverInfo();
        name = (TextView) rootView.findViewById(R.id.pInfoName);
        lastTrips = (TextView) rootView.findViewById(R.id.pInfoLastTrip);
        rating = (TextView) rootView.findViewById(R.id.pInfoRating);

        loadTripImages();
//        GlideApp.with(this).load(trip.getImagesURL().get(0)).
//                placeholder(R.drawable.place_holder_image).into(imageView);
        title.setText(trip.getName());
        priceTV.setText(String.valueOf(trip.getPrice())+ " RS");
        discription.setText(trip.getDescription());


        // PRO DIVER
      //  name.setText(user.getName());
//        lastTrips.setText(String.valueOf(user.getTrips().size()));
//        rating.setText(user.getRating());

        incementor.setOnClickListener(this);
        decrmotor.setOnClickListener(this);
        return rootView;
    }

    private void loadTripImages() {
        List<ImageView> imageViews = new ArrayList<>();
        imageViews.add(firstImageItemFragment);
        imageViews.add(secondImageItemFragment);
        imageViews.add(therdImageItemFragment);
        if(trip.getImagesURL() != null){
            for(int i=0;i<trip.getImagesURL().size();i++){
                GlideApp.with(this).load(trip.getImagesURL().get(i)).placeholder(R.drawable.ic_assetlogo).centerCrop().into(imageViews.get(i));
                imageViews.get(i).setOnClickListener(this);
            }
        }
    }

    public void listenToTicketNumber(){
        Query ticketReff = database.getReference().child("Trips").orderByChild("ticketNumber");
        ticketReff.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                System.out.printf("Query----123");
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

    private void setupTripAndPDiverInfo() {

        locationName.setText(trip.getLocationName());
        zeroHoure.setText(trip.getSartDate());

        numberOfSetes.setText(String.valueOf(trip.getTicketNumber()));
    }

    @Override
    public void onClick(View v) {
//        public Order(int id, String prodictId, String prodictName, String quantity, String price, String discount, String provider) {
        switch (v.getId()){
            case R.id.incrmonter:
                increceTheTicketNumber();
                break;
            case R.id.decrmontor:
                decrsTheTicketNumber();
                break;
            case R.id.addToCart:
                createOrder();
                break;
            case R.id.firstImageItemFragment:
                Log.e(TAG, "onClick: "+ 1 );
                break;
            case R.id.secondImageItemFragment:
                Log.e(TAG, "onClick: "+ 2 );
                break;
            case R.id.therdImageItemFragment:
                Log.e(TAG, "onClick: "+ 3 );
                break;
        }

//        new Database(getContext()).addToCart(new Order("sdcsdc","sdcdsc","1","2332","0","iuuuuu"));
//        new Database(getContext()).addToTable();
        
    }

    private void createOrder() {
        mainActivity.mCartItemCount += 1;
        mainActivity.setupBadge();
        Log.e(TAG, "createOrder: "+ mainActivity.mCartItemCount);

        Order order = new Order(trip.getUid(),trip.getProviderId(),trip.getName(),counter,(((trip.getPrice()*5) /100) + trip.getPrice()),0,trip.getBankAccount());
        Log.e(TAG, "createOrder: "+order.getProviderID() );
        // ADD THE ORDER TO THE DATABASE
        System.out.println(counter);
//        new Database(getContext()).addToCart(order);
        DBManager dbManager= new DBManager(getContext());
        dbManager.open();
        dbManager.insert(order);
        System.out.println(trip.getName());
        dbManager.close();
        Toast.makeText(getContext(),"itmes added to cart Thank you",Toast.LENGTH_LONG).show();
        addToCart.setEnabled(false);
    }
    private void decrsTheTicketNumber() {
        if (counter != 0 ){
            counter -- ;
            count.setText(String.valueOf(counter));
            System.out.println(counter);
        }
    }
    private void increceTheTicketNumber() {
        if ( counter >= trip.getTicketNumber()  ) {
            return;
        }else {
            counter ++ ;
            System.out.println(trip.getTicketNumber());
            System.out.println(counter);
            count.setText(String.valueOf(counter));
        }
    }
}
