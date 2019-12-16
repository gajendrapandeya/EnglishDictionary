package com.codermonkeys.dictionarybeast;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    public String DB_PATH = null;
    public static String DB_NAME = "eng_dictionary.db";
    public SQLiteDatabase myDatabase;
    final Context myContext;

    public DatabaseHelper(Context context) {

        super(context, DB_NAME,null, 1);
        this.myContext = context;
        this.DB_PATH = "/data/data/" + context.getPackageName() + "/" + "database";
        Log.e("path 1", DB_PATH);
    }

    private void copyDatabase() throws IOException {

        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput  = new FileOutputStream(outFileName);

        byte[] buffer = new byte[64];
        int length;

        while ((length = myInput.read(buffer))>0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
        Log.i("Copy Database", "Database Copied");
    }

    public boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        File dbFile = myContext.getDatabasePath(DB_NAME);
        return dbFile.exists();
//        try {
//            String myPath = DB_PATH + DB_NAME;
//            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
//        } catch (SQLException e) {
//
//        }
//
//        if(checkDB!=null) {
//            checkDB.close();
//        }
//        return checkDB != null ? true: false;
    }

    public void createDatabase()  throws IOException {

        boolean dbExist = checkDataBase();
        if(!dbExist) {
            this.getReadableDatabase();
            try {
                 copyDatabase();
            } catch (IOException e) {
              throw new   Error("Error Copying Database");
            }
        }
    }

    public void openDatabase() throws SQLException {

        String myPath = DB_PATH + DB_NAME;
        myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public  synchronized void close() {

        if(myDatabase != null) {
            myDatabase.close();
            super.close();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        try {

            this.getReadableDatabase();
            myContext.deleteDatabase(DB_NAME);
            copyDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
