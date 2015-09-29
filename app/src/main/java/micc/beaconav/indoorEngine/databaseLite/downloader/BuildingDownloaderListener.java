package micc.beaconav.indoorEngine.databaseLite.downloader;

/**
 * Created by nagash on 15/03/15.
 */
public interface BuildingDownloaderListener {
    public void onDownloadFinished(String downloadedFilePath);
}
