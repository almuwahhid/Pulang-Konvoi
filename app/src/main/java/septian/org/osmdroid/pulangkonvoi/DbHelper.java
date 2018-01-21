package septian.org.osmdroid.pulangkonvoi;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Lenovo on 06/10/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    // The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/septian.org.osmdroid.pulangkonvoi/databases/";
    private static String DB_NAME = "nodes";
    private SQLiteDatabase myDatabase;
    private final Context myContext;
    private boolean add2;
    private String DB_PATH(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return myContext.getApplicationContext().getDataDir()+"/databases/";
        }else{
            return "/data/data/"+myContext.getPackageName()+"/databases/";
        }
//        return "/data/data/"+myContext.getPackageName()+"/databases/";
    }

    public DbHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
            // do nothing - database already exist
        } else {

            // By calling this method and empty database will be created into
            // the default system path
            // of your application so we are gonna be able to overwrite that
            // database with our database.
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    public boolean checkDataBase() {
        /*SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH() + DB_NAME;
            Log.d("asdpath", "checkDataBase: "+myPath);
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            Log.d("asderror", "checkDataBase: "+e.getMessage());
            // database does't exist yet.
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;*/
        File dbFile = new File(DB_PATH()+DB_NAME);
        return dbFile.exists();
    }

    /**
     * Copies your database from your local assets-folder to the just created
     * empty database in the system folder, from where it can be accessed and
     * handled. This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException {
        // Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        // Path to the just created empty db
        String outFileName = DB_PATH() + DB_NAME;
        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);
        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

        // Toast.makeText(myContext, "Copy Done", 300).show();
    }

    public void openDataBase() throws SQLException {
        // Open the database
        String myPath = DB_PATH() + DB_NAME;
        myDatabase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);

    }

    @Override
    public synchronized void close() {
        if (myDatabase != null)
            myDatabase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Add your public helper methods to access and get content from the
        // database.
        // You could return cursors by doing "return myDatabase.query(....)" so
        // it'd be easy
        // to you to create adapters for your views.
    }
}
