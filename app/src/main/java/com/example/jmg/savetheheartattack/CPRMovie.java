package com.example.jmg.savetheheartattack;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by lesesm10413 on 2016-09-04.
 */
public class CPRMovie extends Activity {
    VideoView videoview;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cpr_movie);

        Uri video = Uri.parse("android.resource://" + getPackageName() + "/raw/vdvd");
        videoview = (VideoView)findViewById(R.id.videoview);
        videoview.setVideoURI(video);
        // videoview.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vddd));
        // videoview.setVideoPath(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video_file));

        final MediaController mc = new MediaController(CPRMovie.this);
        videoview.setMediaController(mc);
        videoview.postDelayed(new Runnable() {
            @Override
            public void run() {
                mc.show(0);
            }
        }, 100);
        videoview.start();
    }
}