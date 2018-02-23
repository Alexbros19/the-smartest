package com.alexbros.pidlubnyalexey.thesmartest;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.widget.Button;

public class ButtonFont extends AppCompatButton {
	public ButtonFont(Context context) {
		super(context);
		applyCustomFont(context);
	}

	public ButtonFont(Context context, AttributeSet attrs) {
		super(context, attrs);
		applyCustomFont(context);
	}

	public ButtonFont(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		applyCustomFont(context);
	}

	private void applyCustomFont(Context context) {
		this.setTypeface(Typeface.createFromAsset(context.getAssets(),
				"RockwellRegular.ttf"));
	}
}
