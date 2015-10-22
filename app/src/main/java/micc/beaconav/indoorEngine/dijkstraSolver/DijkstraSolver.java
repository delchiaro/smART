package micc.beaconav.indoorEngine.dijkstraSolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class DijkstraSolver< DNA extends DijkstraNodeAdapter> {




    private DijkstraNodeAdapter getBestPredecessor(DijkstraNodeAdapter node) {
        return node.getDijkstraStatistic().getBestPredecessor();
    }
    private double getBestWeight(DijkstraNodeAdapter node) {
        return node.getDijkstraStatistic().getBestWeight();
    }



    private boolean trySetPredecessor(DijkstraNodeAdapter node, DijkstraNodeAdapter tryToBePredecessorNode) {

        DijkstraNodeAdapter bestPredecessor = node.getDijkstraStatistic().getBestPredecessor();
        double newWeight = getBestWeight(tryToBePredecessorNode) +  tryToBePredecessorNode.getArchWeight(node);
        if(bestPredecessor == null || newWeight < getBestWeight(node))
        {
            node.getDijkstraStatistic().setBestPredecessor(tryToBePredecessorNode);
            node.getDijkstraStatistic().setBestWeight(newWeight);
            return true;
        }
        else return false;
    }




    public ArrayList<DijkstraNodeAdapter> solve( DNA start, DNA goal)
    {
        start.getDijkstraStatistic().reset();
        goal.getDijkstraStatistic().reset();


        DijkstraComparator comparator = new DijkstraComparator();


        // non utile per l'algoritmo, ma serve per resettare le statistiche a fine algoritmo
        HashSet<DijkstraNodeAdapter> nodeAdapterVisited = new HashSet<>();

        TreeSet<DijkstraNodeAdapter> frontiera = new TreeSet<DijkstraNodeAdapter>(comparator);

        start.getDijkstraStatistic().setAsStartPoint();

        List<? extends DijkstraNodeAdapter> adjacent = start.getAdjacent();
        nodeAdapterVisited.add(start);
        for(DijkstraNodeAdapter node : adjacent)
        {
            if(nodeAdapterVisited.contains(node)==false)
            {
                nodeAdapterVisited.add(node);
                node.getDijkstraStatistic().reset();
            }
            if( trySetPredecessor(node, start) )
            {
                frontiera.add(node);
            }
            // se il grafo è ben fatto, non dovrebbe mai entrare in 'else return null; '
            // Se un nodo del grafo è collegato due volte ad un altro nodo, entra qua dentro..
            else return null;   // dovrebbe essere sempre true alla partenza
        }


        while(frontiera.isEmpty() == false && goal.getDijkstraStatistic().isPermanentNode() == false)
        {
            DijkstraNodeAdapter<DNA> bestInFrontiera = frontiera.pollFirst();
            bestInFrontiera.getDijkstraStatistic().setAsPermanent();

            for(DijkstraNodeAdapter node : bestInFrontiera.getAdjacent())
            {
                if(nodeAdapterVisited.contains(node)==false)
                {
                    nodeAdapterVisited.add(node);
                    node.getDijkstraStatistic().reset();
                }
                if(node.getDijkstraStatistic().isPermanentNode() == false)
                {
                    if (trySetPredecessor(node, bestInFrontiera))
                    {
                        frontiera.add(node);
                    }
                }
            }
        }




        // creo la lista da ritornare, il risultato
        int nSteps = goal.getDijkstraStatistic().get_nPredecessor();
        int nNodes = nSteps + 1;
        ArrayList<DijkstraNodeAdapter> bestPath = new ArrayList<>(nNodes);
        DijkstraNodeAdapter iter = goal;
        int index = nNodes-1;
        do {
            iter.setPathIndex(index);
            bestPath.add(iter);
            iter = iter.getDijkstraStatistic().getBestPredecessor();
            index--;
        }while(iter != null);

        Collections.reverse(bestPath);

//        for( DijkstraNodeAdapter node : nodeAdapterVisited )
//        {
//            node.getDijkstraStatistic().reset();
//        }
        // resetto tutte le statistiche dei nodi toccati

        return bestPath;

    }
}
