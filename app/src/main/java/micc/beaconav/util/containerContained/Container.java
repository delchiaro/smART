package micc.beaconav.util.containerContained;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class Container<CONTAINED extends IContained>  implements IContainer<CONTAINED>, Iterable<CONTAINED> {

    protected List<CONTAINED> _containedObjects = new ArrayList<>();

    @Override
    public Iterator<CONTAINED> iterator() {
        return _containedObjects.iterator();
    }


    public static class Key { private Key() {} }
    private static Key key = new Key();


    public Container(){}

    /**
     * ATTENZIONE: Copia per riferimento, la lista interna della classe Container
     * fará riferimento alla classe initList!!!
     * @param initList
     */
    public Container(List<CONTAINED> initList) {
        this._containedObjects = initList;
    }

    public void addAll(List<CONTAINED> objectList) {
        this._containedObjects.addAll(objectList);
    }

    @Override
    public void add(CONTAINED newObject) {
        newObject.unsetContainer(key);
        this._containedObjects.add(newObject);
        newObject.setContainer(this, key);
    }

    public void add(int location, CONTAINED newObject) {
        newObject.unsetContainer(key);
        this._containedObjects.add(location, newObject);
        newObject.setContainer(this, key);
    }



    @Override
    public void remove(CONTAINED containedObject){

        if(containedObject != null && containedObject.getContainer() != null && containedObject.getContainer() == this)
        {
            this._containedObjects.remove(containedObject);
            containedObject.unsetContainer(key);
        }
    }

    public void remove(int location){

        CONTAINED contained = _containedObjects.get(location);
        if(contained != null && contained.getContainer() != null && _containedObjects.get(location).getContainer() == this)
        {
            this._containedObjects.remove(location);
            contained.unsetContainer(key);
        }
    }

    public List<CONTAINED> getListClone() {
        return new ArrayList<CONTAINED>(_containedObjects);
    }


    public CONTAINED get(int location) {
        return _containedObjects.get(location);
    }

    public CONTAINED set(int location, CONTAINED replacing) {
        return this._containedObjects.set(location, replacing);
    }


    public int size() {
        return _containedObjects.size();
    }
}
