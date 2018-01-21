package septian.org.osmdroid.pulangkonvoi;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import septian.org.osmdroid.pulangkonvoi.Kelas.Janjian;
import septian.org.osmdroid.pulangkonvoi.Preferences.LoginPref;
import septian.org.osmdroid.pulangkonvoi.Preferences.UserPref;
import septian.org.osmdroid.pulangkonvoi.Services.UpdateKonvoi.UpdateKonvoiService;

/**
 * Created by Lenovo on 17/03/2017.
 */


public class DetailJanjian extends AppCompatActivity implements UpdateKonvoiService.UpdateKonvoi{
    private TextView vTuj, vJam, vId, vLong, vLat, vKoor,vStat, vnam;
    Button btnPan;
    Button btnBerangkat;
    LoginPref loginPref;
    Janjian janjian;

    private String username;

    JSONArray member = null;
   // ArrayList<HashMap<String, String>> memberList;

    private ProgressDialog pDialog;



    //public static String url_detail_janjian = "http://septianskripsi.hol.es/tampilDetail.php";
    //public static String url_detail_janjian2 = "";

    private static final String TAG_SUKSES = "sukses";
    private static final String TAG_MEMBER = "member";
    private static final String TAG_IDJAN = "idJan";
    private static final String TAG_TUJUAN = "tujuan";
    private static final String TAG_JAM = "jam";
    private static final String TAG_LAT = "lat";
    private static final String TAG_LONG = "longit";
    private static final String TAG_KOOR = "idKoordinator";
    private static final String TAG_STATUS = "status";


    JSONParser jsonParser = new JSONParser();

    private Map<String, String> params(){
        Map<String, String> jsonBody = new HashMap<String, String>();
        jsonBody.put("id", String.valueOf(janjian.getIdJan()));
        return jsonBody;
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

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_janjian);
        loginPref = new LoginPref(this);

        getSupportActionBar().setDisplayOptions(
                ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
        getSupportActionBar().setTitle("Detail Janjian Saya");
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Tunggu Sebentar");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        janjian = new Janjian();

        vTuj = (TextView) findViewById(R.id.det_Tujuan);
        vJam = (TextView) findViewById(R.id.det_Jam);
        /*vId = (TextView) findViewById(R.id.det_idJan);
        vLong = (TextView) findViewById(R.id.det_longit);
        vLat = (TextView) findViewById(R.id.det_lat);
        vKoor = (TextView) findViewById(R.id.det_koor);
        vStat = (TextView) findViewById(R.id.det_stat);*/

      //  vnam = (TextView) findViewById(R.id.nam);

        btnPan = (Button) findViewById(R.id.btnPanduan);
        btnBerangkat = (Button) findViewById(R.id.btnBerangkat);

        btnPan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MapPanduan.class);
                i.putExtra("lat", janjian.getLat());
                i.putExtra("lng", janjian.getLng());
                startActivity(i);
            }
        });

        btnBerangkat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog.show();
                new UpdateKonvoiService(DetailJanjian.this, DetailJanjian.this, params());
            }
        });

        username = loginPref.getUsername();

        //new kirimUsername().execute();
        new Detail().execute();
    }

    @Override
    public void updateKonvoi(Boolean b, int status) {
        pDialog.hide();
        if(status==1){
            if(b){
                Toast.makeText(DetailJanjian.this, "Konvoi Berangkat, Status konvoi sudah berubah", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(DetailJanjian.this, "Ada yang bermasalah dengan server", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(DetailJanjian.this, "Ada yang bermasalah dengan server", Toast.LENGTH_SHORT).show();
        }
    }

    public class Detail extends AsyncTask<String, String, JSONObject> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog.show();
            }

            @Override
            protected JSONObject doInBackground(String... args) {

                List<NameValuePair> params = new ArrayList<>();

                String nama="?id="+ UserPref.getIdPeng(DetailJanjian.this);
               //params.add(new BasicNameValuePair("namapeng", username));

                String url_detail_janjian = "http://septianskripsi.hol.es/tampilDetail.php";
                String fixUrl=url_detail_janjian+nama;

                Log.d("asd", "doInBackground: "+fixUrl);

                 JSONObject json = jsonParser.makeHttpRequest(fixUrl, "GET",
                        params);
                return json;
            }

           @Override
            protected void onPostExecute(JSONObject json) {

                pDialog.dismiss();

               Log.d("asdasd", json.toString());

               try {

                   member = json.getJSONArray(TAG_MEMBER);
                    int sukses = json.getInt(TAG_SUKSES);
                    if (sukses == 1) {

                        JSONObject c = member.getJSONObject(0);

                        janjian.setIdJan(Integer.valueOf(c.getString(TAG_IDJAN)));
                        janjian.setTujuan(c.getString(TAG_TUJUAN));
                        janjian.setJam(c.getString(TAG_JAM));
                        janjian.setLat(c.getString(TAG_LAT));
                        janjian.setLng(c.getString(TAG_LONG));
                        janjian.setIdKoordinator(Integer.valueOf(c.getString(TAG_KOOR)));
                        janjian.setStatus(c.getString(TAG_STATUS));

                        Log.d("asdlok", "onPostExecute: "+janjian.getLat());
                        Log.d("asdlok", "onPostExecute: "+janjian.getLng());

//                        vId.setText(janjian.getIdJan());
                        vTuj.setText(janjian.getTujuan());
                        vJam.setText(janjian.getJam());
//                        vLat.setText(janjian.getLat());
//                        vLong.setText(janjian.getLng());
//                        vKoor.setText(janjian.getIdKoordinator());
//                        vStat.setText(janjian.getStatus());

                        if(janjian.getIdKoordinator() == UserPref.getIdPeng(DetailJanjian.this)){
                            btnBerangkat.setVisibility(View.VISIBLE);
                        }

                    } else {

                        Intent i = new Intent(getApplicationContext(),
                                MenuUtama.class);

                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                       startActivity(i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // update UI dari Background Thread
                runOnUiThread(new Runnable() {
                    public void run() {

                    }
                });


            }

        }
    }


