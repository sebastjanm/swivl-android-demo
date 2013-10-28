package com.mighter.videorecorder.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.mighter.videorecorder.R;
import com.mighter.videorecorder.data.Video;

import java.util.List;

public class VideosListAdapter extends ArrayAdapter<Video> {

    public VideosListAdapter(Context context, List<Video> videos) {
        super(context, R.layout.video_item, videos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = newView(getContext());
        TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setText(getItem(position).getName());
        return convertView;
    }

    public View newView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_item, null);
        return view;
    }
}