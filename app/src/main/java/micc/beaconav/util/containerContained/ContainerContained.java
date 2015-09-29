package micc.beaconav.util.containerContained;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class ContainerContained<MY_CONTAINER extends IContainer, CONTAINED extends IContained>
        extends Container<CONTAINED>
    implements IContained<MY_CONTAINER>

{

    public ContainerContained() {  }
    public ContainerContained(MY_CONTAINER _container) {
        _container.add(this);
    }


    /**
     * ATTENZIONE: Copia per riferimento, la lista interna della classe Container
     * fará riferimento alla classe initList!!!
     * @param initList
     */
    public ContainerContained( MY_CONTAINER _container, List<CONTAINED> initList) {
        super(initList);
        _container.add(this);
    }

    private MY_CONTAINER _container = null;

    @Override
    public MY_CONTAINER getContainer() {
        return this._container;
    }

    @Override
    public void removeFromContainer() {
        if(this._container != null)
            this._container.remove(this);
    }

    @Override
    public void setContainer(MY_CONTAINER container, Container.Key key) {
        try{
            key.hashCode();
        }
        catch(Exception e){
            Log.e("BEACONAV_UTIL_CONTAINER_CONTAINED",
                    "FRIEND SIMULATION ERROR: Method setContainer can be called by class Container<C> only.", e);
        }

        this._container = container;
    }

    @Override
    public void unsetContainer(Container.Key key) {
        this._container = null;

    }

}
