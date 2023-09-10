package com.ketty.chinesemedicine.provider.cover;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CoverDBOpenHelper extends SQLiteOpenHelper {
    public CoverDBOpenHelper(@Nullable Context context) {
        super(context, "cover.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table coverInfo (_id BIGINT primary key, tag VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
