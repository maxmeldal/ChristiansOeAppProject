package com.example.christiansoeappproject.ui.admin.attraction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.christiansoeappproject.R;
import com.example.christiansoeappproject.model.Attraction;

import java.util.List;

public class AttractionAdapter extends BaseAdapter {
    private List<Attraction> attractions;
    private LayoutInflater layoutInflater;

    public AttractionAdapter(Context context, List<Attraction> attractions) {
        this.attractions = attractions;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return attractions.size();
    }

    @Override
    public Object getItem(int i) {
        return attractions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view==null){
            view = layoutInflater.inflate(R.layout.activityrow, null);
        }
        TextView titleTextView = view.findViewById(R.id.titleTextView);
        titleTextView.setText(attractions.get(i).getName());

        TextView subtitleTextView = view.findViewById(R.id.subtitleTextView);
        if (attractions.get(i).getDescription().length()>14){
            subtitleTextView.setText(attractions.get(i).getDescription().substring(0,15) + "...");
        } else {
            subtitleTextView.setText(attractions.get(i).getDescription());
        }

        return view;
    }
}
