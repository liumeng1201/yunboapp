package com.realaction.yunbomobile.view;

import com.realaction.yunbomobile.R;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * …Ë÷√ΩÁ√Ê
 * 
 * @author liumeng
 */
public class SettingsPage extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
	}
}
