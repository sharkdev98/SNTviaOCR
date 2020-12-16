package com.example.sntviaocr.ApplicationLayer.Notes;

import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.example.sntviaocr.ApplicationLayer.BasicComponents.BasicActivityClass;
import com.example.sntviaocr.BussinessLayer.MLKit.MlKit;
import com.example.sntviaocr.BussinessLayer.RecyclerView.NoteViewHolder;
import com.example.sntviaocr.R;
import com.example.sntviaocr.databinding.ActivityEditViewOfNoteBinding;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.squareup.picasso.Picasso;

import java.io.File;

public class EditViewOfNote extends BasicActivityClass implements View.OnClickListener, MenuItem.OnMenuItemClickListener, Toolbar.OnMenuItemClickListener, View.OnLongClickListener {

    ActivityEditViewOfNoteBinding activityEditViewOfNoteBinding;
    public static String ACTIVITY_BUTTON_CLICKED_REPLY = "BUTTON";
    public static String ACTIVITY_EXTRACTED_TEXT_TITLE_REPLY = "TITLE";
    public static String ACTIVITY_EXTRACTED_TEXT_REPLY = "TEXT";
    public static String ACTIVITY_EXTRACTED_TEXT_DATE_TIME_REPLY = "DATE_TIME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityEditViewOfNoteBinding = ActivityEditViewOfNoteBinding.inflate(getLayoutInflater());
        setContentView(activityEditViewOfNoteBinding.getRoot());

        String s = mlKit.getMlExtractedText();

        activityEditViewOfNoteBinding.editNoteViewDateTimeStored.setText(cameraHandler.getDateTimeStringForDisplay());
        if(!s.isEmpty()) {
            s = s.replaceAll("\\n","");
            activityEditViewOfNoteBinding.editNoteViewTitle.setText(s.substring(0, (s.length() > MlKit.title_length) ? (MlKit.title_length) : (s.length() - 1)) + "...");
        }
        else {
            activityEditViewOfNoteBinding.editNoteViewTitle.setText("NOTHING CAPTURED");
        }
        activityEditViewOfNoteBinding.editNoteViewText.setText(mlKit.getMlExtractedText());
        Picasso.get().load(new File(cameraHandler.getCurrentPhotoPath())).into(activityEditViewOfNoteBinding.editNoteViewImageZoomageview);

        //OnClickListener
        activityEditViewOfNoteBinding.editNoteViewCancelButton.setOnClickListener(this);
        activityEditViewOfNoteBinding.editNoteViewSaveButton.setOnClickListener(this);

        //OnLongClickListener
        activityEditViewOfNoteBinding.editNoteViewTitle.setOnLongClickListener(this);
        activityEditViewOfNoteBinding.editNoteViewText.setOnLongClickListener(this);

        //OnMenuItemClickListener
        activityEditViewOfNoteBinding.editNoteToolbar.setOnMenuItemClickListener(this);
    }



    @Override
    public void UpdateUI(GoogleSignInAccount account) {

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
        switch(v.getId())
        {
            case R.id.edit_note_view_cancel_button: {
                Intent i = new Intent();
                i.putExtra(ACTIVITY_BUTTON_CLICKED_REPLY, R.id.edit_note_view_cancel_button);
                setResult(RESULT_CANCELED, i);
                finish();
                break;
            }

            case R.id.edit_note_view_save_button: {
                Intent i = new Intent();
                i.putExtra(ACTIVITY_BUTTON_CLICKED_REPLY, R.id.edit_note_view_save_button);
                i.putExtra(ACTIVITY_EXTRACTED_TEXT_TITLE_REPLY, activityEditViewOfNoteBinding.editNoteViewTitle.getText().toString());
                i.putExtra(ACTIVITY_EXTRACTED_TEXT_REPLY, activityEditViewOfNoteBinding.editNoteViewText.getText().toString());
                i.putExtra(ACTIVITY_EXTRACTED_TEXT_DATE_TIME_REPLY, activityEditViewOfNoteBinding.editNoteViewDateTimeStored.getText().toString());
                setResult(RESULT_OK, i);
                finish();
                break;
            }

            default:break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId()){
//            case R.id.edit_note_view_share_button:
//                break;

            case R.id.edit_note_view_speakup_button:
                break;

//            case R.id.edit_note_view_again_button:
//                break;

            default:break;
        }
        return false;
    }




    @Override
    public boolean onLongClick(View v) {
        switch(v.getId()){
            case R.id.edit_note_view_title:
            case R.id.edit_note_view_text:
                if(v.getClass() == EditText.class)
                {
                    ((EditText)v).setInputType(EditorInfo.TYPE_CLASS_TEXT);
                }
                break;

            default:break;
        }
        return false;
    }
}