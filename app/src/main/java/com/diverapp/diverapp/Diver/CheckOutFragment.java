package com.diverapp.diverapp.Diver;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.diverapp.diverapp.R;
import com.stripe.android.view.CardInputWidget;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckOutFragment extends Fragment {


    public CheckOutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_check_out, container, false);
        CardInputWidget mCardInputWidget = (CardInputWidget) rootView.findViewById(R.id.card_input_widget);
        
        return rootView;
    }

}
