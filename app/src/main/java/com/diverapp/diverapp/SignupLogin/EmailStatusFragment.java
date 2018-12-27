package com.diverapp.diverapp.SignupLogin;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.diverapp.diverapp.Diver.ShopFragment;
import com.diverapp.diverapp.MainActivity;
import com.diverapp.diverapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import static java.lang.Thread.sleep;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmailStatusFragment extends Fragment implements View.OnClickListener {

    private   FirebaseAuth mAuth;
   public Button logout, resendButton;
    public EmailStatusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_email_status, container, false);
        logout = rootView.findViewById(R.id.logout);
        resendButton = rootView.findViewById(R.id.resendButton);
mAuth=FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null){
            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        thread_();
                        Toast.makeText(getContext(),
                                "Verification email sent to " + mAuth.getCurrentUser().getEmail(),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(MainActivity.TAG, "sendEmailVerification", task.getException());
                        Toast.makeText(getContext(),
                                "Failed to send verification email.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        logout.setOnClickListener(this);
        resendButton.setOnClickListener(this);
        return  rootView;
    }
    Fragment mFragment;
    private void createAFragment(Fragment blankFragment) {
mFragment=EmailStatusFragment.this;
        FragmentTransaction fragmentTransaction =  mFragment.getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_frame, blankFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    @Override
    public void onStart() {
        super.onStart();
        if ( mAuth.getCurrentUser().isEmailVerified())
        {
           // createAFragment(new ShopFragment());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.resendButton:

                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            thread_();
                            Toast.makeText(getContext(),
                                    "Verification email sent to " + mAuth.getCurrentUser().getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(MainActivity.TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(getContext(),
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.logout:
                getLoginActivity();
                mAuth.signOut();
                break;
        }
    }
    private void getLoginActivity() {
        startActivity(new Intent(getContext(), LoginActivity.class));
        getActivity().finish();
    }
    private void thread_(){
        resendButton.setEnabled(false);
        resendButton.setText("We sent verification code");
//        final int[] i = {0};
//        new Handler().post(new Runnable() {
//
//
//                // add your code here
//
//
//            @Override
//            public void run() {
//
//
//            // a potentially time consuming task
//                    if(i[0] <30000)
//                    {
//
//                        try {
//                            resendButton.setEnabled(false);
//                            String dis= Integer.toString(30- i[0] /1000);
//                            resendButton.setText("Wait "+dis+" S");
//                            sleep(1);
//                            i[0] +=1;
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }else {
//                        resendButton.setEnabled(true);
//                        resendButton.setText(getString(R.string.resend));
//                    }
//            }
//        });
//        new Thread(new Runnable() {
//            int i=0;
//
//            public void run() {
//                // a potentially time consuming task
//              if(i<30000)
//              {
//
//                  try {
//                      resendButton.setEnabled(false);
//                      resendButton.setText(i/1000);
//                      sleep(1000);
//                      i+=1000;
//                  } catch (InterruptedException e) {
//                      e.printStackTrace();
//                  }
//              }else {
//                  resendButton.setEnabled(true);
//                  resendButton.setText(getString(R.string.resend));
//              }
//            }
//        }).start();
    }
}
