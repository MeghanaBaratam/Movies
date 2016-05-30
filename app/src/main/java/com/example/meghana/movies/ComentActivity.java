package com.example.meghana.movies;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by meghana on 9/5/16.
 */
public class ComentActivity extends AppCompatActivity {

    DatabaseHelper db;
    ListView list;
    EditText postCom;
    Users objPosts;
    Users obj;
    ArrayList<Users> data;
    Dialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment);
        db = new DatabaseHelper(this);
        db.getWritableDatabase();

        list = (ListView) findViewById(R.id.listComents);
        objPosts = (Users) getIntent().getSerializableExtra("postsDataForComents");


        final ArrayList<Users> coments = db.getComents(objPosts.o_postId);


        CustomAdapter2 adapter = new CustomAdapter2(this, R.layout.single_comment, coments);
        list.setAdapter(adapter);

        postCom = (EditText) findViewById(R.id.coment);


        if (getSupportActionBar() != null) {


            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        final CharSequence[] items = {Constants.UPDATE, Constants.DELETE};

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                obj = coments.get(position);
                Log.d("list data:","usId:"+obj.o_userId+"\ncomId:"+obj.o_comentId ) ;
                Log.d( "\ncomment:",obj.o_coment+"\nusName:"+obj.o_username);

                String uid =  db.getUidFromEmail(Post.LOG_EMAIL);
                Log.d("uid", uid);
                ArrayAdapter<String> modeAdapter;
                ListView modeList=null;
                if (obj.o_userId.equals(uid))
                {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(ComentActivity.this);

                    modeList = new ListView(ComentActivity.this);
                    String[] stringArray = new String[] { "Update", "Delete" };
                    modeAdapter = new ArrayAdapter<>(ComentActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, stringArray);
                    modeList.setAdapter(modeAdapter);

                    builder.setView(modeList);
                    dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.setTitle("You can  Udpate and Delete the Comments....");
                    dialog.setCancelable(true);
                    dialog.show();

                    modeList.setOnItemClickListener(new AdapterView.OnItemClickListener()
                    {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (position==0)
                            {
                                Toast.makeText(ComentActivity.this,"update",Toast.LENGTH_SHORT).show();

                                dialog.dismiss();
                                Intent intent = new Intent(ComentActivity.this, EditComment.class);
                                intent.putExtra("commentRow", obj);
                                startActivityForResult(intent,101);


                            }
                            else
                            {
                                Toast.makeText(ComentActivity.this,"Delete",Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                boolean isOk = db.delComment(obj.o_comentId);
                                if (isOk)
                                {
                                    DatabaseHelper myDb=new DatabaseHelper(ComentActivity.this);
                                    Log.d("postId:", objPosts.o_postId);
                                    data = myDb.getComents(objPosts.o_postId);
                                    CustomAdapter2 adapter = new CustomAdapter2(ComentActivity.this, R.layout.single_comment, data);
                                    list.setAdapter(adapter);

                                }
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(ComentActivity.this,"not editable", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }






    public void commentUpdate(){

        ArrayList<Users> coments = db.getComents(objPosts.o_postId);


        CustomAdapter2 adapter = new CustomAdapter2(this, R.layout.single_comment, coments);
        list.setAdapter(adapter);

    }



    public void postComment(View view) {
        String pcoment = postCom.getText().toString();

        if (pcoment.length() == 0) {
            postCom.setError("Enter some comment");
        } else {
            Log.d("userId", objPosts.o_userId);
            Log.d("e",Post.LOG_EMAIL+"");
            String userId = db.getUidFromEmail(Post.LOG_EMAIL);

            boolean isOk = db.addComment(userId, pcoment, objPosts.o_postId);
            if (isOk) {
                Toast.makeText(this, "comment added..", Toast.LENGTH_LONG).show();


                Intent intent = new Intent(this, Post.class);
                intent.putExtra("email_id", Post.LOG_EMAIL);
                startActivity(intent);
                setResult(RESULT_OK);

                commentUpdate();


                Log.d("finish", "postComment: ");
               finish();


            } else {
                Log.d("coment fail", "");
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 101) {

                DatabaseHelper myDb=new DatabaseHelper(this);
                Log.d("postId:", objPosts.o_postId);
                this.data = myDb.getComents(objPosts.o_postId);
                CustomAdapter2 adapter = new CustomAdapter2(this, R.layout.single_comment, this.data);
                list.setAdapter(adapter);



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
                Intent intent = new Intent(this,ComentActivity.class );
                startActivity(intent);
                break;


            case android.R.id.home:
                Intent intent3 = new Intent(this,Post.class);
                startActivity(intent3);
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    private static class Constants {
        public static final CharSequence UPDATE = "UPDATE";
        public static final CharSequence DELETE = "DELETE";
    }

}
