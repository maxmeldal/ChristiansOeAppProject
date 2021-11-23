package com.example.christiansoeappproject.ui.admin.facility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.christiansoeappproject.R;
import com.example.christiansoeappproject.model.Facility;

import java.util.List;

public class FacilityAdapter extends BaseAdapter {
    private List<Facility> facilities;
    private LayoutInflater layoutInflater;

    public FacilityAdapter(Context context, List<Facility> facilities) {
        this.facilities = facilities;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return facilities.size();
    }

    @Override
    public Object getItem(int i) {
        return facilities.get(i);
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
        titleTextView.setText(facilities.get(i).getName());

        TextView subtitleTextView = view.findViewById(R.id.subtitleTextView);
        subtitleTextView.setText(facilities.get(i).getLatitude() + ", " + facilities.get(i).getLongitude());

        return view;
    }
}
