package com.diverapp.diverapp.SignupLogin;


import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.diverapp.diverapp.MainActivity;
import com.diverapp.diverapp.R;
import com.diverapp.diverapp.ResizePickedImage;
import com.diverapp.diverapp.User;
import com.diverapp.diverapp.driving;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompleteProfileFragment extends Fragment implements View.OnClickListener {

    public User user ;

    FirebaseStorage storage;
    StorageReference storageReference;

    EditText name,phone,divingOrgniaion,divingLevel1,payPalClintID,bdayPicker,bio_edit;
    Spinner completeDivingID;
    Button idPicker,post,add_phone;
     ImageButton add_new_diving;

    CheckBox checkBoxCompleteProfile;
    TextView divingOrgnaizionTV,iAcceptPrivacyEmailCompleteProfile,paypalTag,bio_tv;
    String birthday;
    List<String> mPaths = new ArrayList<>();
List<driving> drivingList;
    ProgressBar progressBar;
    public CompleteProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_complete_profile, container, false);
        bio_edit = (EditText) rootView.findViewById(R.id.Bio_edit);
        bio_tv = (TextView) rootView.findViewById(R.id.bio_tv);
        name = (EditText) rootView.findViewById(R.id.completeName);
        phone = (EditText) rootView.findViewById(R.id.completePhone);
        divingOrgniaion = (EditText) rootView.findViewById(R.id.completeDivingOrganiztion);
        completeDivingID = (Spinner) rootView.findViewById(R.id.completeDivingID);

progressBar=(ProgressBar)rootView.findViewById(R.id.progress_bar);
        bdayPicker  = (EditText) rootView.findViewById(R.id.completePickDate);
        idPicker  = (Button) rootView.findViewById(R.id.completePickFileId);
        post = (Button) rootView.findViewById(R.id.completePostButton);
        add_phone = (Button) rootView.findViewById(R.id.add_phone);

        add_new_diving = (ImageButton) rootView.findViewById(R.id.add_new_diving);

        payPalClintID = (EditText) rootView.findViewById(R.id.payPalClintID);
        paypalTag  = (TextView) rootView.findViewById(R.id.paypalTag);

        checkBoxCompleteProfile = (CheckBox) rootView.findViewById(R.id.checkBoxCompleteProfile);
        iAcceptPrivacyEmailCompleteProfile = (TextView) rootView.findViewById(R.id.iAcceptPrivacyEmailCompleteProfile);

        iAcceptPrivacyEmailCompleteProfile.setOnClickListener(this);

        divingOrgnaizionTV = (TextView) rootView.findViewById(R.id.divingOrganizaion);
//        divingLevel  = (EditText) rootView.findViewById(R.id.);
        divingLevel1 = (EditText) rootView.findViewById(R.id.completeDivingLevel);
//        bdayPicker.setOnClickListener(this);
        idPicker.setOnClickListener(this);

        post.setOnClickListener(this);
add_new_diving.setOnClickListener(this);
add_phone.setOnClickListener(this);
        if (user.getType() == 2 ){
            payPalClintID.setVisibility(View.GONE);
            paypalTag.setVisibility(View.GONE);
            bio_tv.setVisibility(View.GONE);
            bio_edit.setVisibility(View.GONE);
        }
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference reference=database.getReference();
        FirebaseAuth auth=FirebaseAuth.getInstance();

        user= MainActivity.user;

        name.setText(user.getName());
        phone.setText(user.getMobilNumber());
        bdayPicker.setText(user.getBerthday());
        bio_edit.setText(user.getBio());
        progressBar.setVisibility(View.GONE);

        final List<String> list_divingID=new ArrayList<>();
        drivingList=new ArrayList<>();
        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list_divingID);
        completeDivingID.setAdapter(adapter);//(getActivity(),null,android.R.layout.simple_list_item_1,list_divingID,null));
        reference.child("Users").child(auth.getCurrentUser().getUid()).child("Diving")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        driving driving_=dataSnapshot.getValue(driving.class);
                        drivingList.add(driving_);
                        list_divingID.add(driving_.getDivingID());
                        adapter.notifyDataSetChanged();
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
        completeDivingID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                change_driving_Data(drivingList.get(i));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }) ;
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.completePickDate:
//                pickDate();
//                break;
            case R.id.add_phone:
               startActivityForResult(new Intent(getActivity(),add_phone_number_fragment.class),1);
               break;
            case  R.id.completePickFileId:
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
                startPickIntint(PICK_FILE);
            case R.id.completePickFileDivingId:
