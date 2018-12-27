package com.diverapp.diverapp.SignupLogin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.diverapp.diverapp.MainActivity;
import com.diverapp.diverapp.R;
import com.diverapp.diverapp.ResizePickedImage;
import com.diverapp.diverapp.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ChooseUserTypeActivity extends AppCompatActivity {

    Button emeilSignup;
    int userType;
    User user;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String mPath,username;
    String name ;
    String email ;
    String password;
    String tokinID;
    FirebaseStorage storage;

    ImageView logoImageUserType,proDiverType,divierType;
    TextView proDiverTypeTV,DivierType;

    StorageReference storageReference;
private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_user_type);
progressBar=(ProgressBar)findViewById(R.id.progress_bar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ACD7EA")));
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

        storage = FirebaseStorage.getInstance();

        logoImageUserType = (ImageView) findViewById(R.id.logoImageUserType);

        proDiverType = (ImageView) findViewById(R.id.proDiverType);
        divierType = (ImageView) findViewById(R.id.divierType );

        emeilSignup = (Button) findViewById(R.id.emeilSignup);
        divierType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                divierType.setBackgroundResource(R.drawable.ic_selected_diver);
                proDiverType.setBackgroundResource(R.drawable.ic_unselected_pdiver);
                userType = 2;
                username="Diver";
            }
        });
        proDiverType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userType = 1;
                username="Pro Diver";
                proDiverType.setBackgroundResource(R.drawable.ic_selected_pdiver);
                divierType.setBackgroundResource(R.drawable.ic_unselected_diver);
            }
        });
        emeilSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUserInFirebase();
            }
        });
    }
    private void createUserInFirebase() {
       progressBar.setVisibility(View.VISIBLE);
       emeilSignup.setVisibility(View.GONE);

        if ((getIntent().getStringExtra("NAME") != null ) &&
                (getIntent().getStringExtra("EMAIL") != null ) &&
                (getIntent().getStringExtra("PASSWORD") != null )) {
            name = getIntent().getStringExtra("NAME");
            email = getIntent().getStringExtra("EMAIL");
            password = getIntent().getStringExtra("PASSWORD");
            mPath = getIntent().getStringExtra("MYPATH");
        }
        SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();

        editor.putString("email", email);
        editor.putString("password", password);

        editor.putInt("usertype", userType);
        editor.apply();
        System.out.println(name +" "+ password + " "+ email + " "+ userType );
        /// SAVER PROFIL IMAGE TO STORAG
        // CREATE USER IN FIREBASE
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    System.out.println(task.getResult().getUser().getUid());
                    // CREATE THE USEr
                    final String uid = task.getResult().getUser().getUid();
                    user = new User();
                    user.setName(name);
                    user.setUid(uid);
                    user.setType(userType);
                    user.setUserClass(username);
                    SimpleDateFormat formatter = new SimpleDateFormat("EEE dd/MM");
                    String dat=formatter.format(new Date(System.currentTimeMillis()));
                    user.setCreateionDate(dat);
                    user.setCompletedInfo(false);

                    FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(),"There are errors "+task.getException().getMessage(),Toast.LENGTH_LONG)
                                        .show();
                                progressBar.setVisibility(View.GONE);
                                emeilSignup.setVisibility(View.VISIBLE);
                                return;
                            }
                            user.setTokenId(task.getResult().getToken());
                            myRef.child(uid).setValue(user);
                            if (mPath != null){
                                SaveImageProfileToStorg();
                            }
                            getMainActivity();
                            // Get new Instance ID token
                            String token = task.getResult().getToken();

                            // Log and toast
                            String msg = "HHHHHH---HHHHHH"+ token;
//                            Log.d(TAG, msg);
                        }
                    });
                }else {
                    Toast.makeText(getApplicationContext(),task.getException()
                            .getLocalizedMessage(),Toast.LENGTH_LONG).show();
                   finish();
                }
            }
        });
    }

    private void SaveImageProfileToStorg() {
        // CHECK IF MPATH NOT NULL
        final StorageReference storageRef = storage.getReference();

        System.out.println(storageRef);
        ResizePickedImage resizePickedImage = new ResizePickedImage();
        String str = resizePickedImage.resizeAndCompressImageBeforeSend(getApplicationContext(),mPath, UUID.randomUUID().toString());
        InputStream stream = null;
        try {
            stream = new FileInputStream(new File(str));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        final String uuid = UUID.randomUUID().toString();
        storageReference = storageRef.child("ProfileImages").child(uuid);
        storageRef.putStream(stream).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("ffggffgfggf");
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        System.out.println("uri "+ uri.toString());
                        user.setImageUrl(uri.toString());
                        myRef.child(user.getUid()).setValue(user);
                        finish();
                    }
                });
            }
        });
    }
    private void getMainActivity() {
        MainActivity mainActivity = new MainActivity();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.e(MainActivity.TAG, "onDestroy: " );
    }
}
