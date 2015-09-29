package micc.beaconav.__test;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import micc.beaconav.R;
import micc.beaconav.db.dbJSONManager.JSONParser;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class JSONTest extends ActionBarActivity {

    ListView mListView;
    JSONObject json;
    ArrayList<String> rowsArrayList;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsontest);
        this.context = this;
        mListView = (ListView) findViewById(R.id.jsonListView);


        LoadAllProducts loadAll = new LoadAllProducts();
        loadAll.execute();



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_jsontest, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }







    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadAllProducts extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog = new ProgressDialog(AllProductsActivity.this);
//            pDialog.setMessage("Loading products. Please wait...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(false);
//            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {

            String url ="http://trinity.micc.unifi.it/museumapp/JSON_Museums.php";
            JSONParser jParser = new JSONParser();
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            JSONObject json = jParser.makeHttpRequest(url, "GET", params);
            JSONArray museumArray = null;

            try {
                museumArray = json.getJSONArray("Museum");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            rowsArrayList = new ArrayList<String>();

            if(museumArray != null)
            {
                int arrayLenght = museumArray.length();

                for(int i = 0; i < arrayLenght; i++)
                {
                    JSONObject museumJson = null;

                    try
                    {
                        museumJson = museumArray.getJSONObject(i);

                        String id = museumJson.getString("ID");
                        String latitude = museumJson.getString("latitude");
                        String longitude = museumJson.getString("longitude");

                        rowsArrayList.add("ID: " + id + "; Latitude: " + latitude + "; Longitude " + longitude);

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }


            return null;
        }



        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products


            runOnUiThread(new Runnable()
            {
                public void run()
                {

                    String[] rows = rowsArrayList.toArray(new String[rowsArrayList.size()]);
                    riempiListView(rows);


                }
            });

        }
        private void riempiListView(String[] strArray)
        {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, android.R.id.text1, strArray );

            mListView.setAdapter(adapter);
        }

    }
}
