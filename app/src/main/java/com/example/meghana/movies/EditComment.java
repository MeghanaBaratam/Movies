package com.example.meghana.movies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * Created by meghana on 12/5/16.
 */
public class EditComment extends AppCompatActivity {

    EditText comment;
    Users cmnt;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_comment);
        db= new DatabaseHelper(this);
        db.getWritableDatabase();

        comment = (EditText)findViewById(R.id.EditTextForName);

        cmnt = (Users) getIntent().getSerializableExtra("commentRow");
        comment.setText(cmnt.o_coment);


    }


    public void updateData(View view)
    {
        boolean isOk =   db.EditComment(cmnt.o_comentId, comment.getText().toString() );
        if (isOk)
        {
            setResult(RESULT_OK);
            finish();
        }
    }
}
