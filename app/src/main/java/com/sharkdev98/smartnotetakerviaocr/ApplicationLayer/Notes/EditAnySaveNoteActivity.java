package com.sharkdev98.smartnotetakerviaocr.ApplicationLayer.Notes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.sharkdev98.smartnotetakerviaocr.ApplicationLayer.BasicComponents.BasicActivityClass;
import com.sharkdev98.smartnotetakerviaocr.BussinessLayer.RecyclerView.NoteViewHolder;
import com.sharkdev98.smartnotetakerviaocr.DataLayer.Note.Note;
import com.sharkdev98.smartnotetakerviaocr.R;
import com.sharkdev98.smartnotetakerviaocr.databinding.ActivityEditAnySaveNoteBinding;
import com.squareup.picasso.Picasso;

import java.io.File;

public class EditAnySaveNoteActivity extends AppCompatActivity implements View.OnClickListener, Toolbar.OnMenuItemClickListener {

    private int NoteId;
    private Note n = null;
    private ActivityEditAnySaveNoteBinding activityEditAnySaveNoteBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityEditAnySaveNoteBinding = ActivityEditAnySaveNoteBinding.inflate(getLayoutInflater());
        setContentView(activityEditAnySaveNoteBinding.getRoot());
        NoteId = getIntent().getIntExtra("NoteId", 0);
        n = new Note("", "", "", "", "", "");
        n = (Note) getIntent().getSerializableExtra("ClickedNote");
        activityEditAnySaveNoteBinding.editNoteViewCancelButton.setOnClickListener(this);
        activityEditAnySaveNoteBinding.editNoteViewSaveButton.setOnClickListener(this);
        activityEditAnySaveNoteBinding.editNoteToolbar.setOnMenuItemClickListener(this::onMenuItemClick);
        activityEditAnySaveNoteBinding.editNoteViewDateTimeStored.setText(n.CapturedImageDateTime);
        activityEditAnySaveNoteBinding.editNoteViewTitle.setText(n.Title);
        activityEditAnySaveNoteBinding.editNoteViewText.setText(n.Content);
        Picasso.get().load(new File(n.CapturedImagePath)).into(activityEditAnySaveNoteBinding.editNoteViewImageZoomageview);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(NoteViewHolder.speaking == true)
        {
            BasicActivityClass.stopSpeaking();
        }
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent();

        switch(v.getId()){
            case R.id.edit_note_view_cancel_button:
                Log.i("EDIT:", "Don't Edit");
                setResult(RESULT_CANCELED,i);
                break;

            case R.id.edit_note_view_save_button:
//                BasicActivityClass.mNoteViewModel.updateNote(
//                        NoteId,
//                        activityEditAnySaveNoteBinding.editNoteViewTitle.getText().toString(),
//                        activityEditAnySaveNoteBinding.editNoteViewText.getText().toString());
                n.Title = activityEditAnySaveNoteBinding.editNoteViewTitle.getText().toString();
                n.Content = activityEditAnySaveNoteBinding.editNoteViewText.getText().toString();
                i.putExtra("EditedNote", n);
                setResult(RESULT_OK,i);
                break;

            default:break;
        }
        finish();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit_note_view_speakup_button:
                if(NoteViewHolder.speaking == false)
                {
                    NoteViewHolder.speaking = true;
                    BasicActivityClass.speak(activityEditAnySaveNoteBinding.editNoteViewText.getText().toString());
                }
                else
                {
                    NoteViewHolder.speaking = false;
                    BasicActivityClass.stopSpeaking();
                }
                break;

            default:break;
        }
        return false;
    }
}
