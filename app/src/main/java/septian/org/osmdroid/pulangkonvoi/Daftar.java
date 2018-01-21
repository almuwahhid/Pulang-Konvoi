package septian.org.osmdroid.pulangkonvoi;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import septian.org.osmdroid.pulangkonvoi.Preferences.LoginPref;

/**
 * Created by Lenovo on 17/03/2017.
 */

public class Daftar extends AppCompatActivity {

    LoginPref loginPref;
    EditText txtNama,txtNamPeng,txtAlamat,txtEmail;

    private static String url_tambah_pengguna = "http://septianskripsi.hol.es/tambahpengguna.php";

    private static final String TAG_SUKSES = "sukses";

    private ProgressDialog pDialog;

    public String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/pulangkonvoi";

    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daftar);

        txtNama = (EditText) findViewById(R.id.inputNama);
        txtNamPeng = (EditText) findViewById(R.id.inputNamaPeng);
        txtAlamat = (EditText) findViewById(R.id.inputAlamat);
        txtEmail = (EditText) findViewById(R.id.inputEmail);

        pDialog = new ProgressDialog(Daftar.this);
        pDialog.setMessage("Tambah Pengguna");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);

        Button simpanPeng = (Button) findViewById(R.id.btnSimpanDaf);
        Button batalPeng = (Button) findViewById(R.id.btnKeluarDaf);
        loginPref = new LoginPref(this);

        /*File dir =  new File(path);
        if (!dir.exists()){
            dir.mkdirs();}*/


        simpanPeng.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                new BuatPengguna().execute();

            }
        });

        batalPeng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveTaskToBack(true);

            }
        });

    }


class BuatPengguna extends AsyncTask<String, String, Integer> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog.show();
    }

    protected Integer doInBackground(String... args) {

            String namaLeng = txtNama.getText().toString();
            String namaPeng = txtNamPeng.getText().toString();
            String alamat = txtAlamat.getText().toString();
            String email = txtEmail.getText().toString();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("namaLengkap", namaLeng));
            params.add(new BasicNameValuePair("namaPengguna", namaPeng));
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("alamat", alamat));


            JSONObject json = jsonParser.makeHttpRequest(url_tambah_pengguna,
                    "POST", params);

            // periksa respon log cat
            Log.d("Respon tambah pengguna", json.toString());

            try {
                return json.getInt(TAG_SUKSES);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
    }


    protected void onPostExecute(Integer sukses) {
        pDialog.hide();
        if (sukses == 1) {
            // jika sukses menambah data baru
            loginPref.setUsername(txtEmail.getText().toString());
            Intent i = new Intent(getApplicationContext(),
                    MainActivity.class);
            startActivity(i);
            finish();
            // tutup activity ini
            finish();
        } else if(sukses==2){
            // jika gagal dalam menambah data
            Toast.makeText(getApplicationContext(),"Email sudah terdaftar !", Toast.LENGTH_LONG).show();
        } else{
            Toast.makeText(getApplicationContext(),"Gagal Simpan", Toast.LENGTH_LONG).show();
        }
    }
}
}