package com.example.sntviaocr.ApplicationLayer.BasicComponents;

import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class Lottie{
    public LottieAnimationView animationView;
    public TextView textView;
    protected int raw_resource_file;
    protected int interval_of_animation;
    protected String textString;

    public Lottie(LottieAnimationView rAnimationView, int rRawRes, int rIntervalOfHowLongAnimationRun, TextView rTextView, String rTextString){
        animationView = rAnimationView;
        raw_resource_file = rRawRes;
        interval_of_animation = rIntervalOfHowLongAnimationRun;
        textView = rTextView;
        textString = rTextString;
    }


    //SETTERS
    public void setAnimationView(LottieAnimationView animationView) {
        this.animationView = animationView;
    }

    public void setRaw_resource_file(int raw_resource_file) {
        this.raw_resource_file = raw_resource_file;
    }

    public void setInterval_of_animation(int interval_of_animation) {
        this.interval_of_animation = interval_of_animation;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public void setTextString(String textString) {
        this.textString = textString;
    }

    //GETTERS
    public LottieAnimationView getAnimationView() {
        return animationView;
    }

    public int getInterval_of_animation() {
        return interval_of_animation;
    }

    public int getRaw_resource_file() {
        return raw_resource_file;
    }

    public String getTextString() {
        return textString;
    }

    public TextView getTextView() {
        return textView;
    }

    public void playAnimation(){
        //BasicActivityClass.fade_in(textView);
        //textView.setText(textString);
        animationView.setAnimation(raw_resource_file);
        animationView.playAnimation();
    }
}
