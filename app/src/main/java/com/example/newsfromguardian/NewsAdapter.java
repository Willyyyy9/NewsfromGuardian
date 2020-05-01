package com.example.newsfromguardian;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<News> {
    private Context mContext;
    private int mResource;

    public NewsAdapter(Context context, int resource, ArrayList<News> articles) {
        super(context, resource, articles);
        this.mContext = context;
        this.mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String title = getItem(position).getTitle();
        String sectionName = getItem(position).getSectionName();
        String date = getItem(position).getDate();
        String time = getItem(position).getTime();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource,parent,false);
        TextView titleView = convertView.findViewById(R.id.title);
        TextView sectionNameView = convertView.findViewById(R.id.sectionName);
        TextView dateView = convertView.findViewById(R.id.date);
        TextView timeView = convertView.findViewById(R.id.time);
        titleView.setText(title);
        sectionNameView.setText(sectionName);
        dateView.setText(date);
        timeView.setText(time);
        return convertView;
    }
}
