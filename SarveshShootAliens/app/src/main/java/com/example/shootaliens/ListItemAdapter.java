package com.example.shootaliens;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListItemAdapter extends ArrayAdapter<ListItem> {

    //private static final String LOG_TAG = ListItemAdapter.class.getSimpleName();

    public ListItemAdapter(Activity context, ArrayList<ListItem> listItems) {
        super(context, 0, listItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_row, parent, false);
        }

        ListItem currentListItem =getItem(position);

        TextView nameTextView = (TextView) listItemView.findViewById(R.id.tv_player_name);
        nameTextView.setText(currentListItem.getName());

        TextView scoreTextView = (TextView) listItemView.findViewById(R.id.tv_player_score);
        scoreTextView.setText(currentListItem.getScore());

        return listItemView;
    }
}
