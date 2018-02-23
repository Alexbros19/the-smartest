package com.alexbros.pidlubnyalexey.thesmartest;

import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class Transitions extends AppCompatActivity {
    ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transition_window);
        imageView = (ImageView) findViewById(R.id.transition);
        ((TransitionDrawable) imageView.getDrawable()).startTransition(3000);
    }
}
