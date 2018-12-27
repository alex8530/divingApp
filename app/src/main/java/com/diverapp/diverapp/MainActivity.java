package com.diverapp.diverapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.diverapp.diverapp.Admin.AllPDiversFragment;
import com.diverapp.diverapp.Admin.PanddingFragment;
import com.diverapp.diverapp.Admin.RequestsFragment;
import com.diverapp.diverapp.Admin.TripsFragment;
import com.diverapp.diverapp.Diver.CartFragment;
import com.diverapp.diverapp.Diver.ShopFragment;
import com.diverapp.diverapp.PDiver.AddTripFragment;
import com.diverapp.diverapp.SignupLogin.EmailStatusFragment;
import com.diverapp.diverapp.SignupLogin.LoginActivity;
import com.diverapp.diverapp.SignupLogin.SelectSignupMethodActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.zxing.integration.android.IntentIntegrator;
import com.wang.avi.AVLoadingIndicatorView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import database.SQLiteHelper;

import static android.content.Context.MODE_PRIVATE;

//import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class MainActivity extends AppCompatActivity {

/*
ffgAA_dQYfM:APA91bHSwTfRnovV61BSYtkS5PNOQmuiosW3DHodaMOJ9pp5Wf0Pvri4czBIBxVZOr_ICdqvQm5wAIoLHrkVALc-qY3FybJBNLPq0uV2iYB_-FrDP2K1nIqpDTOjsxVYb3SeGSi94xrw
 */
    public static final String TAG = "YOUR-TAG-NAME";
    public FirebaseAuth mAuth;
    FirebaseUser user_;
    private FrameLayout mMainFrameLayout;
    private BottomNavigationView mNavView;
    ImageButton locButton;

    public ProfileFragment uProfile;

    TextView textCartItemCount;
    public int mCartItemCount;
    LinearLayout loader;
    private LocationManager locationManager;
    private LocationListener locationListener;
    FetchTripsHelper fetchTripsHelper;
    FirebaseDatabase database;
    DatabaseReference myRef;
   public static User user;
    AVLoadingIndicatorView avi;
    String TOKENID;
    private static final int NOTIFICATION_PERMISSION_CODE = 123;
    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

user=new User();
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        user.setType( prefs.getInt("usertype", -1));
        loader = findViewById(R.id.Loader);
        fetchTripsHelper = new FetchTripsHelper();
//        fetchTripsHelper.fetrfkmfgvfkmvc();
        fetchTripsHelper.fetchTheUsers();
        cartFragment = new CartFragment();
        cartFragment.mainActivity = this;
        requestNotificationPermission();
        setupLocationManager();
        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
//        cartFragment.fetchAllTheOrder(getApplicationContext());
        mCartItemCount= cartFragment.cart.size();
        Log.e(TAG, "onCreatecartFragment: "+ cartFragment.cart.size());

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ACD7EA")));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_assetlogo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        System.out.println("onCreate main");
        System.out.println("-- 1 onCreate");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        TOKENID = FirebaseInstanceId.getInstance().getToken();
        mAuth = FirebaseAuth.getInstance();
        user_=mAuth.getCurrentUser();
        if(user_!=null)
        user.setUid(user_.getUid());
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

//        mAuth.signOut();
        if (mAuth.getCurrentUser() == null) {
            System.out.println("Please login first");
            getLoginActivity();
            return;
        }
        mMainFrameLayout = (FrameLayout) findViewById(R.id.main_frame);
        mNavView = (BottomNavigationView) findViewById(R.id.nav_bar0);
        mAuth.getCurrentUser().reload();
        checkTheEmailVerfiy();
        System.out.println("USER LOGIN IN ");
        // CREATE THE NAV




    }
    public void checkTheEmailVerfiy() {
        Log.e(TAG, "checkTheEmailVerfiy: "+ mAuth.getCurrentUser().getEmail() );
        if (mAuth.getCurrentUser().getEmail() != ""){
            if (mAuth.getCurrentUser().isEmailVerified() == false){
                EmailStatusFragment emailStatusFragment = new EmailStatusFragment();

                createAFragment(emailStatusFragment);
            }else {
                fetchUserFromDatabase();
            }
        }else {
            fetchUserFromDatabase();
        }
//        Toast.makeText(this, "email "+mAuth.getCurrentUser().isEmailVerified(), Toast.LENGTH_SHORT).show();

    }
    private  MenuItem cart,scan;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.main_menu, menu);

        cart = menu.findItem(R.id.action_cart);
