package septian.org.osmdroid.pulangkonvoi.Services.CekGabung;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by root on 1/11/18.
 */

public class CekGabungJSONHelper {
    public static boolean isGabung(JSONObject jsonObject) throws JSONException {
        Boolean object = jsonObject.getBoolean("isgabung");
        return object;
    }
}
