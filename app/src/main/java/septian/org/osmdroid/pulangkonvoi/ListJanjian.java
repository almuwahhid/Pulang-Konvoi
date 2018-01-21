package septian.org.osmdroid.pulangkonvoi;


import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lenovo on 18/03/2017.
 */

public class ListJanjian extends ListActivity {


    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> memberList;

    private static String url_data_janjian = "http://septianskripsi.hol.es/tampilListJanjian.php";

    private ProgressDialog pDialog;


    private static final String TAG_SUKSES = "sukses";
    private static final String TAG_MEMBER = "member";
    private static final String TAG_IDJAN = "idJan";
    private static final String TAG_TUJUAN = "tujuan";
    private static final String TAG_JAM = "jam";
    private static final String TAG_LAT = "lat";
    private static final String TAG_LONG = "longit";
    private static final String TAG_KOOR = "idKoordinator";
    private static final String TAG_STATUS = "status";

    JSONArray member = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_janjian);

        memberList = new ArrayList<HashMap<String, String>>();

        pDialog = new ProgressDialog(ListJanjian.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);


        new AmbilDataJson().execute();

        ListView lv = getListView();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String idjan = ((TextView) view.findViewById(R.id.idJan))
                        .getText().toString();
                String tujuan = ((TextView) view.findViewById(R.id.namaTuj))
                        .getText().toString();
                String jam = ((TextView) view.findViewById(R.id.jam))
                        .getText().toString();
                String lat = ((TextView) view.findViewById(R.id.lat))
                        .getText().toString();
                String longit= ((TextView) view.findViewById(R.id.longit))
                        .getText().toString();
                String koordinator= ((TextView) view.findViewById(R.id.koor))
                        .getText().toString();
                String status = ((TextView) view.findViewById(R.id.stat))
                        .getText().toString();



                Intent in = new Intent(getApplicationContext(),
                        Gabung.class);
                in.putExtra(TAG_IDJAN, idjan);
                in.putExtra(TAG_TUJUAN, tujuan);
                in.putExtra(TAG_JAM, jam);
                in.putExtra(TAG_LAT, lat);
                in.putExtra(TAG_LONG, longit);
                in.putExtra(TAG_KOOR, koordinator);
                in.putExtra(TAG_STATUS, status);
                startActivity(in);

            }
        });

    }


    class AmbilDataJson extends AsyncTask<String, String, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog.show();
        }

        protected Boolean doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            JSONObject json = jParser.makeHttpRequest(url_data_janjian, "GET",
                    params);

            Log.d("List : ", json.toString());

            try {

                int sukses = json.getInt(TAG_SUKSES);
                if (sukses == 1) {

                    member = json.getJSONArray(TAG_MEMBER);

                    for (int i = 0; i < member.length(); i++) {
                        JSONObject c = member.getJSONObject(i);

                        String idJan = c.getString(TAG_IDJAN);
                        String tujuan = c.getString(TAG_TUJUAN);
                        String jam = c.getString(TAG_JAM);
                        String latit = c.getString(TAG_LAT);
                        String longit= c.getString(TAG_LONG);
                        String koor= c.getString(TAG_KOOR);
                        String status= c.getString(TAG_STATUS);

                        HashMap<String, String> map = new HashMap<String, String>();

                        map.put(TAG_IDJAN, idJan);
                        map.put(TAG_TUJUAN, tujuan);
                        map.put(TAG_JAM, jam);
                        map.put(TAG_LAT, latit);
                        map.put(TAG_LONG, longit);
                        map.put(TAG_KOOR, koor);
                        map.put(TAG_STATUS, status);


                        // menambah HashList ke ArrayList
                        memberList.add(map);
                    }
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
            // hilangkan dialog setelah mendapatkan semua data member
            pDialog.dismiss();
            if(!b){
                Toast.makeText(ListJanjian.this, "Maaf, belum ada daftar janjian tersedia", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getApplicationContext(),
                        MenuUtama.class);

                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }

            // update UI dari Background Thread
            runOnUiThread(new Runnable() {
                public void run() {

                    // update hasil parsing JSON ke ListView
                    ListAdapter adapter = new SimpleAdapter(
                            ListJanjian.this, memberList,
                            R.layout.list_item, new String[] { TAG_IDJAN,
                            TAG_TUJUAN,TAG_JAM,TAG_LAT,TAG_LONG,TAG_KOOR,TAG_STATUS }, new int[] { R.id.idJan,
                            R.id.namaTuj,R.id.jam,R.id.lat,R.id.longit,R.id.koor,R.id.stat });
                    // update listview
                    setListAdapter(adapter);
                }
            });
        }
    }
}


