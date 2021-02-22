package com.sharkdev98.smartnotetakerviaocr.DataLayer;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.sharkdev98.smartnotetakerviaocr.DataLayer.Note.Note;
import com.sharkdev98.smartnotetakerviaocr.DataLayer.Note.NoteDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Note.class}, version = 3, exportSchema = false)
public abstract class RoomDb extends RoomDatabase {

    public abstract NoteDao noteDao();

    private static volatile RoomDb INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.

                NoteDao noteDao = INSTANCE.noteDao();
                noteDao.deleteAll();
            });
        }
    };


    public static RoomDb getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (com.sharkdev98.smartnotetakerviaocr.DataLayer.RoomDb.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RoomDb.class, "room_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}