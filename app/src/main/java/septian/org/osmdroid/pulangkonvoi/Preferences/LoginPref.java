package septian.org.osmdroid.pulangkonvoi.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by root on 12/7/17.
 */

public class LoginPref {
    Context context;
    SharedPreferences username;

    public LoginPref(Context context) {
        this.context = context;
        username = context.getSharedPreferences("username", 0);
    }

    public void setUsername(String username) {
        SharedPreferences.Editor editor = this.username.edit();
        editor.putString("username", username);
        editor.commit();
    }

    public String getUsername() {
        return this.username.getString("username", "null");
    }

    public Boolean isLogin(){
        if(getUsername().equals("null")){
            return false;
        }
        return true;
    }
}
