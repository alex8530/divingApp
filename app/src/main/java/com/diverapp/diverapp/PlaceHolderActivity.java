package com.diverapp.diverapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.diverapp.diverapp.SignupLogin.SigupActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class PlaceHolderActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    FirebaseDatabase database;
    DatabaseReference myRef;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_holder);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
//        setupLocationManager();
//        mAuth.signOut();
        if (mAuth.getCurrentUser() == null) {

            System.out.println("Please login first");
            getLoginActivity();
            return;
        }
    }
    private void fetchUserFromDatabase() {
        final String uid = mAuth.getUid().toString();
        myRef.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("dataSnapshot " + dataSnapshot);
//                user = dataSnapshot.getValue(User.class);
                HashMap<String, Object> dict = (HashMap<String, Object>) dataSnapshot.getValue();
                System.out.println(dict);
                user = new User();
                finish();
//                Log.d(TAG, "onDataChange: "+ user.getCreateionDate());
//                createAButtonsForUser(user, mNavView);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void getLoginActivity() {
        startActivity(new Intent(getApplicationContext(), SigupActivity.class));
        finish();
    }
}
