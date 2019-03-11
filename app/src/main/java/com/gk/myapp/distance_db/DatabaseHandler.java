package com.gk.myapp.distance_db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gk on 10-09-2017.
 */
public class DatabaseHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "travelling_report";

    // Contacts table name
    private static final String TABLE_TRAVELLING = "table_travelling";
    private static final String TABLE_TRAVELLING_PER = "table_travelling_per";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LNG = "lng";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_DISTANCE = "distance";
    private static final String KEY_DATE = "date";
    private static final String KEY_RESPONSE="response";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_TRAVELLING + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_LAT + " TEXT,"
                + KEY_LNG+ " TEXT,"+KEY_ADDRESS+ " TEXT,"+KEY_DISTANCE + " TEXT,"+KEY_DATE+ " TEXT," + KEY_RESPONSE+ " TEXT)";

              String CREATE_PER_TABLE = "CREATE TABLE " + TABLE_TRAVELLING_PER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_LAT + " TEXT,"
                + KEY_LNG+ " TEXT,"+KEY_ADDRESS+ " TEXT,"+KEY_DISTANCE + " TEXT,"+KEY_DATE+ " TEXT," + KEY_RESPONSE+ " TEXT)";
        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_PER_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRAVELLING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRAVELLING_PER);
        // Create tables again
        onCreate(db);
    }

    // Adding new contact
   public void addData(DistanceModel distanceModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LAT, ""+distanceModel.getLat()); // Contact Name
        values.put(KEY_LNG, ""+distanceModel.getLng()); // Contact Phone
        values.put(KEY_ADDRESS, distanceModel.getAddress()); // Contact Phone
        values.put(KEY_DISTANCE, distanceModel.getDistance()); // Contact Phone
       values.put(KEY_DATE, distanceModel.getDate()); // Contact Phone
        values.put(KEY_RESPONSE, distanceModel.getResponse());
        // Inserting Row
       if(distanceModel.getLat()!=0)
        db.insert(TABLE_TRAVELLING, null, values);
//       db.insert(TABLE_TRAVELLING_PER, null, values);
        db.close(); // Closing database connection
    }

    // Adding new contact
    public void addPermanentData(DistanceModel distanceModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LAT, ""+distanceModel.getLat()); // Contact Name
        values.put(KEY_LNG, ""+distanceModel.getLng()); // Contact Phone
        values.put(KEY_ADDRESS, distanceModel.getAddress()); // Contact Phone
        values.put(KEY_DISTANCE, distanceModel.getDistance()); // Contact Phone
        values.put(KEY_DATE, distanceModel.getDate()); // Contact Phone
        values.put(KEY_RESPONSE,distanceModel.getResponse());
        // Inserting Row
       db.insert(TABLE_TRAVELLING_PER, null, values);
        db.close(); // Closing database connection
    }


    public DistanceModel getLastLatLng(){
        DistanceModel contactList = new DistanceModel();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TRAVELLING +" ORDER BY ID DESC LIMIT 1";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                contactList.setId(Integer.parseInt(cursor.getString(0)));
                contactList.setLat(Double.parseDouble(cursor.getString(1)));
                contactList.setLng(Double.parseDouble(cursor.getString(2)));
                contactList.setAddress(cursor.getString(3));
                contactList.setDistance(cursor.getString(4));
                contactList.setDate(cursor.getString(5));
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }


    public List<DistanceModel> getCompleteData(){
        List<DistanceModel> contactList = new ArrayList<DistanceModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TRAVELLING +" ORDER BY "+KEY_ID+" ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DistanceModel contact = new DistanceModel();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setLat(Double.parseDouble(cursor.getString(1)));
                contact.setLng(Double.parseDouble(cursor.getString(2)));
                contact.setAddress(cursor.getString(3));
                contact.setDistance(cursor.getString(4));
                contact.setDate("" + cursor.getString(5));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }


    public List<DistanceModel> getPerData(){
        List<DistanceModel> contactList = new ArrayList<DistanceModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TRAVELLING_PER +" ORDER BY "+KEY_ID+" ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DistanceModel contact = new DistanceModel();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setLat(Double.parseDouble(cursor.getString(1)));
                contact.setLng(Double.parseDouble(cursor.getString(2)));
                contact.setAddress(cursor.getString(3));
                contact.setDistance(cursor.getString(4));
                contact.setDate("" + cursor.getString(5));
                contact.setResponse(cursor.getString(6));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    public int getCount(){
        String countQuery = "SELECT  * FROM " + TABLE_TRAVELLING;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public String getTotalDistance(){
        double distance=0;
        // Select All Query
        String selectQuery = "SELECT "+KEY_DISTANCE+ " FROM " + TABLE_TRAVELLING ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                distance=distance+Double.parseDouble(cursor.getString(0));

            } while (cursor.moveToNext());
        }

        // return contact list
        return ""+distance;
    }

    public void deleteRecords(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from "+ TABLE_TRAVELLING);
    }


    public void deletePerRecords(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from "+ TABLE_TRAVELLING_PER);
    }
}

