package com.lplus.activities.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lplus.activities.Objects.MarkerObject;

import java.util.ArrayList;

/**
 * Created by Sai_Kameswari on 22-03-2018.
 */

public class MarkersTable extends DatabaseHelper {

    private DatabaseHelper db;

    //Table Name and Columns
    public static final String TABLE_NAME               = "markersTable";

    public static final String COLUMN_ID                    = "id";
    public static final String COLUMN_PLACE_ID              = "placeID";
    public static final String COLUMN_PLACE_NAME            = "place_name";
    public static final String COLUMN_PLACE_ADDRESS         = "place_address";
    public static final String COLUMN_PLACE_CATEGORY        = "place_category";
    public static final String COLUMN_PLACE_FACILITIES      = "place_facilities";
    public static final String COLUMN_PLACE_LATITUDE        = "place_latitude";
    public static final String COLUMN_PLACE_LONGITUDE       = "place_longitude";
    public static final String COLUMN_PLACE_DESCRIPTION     = "place_description";
    public static final String COLUMN_CONTACT                = "place_contact";

    //Create a Table name
    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID                 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_PLACE_ID           + " TEXT,"
                    + COLUMN_PLACE_NAME         + " TEXT,"
                    + COLUMN_PLACE_ADDRESS      + " TEXT,"
                    + COLUMN_PLACE_CATEGORY     + " TEXT,"
                    + COLUMN_PLACE_FACILITIES   + " TEXT,"
                    + COLUMN_PLACE_LATITUDE     + " REAL,"
                    + COLUMN_PLACE_LONGITUDE    + " REAL,"
                    + COLUMN_PLACE_DESCRIPTION  + " TEXT,"
                    + COLUMN_CONTACT  + " TEXT"
                    + ")";

    public MarkersTable(Context context) {
        super(context);
        this.db = DatabaseHelper.Init(context);
    }

    public void SaveRecord(MarkerObject markerObject)
    {
        ContentValues record;

        record = new ContentValues();
        //put values
        record.put(COLUMN_PLACE_ID,             markerObject.getMarkerID());
        record.put(COLUMN_PLACE_NAME,           markerObject.getMarkerName());
        record.put(COLUMN_PLACE_ADDRESS,        markerObject.getMarkerAddress());
        record.put(COLUMN_PLACE_CATEGORY,       markerObject.getMarkerCategory());

        //parse the facilities into String
        ArrayList<String> facii = markerObject.getMarkerFacilities();
        StringBuilder sb=new StringBuilder();

        for (int i = 0; i < facii.size()-1; i++)
        {
            sb.append(facii.get(i)).append(", ");
        }
        if(facii.size() > 0) {
            sb.append(facii.get(facii.size()-1));
        }
        record.put(COLUMN_PLACE_FACILITIES,     sb.toString());
        record.put(COLUMN_PLACE_LATITUDE,       markerObject.getMarkerLatitude());
        record.put(COLUMN_PLACE_LONGITUDE,      markerObject.getMarkerLongitude());
        record.put(COLUMN_PLACE_DESCRIPTION,    markerObject.getMarkerDescription());
        record.put(COLUMN_CONTACT,               markerObject.getContact());

        //save in DB
        Add(record);
    }

    public ArrayList<MarkerObject> ReadRecords()
    {
        ArrayList<MarkerObject> marker_objects_list = new ArrayList<>();
        MarkerObject markerObject;
        ArrayList<String> facii;
        String[] array;
        SQLiteDatabase Rdb = db.getReadableDatabase();

        Cursor dbRows = Rdb.query(TABLE_NAME,
                new String[]{COLUMN_ID, COLUMN_PLACE_ID, COLUMN_PLACE_NAME, COLUMN_PLACE_ADDRESS, COLUMN_PLACE_CATEGORY, COLUMN_PLACE_FACILITIES, COLUMN_PLACE_LATITUDE, COLUMN_PLACE_LONGITUDE, COLUMN_PLACE_DESCRIPTION,COLUMN_CONTACT},
                null,
                null,
                null,
                null,
                null);

        while (dbRows.moveToNext()) {
            facii = new ArrayList<>();

            String placeId                  = dbRows.getString(   dbRows.getColumnIndex(  COLUMN_PLACE_ID));
            String name                     = dbRows.getString(   dbRows.getColumnIndex(  COLUMN_PLACE_NAME));
            String address                  = dbRows.getString(   dbRows.getColumnIndex(  COLUMN_PLACE_ADDRESS));
            String category                 = dbRows.getString(   dbRows.getColumnIndex(  COLUMN_PLACE_CATEGORY));
            String facilities_string        = dbRows.getString(   dbRows.getColumnIndex(  COLUMN_PLACE_FACILITIES));
            array = facilities_string.split(",");
            for(int i=0; i<array.length; i++)
            {
                if(!array[i].equals(""))
                {
                    facii.add(array[i]);
                }
            }
            double latitude                 = dbRows.getDouble(   dbRows.getColumnIndex(  COLUMN_PLACE_LATITUDE));
            double longitude                = dbRows.getDouble(   dbRows.getColumnIndex(  COLUMN_PLACE_LONGITUDE));
            String description              = dbRows.getString(   dbRows.getColumnIndex(  COLUMN_PLACE_DESCRIPTION));
            String contact              = dbRows.getString(   dbRows.getColumnIndex(  COLUMN_CONTACT));

            markerObject = new MarkerObject(placeId, name, address, facii, category, latitude, longitude, description, contact);
            marker_objects_list.add(markerObject);
        }
        return marker_objects_list;
    }

