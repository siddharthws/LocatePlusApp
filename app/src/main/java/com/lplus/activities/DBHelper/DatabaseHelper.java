package com.lplus.activities.DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sai_Kameswari on 17-03-2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper   dbHelper   = null;

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "LOCATEPLUS_DB";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(AddCategoryTable.CREATE_TABLE);
        db.execSQL(AddFacilityTable.CREATE_TABLE);
        db.execSQL(AddFavoutiteTable.CREATE_TABLE);
        db.execSQL(MarkersTable.CREATE_TABLE);
        db.execSQL(AddUnSyncTable.CREATE_TABLE);
        db.execSQL(ReviewsTable.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + AddCategoryTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + AddFacilityTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + AddFavoutiteTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MarkersTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + AddUnSyncTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ReviewsTable.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }



    public static DatabaseHelper Init(Context context)
    {
        if (dbHelper == null)
        {
            dbHelper = new DatabaseHelper(context);
        }
        return dbHelper;
    }
}
