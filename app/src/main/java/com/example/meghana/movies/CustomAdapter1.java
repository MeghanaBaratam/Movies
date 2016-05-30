package com.example.meghana.movies;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by meghana on 9/5/16.
 */
public class CustomAdapter1 extends ArrayAdapter<Users> {

    Context context;
    List<Users> data;
    int id;
    public CustomAdapter1(Context context, int resource, List<Users> objects) {
        super(context, resource, objects);
        this.context = context;
        data = objects;
        id = resource;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater =((Activity)context).getLayoutInflater();
        convertView= inflater.inflate(id, parent, false);
        TextView t1= (TextView) convertView.findViewById(R.id.textView1);
//        Button edit = (Button) convertView.findViewById(R.id.edit);
        TextView t2= (TextView) convertView.findViewById(R.id.textView2);

        Users s= data.get(position);
        t1.setText("Name:"+ s.o_username);

        t2.setText(s.o_email);
        return convertView;
    }


}
