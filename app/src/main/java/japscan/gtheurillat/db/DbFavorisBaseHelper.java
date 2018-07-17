package japscan.gtheurillat.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by gtheurillat on 17/07/2018.
 */

public class DbFavorisBaseHelper  extends SQLiteOpenHelper{

    public static final String TABLE_NAME = "favoris";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "URL";
    public static final String COL_4 = "GENRE";
    public static final String COL_5 = "STATUS";

    /*
    public DbFavorisBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
*/

    public DbFavorisBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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
        Log.e("DB", "DROP TABLE " + TABLE_NAME);

        String METIER_TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

        db.execSQL(METIER_TABLE_DROP);

        onCreate(db);

    }

}
