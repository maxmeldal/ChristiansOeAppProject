package com.example.christiansoeappproject.ui.admin.restaurant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.christiansoeappproject.R;
import com.example.christiansoeappproject.model.Restaurant;

import java.util.List;

public class RestaurantAdapter extends BaseAdapter {


        private List<Restaurant> restaurants;
        private LayoutInflater layoutInflater;

        public RestaurantAdapter(Context context, List<Restaurant> restaurants) {
            this.restaurants = restaurants;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return restaurants.size();
        }

        @Override
        public Object getItem(int i) {
            return restaurants.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view==null){
                view = layoutInflater.inflate(R.layout.activityrow, null);
            }
            TextView titleTextView = view.findViewById(R.id.titleTextView);
            titleTextView.setText(restaurants.get(i).getName());

            TextView subtitleTextView = view.findViewById(R.id.subtitleTextView);
            subtitleTextView.setText(restaurants.get(i).getLatitude() + ", " + restaurants.get(i).getLongitude());

            return view;
        }
}
