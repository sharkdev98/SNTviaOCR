package com.sharkdev98.smartnotetakerviaocr.DataLayer.Note;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.sharkdev98.smartnotetakerviaocr.DataLayer.RoomDb;

import java.util.List;

public class NoteRepository {

    private NoteDao mNoteDao;
    private LiveData<List<Note>> mAllNotes;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public NoteRepository(Application application ,  String user_email) {
        RoomDb db = RoomDb.getDatabase(application);
        mNoteDao = db.noteDao();
        mAllNotes = mNoteDao.getAllNotesByLatestDateTime(user_email);
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Note>> getAllNotes() {
        return mAllNotes;
    }


    public int updateNote(int NoteId, String Title, String Content){
        return mNoteDao.updateItem(NoteId, Title, Content);
    }

    public Note getNote(int note_id){return mNoteDao.selectItem(note_id);}

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(Note note) {
        RoomDb.databaseWriteExecutor.execute(() -> {
            mNoteDao.insert(note);
        });
    }

    public void remove(int position){
        RoomDb.databaseWriteExecutor.execute(()->{
            mNoteDao.removeItem(position);
        });
    }
}
