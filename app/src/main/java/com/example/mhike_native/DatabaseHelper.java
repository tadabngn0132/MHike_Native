package com.example.mhike_native;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mhike_native.models.Hike;
import com.example.mhike_native.models.Observation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MHikeDB.db";
    private static final int DATABASE_VERSION = 1;

    // Hike table
    private static final String TABLE_HIKES = "hikes";
    // Hike table columns
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_DATE = "date";
    private static final String KEY_PARKING_AVAILABLE = "parking_available";
    private static final String KEY_LENGTH_KM = "length_km";
    private static final String KEY_DIFFICULTY = "difficulty";
    private static final String KEY_DESCRIPTION = "description";

    // Observation table
    private static final String TABLE_OBSERVATIONS = "observations";
    // Observation table columns
    private static final String KEY_TITLE = "title";
    private static final String KEY_TIMESTAMP = "timestamp";
    private static final String KEY_COMMENTS = "comments";
    private static final String KEY_HIKE_ID = "hike_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_HIKES_TABLE = "CREATE TABLE " + TABLE_HIKES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + KEY_LOCATION + " TEXT,"
                + KEY_DATE + " INTEGER,"
                + KEY_PARKING_AVAILABLE + " INTEGER,"
                + KEY_LENGTH_KM + " REAL,"
                + KEY_DIFFICULTY + " TEXT,"
                + KEY_DESCRIPTION + " TEXT" + ")";
        db.execSQL(CREATE_HIKES_TABLE);

        String CREATE_OBSERVATIONS_TABLE = "CREATE TABLE " + TABLE_OBSERVATIONS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TITLE + " TEXT,"
                + KEY_TIMESTAMP + " INTEGER,"
                + KEY_COMMENTS + " TEXT,"
                + KEY_HIKE_ID + " INTEGER,"
                + "FOREIGN KEY(" + KEY_HIKE_ID + ") REFERENCES " + TABLE_HIKES + "(" + KEY_ID + "))";
        db.execSQL(CREATE_OBSERVATIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIKES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBSERVATIONS);
        onCreate(db);
    }

    public long addHike(Hike hike) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, hike.getName());
        values.put(KEY_LOCATION, hike.getLocation());
        values.put(KEY_DATE, hike.getDate().toEpochDay());
        values.put(KEY_PARKING_AVAILABLE, hike.isParking_available() ? 1 : 0);
        values.put(KEY_LENGTH_KM, hike.getLength_km());
        values.put(KEY_DIFFICULTY, hike.getDifficulty());
        values.put(KEY_DESCRIPTION, hike.getDescription());

        long id = db.insert(TABLE_HIKES, null, values);
        db.close();
        return id;
    }

    public long addObservation(Observation observation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, observation.getTitle());
        values.put(KEY_TIMESTAMP, observation.getTimestamp().toEpochSecond(ZoneOffset.UTC));
        values.put(KEY_COMMENTS, observation.getComments());
        values.put(KEY_HIKE_ID, observation.getHike_id());

        long id = db.insert(TABLE_OBSERVATIONS, null, values);
        db.close();
        return id;
    }

    public List<Hike> getAllHikes() {
        List<Hike> hikeList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_HIKES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Hike hike = new Hike();
                hike.setId(cursor.getLong(0));
                hike.setName(cursor.getString(1));
                hike.setLocation(cursor.getString(2));
                hike.setDate(LocalDate.ofEpochDay(cursor.getLong(3)));
                hike.setParking_available(cursor.getInt(4) == 1);
                hike.setLength_km(cursor.getDouble(5));
                hike.setDifficulty(cursor.getString(6));
                hike.setDescription(cursor.getString(7));
                hikeList.add(hike);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return hikeList;
    }

    public List<Observation> getAllObservations() {
        List<Observation> observationList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_OBSERVATIONS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Observation observation = new Observation();
                observation.setId(cursor.getLong(0));
                observation.setTitle(cursor.getString(1));
                observation.setTimestamp(LocalDateTime.ofEpochSecond(cursor.getLong(2), 0, ZoneOffset.UTC));
                observation.setComments(cursor.getString(3));
                observation.setHike_id(cursor.getInt(4));
                observationList.add(observation);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return observationList;
    }

    public long updateHike(Hike hike) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, hike.getName());
        values.put(KEY_LOCATION, hike.getLocation());
        values.put(KEY_DATE, hike.getDate().toEpochDay());
        values.put(KEY_PARKING_AVAILABLE, hike.isParking_available() ? 1 : 0);
        values.put(KEY_LENGTH_KM, hike.getLength_km());
        values.put(KEY_DIFFICULTY, hike.getDifficulty());
        values.put(KEY_DESCRIPTION, hike.getDescription());

        long id = db.update(TABLE_HIKES, values, KEY_ID + " = ?", new String[]{String.valueOf(hike.getId())});
        db.close();
        return id;
    }

    public long updateObservation(Observation observation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, observation.getTitle());
        values.put(KEY_TIMESTAMP, observation.getTimestamp().toEpochSecond(ZoneOffset.UTC));
        values.put(KEY_COMMENTS, observation.getComments());
        values.put(KEY_HIKE_ID, observation.getHike_id());

        long id = db.update(TABLE_OBSERVATIONS, values, KEY_ID + " = ?", new String[]{String.valueOf(observation.getId())});
        db.close();
        return id;
    }

    public void deleteHike(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HIKES, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        db.delete(TABLE_OBSERVATIONS, KEY_HIKE_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteObservation(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_OBSERVATIONS, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}
