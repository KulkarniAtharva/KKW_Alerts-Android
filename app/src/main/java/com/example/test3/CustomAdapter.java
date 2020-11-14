package com.example.test3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;

import java.util.ArrayList;
import java.util.Collections;

class CustomAdapter extends ArrayAdapter<String>
{
        private final Context context;
        private final String title;
	    private final String given_data;

        	    //the constructor gets the values to be shown on the listview
        	    public CustomAdapter(Context context, String t, String g)
                {
        	        super( context, R.layout.list_item,R.id.cardView);
        	        this.context = context;
        	        this.title = t;
        	        this.given_data = g;
        	    }

        	    //getview method inflates the values given from the mainactivity on the custom design layout for listview and returns the layout with inflated values in it
        	    @Override
	    public View getView(int position, View view, ViewGroup parent)
                {
        	        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	        View rowView= inflater.inflate(R.layout.list_item, null, false);

        	        //initialize the textview and imageview we declared in the custom_list.xml file

        	        TextView t = (TextView) rowView.findViewById(R.id.title);
                    TextView g = (TextView) rowView.findViewById(R.id.given_date);

					Toast.makeText(context, title+""+given_data, Toast.LENGTH_SHORT).show();

        	        t.setText(title);
                    g.setText(given_data);

        	        return rowView;
        	    }
}