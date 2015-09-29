package micc.beaconav.indoorEngine.dijkstraSolver;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class DijkstraStatistics
{
    private boolean _isPermanent;
    private DijkstraNodeAdapter _bestPredecessor;
    private double _bestWeight;
    private int _nPredecessor;




    public DijkstraStatistics() {
        reset();
    }
    public void reset() {
        _isPermanent = false;
        _bestPredecessor = null;
        _bestWeight = Double.POSITIVE_INFINITY;
        _nPredecessor = -1;
    }


    void setAsStartPoint() {
        _isPermanent = true;
        _nPredecessor = 0;
        _bestWeight = 0.;
        _bestPredecessor = null;
    }

    DijkstraNodeAdapter getBestPredecessor() {
        return this._bestPredecessor;
    }
    void setBestPredecessor(DijkstraNodeAdapter newBestPredecessor) {
        this._bestPredecessor = newBestPredecessor;
    }
    void setBestWeight(double newBestWeight) {
        this._bestWeight = newBestWeight;
    }
    double getBestWeight() {
        return _bestWeight;
    }


    public int get_nPredecessor(){
        return _nPredecessor;
    }
    void setAsPermanent() {
        this._isPermanent = true;
        this._nPredecessor = getBestPredecessor().getDijkstraStatistic().get_nPredecessor() + 1;
    }
    boolean isPermanentNode() {
        return this._isPermanent;
    }




}
