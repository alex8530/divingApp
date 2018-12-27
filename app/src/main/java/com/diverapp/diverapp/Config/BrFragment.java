package com.diverapp.diverapp.Config;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.diverapp.diverapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BrFragment extends Fragment {


    BraintreeFragment mBraintreeFragment;
    public BrFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        try {
            mBraintreeFragment = BraintreeFragment.newInstance(getActivity(), Config.PAYPAL_CLINET_ID);
            // mBraintreeFragment is ready to use!
        } catch (InvalidArgumentException e) {
            // There was an issue with your authorization string.
        }
        return inflater.inflate(R.layout.fragment_br, container, false);
    }

}
