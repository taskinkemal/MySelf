package com.keplersegg.myself.Room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.keplersegg.myself.Room.Dao.EntryDao;
import com.keplersegg.myself.Room.Dao.TaskDao;
import com.keplersegg.myself.Room.Dao.TaskEntryDao;
import com.keplersegg.myself.Room.Entity.Entry;
import com.keplersegg.myself.Room.Entity.Task;
import com.keplersegg.myself.Room.Entity.TaskEntry;

@Database(entities = {Task.class, Entry.class, TaskEntry.class}, version = 6, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract TaskDao taskDao();
    public abstract EntryDao entryDao();
    public abstract TaskEntryDao taskEntryDao();

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
