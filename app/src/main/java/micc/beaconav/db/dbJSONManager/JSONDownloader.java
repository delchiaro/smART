package micc.beaconav.db.dbJSONManager;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.ColumnField;
import micc.beaconav.db.dbJSONManager.tableScheme.TableRow;
import micc.beaconav.db.dbJSONManager.tableScheme.TableSchema;


/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class JSONDownloader<TR extends TableRow, TS extends TableSchema<TR>> extends AsyncTask<String, String, TR[]>
{

    private TS schema;
    // private List<TR> downloadedRowsList = null;
    private TR[] downloadedRows = null;

    private List<JSONHandler<TR>> handlerList;
    private final String URL;

    private final int DOWNLOAD_NOT_STARTED = -1;
    private final int DOWNLOAD_STARTED = 0;

    long downloadIstant = DOWNLOAD_NOT_STARTED;


    HttpParam[] httpParams = null;




    public JSONDownloader(TS schema, String url, HttpParam... params ) {
        this.schema = schema;
        this.downloadedRows = null;
        this.handlerList = new ArrayList<>();
        this.URL = url;
        this.httpParams = params;
    }

    public void addHandler(JSONHandler newHandler){
        this.handlerList.add(newHandler);
        if(isDownloadFinished())
            newHandler.onJSONDownloadFinished(this.getDownloadedRows());
    }


    public boolean isDownloadFinished(){
        if(downloadIstant > DOWNLOAD_STARTED ) return true;
        else return false;
    }
    public boolean isDownloadStarted(){
        if(downloadIstant > DOWNLOAD_NOT_STARTED) return true;
        else return false;
    }
    public long getDownloadIstant(){
        return downloadIstant;
    }



    public TR[] getDownloadedRows()
    {
        if(downloadedRows != null)
            return downloadedRows;
        else return null;
    }

    public List<TR> getDownloadedRowsList()
    {
        if(downloadedRows != null)
            return Arrays.asList(downloadedRows);
        else return null;
    }





    public boolean startDownload() {
        if(isDownloadStarted()==false) {

            //this.execute(URL, schema.getName());
            this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, URL, schema.getName());

            return true;
        }
        else return false;
    }

    @Override
    protected final void onPreExecute() {
        super.onPreExecute();
        this.downloadIstant = DOWNLOAD_STARTED;
    }


    protected final TR[] doInBackground(String... args) {
        // TODO: Gestire eccezione nel caso in cui non si trovi l'oggetto json, e nel caso incuinon ci si possa connettere

        String url = args[0];
        String schemaName = args[1];

        ArrayList<TR> tableRowsList = new ArrayList<TR>();

        TR[] tableRows = null;

        JSONArray jsonArray = null;

        JSONParser jParser = new JSONParser();
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        for(int i = 0; i < httpParams.length; i++)
            params.add(this.httpParams[i]);


        JSONObject json = null;
        while(json == null)
        {
            json = jParser.makeHttpRequest(url, "GET", params);

            if(json == null)
            {
                // TODO: notifica l'utente che la connessione internet è assente o il server non è reperibile
                try
                {
                    Thread.sleep(3000); //blocca il thread per 3 secondi prima di riprovare a scaricare
                }
                catch (InterruptedException e) { e.printStackTrace(); }
            }
        }

        if(json != null)
        {
            try {
                jsonArray = json.getJSONArray(schemaName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        if (jsonArray != null)
        {
            int arrayLenght = jsonArray.length();
            tableRows = (TR[]) Array.newInstance(schema.newRow().getClass(), arrayLenght);


            for (int i = 0; i < arrayLenght; i++)
            {
                JSONObject jsonObject = null;

                try
                {
                    jsonObject = jsonArray.getJSONObject(i);
                    TR row = this.schema.newRow();

                    int nCols = this.schema.size();
                    for (int j = 0; j < nCols; j++)
                    {
                        ColumnField field = row.field(j);
                        String colName = field.columnName();
                        String jsonValue = jsonObject.getString(colName);
                        row.field(j).setParsing(jsonValue);
                    }

                    //todo: funziona??
                    //tableRowsList.add(row);
                    tableRows[i] = row;
                }
                catch (JSONException e) {  e.printStackTrace(); }
            }
        }

        return tableRows;
    }


    protected final void onPostExecute(TR[] result) {

        if(result != null) {
            this.downloadedRows = result;
            // this.downloadedRowsList = new ArrayList<TR>(Arrays.asList(result));

            downloadIstant = System.nanoTime();

            Iterator<JSONHandler<TR>> iter = this.handlerList.iterator();
            while (iter.hasNext())
                iter.next().onJSONDownloadFinished(result);
            // richiama i gestori di tutti gli handler
        }
        else
        {
            // gestire eccezione, notificare utente che non si trova la connessione internet
            // e riprovare
            // non dovrebbe mai accadere, il download riprova in loop adesso.

        }
    }

}
