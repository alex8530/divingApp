package com.diverapp.diverapp.Diver;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.api.BraintreeFragment;
import com.diverapp.diverapp.Config.Config;
import com.diverapp.diverapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.List;

import Modle.Order;
import Modle.Request;

//import com.paypal.android.sdk.payments.PayPalConfiguration;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentMethodFragment extends Fragment implements View.OnClickListener {

    public static final int PAYPAL_PAYMENT_REQUEST = 7177;
    public static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLINET_ID);
    public CartFragment cartFragment;
    CheckBox paypalCheckBox,cashCheckBox;
//    EditText paypalPassword,paypalEmail;
    Button placeOrderCheckOut;
    TextView total;
    LinearLayout paypalInfo;
    public PaymentMethodFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        hanldePlaceOrderCheckOut();
        View rootView =  inflater.inflate(R.layout.fragment_payment_method, container, false);
//        paypalInfo = (LinearLayout) rootView.findViewById(R.id.paypalInfo);
        paypalCheckBox = (CheckBox) rootView.findViewById(R.id.paypalCheckBox);
        cashCheckBox = (CheckBox) rootView.findViewById(R.id.cashCheckBox);
        total = (TextView) rootView.findViewById(R.id.total);
//        paypalEmail = (EditText) rootView.findViewById(R.id.paypalEmail);
//        paypalPassword = (EditText) rootView.findViewById(R.id.paypalPassword);
        placeOrderCheckOut = (Button) rootView.findViewById(R.id.placeOrderCheckOut);
//        paypalInfo.setVisibility(View.GONE);

        cashCheckBox.setOnClickListener(this);
        paypalCheckBox.setOnClickListener(this);
        placeOrderCheckOut.setOnClickListener(this);
        total.setText(String.valueOf(cartFragment.totalInt));
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.paypalCheckBox:
//                moveToCompleteProfileFragment();
//                Log.d(MainActivity.TAG, "onClick: "+ paypalCheckBox.isChecked());
                total.setText(String.valueOf(cartFragment.totalInt));
                cashCheckBox.setChecked(false);
                break;
            case R.id.cashCheckBox:
                if (cashCheckBox.isChecked()){
                    total.setText(String.valueOf(((cartFragment.totalInt * 15) / 100)+ cartFragment.totalInt));
                }
                paypalCheckBox.setChecked(false);
                break;
            case R.id.placeOrderCheckOut:
                hanldePlaceOrderCheckOut();
                break;
        }
    }

    BraintreeFragment mBraintreeFragment;
    private void hanldePlaceOrderCheckOut() {
        if (cashCheckBox.isChecked()){
            double total = (((cartFragment.totalInt *5) / 100) + cartFragment.totalInt);
//            final Request request = new Request(cartFragment.user.getName(),cartFragment.user.getMobilNumber(),total,cartFragment.cart,cartFragment.user.getUid());
//            cartFragment.requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);
            updateTicketsNumber(cartFragment.cart);
            cartFragment.clearTheShoppingList();
            cartFragment.mainActivity.mCartItemCount = 0;
            cartFragment.mainActivity.setupBadge();
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        }
        if (paypalCheckBox.isChecked() == true){
            PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(cartFragment.totalInt/ 3.76)),
                    "USD","Purches from DiverApp",PayPalPayment.PAYMENT_INTENT_SALE);
            Intent intent = new Intent(getContext(), PaymentActivity.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
            startActivityForResult(intent,PAYPAL_PAYMENT_REQUEST);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("Hello onActivityResult");
        if (requestCode == PAYPAL_PAYMENT_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation configuration = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (configuration != null) {
                    try {
                        String paymentDetiles = configuration.toJSONObject().toString(4);
//                        final Request request = new Request(cartFragment.user.getName(),cartFragment.user.getMobilNumber(),cartFragment.totalInt,cartFragment.cart,cartFragment.user.getUid());
//                        cartFragment.requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);

                        cartFragment.clearTheShoppingList();
                        updateTicketsNumber(cartFragment.cart);
                        cartFragment.mainActivity.mCartItemCount = 0;
                        cartFragment.mainActivity.setupBadge();
                        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
                        System.out.println(paymentDetiles);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if( resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(getContext(), "Cancle", Toast.LENGTH_SHORT).show();
            }
        }else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID){
            Toast.makeText(getContext(), "Invalid", Toast.LENGTH_SHORT).show();
        }
    }
    public void updateTicketsNumber(List<Order> orders){
        for(int i=0;i<orders.size();i++){
            final Order order = orders.get(i);
//            Trip trip = order.getTrip();
            final DatabaseReference trip = cartFragment.datebase.getReference().child("Trips")
                    .child(order.getProdictId()).child(order.getProviderID());
            trip.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean closed = (boolean) dataSnapshot.child("ticketNumber").getValue();
                    if (closed)
                    {
                        // this trip is closed
                        return;
                    }
                    Long nn = (Long) dataSnapshot.child("ticketNumber").getValue();
                    int nowTicketsNum = nn.intValue();
                    int ticketRemening = nowTicketsNum - order.getQuantity();
                    if(ticketRemening<0)
                    {
                      // this trip complet
                        return;

                    }
                    else {
                        trip.child("ticketNumber").setValue(ticketRemening);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
