package com.diverapp.diverapp.Admin;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.diverapp.diverapp.R;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

import Modle.Request;

import static com.diverapp.diverapp.MainActivity.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestsFragment extends Fragment {

    public List<Request> requestList;

    RecyclerView requestRecyclerView;
    public RequestsFragment() {
        // Required empty public constructor
    }
    DatabaseReference myRef;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_requests, container, false);
        requestRecyclerView = (RecyclerView) rootView.findViewById(R.id.requestRecyclerView);
        Log.e(TAG, "onCreateView: "+ requestList.size() );
        final RequestsAdapter requestsAdapter = new RequestsAdapter(requestList,getContext(),this);
        requestRecyclerView.setHasFixedSize(true);
        requestRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        requestRecyclerView.setAdapter(requestsAdapter);
        return  rootView;
    }

}
