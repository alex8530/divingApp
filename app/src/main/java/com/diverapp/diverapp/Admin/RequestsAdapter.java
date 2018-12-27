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
import android.widget.TextView;

import com.diverapp.diverapp.MainActivity;
import com.diverapp.diverapp.R;

import java.util.List;

import Modle.Request;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.RequestsAdapterViewHolder> {

    List<Request> requests;
    Context mCntx;
//    User user;
    int selected_position = 0 ;
    Fragment mFragment;
    public RequestsAdapter(List<Request> requests, Context mCntx,Fragment mFragment) {
        this.requests = requests;
        this.mCntx = mCntx;
        this.mFragment = mFragment;
//        this.user = user;
    }

    @NonNull
    @Override
    public RequestsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(mCntx);

        View view = layoutInflater.inflate(R.layout.requests_cell,viewGroup,false);
        return new RequestsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestsAdapterViewHolder requestsAdapterViewHolder, int i) {
        Request request = requests.get(i);
        Log.e(MainActivity.TAG, "onBindViewHolder: "+ request.getTotal() );
        requestsAdapterViewHolder.requestTitle.setText(request.getName());
        requestsAdapterViewHolder.requestTotal.setText(String.valueOf(request.getTotal())+" SR");
        requestsAdapterViewHolder.requestTickets.setText(String.valueOf(request.getNumberOfTickets()));
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public class RequestsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView requestTitle,requestTickets,requestTotal;
        public RequestsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            requestTitle = (TextView) itemView.findViewById(R.id.requestTitle);
            requestTickets = (TextView) itemView.findViewById(R.id.requestTickets);
            requestTotal = (TextView) itemView.findViewById(R.id.requestTotal);
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

            System.out.println(requests.get(selected_position).getTickets().size());
//            createAFragment(editTripFragment);

            // Add the Edit Trip Fragment
        }
        private void createAFragment(Fragment blankFragment) {

            FragmentTransaction fragmentTransaction = mFragment.getFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.main_frame, blankFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
}
