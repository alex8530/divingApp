package com.diverapp.diverapp.Admin;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.diverapp.diverapp.MainActivity;
import com.diverapp.diverapp.R;
import com.diverapp.diverapp.Trip;
import com.diverapp.diverapp.User;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PanddingFragment extends Fragment {

    TextView numberOfUsers,numberOfTrips,theMosteActiveUser,numberofActiveTrips,
            divingFromBeatch,divingFromBoat,divingCourses,
            numberOfDivers,numberOfProDivers;
    public List<User> users;
    public List<Trip> trips;
    public List<Trip> storeList;

    List<Trip> type1 = new ArrayList<>();
    List<Trip> type2 = new ArrayList<>();
    List<Trip> type3 = new ArrayList<>();

    List<User> divers = new ArrayList<>();
    List<User> proDivers = new ArrayList<>();

    public PanddingFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pandding, container, false);
        numberOfUsers = (TextView) rootView.findViewById(R.id.numberOfUsers );
        numberOfTrips = (TextView) rootView.findViewById(R.id.numberOfTrips);
        theMosteActiveUser = (TextView) rootView.findViewById(R.id.theMosteActiveUser);
        numberofActiveTrips = (TextView) rootView.findViewById(R.id.numberofActiveTrips);

        divingFromBeatch=  (TextView) rootView.findViewById(R.id.divingFromBeatch);
        divingFromBoat = (TextView) rootView.findViewById(R.id.divingFromBoat);
        divingCourses = (TextView) rootView.findViewById(R.id.divingCourses);

        numberOfDivers = (TextView) rootView.findViewById(R.id.numberOfDivers);
        numberOfProDivers = (TextView) rootView.findViewById(R.id.numberOfProDivers);

        for (int i=0;i<trips.size();i++){
            Trip trip = trips.get(i);
            switch (trip.getType()){
                case 1:
                    type1.add(trip);
                    break;
                case 2:
                    type2.add(trip);
                    break;
                case 3:
                    type3.add(trip);
                    break;
                default:
                    break;
            }
        }
        for (int i=0;i<users.size();i++){
            User user = users.get(i);
            switch (user.getType()){
                case 1 :
                    proDivers.add(user);
                    break;
                case 2:
                    divers.add(user);
                    break;
                    default:
                        break;
            }
        }
        numberOfUsers.setText(" "+String.valueOf(users.size()));
        numberOfTrips.setText(" "+String.valueOf(trips.size()));
        numberofActiveTrips.setText(" "+String.valueOf(storeList.size()));

        divingFromBeatch.setText(" "+String.valueOf(type1.size()));
        divingFromBoat.setText(" "+String.valueOf(type2.size()));
        divingCourses.setText(" "+String.valueOf(type3.size()));

        numberOfDivers.setText(" "+String.valueOf(divers.size()));
        numberOfProDivers.setText(" "+String.valueOf(proDivers.size()));

        for (int i=0;i<users.size();i++){
            User user = users.get(i);
            }

        return rootView;
    }

}
