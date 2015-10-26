package micc.beaconav.indoorEngine.building;

import android.graphics.Canvas;

import com.google.common.collect.HashBiMap;
import com.google.zxing.qrcode.encoder.QRCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import micc.beaconav.indoorEngine.ArtworkPosition;
import micc.beaconav.indoorEngine.ArtworkRow;
import micc.beaconav.indoorEngine.beaconHelper.BeaconAddress;
import micc.beaconav.indoorEngine.dijkstraSolver.DijkstraNodeAdapter;
import micc.beaconav.indoorEngine.dijkstraSolver.PathSpot;
import micc.beaconav.indoorEngine.spot.marker.MarkerManager;
import micc.beaconav.indoorEngine.dijkstraSolver.PathSpotManager;
import micc.beaconav.indoorEngine.dijkstraSolver.DijkstraSolver;
import micc.beaconav.util.containerContained.Container;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class Building extends Container<Floor>
{

  //  LatLng p1;
  //  LatLng p2; // Il building è un rettangolo definito tra due punti p1 e p2

	private float width; // in  metri
    private float height; // in metri

    private TreeMap<Integer, Floor> floorList;
    private int _activeFloor;

    DijkstraSolver<PathSpot> dijkstraSolver = new DijkstraSolver<>();
    PathSpotManager<PathSpot> dijkstraPath = null;


    private HashBiMap<String, Position> QRCodePositionMap = HashBiMap.create();
    private HashBiMap<BeaconAddress, Position> BeaconPositionMap = HashBiMap.create();



    public List<Position> getAllPositions() {

        LinkedList<Position> positions = new LinkedList<Position>();
        for(Floor f : this)
            for(Room r: f)
                for( ConvexArea ca : r)
                    for( Position p : ca) {
                        positions.addLast(p);
                    }
        return positions;
    }

    public Building(int width, int height)  {
        this.width = width;
        this.height = height;
        _activeFloor = 0;
        floorList = new TreeMap<Integer, Floor>();
	}

    public float    getWidth(){
        return width;
    }
    public float    getHeight(){
        return height;
    }

    public HashBiMap<String, Position> getQRCodePositionMap() {
        return QRCodePositionMap;
    }
    public HashBiMap<BeaconAddress, Position> getBeaconPositionMap() {
        return BeaconPositionMap;
    }


    public Floor getActiveFloor() {
        return get(this._activeFloor);
    }

    public MarkerManager getActiveMarkerManager() {
        return this.getActiveFloor().getMarkerManager();
    }

    public PathSpotManager<PathSpot> drawBestPath( PathSpot startSpot, PathSpot goalSpot) {

        ArrayList<DijkstraNodeAdapter> dijkstraNodesPath =  dijkstraSolver.solve(startSpot, goalSpot);

        if( dijkstraNodesPath != null )
        {
            dijkstraPath = new PathSpotManager(dijkstraNodesPath);

            MarkerManager markerManager = getActiveMarkerManager();
            if (markerManager != null) {
                dijkstraPath.resetAllTranslationAndScale();
                dijkstraPath.translate(markerManager.get_translation_x(), markerManager.get_translation_y());
                dijkstraPath.translateByRealtimeScaling(markerManager.get_last_final_scaleTranslation_factor());
                dijkstraPath.holdScalingFactor();
                dijkstraPath.translateByRealtimeScaling(markerManager.get_realtime_scaleTranslation_factor());
            }
        }
        else
        {
            dijkstraPath = new PathSpotManager<>();
        }

        return dijkstraPath;

    }


    public void draw(Canvas canvas, int floorIndex) {
        super.get(floorIndex).draw(canvas);
    }
    public void draw(Canvas canvas)
    {
        this.draw(canvas, this._activeFloor);
    }


}