package com.example.meghana.movies;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by meghana on 6/5/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    SQLiteDatabase sdb;
    Activity activity;

    public static final String DATABASE_NAME = "RegDb.db";
    public static final String TABLE_NAME = "Reg_table";
    public static final String TABLE_NAME1 = "posts_table";
    public static final String TABLE_NAME2 = "coments_table";
    public static final String TABLE_NAME3 = "likes_table";

    public static final String COL_1 = "USER_ID";
    public static final String COL_2 = "USER_NAME";
    public static final String COL_3 = "EMAIL";
    public static final String COL_4 = "PASSWORD";
    public static final String COL_5 = "POST_ID";
    public static final String COL_6 = "POST";

    public static final String COL_8 = "COMMENT_ID";
    public static final String COL_9 = "COMMENT";

    public static final String COL_7 = "MOBILE";
    public static final String COL_10 = "IMAGE";
    public static final String COL_11 = "LIKE_ID";
    public static final String COL_12 = "STATUS";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        activity= (Activity) context;

    }

    public void onCreate(SQLiteDatabase db) {

        Log.d("create table", "table1");
        db.execSQL("create table " + TABLE_NAME + "(USER_ID INTEGER PRIMARY KEY AUTOINCREMENT, USER_NAME VARCHAR, EMAIL VARCHAR,MOBILE VARCHAR, PASSWORD VARCHAR)");

        Log.d("create table", "table2");
        db.execSQL("create table " + TABLE_NAME1 + "(POST_ID INTEGER PRIMARY KEY AUTOINCREMENT, POST VARCHAR, USER_ID INTEGER,IMAGE BLOB," +
                "FOREIGN KEY(USER_ID)REFERENCES Reg_table(USER_ID) )");

        Log.d("create table", "table3");
        db.execSQL("create table " + TABLE_NAME2 + "(COMMENT_ID INTEGER PRIMARY KEY AUTOINCREMENT, COMMENT VARCHAR, USER_ID INTEGER,POST_ID INTEGER, " +
                "FOREIGN KEY(USER_ID)REFERENCES Reg_table(USER_ID), FOREIGN KEY(POST_ID)REFERENCES posts_table(POST_ID) )");

        Log.d("create table", "table4");

        db.execSQL("create table " + TABLE_NAME3 + "(LIKE_ID INTEGER PRIMARY KEY AUTOINCREMENT,USER_ID INTEGER,POST_ID INTEGER,STATUS VARCHAR," +
                "FOREIGN KEY(USER_ID)REFERENCES Reg_table(USER_ID), FOREIGN KEY(POST_ID)REFERENCES posts_table(POST_ID) )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME1);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME2);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME3);
        onCreate(db);
    }

    /**
     * @return database object for performing read or write operations
     */
    public void openDb() {
        sdb = this.getWritableDatabase();
    }

    public void closeDb() {
        sdb.close();
    }


    public boolean insertData(String username, String email, String password, String mobile) {
        openDb();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, username);
        contentValues.put(COL_3, email);
        contentValues.put(COL_4, password);
        contentValues.put(COL_7, mobile);
        long result = sdb.insert(TABLE_NAME, null, contentValues);
        closeDb();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public ArrayList<Users> GetData() {
        ArrayList<Users> listUsers = new ArrayList<>();
        openDb();
        Cursor cursor = sdb.query(true, TABLE_NAME, new String[]{COL_2, COL_3}, null, null, null, null, null, null);
        if (cursor != null) {
            for (; cursor.moveToNext(); ) {
                Users user = new Users();
                user.o_username = cursor.getString(cursor.getColumnIndex("USER_NAME"));
                user.o_email = cursor.getString(cursor.getColumnIndex("EMAIL"));
                listUsers.add(user);
            }
        }
        assert cursor != null;
        cursor.close();
        closeDb();
        return listUsers;
    }

    public boolean authenticate(String email, String password) {
        Log.d("Entered", "into Authenticate");

        int flag = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("Entered", "authenticate method bef query..");
        Cursor cursor = db.query(TABLE_NAME, null, "EMAIL=? and PASSWORD=?", new String[]{email, password}, null, null, null);
        Log.d("Entered", "authenticate method after query..");
        if (cursor.getCount() > 0) {
            Log.d("Entered", "if loop");
            flag = 1;

        }

        cursor.close();
        db.close();

        return flag != -1;
    }


    public boolean addPost(String em, String post, byte[] byteArray) {

        long isInserted = 1;
        openDb();
        Cursor cur = sdb.query(TABLE_NAME, null, "EMAIL=?", new String[]{em}, null, null, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            String u_id = cur.getString(cur.getColumnIndex("USER_ID"));
            Log.d("u_id", "" + u_id);

            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_6, post);
            contentValues.put(COL_1, u_id);
            Log.d("addPostuser: ",u_id);
            contentValues.put(COL_10, byteArray);
            isInserted = sdb.insert(TABLE_NAME1, null, contentValues);

            Log.d("isInserted", "" + isInserted);

        }
        cur.close();
        closeDb();
        if (isInserted == -1) {
            return false;
        } else {
            return true;
        }

    }


    public ArrayList<Users> getPosts() {
        ArrayList<Users> listUsers = new ArrayList<>();
        openDb();


        Cursor cursor = sdb.query(true, TABLE_NAME1 + " pt", new String[]{"(select user_name from reg_table where user_id=pt.user_id) user_name",
                COL_6, COL_5, COL_1, COL_10, "(select count(comment) from coments_table where post_id=pt.post_id) count"}, null, null, null, null, "POST_ID DESC", null);

        if (cursor != null) {
            for (; cursor.moveToNext(); ) {
                Users user = new Users();
                user.o_post = cursor.getString(cursor.getColumnIndex("POST"));
                user.o_username = cursor.getString(0);
                user.o_userId = cursor.getString(cursor.getColumnIndex("USER_ID"));
                Log.d( "users",user.o_userId);
                user.o_postId = cursor.getString(cursor.getColumnIndex("POST_ID"));
                user.o_bytearray = cursor.getBlob(cursor.getColumnIndex("IMAGE"));
                user.o_status = "0";
                user.o_commentcount = cursor.getString(5);
                Cursor likecursor = sdb.query(true, TABLE_NAME3, null, "USER_ID=? AND POST_ID=?", new String[]{user.o_userId, user.o_postId}, null, null, null, null);

                if (likecursor.getCount() > 0 && likecursor.moveToFirst()) {
                    user.o_status = likecursor.getString(likecursor.getColumnIndex(COL_12));

                }
                likecursor.close();
                long count= DatabaseUtils.longForQuery(sdb, "select count(*) from likes_table where POST_ID=? ", new String[] {user.o_postId});
                user.o_likecount=count;
                listUsers.add(user);
            }


        }
        cursor.close();
        closeDb();
        return listUsers;

    }


    public boolean addComment(String o_userId, String pcoment, String o_postId) {

        long isInserted = 1;
        openDb();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_9, pcoment);
        contentValues.put(COL_1, o_userId);
        contentValues.put(COL_5, o_postId);

        isInserted = sdb.insert(TABLE_NAME2, null, contentValues);

        Log.d("isInserted", "" + isInserted);

        closeDb();
        if (isInserted == -1) {
            return false;
        } else {
            return true;
        }
    }

    public ArrayList<Users> getComents(String p_id) {
     Log.d("getComents: ", p_id);


        ArrayList<Users> listUsers = new ArrayList<>();
        openDb();
        Log.d("before query", "getComents: ");
        Cursor cursor = sdb.query(true, TABLE_NAME2 + " pt", new String[]{"(select user_name from reg_table where user_id=pt.user_id) user_name",
               COL_9, COL_8, COL_1, COL_5}, COL_5+"='"+p_id+"'", null, null, null, null, null);

        Log.d("after query", "getComents: ");
        if (cursor != null) {
            for (; cursor.moveToNext(); ) {
                Users user = new Users();
                user.o_coment = cursor.getString(1);
                user.o_username = cursor.getString(0);
                user.o_userId = cursor.getString(cursor.getColumnIndex("USER_ID"));
                user.o_comentId = cursor.getString(cursor.getColumnIndex("COMMENT_ID"));
                listUsers.add(user);
            }
        }
        assert cursor != null;
        cursor.close();
        closeDb();
        return listUsers;

    }

    public void addLike(String o_userId, String o_postId, CharSequence text, TextView t3) {
        String status = "1";
        long isInserted = 1;
        openDb();
        ContentValues contentValues = new ContentValues();
        Cursor cursor = null;
        if(text.equals("LIKE")) {
            cursor = sdb.query(true, TABLE_NAME3, null, "POST_ID=? AND USER_ID=?", new String[]{o_userId, o_postId}, null, null, null, null);
            contentValues.put(COL_1, o_userId);
            contentValues.put(COL_5, o_postId);
            contentValues.put(COL_12, status);

            isInserted = sdb.insert(TABLE_NAME3, null, contentValues);

            Log.d("isInserted", "" + isInserted);
            cursor.close();
        }
        else{
            contentValues.put(COL_12, status);
            sdb.delete(TABLE_NAME3, "USER_ID=?", new String[]{o_userId});
        }

     long count= DatabaseUtils.longForQuery(sdb, "select count(*) from likes_table where POST_ID=? ", new String[] {o_postId});
        t3.setText(String.valueOf(count));
        Toast.makeText(activity, count+ "",Toast.LENGTH_LONG).show();
//        if (cursor.getCount() == 0) {
//
//
//            contentValues.put(COL_1, o_userId);
//            contentValues.put(COL_5, o_postId);
//            contentValues.put(COL_12, status);
//
//            isInserted = sdb.insert(TABLE_NAME3, null, contentValues);
//
//            Log.d("isInserted", "" + isInserted);
//
//
//        } else
//
//        {
//            if (cursor.moveToFirst()) {
//                if (cursor.getString(cursor.getColumnIndex(COL_12)).equalsIgnoreCase("1"))
//                    status = "0";
//                else
//                    status = "1";
//
//            }
//            contentValues.put(COL_12, status);
//            sdb.update(TABLE_NAME3, contentValues, "USER_ID=? AND POST_ID=?", new String[]{o_userId, o_postId});
//        }


        closeDb();
    }


    public String getUidFromEmail(String logEmail)
    {
        String u_id=null;
        openDb();
        Cursor cur = sdb.query(TABLE_NAME, null, "EMAIL=?", new String[]{logEmail}, null, null, null);
        if (cur.getCount()==1)
        {
            cur.moveToFirst();
            u_id =  cur.getString(cur.getColumnIndex("USER_ID"));
            Log.d("userId",u_id);
        }
        cur.close();
        closeDb();
        return u_id;
    }


    public boolean EditComment(String o_comentId, String coment) {
        openDb();
        ContentValues content = new ContentValues();
        content.put(COL_9, coment);
        long upd = sdb.update(TABLE_NAME2, content, "COMMENT_ID=?", new String[]{o_comentId});
        Log.d("upd:", upd + "");
        closeDb();
        if (upd == -1)
        {
            return false;
        }
        return true;
    }

    public boolean delComment(String o_comentId) {

        openDb();
        long Rows = sdb.delete(TABLE_NAME2, "COMMENT_ID=?", new String[]{o_comentId});
        Log.d("noOfRows", Rows+"");
        closeDb();
        if( Rows == 0)
        {
            return false;
        }
        return true;
    }



    public boolean delPost(String o_postId) {

        openDb();
        long Rows = sdb.delete(TABLE_NAME1, "POST_ID=?", new String[]{o_postId});
        Log.d("noOfRows", Rows + "");
        closeDb();
        if( Rows == 0)
        {
            return false;
        }
        return true;
    }


}