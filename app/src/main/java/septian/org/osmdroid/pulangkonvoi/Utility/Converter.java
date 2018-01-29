package septian.org.osmdroid.pulangkonvoi.Utility;

import android.location.Location;

/**
 * Created by root on 1/8/18.
 */

public class Converter {
    static final double R_EARTH = 6367.45;
    static final double DEG_RAD = 0.01745329251994;


    public static int first_order(String order){
        return Integer.valueOf(order.split("-")[0]);
    }
    public static int last_order(String order){
        if(order.split("-")[1].equals("")){
            return Integer.valueOf(order.split("--")[1]);
        }else{
            return Integer.valueOf(order.split("-")[1]);
        }
    }
    public static double getDistance(Location p1, Location p2){
        double haversine, distance;
        double dLat, dLon;
        dLat = (p2.getLatitude() - p1.getLatitude()) * DEG_RAD;
        dLon = (p2.getLongitude() - p1.getLongitude()) * DEG_RAD;

        haversine = Math.sin(dLat * 0.5) * Math.sin(dLat * 0.5) +
                Math.sin(dLon * 0.5) * Math.sin(dLon * 0.5) *
                        Math.cos(p1.getLatitude() * DEG_RAD) *
                        Math.cos(p2.getLatitude() * DEG_RAD);

        distance = Math.asin(Math.sqrt(haversine)) * R_EARTH * 2.0;
        return distance;
    }

    public static double getDistancePlus(Location p1, Location p2, int heuristik){
        return  getDistance(p1, p2)+heuristik;
    }

    public static double getBobotPlus(Double bobot, int heuristik){
        return  bobot+heuristik;
    }
}