    public ArrayList<MarkerObject> GetMarkersByCategory(String category)
    {
        ArrayList<MarkerObject> marker_objects_list = new ArrayList<>();
        MarkerObject markerObject;
        ArrayList<String> facii;
        String[] array;
        SQLiteDatabase Rdb = db.getReadableDatabase();

        Cursor dbRows = Rdb.query(TABLE_NAME,
                new String[]{COLUMN_ID, COLUMN_PLACE_ID, COLUMN_PLACE_NAME, COLUMN_PLACE_ADDRESS, COLUMN_PLACE_CATEGORY, COLUMN_PLACE_FACILITIES, COLUMN_PLACE_LATITUDE, COLUMN_PLACE_LONGITUDE, COLUMN_PLACE_DESCRIPTION, COLUMN_CONTACT},
                COLUMN_PLACE_CATEGORY + "=?",
                new String[] {category},
                null,
                null,
                null);

        while (dbRows.moveToNext()) {
            facii = new ArrayList<>();
            String contacts = dbRows.getString(dbRows.getColumnIndex(COLUMN_CONTACT));
            if(contacts != null || contacts != "null" || contacts.length() !=0) {
                String placeId                  = dbRows.getString(   dbRows.getColumnIndex(  COLUMN_PLACE_ID));
                String name                     = dbRows.getString(   dbRows.getColumnIndex(  COLUMN_PLACE_NAME));
                String address                  = dbRows.getString(   dbRows.getColumnIndex(  COLUMN_PLACE_ADDRESS));
                String facilities_string        = dbRows.getString(   dbRows.getColumnIndex(  COLUMN_PLACE_FACILITIES));
                array = facilities_string.split(",");
                for(int i=0; i<array.length; i++)
                {
                    if(!array[i].equals(""))
                    {
                        facii.add(array[i]);
                    }
                }
                double latitude                 = dbRows.getDouble(   dbRows.getColumnIndex(  COLUMN_PLACE_LATITUDE));
                double longitude                = dbRows.getDouble(   dbRows.getColumnIndex(  COLUMN_PLACE_LONGITUDE));
                String description              = dbRows.getString(   dbRows.getColumnIndex(  COLUMN_PLACE_DESCRIPTION));

                markerObject = new MarkerObject(placeId, name, address, facii, category, latitude, longitude, description,contacts);
                marker_objects_list.add(markerObject);
            }
        }
        return marker_objects_list;
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

    public MarkerObject getObject(String placeId)
    {
        MarkerObject markerObject = null;
        ArrayList<String> facii;
        String[] array;
        SQLiteDatabase Rdb = db.getReadableDatabase();

        Cursor dbRows = Rdb.query(TABLE_NAME,
                new String[]{COLUMN_ID, COLUMN_PLACE_ID, COLUMN_PLACE_NAME, COLUMN_PLACE_ADDRESS, COLUMN_PLACE_CATEGORY, COLUMN_PLACE_FACILITIES, COLUMN_PLACE_LATITUDE, COLUMN_PLACE_LONGITUDE, COLUMN_PLACE_DESCRIPTION, COLUMN_CONTACT},
                COLUMN_PLACE_ID + "=?",
                new String[] {placeId},
                null,
                null,
                null);

        if(dbRows.getCount() > 0) {
            dbRows.moveToFirst();
            facii = new ArrayList<>();

            String place_id                 = dbRows.getString(   dbRows.getColumnIndex(  COLUMN_PLACE_ID));
            String name                     = dbRows.getString(   dbRows.getColumnIndex(  COLUMN_PLACE_NAME));
            String address                  = dbRows.getString(   dbRows.getColumnIndex(  COLUMN_PLACE_ADDRESS));
            String category                 = dbRows.getString(   dbRows.getColumnIndex(  COLUMN_PLACE_CATEGORY));
            String facilities_string        = dbRows.getString(   dbRows.getColumnIndex(  COLUMN_PLACE_FACILITIES));
            array = facilities_string.split("_");
            for(int i=0; i<array.length; i++)
            {
                if(!array[i].equals(""))
                {
                    facii.add(array[i]);
                }
            }
            double latitude                 = dbRows.getDouble(   dbRows.getColumnIndex(  COLUMN_PLACE_LATITUDE));
            double longitude                = dbRows.getDouble(   dbRows.getColumnIndex(  COLUMN_PLACE_LONGITUDE));
            String description              = dbRows.getString(   dbRows.getColumnIndex(  COLUMN_PLACE_DESCRIPTION));
            String contact                  = dbRows.getString(   dbRows.getColumnIndex(  COLUMN_CONTACT));

            markerObject = new MarkerObject(place_id, name, address, facii, category, latitude, longitude, description, contact);
        }
        return markerObject;
    }

    public ArrayList<MarkerObject> getRecordsOnCategory(String category)
    {
        ArrayList<MarkerObject> marker_objects_list = new ArrayList<>();
        MarkerObject markerObject = null;
        ArrayList<String> facii;
        String[] array;
        SQLiteDatabase Rdb = db.getReadableDatabase();

        Cursor dbRows = Rdb.query(TABLE_NAME,
                new String[]{COLUMN_ID, COLUMN_PLACE_ID, COLUMN_PLACE_NAME, COLUMN_PLACE_ADDRESS, COLUMN_PLACE_CATEGORY, COLUMN_PLACE_FACILITIES, COLUMN_PLACE_LATITUDE, COLUMN_PLACE_LONGITUDE, COLUMN_PLACE_DESCRIPTION, COLUMN_CONTACT},
                COLUMN_PLACE_CATEGORY + "=?",
                new String[] {category},
                null,
                null,
                null);

        if(dbRows.getCount() > 0) {
            dbRows.moveToFirst();
            while (dbRows.moveToNext()) {
                facii = new ArrayList<>();

                String placeId                  = dbRows.getString(   dbRows.getColumnIndex(  COLUMN_PLACE_ID));
                String name                     = dbRows.getString(   dbRows.getColumnIndex(  COLUMN_PLACE_NAME));
                String address                  = dbRows.getString(   dbRows.getColumnIndex(  COLUMN_PLACE_ADDRESS));
                String cat                      = dbRows.getString(   dbRows.getColumnIndex(  COLUMN_PLACE_CATEGORY));
                String facilities_string        = dbRows.getString(   dbRows.getColumnIndex(  COLUMN_PLACE_FACILITIES));
                array = facilities_string.split("_");
                for(int i=0; i<array.length; i++)
                {
                    if(!array[i].equals(""))
                    {
                        facii.add(array[i]);
                    }
                }
                double latitude                 = dbRows.getDouble(   dbRows.getColumnIndex(  COLUMN_PLACE_LATITUDE));
                double longitude                = dbRows.getDouble(   dbRows.getColumnIndex(  COLUMN_PLACE_LONGITUDE));
                String description              = dbRows.getString(   dbRows.getColumnIndex(  COLUMN_PLACE_DESCRIPTION));
                String contact              = dbRows.getString(   dbRows.getColumnIndex(  COLUMN_CONTACT));

                markerObject = new MarkerObject(placeId, name, address, facii, cat, latitude, longitude, description, contact);
                marker_objects_list.add(markerObject);
            }
        }
        return marker_objects_list;
    }

    //methods to add data to table......only accepts content values
    private void Add(ContentValues contentData)
    {
        SQLiteDatabase WDB = db.getWritableDatabase();

        //Insert Data to Database
        WDB.insert(TABLE_NAME, null, contentData);
    }

    public void CloseConnection()
    {
        if (db != null)
        {
            db.close();
        }
    }
}
