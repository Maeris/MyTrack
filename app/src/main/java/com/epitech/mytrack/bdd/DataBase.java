package com.epitech.mytrack.bdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.epitech.mytrack.Track;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Thomas on 22/02/2015.
 */
public class DataBase extends SQLiteOpenHelper {
    private SQLiteDatabase bdd;
    public static final int BDD_VERSION = 1;
    public static final String BDD_NAME = "My_Track_BDD";

    public DataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        bdd = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Historique_Activite ( Duree LONG , Distance Long, Vitesse_Max LONG, Vitesse_Moyenne LONG, Lat_Debut LONG, Lng_Debut LONG, Lat_Fin LONG, Lng_Fin LONG, Date String );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void putNewActivite(Track track) {
        ContentValues values = new ContentValues();
        values.put("Duree", track.getTime());
        values.put("Distance", track.getDistance());
        values.put("Vitesse_Max", track.getVitMax());
        values.put("Vitesse_Moyenne", track.getVitMoyenne());
        values.put("Lat_Debut", track.getStartPoint().latitude);
        values.put("Lng_Debut", track.getStartPoint().longitude);
        values.put("Lat_Fin", track.getEndPoint().latitude);
        values.put("Lng_Fin", track.getEndPoint().longitude);
        values.put("Date", track.getDate());
        bdd.insert("Historique_Activite", null, values);
    }

    public ArrayList<Track> getActivites() {
        ArrayList<Track> list = new ArrayList<>();
        Track track;
        Cursor c = bdd.query("Historique_Activite", new String[]{ "Duree", "Distance", "Vitesse_Max", "Vitesse_Moyenne", "Lat_Debut", "Lng_Debut", "Lat_Fin", "Lng_Fin", "Date" }, null, null, null, null, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            while (c.isAfterLast() != true) {
                track = new Track(c.getDouble(3), c.getDouble(2), c.getDouble(1), c.getDouble(0), new LatLng(c.getLong(4), c.getLong(5)), new LatLng(c.getLong(6), c.getLong(7)), c.getString(8));
                list.add(track);
                c.moveToNext();
            }
        }
        return list;
    }

    public void deleteTrack(Track track) {
        bdd.delete("Historique_Activite", "Duree = " + track.getTime() + "Distance = " + track.getDistance() + "Vitesse_Max = " + track.getVitMax() + "Vitesse_Moyenne = " + track.getVitMoyenne() + "Lat_Debut = " + track.getStartPoint().latitude + "Lng_Debut = " + track.getStartPoint().longitude + "Lat_Fin = " + track.getEndPoint().latitude + "Lng_Fin = " + track.getEndPoint().longitude + "Date = " + track.getDate(), null);
    }

    public void clearHistoriqueActivite() {
        bdd.delete("Historique_Activite", null, null);
    }
}
