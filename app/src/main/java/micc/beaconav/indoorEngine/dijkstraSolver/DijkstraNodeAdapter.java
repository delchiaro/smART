package micc.beaconav.indoorEngine.dijkstraSolver;

import java.util.List;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public interface DijkstraNodeAdapter<DNA extends  DijkstraNodeAdapter> {

    public double getArchWeight(DNA adjacentNodeAdapter);
    public List<? extends DijkstraNodeAdapter> getAdjacent();

    void setPathIndex(int index);

    public DijkstraStatistics getDijkstraStatistic();

}