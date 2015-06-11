package vn.hust.edu.variable;

import java.util.HashSet;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class LoadPreferences {
	private Context context;

	public LoadPreferences(Context context) {
		this.context = context;
	}

	public boolean loadBool(String key) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getBoolean(key, false);
	}

	public int loadInt(String key) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getInt(key, -1);
	}

	public float loadFloat(String key) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getFloat(key, 0.0f);
	}

	public long loadLong(String key) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getLong(key, 0l);
	}

	public String loadString(String key) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getString(key, "");
	}

	@SuppressLint("NewApi")
	public Set<String> loadStringSet(String key) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getStringSet(key, new HashSet<String>());
	}

}
