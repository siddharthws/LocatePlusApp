package com.lplus.activities.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.lplus.activities.Objects.FavouriteObject;
import com.lplus.activities.Objects.PhotoObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

/**
 * Created by CHANDEL on 3/30/2018.
 */

public class AddPhotoTable extends DatabaseHelper {
    private DatabaseHelper db;

    //Table Name and Columns
    public static final String TABLE_NAME               = "PhotoTable";
    public static final String COLUMN_ID                = "id";
    public static final String COLUMN_PLACE_ID          = "place_id";
    public static final String COLUMN_PHOTO_UUID        = "photo_uuid";
    public static final String COLUMN_PHOTO_PATH        = "photo_path";

    //Create a Table name
    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID                + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_PLACE_ID          + " TEXT,"
                    + COLUMN_PHOTO_UUID        + " TEXT,"
                    + COLUMN_PHOTO_PATH        + " TEXT"
                    + ")";

    public AddPhotoTable(Context context) {
        super(context);
        this.db = DatabaseHelper.Init(context);
    }

    public void SavePhotos(PhotoObject photoObject)
    {
        ContentValues record;
        String place_id                 = photoObject.getPlace_id();
        ArrayList<String> photo_uuid    = photoObject.getPhoto_uuids();
        ArrayList<String> photo_paths   = photoObject.getPhoto_paths();

        for(int i = 0;i<photo_uuid.size();i++) {
            //ADD an item and put in DB
            record = new ContentValues();

            //put values
            record.put(COLUMN_PLACE_ID,         place_id);
            record.put(COLUMN_PHOTO_UUID,       photo_uuid.get(i));
            record.put(COLUMN_PHOTO_PATH,       photo_paths.get(i));

            //save in DB
            Add(TABLE_NAME, record);
        }

    }

    public PhotoObject ReadPhotos(String id)
    {
        ArrayList<String> photo_uuid = new ArrayList<>();
        ArrayList<String> photo_path = new ArrayList<>();
        PhotoObject photoObject;
        SQLiteDatabase Rdb = db.getReadableDatabase();

        Cursor dbRows = Rdb.query(TABLE_NAME,
                new String[]{COLUMN_ID, COLUMN_PLACE_ID, COLUMN_PHOTO_UUID, COLUMN_PHOTO_PATH},
                COLUMN_PLACE_ID + "=?",
                new String[] {id},
                null,
                null,
                null);
        System.out.println("message asdjkasj");
        while (dbRows.moveToNext()) {
            System.out.println("message "+dbRows.getString(   dbRows.getColumnIndex(  COLUMN_PHOTO_UUID)));
            System.out.println("message next "+dbRows.getString(   dbRows.getColumnIndex(  COLUMN_PHOTO_PATH)));
            photo_uuid.add(dbRows.getString(   dbRows.getColumnIndex(  COLUMN_PHOTO_UUID)));
            photo_path.add(dbRows.getString(   dbRows.getColumnIndex(  COLUMN_PHOTO_PATH)));
        }
        photoObject =new PhotoObject();
        photoObject.setPlace_id(id);
        photoObject.setPhoto_uuids(photo_uuid);
        photoObject.setPhoto_paths(photo_path);

        return photoObject;
    }

    public Boolean Removeall(String place_id)
    {
        SQLiteDatabase WDB = db.getWritableDatabase();
        int value = WDB.delete(TABLE_NAME, COLUMN_PLACE_ID + "=" + place_id, null);
        if(value != 0) {
            return true;
        }
        return false;
    }

    public Boolean isInStorage(String place_id)
    {
        String favourite_place_id = place_id;
        SQLiteDatabase Rdb = db.getReadableDatabase();

        Cursor dbRows = Rdb.query(TABLE_NAME,
                new String[]{COLUMN_ID, COLUMN_PLACE_ID, COLUMN_PHOTO_UUID, COLUMN_PHOTO_PATH},
                COLUMN_PLACE_ID + "=?",
                new String[] {place_id},
                null,
                null,
                null);

        if(dbRows.getCount() > 0)
            return true;

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
