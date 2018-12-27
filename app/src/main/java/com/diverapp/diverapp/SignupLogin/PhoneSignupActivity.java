package com.diverapp.diverapp.SignupLogin;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.diverapp.diverapp.R;

public class PhoneSignupActivity extends AppCompatActivity implements View.OnClickListener {

    EditText phoneNumber,nameEditText;
    TextView iAcceptPrivecy, iAlreadyHaverAccount;
    Button signupButton;
    CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_signup);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ACD7EA")));
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        nameEditText = (EditText) findViewById(R.id.namePhoneSignup);
        iAcceptPrivecy = (TextView) findViewById(R.id.iAcceptPrivacyPhnoe);
        iAlreadyHaverAccount = (TextView) findViewById(R.id.youAlredyHaveAccountPhoneSignup);
        signupButton = (Button) findViewById(R.id.signupButtonPhoneForm);
        checkBox = (CheckBox) findViewById(R.id.termsCheckBoxPhone);
        signupButton.setOnClickListener(this);
        iAcceptPrivecy.setOnClickListener(this);
        iAlreadyHaverAccount.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signupButtonPhoneForm:
                handlesignupButtonPhoneForm();
                break;
            case R.id.iAcceptPrivacyPhnoe:
                iAcceptPrivacyEmail();
                break;
            case R.id.youAlredyHaveAccountPhoneSignup:
                youAlredyHaveAccountPhoneSignup();
        }
    }

    private void handlesignupButtonPhoneForm() {
        checkForme();
    }

    private void checkForme(){
        String phoneNumberStr = phoneNumber.getText().toString().trim();
        String username = nameEditText.getText().toString().trim();

        if (phoneNumberStr.isEmpty()) {
            phoneNumber.setError("phone number is required");
            phoneNumber.requestFocus();
            return;
        }
        if (username.isEmpty()){
            nameEditText.setError("name is required");
            nameEditText.requestFocus();
            return;
        }if(checkBox.isChecked() == false){
            Toast.makeText(getApplicationContext(),"Please read the Tirms and Conditions",Toast.LENGTH_LONG).show();
            return;
        }
        Intent validation = new Intent(getApplicationContext(),SelectUserTypePhoneActivity.class);
        validation.putExtra("USERNAME",username);
        validation.putExtra("PHONENUMBER","+"+phoneNumberStr);
        startActivity(validation);
    }
    private void youAlredyHaveAccountPhoneSignup() {
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }

    private void iAcceptPrivacyEmail(){
        Intent termsIntent = new Intent(getApplicationContext(),TermsAndConditions.class);
        termsIntent.putExtra("USER_TYPE",2);
        startActivity(termsIntent);
    }
}
