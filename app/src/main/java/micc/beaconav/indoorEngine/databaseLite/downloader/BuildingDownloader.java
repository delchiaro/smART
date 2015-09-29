package micc.beaconav.indoorEngine.databaseLite.downloader;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by nagash on 15/03/15.
 */
// usually, subclasses of AsyncTask are declared inside the activity class.
// that way, you can easily modify the UI thread from here
public class BuildingDownloader extends AsyncTask<String, Integer, String> {

    private Context context;
    private BuildingDownloaderListener listener;
    private PowerManager.WakeLock mWakeLock;
    private String url;
    private String fPath;

    public static String DIR_PATH; // = "/data/data/{package_name}/databases/";
    public static String FILE_NAME = "database.db";
    public static String TEMP_DIR_PATH = Environment.getExternalStorageDirectory().getPath() + "/beaconav/";


    public BuildingDownloader(Context context, String url, BuildingDownloaderListener listener) {
        this.context = context;
        this.listener = listener;
        this.url = url;


        if(android.os.Build.VERSION.SDK_INT >= 17)
            DIR_PATH = context.getApplicationInfo().dataDir + "databases/";

        else
            DIR_PATH = "/data/data/" + context.getPackageName() + "databases/";
    }

    @Override
    protected String doInBackground(String... sUrl) {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;

        String dirPath = url.substring(url.indexOf(":/")+2);
        dirPath = dirPath.substring(0, dirPath.lastIndexOf('/')-1);

        dirPath = TEMP_DIR_PATH + dirPath;
        String filePath = dirPath + File.separator + "db.db";

        try {
            URL url = new URL(this.url);//new URL(sUrl[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();

            // download the file
            input = connection.getInputStream();



            File directory = new File(dirPath);
            directory.mkdirs();
            File file = new File(filePath);
            output = new FileOutputStream(filePath);

            // todo
//
//            if (file.exists())
//            {
//                // controlla la versione del file locale e del file sul server,
//                // se è aggiornato all'uiltima versione non scaricare il file sul server.
//            }

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                // allow canceling with back button
                if (isCancelled()) {
                    input.close();
                    return null;
                }
                total += count;
                // publishing the progress....
                if (fileLength > 0) // only if total length is known
                    publishProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);
            }



        } catch (Exception e) {
            return e.toString();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }
        this.fPath = filePath;
        return null;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // take CPU lock to prevent CPU from going off if the user
        // presses the power button during download
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
        mWakeLock.acquire();
    }


    @Override
    protected void onPostExecute(String result) {
        mWakeLock.release();
        if (result != null)
            Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
        else if( fPath != null ){
            Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show();
            this.listener.onDownloadFinished(fPath);
        }
    }









}



