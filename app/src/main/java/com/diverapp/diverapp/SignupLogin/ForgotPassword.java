package com.diverapp.diverapp.SignupLogin;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.diverapp.diverapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    EditText resetPasswoerdEmail;
    Button resetPasswordEmaileButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ACD7EA")));
        resetPasswoerdEmail = (EditText) findViewById(R.id.resetPasswoerdEmail);
        resetPasswordEmaileButton = (Button) findViewById(R.id.resetPasswordEmaileButton);
        resetPasswordEmaileButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.resetPasswordEmaileButton:
                resetPasswordButton();
                break;
        }
    }

    private void resetPasswordButton() {
        String email = resetPasswoerdEmail.getText().toString().trim();
        if (email.isEmpty()) {
            resetPasswoerdEmail.setError("Email is required");
            resetPasswoerdEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            resetPasswoerdEmail.setError("Please enter a valid Email");
            resetPasswoerdEmail.requestFocus();
            return;
        }
        // Tack Email
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"We send the Link to you by email",Toast.LENGTH_SHORT).show();
                            finish();
//                            backToLoginActivity();
                        }else {

                            Toast.makeText(getApplicationContext(),task.getException().getLocalizedMessage(),Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    private void backToLoginActivity() {
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }

}
