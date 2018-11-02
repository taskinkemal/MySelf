package com.keplersegg.myself.Room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.keplersegg.myself.Room.Dao.EntryDao;
import com.keplersegg.myself.Room.Dao.TaskDao;
import com.keplersegg.myself.Room.Entity.Task;

@Database(entities = {Task.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract TaskDao taskDao();
    public abstract EntryDao entryDao();

    public static AppDatabase getAppDatabase(Context context) {

        if (INSTANCE == null) {
            INSTANCE = Room
                    .databaseBuilder(context.getApplicationContext(), AppDatabase.class, "myself-database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
