package micc.beaconav.db.dbImagesDownloader;

import android.webkit.WebView;

/**
 * Created by Mr_Holmes on 21/10/15.
 */
public class DbImagesDownloader {

    //TODO:Implementarlo come singleton, lo istanzio una volta ed espongo il metodo che fa il download. O come classe statica?
    private String _artworkImageUrl = null;
    private WebView _emptyArtworkImage = null;

    public void getImage(WebView emptyArtworkImage, String artworkImageUrl)
    {
        this._emptyArtworkImage = emptyArtworkImage;
        this._artworkImageUrl = artworkImageUrl;
        String data = "<img src='" + _artworkImageUrl + "' style='max-height:95%'" + " style='max-width:95%' />";
        _emptyArtworkImage.loadData(data, "text/html", "utf-8");
    }

}
