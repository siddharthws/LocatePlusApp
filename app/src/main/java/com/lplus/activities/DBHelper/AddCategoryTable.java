package com.lplus.activities.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lplus.activities.Objects.CategoryObject;

import java.util.ArrayList;

/**
 * Created by Sai_Kameswari on 21-03-2018.
 */

public class AddCategoryTable extends DatabaseHelper {

    private DatabaseHelper db;

    //Table Name and Columns
    public static final String TABLE_NAME               = "categoryTable";

    public static final String COLUMN_ID                = "id";
    public static final String COLUMN_CATEGORY_ID       = "category_id";
    public static final String COLUMN_CATEGORY_VALUE    = "category_value";

    //Create a Table name
    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID                + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_CATEGORY_ID       + " TEXT,"
                    + COLUMN_CATEGORY_VALUE    + " TEXT"
                    + ")";

    public AddCategoryTable(Context context) {
        super(context);
        this.db = DatabaseHelper.Init(context);
    }

    public void SaveRecords(CategoryObject categoryObject)
    {
        ContentValues record;
        ArrayList<String> category_ids = categoryObject.getCategory_ids();
        ArrayList<String> category_values = categoryObject.getCategory_values();

        //Iterate each item and put in DB
        for(int i = 0; i<category_ids.size(); i++)
        {
            record = new ContentValues();

            //put values
            record.put(COLUMN_CATEGORY_ID,      category_ids.get(i));
            record.put(COLUMN_CATEGORY_VALUE,   category_values.get(i));

            //save in DB
            Add(TABLE_NAME, record);
        }
    }

    public ArrayList<ArrayList> ReadRecords()
    {
        ArrayList<String> category_ids = new ArrayList<>();
        ArrayList<String> category_values = new ArrayList<>();
        SQLiteDatabase Rdb = db.getReadableDatabase();

        Cursor dbRows = Rdb.query(TABLE_NAME,
                new String[]{COLUMN_ID, COLUMN_CATEGORY_ID, COLUMN_CATEGORY_VALUE},
                null,
                null,
                null,
                null,
                null);

        while (dbRows.moveToNext()) {

            category_ids.add(       dbRows.getString(   dbRows.getColumnIndex(  COLUMN_CATEGORY_ID)));
            category_values.add(    dbRows.getString(   dbRows.getColumnIndex(  COLUMN_CATEGORY_VALUE)));
        }
        ArrayList<ArrayList> result = new ArrayList<>();
        result.add(category_ids);
        result.add(category_values);

        return result;
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
