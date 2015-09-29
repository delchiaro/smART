package micc.beaconav.indoorEngine.building.spot;


import micc.beaconav.indoorEngine.building.Building;
import micc.beaconav.indoorEngine.building.Floor;
import micc.beaconav.indoorEngine.building.Room;
import micc.beaconav.util.containerContained.Contained;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class Spot extends Contained<Room>
{



    public Spot(){
        this(0,0);
    }
    public Spot(Room room_container) {
        this(0,0, room_container);
    }
    public Spot(float x, float y){
        this(x, y, null);
    }
    public Spot(float x, float y, Room room_container) {
        if(room_container != null ) room_container.add(this);
        this._absolute_x = x;
        this._absolute_y = y;
    }



    private float _absolute_x; //in meters, coordinate assolute indipendenti da traslazioni, rotazioni o scaling
    private float _absolute_y;



    public float x(){ return _absolute_x; }
    public float y(){ return _absolute_y; }

    /** modifica le coordinate assolute dello spot (in metri)*/
    public void x(float x){
        this._absolute_x = x;
    }

    /** modifica le coordinate assolute dello spot (in metri)*/
    public void y(float y){
        this._absolute_y = y;
    }

    /** modifica le coordinate assolute dello spot (in metri)*/
    public void setPosition(float x, float y) {
        this.x(x);
        this.y(y);
    }








    public Room              getContainerRoom()  {
        return super.getContainer();
    }
    public Floor             getContainerFloor()      { return getContainerRoom().getContainerFloor(); }
    public Building          getBuildingContainer()   { return getContainerFloor().getContainerBuilding(); }


}