package com.example.jmg.savetheheartattack;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.AccessibleObject;

/**
 * Created by leesm10413 on 2016-09-04.
 */
public class StepTwo extends Activity {
    TextView titleText2;
    TextView contextT2;
    Button gotoNext2;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_two);

        gotoNext2 = (Button)findViewById(R.id.gotoMov);
        titleText2 = (TextView)findViewById(R.id.titleText2);
        contextT2 = (TextView)findViewById(R.id.contextT2);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "font/amim.ttf");
        titleText2.setTypeface(typeface);
        contextT2.setTypeface(typeface);

        gotoNext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CPRMovie.class);
                startActivity(intent);
            }
        });
    }
}