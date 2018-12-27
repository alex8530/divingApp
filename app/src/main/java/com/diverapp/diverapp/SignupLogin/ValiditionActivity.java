package com.diverapp.diverapp.SignupLogin;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.diverapp.diverapp.MainActivity;
import com.diverapp.diverapp.R;
import com.diverapp.diverapp.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ValiditionActivity extends AppCompatActivity implements View.OnClickListener {

    private String verificationId ,phoneNumber,userName ;
    private int userType;
    private Button  signupButtonPhone,resendButton;
    private TextView phoneCode,timer;
    private FirebaseAuth mAuth;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    FirebaseDatabase database;
    DatabaseReference myRef;
    User user ;
    int  duration;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validition);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ACD7EA")));
        signupButtonPhone = (Button) findViewById(R.id.signupButtonPhone);
        phoneCode = (TextView) findViewById(R.id.phoneCode);
        signupButtonPhone.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        database  = FirebaseDatabase.getInstance();
        resendButton = (Button) findViewById(R.id.resendButton);
        timer = (TextView) findViewById(R.id.timer);
        myRef = database.getReference().child("Users");
        duration = 60;
        if (getIntent().getStringExtra("PHONNUMBER") != null){
            phoneNumber = getIntent().getStringExtra("PHONNUMBER");
        }if (getIntent().getStringExtra("USERNAME") != null){
            userName = getIntent().getStringExtra("USERNAME");
        }if (getIntent().getStringExtra("USERTYPE") != null){
            userType = Integer.parseInt(getIntent().getStringExtra("USERTYPE"));
        }
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText( String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                timer.setText("done!");
            }
        }.start();
//        System.out.println();
        System.out.println("Qahtan ==========    : "+ userName+" "+phoneNumber+" "+userType);
        validatePhoneNumber(phoneNumber);
//        validatePhoneNumber(phoneNumber);
    }

    private void validatePhoneNumber(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                duration,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }
    public void verifyCode(String code ){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,code);
        singnInWithCredential(credential);
    }
    public void singnInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                System.out.println("singnInWithCredential Task");
                if (task.isComplete()){
//                    Intent intent = new Intent(ValidationActivity.this,TheRootActivity.class);
                    System.out.println("singnInWithCredential Complete "+ task.getResult().getUser().getUid().toString());

                    Toast.makeText(getApplicationContext(),"User Registered Successful",Toast.LENGTH_SHORT).show();
//                    FirebaseUser user = mAuth.getCurrentUser();

//                    System.out.println(user.getUid().toString().trim());

                    Map<String, User> map = new HashMap<String, User>();
                    final String uid = task.getResult().getUser().getUid().toString();

                    Toast.makeText(getApplicationContext(),uid,Toast.LENGTH_LONG).show();
                    user = new User();
                    user.setType(userType);
                    user.setUid(uid);
                    user.setName(userName);
                    FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(
                            new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                return;
                            }
                            user.setTokenId(task.getResult().getToken());
                            // Get new Instance ID token
                            String token = task.getResult().getToken();
                            myRef.child(uid).child("tokinID").setValue(token);
                            // Log and toast
                            String msg = "HHHHHH---HHHHHH"+ token;
//                            Log.d(TAG, msg);
                        }
                    });
                   // user.setCreateionDate(System.currentTimeMillis());
                    myRef.child(uid).setValue(user);
                    finishAffinity();
                    getMainActivity();
                }else {
                    Toast.makeText(getApplicationContext(),task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                    System.out.println("singnInWithCredential Filde" + task.getException().getLocalizedMessage());
                }
            }
        });
    }
    private void getMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    System.out.println("PhoneAuthProvider onCodeSent "+ s);
                    verificationId = s;

                }
                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                    // HERE IF THE VERIFICTION IS DONE AUTOMATUCLEY
                    String code = phoneAuthCredential.getSmsCode();
                    if (code != null){
                        System.out.println("PhoneAuthProvider onVerificationCompleted "+phoneAuthCredential);
                        verifyCode(code);
                    }
                }
                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                    System.out.println("onVerificationFailed "+e.getLocalizedMessage());
                }
            };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signupButtonPhone:
                System.out.println("Qahtan ==========    : "+ userName+" "+phoneNumber+" "+userType);
                String code = phoneCode.getText().toString().trim();
                if (code.isEmpty() || code.length() <6 ){
                    phoneCode.setError("Enter code");
                    phoneCode.requestFocus();
                    return;
                }
                verifyCode(code);
                break;
            case R.id.resendButton:
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phoneNumber,        // Phone number to verify
                        1  ,               // Timeout duration
                        TimeUnit.MINUTES,   // Unit of timeout
                        this,               // Activity (for callback binding)
                        mCallbacks,         // OnVerificationStateChangedCallbacks
                        mResendToken);             // Force Resending Token from callbacks
                break;

        }
    }
}
