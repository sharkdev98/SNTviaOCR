package com.example.sntviaocr.ApplicationLayer.BasicComponents;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.sntviaocr.BussinessLayer.CameraHandler.CameraHandler;
import com.example.sntviaocr.BussinessLayer.MLKit.MlKit;
import com.example.sntviaocr.BussinessLayer.RecyclerView.NoteListAdapter;
import com.example.sntviaocr.BussinessLayer.TTS.TextToAudio;
import com.example.sntviaocr.BussinessLayer.ViewModels.NoteViewModel;
import com.example.sntviaocr.DataLayer.Note.Note;
import com.example.sntviaocr.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BasicActivityClass extends AppCompatActivity implements ICallback {
    protected static int                  activityNumber;
    protected  Activity             activity;
    protected  GoogleSignInClient   mGoogleSignInClient;
    protected static GoogleSignInOptions  mGoogleSignInOptions;
    protected static GoogleSignInAccount  account;
    protected static String display_name;
    private static Animation anim_fade_out, anim_fade_in;
    public static RecyclerView recyclerView;
    public static NoteListAdapter noteListAdapter;
    protected static CameraHandler cameraHandler;
    protected static MlKit mlKit;
    public static TextToAudio textToAudio;
    public static NoteViewModel mNoteViewModel;
    public static String email_address;
    public static Note ClickedNote;
    public static int observerCounter = 0;
    public static MutableLiveData<Integer> NoteIdClicked = new MutableLiveData<>();


    public static void shareText(Context context , String subject, String body) {
        Intent txtIntent = new Intent(android.content.Intent.ACTION_SEND);
        txtIntent .setType("text/plain");
        txtIntent .putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        txtIntent .putExtra(android.content.Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(txtIntent ,"Share"));
    }


    public void ShowMessage(String title, String message , Context rActivity)
    {
        AlertDialog.Builder AlertDialogBuilder = new AlertDialog.Builder(rActivity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                });
        AlertDialogBuilder.create().show();
    }

    protected boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            try {
                URL url = new URL("http://www.google.com/");
                HttpURLConnection urlc = (HttpURLConnection)url.openConnection();
                urlc.setRequestProperty("User-Agent", "test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1000); // mTimeout is in seconds
                urlc.connect();
                return urlc.getResponseCode() == 200;
            } catch (IOException e) {
                Log.i("warning", "Error checking internet connection", e);
                return false;
            }
        }

        return false;
    }

    public static void speak(String textForSpeaking) {
        textToAudio.setText(textForSpeaking);
        textToAudio.TextToSpeak();
    }

    public static void stopSpeaking(){
        if(textToAudio.stopSpeaking() == TextToSpeech.SUCCESS){
            Log.i("TEXTTOSPEECH", "Audio Speach Stoped");
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cameraHandler = new CameraHandler();
        mlKit = new MlKit(this);
        textToAudio = new TextToAudio(BasicActivityClass.this);
        ClickedNote = new Note("", "", "", "", "", "");
        init();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    protected void init()
    {
        anim_fade_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        anim_fade_out = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);

        mGoogleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,mGoogleSignInOptions);
    }

    protected void childActivityMustRequireInit(Activity rActivity)
    {
        activity = rActivity;
        activityNumber++;
    }

    protected void onStart() {
        super.onStart();
        account = GoogleSignIn.getLastSignedInAccount(this);
        //UpdateUI(account);
    }

    public void signIn() {
        Intent signInActivity = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInActivity,activityNumber,null);
    }


    public void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener( this, task -> UpdateUI(null));
    }

    public void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener( this, task -> UpdateUI(null));
    }

    public void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try{
            account = completedTask.getResult(ApiException.class);
            email_address = account.getEmail();
            UpdateUI(account);
        } catch (ApiException e) {
            e.printStackTrace();
            UpdateUI(null);
        }
    }

    public abstract void UpdateUI(GoogleSignInAccount account);

    public static void fade_out(View v) {
        v.startAnimation(anim_fade_out);
    }

    public static void fade_in(View v) {
        v.startAnimation(anim_fade_in);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void callback(String s) {

    }
}
