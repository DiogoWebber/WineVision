package com.example.diogo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.diogo.database.model.ClientesModel;

public class DBOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_MAME ="banco.db";
    private static final int DATABASE_VERSION =1;

    public DBOpenHelper(Context context){
        super(context, DATABASE_MAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ClientesModel.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