scan=menu.findItem(R.id.action_scan);
if(user.getType()==2){scan.setVisible(false);}
        View actionView = MenuItemCompat.getActionView(cart);
          textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);
        setupBadge();
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(cart);
            }
        });
        return true;
    }
    public void setupBadge() {
        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                textCartItemCount.setVisibility(View.GONE);
            }else {
                cart.setEnabled(true);
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));

                    textCartItemCount.setVisibility(View.VISIBLE);

            }
        }
    }
    CartFragment cartFragment;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.e(TAG, "onOptionsItemSelected: " );
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.e(TAG, "onOptionsItemSelected: false" );
                return false;
            }
        });
        if (id == R.id.action_scan) {
            new IntentIntegrator(MainActivity.this)
                    .setCaptureActivity(ScannerActivity.class)
                    .initiateScan();

            // go to scan activity
        }
            if (id == R.id.action_cart) {
            try {
                item.setEnabled(false);

                if (cartFragment != null && cartFragment.isVisible()) {
                    // add your code here
                    return false;
                }
                textCartItemCount.setVisibility(View.GONE);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.main_frame, cartFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commitAllowingStateLoss();
                return true;
            }catch (Exception exp){}
        }
        return super.onOptionsItemSelected(item);
    }
    private void fetchUserFromDatabase() {
       myRef.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    user = dataSnapshot.getValue(User.class);
                    SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();

                    editor.putInt("usertype", user.getType());
                    editor.apply();

                    Log.e(TAG, "onDataChange: " + TOKENID);
                    if (!user.getTokenId().equals(TOKENID)) {
                        database.getReference().child("Users").child(user_.getUid()).child("tokinID").setValue(TOKENID);
                    }
                    createAButtonsForUser(mNavView);
                }
                // CONECT THE MENUE
//                Log.d(TAG, "onDataChange fetchUserFromDatabase: "+ user);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setupLocationManager() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                System.out.println("Location " + location.getLatitude());
                List<Double> userLoaction = new ArrayList<>();
                userLoaction.add(location.getLatitude());
                userLoaction.add(location.getLongitude());

                System.out.println(userLoaction.get(0));
                System.out.println(userLoaction.get(1));
//                user.location = userLoaction;
                locationManager.removeUpdates(locationListener);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent setting = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, 15);
                return;
            } else {
                locationRequest();
            }
        }

    }
    public void locationRequest() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 15:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationRequest();
                    return;
                }
        }
    }
    public void getLoginActivity() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    private void createAFragment(Fragment blankFragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, blankFragment);
