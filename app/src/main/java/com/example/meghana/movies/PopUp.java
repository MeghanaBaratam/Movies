package com.example.meghana.movies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by meghana on 13/5/16.
 */
public class PopUp extends AppCompatActivity {

    ImageView img;
    DatabaseHelper db;
    Users data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup);
        db = new DatabaseHelper(this);

        Log.d("Enter", "popUpActivity");

        img = (ImageView)findViewById(R.id.pic);
        TextView t1= (TextView)findViewById(R.id.v_fromvalue);
        Button coment_Btn =(Button)findViewById(R.id.commentBtn);

        data = (Users) getIntent().getSerializableExtra("items");
        Log.d("after", "data");

        Bitmap bmp = convertImageArrayToImage(data.o_bytearray);


        img.setImageBitmap(bmp);
        assert t1 != null;
        t1.setText(data.o_username);

        assert coment_Btn != null;
        coment_Btn.setText("COMMENTS"+"("+data.o_commentcount+")");


        coment_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PopUp.this, ComentActivity.class);
                intent.putExtra("postsDataForComents", data);
                startActivity(intent);


            }
        });


    }


    public  Bitmap convertImageArrayToImage(byte[] byteArray) {

        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return bmp;

    }

    public void downloadBtn(View view)
    {
        final ProgressDialog dial = new ProgressDialog(this);
        dial.setTitle("please wait..");
        dial.setMessage("Downloading....");
        dial.show();

    }

}
