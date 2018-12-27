package com.diverapp.diverapp.SignupLogin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.diverapp.diverapp.MainActivity;
import com.diverapp.diverapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    private EditText emailTV,passwordTV;
    private TextView forgotPassword,youDontHaveAccount;
    private Button loginButton;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ACD7EA")));
        mAuth = FirebaseAuth.getInstance();
        emailTV = findViewById(R.id.loginEmail);
        passwordTV = findViewById(R.id.loginpassword);
progressBar=findViewById(R.id.progress_bar);
        loginButton = findViewById(R.id.loginButton);
        forgotPassword = findViewById(R.id.didYouForgetYourPassword);
        youDontHaveAccount = findViewById(R.id.youDontHaveAnAccount);

        loginButton.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        youDontHaveAccount.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginButton:
                progressBar.setVisibility(View.VISIBLE);
                handleLogin();
                break;
            case R.id.didYouForgetYourPassword:
                handleForgotPassword();
                break;
            case R.id.youDontHaveAnAccount:
                handleYouDontHaveAccount();
                break;
        }
    }

    private void handleYouDontHaveAccount() {
        Intent signupMethods = new Intent(getApplicationContext(),SigupActivity.class);
        startActivity(signupMethods);

    }
    private void handleForgotPassword() {
        startActivity(new Intent(getApplicationContext(),ForgotPassword.class));
    }

    public void handleLogin(){
        checkForm();
        System.out.println("handleLogin");
    }

    private void checkForm() {
        final String email = emailTV.getText().toString().trim();
        final String password = passwordTV.getText().toString().trim();
        if (email.isEmpty()){
            emailTV.setError("Please enter your email");
            emailTV.requestFocus();
           progressBar.setVisibility(View.GONE);
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailTV.setError("Please enter a valid Email");
            emailTV.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }
        if (password.isEmpty()){
            passwordTV.setError("Please enter your password");
            passwordTV.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }
        if (password.length() < 6 ){
            passwordTV.setError("Minmum length of password should be 6");
            passwordTV.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                            // handle error
                            Toast.makeText(getApplicationContext(),task.getException().getLocalizedMessage(),Toast.LENGTH_LONG).show();
                       progressBar.setVisibility(View.GONE);
                        }else {
                            Toast.makeText(getApplicationContext(),"Welcom",Toast.LENGTH_SHORT).show();
                            System.out.println("Successful login");
                            progressBar.setVisibility(View.GONE);

                            SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();

                            editor.putString("email", email);
                            editor.putString("password", password);
                            editor.apply();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                    }
                });
    }
}
