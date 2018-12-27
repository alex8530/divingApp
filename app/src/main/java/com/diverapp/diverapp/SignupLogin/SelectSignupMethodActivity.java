package com.diverapp.diverapp.SignupLogin;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.diverapp.diverapp.R;

public class SelectSignupMethodActivity extends AppCompatActivity implements View.OnClickListener {

    Button emailSignup,phoneSignup;
    TextView haveAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_signup_method);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ACD7EA")));
        emailSignup = (Button) findViewById(R.id.emailButtonSignupMethods);
        phoneSignup = (Button) findViewById(R.id.phoneButtonSignupMethods);

        haveAccount = (TextView) findViewById(R.id.haveAccountSignupMethod);

        emailSignup.setOnClickListener(this);
        phoneSignup.setOnClickListener(this);
        haveAccount.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.haveAccountSignupMethod:
                handleLogin();
                break;
            case R.id.emailButtonSignupMethods:
                handleEmailSignupForm();
                break;
            case R.id.phoneButtonSignupMethods:
                handlePhoneSignupForme();
                break;
        }
    }

    private void habdleFacebookSignup() {

    }

    private void handleEmailSignupForm() {
        startActivity(new Intent(getApplicationContext(),SigupActivity.class));
        System.out.println("handleEmailSignupForm");
        finish();
    }

    private void handleLogin() {
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        System.out.println("handleLogin");
        finish();
    }
    private void handlePhoneSignupForme(){
        startActivity(new Intent(getApplicationContext(),PhoneSignupActivity.class));
        System.out.println("handlePhoneSignupForme");
        finish();
    }
}
