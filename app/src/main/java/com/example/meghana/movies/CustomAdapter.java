package com.example.meghana.movies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by meghana on 6/5/16.
 *
 */
public class CustomAdapter extends ArrayAdapter<Users>{


    Context context;
    List<Users> data;
    int id;
    DatabaseHelper db;
    public CustomAdapter(Context context, int resource, List<Users> objects) {
        super(context, resource, objects);
        this.context = context;
        data = objects;
        id = resource;

        db=new DatabaseHelper(context);

    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater =((Activity)context).getLayoutInflater();
        convertView= inflater.inflate(id, parent, false);

        TextView t1= (TextView) convertView.findViewById(R.id.v_fromvalue);
        TextView t2= (TextView) convertView.findViewById(R.id.v_postvalue);
        final TextView t3 = (TextView) convertView.findViewById(R.id.v_likecount);

        ImageView I= (ImageView) convertView.findViewById(R.id.grid_image);

        final Button like_Btn =(Button) convertView.findViewById(R.id.likeBtn);
        Button coment_Btn =(Button) convertView.findViewById(R.id.commentBtn);
        Button delete_Btn = (Button) convertView.findViewById(R.id.deletepost);




        final Users s= data.get(position);
        t1.setText(s.o_username);
        t2.setText(s.o_post);
        t3.setText(s.o_likecount+"Likes");


       coment_Btn.setText("COMMENTS"+"("+s.o_commentcount+")");



        Bitmap bmp = convertImageArrayToImage(s.o_bytearray);

        if (s.o_bytearray != null) {
            I.setImageBitmap(bmp);
            I.setScaleY((float) 1.05);
            I.setScaleX((float) 1.75);
        }

        Log.d("u_id0", s.o_userId);

        if (s.o_status.equalsIgnoreCase("0")){

            like_Btn.setText("UNLIKE");

        }

        else
        {

            like_Btn.setText("LIKE");
        }


            like_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d("u_id1", s.o_userId);
                    Log.d("p_id", s.o_postId);

                    db.addLike(s.o_userId, s.o_postId,like_Btn.getText(),t3);

                    if (s.o_status.equalsIgnoreCase("0")){

                        like_Btn.setText("LIKE");
                        s.o_status="1";

                        Log.d("p0:", s.o_postId);


                    }

                    else
                    {
                        s.o_status="0";
                        like_Btn.setText("UNLIKE");
                        Log.d("p1:", s.o_postId);


                    }



                }
            });

        coment_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ComentActivity.class);
                intent.putExtra("postsDataForComents", s);
                ((Activity) context).startActivityForResult(intent, 101);


            }
        });


        I.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.d("popup", "after");

                Intent intent = new Intent(context, PopUp.class);
                intent.putExtra("items", s);
                context.startActivity(intent);
            }
        });



        delete_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.d("u_id1", s.o_userId);
                Log.d("p_id", s.o_postId);

                db.delPost(s.o_postId);
                Intent intent = new Intent(context,Post.class);
                intent.putExtra("Deletepost",s);
                context.startActivity(intent);
            }
        });


        return convertView;
    }

    public  Bitmap convertImageArrayToImage(byte[] byteArray) {

        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return bmp;

    }


    }




