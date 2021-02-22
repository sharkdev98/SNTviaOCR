package com.sharkdev98.smartnotetakerviaocr.ApplicationLayer.BasicComponents;

import android.content.Context;
import android.os.CountDownTimer;

import java.util.ArrayList;
import java.util.List;

public class LottieList {
    private static Context context;
    List<Lottie> lottie_list;
    int current_animation_index = 0;

    public LottieList(Context context)
    {
        this.context = context;
        lottie_list = new ArrayList<>();
    }

    public boolean add(Lottie rLottie)
    {
        return lottie_list.add(rLottie);
    }

    public boolean remove(Lottie rLottie)
    {
        return lottie_list.remove(rLottie);
    }

    public void play_all_animations()
    {
        if(lottie_list.size() <= 0) return;
        if(lottie_list.size() <= current_animation_index) current_animation_index = 0;

        final int tickInterval = 1000;
        final Lottie anim = lottie_list.get(current_animation_index++);
        anim.playAnimation();

        new CountDownTimer(anim.interval_of_animation, tickInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(millisUntilFinished <= tickInterval) {
                    //BasicActivityClass.fade_out(anim.textView);
                }
            }

            @Override
            public void onFinish() {
                play_all_animations();
            }
        }.start();
    }
}
