package com.example.meghana.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;

/**
 * Created by meghana on 6/5/16.
 */
public class SignUpForm  extends AppCompatActivity {


    DatabaseHelper db;
    EditText   USERNAME, EMAIL, MOBILENUMBER,PASSWORD;
    Button SIGNUPBTN, RESETBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);


      db=new DatabaseHelper(this);
        db.getWritableDatabase();

        USERNAME = (EditText) findViewById(R.id.userName);
        EMAIL = (EditText) findViewById(R.id.email);
        PASSWORD = (EditText) findViewById(R.id.password);
        MOBILENUMBER = (EditText) findViewById(R.id.mobileNo);

        SIGNUPBTN = (Button) findViewById(R.id.SignUp);
        RESETBTN = (Button) findViewById(R.id.reset);


        if(getSupportActionBar()!= null) {


            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void resetForm(View view)
    {
        USERNAME.setText("");
        EMAIL.setText("");
        PASSWORD.setText("");
        MOBILENUMBER.setText("");
    }

    /**
     * Defined method for SignUpAccount for user
     * @param view clicked view */

    public void SignUpAccount(View view) {
        String EMAILPATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        String UN = USERNAME.getText().toString();
        String E = EMAIL.getText().toString();
        String P = PASSWORD.getText().toString();
        String M = MOBILENUMBER.getText().toString();
        boolean isOk = true;


        if (UN.length() == 0) {
            USERNAME.setError("plz fill the name");
            isOk = false;
        } else if (E.length() == 0) {
            EMAIL.setError("plz enter Email ");
            isOk = false;
        } else if (P.length() == 0) {

            PASSWORD.setError("please enter password");
            isOk = false;
        } else if (!E.trim().matches(EMAILPATTERN)) {
            Log.d("email", "SignUpAccount: ");
            EMAIL.setError("Invalid Email");
            isOk = false;

        }

        if (M.length() == 0) {
            MOBILENUMBER.setError("plz Enter Mobile Number");
            return;
        }
        if (M.length() != 10) {

            MOBILENUMBER.setError("Enter valid 10digit Mobile Number");
            return;
        }
        if (M.length() == 10) {
            boolean num = isValidNumber(M);
            if (!num) {
                MOBILENUMBER.setError("Characters are present");
                return;
            }

        }
        if (isOk) {
            boolean isInserted = db.insertData(UN,E,P,M);
            USERNAME.setText("");
            EMAIL.setText("");
            MOBILENUMBER.setText("");
            PASSWORD.setText("");

            if (isInserted) {

                Toast.makeText(this, "Data is inserted", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this,"Data is not inserted",Toast.LENGTH_LONG).show();

            }
        }

    }

    public boolean isValidNumber(String m) {

        return android.util.Patterns.PHONE.matcher(m).matches();
    }



    public void displayUsers(View view)
    {
        Log.d("disp:::", "dispUsers");
        ArrayList<Users> res = db.GetData();

        Intent intent = new Intent(this, ListView.class);

        Bundle information = new Bundle();
        information.putSerializable("Users", res);
        intent.putExtra("key", information);
        startActivity(intent);

        Toast.makeText(getApplicationContext(), "Display All users", Toast.LENGTH_LONG).show();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Integer id = item.getItemId();
        switch (id)
        {
            case R.id.Refresh:
                Toast.makeText(this, "SignUp", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this,SignUpForm.class);
                startActivity(intent);
                break;


            case android.R.id.home:
                Toast.makeText(this, "Login", Toast.LENGTH_LONG).show();
                Intent intent1 = new Intent(this,MainActivity.class);
                startActivity(intent1);
                break;



        }
        return super.onOptionsItemSelected(item);
    }



}
