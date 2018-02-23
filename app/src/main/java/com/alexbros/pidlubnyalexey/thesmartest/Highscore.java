package com.alexbros.pidlubnyalexey.thesmartest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class Highscore extends AppCompatActivity {

	private final int NUMBER_FOR_UNLOCK_SECRET = 20;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.highscore_window);
		loadNames();
	}
	private void loadNames(){
		// save properties
		SharedPreferences saving = getSharedPreferences("highscore", MODE_PRIVATE);
		SharedPreferences checkSecretValue = getSharedPreferences("checkvalue", MODE_PRIVATE);
		SharedPreferences.Editor editor = checkSecretValue.edit();

		ArrayList<TextView> names = new ArrayList<TextView>();
		names.add((TextView) findViewById(R.id.TextViewName1));
		names.add((TextView) findViewById(R.id.TextViewName2));
		names.add((TextView) findViewById(R.id.TextViewName3));
		names.add((TextView) findViewById(R.id.TextViewName4));
		names.add((TextView) findViewById(R.id.TextViewName5));
		names.add((TextView) findViewById(R.id.TextViewName6));
		names.add((TextView) findViewById(R.id.TextViewName7));
		names.add((TextView) findViewById(R.id.TextViewName8));
		names.add((TextView) findViewById(R.id.TextViewName9));
		names.add((TextView) findViewById(R.id.TextViewName10));
		ArrayList<TextView> scores = new ArrayList<TextView>();
		scores.add((TextView) findViewById(R.id.TextViewScore1));
		scores.add((TextView) findViewById(R.id.TextViewScore2));
		scores.add((TextView) findViewById(R.id.TextViewScore3));
		scores.add((TextView) findViewById(R.id.TextViewScore4));
		scores.add((TextView) findViewById(R.id.TextViewScore5));
		scores.add((TextView) findViewById(R.id.TextViewScore6));
		scores.add((TextView) findViewById(R.id.TextViewScore7));
		scores.add((TextView) findViewById(R.id.TextViewScore8));
		scores.add((TextView) findViewById(R.id.TextViewScore9));
		scores.add((TextView) findViewById(R.id.TextViewScore10));
		for (int i = 1; i <= 10; i++){
			if (! saving.getString("Name" + Integer.toString(i), "").equals("")){
				names.get(i - 1).setText(saving.getString("Name" + Integer.toString(i), ""));
				scores.get(i - 1).setText(Integer.toString(saving.getInt("Score" + Integer.toString(i), 0)));
			} else {
				names.get(i - 1).setText("");
				scores.get(i - 1).setText("");
			}

			//-----------------------------------------------------------------------------------------------------
			if(saving.getInt("Score" + Integer.toString(i), 0) >= NUMBER_FOR_UNLOCK_SECRET){

				editor.putBoolean("unlocksecret", true);
				editor.apply();
			}
		}
	}

	public void BackToMenu(View view){
		Intent i = new Intent(Highscore.this, MainMenu.class);
		startActivity(i);
	}
}
