package com.example.meghana.movies;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by meghana on 9/5/16.
 *
 */
public class CustomAdapter2 extends ArrayAdapter<Users>{


    Context context;
    List<Users> data;
    int id;

    public CustomAdapter2(Context context, int resource, List<Users> objects) {
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
        final LayoutInflater inflater =((Activity)context).getLayoutInflater();
        convertView= inflater.inflate(id, parent, false);
        TextView t1= (TextView) convertView.findViewById(R.id.v_c_fromvalue);
        TextView t2= (TextView) convertView.findViewById(R.id.v_c_comentValue);
//        TextView t3= (TextView) convertView.findViewById(R.id.v_c_comentValue1);

        final Users s= data.get(position);
        t1.setText(s.o_username);
      Log.d("name", s.o_username);
        t2.setText(s.o_coment);
        Log.d("name", s.o_coment);
//        t3.setText(s.o_commentcount);
//        Log.d("count",s.o_commentcount);



        return convertView;
    }
}
