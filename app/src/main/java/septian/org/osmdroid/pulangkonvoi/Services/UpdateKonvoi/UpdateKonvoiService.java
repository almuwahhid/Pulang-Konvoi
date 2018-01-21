package septian.org.osmdroid.pulangkonvoi.Services.UpdateKonvoi;

import android.content.Context;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import septian.org.osmdroid.pulangkonvoi.LocalData.Konstanta;
import septian.org.osmdroid.pulangkonvoi.Service.Volley.Interface.VolleyCallback;
import septian.org.osmdroid.pulangkonvoi.Service.Volley.VolleyService;

/**
 * Created by root on 1/13/18.
 */

public class UpdateKonvoiService implements VolleyCallback{
    VolleyService volleyService;
    Context context;
    UpdateKonvoi updateKonvoi;
    @Override
    public void notifySuccess(String requestType, JSONObject response) {
        try {
            if(response.getInt("sukses")==1){
                updateKonvoi.updateKonvoi(true, 1);
            }else{
                updateKonvoi.updateKonvoi(false, 1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyError(String requestType, VolleyError error) {
        updateKonvoi.updateKonvoi(true, 0);
    }

    public interface UpdateKonvoi{
        public void updateKonvoi(Boolean b, int status);
    }

    public UpdateKonvoiService(Context context, UpdateKonvoi updateKonvoi, Map<String, String> params) {
        this.context = context;
        this.updateKonvoi = updateKonvoi;
        getDatas(params);
    }

    private void getDatas(Map<String, String> params) {
        volleyService = new VolleyService(this, context);
        volleyService.postDataVolley("POSTCALL", Konstanta.updateKonvoi, params);
    }

}
