package micc.beaconav.util.containerContained;


/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */


public interface IContainer<CONTAINED extends IContained> {



    public void add( CONTAINED newObject);
    public void remove(CONTAINED containedObject);
}
