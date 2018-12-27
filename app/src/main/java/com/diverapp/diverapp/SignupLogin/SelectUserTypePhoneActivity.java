package com.diverapp.diverapp.SignupLogin;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.diverapp.diverapp.R;

public class SelectUserTypePhoneActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView logoImageUserType,proDiverType,divierType;
    TextView proDiverTypeTV,DivierType;
    int type = 0;
    String phoneNumber;
    String userName;
    Button phoneSignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user_type_phone);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ACD7EA")));
        logoImageUserType = (ImageView) findViewById(R.id.logoImageUserType);
        proDiverType = (ImageView) findViewById(R.id.proDiverType);
        divierType = (ImageView) findViewById(R.id.divierType );

        phoneSignup = (Button) findViewById(R.id.phoneSignup);

        phoneSignup.setOnClickListener(this);
        proDiverType.setOnClickListener(this);
        proDiverType.setOnClickListener(this);
        divierType.setOnClickListener(this);
        proDiverTypeTV = (TextView) findViewById(R.id.proDiverTypeTV);
        DivierType = (TextView) findViewById(R.id.DivierType);

        if (getIntent().getStringExtra("USERNAME") != null){
            userName = getIntent().getStringExtra("USERNAME");
        }if (getIntent().getStringExtra("PHONENUMBER") != null){
            phoneNumber = getIntent().getStringExtra("PHONENUMBER");
        }
        System.out.println("SelectUserTypePhoneActivity "+userName +" "+phoneNumber);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.divierType:
                type = 2 ;
//                userType(type);
                break;
            case R.id.proDiverType:
                type = 1;
//                userType(type);
                break;
            case R.id.phoneSignup:
                System.out.println(type);
                createValdtionIntent(type);
                break;
        }
    }
    private void createValdtionIntent(int type){
        if (type == 0){
            Toast.makeText(getApplicationContext(),"Select your account type",Toast.LENGTH_LONG).show();
            return;
        }
        System.out.println(type +" "+ userName +" "+phoneNumber);
        Intent intent = new Intent(getApplicationContext(),ValiditionActivity.class);
        intent.putExtra("USERTYPE",String.valueOf(type));
        intent.putExtra("USERNAME",userName);
        intent.putExtra("PHONNUMBER",phoneNumber);
        startActivity(intent);
    }
}
