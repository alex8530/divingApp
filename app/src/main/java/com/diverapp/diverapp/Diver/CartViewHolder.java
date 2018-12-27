package com.diverapp.diverapp.Diver;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.diverapp.diverapp.R;

import java.util.ArrayList;
import java.util.List;

import Modle.Order;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView price,name;
    public ImageView imageCartCount;
    private AdapterView.OnItemClickListener onItemClickListener;


    public void setPrice(TextView price) {
        this.price = price;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public CartViewHolder(View itemView){
        super(itemView);

        name = (TextView) itemView.findViewById(R.id.cardViewItemId);
        price = (TextView) itemView.findViewById(R.id.cartItemPrice);
        imageCartCount= (ImageView) itemView.findViewById(R.id.cartItemCount);
    }
    @Override
    public void onClick(View v) {

    }
    public static class CartViewHolderAdapter extends RecyclerView.Adapter<CartViewHolder> {
        private List<Order>  orderList = new ArrayList<>();
        private Context context;

        public CartViewHolderAdapter(List<Order> orderList, Context context) {
            this.orderList = orderList;
            this.context = context;
        }

        @NonNull
        @Override
        public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.summary_items,viewGroup,false);
            return new CartViewHolder(itemView);
        }

        int total = 0 ;
        @Override
        public void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i) {
//            Log.d(TAG, "onBindViewHolder: "+ orderList.get(i).getQuantity());
//            Log.d(TAG, "onBindViewHolder: "+ orderList.get(i).getProdictName());
//            Log.d(TAG, "onBindViewHolder: "+ orderList.get(i).getQuantity());
//            Log.d(TAG, "onBindViewHolder: "+ orderList.get(i).getProviderID());
//            Log.d(TAG, "onBindViewHolder: "+ orderList.get(i).getPrice());

            TextDrawable textDrawable = TextDrawable.builder().buildRound(""+String.valueOf(orderList.get(i).getQuantity()), Color.RED);
            cartViewHolder.imageCartCount.setImageDrawable(textDrawable);
            total += orderList.get(i).getPrice() * orderList.get(i).getQuantity();
            cartViewHolder.name.setText(orderList.get(i).getProdictName());

            cartViewHolder.price.setText(String.valueOf(orderList.get(i).getPrice()));
//            Log.d(MainActivity.TAG, "onBindViewHolder: "+ total);
        }

        @Override
        public int getItemCount() {
            return orderList.size();
        }
    }
}
