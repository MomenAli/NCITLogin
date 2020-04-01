package com.eng.momen.ncitlogin.user;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

@Database(entities = {User.class},version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "USER_INFO";
    private static AppDatabase sInstance;


    public static AppDatabase getsInstance(Context context) {
        if (sInstance == null){
            synchronized (LOCK){
                Log.d(TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        // add this line for testing only
                        // remove it before release
                        .allowMainThreadQueries()
                        .build();

            }
        }
        Log.d(TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract UserDao userDao();
}
