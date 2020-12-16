package com.example.sntviaocr.BussinessLayer.RecyclerView;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.sntviaocr.DataLayer.Note.Note;

public class NoteListAdapter extends ListAdapter<Note, NoteViewHolder> {

    public NoteListAdapter(@NonNull DiffUtil.ItemCallback<Note> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return NoteViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note current = getItem(position);
        holder.bind(current.NoteId, current.Title , current.Content, current.CapturedImagePath , current.CapturedImageThumbnailPath, current.CapturedImageDateTime);
    }

    public void notifyAdapterDataSetChanged(){
        this.notifyDataSetChanged();
    }

    public static class NoteDiff extends DiffUtil.ItemCallback<Note> {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
//            return oldItem == newItem;
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
//            return oldItem.Title.equals(newItem.Title);
            return false;
        }
    }
}
