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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.diverapp.diverapp.MainActivity;
import com.diverapp.diverapp.ProfileAdapter;
import com.diverapp.diverapp.R;
import com.diverapp.diverapp.Trip;
import com.diverapp.diverapp.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import static com.diverapp.diverapp.MainActivity.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserAdminProfileFragment extends Fragment implements View.OnClickListener {

    private ImageView divingIDImage,idImage;
    private RecyclerView recyclerView;
    private ProfileAdapter profileAdapter;
    private TextView userName,title,divingLevel,divingId,rating ,phoneNumber,birthDay;;
    public ImageView userImage;
    public MainActivity mainActivity;
    private LinearLayout birthDaylayou,phoneLayout;
    public User user;

    public UserAdminProfileFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user_admin_profile, container, false);


        phoneLayout = (LinearLayout) rootView.findViewById(R.id.phoneLayout);
        birthDaylayou = (LinearLayout) rootView.findViewById(R.id.birthDaylayou);
        userName = (TextView) rootView.findViewById(R.id.profileUserName);
        title = (TextView) rootView.findViewById(R.id.profileUserTitle);
//        logout = (Button) rootView.findViewById(R.id.logout);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.profile_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userImage = (ImageView) rootView.findViewById(R.id.userImageProfile);
        rating = (TextView) rootView.findViewById(R.id.rating);
        divingLevel = (TextView) rootView.findViewById(R.id.divingLevel);
        divingId = (TextView) rootView.findViewById(R.id.divingId);
        divingIDImage = (ImageView) rootView.findViewById(R.id.divingIDImage);
        idImage = (ImageView) rootView.findViewById(R.id.idImage);

        phoneNumber = (TextView) rootView.findViewById(R.id.phoneNumber);
        birthDay = (TextView) rootView.findViewById(R.id.birthDay);

//        logout.setOnClickListener(this);
//        if (user != null){
//            fetchUserTrips();
//            profileAdapter = new ProfileAdapter(getContext(),user.getTrips());
//            recyclerView.setAdapter(profileAdapter);
//            Glide.with(getContext()).load(user.getImageUrl()).into(userImage);
//            if (user.getCompletedInfo() == true){
//                Glide.with(getContext()).load(user.getDivingId()).into(divingIDImage);
//                Glide.with(getContext()).load(user.getId()).into(idImage);
//            }
//            userName.setText(user.getName());
//            title.setText(user.getUserClass());
//            if (user.completedInfo == true){
//
//                rating.setText(user.getRating());
//                divingId.setText(user.getDivingId());
//                divingLevel.setText(user.getDivingLevel());
//
//                birthDaylayou.setVisibility(View.VISIBLE);
//                phoneLayout.setVisibility(View.VISIBLE);
//                divingLevel.setText(user.getDivingLevel());
//                phoneNumber.setText(user.getMobilNumber());
//                birthDay.setText(user.getBerthday());
//
//            }
//        }
        return  rootView;
    }
    private void fetchUserTrips() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        database.getReference().child("Trips").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Map<String,Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                Trip trip =  dataSnapshot.getValue(Trip.class);
                Log.e(TAG, "onChildAdded: "+ trip.getProviderId() );
//                if (user.getUid().equals(trip.getProviderId())){
//                    user.getTrips().add(trip);
//                    Log.e(TAG, "add(trip): "+ user.getTrips().size());
//                    profileAdapter.notifyDataSetChanged();
//                }
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
    @Override
    public void onClick(View v) {

    }
}
