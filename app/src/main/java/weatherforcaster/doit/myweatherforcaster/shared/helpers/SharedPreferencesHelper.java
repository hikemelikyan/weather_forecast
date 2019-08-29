package weatherforcaster.doit.myweatherforcaster.shared.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import weatherforcaster.doit.myweatherforcaster.shared.configurations.Common;

public class SharedPreferencesHelper {

    private SharedPreferences mShared;
    private SharedPreferences.Editor mEditor;

    @SuppressLint("CommitPrefEdits")
    public SharedPreferencesHelper(Context context) {
        mShared = context.getSharedPreferences(Common.SHARED_PREFERANCE_NAME, Context.MODE_PRIVATE);
        mEditor = mShared.edit();
    }

    public void addIntSharedPreferences(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.apply();
    }

    public void addStringSharedPreferences(String key, String value) {
        mEditor.putString(key, value);
        mEditor.apply();
    }

    public void addFloatSharedPreferences(String key, float value) {
        mEditor.putFloat(key, value);
        mEditor.apply();
    }

    public void addBooleanSharedPreferences(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.apply();
    }

    public int getIntSharedPreferences(String key) {
        return mShared.getInt(key, 0);
    }

    public String getStringSharedPreferences(String key) {
        return mShared.getString(key, null);
    }

    public float getFloatSharedPreferences(String key) {
        return mShared.getFloat(key, 0);
    }

    public boolean getBooleanSharedPreferences(String key) {
        return mShared.getBoolean(key, false);
    }
//
//    public <T> void addObjectSharedPreferences(String key, T value){
//        Gson gson = new Gson();
//        mEditor.putString(key,gson.toJson(value));
//        mEditor.apply();
//    }
//
//    public <T> T getObjectSharedPrefernces(String key){
//        if(mShared.getString(key,null)==null){
//            return null;
//        }else{
//            return new Gson().fromJson(mShared.getString(key,null),);
//        }
//    }
}
