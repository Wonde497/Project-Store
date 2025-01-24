package com.verifone.demo.emv.Utilities;
import android.content.Context;
import android.content.SharedPreferences;
import com.verifone.demo.emv.VFIApplication;
import java.util.Map;

/*
 *  author: Derrick
 *  Time: 2019/7/27 9:19
 *  title: a SharedPreferenced helper
 */
public class SPHelper {

    private static SPHelper instance;
    private static SharedPreferences sharedPreferences;

    private SPHelper(){

    }

    public static SPHelper getInstance() {
        if (instance == null){
            instance = new SPHelper();
            sharedPreferences = VFIApplication.getInstance().getSharedPreferences("AppParam", Context.MODE_PRIVATE);

        }
        return instance;
    }
    public String getString(String key, String defalutValue){
        return sharedPreferences.getString(key, defalutValue);
    }

    public boolean getBoolean(String key, boolean defalutValue){
        return sharedPreferences.getBoolean(key, defalutValue);
    }

    public int getInt(String key, int defalutValue){
        return sharedPreferences.getInt(key, defalutValue);
    }

    public void putString(String key, String value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void putStrings(Map<String, String> stringMap){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (Map.Entry<String, String> item: stringMap.entrySet()){
            editor.putString(item.getKey(), item.getValue());
        }
        editor.apply();
    }

    public void putBooleans(Map<String, Boolean> stringMap){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (Map.Entry<String, Boolean> item: stringMap.entrySet()){
            editor.putBoolean(item.getKey(), item.getValue());
        }
        editor.apply();
    }

    public void putBoolean(String key, boolean value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void putInt(String key, int value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

}
