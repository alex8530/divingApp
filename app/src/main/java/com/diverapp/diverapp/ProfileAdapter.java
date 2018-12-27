package com.diverapp.diverapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mCtx;
    //    private List<Report> reports;
//    private User user;
    List<Trip> trips;
    public ProfileAdapter(Context mCtx,List<Trip> trips ) {
        this.mCtx = mCtx;
        this.trips = trips;
    }

//    @Override
//    public int getItemViewType(int position) {
//        // Just as an example, return 0 or 2 depending on position
//        // Note that unlike in ListView adapters, types don't have to be contiguous
////        Log.e(TAG, "getItemViewType: "+ position );
////        return position % 2 * 2;
//    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(mCtx);
        View view = layoutInflater.inflate(R.layout.profile_cell_1,viewGroup,false);
//        Log.e(TAG, "onCreateViewHolder: "+ i );
//        switch (i){
//            case 0:
                return new ProfilePlaceHoleder(view);
//            case 1:
//                return new UserTrips(view);
//                default:
//                    return null;
//        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

        ProfilePlaceHoleder profilePlaceHoleder = (ProfilePlaceHoleder)holder;
        Trip trip = trips.get(i);
        profilePlaceHoleder.userNameLabel.setText(trip.getName());
        profilePlaceHoleder.titleLabel.setText(trip.getDescription());
        if (trip.getImagesURL() != null){
            Glide.with(mCtx).load(trip.getImagesURL().get(0)).into(profilePlaceHoleder.profileImage);
        }
//        Log.e(TAG, "onBindViewHolder: "+ user.requests.size());
//        if ( != null && user.getRating() != null){
//            profilePlaceHoleder.ratingTV.setText("");
//            profilePlaceHoleder.divingID.setText(user.getDivingId());
//        }

//                break;
//            case 2:
//                UserTrips userProfileInto = (UserTrips) holder;
////                Log.e(TAG, "onBindViewHolder: "+ user.requests.get(i).getName());
////                userProfileInto.userTripName.setText(user.requests.get(i).getName());
//                break;
//        }

    }
    @Override
    public int getItemCount() {
        return trips.size() ;
    }

    class ProfilePlaceHoleder extends RecyclerView.ViewHolder{

        TextView userNameLabel,titleLabel,ratingTV,divingID;
        ImageView profileImage;

        public ProfilePlaceHoleder(@NonNull View itemView) {
            super(itemView);
            LinearLayout relativeLayout = (LinearLayout) itemView.findViewById(R.id.reletiveLayout0);
            userNameLabel = (TextView) itemView.findViewById(R.id.profile_userName_);
            titleLabel = (TextView) itemView.findViewById(R.id.profile_user_titleTV_);
            profileImage = (ImageView) itemView.findViewById(R.id.user_pfroile_imageView0);
//            titleLabel.setText(user.getUserClass());
//            Glide.with(mCtx).load(user.getImageUrl()).into(profileImage);
        }
    }
    public class UserTrips extends RecyclerView.ViewHolder {
        TextView userTripName;
        public UserTrips(@NonNull View itemView) {
            super(itemView);
            LinearLayout relativeLayout = (LinearLayout) itemView.findViewById(R.id.userTripRelativeLayout);
            userTripName = (TextView) relativeLayout.findViewById(R.id.userTripName);
        }
    }
}
