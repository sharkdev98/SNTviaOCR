package com.example.sntviaocr.BussinessLayer.ViewModels;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.sntviaocr.ApplicationLayer.BasicComponents.BasicActivityClass;
import com.example.sntviaocr.DataLayer.Note.Note;
import com.example.sntviaocr.DataLayer.Note.NoteRepository;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository mRepository;

    private final LiveData<List<Note>> mAllNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        mRepository = new NoteRepository(application , BasicActivityClass.email_address);
        mAllNotes = mRepository.getAllNotes();
    }

    public LiveData<List<Note>> getAllNotes() { return mAllNotes; }

    public int updateNote(int NoteId, String Title, String Content){
        return mRepository.updateNote(NoteId, Title, Content);
    }

    public Note getNote(int note_id){return mRepository.getNote(note_id);}

    public void insert(Note note) { mRepository.insert(note); }

    public void remove(int position) {
        mRepository.remove(position);
    }
}