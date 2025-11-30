package com.example.mhike_native.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
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
        if (hike == null) {
            Log.e("DatabaseHelper", "Hike is null");
            return -1;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        long id = -1;

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, hike.getName());
            values.put(KEY_LOCATION, hike.getLocation());
            values.put(KEY_DATE, hike.getDate().toEpochDay());
            values.put(KEY_PARKING_AVAILABLE, hike.isParking_available() ? 1 : 0);
            values.put(KEY_LENGTH_KM, hike.getLength_km());
            values.put(KEY_DIFFICULTY, hike.getDifficulty());
            values.put(KEY_DESCRIPTION, hike.getDescription());

            id = db.insert(TABLE_HIKES, null, values);
            if (id == -1) {
                Log.e("DatabaseHelper", "Failed to insert hike");
            } else {
                Log.d("DatabaseHelper", "Hike added successfully with ID: " + id);
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error adding hike", e);
        }

        return id;
    }

    public long addObservation(Observation observation) {
        if (observation == null) {
            Log.e("DatabaseHelper", "Observation is null");
            return -1;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        long id = -1;

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TITLE, observation.getTitle());
            values.put(KEY_TIMESTAMP, observation.getTimestamp().toEpochSecond(ZoneOffset.UTC));
            values.put(KEY_COMMENTS, observation.getComments());
            values.put(KEY_HIKE_ID, observation.getHike_id());

            id = db.insert(TABLE_OBSERVATIONS, null, values);
            if (id == -1) {
                Log.e("DatabaseHelper", "Failed to insert observation");
            } else {
                Log.d("DatabaseHelper", "Observation added successfully with ID: " + id);
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error adding observation", e);
        }

        return id;
    }

    public List<Hike> getAllHikes() {
        List<Hike> hikeList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_HIKES;
        SQLiteDatabase db = this.getReadableDatabase();

        try (Cursor cursor = db.rawQuery(selectQuery, null)) {

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
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error retrieving hikes", e);
        }

        return hikeList;
    }

    public List<Observation> getAllObservationsByHikeId(long hikeId) {
        List<Observation> observationList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_OBSERVATIONS + " WHERE " + KEY_HIKE_ID + " = " + hikeId;
        SQLiteDatabase db = this.getReadableDatabase();

        try (Cursor cursor = db.rawQuery(selectQuery, null)) {
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
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error retrieving observations", e);
        }

        return observationList;
    }

    public Hike getHikeById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_HIKES + " WHERE " + KEY_ID + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(id)});
        Hike hike = new Hike();

        try {
            if (cursor.moveToFirst()) {
                hike.setId(cursor.getLong(0));
                hike.setName(cursor.getString(1));
                hike.setLocation(cursor.getString(2));
                hike.setDate(LocalDate.ofEpochDay(cursor.getLong(3)));
                hike.setParking_available(cursor.getInt(4) == 1);
                hike.setLength_km(cursor.getDouble(5));
                hike.setDifficulty(cursor.getString(6));
                hike.setDescription(cursor.getString(7));
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error retrieving hike by ID", e);
        } finally {
            cursor.close();
        }

        return hike;
    }

    public Observation getObservationById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " +TABLE_OBSERVATIONS + " WHERE " + KEY_ID + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(id)});
        Observation observation = new Observation();

        try {
            if (cursor.moveToFirst()) {
                observation.setId(cursor.getLong(0));
                observation.setTitle(cursor.getString(1));
                observation.setTimestamp(LocalDateTime.ofEpochSecond(cursor.getLong(2), 0, ZoneOffset.UTC));
                observation.setComments(cursor.getString(3));
                observation.setHike_id(cursor.getInt(4));
            }
        } catch (SQLException e) {
            Log.e("DatabaseHelper", "Error retrieving observation by ID", e);
        } finally {
            cursor.close();
        }

        return observation;
    }

    public Hike getHikeNameAndDateByHikeId(long hikeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT " + KEY_NAME + ", " + KEY_DATE + " FROM " + TABLE_HIKES + " WHERE " + KEY_ID + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(hikeId)});

        Hike hike = new Hike();

        try {
            if (cursor.moveToFirst()) {
                hike.setName(cursor.getString(0));
                hike.setDate(LocalDate.ofEpochDay(cursor.getLong(1)));
            }
        } catch (SQLException e) {
            Log.e("DatabaseHelper", "Error retrieving hike name and date by hike ID", e);
        } finally {
            cursor.close();
        }

        return hike;
    }

    public long updateHike(Hike hike) {
        if (hike == null) {
            Log.e("DatabaseHelper", "Hike is null");
            return -1;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        long id = -1;

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, hike.getName());
            values.put(KEY_LOCATION, hike.getLocation());
            values.put(KEY_DATE, hike.getDate().toEpochDay());
            values.put(KEY_PARKING_AVAILABLE, hike.isParking_available() ? 1 : 0);
            values.put(KEY_LENGTH_KM, hike.getLength_km());
            values.put(KEY_DIFFICULTY, hike.getDifficulty());
            values.put(KEY_DESCRIPTION, hike.getDescription());

            id = db.update(TABLE_HIKES, values, KEY_ID + " = ?", new String[]{String.valueOf(hike.getId())});
            if (id == -1) {
                Log.e("DatabaseHelper", "Failed to update hike");
            } else {
                Log.d("DatabaseHelper", "Hike updated successfully with ID: " + id);
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error updating hike", e);
        }

        return id;
    }

    public long updateObservation(Observation observation) {
        if (observation == null) {
            Log.e("DatabaseHelper", "Observation is null");
            return -1;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        long id = -1;

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TITLE, observation.getTitle());
            values.put(KEY_TIMESTAMP, observation.getTimestamp().toEpochSecond(ZoneOffset.UTC));
            values.put(KEY_COMMENTS, observation.getComments());
            values.put(KEY_HIKE_ID, observation.getHike_id());

            id = db.update(TABLE_OBSERVATIONS, values, KEY_ID + " = ?", new String[]{String.valueOf(observation.getId())});
            if (id == -1) {
                Log.e("DatabaseHelper", "Failed to update observation");
            } else {
                Log.d("DatabaseHelper", "Observation updated successfully with ID: " + id);
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error updating observation", e);
        }

        return id;
    }

    public long deleteHike(long id) {
        if (id == -1) {
            Log.e("DatabaseHelper", "Invalid hike ID");
            return -1;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        long deletedHikeId = -1;
        try {
            deletedHikeId = db.delete(TABLE_HIKES, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error deleting hike", e);
        }

        return deletedHikeId;
    }

    public long deleteObservation(long id) {
        if (id == -1) {
            Log.e("DatabaseHelper", "Invalid observation ID");
            return -1;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        long deletedObservationId = -1;

        try {
            deletedObservationId = db.delete(TABLE_OBSERVATIONS, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error deleting observation", e);
        }

        return deletedObservationId;
    }

    public void deleteAllHikes() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(TABLE_HIKES, null, null);
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error deleting all hikes", e);
        }
    }

    public List<Hike> searchHikes(String name, String location, String date, Double minLength, Double maxLength, String difficulty, Boolean parkingAvailable) {
        List<Hike> hikeList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_HIKES + " WHERE 1=1";

        if (name != null && !name.isEmpty()) {
            selectQuery += " AND " + KEY_NAME + " LIKE '%" + name + "%'";
        }

        if (location != null && !location.isEmpty()) {
            selectQuery += " AND " + KEY_LOCATION + " LIKE '%" + location + "%'";
        }

        if (date != null && !date.isEmpty()) {
            LocalDate localDate = LocalDate.parse(date);
            long epochDay = localDate.toEpochDay();
            selectQuery += " AND " + KEY_DATE + " = " + epochDay;
        }

        if (minLength != null) {
            selectQuery += " AND " + KEY_LENGTH_KM + " >= " + minLength;
        }

        if (maxLength != null) {
            selectQuery += " AND " + KEY_LENGTH_KM + " <= " + maxLength;
        }

        if (difficulty != null && !difficulty.isEmpty()) {
            selectQuery += " AND " + KEY_DIFFICULTY + " = '" + difficulty + "'";
        }

        if (parkingAvailable != null) {
            selectQuery += " AND " + KEY_PARKING_AVAILABLE + " = " + (parkingAvailable ? 1 : 0);
        }

        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.rawQuery(selectQuery, null)) {
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
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error searching hikes", e);
        }

        return hikeList;
    }
}
