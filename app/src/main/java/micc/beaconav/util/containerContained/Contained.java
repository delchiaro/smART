package micc.beaconav.util.containerContained;

import android.util.Log;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class Contained<CONTAINER extends IContainer> implements IContained<CONTAINER> {

    private CONTAINER _container = null;

    @Override
    public CONTAINER getContainer() {
        return this._container;
    }

    @Override
    public void removeFromContainer() {
        if(this._container != null)
                this._container.remove(this);
    }

    @Override
    public void setContainer(CONTAINER container, Container.Key key) {
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
