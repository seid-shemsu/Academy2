package com.example.academy.users;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class UsersDatabase extends SQLiteOpenHelper {

    public UsersDatabase(Context context, String name) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table users " +
                "(name text primary key, phone text, uri text, gender text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    public void insert(String name, String phone, String uri, String gender) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("uri", uri);
        contentValues.put("gender", gender);
        db.insert("users", null, contentValues);
    }

    public String getUser(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        String uri = "0";
        Cursor res = db.rawQuery("select * from users where phone = ?", new String[] {phone});
        if (res.moveToFirst())
            uri = res.getString(res.getColumnIndex("uri"));
        res.close();
        return uri;
    }

    public ArrayList<UserObject> getAll() {
        ArrayList<UserObject> objects = new ArrayList();
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from users", null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            String name = res.getString(res.getColumnIndex("name"));
            String uri = res.getString(res.getColumnIndex("uri"));
            String phone = res.getString(res.getColumnIndex("phone"));
            String gender = res.getString(res.getColumnIndex("gender"));
            UserObject object = new UserObject(name, uri, phone, gender);
            objects.add(object);
            res.moveToNext();
        }
        return objects;
    }

    public boolean updateUser(String name, String phone, String uri, String gender) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("uri", uri);
        contentValues.put("gender", gender);
        db.update("users", contentValues, "name = ? ", new String[]{name});
        return true;
    }

    public void delete(String phone){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("users", "phone=?", new String[] {phone});
    }
}
