package com.sysu.yibaosysu.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtils {

	public static final String USER_ID = "user_id";
	private static final int MODE = Context.MODE_PRIVATE;
	private static final String NAME = "TaoshuSYSU";
	private static SharedPreferences preferences;

	public static void saveUserId(Context context, int userId) {
		preferences = context.getSharedPreferences(NAME, MODE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(USER_ID, userId);
		editor.commit();
	}

	public static int getUserId(Context context) {
		preferences = context.getSharedPreferences(NAME, MODE);
		return preferences.getInt(USER_ID, -1);
	}

	public static void clear(Context context) {
		preferences = context.getSharedPreferences(NAME, MODE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
	}
}
