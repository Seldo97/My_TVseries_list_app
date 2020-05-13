package pl.kalisz.pwsz.pup.marcin.apkamarcin27482;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProjektSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Projekt";
    private static final Integer DB_VERSION = 1;

    ProjektSQLiteOpenHelper(Context context)
    {
        super(context,DB_NAME,null,DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db,0,DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db,oldVersion,newVersion);
    }

    private static void  wstawSerial(SQLiteDatabase db, String nazwa, Integer sezon, String serwis, String kategoria, String ocena, Integer odcinki, Integer aktualnyOdc, Integer ulubiony)
    {
        ContentValues obiektValues =  new ContentValues();
        obiektValues.put("Nazwa",nazwa);
        obiektValues.put("Sezon",sezon);
        obiektValues.put("Serwis",serwis);
        obiektValues.put("Kategoria",kategoria);
        obiektValues.put("Ocena",ocena);
        obiektValues.put("Odcinki",odcinki);
        obiektValues.put("Aktualny_odc",aktualnyOdc);
        obiektValues.put("Ulubiony",ulubiony);
        db.insert("SERIAL",null,obiektValues);
    }

    private  void  updateMyDatabase(SQLiteDatabase db, int oldVersion,int  newVersion)
    {
        if(oldVersion < 1)
        {
            db.execSQL("CREATE TABLE SERIAL (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "NAZWA TEXT, " +
                    "SEZON INTEGER, " +
                    "SERWIS TEXT, " +
                    "KATEGORIA TEXT, " +
                    "OCENA TEXT, " +
                    "ODCINKI INTEGER, " +
                    "AKTUALNY_ODC INTEGER, " +
                    "ULUBIONY INTEGER);");
            wstawSerial(db,"DareDevil", 1,"Netflix", "Planowane", "--", 13, 0, 0);
            wstawSerial(db,"Your Lie in April", 1,"Netflix", "Planowane", "--", 24, 0, 0);
            wstawSerial(db,"The Boys", 1,"Amazon", "Planowane", "--", 13, 0, 1);
        }
    }
}