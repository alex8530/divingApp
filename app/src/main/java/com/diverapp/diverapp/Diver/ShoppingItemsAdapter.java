package com.diverapp.diverapp.Diver;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.diverapp.diverapp.MainActivity;
import com.diverapp.diverapp.R;
import com.diverapp.diverapp.Trip;
import com.diverapp.diverapp.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ShoppingItemsAdapter extends RecyclerView.Adapter<ShoppingItemsAdapter.ShoppingItemsViewHolder> {


    List<Trip> tripList;
    Context mCtx;
    int selected_position = 0;
    Fragment mFragment;
    List<User> users;
    MainActivity mainActivity;
    public ShoppingItemsAdapter(List<Trip> tripList, Context mCtx,Fragment mFragment,List<User> users,MainActivity mainActivity) {
        this.tripList = tripList;
        this.mCtx = mCtx;
        this.mFragment = mFragment;
        this.users = users;
        this.mainActivity = mainActivity;
    }
    @NonNull
    @Override
    public ShoppingItemsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        System.out.println("ShoppingItemsViewHolder");
        LayoutInflater layoutInflater = LayoutInflater.from(mCtx);
        View view = layoutInflater.inflate(R.layout.shopping_items_cell,viewGroup,false);
        return new ShoppingItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingItemsViewHolder shoppingItemsViewHolder, int i) {
        Trip trip = tripList.get(i);
        shoppingItemsViewHolder.nameText.setText( trip.getName());
        shoppingItemsViewHolder.descrption.setText(trip.getLocationName());
        SimpleDateFormat formatter = new SimpleDateFormat("EEE dd/MM");
        String dateString=null;
try {
     dateString = formatter.format(new Date(trip.getSartDate()));
}catch (Exception exc){}
        String enddate=null;
        try {
            enddate = formatter.format(new Date(trip.getEndDate()));
        }catch (Exception exc){}
//        String endDate = formatter.format(new Date(trip.getEndDate()));
//        shoppingItemsViewHolder.start.setText(dateString);
        double priceWithVat = trip.getPrice() + ((trip.getPrice() *5 ) / 100);
        shoppingItemsViewHolder.price.setText(String.valueOf(priceWithVat)+" RS");
//        shoppingItemsViewHolder.end.setText(enddate);
        shoppingItemsViewHolder.descrption.setText(trip.getDescription());
        shoppingItemsViewHolder.location.setText(trip.getLocationName());

        shoppingItemsViewHolder.tickets.setText(String.valueOf(trip.getTicketNumber()));
        if (trip.getImagesURL() != null ){
            Glide.with(mCtx).load(trip.getImagesURL().get(0)).into(shoppingItemsViewHolder.imageView);
            //Toast.makeText(mCtx,trip.getImagesURL().get(0),Toast.LENGTH_LONG).show();
        }

    }
    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public class ShoppingItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nameText,descrption,start,end,tickets,price,location;
        ImageView imageView;

        public ShoppingItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            nameText = (TextView) itemView.findViewById(R.id.tripNameTV);
            descrption = (TextView) itemView.findViewById(R.id.description);
//            start = (TextView) itemView.findViewById(R.id.startFrom);
//            end = (TextView) itemView.findViewById(R.id.endIn);
            imageView = (ImageView) itemView.findViewById(R.id.tripImageView);
            tickets = (TextView) itemView.findViewById(R.id.ticketsTV);
            price = (TextView) itemView.findViewById(R.id.price);
            location = (TextView) itemView.findViewById(R.id.locationName);

        }
        @Override
        public void onClick(View v) {
            // Below line is just like a safety check, because sometimes holder could be null,
            // in that case, getAdapterPosition() will return RecyclerView.NO_POSITION
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;

            // Updating old as well as new positions
            notifyItemChanged(selected_position);
            selected_position = getAdapterPosition();
            notifyItemChanged(selected_position);
            System.out.println(selected_position);

            ItemFragment itemFragment = new ItemFragment(tripList.get(selected_position), mainActivity);
            for (int i=0;i<users.size();i++){
                User provider = users.get(i);
                if (tripList.get(selected_position).getProviderId().equals(provider.getUid())){
                    itemFragment.user = provider;
                }
            }
            createAFragment(itemFragment);
            // Do your another stuff for your onClick
        }
    }
    private void createAFragment(Fragment blankFragment) {

        FragmentTransaction fragmentTransaction =  mFragment.getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_frame, blankFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
