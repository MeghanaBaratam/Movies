package com.example.meghana.movies;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by meghana on 9/5/16.
 */
public class ListView extends AppCompatActivity {


    android.widget.ListView list1;
    ArrayList<Users> data;
    Bundle b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);

        list1 = (android.widget.ListView) findViewById(R.id.list);

        TextView listHdr = new TextView(this);
        listHdr.setText("Tap to Update..");
         listHdr.setTextColor(Color.BLUE);

        listHdr.setBackgroundColor(Color.WHITE);
        list1.addHeaderView(listHdr);


        b = getIntent().getBundleExtra("key");

        data = (ArrayList<Users>) b.getSerializable("Users");

        CustomAdapter1 adapter = new CustomAdapter1(this, R.layout.listitem, data);
        list1.setAdapter(adapter);


        if(getSupportActionBar()!= null) {


            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            if(requestCode==101)
            {
                DatabaseHelper myDb=new DatabaseHelper(this);
                this.data = myDb.GetData();
                CustomAdapter1 adapter=new CustomAdapter1(this, R.layout.listitem, this.data);
                list1.setAdapter(adapter);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Integer id = item.getItemId();
        switch (id)
        {


            case android.R.id.home:
                Intent intent3 = new Intent(this,SignUpForm.class);
                startActivity(intent3);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
