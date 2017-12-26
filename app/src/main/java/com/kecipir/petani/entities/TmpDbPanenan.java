package com.kecipir.petani.entities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by husein on 20/12/17.
 */

public class TmpDbPanenan extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "TmpPanenan.db";
    private static final int DATABASE_VERSION = 2;

    public static final String PANENAN_TABLE_NAME = "Panenan";
    public static final String PANENAN_COLUMN_ID = "id";
    public static final String PANENAN_COLUMN_NAMA = "namaPanenan";
    public static final String PANENAN_COLUMN_HARGA = "harga";
    public static final String PANENAN_COLUMN_QUANTITY = "quantity";

    public TmpDbPanenan(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + PANENAN_TABLE_NAME +
                        "(" + PANENAN_COLUMN_ID + " INTEGER UNIQUE, " +
                        PANENAN_COLUMN_NAMA + " TEXT, " +
                        PANENAN_COLUMN_HARGA + " TEXT, " +
                        PANENAN_COLUMN_QUANTITY + " INTEGER)"
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PANENAN_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertPanenan(int id, String nama, String harga, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(PANENAN_COLUMN_ID, id);
        contentValues.put(PANENAN_COLUMN_NAMA, nama);
        contentValues.put(PANENAN_COLUMN_HARGA, harga);
        contentValues.put(PANENAN_COLUMN_QUANTITY, quantity);

        db.insert(PANENAN_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updatePanenan(int id, String nama, String harga, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PANENAN_COLUMN_NAMA, nama);
        contentValues.put(PANENAN_COLUMN_HARGA, harga);
        contentValues.put(PANENAN_COLUMN_QUANTITY, quantity);
        db.update(PANENAN_TABLE_NAME, contentValues, PANENAN_COLUMN_ID + " = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public ArrayList<HashMap<String, ?>> getAllPanenans() {

        ArrayList<HashMap<String, ?>> list = new ArrayList<HashMap<String, ?>>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM " + PANENAN_TABLE_NAME, null );

        if (res.moveToFirst()) {

            do {
                HashMap<String, Object> temp = new HashMap<String, Object>();
                temp.put("id", res.getString(res.getColumnIndex("id")));
                temp.put("nama", res.getString(res.getColumnIndex("namaPanenan")));
                temp.put("price", res.getString(res.getColumnIndex("harga")));
                temp.put("qty", res.getString(res.getColumnIndex("quantity")));
                list.add(temp);
                Log.i("","test acb " + temp);

            } while (res.moveToNext());

//            do {
//                values.add(res.getString(res.getColumnIndex("id")));
//                values.add(res.getString(res.getColumnIndex("namaPanenan")));
//                values.add(res.getString(res.getColumnIndex("harga")));
//                values.add(res.getString(res.getColumnIndex("quantity")));
//
//                Log.i("","test acb " + values);
//
//            } while (res.moveToNext());
        }
        res.close();

        return list;
    }


    public void DeletePanenans()
    {
        // db.delete(String tableName, String whereClause, String[] whereArgs);
        // If whereClause is null, it will delete all rows.
        SQLiteDatabase db = this.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        db.execSQL("delete from "+ PANENAN_TABLE_NAME);
    }

}
