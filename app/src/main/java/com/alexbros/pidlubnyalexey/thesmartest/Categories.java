package com.alexbros.pidlubnyalexey.thesmartest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class Categories extends AppCompatActivity{

	private AdView adView;
	private boolean unlockSecret;
	private Button countriesButton;
	private Button gamesButton;
	private Button sportsButton;
	private Button logotypesButton;
	private Button secretButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.categories);

		countriesButton = (Button) findViewById(R.id.countriesButton);
		gamesButton = (Button) findViewById(R.id.gamesButton);
		sportsButton = (Button) findViewById(R.id.sportsButton);
		logotypesButton = (Button) findViewById(R.id.logotypesButton);
		secretButton = (Button) findViewById(R.id.secretButton);
//-----------------------MOBILE ADS-----------------------------------------------------------------
		MobileAds.initialize(getApplicationContext(), "ca-app-pub-3889824584026064~7599520832");
		adView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
	}

	@Override
	public void onBackPressed() {
		// super.onBackPressed();
		Intent i = new Intent(Categories.this, MainMenu.class);
		startActivity(i);
	}

	@Override
	protected void onResume(){
		super.onResume();
		adView.resume();
	}

	@Override
	protected void onPause(){
		super.onPause();
		adView.pause();
	}

	@Override
	protected void onDestroy(){
		super.onDestroy();
		adView.destroy();
	}

	private void ScaleAnim(Button button){
		ScaleAnimation anim = new ScaleAnimation(1.0f, 0.5f, 1.0f, 0.5f, 50, 50);
		anim.setDuration(100);
		anim.setFillAfter(true);
		button.startAnimation(anim);
	}

	private void ScaleAnimReturn(Button button){
		ScaleAnimation anim = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f, 50, 50);
		anim.setDuration(100);
		anim.setFillAfter(true);
		button.startAnimation(anim);
	}

	public void CountriesBtnPlay(View view){
		ScaleAnim(countriesButton);
		ScaleAnimReturn(countriesButton);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent i = new Intent(Categories.this, PlayMode.class);
				startActivity(i);
			}
		}, 100);
	}

	public void GamesBtnPlay(View view) {
		ScaleAnim(gamesButton);
		ScaleAnimReturn(gamesButton);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent i = new Intent(Categories.this, PlayCategoryGames.class);
				startActivity(i);
			}
		}, 100);
	}

	public void SportsBtnPlay(View view) {
		ScaleAnim(sportsButton);
		ScaleAnimReturn(sportsButton);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent i = new Intent(Categories.this, PlayCategorySports.class);
				startActivity(i);
			}
		}, 100);
	}

	public void LogotypesBtnPlay(View view) {
		ScaleAnim(logotypesButton);
		ScaleAnimReturn(logotypesButton);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent i = new Intent(Categories.this, PlayCategoryLogotypes.class);
				startActivity(i);
			}
		}, 100);
	}

	public void CartoonsBtnPlay(View view) {
		ScaleAnim(secretButton);
		ScaleAnimReturn(secretButton);

		SharedPreferences checkSecretValue = getSharedPreferences("checkvalue", MODE_PRIVATE);

		unlockSecret = checkSecretValue.getBoolean("unlocksecret", false);

		if(unlockSecret){
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Intent i = new Intent(Categories.this, PlayCategoryCartoons.class);
					startActivity(i);
				}
			}, 100);
		} else {
			Toast toast = Toast.makeText(getApplicationContext(),
					"Reach 20 points in anyone category",
					Toast.LENGTH_LONG);
			toast.setGravity(Gravity.TOP, 0, 0);
			toast.show();
		}
	}

	public void BackToMenu(View view){
		Intent i = new Intent(Categories.this, MainMenu.class);
		startActivity(i);
	}
}
