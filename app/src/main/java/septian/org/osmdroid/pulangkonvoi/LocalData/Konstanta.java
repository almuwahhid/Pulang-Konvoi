package septian.org.osmdroid.pulangkonvoi.LocalData;

import android.util.Log;

/**
 * Created by root on 1/11/18.
 */

public class Konstanta {
    private static String base_url = "http://septianskripsi.hol.es/";

    public static String cekgabung = base_url+"cekGabung.php";
    public static String cekkonvoi = base_url+"cekKonvoi.php";
    public static String detailUser = base_url+"detailPengguna.php";
    public static String updateKonvoi = base_url+"updateKonvoi.php";
    public static String batalgabung = base_url+"batalGabung.php";
    public static final String URL_DIRECTION(Double lat1, Double lat2, Double lng1, Double lng2){
        String str_origin = "origin="+lat1+","+lng1;
        String str_dest = "destination="+lat2+","+lng2;
        String sensor = "sensor=false";
        String parameters = str_origin+"&"+str_dest+"&"+sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
        Log.d("url", "URL_DIRECTION: "+url);
        return url;
    }

}
