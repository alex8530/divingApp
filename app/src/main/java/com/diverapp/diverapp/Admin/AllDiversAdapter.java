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
import com.diverapp.diverapp.User;

import java.util.List;

public class AllDiversAdapter extends RecyclerView.Adapter<AllDiversAdapter.AllDiversViewHolder> {

    Fragment mFragment;
    Context mcontex;
    List<User> users;
    int selected_position = 0;

    public AllDiversAdapter(Context mcontex, List<User> users,Fragment mFragment) {
        this.mcontex = mcontex;
        this.users = users;
        this.mFragment = mFragment;
    }
    @NonNull
    @Override
    public AllDiversViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(mcontex);

        View view = layoutInflater.inflate(R.layout.all_diviers_cell,viewGroup,false);
        return new AllDiversViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllDiversViewHolder allDiversViewHolder, int i) {
        User user = users.get(i);
                    GlideApp.with(mcontex).load(user.getImageUrl()).placeholder(R.drawable.ic_assetlogo)
                    .centerCrop().into(allDiversViewHolder.divierImageView);
        allDiversViewHolder.divierName.setText(user.getName());
        allDiversViewHolder.divierId.setText(String.valueOf(user.getUserClass()));
    }
    @Override
    public int getItemCount() {
        return users.size();
    }

    public class AllDiversViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView divierImageView;
        TextView divierName,divierId;
        public AllDiversViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            divierImageView = (ImageView) itemView.findViewById(R.id.divierImage);
            divierName = (TextView) itemView.findViewById(R.id.divierName);
            divierId = (TextView) itemView.findViewById(R.id.divierId);
        }

        @Override
        public void onClick(View v) {

            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;

            notifyItemChanged(selected_position);

            selected_position = getAdapterPosition();
            notifyItemChanged(selected_position);
            System.out.println(selected_position);
            UserAdminProfileFragment profileFragment = new UserAdminProfileFragment();
            profileFragment.user = users.get(selected_position);
            createAFragment(profileFragment);
            }
    }
    private void createAFragment(Fragment blankFragment) {

        FragmentTransaction fragmentTransaction =  mFragment.getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_frame, blankFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
