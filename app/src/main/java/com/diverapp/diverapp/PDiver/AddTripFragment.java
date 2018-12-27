package com.diverapp.diverapp.PDiver;


import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.diverapp.diverapp.MainActivity;
import com.diverapp.diverapp.R;
import com.diverapp.diverapp.ResizePickedImage;
import com.diverapp.diverapp.SignupLogin.CompleteProfileFragment;
import com.diverapp.diverapp.Trip;
import com.diverapp.diverapp.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import Modle.Notifi;
import pub.devrel.easypermissions.EasyPermissions;

import static android.support.constraint.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTripFragment extends Fragment implements AdapterView.OnItemSelectedListener,View.OnClickListener {

    ScrollView scrollView;
    Spinner tripeType;
//    CalendarView calendarView;
    FirebaseDatabase database;
    DatabaseReference myRef;
    LinearLayout condtionAndTerms;
    FirebaseStorage storage;
    StorageTask<UploadTask.TaskSnapshot> storageReference;
    LinearLayout loader;
    int MAP_ACATIVITY = 2031;
    TextView loaderStatus;
    public User user;
    EditText tripName,tripDescription,ticketNum,minmummTicketNum,price,location;
    TextView minmmumTicketNumberTV,iAcceptPrivacyEmailPro;
    CheckBox proDiverCheckBox;
    int i = 0;
    AddImageFromStudio addImageFromStudio;
    Trip trip;
    String tripClass;
    int tripTy;

    Long startTimeStamp;
    Long endTimeStamp;

    Button sendButton,startDate,endDate;

    ImageView image1,image2,image3;
    AddTripHelper addTripHelper;
    ArrayAdapter<String> spinnerArrayAdapter ;
    Location location1;
    Drawable drawable1 ;
    Drawable drawable2 ;
    Drawable drawable3 ;

//    LinearLayout progressBarLayout;
    TextView progressBarInfo;
//    ProgressBar uploadingImagePB;
    public AddTripFragment() {
        // Required empty public constructor

    }
    private String getTime(int hr,int min) {
        Time tme = new Time(hr,min,0);//seconds by default set to zero
        Format formatter;
        formatter = new SimpleDateFormat("h:mm a");
        return formatter.format(tme);
    }
    Calendar calendar;
    final TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute ) {
            calendar.set(Calendar.HOUR,hourOfDay);
            calendar.set(Calendar.MINUTE,minute);
            if (hourOfDay < 12) {
                calendar.set(Calendar.AM_PM,1);
            }else {
                calendar.set(Calendar.AM_PM,2);
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd h:mm a ");
            view.setVisibility(view.isShown()
                    ?View.GONE
                    :View.VISIBLE);
            if (i == 1 ){
                String formatedDate = format.format(calendar.getTime());
                startDate.setText(formatedDate);
                startTimeStamp = calendar.getTime().getTime();
//                Log.e(TAG, "onTimeSet:----- "+ time );

            }else if (i == 2 ){
                String formatedDate = format.format(calendar.getTime());
                endDate.setText(formatedDate);
//                Log.e(TAG, "onTimeSet:----- "+ time );
                endTimeStamp = calendar.getTime().getTime();
            }

        }
    };
    public DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            calendar.set(Calendar.YEAR,selectedYear);
            calendar.set(Calendar.DAY_OF_MONTH,selectedDay);
            calendar.set(Calendar.MONTH,selectedMonth);
            TimePickerDialog myTPDialog = new TimePickerDialog(getContext(),R.style.TimePickerTheme,mTimeSetListener,0,0,true);
            myTPDialog.show();
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_trip, container, false);
        addImageFromStudio = new AddImageFromStudio();
        loader = rootView.findViewById(R.id.Loader);
        loaderStatus = rootView.findViewById(R.id.LoaderStatus);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Trips");
        storage = FirebaseStorage.getInstance();
        addTripHelper = new AddTripHelper();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        // Inflate the layout for this fragment
        user= MainActivity.user;
        trip = new Trip();
        trip.setBankAccount(user.getBankAccount());
        Log.e(TAG, "onCreateView: getCompletedInfo "+ user.getCompletedInfo() );
        calendar = Calendar.getInstance();

        condtionAndTerms = (LinearLayout) rootView.findViewById(R.id.condtionAndTerms);
        if (user.getCompletedInfo() == false){
            condtionAndTerms.setVisibility(View.VISIBLE);
        }
        scrollView = (ScrollView) rootView.findViewById(R.id.scrollView);
        tripName = (EditText) rootView.findViewById(R.id.tripNameTF);
        proDiverCheckBox = (CheckBox) rootView.findViewById(R.id.proDiverCheckBox);
        iAcceptPrivacyEmailPro = (TextView) rootView.findViewById(R.id.iAcceptPrivacyEmailPro);
        tripDescription = (EditText) rootView.findViewById(R.id.trip_description_TV);

        startDate = (Button) rootView.findViewById(R.id.startDateTV);
        endDate = (Button) rootView.findViewById(R.id.endDateTV);

        minmmumTicketNumberTV = rootView.findViewById(R.id.minmmumTicketNumberTV);
        ticketNum = (EditText) rootView.findViewById(R.id.ticketNumber);
        minmummTicketNum = (EditText) rootView.findViewById(R.id.minmmumTicketNumber);
        price = (EditText) rootView.findViewById(R.id.price);

        tripeType = (Spinner) rootView.findViewById(R.id.tripType);
        sendButton = (Button) rootView.findViewById(R.id.sendTripButton);
        sendButton.setOnClickListener(this);

        location = (EditText) rootView.findViewById(R.id.locationButton);
