package com.diverapp.diverapp.Admin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.diverapp.diverapp.Config.GlideApp;
import com.diverapp.diverapp.MainActivity;
import com.diverapp.diverapp.R;
import com.diverapp.diverapp.Trip;
import com.diverapp.diverapp.User;

import java.util.List;

public class TripCellAdapter extends RecyclerView.Adapter<TripCellAdapter.TripCellViewHolder> {

    List<Trip> tripList;
    Context mContx;
    Fragment mFragment;
    List<User> users;
    User user;
    int selected_position = 0 ;
    public TripCellAdapter(List<Trip> tripList, Context mContx ,Fragment fragment,List<User> users) {
        this.tripList = tripList;
        this.mContx = mContx;
        this.mFragment = fragment;
        this.users = users;
    }
    @NonNull
    @Override
    public TripCellViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContx);

        View view = layoutInflater.inflate(R.layout.trips_list_cell,viewGroup,false);
        return new TripCellViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull TripCellViewHolder viewHolder, int i) {
        Trip trip = tripList.get(i);
        for (int y=0;y<users.size();y++){
            User user = users.get(y);
            if (trip.getProviderId().equals(user.getUid())){
                this.user = user;
            }
        }
        if (trip.getImagesURL() != null){
            GlideApp.with(mContx).load(trip.getImagesURL().get(0)).placeholder(R.drawable.ic_assetlogo)
                    .centerCrop().into(viewHolder.tripImageView);
        }

//        Glide.with(mContx).load().into();
        viewHolder.tripName.setText(trip.getName());
        viewHolder.tripDiscreption.setText(String.valueOf(trip.getActivion()));
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public class TripCellViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView tripImageView;
        TextView tripName, tripDiscreption;

        public TripCellViewHolder(@NonNull View itemView) {
            super(itemView);

            tripImageView = (ImageView) itemView.findViewById(R.id.tripImageView);
            tripName = (TextView) itemView.findViewById(R.id.tripName);
            tripDiscreption = (TextView) itemView.findViewById(R.id.tripDiscreption);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            notifyItemChanged(selected_position);
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;
            selected_position = getAdapterPosition();
            notifyItemChanged(selected_position);
            System.out.println(selected_position);
            Log.e(MainActivity.TAG, "onClick: "+ selected_position );

            EditTripFragment editTripFragment = new EditTripFragment();
            editTripFragment.user = user;
            editTripFragment.trip = tripList.get(selected_position);
            createAFragment(editTripFragment);

            // Add the Edit Trip Fragment
        }
        private void createAFragment(Fragment blankFragment) {

            FragmentTransaction fragmentTransaction =  mFragment.getFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.main_frame, blankFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
}
