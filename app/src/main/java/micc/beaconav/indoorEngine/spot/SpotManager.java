package micc.beaconav.indoorEngine.spot;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class SpotManager<S extends Spot> extends LinkedHashSet<S>
{


    public SpotManager() {
        super();
    }

    public SpotManager( Collection<S> init) {
       super(init);
    }


    public Set<S> getContainedSpots() {
        return this;
    }

    public Spot[] getContainedSpotsArray() {
        return super.toArray(new Spot[super.size()]);
    }

//
//
//    public SpotManager() {
//
//        _containedSpots = new LinkedHashSet<>();
//    }
//
//    public SpotManager( Collection<S> init) {
//
//        _containedSpots = new LinkedHashSet<>(init);
//    }
//
//
//    public Set<S> getContainedSpots() {
//        return _containedSpots;
//    }
//
//    public Spot[] getContainedSpotsArray() {
//        return _containedSpots.toArray(new Spot[_containedSpots.size()]);
//    }
//
//
//
//

//
//
//
//    public boolean addAll(Collection<S> spots) {
//        return this._containedSpots.addAll(spots);
//    }
//    public boolean add(S spot) {
//        return this._containedSpots.add(spot);
//    }
//
//    public boolean remove(S spot) {
//        return this._containedSpots.remove(spot);
//    }
//
//    public boolean contains(S spot) {
//        return this._containedSpots.contains(spot);
//    }
//
//    public int size() {
//        return this._containedSpots.size();
//    }
//
//    public iterator<S> iterator() {
//        return _containedSpots.iterator();
//    }

}
