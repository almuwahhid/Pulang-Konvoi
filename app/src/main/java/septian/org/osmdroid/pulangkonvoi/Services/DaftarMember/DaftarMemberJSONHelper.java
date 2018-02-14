package septian.org.osmdroid.pulangkonvoi.Services.DaftarMember;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

import septian.org.osmdroid.pulangkonvoi.Kelas.User;

/**
 * Created by root on 2/14/18.
 */

public class DaftarMemberJSONHelper {
    public static List<User> getMembers(JSONObject jsonObject) throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray("member");
        List<User> getMembers = new ArrayList<>();
        for (int a=0;a<jsonArray.length();a++){
            JSONObject object = jsonArray.getJSONObject(a);
            User user = new User();
            user.setNamaPeng(object.getString("namaPeng"));
            user.setAlamat(object.getString("alamat"));
            getMembers.add(user);
        }
        return getMembers;
    }
}