//        location.setOnClickListener(this);

        // CALENDAR AND TIME PICKERS

//        calendarView = (CalendarView) rootView.findViewById(R.id.expandble_calnenderView);
//        calendarView.setVisibility(View.GONE);
//        timePicker = (TimePicker) rootView.findViewById(R.id.timePicker);
//        timePicker.setVisibility(View.GONE);

        // The IMAGES
        image1 = (ImageView) rootView.findViewById(R.id.image1);
        image2 = (ImageView) rootView.findViewById(R.id.image2);
        image3 = (ImageView) rootView.findViewById(R.id.image3);

        drawable1 = image1.getDrawable();
        drawable2 = image2.getDrawable();
        drawable3 = image3.getDrawable();

        image1.setOnClickListener(this);
        image2.setOnClickListener(this);
        image3.setOnClickListener(this);

        // PROGRESS BAR
        progressBarInfo = rootView.findViewById(R.id.progressBarInfo);
        // HANDLE THE TIME PICKER LISTENER
        final TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
                view.setVisibility(view.isShown()
                        ?View.GONE
                        :View.VISIBLE);
            }
        };
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 1 ;
                showThePickers();
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 2;
                showThePickers();
            }
        });
        setupArrayAdapter();
        return rootView;
    }
    List<String> mPaths = new ArrayList<>();
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != getActivity().RESULT_OK){
            return;
        }
        System.out.println("onActivityResult- - ");
        if(resultCode == getActivity().RESULT_OK) {
            System.out.println("RESULT_OK");
        Resources res = getResources();
//        Drawable shape = res.getDrawable(R.drawable.signup_email_button);
            if (requestCode == MAP_ACATIVITY){
                System.out.println("MAP_ACATIVITY");
                Bundle bundle = data.getExtras();
                double lat = bundle.getDouble("lat");
                double lng = bundle.getDouble("lng");
                String add = addTripHelper.getAddress(lat,lng,getContext());

                trip.setLocationName(add);
                location.setText(add);
//                System.out.println(add);
            } else if (requestCode == 1 ||requestCode == 2 ||requestCode == 3 ) {
                System.out.println("100000");
                Uri returnUri = data.getData();
                ResizePickedImage resizePickedImage = new ResizePickedImage();
                String realePath = resizePickedImage.getRealPathFromURI(returnUri,getActivity());
                System.out.println(realePath);
                String compresedImagePath;
                Bitmap bitmapImage = null;
                try {
                    bitmapImage = addImageFromStudio.getThumbnail(returnUri, getContext());
                    if (requestCode==1) {

                        compresedImagePath = resizePickedImage.resizeAndCompressImageBeforeSend
                                (getContext(),realePath, "image1");
                        if(!mPaths.contains(compresedImagePath))
                            mPaths.add(compresedImagePath);
                        image1.setImageBitmap(bitmapImage);

                    }else if (requestCode==2){
                        compresedImagePath = resizePickedImage.resizeAndCompressImageBeforeSend
                                (getContext(),realePath, "image1");
                        if(!mPaths.contains(compresedImagePath))
                            mPaths.add(compresedImagePath);
                        image2.setImageBitmap(bitmapImage);

                    }else if (requestCode==3) {
                        compresedImagePath = resizePickedImage.resizeAndCompressImageBeforeSend
                                (getContext(),realePath, "image1");
                        if(!mPaths.contains(compresedImagePath))
                        mPaths.add(compresedImagePath);
                        image3.setImageBitmap(bitmapImage);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }



            }
        }
    }
    public void showThePickers() {
        Calendar c = Calendar.getInstance();
        DatePickerDialog datePickerDialog;
        datePickerDialog = new DatePickerDialog(getContext(),R.style.TimePickerTheme,datePickerListener,c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    String[] plants = new String[]{
            "Select an item...",
            "Diving from the beach",
            "Diving from the boat",
            "Diving courses"
    };

    private void setupArrayAdapter() {
        spinnerArrayAdapter =  new ArrayAdapter<String>(
                getContext(),android.R.layout.simple_spinner_item,plants){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }

        };
        System.out.println("------ "+ spinnerArrayAdapter.getCount());
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tripeType.setAdapter(spinnerArrayAdapter);
        tripeType.setOnItemSelectedListener(this);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        tripTy = position;
        tripClass  = parent.getItemAtPosition(position).toString();
        if (position != 2) {
            minmummTicketNum.setVisibility(View.VISIBLE);
            minmmumTicketNumberTV.setVisibility(View.VISIBLE);
        }else {
            minmummTicketNum.setVisibility(View.GONE);
            minmmumTicketNumberTV.setVisibility(View.GONE);
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(getContext(),"Please select item",Toast.LENGTH_LONG).show();
    }
    private void moveToCompleteProfileFragment() {
        CompleteProfileFragment completeProfileFragment = new CompleteProfileFragment();
        FragmentTransaction fragmentTransaction =  this.getFragmentManager().beginTransaction();
        completeProfileFragment.user = user;
        fragmentTransaction.add(R.id.main_frame, completeProfileFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void chickTheForm() {
        // CHECK
        if (user.getCompletedInfo() == false){
            Toast.makeText(getContext(),"Complete your profile first",Toast.LENGTH_LONG).show();
            moveToCompleteProfileFragment();
            return;
        }
        if (tripTy == 0 ){
            Toast.makeText(addTripHelper, "Plaes select the Trip type", Toast.LENGTH_SHORT).show();
            return;
        }
        if (ticketNum.getText().toString().isEmpty()){
            ticketNum.setError(getString(R.string.ticketnum));
            return;
        }if (tripDescription.getText().toString().isEmpty()){
            tripDescription.setError(getString(R.string.tripDescription));
            return;
        }
        if (minmummTicketNum.getText().toString().isEmpty() && tripeType.getSelectedItemPosition()!=2){
            tripName.setError("Please Enter trip minmumm Ticket number ");
            tripName.requestFocus();
            return;
        }if (price.getText().toString().isEmpty()){
            return;
        }if (tripName.getText().toString().isEmpty()){
            return;
        }
        Date date= new Date();
        final String name = tripName.getText().toString().trim();
        String description = tripDescription.getText().toString().trim();
        String ticketNumberString = ticketNum.getText().toString();
        int ticketNumber = Integer.parseInt(ticketNumberString);
        String minmummTicketNumString = minmummTicketNum.getText().toString();

        int priceInt = Integer.parseInt(price.getText().toString());
        String locationName = location.getText().toString().trim();
        if (locationName.isEmpty()){
            location.setError("Please enter a city name");
            location.requestFocus();
        }if (name.isEmpty()) {
            tripName.setError("Please Enter trip name");
            tripName.requestFocus();
            return;
        }if (description.isEmpty()){
            tripDescription.setError("Please enter trip description");
            tripDescription.requestFocus();
            return;
        }if (tripClass.isEmpty()){
            tripeType.requestFocus();
            return;
        }if (ticketNumber == 0){
            ticketNum.setError("Please enter ticket number");
            ticketNum.requestFocus();
            return;
        }if (priceInt == 0){
            price.setError("Please enter the price for the tikcet");
            price.requestFocus();
            return;
        }if (startTimeStamp == null){
            Toast.makeText(getContext(),"Please select start date",Toast.LENGTH_LONG).show();
            return;
        }if (startTimeStamp < date.getTime()){
            Toast.makeText(getContext(),"Please enter correct start date",Toast.LENGTH_LONG).show();
            startDate.setTextColor(Color.RED);
            return;
        }if (startTimeStamp >= endTimeStamp) {
            Toast.makeText(getContext(), "Please enter correct end date", Toast.LENGTH_LONG).show();
            endDate.setTextColor(Color.RED);
            return;
        }if (endTimeStamp == null){
            Toast.makeText(getContext(),"Please select end date",Toast.LENGTH_LONG).show();
            return;
        }if((condtionAndTerms.getVisibility() == View.VISIBLE) && (!proDiverCheckBox.isChecked())){
            Toast.makeText(getContext(), "Please read the Privcy and Condtions", Toast.LENGTH_SHORT).show();
            return;
        }
        loader.setVisibility(View.VISIBLE);
        sendButton.setEnabled(false);
        System.out.println(tripTy);
        String uid = newChildRef();
        myRef = database.getReference();
        final String key=myRef.child("Trips").push().getKey();
        if (mPaths.size() != 0){
            final List<String> imagesURL = new ArrayList<>();
            for (int i = 0; i < mPaths.size(); i++) {
                System.out.println("Start upload images");
                InputStream stream = null;
                try {
                    stream = new FileInputStream(new File(mPaths.get(i)));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                final String uuid = "image"+i;
                final StorageReference ref = storage.getReference().child("TripImages").child(user.getUid()).child(uuid+".jpg");

                final UploadTask uploadTask = ref.putStream(stream);
//                uploadingImagePB.setProgress(0);
                uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                        progressBarLayout.setVisibility(View.VISIBLE);
                        loaderStatus.setText("Uploading images...");
//                        System.out.println("onProgress "+taskSnapshot.getTotalByteCount() +" "+ taskSnapshot.getBytesTransferred());

                        double progress = (-100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        Log.d(TAG, "onProgress: "+ progress);
                        System.out.println("Upload is " + progress + "% done");
                        int currentprogress = (int) progress;
//                        uploadingImagePB.setProgress(currentprogress);
                    }
                });
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        // GET THE IMAGE DOWNLOAD URL

                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                System.out.println("uri "+ uri.toString());
//                                uploadingImagePB.setProgress(0);
//                                progressBarLayout.setVisibility(View.GONE);

                                Log.d(TAG, "onSuccess: the image uploded "+ uri.toString());
                                Map<String,Object> map = new HashMap<>();
                                imagesURL.add(uri.toString());
                                map.put("imagesURL",imagesURL);
                                myRef.child("Trips").child(key).updateChildren(map);
                                /// START NEW FRAGMENT
                                loader.setVisibility(View.GONE);
                                AddTripFragment newAddTripFragment = new AddTripFragment();
                                newAddTripFragment.user = user;
//                                Toast.makeText(getContext(), "Your trip has been send Succuffly", Toast.LENGTH_SHORT).show();
                                FragmentTransaction tr = getFragmentManager().beginTransaction();
                                tr.replace(R.id.addTripe, newAddTripFragment);
                                tr.commit();
//                            imagesURL.add(uri.toString());
                                DatabaseReference dRef=FirebaseDatabase.getInstance().getReference().child("Notification").child(user.getUid());
                                Notifi notift = new Notifi("0","your trip "+name+" is under check","thank you");
                                dRef.push().setValue(notift);
                                System.out.println(" END OF THE ON OnSuccessListener");
                            }
//                            System.out.println(" END OF THE ON OnSuccessListener");
                        });
                        System.out.println(" END OF THE ON COMPLETE");
                    }
                });
            }
        }
        System.out.println(" END OF THE CHECK FUNCTION");
        trip = new Trip();
        trip.setName(name);
        trip.setType(tripTy);
        trip.setTripClass(tripClass);
        trip.setDescription(description);
        trip.setSartDate(DateLongTostring(startTimeStamp));
        trip.setTicketNumber(ticketNumber);
        trip.setPrice(priceInt);
        trip.setProviderId(user.getUid());
        trip.setProvider_bio(user.getBio());
        trip.setProvider_name(user.getName());
        trip.setProvider_name(user.getName());
        trip.setProvider_bio(user.getBio());
