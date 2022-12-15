package com.ravenioet.notey.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class PrefManager {
    public static PrefManager prefManager;
    public Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public String DEFAULT_PREF = "def_prex0";
    public String database = null;
    private PrefManager(Context ctx, String database_x) {
        context = ctx;
        if(database_x == null){
            database = DEFAULT_PREF;
        }else {
            database = database_x;
        }
        String prefer_db = getUserId()+database;
        sharedPreferences = context.getSharedPreferences(prefer_db, MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public static PrefManager getPrefMan(Context context, String database_x) {
        if (prefManager == null) {
            prefManager = new PrefManager(context,database_x);
        }
        return prefManager;
    }
    public String getUserId(){
        return "pref_store_user";
        //return UserManager.getInstance(context).spec_user();
    }
    public String getDeviceId(){
        return "pref_store_device";
        //return DeviceManager.getInstance(context).get_device_id();
    }

    public boolean putBoolean(String name,boolean value){
        editor.putBoolean(name, value);
        editor.apply();
        return editor.commit();
    }
    public boolean putInt(String name,int value){
        editor.putInt(name, value);
        editor.apply();
        return editor.commit();
    }
    public boolean putString(String name,String value){
        Log.d("saving-", name+"-"+value);
        editor.putString(name, value);
        editor.apply();
        return editor.commit();
    }
    public boolean putFloat(String name,float value){
        editor.putFloat(name, value);
        editor.apply();
        return editor.commit();
    }
    public boolean putLong(String name,long value){
        editor.putLong(name, value);
        editor.apply();
        return editor.commit();
    }

    public boolean getBoolean(String name){
        return sharedPreferences.getBoolean(name, false);
    }
    public int getInt(String name){
        return sharedPreferences.getInt(name, -1);
    }
    public String getString(String name){
        return sharedPreferences.getString(name, "Default");
    }
    public float getFloat(String name){
        return sharedPreferences.getFloat(name, 0);
    }
    public long getLong(String name){
        return sharedPreferences.getLong(name, 0);
    }

}
