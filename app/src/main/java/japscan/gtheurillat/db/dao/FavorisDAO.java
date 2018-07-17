package japscan.gtheurillat.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import japscan.gtheurillat.db.DAOBase;
import japscan.gtheurillat.db.model.Favoris;

/**
 * Created by gtheurillat on 17/07/2018.
 */

public class FavorisDAO extends DAOBase{
    public static final String TABLE_NAME = "favoris";
    public static final String KEY = "ID";
    public static final String NAME = "NAME";
    public static final String URL = "URL";
    public static final String GENRE = "GENRE";
    public static final String STATUS = "STATUS";

    public static final String METIER_TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
                                                        KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                        NAME + " TEXT, " +
                                                        URL + " TEXT, " +
                                                        GENRE + " TEXT, " +
                                                        STATUS + " TEXT);";

    public static final String METIER_TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    public FavorisDAO(Context pContext) {
        super(pContext);
    }

    /**
     * @param fav le métier à ajouter à la base
     */
    public void ajouter(Favoris fav) {
        /*
        SQLiteDatabase db = this.getDb();

        String METIER_TABLE_AJOUT = "INSERT INTO " + TABLE_NAME + " ("+NAME+", "+URL+") VALUES ('"+fav.getName()+"', '"+fav.getUrl()+"');";
        db.execSQL(METIER_TABLE_AJOUT);
        */
        open();
        Log.e("DB", "ADD ENTRY FAVORIS " + fav.getUrl() + " IN TABLE " +TABLE_NAME);

        ContentValues value = new ContentValues();

        value.put(FavorisDAO.NAME, fav.getName());
        value.put(FavorisDAO.URL, fav.getUrl());
        value.put(FavorisDAO.STATUS, fav.getStatus());
        value.put(FavorisDAO.GENRE, fav.getGenre());



        mDb.insert(FavorisDAO.TABLE_NAME, null, value);
        close();
    }

    /**
     * @param fav le métier à ajouter à la base
     */
    public void supprimer(Favoris fav) {
        open();
        Log.e("DB", "DELETE ENTRY FAVORIS " + fav.getId() + " IN TABLE " +TABLE_NAME);
        mDb.delete(TABLE_NAME, KEY + " = ?", new String[] {String.valueOf(fav.getId())});
        close();
    }

    /**
     * @param id l'id métier à ajouter à la base
     */
    public void supprimer(long id) {
        open();
        Log.e("DB", "DELETE ENTRY FAVORIS " + id + " IN TABLE " +TABLE_NAME);
        mDb.delete(TABLE_NAME, KEY + " = ?", new String[] {String.valueOf(id)});
        close();
    }

    /**
     * @param fav le métier modifié
     */
    public void modifier(Favoris fav) {
        open();
        Log.e("DB", "UPDATE ENTRY FAVORIS " + fav.getId() + " IN TABLE " +TABLE_NAME);
        ContentValues value = new ContentValues();
        value.put(NAME, fav.getName());
        value.put(URL, fav.getUrl());
        value.put(STATUS, fav.getStatus());
        value.put(GENRE, fav.getGenre());

        mDb.update(TABLE_NAME, value, KEY  + " = ?", new String[] {String.valueOf(fav.getId())});
        close();
    }

    /**
     * @param url l'identifiant du métier à récupérer
     */
    public Favoris  selectionner(String url) {

        open();

        Log.e("DB", "GET ENTRY FAVORIS WITH URL " + url + " IN TABLE " +TABLE_NAME);
        Cursor cursor = mDb.rawQuery("select * from " + TABLE_NAME + " where url='"+url+"';", null);
        Favoris favoris = null;

        if (cursor.moveToFirst()) {
            favoris = new Favoris();
            favoris.setId(cursor.getInt(cursor.getColumnIndex(KEY)));
            favoris.setName(cursor.getString(cursor.getColumnIndex(NAME)));
            favoris.setUrl(cursor.getString(cursor.getColumnIndex(URL)));
            favoris.setGenre(cursor.getString(cursor.getColumnIndex(GENRE)));
            favoris.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
        }

        close();
        return favoris;
    }

    public ArrayList<Favoris>  selectionnerAll() {
        Log.e("DB", "GET ALL ENTRY FAVORIS IN TABLE " +TABLE_NAME);
        open();
        Cursor cursor = mDb.rawQuery("select * from " + TABLE_NAME + ";", null);
        ArrayList<Favoris> lstFavoris = new ArrayList<Favoris>();

        if (cursor.moveToFirst()) {
            do {
                Favoris favoris = new Favoris();
                favoris.setId(cursor.getInt(cursor.getColumnIndex(KEY)));
                favoris.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                favoris.setUrl(cursor.getString(cursor.getColumnIndex(URL)));
                favoris.setGenre(cursor.getString(cursor.getColumnIndex(GENRE)));
                favoris.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));

                lstFavoris.add(favoris);

            } while (cursor.moveToNext());
        }

        close();

        Log.e("DB", "NB ENTRIES = " + String.valueOf(lstFavoris.size()));

        return lstFavoris;
    }

}
