package com.example.meghana.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper db ;

    EditText email,password;
    Button logInBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        db=new DatabaseHelper(this);
        db.getWritableDatabase();

        email    = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        logInBtn = (Button)findViewById(R.id.LogIn);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2)
        {
            Toast.makeText(this, "Login now..", Toast.LENGTH_LONG).show();
        }
    }

    /** Method For Validating  the Email
     and  Password **/

    public void loginValidate(View view)
    {
        Log.d("Entered", "LoginValidate method");
        String a = email.getText().toString();
        String b = password.getText().toString();
        Log.d("a:", ""+a);
        Log.d("b:", ""+b);
        if(a.length() == 0)
        {
            email.setError("plz enter email");
            return;
        }
        if(b.length() == 0)
        {
            password.setError("plZ enter password");
            return;
        }
        if(!isValidEmail(a))
        {
            email.setError("Enter valid email");
            return;
        }

        if(a.length() != 0 && b.length() != 0)
        {
            Log.d("Entered", "LoginValidate method if cond...");

            boolean getValidated = db.authenticate(a, b);

            if (getValidated)
            {

                Log.d("Entered", "LoginValidate get validated ");
                Toast.makeText(this, "Welcome to post", Toast.LENGTH_LONG).show();




                Intent intent = new Intent(this,Post.class );
                intent.putExtra("emailId", a);
                Log.d("email", a);
                startActivity(intent);
                email.setText("");
                password.setText("");
            }
            else {
                email.setText("");
                password.setText("");
                Toast.makeText(this, " Invalid user details", Toast.LENGTH_LONG).show();
            }
        }

    }

    public boolean isValidEmail(String email)
    {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    public void OpenSignUpForm(View view)
    {
        Intent intent = new Intent(this, SignUpForm.class);
        startActivity(intent);
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


//            case android.R.id.home:finish();


        }
        return super.onOptionsItemSelected(item);
    }


}
