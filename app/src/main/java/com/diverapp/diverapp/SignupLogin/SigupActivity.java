package com.diverapp.diverapp.SignupLogin;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.diverapp.diverapp.R;

import pub.devrel.easypermissions.EasyPermissions;

public class SigupActivity extends AppCompatActivity implements View.OnClickListener {


//    private FusedLocationProviderClient mFusedLocationClient;

//    private FirebaseDatabase database;
//    private DatabaseReference myRef;

    byte[] byteArray;
    Uri imageCaptureUri;
    int Pick_FROM_FILE = 1;
    EditText signupEmail,signupName,signupPassword;
    Button signupButton;
    TextView iAcceptPrivacy,alreadyHaveAccount;
    ImageView logo;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigup);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ACD7EA")));
        signupEmail = (EditText) findViewById(R.id.signupEmail);
        signupName = (EditText) findViewById(R.id.signupName);
        signupPassword = (EditText) findViewById(R.id.signupPassword);
        logo = (ImageView) findViewById(R.id.logoImage);
        signupButton = (Button) findViewById(R.id.signupButton);

        iAcceptPrivacy = (TextView) findViewById(R.id.iAcceptPrivacyEmail);
        alreadyHaveAccount = (TextView) findViewById(R.id.youAlredyHaveAccountEmailSignup);

        checkBox = (CheckBox) findViewById(R.id.termsCheckBox);
        checkBox.setOnClickListener(this);
        signupButton.setOnClickListener(this);
        iAcceptPrivacy.setOnClickListener(this);
        alreadyHaveAccount.setOnClickListener(this);
        logo.setOnClickListener(this);
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signupButton:
                signupButton();
                break;
            case R.id.iAcceptPrivacyEmail:
                iAcceptPrivacyEmail();
                break;
            case R.id.youAlredyHaveAccountEmailSignup:
                youAlredyHaveAccountEmailSignup();
                break;
            case R.id.logoImage:
                pickProfileImage();
                break;
            case R.id.termsCheckBox:
//                Log.e(MainActivity.TAG, "onClick: "+ checkBox.isChecked() );
                break;
        }
    }
    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private void pickProfileImage() {
        if (EasyPermissions.hasPermissions(this, galleryPermissions)) {
            Intent pickerPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickerPhotoIntent,Pick_FROM_FILE);
            System.out.println("imageView LoGOG");
        } else {
            EasyPermissions.requestPermissions(this, "Access for storage",
                    101, galleryPermissions);
        }

    }
    String mPath;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK){
            return;
        }
        Bitmap bitmap = null;
        String path;
        RoundedBitmapDrawable roundedBitmapDrawableFactory = null;
        if (requestCode == Pick_FROM_FILE){
            imageCaptureUri = data.getData();
            path = getRealPathFromURI(imageCaptureUri);
            if (path == null){
                path=imageCaptureUri.getPath();
            }
            if (path != null){
                bitmap = BitmapFactory.decodeFile(path);
                roundedBitmapDrawableFactory =
                        RoundedBitmapDrawableFactory.create(getResources(),bitmap);
                roundedBitmapDrawableFactory.setCircular(true);
                mPath = path;
            }
//            logo.setImageBitmap(bitmap);
            logo.setImageDrawable(roundedBitmapDrawableFactory);
            System.out.println("data "+ data.getData());
        }
    }
    public String getRealPathFromURI(Uri contentURI){
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentURI,proj,null,null,null);
        if (cursor == null) return  null;
        int colmunIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return  cursor.getString(colmunIndex);
    }

    private void checkForme(){
        String email = signupEmail.getText().toString().trim();
        String password = signupPassword.getText().toString().trim();
        final String username = signupName.getText().toString().trim();

        if (email.isEmpty()) {
            signupEmail.setError("Email is required");
            signupEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            signupEmail.setError("Please enter a valid Email");
            signupEmail.requestFocus();
            return;
        }
        if (password.isEmpty()){
            signupPassword.setError("Email is required");
            signupPassword.requestFocus();
            return;
        }
        if (username.isEmpty()){
            signupName.setError("Email is required");
            signupName.requestFocus();
            return;
        }
        if (password.length() < 6 ){
            signupPassword.setError("Minmum length of password should be 6");
            signupName.requestFocus();
            return;
        }if (checkBox.isChecked() != true){
            Toast.makeText(getApplicationContext(),"Please read the Terms and Conditions",Toast.LENGTH_LONG).show();
            return;
        }
        Intent chooseUserTypeActivity = new Intent(getApplicationContext(),ChooseUserTypeActivity.class);
        chooseUserTypeActivity.putExtra("NAME",username);
        chooseUserTypeActivity.putExtra("EMAIL",email);
        chooseUserTypeActivity.putExtra("PASSWORD",password);
        chooseUserTypeActivity.putExtra("MYPATH",mPath);
        startActivity(chooseUserTypeActivity);
      finish();
    }
    private void signupButton() {
        checkForme();
//        String username = signupName.getText().toString().trim();
//        String email = signupEmail.getText().toString().trim();
//        String password = signupPassword.getText().toString().trim();

    }
    private void iAcceptPrivacyEmail(){
        Intent termsIntent = new Intent(getApplicationContext(),TermsAndConditions.class);
        termsIntent.putExtra("USER_TYPE",2);
        startActivity(termsIntent);
    }
    private void youAlredyHaveAccountEmailSignup() {
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }
}
