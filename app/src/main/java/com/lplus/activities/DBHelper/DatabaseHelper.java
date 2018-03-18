package com.lplus.activities.DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lplus.activities.Models.NewPlace;

/**
 * Created by Sai_Kameswari on 17-03-2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    private static SQLiteDatabase db = null;

    // Database Name
    private static final String DATABASE_NAME = "locateplus_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        this.db = db;
        // create notes table
        db.execSQL(NewPlace.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + NewPlace.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public static SQLiteDatabase getDB()
    {
        return db;
    }
}
