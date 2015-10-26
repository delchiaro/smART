package micc.beaconav.db.dbHelper;

import java.util.HashMap;

import micc.beaconav.db.dbHelper.artworkImage.ArtworkImageRow;
import micc.beaconav.db.dbHelper.artworkImage.ArtworkImageSchema;
import micc.beaconav.db.dbHelper.museum.MuseumRow;
import micc.beaconav.db.dbHelper.museum.MuseumSchema;
import micc.beaconav.db.dbHelper.room.VertexSchema;
import micc.beaconav.db.dbJSONManager.HttpParam;
import micc.beaconav.db.dbJSONManager.JSONDownloader;
import micc.beaconav.db.dbJSONManager.JSONHandler;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class DbManager
{


    static MuseumSchema museumSchema = new MuseumSchema();
    static ArtworkImageSchema artworkImageSchema = new ArtworkImageSchema();

//    static private final String museumJSONLink = "http://trinity.micc.unifi.it/museumapp/JSON_Museums.php";
//    static private String vertexJSONLink = "http://trinity.micc.unifi.it/museumapp/JSON_RoomVertices.php";
//    static private final String artworkJSONLink = "http://trinity.micc.unifi.it/museumapp/JSON_Artworks.php";

//    static private final String museumJSONLink = "http://whitelight.altervista.org/JSON_Museums.php";
      static private final String museumJSONLink = "http://trinity.micc.unifi.it/museumapp/JSON_Museums.php";
    static private final String artworkJSONLink = "http://trinity.micc.unifi.it/museumapp/JSON_ArtworkImagesFromMuseum.php";


    static public final JSONDownloader<MuseumRow, MuseumSchema> museumDownloader =
                            new JSONDownloader<MuseumRow, MuseumSchema>(museumSchema, museumJSONLink);


    static private HashMap<Long, JSONDownloader<ArtworkImageRow, ArtworkImageSchema>> artworkJSONDownloaders = new HashMap<>();


    public static JSONDownloader<ArtworkImageRow, ArtworkImageSchema> getArtworkDownloader(long ID_museum) {

        if(artworkJSONDownloaders.containsKey(ID_museum))
            return artworkJSONDownloaders.get(ID_museum);
        else {
            String link = artworkJSONLink + "?id_museum=" + ID_museum;
            JSONDownloader<ArtworkImageRow, ArtworkImageSchema> artworkDownloader =
                    new JSONDownloader<ArtworkImageRow, ArtworkImageSchema>(artworkImageSchema, link);

            artworkJSONDownloaders.put(ID_museum, artworkDownloader);
            return artworkDownloader;
        }

    }


}
