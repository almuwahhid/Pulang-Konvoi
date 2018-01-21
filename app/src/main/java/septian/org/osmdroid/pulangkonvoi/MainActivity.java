package septian.org.osmdroid.pulangkonvoi;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import septian.org.osmdroid.pulangkonvoi.Preferences.LoginPref;
import septian.org.osmdroid.pulangkonvoi.Preferences.UserPref;
import septian.org.osmdroid.pulangkonvoi.Services.DetailUser.DetailUserService;
import android.support.v4.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements DetailUserService.DetailUser{
    private ProgressDialog pDialog;

    LoginPref loginPref;
    public String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/pulangkonvoi";


    //String path = getApplicationContext().getFilesDir()+"/log";

    private Map<String, String> params(){
        Map<String, String> jsonBody = new HashMap<String, String>();
        Log.d("asd", "params: "+new LoginPref(this).getUsername());
        jsonBody.put("email", new LoginPref(this).getUsername());
        return jsonBody;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginPref = new LoginPref(this);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Gabung");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        if(loginPref.isLogin()){
            if(UserPref.getIdPeng(this)!=0){
                Intent i = new Intent(getApplicationContext(), MenuUtama.class);
                startActivity(i);
                finish();
            }else{
                //Context context, DetailUser detailUser, Map<String, String> params
                pDialog.show();
                new DetailUserService(this, this, params());
            }

        }else{
            Toast.makeText(getApplicationContext(), "Anda Belum Mendaftar", Toast.LENGTH_LONG).show();
            Intent in = new Intent(getApplicationContext(), Daftar.class);
            startActivity(in);
        }
    }

    @Override
    public void CekDetail(int status) {
        pDialog.hide();
        if(status==1){
            Intent i = new Intent(getApplicationContext(), MenuUtama.class);
            startActivity(i);
            finish();
        }else if(status==0){
            Toast.makeText(MainActivity.this, "Bermasalah dengan koneksi", Toast.LENGTH_SHORT).show();
        }
    }
}



