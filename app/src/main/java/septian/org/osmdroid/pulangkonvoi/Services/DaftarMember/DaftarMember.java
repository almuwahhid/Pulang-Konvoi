package septian.org.osmdroid.pulangkonvoi.Services.DaftarMember;

import android.content.Context;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Member;
import java.util.List;

import septian.org.osmdroid.pulangkonvoi.Kelas.User;
import septian.org.osmdroid.pulangkonvoi.LocalData.Konstanta;
import septian.org.osmdroid.pulangkonvoi.Service.Volley.Interface.VolleyCallback;
import septian.org.osmdroid.pulangkonvoi.Service.Volley.VolleyService;

/**
 * Created by root on 2/14/18.
 */

public class DaftarMember implements VolleyCallback {
    VolleyService volleyService;
    Context context;
    ListMember listMember;
    int id;

    @Override
    public void notifySuccess(String requestType, JSONObject response) {
        try {
            if(response.getInt("sukses")==1){
                listMember.onListMember(DaftarMemberJSONHelper.getMembers(response), 1);
            }else{
                listMember.onListMember(null, 2);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyError(String requestType, VolleyError error) {
        listMember.onListMember(null, 0);
    }

    public interface ListMember{
        public void onListMember(List<User> memberList, int status);
    }

    public DaftarMember(Context context, ListMember listMember, int id) {
        this.context = context;
        this.listMember = listMember;
        this.id = id;
        getDatas();
    }

    private void getDatas() {
        volleyService = new VolleyService(this, context);
        volleyService.getDataVolley("GETCALL", Konstanta.listMember+"?id="+id);
    }


}
