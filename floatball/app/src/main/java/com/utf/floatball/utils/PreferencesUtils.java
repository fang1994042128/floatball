package com.utf.floatball.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.utf.floatball.MyApplication;

/**
 * 配置文件内容
 * @author jzy
 *
 */
public class PreferencesUtils {

	private Editor editor;
	private SharedPreferences sp;

	private static class HOLDER {
		static final PreferencesUtils INS = new PreferencesUtils();
	}
	private PreferencesUtils(){
		init();
	}

	public static PreferencesUtils getInstance(){
		return HOLDER.INS;
	}
	
	// 根据文件名，取配置文件
	public void init() {
		sp = MyApplication.getApplicationInstance().getSharedPreferences(PreferenceConstants.SAVE_FLOAT_BALL_NAME, Context.MODE_PRIVATE);
	}

	
	public boolean getBoolean(String key, boolean defValue) {
		if(sp == null){
			return defValue;
		}
		return sp.getBoolean(key, defValue);
	}
	
	public void putBoolean(String key, boolean state) {
		if(sp == null){
			return;
		}
		editor = sp.edit();
		editor.putBoolean(key, state);
		editor.commit();
	}
	
	public String getString(String key, String defValue) {
		if(sp == null){
			return "";
		}
		return sp.getString(key, defValue);
	}
	
	public void putString(String key, String value) {
		if(sp == null){
			return;
		}
		editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public int getInt(String key, int defValue) {
		if(sp == null){
			return defValue;
		}
		return sp.getInt(key, defValue);
	}
	
	public void putInt(String key, int value) {
		if(sp == null){
			return;
		}
		editor = sp.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
	public void putLong(String key, long value) {
		if(sp == null){
			return;
		}
		editor = sp.edit();
		editor.putLong(key, value);
		editor.commit();
	}
	
	public long getLong(String key, Long defValue) {
		if(sp == null){
			return defValue;
		}
		return sp.getLong(key, defValue);
	}
	
	public boolean contains(String key) {
		if(sp == null){
			return false;
		}
		return sp.contains(key);
	}
}


