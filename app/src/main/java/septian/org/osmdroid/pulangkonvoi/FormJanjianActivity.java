package septian.org.osmdroid.pulangkonvoi;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import septian.org.osmdroid.pulangkonvoi.Preferences.LoginPref;
import septian.org.osmdroid.pulangkonvoi.Preferences.UserPref;
import septian.org.osmdroid.pulangkonvoi.Services.CekGabung.CekGabungService;

/**
 * Created by Lenovo on 08/03/2017.
 */

public class FormJanjianActivity extends AppCompatActivity{

    EditText txtTujuan,txtJam;
    String lat,longit, username;
    TextView fLat,fLong,us;
    LoginPref loginPref;


   // public String path= getApplicationContext().getFilesDir()+"/log";

    private static String url_tambah_janjian = "http://septianskripsi.hol.es/tambahjanjian.php";

    private static final String TAG_SUKSES = "sukses";


    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_janjian);
        loginPref = new LoginPref(this);

        getSupportActionBar().setDisplayOptions(
                ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
        getSupportActionBar().setTitle("Form Janjian");

        us = (TextView) findViewById(R.id.user);
        fLat = (TextView) findViewById(R.id.formLat);
        fLong = (TextView) findViewById(R.id.formLong);

        pDialog = new ProgressDialog(FormJanjianActivity.this);
        pDialog.setMessage("Tambah Janjian");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);

        username = loginPref.getUsername();
        us.setText(UserPref.getNamaLengkap(this));


        if (getIntent().getStringExtra("Uniqid").equals("MapsOsm")) {

            Bundle ex = getIntent().getExtras();

            lat = ex.getString("lat");
            longit = ex.getString("longit");

           // fLat.setVisibility(View.VISIBLE);
            //fLong.setVisibility(View.VISIBLE);

            fLat.setText("latitude : "+lat);
            fLong.setText("lonitude : " +longit);

            InitApp();
        }else if (getIntent().getStringExtra("Uniqid").equals("MenuUtama")){

            InitApp();

        }}

    public void  InitApp() {
        txtTujuan = (EditText) findViewById(R.id.inputTujuanPulang);
        txtJam = (EditText) findViewById(R.id.inputJamKumpul);

        TextView us = (TextView) findViewById(R.id.user);

        Button btnTandaiMap = (Button) findViewById(R.id.btnTandaiKumpul);
        Button btnSimpanJan = (Button) findViewById(R.id.SimpanJan);
        Button btnBatalSimJan = (Button) findViewById(R.id.BatalSimJan);


        btnTandaiMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        MapsOsmActivity.class);
                startActivity(i);
            }
        });

        btnSimpanJan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // buat method pada background thread

                new BuatJanjian().execute();

            }
        });

        btnBatalSimJan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                moveTaskToBack(true);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //======================================BUAT JANJIAN=============================================

class BuatJanjian extends AsyncTask<String, String, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.show();
        }

        protected Boolean doInBackground(String... args) {

            String namaTujuan = txtTujuan.getText().toString();
            String jamKum = txtJam.getText().toString();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tujuan", namaTujuan));
            params.add(new BasicNameValuePair("jam", jamKum));
            params.add(new BasicNameValuePair("longit",longit));
            params.add(new BasicNameValuePair("lat",lat));
            params.add(new BasicNameValuePair("koor", String.valueOf(UserPref.getIdPeng(FormJanjianActivity.this))));


            JSONObject json = jsonParser.makeHttpRequest(url_tambah_janjian,
                    "POST", params);

            // periksa respon log cat
            Log.d("Respon tambah janjian", json.toString());

            try {
                int sukses = json.getInt(TAG_SUKSES);
                if (sukses == 1) {
                    return true;
                } else {
                    return false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }
        protected void onPostExecute(Boolean b) {
            pDialog.dismiss();
            if(!b){
                Toast.makeText(FormJanjianActivity.this, "Bermasalah dengan server", Toast.LENGTH_SHORT).show();
            }else{
                Intent i = new Intent(getApplicationContext(),
                        DetailJanjian.class);
                startActivity(i);
                finish();
            }
            finish();
        }
    }
}
