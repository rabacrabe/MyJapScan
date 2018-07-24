package japscan.gtheurillat.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import japscan.gtheurillat.db.dao.BookmarkDAO;
import japscan.gtheurillat.db.model.Bookmark;

/**
 * Created by gtheurillat on 17/07/2018.
 */

public class DbBookmarkBaseHelper extends SQLiteOpenHelper{

    public static final String TABLE_NAME = "bookmark";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME_SERIE";
    public static final String COL_3 = "NAME_CHAPTER";
    public static final String COL_4 = "NAME_PAGE";
    public static final String COL_5 = "URL";

    /*
    public DbFavorisBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
*/

    public DbBookmarkBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("DB", "CREATE TABLE " + TABLE_NAME);

        String METIER_TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
                                            COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                            COL_2 + " TEXT, " +
                                            COL_3 + " TEXT, " +
                                            COL_4 + " TEXT, " +
                                            COL_5 + " TEXT);";


        db.execSQL(METIER_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("DB", "SAVE TABLE " + TABLE_NAME);
        String METIER_TABLE_SELECT_ALL = "SELECT * FROM " + TABLE_NAME + ";";
        Cursor cursor = db.rawQuery(METIER_TABLE_SELECT_ALL, null);

        Log.e("DB", "DROP TABLE " + TABLE_NAME);
        String METIER_TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        db.execSQL(METIER_TABLE_DROP);

        onCreate(db);

        Log.e("DB", "INSERT OLD RECORDS TABLE IN " + TABLE_NAME);
        if (cursor.moveToFirst()) {
            do {
                ContentValues value = new ContentValues();

                value.put(COL_2, cursor.getColumnIndex(COL_2));
                value.put(COL_3, cursor.getColumnIndex(COL_3));
                value.put(COL_4, cursor.getColumnIndex(COL_4));
                value.put(COL_5, cursor.getColumnIndex(COL_5));

                db.insert(TABLE_NAME, null, value);

            } while (cursor.moveToNext());
        }
    }

}
