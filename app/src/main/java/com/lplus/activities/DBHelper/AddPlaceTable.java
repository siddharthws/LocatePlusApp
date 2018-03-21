package com.lplus.activities.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lplus.activities.Objects.NewPlace;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sai_Kameswari on 17-03-2018.
 */

public class AddPlaceTable extends DatabaseHelper{

    private SQLiteDatabase db;
    private NewPlace newPlace;

    public AddPlaceTable(Context context) {
        super(context);
        this.db = DatabaseHelper.getDB();
    }

    public void savePlace(NewPlace newPlace) {
        this.newPlace = newPlace;

        SQLiteDatabase Wdb = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(newPlace.COLUMN_LATITUDE, newPlace.getLatitude());
        values.put(newPlace.COLUMN_LONGITUDE, newPlace.getLongitude());
        values.put(newPlace.COLUMN_TIMESTAMP, newPlace.getTimestamp());
        values.put(newPlace.COLUMN_NAME, newPlace.getName());
        values.put(newPlace.COLUMN_ADDRESS, newPlace.getAddress());
        values.put(newPlace.COLUMN_CATEGORY, newPlace.getCategory());
        values.put(newPlace.COLUMN_FACILITIES, newPlace.getFacilities());
        values.put(newPlace.COLUMN_PHOTOS, newPlace.getPhotos());


        // insert row
        long id = db.insert(newPlace.TABLE_NAME, null, values);

        // close db connection
        db.close();
    }

    //Read Table
    public List<NewPlace> getAllPlaces() {
        List<NewPlace> newPlaces = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + newPlace.TABLE_NAME;

        SQLiteDatabase rdb = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
               NewPlace newPlace = new NewPlace();
                newPlace.setId(cursor.getInt(cursor.getColumnIndex(newPlace.COLUMN_ID)));
                newPlace.setLatitude(cursor.getDouble(cursor.getColumnIndex(newPlace.COLUMN_LATITUDE)));
                newPlace.setLongitude(cursor.getDouble(cursor.getColumnIndex(newPlace.COLUMN_LONGITUDE)));
                newPlace.setTimestamp(cursor.getLong(cursor.getColumnIndex(newPlace.COLUMN_TIMESTAMP)));
                newPlace.setName(cursor.getString(cursor.getColumnIndex(newPlace.COLUMN_NAME)));
                newPlace.setAddress(cursor.getString(cursor.getColumnIndex(newPlace.COLUMN_ADDRESS)));
                newPlace.setCategory(cursor.getString(cursor.getColumnIndex(newPlace.COLUMN_CATEGORY)));
                newPlace.setFacilities(cursor.getString(cursor.getColumnIndex(newPlace.COLUMN_FACILITIES)));
                newPlace.setPhotos(cursor.getString(cursor.getColumnIndex(newPlace.COLUMN_PHOTOS)));

                newPlaces.add(newPlace);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return newPlaces;
    }
}
