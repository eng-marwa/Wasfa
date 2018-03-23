package com.mr.java.shno.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.mr.java.shno.db.Data.COL_CAT;
import static com.mr.java.shno.db.Data.COL_COOKING;
import static com.mr.java.shno.db.Data.COL_ID;
import static com.mr.java.shno.db.Data.COL_IMG;
import static com.mr.java.shno.db.Data.COL_INGREDIENTS;
import static com.mr.java.shno.db.Data.COL_KEY;
import static com.mr.java.shno.db.Data.COL_NAME;
import static com.mr.java.shno.db.Data.COL_SERVING;
import static com.mr.java.shno.db.Data.COL_STEPS;
import static com.mr.java.shno.db.Data.COL_TAG;
import static com.mr.java.shno.db.Data.COL_USER_ID;
import static com.mr.java.shno.db.Data.DBNAME;
import static com.mr.java.shno.db.Data.DBVER;
import static com.mr.java.shno.db.Data.FAV_TABLE_NAME;

/**
 * Created by java on 28/10/2017.
 */


public class DBHelper extends SQLiteOpenHelper {
    private Context context;
    public DBHelper(Context context) {
        super(context, DBNAME, null, DBVER);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql ="create table "+FAV_TABLE_NAME+" ( "+COL_ID+" integer primary key autoincrement , "
                +COL_NAME+" text , "+COL_IMG+" blob , "+COL_INGREDIENTS+" text , "+COL_STEPS+" text , "+COL_TAG+" text , "+COL_CAT+" text,"+
        COL_SERVING+" text , "+COL_COOKING+" text, "+COL_USER_ID+" text, "+COL_KEY+" text )";

        sqLiteDatabase.execSQL(sql);

    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "drop table "+FAV_TABLE_NAME;
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }


}
