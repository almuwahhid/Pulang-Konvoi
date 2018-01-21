package septian.org.osmdroid.pulangkonvoi.Services.BatalGabung;

import android.content.Context;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Map;

import septian.org.osmdroid.pulangkonvoi.LocalData.Konstanta;
import septian.org.osmdroid.pulangkonvoi.Service.Volley.Interface.VolleyCallback;
import septian.org.osmdroid.pulangkonvoi.Service.Volley.VolleyService;

/**
 * Created by root on 1/18/18.
 */

public class BatalGabungService implements VolleyCallback {
    VolleyService volleyService;
    Context context;
    BatalGabung batalGabung;

    public interface BatalGabung{
        public void OnAfterBatal(int status);
    }


    public BatalGabungService(Context context, BatalGabung batalGabung, Map<String, String> params){
        this.batalGabung = batalGabung;
        this.context = context;
        getDatas(params);
    }
    private void getDatas(Map<String, String> params) {
        volleyService = new VolleyService(this, context);
        volleyService.postDataVolley("POSTCALL", Konstanta.batalgabung, params);
    }

    @Override
    public void notifySuccess(String requestType, JSONObject response) {
        batalGabung.OnAfterBatal(1);
    }

    @Override
    public void notifyError(String requestType, VolleyError error) {
        batalGabung.OnAfterBatal(0);
    }
}
