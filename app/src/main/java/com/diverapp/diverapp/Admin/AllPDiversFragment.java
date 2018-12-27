package com.diverapp.diverapp.Admin;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.diverapp.diverapp.MainActivity;
import com.diverapp.diverapp.R;
import com.diverapp.diverapp.User;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllPDiversFragment extends Fragment implements AdapterView.OnItemSelectedListener {


    RecyclerView recyclerView;
    public List<User> users;
    Spinner userType;
    public Button logout;
    ArrayAdapter<String> spinnerArrayAdapter ;
    public AllPDiversFragment() {
        // Required empty public constructor
    }
    String[] plants = new String[]{
            "Select an item...",
            "All",
            "Pro Diviners",
            "Diviers"
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // recyclerView
//        Log.e(MainActivity.TAG, "onCreateView: "+ users.size());
        View rootView = inflater.inflate(R.layout.fragment_all_pdivers, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.allDivers);
        AllDiversAdapter allDiversAdapter = new AllDiversAdapter(getContext(),users,this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(allDiversAdapter);
        userType = (Spinner) rootView.findViewById(R.id.userType);
        logout  = (Button) rootView.findViewById(R.id.logout);
//        Log.e(MainActivity.TAG, "onCreateView: "+ users.size() );
        setupArrayAdapter();
        return rootView;
    }
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
        userType.setAdapter(spinnerArrayAdapter);
        userType.setOnItemSelectedListener(this);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.e(MainActivity.TAG, "onItemSelected: "+ position );
        List<User> filterArry = filterTheUsers(position);

            AllDiversAdapter allDiversAdapter = new AllDiversAdapter(getContext(),filterArry,this);
            recyclerView.setAdapter(allDiversAdapter);
    }
    private List<User> filterTheUsers(int position){
        List<User> divers = new ArrayList<>();
        List<User> proDivers = new ArrayList<>();
        for (int i=0;i<users.size();i++) {
            User user = users.get(i);
            if (user.getType() == 1 ){
                proDivers.add(user);
            }else if (user.getType() == 2) {
                divers.add(user);
            }
        }
        if (position == 2 ){
            return proDivers;
        }else if (position == 3 ){
            return divers;
        }else {
            return users;
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
