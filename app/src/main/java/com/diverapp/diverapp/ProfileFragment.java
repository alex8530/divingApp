package com.diverapp.diverapp;


import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.diverapp.diverapp.Config.GlideApp;
import com.diverapp.diverapp.Diver.ShoppingItemsAdapter;
import com.diverapp.diverapp.SignupLogin.CompleteProfileFragment;
import com.diverapp.diverapp.SignupLogin.LoginActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import database.DBManager;
import database.Database;

import static com.diverapp.diverapp.MainActivity.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

//    public static final String TAG = "YOUR-TAG-NAME";

    public User user;
    private RecyclerView recyclerView;
    private ProfileAdapter profileAdapter;
    private TextView userName,title,divingLevel,divingId,rating ,phoneNumber,birthDay,email;
    public ImageView userImage;
    private Button logout,edit_prof;
    ProgressBar progressBar;
    private LinearLayout birthDaylayou,phoneLayout;
    public MainActivity mainActivity;
    public ProfileFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        System.out.println("onCreateView");
        View rootView =inflater.inflate(R.layout.profile_fragment, container, false);
progressBar=(ProgressBar)rootView.findViewById(R.id.progress_bar);
        email = (TextView) rootView.findViewById(R.id.profileUserEmail);
        userName = (TextView) rootView.findViewById(R.id.profileUserName);
        phoneLayout = (LinearLayout) rootView.findViewById(R.id.phoneLayout);
        birthDaylayou = (LinearLayout) rootView.findViewById(R.id.birthDaylayou);
        title = (TextView) rootView.findViewById(R.id.profileUserTitle);
        logout = (Button) rootView.findViewById(R.id.logout);
        edit_prof=(Button)rootView.findViewById(R.id.edit_prof);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.profile_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userImage = (ImageView) rootView.findViewById(R.id.userImageProfile);
         rating = (TextView) rootView.findViewById(R.id.rating);
        divingLevel = (TextView) rootView.findViewById(R.id.divingLevel);
        divingId = (TextView) rootView.findViewById(R.id.divingId);
        phoneNumber = (TextView) rootView.findViewById(R.id.phoneNumber);
        birthDay = (TextView) rootView.findViewById(R.id.birthDay);
        logout.setOnClickListener(this);
        userImage.setOnClickListener(this);
