package com.example.restaurantmap.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.restaurantmap.Model.Restaurant;
import com.example.restaurantmap.Util.Util;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_RES_TABLE = "CREATE TABLE " + Util.TABLE_NAME + "(" +
                Util.RES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Util.RES_NAME + " TEXT," +
                Util.RES_LAT + " REAL," +
                Util.RES_LNG + " REAL)";


        db.execSQL(CREATE_RES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String DROP_TABLE = "DROP TABLE IF EXISTS " + Util.DATABASE_NAME;


        db.execSQL(DROP_TABLE);
        this.onCreate(db);
    }


    public long insertRestaurant(Restaurant restaurant) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues val = new ContentValues();

        // Put all values to be inserted to ContentValues
        val.put(Util.RES_NAME, restaurant.getName());
        val.put(Util.RES_LAT, restaurant.getLat());
        val.put(Util.RES_LNG, restaurant.getLng());

        long newRowId = db.insert(Util.TABLE_NAME, null, val);
        db.close();
        return newRowId;
    }


    public List<Restaurant> getRestaurants() {
        SQLiteDatabase db = getReadableDatabase();
        List<Restaurant> restaurants = new ArrayList<>();


        String QUERY = "SELECT * FROM " + Util.TABLE_NAME;
        Cursor cursor = db.rawQuery(QUERY, null);


        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {

                int id = cursor.getInt(cursor.getColumnIndex(Util.RES_ID));
                String name = cursor.getString(cursor.getColumnIndex(Util.RES_NAME));
                double lat = cursor.getDouble(cursor.getColumnIndex(Util.RES_LAT));
                double lng = cursor.getDouble(cursor.getColumnIndex(Util.RES_LNG));


                restaurants.add(new Restaurant(id, name, lat, lng));

                cursor.moveToNext();
            }
        }


        db.close(); cursor.close();


        return restaurants;
    }
}
