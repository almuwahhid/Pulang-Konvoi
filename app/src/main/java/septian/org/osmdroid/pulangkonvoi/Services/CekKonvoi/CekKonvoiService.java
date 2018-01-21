package septian.org.osmdroid.pulangkonvoi.Services.CekKonvoi;

import android.content.Context;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import septian.org.osmdroid.pulangkonvoi.LocalData.Konstanta;
import septian.org.osmdroid.pulangkonvoi.Service.Volley.Interface.VolleyCallback;
import septian.org.osmdroid.pulangkonvoi.Service.Volley.VolleyService;

/**
 * Created by root on 1/17/18.
 */

public class CekKonvoiService implements VolleyCallback{
    VolleyService volleyService;
    Context context;
    CekKonvoi cekKonvoi;



    public interface CekKonvoi{
        public void onCekKonvoi(Boolean b, int status);
    }

    public CekKonvoiService(Context context, CekKonvoi cekKonvoi) {
        this.context = context;
        this.cekKonvoi = cekKonvoi;
        getDatas();
    }

    private void getDatas() {
        volleyService = new VolleyService(this, context);
        volleyService.getDataVolley("GETCALL", Konstanta.cekkonvoi);
    }

    @Override
    public void notifySuccess(String requestType, JSONObject response) {
        try {
            cekKonvoi.onCekKonvoi(response.getBoolean("isKonvoi"), 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyError(String requestType, VolleyError error) {
        cekKonvoi.onCekKonvoi(false, 0);
    }
}
