package micc.beaconav.indoorEngine.spot;


/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class Spot
{
// NB: IN FUTURO POTREBBE ESSERE UN PUNTO NELLO SPAZIO N-DIMENSIONALE NON SOLO 2-DIEMNSIONALE.
// SI POTREBBE SCEGLIERE LA DIMENSIONE TRAMITE MECCANISMO DI TEMPLATE O altro.


    public Spot(){
        this(0,0);
    }
    public Spot(float x, float y) {
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



}