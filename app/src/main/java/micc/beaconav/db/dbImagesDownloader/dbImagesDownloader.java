package micc.beaconav.db.dbImagesDownloader;

import android.content.Context;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import micc.beaconav.FragmentHelper;

/**
 * Created by Mr_Holmes on 21/10/15.
 */
public class DbImagesDownloader {

    private WebView _emptyArtworkImage = null;
    private LinearLayout _imgGalleryContainer = null;
//
//    public void loadImage(ImageView emptyArtworkImage, String artworkImageUrl, Context context)
//    {
//        this._emptyArtworkImage = emptyArtworkImage;
//
//        ImageLoader imageLoader = ImageLoader.getInstance();
//        imageLoader.displayImage(artworkImageUrl, imageView);
//
//        // String data = "<img src='" + artworkImageUrl + "' style='max-height:100%'" + " style='max-width:100%' />";
//        // _emptyArtworkImage.loadData(data, "text/html", "utf-8");
//    }

    public void loadGallery(LinearLayout imgGalleryContainer, ArrayList<String> images)
    {
        this._imgGalleryContainer = imgGalleryContainer;
//        for(int i = 0; i < images.size(); i++)
//        {
//            //TODO:Correggere il bug che spara le immagini di dimensioni diverse
//            WebView artworkImg = new WebView(FragmentHelper.instance().getMainActivity());
//            String data = "<img src='" + images.get(i) + "' style='max-height:100%'" + " style='max-width:100%' />";
//            artworkImg.loadData(data, "text/html", "utf-8");
//            _imgGalleryContainer.addView(artworkImg);
//        }
    }

}
