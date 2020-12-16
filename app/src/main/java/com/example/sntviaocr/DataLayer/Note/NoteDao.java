package com.example.sntviaocr.DataLayer.Note;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert(entity = Note.class)
    void insert(Note note);

    @Query("DELETE FROM note_table WHERE NoteId LIKE :note_id")
    void removeItem(int note_id);

    @Query("SELECT * FROM note_table WHERE NoteId = :note_id")
    Note selectItem(int note_id);

    @Query("UPDATE note_table SET Title = :Title, Content = :Content WHERE NoteId = :NoteId")
    int updateItem(int NoteId, String Title, String Content);

    @Query("DELETE FROM note_table")
    void deleteAll();

    @Query("SELECT * FROM note_table WHERE user_email = :user_email ORDER BY NoteId DESC")
    LiveData<List<Note>> getAllNotesByLatestDateTime(String user_email);

    @Query("SELECT * FROM note_table WHERE user_email = :user_email  ORDER BY Title ASC")
    LiveData<List<Note>> getAllNotesByAlphabetically(String user_email);

}
