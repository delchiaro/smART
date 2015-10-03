package micc.beaconav.indoorEngine.spot.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import micc.beaconav.indoorEngine.spot.SpotManager;


/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 *
 */
public class DrawableSpotManager<DS extends DrawableSpot> extends SpotManager<DS>
{



    private Drawable _wrapperDrawable = null;


    public DrawableSpotManager() {
        super();
    }


    public DrawableSpotManager( List<DS> initList) {
        super();
        this.addAll(initList);
    }


    @Override
    public boolean add(DS spot) {
        boolean superResult = super.add(spot);
        spot.setTranslation(this._translation_x, this._translation_y);
        spot.setRealtimeScaleTranslationFactor(this._last_final_scaleTranslation_factor);
        spot.setFinalScaleTranlationFactor();
        spot.setRealtimeScaleTranslationFactor(this._realtime_scaleTranslation_factor);
        return superResult;
    }

    @Override
    public boolean addAll(Collection<? extends DS> spots) {
        boolean result = true;

        for(DS spot : spots){
            if(this.add(spot) == false)
                result = false;
        }
        return true;
    }

    public float get_translation_x() {
        return _translation_x;
    }

    public float get_translation_y() {
        return _translation_y;
    }

    public float get_realtime_scaleTranslation_factor() {
        return _realtime_scaleTranslation_factor;
    }

    public float get_last_final_scaleTranslation_factor() {
        return _last_final_scaleTranslation_factor;
    }

    private float _translation_x = 0; // traslazione dell'oggetto rispetto alle coordinate assolute
    private float _translation_y = 0;

    /** scale factor in tempo reale (quando l'utente sta scalando in tempo reale con delle gestures)
     * del background che implica una traslazione delle coordinate
     * per mantenere la posizione dello spot in pixel reali invariata rispetto all punto di aggancio
     * sul background che è stato scalato */
    private float _realtime_scaleTranslation_factor = 1;


    /** scale factor del background che implica una traslazione delle coordinate
     * per mantenere la posizione dello spot in pixel reali invariata rispetto all punto di aggancio
     * sul background che è stato scalato. Il realtime viene settato sul last_final quando l'utente
     * termina la gesture, in modo da memorizzare l'ultimo livello di scale. Il realtime verá quindi
     * settato ad 1 in quel momento */
    private float _last_final_scaleTranslation_factor = 1;




    /**
     *  Da richiamare quando si termina il pinch to zoom. Setta la translazione dovuta allo zoom
     *  (scaling) di ogni spot prendendo come scaling factor l'ultimo scaling factor in real time
     *  settato.
     */
    public final void holdScalingFactor() {
        this._last_final_scaleTranslation_factor *= _realtime_scaleTranslation_factor;
        this._realtime_scaleTranslation_factor = 1;


        Iterator<DS> spotIter =  super.iterator();
        while(spotIter.hasNext())
            spotIter.next().setFinalScaleTranlationFactor();

        this.invalidate();
    }

    /**
     *  Da richiamare quando si esegue un rescale in tempo reale, come per esempio un pinch to zoom.
     *  modificherá la posizione di ogni spot in modo che le proprie coordinate seguano il rescale della mappa.
     *  La dimensione dei DrawableSpot non verrá influenzata dal rescale, solo la posizione verrá influenzata.
     *  In  memoria ogni drawable ha una posizione relativa allo scale factor ed una posizione assoluta (in metri)
     *  che non è influenzata in alcun modo dallo scale factor.
     */
    public final void translateByRealtimeScaling(float realtimeScaleFactor) {
        _realtime_scaleTranslation_factor = realtimeScaleFactor;



        Iterator<DS> spotIter =  super.iterator();
        while(spotIter.hasNext())
            spotIter.next().setRealtimeScaleTranslationFactor(realtimeScaleFactor);

        this.invalidate();
    }

    /**
     * Trasla la posizione modificando le coordiante relative all'immagine, le coordinate assolute
     * (che potrebbero essere espresse in metri o altro) non sono toccate.
     * Si può pensare che trasli la posizione di ogni spot sul telefono, ma non trasla la posizione
     * degi spot dal punto di vista di coordinate reali degli oggetti che gli spot rappresentano
     * (come markers su una mappa).
     * Per modificare la posizione reale si usino sui singoli spot i setter x(float newX) e y(float newY)
     * e per apprezzare tali modifiche sul disegno si dovrá provvedere a richiamare invalidateSelf() di ogni
     * spot a cui abbiamo modificato tali valori.
     *
     * @param translation_x
     * @param translation_y
     */
    public final void translate(float translation_x, float translation_y) {
        this._translation_x = translation_x;
        this._translation_y = translation_y;

        Iterator<DS> spotIter =  super.iterator();
        while(spotIter.hasNext())
            spotIter.next().setTranslation(translation_x, translation_y);

        this.invalidate();
    }

    public void invalidate() {
        if(this._wrapperDrawable != null )
            this._wrapperDrawable.invalidateSelf();
    }

    public void resetAllTranslationAndScale() {
        Iterator<DS> iter = iterator();
        while(iter.hasNext())
            iter.next().resetTranslationAndScale();
    }


    protected Drawable generateWrapperDrawable() {
        return new Drawable() {
            @Override
            public void draw(Canvas canvas) {
                Iterator<DS> spotIter = iterator();
                while(spotIter.hasNext())
                {
                    DS spot = spotIter.next();
                    spot.drawable().draw(canvas);
                }
            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(ColorFilter cf) {

            }

            @Override
            public int getOpacity() {
                return 0;
            }
        };
    }

    public final Drawable newWrapperDrawable(){
        return _wrapperDrawable = generateWrapperDrawable();
    }

    public final Drawable getWrapperDrawable() {
        return this._wrapperDrawable;
    }




}
