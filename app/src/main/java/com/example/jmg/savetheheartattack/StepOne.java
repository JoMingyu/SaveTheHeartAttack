package com.example.jmg.savetheheartattack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by leesm10413 on 2016-09-04.
 */
public class StepOne extends Activity {
    TextView titleText;
    TextView contextT;
    Button gotoNext;
    MediaPlayer music;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_one);

        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 15, AudioManager.FLAG_SHOW_UI); // 볼륨 강제로 MAX

        music = MediaPlayer.create(this, R.raw.siren);
        music.setLooping(true);
        music.start();

        titleText = (TextView)findViewById(R.id.titleText);
        contextT = (TextView)findViewById(R.id.contextT);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "font/amim.ttf");
        titleText.setTypeface(typeface);
        contextT.setTypeface(typeface);

        gotoNext = (Button)findViewById(R.id.gotoTwo);
        gotoNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // NEXT 버튼이 클릭되면 음악이 종료되고 StepTwo로 인텐트
                music.stop();
                Intent intent = new Intent(getApplicationContext(), com.example.jmg.savetheheartattack.StepTwo.class);
                startActivity(intent);
            }
        });
    }
}