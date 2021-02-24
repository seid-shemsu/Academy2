package com.example.academy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DB extends SQLiteOpenHelper {
    public DB(@Nullable Context context) {
        super(context, "db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table parts(" +
                "number text," +
                "course_name text," +
                "name text," +
                "youtube text," +
                "music text," +
                "pdf text," +
                "language text)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists parts");
        onCreate(db);
    }

    public List<PartObject> getParts(String course_name, String language){
        SQLiteDatabase db = this.getReadableDatabase();
        List<PartObject> partObjects = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from parts where (course_name = ? and language = ?)", new String[]{course_name, language});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            partObjects.add(new PartObject(
                    cursor.getString(cursor.getColumnIndex("course_name")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("number")),
                    cursor.getString(cursor.getColumnIndex("youtube")),
                    cursor.getString(cursor.getColumnIndex("music")),
                    cursor.getString(cursor.getColumnIndex("pdf")),
                    cursor.getString(cursor.getColumnIndex("language"))));
            cursor.moveToNext();
        }
        return partObjects;
    }

    public long addPart(PartObject partObject){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("number", partObject.getNumber());
        contentValues.put("course_name", partObject.getCourse_name());
        contentValues.put("name", partObject.getName());
        contentValues.put("youtube", partObject.getYoutube());
        contentValues.put("music", partObject.getYoutube());
        contentValues.put("pdf", partObject.getPdf());
        contentValues.put("language", partObject.getLanguage());
        return db.insert("parts", null, contentValues);
    }
}
