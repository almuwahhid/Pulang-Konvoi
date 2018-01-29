package septian.org.osmdroid.pulangkonvoi.Services.Rute;

import android.location.Location;

import java.util.HashMap;
import java.util.List;

import septian.org.osmdroid.pulangkonvoi.Kelas.GRoute;

/**
 * Created by root on 12/25/17.
 */

public interface RuteCallback {
    public void setMarker(String jarak, List<GRoute> routes, Location l, int status);
}