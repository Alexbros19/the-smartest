package com.alexbros.pidlubnyalexey.thesmartest;

import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class PlayCategorySports extends PlayMode{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		guessThePictureTitle.setText("Guess the sport");
	}

	@Override
	public void AddImage(){
		XmlResourceParser sports = getResources().getXml(R.xml.sports);
		int eventType = -1;

		while (eventType != XmlResourceParser.END_DOCUMENT) {
			if (eventType == XmlResourceParser.START_TAG) {
				String strName = sports.getName();
				if (strName.equals("sport")) {

					OneCategory category = new OneCategory(sports.getAttributeValue(null, "name"),
							getResources().getIdentifier(sports.getAttributeValue(null, "picture"),
									"drawable", getPackageName()));
					pictureList.add(category);
				}
			}

			try {
				eventType = sports.next();
			} catch (IOException ioException) {
				Toast.makeText(this, "Error i/o", Toast.LENGTH_LONG).show();
			} catch (XmlPullParserException xmlPullParserException) {
				Toast.makeText(this, "Error parse xml", Toast.LENGTH_LONG).show();
			}
		}

		unusedPicture = (ArrayList<OneCategory>) pictureList.clone();
	}
}
