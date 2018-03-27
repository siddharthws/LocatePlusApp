package com.lplus.activities.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lplus.activities.Objects.CategoryObject;
import com.lplus.activities.Objects.RateObject;

import java.util.ArrayList;

/**
 * Created by CHANDEL on 3/27/2018.
 */


public class AddRateTable extends DatabaseHelper {
    private DatabaseHelper db;

    //Table Name and Columns
    public static final String TABLE_NAME               = "categoryTable";

    public static final String COLUMN_ID                = "id";
    public static final String COLUMN_PLACE_ID       = "category_id";
    public static final String COLUMN_PLACE_RATE    = "category_value";

    //Create a Table name
    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID                + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_PLACE_ID       + " TEXT,"
                    + COLUMN_PLACE_RATE    + " REAL"
                    + ")";

    public AddRateTable(Context context) {
        super(context);
        this.db = DatabaseHelper.Init(context);
    }

    public void SaveRecord(RateObject rateObject)
    {
        ContentValues record;
        String place_id = rateObject.getPlaceId();
        Double place_rate = rateObject.getPlacerate();

        //Iterate each item and put in DB
            record = new ContentValues();

            //put values
            record.put(COLUMN_PLACE_ID,      place_id);
            record.put(COLUMN_PLACE_RATE,   place_rate);

            //save in DB
            Add(TABLE_NAME, record);
    }

    public Double GetRateById(String id)
    {
        Double rate = 0.0;
        SQLiteDatabase Rdb = db.getReadableDatabase();

        Cursor dbRows = Rdb.query(TABLE_NAME,
                new String[]{COLUMN_ID, COLUMN_PLACE_ID, COLUMN_PLACE_RATE},
                COLUMN_PLACE_ID + "=?",
                new String[] {id},
                null,
                null,
                null);

        while (dbRows.moveToNext()) {

            rate = dbRows.getDouble(   dbRows.getColumnIndex(  COLUMN_PLACE_RATE));
        }

        return rate;
    }

    public Boolean RemoveRate(String place_id)
    {
        SQLiteDatabase WDB = db.getWritableDatabase();
        int value = WDB.delete(TABLE_NAME, COLUMN_PLACE_ID + "=" + place_id, null);
        if(value != 0) {
            return true;
        }
        return false;
    }

    public Boolean isRateAvailable(String place_id)
    {
        SQLiteDatabase Rdb = db.getReadableDatabase();

        Cursor dbRows = Rdb.query(TABLE_NAME,
                new String[]{COLUMN_ID, COLUMN_PLACE_ID, COLUMN_PLACE_RATE},
                COLUMN_PLACE_ID + "=?",
                new String[] {place_id},
                null,
                null,
                null);

        if(dbRows.getCount() > 0)
            return true;

        return false;

    }

    public boolean UpdateRateByID(RateObject rateObject) {
        SQLiteDatabase Wdb = db.getWritableDatabase();
        String place_id = rateObject.getPlaceId();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PLACE_ID, place_id);
        contentValues.put(COLUMN_PLACE_RATE, rateObject.getPlacerate());
        int value = Wdb.update(TABLE_NAME,contentValues,COLUMN_PLACE_ID + "=?",new String[] {place_id});

        if(value != 0)
            return true;

        return false;
    }

    public Boolean DeleteAll()
    {
        SQLiteDatabase WDB = db.getWritableDatabase();
        int value = WDB.delete(TABLE_NAME, null, null);
        if(value != 0) {
            return true;
        }
        return false;
    }

    //methods to add data to table......only accepts content values
    private void Add(String tableName, ContentValues contentData)
    {
        SQLiteDatabase WDB = db.getWritableDatabase();

        //Insert Data to Database
        WDB.insert(tableName, null, contentData);
    }

    public void CloseConnection()
    {
        if (db != null)
        {
            db.close();
        }
    }
}
