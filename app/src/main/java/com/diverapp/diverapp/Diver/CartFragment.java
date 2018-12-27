package com.diverapp.diverapp.Diver;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.diverapp.diverapp.MainActivity;
import com.diverapp.diverapp.R;
import com.diverapp.diverapp.SignupLogin.CompleteProfileFragment;
import com.diverapp.diverapp.SignupLogin.LoginActivity;
import com.diverapp.diverapp.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Modle.Order;
import database.DBManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment implements View.OnClickListener {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutParams;


    public MainActivity mainActivity;
    public User user;
    public List<Order> cart = new ArrayList<>();

    FirebaseDatabase datebase;
    DatabaseReference requests;

    DatabaseReference myRef;
    FirebaseAuth mAuth;

    Button placeOrder,emptyCart;
    TextView total , vat;
    CartViewHolder.CartViewHolderAdapter cartViewHolderAdapter;
    public CartFragment() {
        // Required empty public constructor
    }
    public void fetchAllTheOrder(Context context) {
         cart.clear();
        DBManager dbManager = new DBManager(context);
        dbManager.open();
        Cursor cursor = dbManager.fetch();
        if (cursor != null){
            if (cursor.moveToFirst()){
//                Log.e(MainActivity.TAG, "fetchAllTheOrder: cursor "+cursor.getCount() );
                do {
//                    Log.e(MainActivity.TAG, "fetchAllTheOrder: do "+cursor.getCount() );
                    // get the data into array, or class variable
                    Order order = new Order(cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getInt(4),
                            cursor.getDouble(5),
                            cursor.getInt(6),
                            cursor.getString(7));
                    cart.add(order);
                } while (cursor.moveToNext());
//                Log.e(MainActivity.TAG, "fetchAllTheOrder: after "+cursor.getCount() );
//            while (cursor.moveToNext());
//// String prodictId, 1
//// String provider2
//// , String prodictName3
//// , int quantity4
//// , int price 5
//// , int discount 6
            }
            dbManager.close();
        }
//        Log.d(MainActivity.TAG, "fetchAllTheOrder: 1");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        fetchAllTheOrder(getContext());
//        Log.e(MainActivity.TAG, "onCreateView: cart.size() "+ cart.size() );
//        DBManager dbManager = new DBManager(getContext());
//        dbManager.open();
//        dbManager.deleteFromCart();
//        dbManager.close();
//        Log.d(MainActivity.TAG, "fetchAllTheOrder: "+ user);
        datebase = FirebaseDatabase.getInstance();
        requests = datebase.getReference("Requests");

        mAuth = FirebaseAuth.getInstance();
        myRef = datebase.getReference("Users");
        if (mAuth.getCurrentUser() == null){
            placeOrder.setText(" Login to procces check out");
            placeOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
            });
            // show the user that he is not login
        }else {
            fetchUserFromDatabase();
        }
        View rootView = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = (RecyclerView ) rootView.findViewById(R.id.cartListItems);
        total = (TextView) rootView.findViewById(R.id.totalTV);
        vat = (TextView) rootView.findViewById(R.id.vatTV);
        placeOrder = (Button) rootView.findViewById(R.id.placeOrderButton);
        emptyCart = (Button) rootView.findViewById(R.id.emptyCart);


        loadOrders();

        emptyCart.setOnClickListener(this);
        placeOrder.setOnClickListener(this);
        return rootView;
        // FireBase
    }
    double totalInt = 0;
    double VAT = 0.0;
    public void clearTheShoppingList() {

        DBManager dbManager = new DBManager(getContext());
        dbManager.open();
        dbManager.deleteFromCart();
        dbManager.close();
        Toast.makeText(getContext(),"Your cart is empty 💔",Toast.LENGTH_LONG).show();
        cart.clear();
        totalInt = 0;
        VAT =0;
        total.setText(String.valueOf(totalInt));
        vat.setText(String.valueOf(VAT));
        cartViewHolderAdapter = new CartViewHolder.CartViewHolderAdapter(cart,getContext());
        recyclerView.setAdapter(cartViewHolderAdapter);
    }
    public void loadOrders() {
        cartViewHolderAdapter = new CartViewHolder.CartViewHolderAdapter(cart,getContext());
        layoutParams = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutParams);
        recyclerView.setAdapter(cartViewHolderAdapter);
        for (int i=0;i<cart.size(); i++){
            Order order = cart.get(i);
            totalInt += order.getPrice() * order.getQuantity() ;
        }
        if (cart.isEmpty()){
            placeOrder.setEnabled(false);
        }
        VAT = ((totalInt * 5) /100);
        vat.setText(String.valueOf(VAT+" RS"));
        total.setText(String.valueOf(totalInt+VAT)+" RS");

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.placeOrderButton:
                placeOrderToDataBase();
                break;
            case R.id.emptyCart:
                clearTheShoppingList();
                break;
        }
    }
    private void placeOrderToDataBase() {
        // SAVE REQURST TO FIREBASE DATABASE
        user=MainActivity.user;
        if (user.getCompletedInfo() == false){
            moveToCompleteProfileFragment();
            // show complete profil fragment + dismiss the and complete the Proccess
        }else {

            PaymentMethodFragment paymentMethodFragment = new PaymentMethodFragment();
            FragmentTransaction fragmentTransaction =  this.getFragmentManager().beginTransaction();
//            completeProfileFragment.user = user;/
            paymentMethodFragment.cartFragment = this;
            fragmentTransaction.add(R.id.main_frame, paymentMethodFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }
    }

    private void moveToCompleteProfileFragment() {
        CompleteProfileFragment completeProfileFragment = new CompleteProfileFragment();
        FragmentTransaction fragmentTransaction =  this.getFragmentManager().beginTransaction();
        completeProfileFragment.user = user;
        fragmentTransaction.add(R.id.main_frame, completeProfileFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void fetchUserFromDatabase() {
        final String uid = mAuth.getUid().toString();
        myRef.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("dataSnapshot " + dataSnapshot);
//                user = dataSnapshot.getValue(User.class);
                HashMap<String, Object> dict = (HashMap<String, Object>) dataSnapshot.getValue();
                System.out.println(dict);
                user = new User();
                // CONECT THE MENUE
//                Log.d(MainActivity.TAG, "onDataChange: "+ user);
//                createAButtonsForUser(user, mNavView);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
