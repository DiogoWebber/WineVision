package com.example.diogo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.diogo.database.model.ClientesModel;
import com.example.diogo.database.model.VendasModel;
import com.example.diogo.database.model.VinhosModel;

public class DBOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "banco.db";
    private static final int DATABASE_VERSION = 4;

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ClientesModel.CREATE_TABLE);
        db.execSQL(VendasModel.CREATE_TABLE);
        db.execSQL(VinhosModel.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Atualizar o banco de dados se necessário
        db.execSQL("DROP TABLE IF EXISTS " + ClientesModel.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + VendasModel.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + VinhosModel.TABLE_NAME);
        onCreate(db);
    }
}