//        fragmentTransaction.commit();
        fragmentTransaction.commitAllowingStateLoss();
    }

    public Boolean createAButtonsForUser( BottomNavigationView mBottmNav) {

//        fetchTripsHelper.fetchTheUsers();
//        User profileUser = new User();
//        for (int i=0;i<fetchTripsHelper.users.size();i++){
//            User fullUser = fetchTripsHelper.users.get(i);
//            if (fullUser.getUid().equals(user.getUid())){
//                user = fullUser;
//                break;
//            }
//        }
        // ADMIN = 0
        // P.DIVAER = 1
        // DIVER = 2
        final DatabaseReference tripRef = database.getReference().child("Trips");
//        fetchTripsHelper.checkForAnyUpdateStatus(tripRef);
        final AllPDiversFragment allPDiversFragment = new AllPDiversFragment();
        allPDiversFragment.users = fetchTripsHelper.users;
        mBottmNav.getMenu().clear();

        if (user.type == 0) {
            // SEND THE USER
            //CREATE THE NAVITEMS
            mNavView.inflateMenu(R.menu.navigation);
            mNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.nav_all_pDiver0:

                            createAFragment(allPDiversFragment);
                            // TABLE CONTENS ALL USERS TYP 1
                            mNavView.setItemBackgroundResource(R.color.mSkyColor);
                            Log.e(TAG, "onNavigationItemSelected: allPDiversFragment"+ allPDiversFragment.users.size() );
                            return true;
                        case R.id.nav_noti0:
                            TripsFragment tripsFragment = new TripsFragment();
                            tripsFragment.tripList = fetchTripsHelper.tripList;
                            Log.e(TAG, "onNavigationItemSelected: "+ fetchTripsHelper.tripList.size());
                            tripsFragment.users = fetchTripsHelper.users;
                            createAFragment(tripsFragment);
                            Log.e(TAG, "onNavigationItemSelected: onNavigationItemSelected" );
                            // TABLE CONTENS ALL THE LESTNERS

                            return true;
                        case R.id.nav_all_divers0:
                            RequestsFragment requestsFragment = new RequestsFragment();
                            createAFragment(requestsFragment);
                            requestsFragment.requestList = fetchTripsHelper.requests;
                            // TABLE CONTENTS ALL THE USERS TYP 2
                            return true;
                        case R.id.nav_pending0:
                            PanddingFragment panddingFragment = new PanddingFragment();
                            panddingFragment.users = fetchTripsHelper.users;
                            panddingFragment.trips = fetchTripsHelper.tripList;
                            panddingFragment.storeList = fetchTripsHelper.storeList;
                            stopAnim();
                            createAFragment(panddingFragment);
                            // ALL THE REQUEST
                            return true;
                        default:
                            return true;
                    }
                }
            });
            mBottmNav.setSelectedItemId(R.id.nav_all_pDiver0);
        } else
            if (user.type == 1) {
            //here you are pro diving
                //here you are diving
            mNavView.inflateMenu(R.menu.navigation_u1);
            mNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    mNavView.setItemBackgroundResource(R.color.mSkyColor);
                    switch (menuItem.getItemId()) {
                        case R.id.nav_profile_1:
                            uProfile = new ProfileFragment();
                            // SET THE USER;
                            uProfile.user = user;
                            uProfile.mainActivity = MainActivity.this;
                            System.out.println("I AM HERE ");
                            mNavView.setItemBackgroundResource(R.color.mSkyColor);
                            createAFragment(uProfile);
                            return true;
                        case R.id.nav_add_tripe_1:
                            AddTripFragment addTripFragment = new AddTripFragment();
                            addTripFragment.user = user;
                            createAFragment(addTripFragment);
                            return true;
                        case R.id.nav_notif_1:
                            NotificationFragment nofiticationFragment = new NotificationFragment();
                            nofiticationFragment.user = user;
                            createAFragment(nofiticationFragment);
                            return  true;
                            default: return false;
                        case R.id.nav_showTrips0:
                            ShopFragment shopFragment = new ShopFragment();
//                            shopFragment.tripList = fetchTripsHelper.storeList;
                            shopFragment.mainActivity = MainActivity.this;
                            shopFragment.users = fetchTripsHelper.users;
                            mNavView.setItemBackgroundResource(R.color.mSkyColor);
//                            Log.e(TAG, "onNavigationItemSelected: "+fetchTripsHelper.storeList.size() );
                            createAFragment(shopFragment);
                            return true;
                        // RETURN NOTIFICATION VIEW
                    }
                }
            });
            // SET SELECTED ITEM
            mBottmNav.setSelectedItemId(R.id.nav_profile_1);
        }else {
            // DIVER

            // SEND THE USER
            //CREATE THE NAVITEMS
            mNavView.inflateMenu(R.menu.navigation_u2);
//            final User finalUser = user;
            mNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.nav_profile_2:
                            // RETURN PROFILE VIEW
                            uProfile = new ProfileFragment();
                            uProfile.mainActivity = MainActivity.this;
                            uProfile.user = user;
                            mNavView.setItemBackgroundResource(R.color.mSkyColor);
                            createAFragment(uProfile);
                            // SET THE USER;
                            return  true;
                        // RETURN SHOPPE VIEW
                        case R.id.nav_shopping_2:
                            ShopFragment shopFragment = new ShopFragment();
//                            shopFragment.tripList = fetchTripsHelper.storeList;
                            shopFragment.mainActivity = MainActivity.this;
                            shopFragment.users = fetchTripsHelper.users;
                            mNavView.setItemBackgroundResource(R.color.mSkyColor);
//                            Log.e(TAG, "onNavigationItemSelected: "+fetchTripsHelper.storeList.size() );
                            createAFragment(shopFragment);
                            return true;
                        // RETURN NOTIFICATION VIEW
                        case R.id.nav_notif_2:
                            NotificationFragment nofiticationFragment = new NotificationFragment();
                            nofiticationFragment.user = user;
                            mNavView.setItemBackgroundResource(R.color.mSkyColor);
                            createAFragment(nofiticationFragment);
                            return  true;
                        default: return true;
                    }
                }
            });
            mBottmNav.setSelectedItemId(R.id.nav_shopping_2);
        }
        return true;
    }
    void startAnim(){
        avi.show();
        // or avi.smoothToShow();
    }

    void stopAnim(){
        avi.hide();
        // or avi.smoothToHide();
    }
    private void requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NOTIFICATION_POLICY) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_NOTIFICATION_POLICY)) {

        }

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NOTIFICATION_POLICY}, NOTIFICATION_PERMISSION_CODE );
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//        //Checking the request code of our request
//
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}