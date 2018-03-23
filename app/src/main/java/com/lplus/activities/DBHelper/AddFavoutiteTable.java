package com.lplus.activities.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lplus.activities.Objects.FavouriteObject;

import java.util.ArrayList;

/**
 * Created by CHANDEL on 3/22/2018.
 */

public class AddFavoutiteTable extends DatabaseHelper {

    private DatabaseHelper db;

    //Table Name and Columns
    public static final String TABLE_NAME               = "favouriteTable";
    public static final String COLUMN_ID                = "id";
    public static final String COLUMN_PLACE_ID          = "place_id";
    public static final String COLUMN_PLACE_NAME        = "place_name";
    public static final String COLUMN_PLACE_ADDRESS     = "place_address";
    public static final String COLUMN_MARKER_LAT        = "marker_lat";
    public static final String COLUMN_MARKER_LONG       = "marker_lng";

    //Create a Table name
    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID                + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_PLACE_ID          + " TEXT,"
                    + COLUMN_PLACE_NAME        + " TEXT,"
                    + COLUMN_PLACE_ADDRESS     + " TEXT,"
                    + COLUMN_MARKER_LAT        + " REAL,"
                    + COLUMN_MARKER_LONG       + " REAL"
                    + ")";

    public AddFavoutiteTable(Context context) {
        super(context);
        this.db = DatabaseHelper.Init(context);
    }

    public void SaveRecord(FavouriteObject favouriteObject)
    {
        ContentValues record;
        String favourite_name           = favouriteObject.getFavourite_name();
        String favourite_address        = favouriteObject.getFavourite_address();
        Double favourite_lat            = favouriteObject.getFavourite_lat();
        Double favourite_lng            = favouriteObject.getFavourite_lng();
        String favourite_place_id       = favouriteObject.getFavourite_place_id();

        //ADD an item and put in DB
            record = new ContentValues();

            //put values
            record.put(COLUMN_PLACE_ID,         favourite_place_id);
            record.put(COLUMN_PLACE_NAME,       favourite_name);
            record.put(COLUMN_PLACE_ADDRESS,    favourite_address);
            record.put(COLUMN_MARKER_LAT,       favourite_lat);
            record.put(COLUMN_MARKER_LONG,      favourite_lng);

            //save in DB
            Add(TABLE_NAME, record);
        }

    public ArrayList<FavouriteObject> ReadRecords()
    {
        ArrayList<FavouriteObject> favouriteObjectsList = new ArrayList<>();
       FavouriteObject favouriteObject;
        SQLiteDatabase Rdb = db.getReadableDatabase();

        Cursor dbRows = Rdb.query(TABLE_NAME,
                new String[]{COLUMN_ID, COLUMN_PLACE_ID, COLUMN_PLACE_NAME, COLUMN_PLACE_ADDRESS, COLUMN_MARKER_LAT, COLUMN_MARKER_LONG},
                null,
                null,
                null,
                null,
                null);

        while (dbRows.moveToNext()) {

            favouriteObject = new FavouriteObject();
            favouriteObject.setFavourite_place_id(dbRows.getString(   dbRows.getColumnIndex(  COLUMN_PLACE_ID)));
            favouriteObject.setFavourite_name(dbRows.getString(   dbRows.getColumnIndex(  COLUMN_PLACE_NAME)));
            favouriteObject.setFavourite_address(dbRows.getString(   dbRows.getColumnIndex(  COLUMN_PLACE_ADDRESS)));
            favouriteObject.setFavourite_lat(dbRows.getDouble(   dbRows.getColumnIndex(  COLUMN_MARKER_LAT)));
            favouriteObject.setFavourite_lng(dbRows.getDouble(   dbRows.getColumnIndex(  COLUMN_MARKER_LONG)));
            favouriteObjectsList.add(favouriteObject);
        }
        return favouriteObjectsList;
    }

    public Boolean RemoveFavourite(String place_id)
    {
        SQLiteDatabase WDB = db.getWritableDatabase();
        int value = WDB.delete(TABLE_NAME, COLUMN_PLACE_ID + "=" + place_id, null);
        if(value != 0) {
            return true;
        }
        return false;
    }

    public Boolean isFavourite(String place_id)
    {
        String favourite_place_id = place_id;
        SQLiteDatabase Rdb = db.getReadableDatabase();

        Cursor dbRows = Rdb.query(TABLE_NAME,
                new String[]{COLUMN_ID, COLUMN_PLACE_ID, COLUMN_PLACE_NAME, COLUMN_PLACE_ADDRESS, COLUMN_MARKER_LAT, COLUMN_MARKER_LONG},
                COLUMN_PLACE_ID + "=?",
                new String[] {place_id},
                null,
                null,
                null);

        if(dbRows.getCount() > 0)
            return true;

        return false;

    }

    public FavouriteObject getClickedRecord(String place_id)
    {
        FavouriteObject selectedFavoriteObject = new FavouriteObject();
        String favourite_place_id = place_id;
        SQLiteDatabase Rdb = db.getReadableDatabase();

        Cursor dbRows = Rdb.query(TABLE_NAME,
                new String[]{COLUMN_ID, COLUMN_PLACE_ID, COLUMN_PLACE_NAME, COLUMN_PLACE_ADDRESS, COLUMN_MARKER_LAT, COLUMN_MARKER_LONG},
                COLUMN_PLACE_ID + "=?",
                new String[] {place_id},
                null,
                null,
                null);

        if(dbRows.getCount() > 0) {
            dbRows.moveToFirst();
            selectedFavoriteObject.setFavourite_place_id(dbRows.getString(dbRows.getColumnIndex(COLUMN_PLACE_ID)));
            selectedFavoriteObject.setFavourite_name(dbRows.getString(dbRows.getColumnIndex(COLUMN_PLACE_NAME)));
            selectedFavoriteObject.setFavourite_address(dbRows.getString(dbRows.getColumnIndex(COLUMN_PLACE_ADDRESS)));
            selectedFavoriteObject.setFavourite_lat(dbRows.getDouble(dbRows.getColumnIndex(COLUMN_MARKER_LAT)));
            selectedFavoriteObject.setFavourite_lng(dbRows.getDouble(dbRows.getColumnIndex(COLUMN_MARKER_LONG)));
        }
        return selectedFavoriteObject;

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
