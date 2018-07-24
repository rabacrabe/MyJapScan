package japscan.gtheurillat.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by gtheurillat on 17/07/2018.
 */

public abstract class DAOBase {
    // Nous sommes à la première version de la base
    // Si je décide de la mettre à jour, il faudra changer cet attribut
    protected final static int VERSION = 12;
    // Le nom du fichier qui représente ma base
    protected final static String NOM = "japscan.db";

    protected SQLiteDatabase mDb = null;
    //protected DbFavorisBaseHelper mHandler = null;
    protected DbBaseHelper mHandler = null;

    public DAOBase(Context pContext) {
        //this.mHandler = new DbFavorisBaseHelper(pContext, NOM, null, VERSION);
        this.mHandler = new DbBaseHelper(pContext, NOM, null, VERSION);
    }

    public SQLiteDatabase open() {
        // Pas besoin de fermer la dernière base puisque getWritableDatabase s'en charge
        Log.e("DB", "GET WRITABLE DATABASE");
        mDb = mHandler.getWritableDatabase();
        return mDb;
    }

    public void close() {
        mDb.close();
    }

    public SQLiteDatabase getDb() {
        return mDb;
    }
}
