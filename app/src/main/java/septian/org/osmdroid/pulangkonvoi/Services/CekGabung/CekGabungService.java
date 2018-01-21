package septian.org.osmdroid.pulangkonvoi.Services.CekGabung;

import android.content.Context;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import septian.org.osmdroid.pulangkonvoi.LocalData.Konstanta;
import septian.org.osmdroid.pulangkonvoi.Service.Volley.Interface.VolleyCallback;
import septian.org.osmdroid.pulangkonvoi.Service.Volley.VolleyService;

/**
 * Created by root on 1/11/18.
 */

public class CekGabungService implements VolleyCallback{

    VolleyService volleyService;
    Context context;
    CekGabung cekGabung;

    public CekGabungService(Context context, CekGabung cekGabung, Map<String, String> params){
        this.cekGabung = cekGabung;
        this.context = context;
        getDatas(params);
    }

    private void getDatas(Map<String, String> params) {
        volleyService = new VolleyService(this, context);
        volleyService.postDataVolley("POSTCALL", Konstanta.cekgabung, params);
    }

    public interface CekGabung{
        public void IsGabung(Boolean isgabung, int status, int idPeng, int idJan);
    }

    @Override
    public void notifySuccess(String requestType, JSONObject response) {
        try {
            cekGabung.IsGabung(CekGabungJSONHelper.isGabung(response), response.getInt("sukses"), response.getInt("idPeng"), response.getInt("idJan"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyError(String requestType, VolleyError error) {
        cekGabung.IsGabung(null, 0, 0, 0);
    }
}
