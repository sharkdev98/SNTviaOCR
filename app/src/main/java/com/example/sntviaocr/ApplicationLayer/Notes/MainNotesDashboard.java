package com.example.sntviaocr.ApplicationLayer.Notes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.example.sntviaocr.ApplicationLayer.BasicComponents.BasicActivityClass;
import com.example.sntviaocr.ApplicationLayer.BasicComponents.ICallback;
import com.example.sntviaocr.ApplicationLayer.StartUpAndSignIn.SplashScreen;
import com.example.sntviaocr.BussinessLayer.CameraHandler.CameraHandler;
import com.example.sntviaocr.BussinessLayer.MLKit.MlKit;
import com.example.sntviaocr.BussinessLayer.RecyclerView.NoteListAdapter;
import com.example.sntviaocr.BussinessLayer.RecyclerView.NoteViewHolder;
import com.example.sntviaocr.BussinessLayer.ViewModels.NoteViewModel;
import com.example.sntviaocr.DataLayer.Note.Note;
import com.example.sntviaocr.R;
import com.example.sntviaocr.databinding.ActivityMainNotesDashboardBinding;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.navigation.NavigationView;
import com.google.mlkit.vision.common.InputImage;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainNotesDashboard extends BasicActivityClass implements View.OnClickListener, MenuItem.OnMenuItemClickListener, NavigationView.OnNavigationItemSelectedListener, ICallback {

    public static final int NEW_NOTE_ACTIVITY_REQUEST_CODE = 2;
    private static final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 101;
    private final int EDIT_NOTE_REQUEST = 102;
    private ActivityMainNotesDashboardBinding thisActivity;
    final int thumbSize = 128;
    private Observer<Integer> o;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        childActivityMustRequireInit(this);
        super.onCreate(savedInstanceState);
        thisActivity = ActivityMainNotesDashboardBinding.inflate(getLayoutInflater());
        setContentView(thisActivity.getRoot());

        recyclerView = findViewById(R.id.notes_list_recyclerview);
        noteListAdapter = new NoteListAdapter(new NoteListAdapter.NoteDiff());
        recyclerView.setAdapter(noteListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mNoteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);


        mNoteViewModel.getAllNotes()
                        .observe(this, notes -> {
                            // Update the cached copy of the words in the adapter.
                            noteListAdapter.submitList(notes);
                        });


        Observer<Integer> o = new Observer<Integer>() {
            @Override
            public void onChanged(Integer note_id) {
                if(observerCounter > 0){
                    Log.i("MUTEABLELIVEDATA", "INTEGER CHANGED : " + note_id);
                    Intent i = new Intent(MainNotesDashboard.this, EditAnySaveNoteActivity.class);
                    i.putExtra("NoteId", BasicActivityClass.NoteIdClicked.getValue());
                    i.putExtra("ClickedNote", ClickedNote);
                    startActivityForResult(i,EDIT_NOTE_REQUEST);
                }
                observerCounter++;
            }
        };

        NoteIdClicked.observe(this, o);


        thisActivity.drawerNavigationMenu.setNavigationItemSelectedListener(this);

        thisActivity.disconnectButton.setOnClickListener(this);
        thisActivity.logoutButton.setOnClickListener(this);
        thisActivity.takePhotoFabButton.setOnClickListener(this);
//        thisActivity.bottomAppBar.getMenu().findItem(R.id.rescan_photo_button).setOnMenuItemClickListener(this);
        thisActivity.bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thisActivity.drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(NoteViewHolder.speaking == true)
        {
            BasicActivityClass.stopSpeaking();
        }
    }


    public static String[] getStringArray(ArrayList<String> arr)
    {

        // declaration and initialise String Array
        String str[] = new String[arr.size()];

        // ArrayList to Array Conversion
        for (int j = 0; j < arr.size(); j++) {

            // Assign each value to String array
            str[j] = arr.get(j);
        }

        return str;
    }

    @Override
    protected void onStart() {
        super.onStart();
        childActivityMustRequireInit(this);
        UpdateUI(account);
    }



    @Override
    public void UpdateUI(GoogleSignInAccount account) {
        if(account != null) {
        }
        else{
            Intent mainToSignInIntent = new Intent(MainNotesDashboard.this, SplashScreen.class);
            startActivity(mainToSignInIntent);
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_MEDIA:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    cameraHandler.deleteLastPhotoTaken(getApplicationContext());
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CameraHandler.getCameraHandlerRequestCode()){

            try {
                FileOutputStream out = new FileOutputStream(cameraHandler.getCurrentPhotoThumbnailPath());
                Bitmap thumbBitmap;
                thumbBitmap = ThumbnailUtils.extractThumbnail(
                        BitmapFactory.decodeFile(cameraHandler.getCurrentPhotoPath()),
                        thumbSize,
                        thumbSize);
                if(thumbBitmap != null){
                    thumbBitmap.compress(Bitmap.CompressFormat.PNG,100,out);
                    out.flush();
                    out.close();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Picasso.get().load(new File(cameraHandler.getCurrentPhotoPath())).into(thisActivity.pictureCaptured);
            try{
                processImage(cameraHandler.getImage(this));
            }catch(NullPointerException ex){
//                ShowMessage("Error",ex.getMessage());
            }

        }
        else if(requestCode == NEW_NOTE_ACTIVITY_REQUEST_CODE) {
            int button_clicked = data.getIntExtra(EditViewOfNote.ACTIVITY_BUTTON_CLICKED_REPLY , 0);
            if(button_clicked == R.id.edit_note_view_save_button){
                String s = data.getStringExtra(EditViewOfNote.ACTIVITY_EXTRACTED_TEXT_TITLE_REPLY);
                Note note = new Note(
                                (!s.isEmpty())?(s.substring(0,(s.length()>MlKit.title_length)?(MlKit.title_length):(s.length()-1))+"..."):("NOTHING CAPTURED"),
                                data.getStringExtra(EditViewOfNote.ACTIVITY_EXTRACTED_TEXT_REPLY),
                                cameraHandler.getCurrentPhotoThumbnailPath(),
                                cameraHandler.getCurrentPhotoPath(),
                                data.getStringExtra(EditViewOfNote.ACTIVITY_EXTRACTED_TEXT_DATE_TIME_REPLY),
                                BasicActivityClass.email_address);
                mNoteViewModel.insert(note);
            }
            else if(button_clicked == R.id.edit_note_view_cancel_button){
//                ShowMessage("Button Click" , "Cancel Button Clicked",this);
                //TODO Init ViewModel if cancel button clicked when taking new note
                NoteViewHolder.delete_images_from_app_folder(CameraHandler.currentPhotoPath);
                NoteViewHolder.delete_images_from_app_folder(CameraHandler.currentPhotoThumbnailPath);
            }

            int permissionWriteCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionWriteCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_MEDIA);
            } else {
                cameraHandler.deleteLastPhotoTaken(getApplicationContext());
            }

        }else if(requestCode == EDIT_NOTE_REQUEST){
            //TODO SAVE EDITED NOTE
            try {
                Note n = (Note) data.getSerializableExtra("EditedNote");
                if(n != null){
                    ClickedNote = n;
//                    mNoteViewModel.updateNote(ClickedNote.NoteId, ClickedNote.Title, ClickedNote.Content);
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            mNoteViewModel.updateNote(ClickedNote.NoteId, ClickedNote.Title, ClickedNote.Content);
                        }
                    });
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public void processImage(InputImage image) {
        mlKit.recognizeText(image);
    }



    public void previewNoteOnScreenForSurkhiPowder(){
        Intent i = new Intent(MainNotesDashboard.this, EditViewOfNote.class);
        startActivityForResult(i, NEW_NOTE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId())
        {
//            case R.id.rescan_photo_button:
//                try{
//                    processImage(cameraHandler.getImage(this));
//                }catch(NullPointerException ex){
//                    ShowMessage("Error", ex.getMessage(),this);
//                }
//                return true;

            default:break;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
//            case R.id.menu_item_sync_to_google_drive:
                //TODO Sync all notes to google drive
//                break;

//            case R.id.menu_item_sort_by:
                //TODO Sorting algo change
//                break;

//            case R.id.menu_item_setting:
                //TODO Implement Settings
//                break;

            case R.id.menu_item_logout:
                signOut();
//                NoteIdClicked.removeObserver(o);
                NoteIdClicked.removeObservers(this);
                observerCounter = 0;
                NoteIdClicked.setValue(null);
                break;
        }
        thisActivity.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.logout_button:
                break;

            case R.id.disconnect_button:
                break;

            case R.id.take_photo_fab_button:
//                Snackbar.make(findViewById(android.R.id.content),"SNACKBAR DEMO",Snackbar.LENGTH_LONG).show();
                mlKit.setIc(this);
                startActivityForResult(cameraHandler.GetPictureIntent(activity), CameraHandler.getCameraHandlerRequestCode());
                break;

            default:break;
        }
    }


    @Override
    public void callback(String s) {
        previewNoteOnScreenForSurkhiPowder();
//        speak(s);

    }
}