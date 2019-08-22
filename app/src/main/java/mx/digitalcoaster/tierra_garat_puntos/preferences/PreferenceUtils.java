package mx.digitalcoaster.tierra_garat_puntos.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtils {

    public PreferenceUtils(){

    }

    //Se guardan todas las variables desadas para despues ser conpartidas con cualquier actividad, fragmento, etc..
    public static boolean saveEmail(String email, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_EMAIL, email);
        prefsEditor.apply();
        return true;
    }

    public static String getEmail(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_EMAIL, null);
    }

    public static boolean savePassword(String password, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_PASSWORD, password);
        prefsEditor.apply();
        return true;
    }

    public static String getPassword(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_PASSWORD, null);
    }

    public static void saveName(String name, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences (context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString (Constants.KEY_NAME, name);
        prefsEditor.apply ();
    }

    public static String getName(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences (context);
        return prefs.getString (Constants.KEY_NAME, null);
    }

    public static void saveCodigo(String codigo, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences (context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString (Constants.KEY_CODIGO, codigo);
        prefsEditor.apply ();
    }

    public static String getCodigo(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences (context);
        return prefs.getString (Constants.KEY_CODIGO, null);
    }

    public static void savePuntos(int puntos, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences (context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putInt(String.valueOf (Constants.KEY_PUNTOS), puntos);
        prefsEditor.apply ();
    }

    public static int getPuntos(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences (context);
        return prefs.getInt (String.valueOf (Constants.KEY_PUNTOS), 0);
    }


    public static void saveAddress(String address, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences (context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString (Constants.KEY_ADDRESS, address);
        prefsEditor.apply ();
    }

    public static String getAddress(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences (context);
        return prefs.getString (Constants.KEY_ADDRESS, null);
    }

    public static void saveMenu(String menu, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences (context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString (Constants.KEY_MENU, menu);
        prefsEditor.apply ();
    }

    public static String getMenu(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences (context);
        return prefs.getString (Constants.KEY_MENU, null);
    }

    public static void saveBeneficios(String beneficios, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences (context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString (Constants.KEY_BENEFICIOS, beneficios);
        prefsEditor.apply ();
    }

    public static String getBeneficios(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences (context);
        return prefs.getString (Constants.KEY_BENEFICIOS, null);
    }

    public static void saveSession(Boolean session, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences (context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putBoolean (Constants.KEY_SESSION, session);
        prefsEditor.apply ();
    }

    public static Boolean getSession(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences (context);
        return prefs.getBoolean (Constants.KEY_SESSION, false);
    }

    public  static void deleteData(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = preferences.edit();
        prefsEditor.clear();
        prefsEditor.apply ();
    }

}
