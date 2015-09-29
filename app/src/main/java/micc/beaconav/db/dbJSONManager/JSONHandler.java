package micc.beaconav.db.dbJSONManager;

import java.util.List;

import micc.beaconav.db.dbJSONManager.tableScheme.TableRow;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public interface JSONHandler<TR extends TableRow>
{
    abstract void onJSONDownloadFinished(TR[] result);
}
