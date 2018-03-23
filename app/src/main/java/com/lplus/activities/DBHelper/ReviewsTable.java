package com.lplus.activities.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lplus.activities.Objects.ReviewsObject;

import java.util.ArrayList;

/**
 * Created by Sai_Kameswari on 23-03-2018.
 */

public class ReviewsTable extends DatabaseHelper {

    private DatabaseHelper db;

    //Table Name and Columns
    public static final String TABLE_NAME               = "reviewsTable";

    public static final String COLUMN_ID                    = "id";
    public static final String COLUMN_PLACE_ID              = "placeID";
    public static final String COLUMN_PLACE_REVIEWS         = "placeReviews";

    //Create a Table name
    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID                 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_PLACE_ID           + " TEXT,"
                    + COLUMN_PLACE_REVIEWS      + " TEXT"
                    + ")";

    public ReviewsTable(Context context) {
        super(context);
        this.db = DatabaseHelper.Init(context);
    }

    public void SaveRecord(ReviewsObject reviewsObject)
    {
        ContentValues record;

        record = new ContentValues();
        //put values
        record.put(COLUMN_PLACE_ID,             reviewsObject.getPlaceId());

        //parse the facilities into String
        ArrayList<String> reviews = reviewsObject.getReviews();
        StringBuilder sb=new StringBuilder();
        int j=0;
        for (int i = 0; i < reviews.size(); i++)
        {
            j=i+1;
            sb.insert(i, reviews.get(i));
            sb.insert(j, "_");
        }
        record.put(COLUMN_PLACE_REVIEWS,    sb.toString());

        //save in DB
        Add(record);
    }

    public ArrayList<ReviewsObject> ReadRecords()
    {
        ArrayList<ReviewsObject> reviews_object_list = new ArrayList<>();
        ReviewsObject reviewsObject;
        ArrayList<String> reviews;
        String[] array;
        SQLiteDatabase Rdb = db.getReadableDatabase();

        Cursor dbRows = Rdb.query(TABLE_NAME,
                new String[]{COLUMN_ID, COLUMN_PLACE_ID, COLUMN_PLACE_REVIEWS},
                null,
                null,
                null,
                null,
                null);

        while (dbRows.moveToNext()) {
            reviews = new ArrayList<>();

            String placeId                  = dbRows.getString(   dbRows.getColumnIndex(  COLUMN_PLACE_ID));
            String reviews_string           = dbRows.getString(   dbRows.getColumnIndex(  COLUMN_PLACE_REVIEWS));
            array = reviews_string.split("_");
            for(int i=0; i<array.length; i++)
            {
                if(!array[i].equals(""))
                {
                    reviews.add(array[i]);
                }
            }
            reviewsObject = new ReviewsObject(placeId, reviews);
            reviews_object_list.add(reviewsObject);
        }
        return reviews_object_list;
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
