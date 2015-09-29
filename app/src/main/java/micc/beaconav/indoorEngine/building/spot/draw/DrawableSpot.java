package micc.beaconav.indoorEngine.building.spot.draw;

import android.graphics.drawable.Drawable;

import micc.beaconav.indoorEngine.ProportionsHelper;
import micc.beaconav.indoorEngine.building.Room;
import micc.beaconav.indoorEngine.building.spot.Spot;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 *
 */
public abstract class DrawableSpot extends Spot
{
//    private Bitmap _bitmap;
    private final Drawable _drawable;

    public DrawableSpot() { this(0,0); }
    public DrawableSpot(float x, float y) {
        this(x, y, null);
    }
    public DrawableSpot(float x, float y, Room room_container) {
        super(x, y, room_container);
        this._drawable = generateDrawable();
        _scaleTranslation_x = x_pixel();
        _scaleTranslation_y = y_pixel();
    }



    private static float PPM = ProportionsHelper.PPM;



    /**  se si vuole scalare la dimensione fisica dello spot si usa questo parametro  */
    private float _spot_rescale_factor = 1;

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



    private float _translation_x = 0; // traslazione dell'oggetto rispetto alle coordinate assolute
    private float _translation_y = 0;

    private float _scaleTranslation_x;// traslazione dell'oggetto rispetto alle cordinate assolute
    private float _scaleTranslation_y;// dovuta a rescale del background


    public final float x_for_drawing() {
        return _scaleTranslation_x + _translation_x;
    }
    public final float y_for_drawing() {
        return _scaleTranslation_y + _translation_y;
    }


    public void resetTranslationAndScale() {
        _translation_x = 0;
        _translation_y = 0;
        _scaleTranslation_x = 0;
        _scaleTranslation_y = 0;

        _realtime_scaleTranslation_factor   = 1;
        _last_final_scaleTranslation_factor = 1;

        _spot_rescale_factor = 1;
    }


    private final void _setScaledCoords(){
        _scaleTranslation_x = x_pixel() * _last_final_scaleTranslation_factor * _realtime_scaleTranslation_factor;
        _scaleTranslation_y = y_pixel() * _last_final_scaleTranslation_factor * _realtime_scaleTranslation_factor;
    }

    /**
     * Questo metodo deve essere richiamato mentre si sta eseguendo il pinch to zoom.
     * Servirá per settare un fattore di scala momentaneo in tempo reale senza sovrascrivere
     * il vecchio valore di scala settato per l'immagine al termine del precedente pinch to zoom
     * (si andranno a moltiplicare in tempo reale)
     * @param scale fattore di scala istantaneo al momento del pinch to zoom
     */
    public void setRealtimeScaleTranslationFactor(float scale) {
        _realtime_scaleTranslation_factor = scale;
        _setScaledCoords();
    }

    /**
     * Questo metodo deve essere richiamato appena termina l'esecuzione del pinch to zoom
     * (ovvero quando le dita sono staccate dallo schermo, evento ACTION_UP).
     * Ingloberá il fattore di scala provvisorio nel fattore di scala totale dell'immagine
     * (che adesso è ferma e non necessita di un costante aggiornamento del fattore di scala)
     * e resetterá ad 1 (nullo) il fattore di scala in tempo reale.
     */
    public void setFinalScaleTranlationFactor(){
        this._last_final_scaleTranslation_factor *= _realtime_scaleTranslation_factor;
        this._realtime_scaleTranslation_factor = 1;
        _setScaledCoords();
    }



    /**  se si vuole scalare la dimensione fisica dello spot si usa questo parametro  */
    public void setScaleFactor(float scaleFactor) {
        this._spot_rescale_factor = scaleFactor;
    }
    public float getScaleFactor() { return this._spot_rescale_factor; }




    @Override
    public void x(float x) {
        super.x(x);
        _setScaledCoords();
    }

    @Override
    public void y(float y) {
        super.y(y);
        _setScaledCoords();
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        _setScaledCoords();
    }

    public float x_pixel(){ return x() * PPM; }
    public float y_pixel(){ return y() * PPM; }

    /** trasla lo spot nello spazio senza modificare le sue coordinate assolute che rimangano invariate*/
    public void setTranslation(float x, float y) {
        this._translation_x = x;
        this._translation_y = y;
        _setScaledCoords();
    }



    public Drawable drawable(){ return this._drawable; }





//    public Bitmap bitmap() { return _bitmap; }

//    public BitmapDrawable bitmapDrawable(Resources resources) {
//        return new BitmapDrawable(resources, _bitmap);
//    }
//
//    @Deprecated
//    public BitmapDrawable bitmapDrawable() {
//        return new BitmapDrawable( _bitmap);
//    }


    protected abstract Drawable generateDrawable();

//    public final void refreshBitmap() {
//
//        _bitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
//        Canvas tempCanvas = new Canvas(_bitmap);
//        _drawable.draw(tempCanvas);
//    }

}
