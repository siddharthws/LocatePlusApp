package com.lplus.activities.Extras;

import android.content.Context;

import com.lplus.activities.Macros.Keys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sai_Kameswari on 19-03-2018.
 */

public class Statics {
    private static ArrayList<String> categories_key;
    private static ArrayList<String> facilities_key;
    private static ArrayList<String> categories_value;
    private static ArrayList<String> facilities_value;
    private static Context context;
    private static TinyDB tinydb;

    public static void onFailInit()
    {
        categories_key=new ArrayList<>();
        categories_value = new ArrayList<>();

        facilities_key=new ArrayList<>();
        facilities_value = new ArrayList<>();
    }

    public static void Init(Context contexts, JSONArray categoriesarray, JSONArray facilitiesarray)
    {
        context = contexts;
        tinydb = new TinyDB(context);
        setCategories(categoriesarray);
        setFacilities(facilitiesarray);
    }

    public static void setCategories(JSONArray categoriesarray)
    {
        categories_key=new ArrayList<>();
        categories_value = new ArrayList<>();

       for(int i=0; i<categoriesarray.length();i++)
       {
           try {
               JSONObject cat = categoriesarray.getJSONObject(i);
               System.out.println("Cat ID: "+cat.getInt(Keys.CAT_ID));
               System.out.println("Cat: "+cat.getString(Keys.CAT_NAME));
               categories_key.add(cat.getString(Keys.CAT_ID));
               categories_value.add(cat.getString(Keys.CAT_NAME));

           } catch (JSONException e) {
               e.printStackTrace();
           }
       }
        System.out.println("Set in shared preferences: "+categories_value.toString());

        tinydb.putListString(Keys.CATEGORY_KEY, categories_key);
        tinydb.putListString(Keys.CATEGORY_VALUE, categories_value);
    }

    public static void setFacilities(JSONArray facilitiesarray)
    {
        facilities_key=new ArrayList<>();
        facilities_value = new ArrayList<>();

        for(int i=0; i<facilitiesarray.length();i++)
        {
            try {
                JSONObject fac = facilitiesarray.getJSONObject(i);
                System.out.println("Cat ID: "+fac.getInt(Keys.FAC_ID));
                System.out.println("Cat: "+fac.getString(Keys.FAC_NAME));
                facilities_key.add(fac.getString(Keys.FAC_ID));
                facilities_value.add(fac.getString(Keys.FAC_NAME));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Set in shared preferences facilities: "+facilities_value.toString());

        tinydb.putListString(Keys.FACILITIES_KEY, facilities_key);
        tinydb.putListString(Keys.FACILITIES_VALUE, facilities_value);
    }
}
