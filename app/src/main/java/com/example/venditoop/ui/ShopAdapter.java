package com.example.venditoop.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.venditoop.R;
import com.example.venditoop.data.model.data_stores.Product;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * @author  Tomer Amiel, Wilmayer Dure, Carlos Salazar
 * This Adapter class is responsible for setting up the layout of the Shopping page with a recycler
 * view that holds a list of products from the user's cart
 */
public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.Viewholder> {
    private final Context context;
    private final ArrayList<Product> productsList;
    private final RecylcerViewCLickListener listener;

    /**
     Constructs a ShopAdapter object to generate a recycler view with a list of products in the
     inventory
     @param context the current state of the application (the current screen that the user is on)
     @param productsList a list of Products
     @param listener a listener for the recycler view. whenever the user clicks on any element
     in an item card, the recycler view listener is used
     */
    public ShopAdapter(Context context, ArrayList<Product> productsList, RecylcerViewCLickListener listener) {
        this.context = context;
        this.productsList = productsList;
        this.listener = listener;
    }

    /**
     View holder class for intializing the views (recycler view)
     */
    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView itemName, itemDescription, itemPrice, itemQuantity;
        private final ImageView itemImage;

        /**
         Constructs a Viewholder object to intialize all elements in for a specific view
         @param itemView the current component being rendered
         */
        public Viewholder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.name);
            itemDescription = itemView.findViewById(R.id.description);
            itemPrice = itemView.findViewById(R.id.price);
            itemImage = itemView.findViewById(R.id.img);
            itemQuantity = itemView.findViewById(R.id.quantity);

            Button addButton = itemView.findViewById(R.id.add_item);
            addButton.setOnClickListener(this);
        }

        /**
         Method that assigns the current view component to the recycler view listener
         @param view the current view component that has a onClickListener
         */
        @Override
        public void onClick(View view) { listener.onClick(view, getAdapterPosition()); }
    }

    /**
     Interface that will assign a button view and the current position of a view to the recycler view
     */
    public interface RecylcerViewCLickListener {
        void onClick(View button, int position);
    }

    /**
     Class that inflates the layout for each item in the recycler vie
     */
    @NonNull
    @Override
    public ShopAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new Viewholder(view);
    }

    /**
     Method that is used for showing the number of card items in the recycler view.
     */
    @Override
    public int getItemCount() {
        try {
            return productsList.size();
        } catch (NullPointerException e) {
            return 0;
        }
    }

    /**
     Class that sets up the data to the designated elements of each card layout
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ShopAdapter.Viewholder holder, int position) {
        DecimalFormat form = new DecimalFormat("0.00");

        Product model = productsList.get(position);
        holder.itemName.setText(model.getName());
        holder.itemDescription.setText("" + model.getDescription());
        holder.itemPrice.setText("$" + form.format(model.getPrice()));
        holder.itemQuantity.setText("" + model.getQuantity());
        Glide.with(context).load(model.getImage()).into(holder.itemImage);
    }
}
