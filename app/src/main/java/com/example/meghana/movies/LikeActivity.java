package com.example.meghana.movies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by meghana on 9/5/16.
 */
public class LikeActivity extends AppCompatActivity {

    EditText postText ;
    DatabaseHelper db;
    String em;
    Button btlike;
    android.widget.ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postpage);
        db = new DatabaseHelper(this);

        list = (android.widget.ListView)findViewById(R.id.listPosts);

        em =  getIntent().getStringExtra("emailId");
        final Users objData = (Users) getIntent().getSerializableExtra("postsData");

        postText =   (EditText)findViewById(R.id.post);
        btlike =(Button)findViewById(R.id.likeBtn);

        ArrayList<Users> posts = db.getPosts();


        CustomAdapter adapter=new CustomAdapter(this, R.layout.single_post, posts);
        list.setAdapter(adapter);



    }

//       db.addLike();
    }




