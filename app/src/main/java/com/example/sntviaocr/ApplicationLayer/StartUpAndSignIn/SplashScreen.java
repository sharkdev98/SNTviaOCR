package com.example.sntviaocr.ApplicationLayer.StartUpAndSignIn;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;

import com.example.sntviaocr.ApplicationLayer.BasicComponents.BasicActivityClass;
import com.example.sntviaocr.ApplicationLayer.BasicComponents.Lottie;
import com.example.sntviaocr.ApplicationLayer.BasicComponents.LottieList;
import com.example.sntviaocr.ApplicationLayer.Notes.MainNotesDashboard;
import com.example.sntviaocr.BussinessLayer.RecyclerView.NoteViewHolder;
import com.example.sntviaocr.R;

import com.example.sntviaocr.databinding.ActivitySplashScreenBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

public class SplashScreen extends BasicActivityClass implements View.OnClickListener {

    private ActivitySplashScreenBinding activitySplashScreenBinding;
    private LottieList lottieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //HIDE Title Bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) getWindow().getInsetsController().hide(WindowInsets.Type.statusBars());
        else getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //SET View
        activitySplashScreenBinding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(activitySplashScreenBinding.getRoot());

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.startup_activity_fade_in);


        //Init View Objects
        activitySplashScreenBinding.getRoot().startAnimation(animation);
        activitySplashScreenBinding.signInButton.setOnClickListener(this);
        lottieList = new LottieList(this);
        addAllAnimation();
        lottieList.play_all_animations();
    }

    public void addAllAnimation()
    {
        Lottie lottie1 = new Lottie(
                activitySplashScreenBinding.animationView,
                R.raw.camera_animation_lottie_4_sec,
                3900,
                activitySplashScreenBinding.splashImageName,
                "1) Take Photo");
        lottieList.add(lottie1);
        Lottie lottie2 = new Lottie(
                activitySplashScreenBinding.animationView,
                R.raw.notes_animation_1_3sec,
                3999,
                activitySplashScreenBinding.splashImageName,
                "2) Get Notes");
        lottieList.add(lottie2);
        Lottie lottie3 = new Lottie(
                activitySplashScreenBinding.animationView,
                R.raw.audio_animation_2_0,
                2000,
                activitySplashScreenBinding.splashImageName,
                "3) Speak Out");
        lottieList.add(lottie3);
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
    public void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        UpdateUI(account);
    }

    @Override
    public void UpdateUI(GoogleSignInAccount account) {
        if(isNetworkConnected(this));
        else {
            ShowMessage("Error","Network not Available !" , this);
            return;
        }
        if(account != null){
            BasicActivityClass.email_address = account.getEmail();
            Intent signInToMainIntent = new Intent(SplashScreen.this, MainNotesDashboard.class);
            startActivity(signInToMainIntent);
            finish();
        }else {
            activitySplashScreenBinding.animationView.setVisibility(View.VISIBLE);
            activitySplashScreenBinding.splashAppName.setVisibility(View.VISIBLE);
        }
    }



    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == activityNumber) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.sign_in_button:
                activitySplashScreenBinding.animationView.setVisibility(View.INVISIBLE);
                activitySplashScreenBinding.splashAppName.setVisibility(View.INVISIBLE);
                signIn();
                break;
            default:break;
        }
    }
}