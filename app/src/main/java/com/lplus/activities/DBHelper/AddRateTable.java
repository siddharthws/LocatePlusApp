package com.lplus.activities.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.lplus.activities.Objects.CategoryObject;
import com.lplus.activities.Objects.RateObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

/**
 * Created by CHANDEL on 3/27/2018.
 */


public class AddRateTable extends DatabaseHelper {
    private DatabaseHelper db;

    //Table Name and Columns
    public static final String TABLE_NAME               = "RateTable";

    public static final String COLUMN_ID                = "id";
    public static final String COLUMN_PLACE_ID          = "place_id";
    public static final String COLUMN_PLACE_RATE        = "place_rate";
    public static final String COLUMN_RATE_USERS        = "rate_users";

    //Create a Table name
    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID                + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_PLACE_ID       + " TEXT,"
                    + COLUMN_PLACE_RATE    + " REAL,"
                    + COLUMN_RATE_USERS    + " INTEGER"
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
        int rate_users = rateObject.getRateusers();

        //Iterate each item and put in DB
            record = new ContentValues();

            //put values
            record.put(COLUMN_PLACE_ID,      place_id);
            record.put(COLUMN_PLACE_RATE,   place_rate);
            record.put(COLUMN_RATE_USERS,   rate_users);

            //save in DB
            Add(TABLE_NAME, record);
    }

    public Double GetRateById(String id)
    {
        Double rate = 0.0;
        SQLiteDatabase Rdb = db.getReadableDatabase();

        Cursor dbRows = Rdb.query(TABLE_NAME,
                new String[]{COLUMN_ID, COLUMN_PLACE_ID, COLUMN_PLACE_RATE,COLUMN_RATE_USERS},
                COLUMN_PLACE_ID + "=?",
                new String[] {id},
                null,
                null,
                null);

        while (dbRows.moveToNext()) {
            System.out.println("Rate Available "+ dbRows);

            rate = dbRows.getDouble(   dbRows.getColumnIndex(  COLUMN_PLACE_RATE));
            System.out.println("rate = "+rate);
        }

        return rate;
    }

    public Integer GetUsersById(String id)
    {
        int users = 0;
        SQLiteDatabase Rdb = db.getReadableDatabase();

        Cursor dbRows = Rdb.query(TABLE_NAME,
                new String[]{COLUMN_ID, COLUMN_PLACE_ID, COLUMN_PLACE_RATE,COLUMN_RATE_USERS},
                COLUMN_PLACE_ID + "=?",
                new String[] {id},
                null,
                null,
                null);

        while (dbRows.moveToNext()) {

            users = dbRows.getInt(   dbRows.getColumnIndex(  COLUMN_RATE_USERS));
            System.out.println("users = "+users);
        }

        return users;
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
                new String[]{COLUMN_ID, COLUMN_PLACE_ID, COLUMN_PLACE_RATE, COLUMN_RATE_USERS},
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
        contentValues.put(COLUMN_RATE_USERS, rateObject.getRateusers());
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
