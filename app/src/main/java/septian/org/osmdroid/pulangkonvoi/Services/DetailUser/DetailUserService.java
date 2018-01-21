package septian.org.osmdroid.pulangkonvoi.Services.DetailUser;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import septian.org.osmdroid.pulangkonvoi.LocalData.Konstanta;
import septian.org.osmdroid.pulangkonvoi.Preferences.UserPref;
import septian.org.osmdroid.pulangkonvoi.Service.Volley.Interface.VolleyCallback;
import septian.org.osmdroid.pulangkonvoi.Service.Volley.VolleyService;

/**
 * Created by root on 1/12/18.
 */

public class DetailUserService implements VolleyCallback{
    VolleyService volleyService;
    Context context;
    DetailUser detailUser;

    @Override
    public void notifySuccess(String requestType, JSONObject response) {
        try {
            Log.d("asd", "notifySuccess: "+Integer.valueOf(response.getString("idPeng")));
            UserPref.saveEmail(context, response.getString("email"));
            UserPref.saveAlamat(context, response.getString("alamat"));
            UserPref.saveNamaLengkap(context, response.getString("NamaLeng"));
            UserPref.saveIdPeng(context, Integer.valueOf(response.getString("idPeng")));
            UserPref.saveNamaPanggilan(context, response.getString("namaPang"));
            Log.d("asd", "notifySuccess2: "+UserPref.getIdPeng(context));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        detailUser.CekDetail(1);
    }

    @Override
    public void notifyError(String requestType, VolleyError error) {
        detailUser.CekDetail(0);
    }

    public interface DetailUser{
        public void CekDetail(int status);
    }

    public DetailUserService(Context context, DetailUser detailUser, Map<String, String> params) {
        this.context = context;
        this.detailUser = detailUser;
        getDatas(params);
    }

    private void getDatas(Map<String, String> params) {
        volleyService = new VolleyService(this, context);
        volleyService.postDataVolley("POSTCALL", Konstanta.detailUser, params);
    }
}