edit_prof.setOnClickListener(this);
user=MainActivity.user;
        if (user != null){
            fetchUserTrips();
//             profileAdapter = new ProfileAdapter(getContext(),user.getTrips());
////            user = fetchTripsHelper.fullUpTheUserContent(user,profileAdapter);
//            recyclerView.setAdapter(profileAdapter);
//
            Log.e(TAG, "onCreateView: "+ user.getImageUrl());
            GlideApp.with(getContext()).load(user.getProfileImage()).centerCrop().into(userImage);
            Glide.with(getContext()).load(user.getImageUrl()).into(userImage);
            userName.setText(user.getName());
            title.setText(user.getUserClass());
            final FirebaseDatabase database=FirebaseDatabase.getInstance();
          try {
              database.getReference().child("Users").child(user.getUid())
                      .child("Diving").child(user.getDiving_Prenc())
                      .addListenerForSingleValueEvent(new ValueEventListener() {
                          @Override
                          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                              if (dataSnapshot.exists()) {
                                  driving driving_ = dataSnapshot.getValue(driving.class);
                                  divingId.setText(driving_.getDivingID());
                                  divingLevel.setText(driving_.getDivingLevel());
                              }
                          }

                          @Override
                          public void onCancelled(@NonNull DatabaseError databaseError) {

                          }
                      });
          }catch (Exception exc){}
           final List<Trip> trips=new ArrayList<>();
             List<User> users=new ArrayList<>();
             MainActivity mainActivity=new MainActivity();
            final ShoppingItemsAdapter adapter=new ShoppingItemsAdapter(trips,getContext(),ProfileFragment.this,users,mainActivity);
            recyclerView.setAdapter(adapter);
            database.getReference().child("Users").child(user.getUid())
                   .child("MyTrips")
                   .addChildEventListener(new ChildEventListener() {
                       @Override
                       public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                           database.getReference().child("Trips").child(dataSnapshot.getValue(String.class))
                                   .addListenerForSingleValueEvent(new ValueEventListener() {
                                       @Override
                                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                           if (dataSnapshot.exists())
                                           {
                                               Trip trip_=dataSnapshot.getValue(Trip.class);
                                               trips.add(trip_);
                                               adapter.notifyDataSetChanged();
                                           }
                                       }

                                       @Override
                                       public void onCancelled(@NonNull DatabaseError databaseError) {

                                       }
                                   });
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
      try {
          if (user.getCompletedInfo() == true) {
//                rating.setText(user.getra());
//                divingId.setText(user.id());
              birthDaylayou.setVisibility(View.VISIBLE);
              phoneLayout.setVisibility(View.VISIBLE);
//                divingLevel.setText(user.getDivingLevel());
              phoneNumber.setText(user.getMobilNumber());
              birthDay.setText(user.getBerthday());
          }
      }catch (Exception Exc){}
        }
        FirebaseAuth auth=FirebaseAuth.getInstance();
        FirebaseUser user_=auth.getCurrentUser();
        if(user_!=null)
        email.setText(user_.getEmail().toString());
        return  rootView;
    }
    private void SaveImageProfileToStorg() {
        progressBar.setVisibility(View.VISIBLE);
//        System.out.println("user.createionDate "+user.createionDate);
FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference myRef=database.getReference();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();

        System.out.println(storageRef);
//        System.out.println(mPath);
        ResizePickedImage resizePickedImage = new ResizePickedImage();
        for (int i=0;i<mPaths.size();i++){
            final String userID = resizePickedImage.resizeAndCompressImageBeforeSend(getContext(),mPaths.get(i), UUID.randomUUID().toString());
            InputStream stream = null;
            try {
                stream = new FileInputStream(new File(userID));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            final String uuid = UUID.randomUUID().toString();

            final StorageReference ref = storageRef.child("USERS").child("IDs").child(uuid);
            final int finalI = i;
            ref.putStream(stream).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            System.out.println("uri "+ uri.toString());
                            // SET THE USER ID URL
                            HashMap<String,Object> map = new HashMap<>();
                            if ( finalI == 0){
                                map.put("UserID",uri.toString());
                            }else if (finalI == 1 ){


                                FirebaseAuth auth=FirebaseAuth.getInstance();


                                myRef.child("Users").child(auth.getCurrentUser().getUid()).child("profileImage").setValue(uri.toString());
//ProfileImg added with successful
                                Glide.with(getContext()).load(user.getImageUrl()).into(userImage);
                                progressBar.setVisibility(View.GONE);

                            }

                        }
                    });
                }
            });
        }

    }
    List<String> mPaths = new ArrayList<>();
    public void compressImageAndSaveThePath(Intent data) {
        Uri returnUri = data.getData();
        ResizePickedImage resizePickedImage = new ResizePickedImage();
        String realePath = resizePickedImage.getRealPathFromURI(returnUri,getActivity());
        System.out.println(realePath);
        String compresedImagePath = resizePickedImage.resizeAndCompressImageBeforeSend(getContext(),realePath, UUID.randomUUID().toString());
        System.out.println(compresedImagePath);
        mPaths.add(compresedImagePath);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
        System.out.println("onActivityResult- - ");
        if(resultCode == getActivity().RESULT_OK && requestCode == PICK_FILE) {
                // pick id file
                compressImageAndSaveThePath(data);
                SaveImageProfileToStorg();

        }
    }
    int PICK_FILE = 1;
    private void fetchUserTrips() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // requests
        database.getReference().child("Requests").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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


//        database.getReference().child("Trips").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Map<String,Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
//                Trip trip = new Trip(dataSnapshot.getKey(),map);
//                Log.e(TAG, "onChildAdded: "+ trip.getProviderId() );
//                if (user.getUid().equals(trip.providerId)){
//                    user.getTrips().add(trip);
//                    Log.e(TAG, "add(trip): "+ user.getTrips().size());
//                    profileAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }
    private void moveToCompleteProfileFragment() {
        CompleteProfileFragment completeProfileFragment = new CompleteProfileFragment();
        FragmentTransaction fragmentTransaction =  this.getFragmentManager().beginTransaction();
        completeProfileFragment.user = user;
        fragmentTransaction.add(R.id.main_frame, completeProfileFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void startPickIntint(int p){
//        Log.d(MainActivity.TAG, "startPickIntint: ");
        Intent pickerPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickerPhotoIntent, p);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.userImageProfile:
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
                startPickIntint(PICK_FILE);
                break;
            case R.id.logout:
                mainActivity.mAuth.signOut();
                DBManager dbManager = new DBManager(getContext());
                dbManager.open();
                dbManager.deleteFromCart();
                dbManager.close();
                mainActivity.finish();
                startActivity(new Intent(getContext(), LoginActivity.class));
                break;
            case R.id.edit_prof:
                moveToCompleteProfileFragment();
                break;
        }
    }
}
