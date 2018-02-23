package com.alexbros.pidlubnyalexey.thesmartest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class PlayMode extends AppCompatActivity {

	private int score = 0;
	private int timeLeft = 60;
	private int answer;
	private int givenAnswer;
	private int lastHighscore;

	private boolean buttonsActivated = true;
	private boolean gameStopped = false;
	private boolean allowPauseGame = true;
	private boolean newHighscoreAlreadySaid = false;
	private boolean pausedFlag = false;

	private ImageView categoryImage;
	private TextView textPoints;
	private TextView textTime;
	private TextView guessThePictureTitle;

	private ArrayList<OneCategory> pictureList = new ArrayList<OneCategory>();
	private ArrayList<Button> answerButtons = new ArrayList<Button>();
	private ArrayList<OneCategory> unusedPicture;

	private CountDownTimer countdown;
	private Dialog highscoreDialog;
	private String name;
	private ImageButton pauseGameButton;
	private InterstitialAd interstitial;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_mode);

		//-----------------------ADD MOBILE INTERSTITIAL AD--------------------------------------------------------
		MobileAds.initialize(getApplicationContext(), "ca-app-pub-3889824584026064~7599520832");
		interstitial = new InterstitialAd(this);
		interstitial.setAdUnitId("ca-app-pub-3889824584026064/5424783631");
		AdRequest adRequest = new AdRequest.Builder().build();
		interstitial.loadAd(adRequest);

		pauseGameButton = (ImageButton) findViewById(R.id.pauseGameButton);
		categoryImage = (ImageView) findViewById(R.id.contentImageView);
		textPoints = (TextView) findViewById(R.id.textViewPoints);
		textTime = (TextView) findViewById(R.id.textViewTime);
		guessThePictureTitle = (TextView) findViewById(R.id.guessThePictureTitle);
		answerButtons.add((Button) findViewById(R.id.firstBtnAnswer));
		answerButtons.add((Button) findViewById(R.id.secondBtnAnswer));
		answerButtons.add((Button) findViewById(R.id.thirdBtnClick));
		answerButtons.add((Button) findViewById(R.id.fourthBtnClick));
		textPoints.setText(getResources().getText(R.string.points) + ": " + score);
		textTime.setText(getResources().getText(R.string.time) + ": " + timeLeft);
		guessThePictureTitle.setText("Guess the country");
		lastHighscore = getSharedPreferences("highscore", MODE_PRIVATE).getInt("Score" + "1", -10000);

		AddImage();
		ChooseAnswer();
		StartCountdownAgain();
	}

	public void StartCountdownAgain(){
		countdown = new CountDownTimer(timeLeft * 1000, 1000){

			public void onTick(long millisUntilFinished) {
				timeLeft -= 1;
				textTime.setText(getResources().getText(R.string.time) + ": " + Integer.toString(timeLeft));
			}

			public void onFinish() {
				gameStopped = true;
				allowPauseGame = false;
				pauseGameButton.setImageResource(R.drawable.pause_off);
				highscoreDialog = new Dialog(PlayMode.this);
				highscoreDialog.setContentView(R.layout.highscoredialog);
				highscoreDialog.setTitle("HIGHSCORE");
				highscoreDialog.getWindow().setBackgroundDrawableResource(R.color.colorMenuBackground);
				highscoreDialog.getWindow().setTitleColor(getResources().getColor(R.color.colorMainFont));
				highscoreDialog.setCancelable(false);
				highscoreDialog.show();
				CharSequence statement;
				if (score == 0){
					statement = getResources().getText(R.string.statementEqualZero);
				} else if (score <= 10){
					statement = getResources().getText(R.string.statementZeroToTen);
				} else {
					statement = getResources().getText(R.string.statementTenToRecord);
				}
				((TextView) highscoreDialog.findViewById(R.id.textViewHighscore)).setText(statement + "\n" + getResources().getText(R.string.highscorePoints) + ": " + Integer.toString(score) + "\n" + getResources().getText(R.string.pleaseEnterYourName));
				SharedPreferences prefs = getPreferences(MODE_PRIVATE);
				((EditText) highscoreDialog.findViewById(R.id.editText1)).setText(prefs.getString("lastUsedName", ""));
				((EditText) highscoreDialog.findViewById(R.id.editText1)).addTextChangedListener(new TextWatcher(){

					public void afterTextChanged(Editable arg0) {
					}
					public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
					}
					public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
						if (arg0.toString().equals("")){
							((Button) highscoreDialog.findViewById(R.id.buttonHighscore)).setEnabled(false);
						} else {
							((Button) highscoreDialog.findViewById(R.id.buttonHighscore)).setEnabled(true);
						}
					}
				});

				Button buttonHighscore = (Button) highscoreDialog.findViewById(R.id.buttonHighscore);
				buttonHighscore.setEnabled(! ((EditText) highscoreDialog.findViewById(R.id.editText1)).getText().toString().equals(""));
				buttonHighscore.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
//-----------------------------ADD INTERSTITIAL-----------------------------------
						if (interstitial.isLoaded()) {
							interstitial.show();
						}else {
							name = ((EditText) highscoreDialog.findViewById(R.id.editText1)).getText().toString();
							SharedPreferences saving = getSharedPreferences("highscore", MODE_PRIVATE);
							SharedPreferences.Editor editor = saving.edit();
							if (score > saving.getInt("Score" + Integer.toString(10), -10000)){
								int position = 1;
								for (int i = 9; i >= 1; i--){
									if (score <= saving.getInt("Score" + Integer.toString(i), -10000)){
										position = i + 1;
										break;
									}
								}
								for (int i = 10; i >= position + 1; i--){
									editor.putString("Name" + Integer.toString(i), saving.getString("Name" + Integer.toString(i - 1), ""));
									editor.putInt("Score" + Integer.toString(i), saving.getInt("Score" + Integer.toString(i - 1), 0));
								}
								editor.putString("Name" + Integer.toString(position), name);
								editor.putInt("Score" + Integer.toString(position), score);
								editor.commit();
							}

							SharedPreferences.Editor localEditor = getPreferences(MODE_PRIVATE).edit();
							localEditor.putString("lastUsedName", name);
							localEditor.commit();
							highscoreDialog.dismiss();
							PlayMode.this.finish();
						}
					}
				});
			}
		};
		countdown.start();
	}

	public void AddImage(){
		XmlResourceParser countries = getResources().getXml(R.xml.countries);
		int eventType = -1;

		while (eventType != XmlResourceParser.END_DOCUMENT) {
			if (eventType == XmlResourceParser.START_TAG) {
				String strName = countries.getName();
				if (strName.equals("country")) {

					OneCategory country = new OneCategory(countries.getAttributeValue(null, "name"),
							getResources().getIdentifier(countries.getAttributeValue(null, "picture"),
									"drawable", getPackageName()));
					pictureList.add(country);
				}
			}

			try {
				eventType = countries.next();
			} catch (IOException ioException) {
				Toast.makeText(this, "Error i/o", Toast.LENGTH_LONG).show();
			} catch (XmlPullParserException xmlPullParserException) {
				Toast.makeText(this, "Error parse xml", Toast.LENGTH_LONG).show();
			}
		}

		unusedPicture = (ArrayList<OneCategory>) pictureList.clone();
	}

	public void ChooseAnswer(){

		if (unusedPicture.size() == 0){
			unusedPicture = (ArrayList<OneCategory>) pictureList.clone();
		}
		ArrayList<OneCategory> chooseList = (ArrayList<OneCategory>) unusedPicture.clone();
		ArrayList<OneCategory> alternateChooseList = (ArrayList<OneCategory>) unusedPicture.clone();
		ArrayList<Button> chooseAnswerButtons = (ArrayList<Button>) answerButtons.clone();
		OneCategory choosenCountry = chooseList.get((int) Math.round(Math.random() * chooseList.size() - 0.5));
		categoryImage.setImageResource(choosenCountry.resourceID);
		chooseList.remove(choosenCountry);
		alternateChooseList.remove(choosenCountry);
		unusedPicture.remove(choosenCountry);
		answer = (int) Math.round(Math.random() * chooseAnswerButtons.size() + 0.5);
		chooseAnswerButtons.get(answer - 1).setText(choosenCountry.name);
		chooseAnswerButtons.remove(answer - 1);
		boolean chooseListBecameEmpty = false;

		if (chooseList.size() == 0){
			chooseListBecameEmpty = true;
		}
		for (int j = 1; j <= 3; j++){
			if (! chooseListBecameEmpty){
				OneCategory choosenCountry2 = chooseList.get((int) Math.round(Math.random() * chooseList.size() - 0.5));
				chooseList.remove(choosenCountry2);
				alternateChooseList.remove(choosenCountry2);
				int alternateAnswerButton = (int) Math.round(Math.random() * chooseAnswerButtons.size() + 0.5);
				chooseAnswerButtons.get(alternateAnswerButton - 1).setText(choosenCountry2.name);
				chooseAnswerButtons.remove(alternateAnswerButton - 1);

				if (chooseList.size() == 0){
					chooseListBecameEmpty = true;
				}
			} else {
				OneCategory choosenCountry2 = alternateChooseList.get((int) Math.round(Math.random() * alternateChooseList.size() - 0.5));
				alternateChooseList.remove(choosenCountry2);
				int alternateAnswerButton = (int) Math.round(Math.random() * chooseAnswerButtons.size() + 0.5);
				chooseAnswerButtons.get(alternateAnswerButton - 1).setText(choosenCountry2.name);
				chooseAnswerButtons.remove(alternateAnswerButton - 1);
			}
		}
	}

	public void ButtonAnswerClick(View view){
		if (buttonsActivated && ! gameStopped){
			buttonsActivated = false;
			Button clickedButton = (Button) view;
			switch (clickedButton.getId()){
				case R.id.firstBtnAnswer:
					givenAnswer = 1;
					break;
				case R.id.secondBtnAnswer:
					givenAnswer = 2;
					break;
				case R.id.thirdBtnClick:
					givenAnswer = 3;
					break;
				case R.id.fourthBtnClick:
					givenAnswer = 4;
					break;
			}
			if (givenAnswer == answer){
				score += 1;
				textPoints.setText(getResources().getText(R.string.points) + ": " + Integer.toString(score));
				answerButtons.get(answer - 1).setBackgroundResource(R.drawable.positive_answer_btn_style);
				//set handler to do some code with delay
				//handler update view data (exp. buttons color) from main thread
				Handler handler = new Handler();
				handler.postDelayed(new Runnable(){
					public void run(){
						answerButtons.get(answer - 1).setBackgroundResource(R.drawable.answer_btns_style);
						ChooseAnswer();
						buttonsActivated = true;
					}
				}, 750);
			} else {
				answerButtons.get(givenAnswer - 1).setBackgroundResource(R.drawable.negative_answer_btn_style);
				answerButtons.get(answer - 1).setBackgroundResource(R.drawable.positive_answer_btn_style);
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					public void run() {
						answerButtons.get(givenAnswer - 1).setBackgroundResource(R.drawable.answer_btns_style);
						answerButtons.get(answer - 1).setBackgroundResource(R.drawable.answer_btns_style);
						ChooseAnswer();
						buttonsActivated = true;
					}
				}, 1250);
			}
			if (! newHighscoreAlreadySaid){
				if (score > lastHighscore){
					Toast toast = Toast.makeText(getApplicationContext(), R.string.highscoreTitle, Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP, 0, 0);
					toast.show();
					newHighscoreAlreadySaid = true;
				}
			}
		}
	}

	@Override
	public void onBackPressed() {
		pause();
		pauseGameButton.setImageResource(R.drawable.pause_off);
	}
	@Override
	public void onPause(){
		super.onPause();
		pause();
	}
	@Override
	public boolean onPrepareOptionsMenu(Menu menu){
		pause();
		pauseGameButton.setImageResource(R.drawable.pause_off);
		return true;
	}
	protected void pause(){
		if (allowPauseGame && !pausedFlag){
			pausedFlag = true;
			countdown.cancel();

			AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.myAlertDialogStyle);
			builder.setTitle(R.string.gamePaused);
			builder.setPositiveButton(R.string.gameContinue, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					StartCountdownAgain();
					pausedFlag = false;
					pauseGameButton.setImageResource(R.drawable.pause_on);
				}
			});
			builder.setNegativeButton(R.string.backToMenu, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
//-----------------------------ADD INTERSTITIAL-----------------------------------
					if (interstitial.isLoaded()) {
						interstitial.show();
						finish();
					}else {
						finish();
					}
				}
			});
			builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
					StartCountdownAgain();
					pausedFlag = false;
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
		}
	}

	public void PauseGameButton(View view){
		pause();
		pauseGameButton.setImageResource(R.drawable.pause_off);
	}
}
