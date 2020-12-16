package com.example.sntviaocr.BussinessLayer.TTS;

import android.app.Activity;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.example.sntviaocr.BussinessLayer.Common.CommonBussinessLogic;

import java.io.File;
import java.util.Locale;

public class TextToAudio extends CommonBussinessLogic {

    TextToSpeech textToSpeech;
    String extractText;

    public TextToAudio(Activity activity) {
        super();
        textToSpeech = new TextToSpeech(activity, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    int result = textToSpeech.setLanguage(Locale.US);
                    if(result==TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("error", "This Language is not supported");
                        ShowMessage("Error","Language Not Supported", activity);
                    }
                    else{
                        TextToSpeak();
                    }
                }
                else
                    Log.e("error", "Initilization Failed!");
            }
        });
    }

    public void TextToSpeak() {
        // TODO Auto-generated method stub
        if(extractText==null || "".equals(extractText));
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textToSpeech.speak(extractText, TextToSpeech.QUEUE_FLUSH, null, null);
            }
            else {
                textToSpeech.speak(extractText, TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    }

    public void downloadMp3File(String text , File file){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.synthesizeToFile(text, null, file,null);
        }else {
            textToSpeech.synthesizeToFile(text, null, file.getAbsolutePath());
        }
    }

    public void setText(String mlExtractedText) {
        extractText = mlExtractedText;
    }

    public int stopSpeaking(){
        return textToSpeech.stop();
    }
}
