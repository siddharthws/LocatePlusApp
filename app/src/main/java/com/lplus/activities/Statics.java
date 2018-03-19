package com.lplus.activities;

import com.lplus.activities.Macros.Keys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Sai_Kameswari on 19-03-2018.
 */

public class Statics {
    private static HashMap<Integer, String> categories;
    private static HashMap<Integer, String> facilities;

    public static void onFailInit()
    {
        categories=new HashMap<>();
        facilities = new HashMap<>();
    }

    public static void setCategories(JSONArray categoriesarray)
    {
        categories = new HashMap<>();
       for(int i=0; i<categoriesarray.length();i++)
       {
           try {
               JSONObject cat = categoriesarray.getJSONObject(i);
               System.out.println("Cat ID: "+cat.getString(Keys.CAT_ID));
               System.out.println("Cat: "+cat.getString(Keys.CAT_NAME));
               categories.put(cat.getInt(Keys.CAT_ID), cat.getString(Keys.CAT_NAME));
           } catch (JSONException e) {
               e.printStackTrace();
           }
       }

    }

    public static void setFacilities(JSONArray facilitiesarray)
    {
        facilities = new HashMap<>();
        for(int i=0; i<facilitiesarray.length();i++)
        {
            try {
                JSONObject fac = facilitiesarray.getJSONObject(i);
                System.out.println("Fac ID: "+fac.getString(Keys.FAC_ID));
                System.out.println("Fac: "+fac.getString(Keys.FAC_NAME));
                facilities.put(fac.getInt(Keys.FAC_ID), fac.getString(Keys.FAC_NAME));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static HashMap<Integer, String> getCategories()
    {
        System.out.println("Category List at statics: "+categories.toString());
        return categories;
    }

    public static HashMap<Integer, String> getFacilities()
    {
        return facilities;
    }
}
