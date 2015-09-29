package micc.beaconav.indoorEngine.dijkstraSolver;

import java.util.Comparator;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class DijkstraComparator implements Comparator<DijkstraNodeAdapter> {

    @Override
    public int compare(DijkstraNodeAdapter lhs, DijkstraNodeAdapter rhs) {
        if( lhs.getDijkstraStatistic().getBestWeight() > rhs.getDijkstraStatistic().getBestWeight() )
            return 1;
        else if( lhs.getDijkstraStatistic().getBestWeight() < rhs.getDijkstraStatistic().getBestWeight() )
            return -1;
        else return 0;
    }
}