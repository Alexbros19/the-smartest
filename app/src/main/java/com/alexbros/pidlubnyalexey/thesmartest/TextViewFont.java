package com.alexbros.pidlubnyalexey.thesmartest;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewFont extends AppCompatTextView {
	public TextViewFont(Context context) {
		super(context);
		applyCustomFont(context);
	}

	public TextViewFont(Context context, AttributeSet attrs) {
		super(context, attrs);
		applyCustomFont(context);
	}

	public TextViewFont(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		applyCustomFont(context);
	}

	private void applyCustomFont(Context context) {
		this.setTypeface(Typeface.createFromAsset(context.getAssets(),
				"RockwellRegular.ttf"));
	}
}
