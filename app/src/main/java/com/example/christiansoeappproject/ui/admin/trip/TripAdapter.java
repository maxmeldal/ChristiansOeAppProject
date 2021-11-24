package com.example.christiansoeappproject.ui.admin.trip;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.christiansoeappproject.R;
import com.example.christiansoeappproject.model.Theme;
import com.example.christiansoeappproject.model.Trip;

import java.util.List;

public class TripAdapter extends BaseAdapter {
    private List<Trip> trips;
    private LayoutInflater layoutInflater;

    public TripAdapter(Context context, List<Trip> trips) {
        this.trips = trips;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return trips.size();
    }

    @Override
    public Object getItem(int i) {
        return trips.get(i);
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
        titleTextView.setText(trips.get(i).getName());
        TextView subtitleTextView = view.findViewById(R.id.subtitleTextView);
        subtitleTextView.setText(trips.get(i).getInfo()+ ", Theme: " + convertToTheme(trips.get(i).getTheme()));

        return view;
    }

    public String convertToTheme(int i){
        if(i == 1){ return Theme.Nature.name();
        } else if(i == 2){ return Theme.History.name();
        } else if(i == 3){ return Theme.War.name();
        }
        return Theme.None.name();
    }
}
