package micc.beaconav.indoorEngine.spot.marker;

import java.util.ArrayList;

import micc.beaconav.indoorEngine.spot.drawable.DrawableSpot;
import micc.beaconav.indoorEngine.spot.marker.collidable.Collidable;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 *
 *
 *  Da Google Translate (Dictionary):
 *  Marker: an object used to indicate a position, place, or route.
 *
 *  Un marker INDICA UNA POSIZIONE O UN LUOGO, è cioè posto su una posizione, su un luogo.
 *  Poichè un marker É anche una "posizione" nel senso di coordinata (non di posizione nel senso
 *  di "specifico luogo o area con una certa caratteristica, con un certo nome" ecc.. ma solo un luogo)
 *  dal punto di vista "matematico" di COORDINATE SPAZIALI, poichè estende Spot).
 *
 *  Quindi presumibilmente queste coordinate precise indicate dal marker, rappresenteranno un punto
 *  dello spazio nel quale è PRESENTE UN LUOGO (un bar o un edificio in una mappa di una cittá,
 *  uno stanza o un'aula in una mappa di un edificio, un componente all'interno di un circuito elettrico,
 *  un componente all'interno di un motore..)
 *
 *
 *  PER QUESTO MOTIVO IL MARKER HA UN CONTAINER, OVVERO UN OGGETTO CHE LO CONTIENE.
 *  In una mappa outdoor per un marker che indica un edificio, il container potrebbe essere un oggetto
 *  che descrive il perimetro preciso dell'edificio indicato dal container.
 *  In un circuito elettrico, il marker che indica un componente potrebbe essere il componente stesso,
 *  che è poi collegato a grafo con gli altri componenti del circuito elettrico ( i marker servono
 *  solo per selezionarli e interagire con essi, e per avere un punto del piano (x, y) di riferimento).
 *  Per una mappa indoor, l'oggetto che lo contiene potrebbe essere la stanza o l'area della stanza
 *   a cui si fa riferimento o che contiene l'oggetto che il marker sta ad indicare all'interno della
 *   stanza.
 *
 *
 *
 *
 *
 */

public abstract class Marker extends DrawableSpot {

    private final boolean forceUnreplaceableContainer;
    private boolean _selected = false;


    protected ArrayList<IMarkerObserver> _observers = null;
    protected IMarkerContainer _container = null;


    public class IrreplaceableObserverException extends  Exception {}

    public Marker(float x, float y, boolean forceUnreplaceableContainer) {
        super(x, y);
        this._selected = false;
        this.forceUnreplaceableContainer = forceUnreplaceableContainer;
    }
    public Marker(float x, float y) {
        this(x, y, false);
    }



    public void addObserver(IMarkerObserver newObserver)
    {
        if( this._observers == null)
            _observers = new ArrayList<>();
        _observers.add(newObserver);
    }
    public boolean removeObserver(IMarkerObserver observer)
    {
        boolean ret = false;
        if(this._observers != null)
        {
            ret = this._observers.remove(observer);
            if(_observers.size() == 0)
                _observers = null;
        }

        return ret;
    }

    public IMarkerContainer setContainer(IMarkerContainer newContainer) throws IrreplaceableObserverException {
        if(this.forceUnreplaceableContainer && _container != null )
            throw new IrreplaceableObserverException();
        else
        {
            IMarkerContainer ret = this._container;
            this._container = newContainer;
            return ret;
        }
    }

    public void select() {
        if (this.isSelected())
            this._onReselected();
        else {
            this._selected = true;
            this._onSelected();
        }
    }

    public void deselect() {
        this._selected = false;
        this._onDeselected();
    }

    public final boolean isSelected() {
        return this._selected;
    }



    // Avverte gli observer e poi gestisce l'evento internamente (nella sottoclasse concreta ovviamente)
    private final void _onSelected() {

        if( _observers != null)
            for (IMarkerObserver obs:_observers)
                obs.onMarkerSelected(this);

        this.onSelected();
    }
    private final void _onDeselected() {
        if(_observers != null)
            for (IMarkerObserver obs:_observers)
                obs.onMarkerDeselected(this);

        this.onDeselected();
    }
    private void _onReselected() {
        if(_observers != null)
            for (IMarkerObserver obs:_observers)
                obs.onMarkerReselected(this);

        this.onReselected();
    }


    // Gestione eventi interni
    protected abstract void onReselected();
    protected abstract void onSelected();
    protected abstract void onDeselected();

    protected abstract Collidable generateCollidable(float x, float y);


    public boolean checkCollision(float x, float y) {
        return generateCollidable(x_for_drawing(), y_for_drawing()) . checkCollision(x, y);
    }





}
