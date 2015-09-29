package micc.beaconav.indoorEngine.building.spot;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class SpotManager<S extends Spot>
{


    LinkedHashSet<S> _containedSpots;

    public SpotManager() {
        _containedSpots = new LinkedHashSet<>();
    }

    public SpotManager( Collection<S> init) {
        _containedSpots = new LinkedHashSet<>(init);
    }



    public boolean addAll(Collection<S> spots) {
       return this._containedSpots.addAll(spots);
    }
    public boolean add(S spot) {
        return this._containedSpots.add(spot);
    }

    public boolean remove(S spot) {
        return this._containedSpots.remove(spot);
    }

    public boolean contains(S spot) {
        return this._containedSpots.contains(spot);
    }

    public int size() {
        return this._containedSpots.size();
    }

    public Iterator<S> iterator() {
        return _containedSpots.iterator();
    }

    public Set<S> getContainedSpots() {
        return _containedSpots;
    }

    public Spot[] getContainedSpotsArray() {
        return _containedSpots.toArray(new Spot[_containedSpots.size()]);
    }

}