trip.setLocationName(locationName);
trip.setEndDate(DateLongTostring(endTimeStamp));
        if (tripeType.getSelectedItemPosition()!=2) {
            int minmummTicket = Integer.parseInt(minmummTicketNumString);
trip.setMinummTicket(minmummTicket);
            /// start new fragment
        }

        myRef.child("Trips").child(key).setValue(trip);
        // add this trip to mytrip list
        FirebaseUser user_=FirebaseAuth.getInstance().getCurrentUser();
        myRef.child("Users").child(user_.getUid()).child("MyTrips").push().setValue(key);
if (mPaths.size()==0)
{
    /// START NEW FRAGMENT
    loader.setVisibility(View.GONE);
    AddTripFragment newAddTripFragment = new AddTripFragment();
    newAddTripFragment.user = user;
//                                Toast.makeText(getContext(), "Your trip has been send Succuffly", Toast.LENGTH_SHORT).show();
    FragmentTransaction tr = getFragmentManager().beginTransaction();
    tr.replace(R.id.addTripe, newAddTripFragment);
    tr.commit();
}


    }

    private String DateLongTostring(Long date) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE dd/MM");
        String dateString=null;

            dateString = formatter.format(date);
            return dateString;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sendTripButton:
                chickTheForm();
                break;
            case R.id.locationButton:
                getMapIntent();
                break;
            case R.id.image1:
                // OPEN THE STAUDIO
                selectImage("image1");
                // REPLACE THE IMAGE VIEW WITH SELECTED IMAGE
                break;
            case R.id.image2:
                selectImage("image2");
                break;
            case R.id.image3:
                selectImage("image3");
                break;
        }
    }
    int Pick_FROM_FILE = 4;
    public String newChildRef(){
        DatabaseReference newChildRef = database.getReference().push();
//        newChildRef.updateChildren(map);
        return newChildRef.getKey();
    }
    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};


    public void selectImage(String image1) {
        if (EasyPermissions.hasPermissions(getContext(), galleryPermissions)) {
            Intent pickerPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            switch (image1) {
                case "image1":startActivityForResult(pickerPhotoIntent, 1);
break;
                case "image2":startActivityForResult(pickerPhotoIntent, 2);
                    break;
                case "image3":startActivityForResult(pickerPhotoIntent, 3);
                    break;

            }

            }
              else {
            EasyPermissions.requestPermissions(getActivity(), "Access for storage",
                    1000, galleryPermissions);
        }
    }
    public void getMapIntent() {
        Intent mapIntent = new Intent(getContext(),MapsActivity.class);

        startActivityForResult(mapIntent,MAP_ACATIVITY);
//        startActivity(mapIntent);
        System.out.println("getMapIntent");
    }
}
