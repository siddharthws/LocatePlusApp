package com.lplus.activities.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lplus.activities.Objects.FacilityObject;

import java.util.ArrayList;

/**
 * Created by Sai_Kameswari on 22-03-2018.
 */

public class AddFacilityTable extends DatabaseHelper {

    private DatabaseHelper db;

    //Table Name and Columns
    public static final String TABLE_NAME               = "facilityTable";

    public static final String COLUMN_ID                = "id";
    public static final String COLUMN_FACILITY_ID       = "facility_id";
    public static final String COLUMN_FACILITY_VALUE    = "facility_value";

    //Create a Table name
    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID                + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_FACILITY_ID       + " TEXT,"
                    + COLUMN_FACILITY_VALUE    + " TEXT,"
                    + ")";

    public AddFacilityTable(Context context) {
        super(context);
        this.db = DatabaseHelper.Init(context);
    }

    public void SaveRecords(FacilityObject facilityObject)
    {
        ContentValues record;
        ArrayList<String> facility_ids = facilityObject.getFacility_ids();
        ArrayList<String> facility_values = facilityObject.getFacility_values();

        //Iterate each item and put in DB
        for(int i = 0; i<facility_ids.size(); i++)
        {
            record = new ContentValues();

            //put values
            record.put(COLUMN_FACILITY_ID,      facility_ids.get(i));
            record.put(COLUMN_FACILITY_VALUE,   facility_values.get(i));

            //save in DB
            Add(TABLE_NAME, record);
        }
    }

    public ArrayList<ArrayList> ReadRecords()
    {
        ArrayList<String> facility_ids = new ArrayList<>();
        ArrayList<String> facility_values = new ArrayList<>();
        SQLiteDatabase Rdb = db.getReadableDatabase();

        Cursor dbRows = Rdb.query(TABLE_NAME,
                new String[]{COLUMN_ID, COLUMN_FACILITY_ID, COLUMN_FACILITY_VALUE},
                null,
                null,
                null,
                null,
                null);

        while (dbRows.moveToNext()) {

            facility_ids.add(       dbRows.getString(   dbRows.getColumnIndex(  COLUMN_FACILITY_ID)));
            facility_values.add(    dbRows.getString(   dbRows.getColumnIndex(  COLUMN_FACILITY_VALUE)));
        }
        ArrayList<ArrayList> result = new ArrayList<>();
        result.add(facility_ids);
        result.add(facility_values);

        return result;
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
