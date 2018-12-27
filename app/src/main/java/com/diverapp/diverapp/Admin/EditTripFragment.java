package com.diverapp.diverapp.Admin;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.diverapp.diverapp.Config.GlideApp;
import com.diverapp.diverapp.R;
import com.diverapp.diverapp.Trip;
import com.diverapp.diverapp.User;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.diverapp.diverapp.MainActivity.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditTripFragment extends Fragment implements View.OnClickListener {

    public Trip trip;
    TextView userName,phone,tripN,discrip,locationName,ticketNumber,price10;
    Button activeButton, rejectButton;
    ImageView firstImage,secondImage,therdImage;
    EditText changPriceButton;
    public User user;

    FirebaseDatabase database;
    DatabaseReference ref;
    public EditTripFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_trip, container, false);

        changPriceButton = (EditText) rootView.findViewById(R.id.changPriceButton);
        database = FirebaseDatabase.getInstance();
        //USER
        userName = (TextView) rootView.findViewById(R.id.userName);
        phone = (TextView) rootView.findViewById(R.id.phone);
        // TRIP
        tripN = (TextView) rootView.findViewById(R.id.tripN);
        discrip = (TextView) rootView.findViewById(R.id.discrip);
        locationName = (TextView) rootView.findViewById(R.id.locationName);
        ticketNumber = (TextView) rootView.findViewById(R.id.ticketNumber);
        price10 = (TextView) rootView.findViewById(R.id.price10);

        // TRIP IMAGES
        firstImage = (ImageView) rootView.findViewById(R.id.firstImage);
        secondImage = (ImageView) rootView.findViewById(R.id.secondImage);
        therdImage = (ImageView) rootView.findViewById(R.id.therdImage);

        firstImage.setOnClickListener(this);
        secondImage.setOnClickListener(this);
        therdImage.setOnClickListener(this);

        Log.e(TAG, "onCreateView: "+ user );
        userName.setText(user.getName());
        phone.setText(user.getMobilNumber());

        tripN.setText(trip.getName());
        discrip.setText(trip.getDescription());
        locationName.setText(trip.getLocationName());
        ticketNumber.setText(String.valueOf(trip.getTicketNumber()));
        price10.setText(String.valueOf(trip.getPrice()));
        changPriceButton.setText(String.valueOf(trip.getPrice()));


        activeButton = (Button) rootView.findViewById(R.id.activeButton);
        rejectButton = (Button) rootView.findViewById(R.id.rejectButton);
        activeButton.setOnClickListener(this);
        rejectButton.setOnClickListener(this);


        loadImages();
        return rootView;
    }
    private void loadImages() {
        List<ImageView> imageViews = new ArrayList<>();
        imageViews.add(firstImage);
        imageViews.add(secondImage);
        imageViews.add(therdImage);
        if(trip.getImagesURL() != null){
            for(int i=0;i<trip.getImagesURL().size();i++){
                GlideApp.with(this).load(trip.getImagesURL().get(i)).placeholder(R.drawable.ic_assetlogo).centerCrop().into(imageViews.get(i));
            }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activeButton:
                activiteTheTrip();
                break;
            case R.id.rejectButton:
                rejectTheTrip();
                break;
        }
    }
    private void activiteTheTrip() {
        ref = database.getReference().child("Trips").child(trip.getUid());

        String price = changPriceButton.getText().toString().trim();
        if (!price.isEmpty()){
            int intPrice = Integer.parseInt(price);
            ref.child("price").setValue(intPrice);
        }
        System.out.println(ref + "  ");
        ref.child("activion").setValue(1);;
    }
    private void rejectTheTrip(){
        ref = database.getReference().child("Trips").child(trip.getUid());
        ref.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                Toast.makeText(getContext(),"The trip has been deleted",Toast.LENGTH_LONG).show();
                removeFragment();
            }
        });
    }
    private void removeFragment() {
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }
}