//                Log.d(MainActivity.TAG, "onClick: ");
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
                startPickIntint(PICK_DIVIE_ID);
                break;
            case R.id.completePostButton:
                postInfo();
                break;
            case R.id.add_new_diving:
                Add_new_diving();
                break;

        }
    }
    int PICK_FILE = 2;
    int PICK_DIVIE_ID = 11;
private void Add_new_diving(){
    final Dialog dialog = new Dialog(getContext());
    dialog .setContentView(R.layout.add_diving);
    dialog.setTitle("Add New Diving");
    dialog.setCancelable(true);


    dialog.show();// to show dialog
final CheckBox isprenc=dialog.findViewById(R.id.isprinc);
    final EditText id=dialog.findViewById(R.id.completeDivingID);
    final EditText level=dialog.findViewById(R.id.completeDivingLevel);
    final EditText org=dialog.findViewById(R.id.completeDivingOrganiztion);
    Button add_file=dialog.findViewById(R.id.completePickFileDivingId);
    Button add_diving=dialog.findViewById(R.id.addthisdrivingButton);


add_file.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
        startPickIntint(PICK_FILE);
    }
});
   add_diving.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view) {
          if (add_driving(org, level, id,isprenc.isChecked()))
           dialog.dismiss();
       }
   });
}
    public void startPickIntint(int p){
//        Log.d(MainActivity.TAG, "startPickIntint: ");
        Intent pickerPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickerPhotoIntent, p);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getContext(), "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void postInfo() {
        // Check if all the info is correct and save it to data base
        // asign it to user ;
        checkTheUserInput();
        System.out.println(mPaths);
    }

    private void pickDate() {
        DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                birthday =   dayOfMonth+" / "+month+" / "+year;
                bdayPicker.setText(birthday);
            }
        };
        DatePickerDialog datePickerDialog;
        Calendar c = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(getContext(),datePickerListener,c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
        Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR,1900);
        date.set(Calendar.MONTH,1);
        date.set(Calendar.DAY_OF_MONTH,1);

        datePickerDialog.getDatePicker().setMinDate(date.getTimeInMillis());
        datePickerDialog.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != getActivity().RESULT_OK){
            return;
        }
        System.out.println("onActivityResult- - ");
        if(resultCode == getActivity().RESULT_OK) {
            if (requestCode == PICK_FILE){
                // pick id file
                compressImageAndSaveThePath(data);
            }

            if (requestCode == PICK_DIVIE_ID ){
                // pick the divier Id
                compressImageAndSaveThePath(data);
            }

            if (requestCode == 1 ){
                progressBar.setVisibility(View.VISIBLE);
                // pick the divier Id
               final FirebaseAuth mAuth=FirebaseAuth.getInstance();
                SharedPreferences prefs = getActivity().getSharedPreferences("user", MODE_PRIVATE);

                String email=prefs.getString("email"," ");
                String password=prefs.getString("password"," ");
                Toast.makeText(getContext(),email+" "+password,Toast.LENGTH_LONG).show();

                mAuth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful())
                                {
                                    Toast.makeText(getContext(),"yes signin again !"+mAuth.getCurrentUser().getEmail(),Toast.LENGTH_LONG).show();
progressBar.setVisibility(View.GONE);

                                }else{

                                    Toast.makeText(getContext(),"error happen we are sorry signin again !",Toast.LENGTH_LONG).show();
                                   progressBar.setVisibility(View.GONE);
                                    }
                            }
                        });
                phone.setText(data.getStringExtra("phone"));
            }
        }
    }
    private void clearData(){
        divingLevel1.setText("");
        divingOrgniaion.setText("");

    }
    private void change_driving_Data(driving driving_){
        divingLevel1.setText(driving_.getDivingLevel());
        divingOrgniaion.setText(driving_.getDivingOrg());


    }
    private boolean add_driving(EditText divingOrg, EditText divingLevel, EditText divingID, boolean checked){

boolean empty=false;
        if (divingOrg.getText().toString().isEmpty()){

            divingOrg.setError("Please enter your diving orgnazion");
            divingOrg.requestFocus();
            empty=true;

        }
        if (divingLevel.getText().toString().isEmpty()){
            divingLevel.setError("Please enter your diving level");
            divingLevel.requestFocus();
            empty=true;
        }
        if (divingID.getText().toString().isEmpty()){
            divingID.setError("Please enter your diving ID ");
            divingID.requestFocus();
            empty=true;
        }
        if (mPaths.isEmpty()){
            Toast.makeText(getContext(),"Please upload your IDs",Toast.LENGTH_LONG).show();
            empty=true;
        }
if (!empty) {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    SaveImageProfileToStorg(
            divingID.getText().toString(), divingLevel.getText().toString(), divingOrg.getText().toString(),checked);
    return true;
}
return false;
    }
    public void checkTheUserInput() {

        String fullName = name.getText().toString().trim();
        String phoneNumber =  phone.getText().toString().trim();
      String bankAccount = payPalClintID.getText().toString().trim();
        String bday = bdayPicker.getText().toString().trim();
        if (fullName.isEmpty()){
            name.setError("Please enter your name");
            name.requestFocus();
            return;
        }
//        if (PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
//            phone.setError("Please enter a valid phone number");
//            phone.requestFocus();
//            return;
//        }
     if (bday.isEmpty()) {
            bdayPicker.setError("Please enter your birthday date ");
            bdayPicker.requestFocus();
            return;
        }
//        if (checkBoxCompleteProfile.isChecked() == false){
//            Toast.makeText(getContext(),"Please read the Tirms and Conditions",Toast.LENGTH_LONG).show();
//            return;
//        }
        if (bankAccount.isEmpty()){
            payPalClintID.setError("Please enter your diving ID ");
            payPalClintID.requestFocus();

        }
        Toast.makeText(getContext(),"Your profile is updated.Thank you",Toast.LENGTH_LONG).show();
        user.setName(fullName);
        user.setMobilNumber(phoneNumber);
        user.setBerthday(bdayPicker.getText().toString());
        user.setCompletedInfo(true);
        user.setBankAccount(bankAccount);
user.setBio(bio_edit.getText().toString());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().child("Users").child(user.getUid()).child("name").setValue(user.getName());
        database.getReference().child("Users").child(user.getUid()).child("mobilNumber").setValue(user.getMobilNumber());
        database.getReference().child("Users").child(user.getUid()).child("berthday").setValue(user.getBerthday());
        database.getReference().child("Users").child(user.getUid()).child("completedInfo").setValue(true);
        database.getReference().child("Users").child(user.getUid()).child("bankAccount").setValue(user.getBankAccount());

        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        /*
        ID URL
        DIVING ID URL


        COMPLETED = true

         */
    }
    private void SaveImageProfileToStorg(final String divingID, final String divingLevel, final String divingOrg, final boolean checked) {
//        System.out.println("user.createionDate "+user.createionDate);

        storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();
        final DatabaseReference myRef=FirebaseDatabase.getInstance().getReference();
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
            final String uuid = user.getUid();

            final StorageReference ref = storageRef.child("USERS").child("IDs").child(uuid);
            final int finalI = i;
            Toast.makeText(getContext(),"Wait a minute to upload your driving ID",Toast.LENGTH_LONG).show();

            ref.putStream(stream).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            System.out.println("uri "+ uri.toString());


                                driving driving_=new driving();
                                driving_.setDivingID(divingID);
                                driving_.setDivingLevel(divingLevel);
                                driving_.setDivingOrg(divingOrg);
                                driving_.setDriving_img(uri.toString());
                                FirebaseAuth auth=FirebaseAuth.getInstance();

String key=myRef.child("Users").child(auth.getCurrentUser().getUid()).child("Diving").push().getKey();
                                myRef.child("Users").child(auth.getCurrentUser().getUid()).child("Diving").child(key).setValue(driving_);
if(checked){
    myRef.child("Users").child(auth.getCurrentUser().getUid()).child("Diving_Prenc").setValue(key);
}
                                //driving added with successful
                            Toast.makeText(getContext(),"driving ID added with successful",Toast.LENGTH_LONG).show();


                        }
                    });
                }
            });
        }

//        String divingID = resizePickedImage.resizeAndCompressImageBeforeSend(getContext(),mPaths.get(0), UUID.randomUUID().toString());
//        System.out.println(str);
//        storageReference = storage.getReference().child("ProfileImages");

    }

    public void compressImageAndSaveThePath(Intent data) {
        Uri returnUri = data.getData();
        ResizePickedImage resizePickedImage = new ResizePickedImage();
        String realePath = resizePickedImage.getRealPathFromURI(returnUri,getActivity());
        System.out.println(realePath);
        String compresedImagePath = resizePickedImage.resizeAndCompressImageBeforeSend(getContext(),realePath, UUID.randomUUID().toString());
        System.out.println(compresedImagePath);
        mPaths.add(compresedImagePath);
    }
}
