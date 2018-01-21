package septian.org.osmdroid.pulangkonvoi;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import septian.org.osmdroid.pulangkonvoi.Preferences.LoginPref;
import septian.org.osmdroid.pulangkonvoi.Preferences.UserPref;
import septian.org.osmdroid.pulangkonvoi.Services.BatalGabung.BatalGabungService;
import septian.org.osmdroid.pulangkonvoi.Services.CekGabung.CekGabungService;

public class Gabung extends AppCompatActivity implements CekGabungService.CekGabung, BatalGabungService.BatalGabung{
    LoginPref loginPref;

    TextView vTuj, vjam, vIdGab,vLatGab,vLongGab, vKoorGab,vStatGab;
    Button btnGabung , btnBatal;

    String idJan,namaTujuan,jam,latitude,longitude,koordinator,status;

    String username;
    int id_janjian;
    private ProgressDialog pDialog;


    private static String url_gabung = "http://septianskripsi.hol.es/gabung.php";

    private static final String TAG_SUKSES = "sukses";
    private static final String TAG_MEMBER = "member";
    private static final String TAG_IDJAN = "idJan";
    private static final String TAG_TUJUAN = "tujuan";
    private static final String TAG_JAM = "jam";
    private static final String TAG_LAT = "lat";
    private static final String TAG_LONG = "long";
    private static final String TAG_KOOR = "koor";
    private static final String TAG_STATUS = "status";

    JSONParser jsonParser = new JSONParser();

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gabung);
        loginPref = new LoginPref(this);
        getSupportActionBar().setDisplayOptions(
                ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
        getSupportActionBar().setTitle("Informasi Janjian");

        id_janjian = getIntent().getIntExtra("idJan", 0);

        vTuj = (TextView) findViewById(R.id.viewTujuanGab);
        vjam = (TextView) findViewById(R.id.viewJamGab);
        vIdGab = (TextView) findViewById(R.id.idGab);
        vLatGab = (TextView) findViewById(R.id.latGab);
        vLongGab = (TextView) findViewById(R.id.longGab);
        vKoorGab = (TextView) findViewById(R.id.koorGab);
        vStatGab = (TextView) findViewById(R.id.statGab);

        btnGabung = (Button) findViewById(R.id.btnGab);
        btnBatal = (Button) findViewById(R.id.btnBatalGab);

        Bundle dataGabung = getIntent().getExtras();
        idJan = dataGabung.getString(TAG_IDJAN);
        namaTujuan = dataGabung.getString(TAG_TUJUAN);
        jam = dataGabung.getString(TAG_JAM);
        latitude = dataGabung.getString(TAG_LAT);
        longitude = dataGabung.getString(TAG_LONG);
        koordinator = dataGabung.getString(TAG_KOOR);
        status = dataGabung.getString(TAG_STATUS);

        vTuj.setText(namaTujuan);
        vjam.setText(jam);
        vIdGab.setText(idJan);
        vLatGab.setText(latitude);
        vLongGab.setText(longitude);
        vKoorGab.setText(koordinator);
        vStatGab.setText(status);

        pDialog = new ProgressDialog(Gabung.this);
        pDialog.setMessage("Gabung");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);

        username = loginPref.getUsername();
        btnGabung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new SimpanGabung().execute();
                pDialog.show();
                new SimpanGabung().execute();
            }
        });
        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog.show();
                new BatalGabungService(Gabung.this, Gabung.this, paramBatal());
            }
        });

        pDialog.show();
        new CekGabungService(Gabung.this, Gabung.this, params());
    }

    private Map<String, String> paramBatal() {
        Map<String, String> jsonBody = new HashMap<String, String>();
        Log.d("asd", "params: "+new LoginPref(this).getUsername());
        Log.d("asd", "params id: "+String.valueOf(UserPref.getAlamat(this)));
        jsonBody.put("id", String.valueOf(UserPref.getIdPeng(this)));
        jsonBody.put("idJan", String.valueOf(id_janjian));
        return jsonBody;
    }

    private Map<String, String> params() {
        Map<String, String> jsonBody = new HashMap<String, String>();
        Log.d("asd", "params: "+new LoginPref(this).getUsername());
        Log.d("asd", "params id: "+String.valueOf(UserPref.getAlamat(this)));
        jsonBody.put("id", String.valueOf(UserPref.getIdPeng(this)));
        return jsonBody;
    }

    // 2 = sudah gabung
    // 3 = jadi koordinator
    @Override
    public void IsGabung(Boolean isgabung, int status, int idPeng, int idJan) {
        pDialog.hide();
        if(status!=0){
            if(isgabung){
                if(id_janjian==idJan && idPeng == UserPref.getIdPeng(Gabung.this)){
                    switch (status){
                        case 2:
                            btnGabung.setVisibility(View.GONE);
                            btnBatal.setVisibility(View.VISIBLE);
                            break;
                        case 3:
                            btnGabung.setVisibility(View.GONE);
                            btnBatal.setVisibility(View.GONE);
                            break;
                    }
                }else{
                    btnGabung.setVisibility(View.GONE);
                    btnBatal.setVisibility(View.GONE);
                }
            }else{
                btnGabung.setVisibility(View.VISIBLE);
                btnBatal.setVisibility(View.GONE);
            }
        }else{
            Toast.makeText(Gabung.this, "Bermasalah dengan koneksi", Toast.LENGTH_SHORT).show();
            finish();
        }

        if(status==1){
            if (!isgabung){
                new SimpanGabung().execute();
            }else {
                pDialog.hide();
                Toast.makeText(Gabung.this, "Maaf Anda sudah mendaftar konvoi", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(Gabung.this, "Bermasalah dengan koneksi", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void OnAfterBatal(int status) {
        if(status==1){
            finish();
        }else{
            Toast.makeText(Gabung.this, "Bermasalah dengan koneksi", Toast.LENGTH_SHORT).show();
        }
    }

    class SimpanGabung extends AsyncTask<String, String, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                pDialog.show();
            }

            // menambah data
            protected String doInBackground(String... args) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                params.add(new BasicNameValuePair("idJan", idJan));
                params.add(new BasicNameValuePair("namaPeng", username));


                JSONObject json = jsonParser.makeHttpRequest(url_gabung,
                        "POST", params);

                // periksa respon log cat
                Log.d("Respon tambah lokasi", json.toString());

                try {
                    int sukses = json.getInt(TAG_SUKSES);
                    if (sukses == 1) {

                        // jika sukses menambah data baru
                        Intent i = new Intent(Gabung.this,
                                DetailJanjian.class);
                        startActivity(i);

                        // tutup activity ini
                        finish();
                    } else {
                        // jika gagal dalam menambah data
                        Toast.makeText(getApplicationContext(),"Gagal Simpan", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(String file_url) {

                pDialog.dismiss();
            }
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
}
