package com.lplus.activities.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lplus.activities.Objects.CategoryObject;
import com.lplus.activities.Objects.UnSyncObject;

import java.util.ArrayList;

/**
 * Created by CHANDEL on 3/23/2018.
 */

public class AddUnSyncTable extends DatabaseHelper {
    private DatabaseHelper db;

    //Table Name and Columns
    public static final String TABLE_NAME               = "photosTable";

    public static final String COLUMN_ID                = "id";
    public static final String COLUMN_PHOTO_IDS       = "photo_ids";
    public static final String COLUMN_PHOTO_PATHS       = "photo_paths";
    public static final String COLUMN_PLACE_NAME    = "place_name";
    public static final String COLUMN_PLACE_ADDRESS    = "place_address";
    public static final String COLUMN_PLACE_CATEGORY    = "place_category";
    public static final String COLUMN_PLACE_FACILITIES    = "place_facilities";
    public static final String COLUMN_PLACE_LAT    = "place_latitude";
    public static final String COLUMN_PLACE_LONG    = "place_longitude";
    public static final String COLUMN_PLACE_DESCRIPTION    = "place_dec";


    //Create a Table name
    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID                + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_PHOTO_IDS       + " TEXT,"
                    + COLUMN_PHOTO_PATHS    + " TEXT,"
                    + COLUMN_PLACE_NAME    + " TEXT,"
                    + COLUMN_PLACE_ADDRESS    + " TEXT,"
                    + COLUMN_PLACE_LAT    + " REAL,"
                    + COLUMN_PLACE_LONG    + " REAL,"
                    + COLUMN_PLACE_CATEGORY    + " TEXT,"
                    + COLUMN_PLACE_FACILITIES    + " TEXT,"
                    + COLUMN_PLACE_DESCRIPTION    + " TEXT"
                    + ")";

    public AddUnSyncTable(Context context) {
        super(context);
        this.db = DatabaseHelper.Init(context);
    }

    public void SaveRecord(UnSyncObject unSyncObject)
    {
        ContentValues record;

        //Iterate each item and put in DB
            record = new ContentValues();

            //put values
            record.put(COLUMN_PHOTO_IDS,        unSyncObject.getPhoto_ids());
            record.put(COLUMN_PHOTO_PATHS,   unSyncObject.getPhoto_paths());
            record.put(COLUMN_PLACE_NAME,   unSyncObject.getPlace_name());
            record.put(COLUMN_PLACE_ADDRESS,   unSyncObject.getPlace_addres());
            record.put(COLUMN_PLACE_LAT,   unSyncObject.getPlace_lat());
            record.put(COLUMN_PLACE_LONG,   unSyncObject.getPlace_lng());
            record.put(COLUMN_PLACE_CATEGORY,   unSyncObject.getPlace_category());
            record.put(COLUMN_PLACE_FACILITIES,   unSyncObject.getPlace_facilities());
            record.put(COLUMN_PLACE_DESCRIPTION,   unSyncObject.getPlace_decription());

            //save in DB
            Add(TABLE_NAME, record);

    }

    public ArrayList<UnSyncObject> ReadRecords()
    {
        ArrayList<UnSyncObject> unSyncDataList = new ArrayList<>();
        SQLiteDatabase Rdb = db.getReadableDatabase();
        UnSyncObject unSyncObject;

        Cursor dbRows = Rdb.query(TABLE_NAME,
                new String[]{COLUMN_ID, COLUMN_PHOTO_IDS, COLUMN_PHOTO_PATHS, COLUMN_PLACE_NAME, COLUMN_PLACE_ADDRESS,COLUMN_PLACE_LAT, COLUMN_PLACE_LONG, COLUMN_PLACE_CATEGORY, COLUMN_PLACE_FACILITIES, COLUMN_PLACE_DESCRIPTION},
                null,
                null,
                null,
                null,
                null);

        while (dbRows.moveToNext()) {
            unSyncObject = new UnSyncObject();
            unSyncObject.setPhoto_ids(       dbRows.getString(   dbRows.getColumnIndex(  COLUMN_PHOTO_IDS)));
            unSyncObject.setPhoto_paths(       dbRows.getString(   dbRows.getColumnIndex( COLUMN_PHOTO_PATHS )));
            unSyncObject.setPlace_name(      dbRows.getString(   dbRows.getColumnIndex( COLUMN_PLACE_NAME )));
            unSyncObject.setPlace_addres(       dbRows.getString(   dbRows.getColumnIndex( COLUMN_PLACE_ADDRESS )));
            unSyncObject.setPlace_lat(       dbRows.getDouble(   dbRows.getColumnIndex( COLUMN_PLACE_LAT )));
            unSyncObject.setPlace_lng(       dbRows.getDouble(   dbRows.getColumnIndex( COLUMN_PLACE_LONG )));
            unSyncObject.setPlace_category(       dbRows.getString(   dbRows.getColumnIndex( COLUMN_PLACE_CATEGORY )));
            unSyncObject.setPlace_facilities(       dbRows.getString(   dbRows.getColumnIndex( COLUMN_PLACE_FACILITIES )));
            unSyncObject.setPlace_decription(       dbRows.getString(   dbRows.getColumnIndex( COLUMN_PLACE_DESCRIPTION )));
            unSyncDataList.add(unSyncObject);
        }

        return unSyncDataList;
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

    public Boolean DeleteUnSyncData(String place_id)
    {
        SQLiteDatabase WDB = db.getWritableDatabase();
        int value = WDB.delete(TABLE_NAME,  null, null);
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
