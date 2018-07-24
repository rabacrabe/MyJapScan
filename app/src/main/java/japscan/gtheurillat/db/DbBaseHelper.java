package japscan.gtheurillat.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import japscan.gtheurillat.db.dao.BookmarkDAO;
import japscan.gtheurillat.db.dao.FavorisDAO;

/**
 * Created by gtheurillat on 17/07/2018.
 */

public class DbBaseHelper extends SQLiteOpenHelper{

    private DbBaseHelper mHandler = null;

    /*
    public DbFavorisBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
*/

    public DbBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.createTableFavoris(db);
        this.createTableBookmark(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("DB", "UPGRADE DB ON VERSION " + String.valueOf(oldVersion) + " -> " + String.valueOf(newVersion));
        this.onUpgradeFavoris(db, oldVersion, newVersion);
        this.onUpgradeBookmark(db, oldVersion, newVersion);
    }

    private void createTableFavoris(SQLiteDatabase db) {

        Log.e("DB", "CREATE TABLE " + FavorisDAO.TABLE_NAME);
/*
        String METIER_TABLE_CREATE = "CREATE TABLE " + FavorisDAO.TABLE_NAME + " (" +
                FavorisDAO.KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavorisDAO.NAME + " TEXT, " +
                FavorisDAO.URL + " TEXT, " +
                FavorisDAO.GENRE + " TEXT, " +
                FavorisDAO.STATUS + " TEXT);";

*/
        Log.e("DB", "-> " + FavorisDAO.METIER_TABLE_CREATE);
        db.execSQL(FavorisDAO.METIER_TABLE_CREATE);
    }

    private void createTableBookmark(SQLiteDatabase db) {

        Log.e("DB", "CREATE TABLE " + BookmarkDAO.TABLE_NAME);
/*
        String METIER_TABLE_CREATE = "CREATE TABLE " + BookmarkDAO.TABLE_NAME + " (" +
                BookmarkDAO.KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BookmarkDAO.NAME_SERIE + " TEXT, " +
                BookmarkDAO.NAME_CHAPTER + " TEXT, " +
                BookmarkDAO.NAME_PAGE + " TEXT, " +
                BookmarkDAO.URL + " TEXT);";
*/
        Log.e("DB", "-> " + BookmarkDAO.METIER_TABLE_CREATE);
        db.execSQL(BookmarkDAO.METIER_TABLE_CREATE);
    }



    private void onUpgradeFavoris(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("DB", "SAVE TABLE " + FavorisDAO.TABLE_NAME);
        String METIER_TABLE_SELECT_ALL = "SELECT * FROM " + FavorisDAO.TABLE_NAME + ";";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(METIER_TABLE_SELECT_ALL, null);
        } catch (Exception e) {
            cursor = null;
        }

        Log.e("DB", "DROP TABLE " + FavorisDAO.TABLE_NAME);
        Log.e("DB", "-> " + FavorisDAO.METIER_TABLE_DROP);
        db.execSQL(FavorisDAO.METIER_TABLE_DROP);


        createTableFavoris(db);

        if (cursor != null) {
            Log.e("DB", "INSERT OLD RECORDS TABLE IN " + FavorisDAO.TABLE_NAME);
            if (cursor.moveToFirst()) {
                do {
                    ContentValues value = new ContentValues();

                    value.put(FavorisDAO.NAME, cursor.getColumnIndex(FavorisDAO.NAME));
                    value.put(FavorisDAO.URL, cursor.getColumnIndex(FavorisDAO.URL));
                    value.put(FavorisDAO.GENRE, cursor.getColumnIndex(FavorisDAO.GENRE));
                    value.put(FavorisDAO.STATUS, cursor.getColumnIndex(FavorisDAO.STATUS));

                    db.insert(FavorisDAO.TABLE_NAME, null, value);

                } while (cursor.moveToNext());
            }
        }
    }

    private void onUpgradeBookmark(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("DB", "SAVE TABLE " + BookmarkDAO.TABLE_NAME);
        String METIER_TABLE_SELECT_ALL = "SELECT * FROM " + BookmarkDAO.TABLE_NAME + ";";

        Cursor cursor = null;
        try {
            cursor = db.rawQuery(METIER_TABLE_SELECT_ALL, null);
        } catch (Exception e) {
            cursor = null;
        }
        Log.e("DB", "DROP TABLE " + BookmarkDAO.TABLE_NAME);
        Log.e("DB", "-> " + BookmarkDAO.METIER_TABLE_DROP);
        db.execSQL(BookmarkDAO.METIER_TABLE_DROP);


        createTableBookmark(db);

        if (cursor != null) {
            Log.e("DB", "INSERT OLD RECORDS TABLE IN " + BookmarkDAO.TABLE_NAME);
            if (cursor.moveToFirst()) {
                do {
                    ContentValues value = new ContentValues();

                    value.put(BookmarkDAO.NAME_SERIE, cursor.getColumnIndex(BookmarkDAO.NAME_SERIE));
                    value.put(BookmarkDAO.NAME_CHAPTER, cursor.getColumnIndex(BookmarkDAO.NAME_CHAPTER));
                    value.put(BookmarkDAO.NAME_PAGE, cursor.getColumnIndex(BookmarkDAO.NAME_PAGE));
                    value.put(BookmarkDAO.URL, cursor.getColumnIndex(BookmarkDAO.URL));

                    db.insert(BookmarkDAO.TABLE_NAME, null, value);

                } while (cursor.moveToNext());
            }
        }
    }

}
