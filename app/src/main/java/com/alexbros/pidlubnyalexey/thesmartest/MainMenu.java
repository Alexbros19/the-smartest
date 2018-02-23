package com.alexbros.pidlubnyalexey.thesmartest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;

public class MainMenu extends AppCompatActivity{

	private ImageButton aboutButton;
	private ImageButton highscoreButton;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		aboutButton = (ImageButton) findViewById(R.id.buttonAbout);
		highscoreButton = (ImageButton) findViewById(R.id.buttonHighscore);

		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		PackageManager manager = this.getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			if (! prefs.getString("notificationShowedVersion", "").equals(info.versionName)){
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString("notificationShowedVersion", info.versionName);
				editor.commit();
			}
		} catch (NameNotFoundException e) {}
	}

	public void Play(View view){
		startActivity(new Intent(MainMenu.this, Categories.class));
	}

	public void ButtonAboutClick(View view){
		ScaleAnim(aboutButton);

		AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.myAlertDialogStyle);
		builder.setTitle(R.string.about);
		builder.setMessage(R.string.aboutText);
		builder.setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	}

	public void ShowHighscore(View view){
		ScaleAnim(highscoreButton);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				startActivity(new Intent(MainMenu.this, Highscore.class));
			}
		}, 100);
	}

	private void ScaleAnim(ImageButton imagebutton){
		ScaleAnimation anim = new ScaleAnimation(1.0f, 0.75f, 1.0f, 0.75f, 50, 50);
		anim.setDuration(100);
		anim.setFillAfter(true);
		anim.setRepeatCount(1);
		anim.setRepeatMode(Animation.REVERSE);
		imagebutton.startAnimation(anim);
	}
}
