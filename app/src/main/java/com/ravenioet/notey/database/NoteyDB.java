package com.ravenioet.notey.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.ravenioet.notey.models.Note;

@Database(entities = {Note.class}, version = 2)
public abstract class NoteyDB extends RoomDatabase {
    private static NoteyDB instance;

    private static Context context;
    public static synchronized NoteyDB getInstance(Context context, String user_sec) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            NoteyDB.class, user_sec)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }

    public static void destroyInstance() {
        instance = null;
    }

    public abstract NoteDao noteDao();
}

