package septian.org.osmdroid.pulangkonvoi;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Splashscreen extends Activity {

    private static int splashscreeninterval = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splashscreen);

        System.out.println(getExternalCacheDir());

        new Handler().postDelayed(new Runnable() {@Override
        public void run() {
            // TODO Auto-generated method stub
            Intent i = new Intent(Splashscreen.this, MainActivity.class);
            startActivity(i);
            finish();


            //jeda selesai Splashscreen
            this.finish();
        }

            private void finish() {

            }
        }, splashscreeninterval);

    }
}


