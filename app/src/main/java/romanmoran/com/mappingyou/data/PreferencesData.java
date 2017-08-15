package romanmoran.com.mappingyou.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.maps.model.LatLng;

import romanmoran.com.mappingyou.MyApplication;

/**
 * Created by roman on 02.08.2017.
 */

public class PreferencesData {
    public static final String OAUTH_KEY = "OAUTH_KEY";
    public static final String JSON_KEY = "JSON_KEY";
    public static final String LAST_LOC_X = "LAST_LOC_X";
    public static final String LAST_LOC_Y = "LAST_LOC_Y";
    public static final String LAST_LOC_ZOOM = "LAST_LOC_ZOOM";
    public static final String CATEGORY_POS = "CATEGORY_POS";
    private static final String KEY_FCM_TOKEN = "fcm_token";

    public static void saveLastCoordinates(double x,double y,double zoom,int categoryPosition){
        save(LAST_LOC_X,(float)x);
        save(LAST_LOC_Y,(float)y);
        save(LAST_LOC_ZOOM,(float)zoom);
        save(CATEGORY_POS,categoryPosition);
    }

    public static void saveLastLocation(LatLng latLng,double zoom,int categoryPosition){
        save(LAST_LOC_X,(float)latLng.latitude);
        save(LAST_LOC_Y,(float)latLng.longitude);
        save(LAST_LOC_ZOOM,(float)zoom);
        save(CATEGORY_POS,categoryPosition);
    }



    public static void saveAuthJson(String value,boolean auth){
        if (value==null) return;
        save(JSON_KEY,value);
        save(OAUTH_KEY,auth);
    }

    public static void saveAuth(boolean value){
        save(OAUTH_KEY,value);
    }
    public static void saveJson(String value){
        if (value==null) return;
        save(JSON_KEY,value);
    }

    public static boolean getAuth(){
       return getBoolean(OAUTH_KEY,false);
    }

    public static String getJson(){
        return getString(JSON_KEY,"");
    }

    public static LatLng getLatLng(){
        return new LatLng(getLastX(),getLastY());
    }

    public static int getCategoryPosition(){ return getInt(CATEGORY_POS,0);}
    public static float getLastX(){ return getFloat(LAST_LOC_X,0);}
    public static float getLastY(){ return getFloat(LAST_LOC_Y,0);}
    public static float getZoom(){ return getFloat(LAST_LOC_ZOOM,0);}


    public static boolean getBoolean(String key,boolean defValue){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return prefs.getBoolean(key,defValue);
    }

    public static String getString(String key,String defValue){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return prefs.getString(key,defValue);
    }

    public static double getDouble(String key, double defValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defValue)));
    }

    public static float getFloat(String key, float defValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return prefs.getFloat(key,defValue);
    }

    public static int getInt(String key, int defValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return prefs.getInt(key, defValue);
    }


    public static void save(String key,boolean value){
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preference.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }

    public static void save(String key,int value){
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt(key,value);
        editor.apply();
    }

    public static void save(String key,float value){
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preference.edit();
        editor.putFloat(key,value);
        editor.apply();
    }

    public static void save(String key, String value){
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preference.edit();
        editor.putString(key,value);
        editor.apply();
    }


    private static Context getApplicationContext() {
        return MyApplication.getInstance();
    }

    public static String getFCMToken() {
        return getString(KEY_FCM_TOKEN,null);
    }

    public static void setFCMToken(String token){
        save(KEY_FCM_TOKEN,token);
    }
}
