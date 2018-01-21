package septian.org.osmdroid.pulangkonvoi.Preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by root on 1/12/18.
 */

public class UserPref {
    public static void saveEmail(Context context, String username){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = sp.edit();
        prefsEditor.putString("email", username);
        prefsEditor.commit();
    }
    public static String getEmail(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString("email", "");
    }
    public static void saveIdPeng(Context context, int id){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = sp.edit();
        prefsEditor.putInt("idPeng", id);
        prefsEditor.commit();
    }
    public static int getIdPeng(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt("idPeng", 0);
    }
    public static void saveNamaLengkap(Context context, String nama){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = sp.edit();
        prefsEditor.putString("namaLeng", nama);
        prefsEditor.commit();
    }
    public static String getNamaLengkap(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString("namaLeng", "");
    }
    public static void saveNamaPanggilan(Context context, String nama){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = sp.edit();
        prefsEditor.putString("namaPang", nama);
        prefsEditor.commit();
    }
    public static String getNamaPanggilan(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString("namaPang", "");
    }
    public static void saveAlamat(Context context, String alamat){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = sp.edit();
        prefsEditor.putString("alamat", alamat);
        prefsEditor.commit();
    }
    public static String getAlamat(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString("alamat", "");
    }
}
