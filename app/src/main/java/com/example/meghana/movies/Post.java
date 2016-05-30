package com.example.meghana.movies;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by meghana on 6/5/16.
 */
public class Post extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;
    EditText postText ;
    DatabaseHelper db;
    String em;
    ListView list;
    byte[] byteArray;
    public  static String LOG_EMAIL;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postpage);

        db=new DatabaseHelper(this);
        db.getWritableDatabase();



        Button buttonLoadImage = (Button) findViewById(R.id.selectImage);
        assert buttonLoadImage != null;
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });



     if(getSupportActionBar()!= null) {


         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
     }

        list = (ListView)findViewById(R.id.listPosts);

        em =  getIntent().getStringExtra("emailId");
        if (em==null)
        {
            em =  getIntent().getStringExtra("email_id");
        }
        LOG_EMAIL=em;

        postText =   (EditText)findViewById(R.id.post);


        ArrayList<Users> posts = db.getPosts();


        CustomAdapter adapter=new CustomAdapter(this, R.layout.single_post, posts);
        list.setAdapter(adapter);



    }

    public void listviewUpdate(){

        ArrayList<Users> posts = db.getPosts();


        CustomAdapter adapter=new CustomAdapter(this, R.layout.single_post, posts);
        list.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Log.d("image", "onActivityResult: ");

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.image);
            assert imageView != null;

            Bitmap b= BitmapFactory.decodeFile(picturePath);
            Log.d( "onActivityResult: ", String.valueOf(b));

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byteArray = stream.toByteArray();

            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            imageView.setImageDrawable(null);
        }
        else if (requestCode == 101 && resultCode == RESULT_OK){
            Log.d("post", "onActivityResult: ");
            listviewUpdate();
            setResult(RESULT_OK);


        }

    }



    public void postData(View view)
    {
        String p =  postText.getText().toString();

        if(p.length() ==0)
        {
            postText.setError("post must not be empty");
        }
        else {
            boolean isOk = db.addPost(em, p, byteArray);
            if (isOk) {
                Toast.makeText(this, "post added..", Toast.LENGTH_LONG).show();

                postText.setText("");


                ArrayList<Users> posts = db.getPosts();


                CustomAdapter adapter = new CustomAdapter(this, R.layout.single_post, posts);
                list.setAdapter(adapter);
                setResult(RESULT_OK);




            } else {
                Toast.makeText(this, "post not added..", Toast.LENGTH_LONG).show();
            }
        }


    }








    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Integer id = item.getItemId();
        switch (id)
        {
            case R.id.Refresh:
                Toast.makeText(this, "Post", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this,Post.class );
                startActivity(intent);
                break;

            case R.id.likes:
                Toast.makeText(this, "Likes", Toast.LENGTH_LONG).show();
                Intent intent1 = new Intent(this,LikeActivity.class);
                startActivity(intent1);
                break;

            case R.id.post:
                Toast.makeText(this, "Comments", Toast.LENGTH_LONG).show();
                Intent intent2 = new Intent(this,ComentActivity.class);
                startActivity(intent2);
                break;


            case android.R.id.home:
                Intent intent3 = new Intent(this,MainActivity.class);
                startActivity(intent3);
                break;

        }
        return super.onOptionsItemSelected(item);
    }





}





