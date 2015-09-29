package micc.beaconav.db.dbHelper;

import java.util.HashMap;

import micc.beaconav.db.dbHelper.artwork.ArtworkRow;
import micc.beaconav.db.dbHelper.artwork.ArtworkSchema;
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

    static private final String museumJSONLink = "http://trinity.micc.unifi.it/museumapp/JSON_Museums.php";
    static public final JSONDownloader<MuseumRow, MuseumSchema> museumDownloader =
                            new JSONDownloader<MuseumRow, MuseumSchema>(museumSchema, museumJSONLink);




    static private final String artworkJSONLink = "http://trinity.micc.unifi.it/museumapp/JSON_Artworks.php";
    static public JSONDownloader<ArtworkRow, ArtworkSchema> lastArtworkDownloader = null;
    public static JSONDownloader<ArtworkRow, ArtworkSchema> getArtworkDownloader(MuseumRow museum,
                                                                                 JSONHandler<ArtworkRow> handler) {

        HttpParam param = new HttpParam("id_museum",Long.toString(museum.ID.getValue()));
        JSONDownloader<ArtworkRow, ArtworkSchema> artworkDownloader =
                                        new JSONDownloader<>(new ArtworkSchema(), artworkJSONLink, param );
        artworkDownloader.addHandler(handler);
        artworkDownloader.startDownload();
        lastArtworkDownloader = artworkDownloader;
        return artworkDownloader;
    }

    public static JSONDownloader<ArtworkRow, ArtworkSchema> getLastArtworkDownloader() {
        return lastArtworkDownloader;
    }







    static private String vertexJSONLink = "http://trinity.micc.unifi.it/museumapp/JSON_RoomVertices.php";
    static private HashMap<Integer, JSONDownloader> vertexFromRoomDownloaders = new HashMap<>();

    static public JSONDownloader getVertexFromRoomDownloader(int roomID)
    {
        JSONDownloader dl;
        if(vertexFromRoomDownloaders.get(roomID) == null)
        {
            //vertexJSONLink += "id=" + roomID; // TODO: implementare id in php
            dl = new JSONDownloader(new VertexSchema(), vertexJSONLink);
            vertexFromRoomDownloaders.put(roomID, dl);
        }
        else dl = vertexFromRoomDownloaders.get(roomID);
        return dl;
    }


}
