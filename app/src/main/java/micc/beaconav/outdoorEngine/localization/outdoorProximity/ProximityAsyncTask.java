package micc.beaconav.outdoorEngine.localization.outdoorProximity;

import android.os.AsyncTask;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class ProximityAsyncTask extends AsyncTask<String, String, ProximityObject> {

    ProximityManager manager;

    public ProximityAsyncTask(ProximityManager manager){
        this.manager = manager;
    }




    @Override
    protected ProximityObject doInBackground(String... params) {

        ProximityAnalysisTask task = null;

            do {
                task = manager.tasks.pollFirst();

                if (task == null) {
                    try {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e) {
                        continue;
                    }

                }

            } while (task == null);

            ProximityObject result = task.startAnalysis();

        return result;
    }

    @Override
    protected void onPostExecute(ProximityObject result) {
        super.onPostExecute(result);
        manager.onProximityAnalysisExecuted(result);

    }
}


