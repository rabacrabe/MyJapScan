package japscan.gtheurillat.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import japscan.gtheurillat.db.DAOBase;
import japscan.gtheurillat.db.model.Bookmark;
import japscan.gtheurillat.db.model.Favoris;

/**
 * Created by gtheurillat on 17/07/2018.
 */

public class BookmarkDAO extends DAOBase{
    public static final String TABLE_NAME = "bookmark";
    public static final String KEY = "ID";
    public static final String NAME_SERIE = "NAME_SERIE";
    public static final String NAME_CHAPTER = "NAME_CHAPTER";
    public static final String NAME_PAGE = "NAME_PAGE";
    public static final String URL = "URL";

    public static final String METIER_TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
                                                        KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                        NAME_SERIE + " TEXT, " +
                                                        NAME_CHAPTER + " TEXT, " +
                                                        NAME_PAGE + " TEXT, " +
                                                        URL + " TEXT);";

    public static final String METIER_TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    public BookmarkDAO(Context pContext) {
        super(pContext);
    }

    /**
     * @param bm le métier à ajouter à la base
     */
    public void ajouter(Bookmark bm) {
        open();
        Log.e("DB", "ADD ENTRY "+TABLE_NAME+ " " + bm.getPageUrl() + " IN TABLE " +TABLE_NAME);

        ContentValues value = new ContentValues();

        value.put(BookmarkDAO.NAME_SERIE, bm.getSerieName());
        value.put(BookmarkDAO.NAME_CHAPTER, bm.getChapterName());
        value.put(BookmarkDAO.NAME_PAGE, bm.getPageNumber());
        value.put(BookmarkDAO.URL, bm.getPageUrl());


        mDb.insert(BookmarkDAO.TABLE_NAME, null, value);
        close();
    }

    /**
     * @param bm le métier à ajouter à la base
     */
    public void supprimer(Bookmark bm) {
        open();
        Log.e("DB", "DELETE ENTRY "+TABLE_NAME+ " " + bm.getId() + " IN TABLE " +TABLE_NAME);
        mDb.delete(TABLE_NAME, KEY + " = ?", new String[] {String.valueOf(bm.getId())});
        close();
    }

    /**
     * @param id l'id métier à ajouter à la base
     */
    public void supprimer(long id) {
        open();
        Log.e("DB", "DELETE ENTRY " + TABLE_NAME + " " + id + " IN TABLE " + TABLE_NAME);
        mDb.delete(TABLE_NAME, KEY + " = ?", new String[] {String.valueOf(id)});
        close();
    }

    /**
     * @param bm le métier modifié
     */
    public void modifier(Bookmark bm) {
        open();
        Log.e("DB", "UPDATE ENTRY " + bm.getId() + " IN TABLE " +TABLE_NAME);
        ContentValues value = new ContentValues();
        value.put(NAME_SERIE, bm.getSerieName());
        value.put(NAME_CHAPTER, bm.getChapterName());
        value.put(NAME_PAGE, bm.getPageNumber());
        value.put(URL, bm.getPageUrl());

        mDb.update(TABLE_NAME, value, KEY  + " = ?", new String[] {String.valueOf(bm.getId())});
        close();
    }

    /**
     * @param url l'identifiant du métier à récupérer
     */
    public Bookmark  selectionnerFromUrl(String url) {

        open();

        Log.e("DB", "GET ENTRY WITH URL " + url + " IN TABLE " +TABLE_NAME);
        Cursor cursor = mDb.rawQuery("select * from " + TABLE_NAME + " where url='"+url+"';", null);
        Bookmark bookmark = null;

        if (cursor.moveToFirst()) {
            bookmark = new Bookmark();
            bookmark.setId(cursor.getInt(cursor.getColumnIndex(KEY)));
            bookmark.setSerieName(cursor.getString(cursor.getColumnIndex(NAME_SERIE)));
            bookmark.setChapterName(cursor.getString(cursor.getColumnIndex(NAME_CHAPTER)));
            bookmark.setPageNumber(cursor.getString(cursor.getColumnIndex(NAME_PAGE)));
            bookmark.setPageUrl(cursor.getString(cursor.getColumnIndex(URL)));

        }

        close();
        return bookmark;
    }

    /**
     * @param serieName l'identifiant du métier à récupérer
     */
    public Bookmark  selectionnerFromSerie(String serieName) {

        open();

        Log.e("DB", "GET ENTRY WITH "+NAME_SERIE+"=" + serieName + " IN TABLE " +TABLE_NAME);
        Cursor cursor = mDb.rawQuery("select * from " + TABLE_NAME + " where "+NAME_SERIE+"='"+serieName+"';", null);
        Bookmark bookmark = null;

        if (cursor.moveToFirst()) {
            bookmark = new Bookmark();
            bookmark.setId(cursor.getInt(cursor.getColumnIndex(KEY)));
            bookmark.setSerieName(cursor.getString(cursor.getColumnIndex(NAME_SERIE)));
            bookmark.setChapterName(cursor.getString(cursor.getColumnIndex(NAME_CHAPTER)));
            bookmark.setPageNumber(cursor.getString(cursor.getColumnIndex(NAME_PAGE)));
            bookmark.setPageUrl(cursor.getString(cursor.getColumnIndex(URL)));

        }

        close();
        return bookmark;
    }



    public ArrayList<Bookmark>  selectionnerAll() {
        Log.e("DB", "GET ALL ENTRIES IN TABLE " +TABLE_NAME);
        open();
        Cursor cursor = mDb.rawQuery("select * from " + TABLE_NAME + ";", null);
        ArrayList<Bookmark> lstBookmark = new ArrayList<Bookmark>();

        if (cursor.moveToFirst()) {
            do {
                Bookmark bookmark = new Bookmark();
                bookmark.setId(cursor.getInt(cursor.getColumnIndex(KEY)));
                bookmark.setSerieName(cursor.getString(cursor.getColumnIndex(NAME_SERIE)));
                bookmark.setChapterName(cursor.getString(cursor.getColumnIndex(NAME_CHAPTER)));
                bookmark.setPageNumber(cursor.getString(cursor.getColumnIndex(NAME_PAGE)));
                bookmark.setPageUrl(cursor.getString(cursor.getColumnIndex(URL)));

                lstBookmark.add(bookmark);

            } while (cursor.moveToNext());
        }

        close();

        Log.e("DB", "NB ENTRIES = " + String.valueOf(lstBookmark.size()));

        return lstBookmark;
    }

}
