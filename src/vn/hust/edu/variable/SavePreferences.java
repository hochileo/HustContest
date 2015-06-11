package vn.hust.edu.variable;

import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;


public class SavePreferences {
	
	public Context context;
	public SavePreferences(Context context) {
		this.context = context;
	}
	
	
	public void SavePreferencesInt(String key, int value) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor edit = sp.edit();
		edit.putInt(key, value);
		edit.commit();
	}
	public void  SavePreferencesString(String key, String value) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor edit = sp.edit();
		edit.putString(key, value);
		edit.commit();
	}
	public void  SavePreferencesBool(String key, boolean value) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor edit = sp.edit();
		edit.putBoolean(key, value);
		edit.commit();
	}
	public void SavePreferencesLong(String key, long value) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor edit = sp.edit();
		edit.putLong(key, value);
		edit.commit();
	}
	public void SavePreferencesFloat(String key, float value) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor edit = sp.edit();
		edit.putFloat(key, value);
		edit.commit();
	}
	@SuppressLint("NewApi")
	public void SavePreferencesSet(String key, Set<String> value) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor edit = sp.edit();
		edit.putStringSet(key, value);
		edit.commit();
	}
}
