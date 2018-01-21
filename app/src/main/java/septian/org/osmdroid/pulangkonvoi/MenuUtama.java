package septian.org.osmdroid.pulangkonvoi;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import septian.org.osmdroid.pulangkonvoi.Preferences.LoginPref;
import septian.org.osmdroid.pulangkonvoi.Preferences.UserPref;
import septian.org.osmdroid.pulangkonvoi.Services.CekGabung.CekGabungService;
import septian.org.osmdroid.pulangkonvoi.Services.CekKonvoi.CekKonvoiService;

/**
 * Created by Lenovo on 18/03/2017.
 */

public class MenuUtama extends AppCompatActivity implements CekGabungService.CekGabung, CekKonvoiService.CekKonvoi{
    LoginPref loginPref;
    private ProgressDialog pDialog;

    private Map<String, String> params(){
        Map<String, String> jsonBody = new HashMap<String, String>();
        Log.d("asd", "params: "+new LoginPref(this).getUsername());
        Log.d("asd", "params id: "+String.valueOf(UserPref.getAlamat(this)));
        jsonBody.put("id", String.valueOf(UserPref.getIdPeng(this)));
        return jsonBody;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_utama);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }

        loginPref = new LoginPref(this);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Cek Gabung");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);

        if(!loginPref.isLogin()){
            startActivity(new Intent(MenuUtama.this, Daftar.class));
        }

        ImageButton btnBuatJanjian = (ImageButton)findViewById(R.id.btnBuatJan);
        ImageButton btnListJanjian = (ImageButton)findViewById(R.id.btnListJan);
        ImageButton btnJanjianSaya = (ImageButton)findViewById(R.id.btnJanSaya);


        btnBuatJanjian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pDialog.show();
                new CekGabungService(MenuUtama.this, MenuUtama.this, params());

            }
        });
        btnListJanjian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuUtama.this,ListJanjian.class);
                startActivity(i);

            }
        });
        btnJanjianSaya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pDialog.show();
                new CekGabungService(MenuUtama.this, new CekGabungService.CekGabung() {
                    @Override
                    public void IsGabung(Boolean isgabung, int status, int idPeng, int idJan) {
                        pDialog.hide();

                        if(status!=0){
                            if (isgabung){
                                Intent i = new Intent(MenuUtama.this,DetailJanjian.class);
                                startActivity(i);
                            }else {
                                Toast.makeText(MenuUtama.this, "Maaf Anda belum terdaftar di konvoi manapun yang belum berangkat", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(MenuUtama.this, "Bermasalah dengan koneksi", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, params());

            }
        });
    }

    @Override
    public void IsGabung(Boolean isgabung, int status, int idPeng, int idJan) {
        if(status!=0){
            if (!isgabung){
                new CekKonvoiService(MenuUtama.this, MenuUtama.this);
            }else {
                pDialog.hide();
                Toast.makeText(MenuUtama.this, "Maaf Anda sudah mendaftar konvoi", Toast.LENGTH_SHORT).show();
            }
        }else{
            pDialog.hide();
            Toast.makeText(MenuUtama.this, "Bermasalah dengan koneksi", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCekKonvoi(Boolean isKonvoi, int status) {
        pDialog.hide();
        if(status!=0){
            if(!isKonvoi){
                Intent i = new Intent(MenuUtama.this,FormJanjianActivity.class);
                i.putExtra("Uniqid","MenuUtama");
                startActivity(i);
            }else{
                Toast.makeText(MenuUtama.this, "Maaf List konvoi sudah mencapai batas maksimal terdaftar", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(MenuUtama.this, "Bermasalah dengan koneksi", Toast.LENGTH_SHORT).show();
        }
    }
}
