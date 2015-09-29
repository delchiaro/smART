package micc.beaconav.indoorEngine.beaconHelper;

import android.app.Activity;

import com.estimote.sdk.Beacon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class GoodBadBeaconProximityManager extends ABeaconProximityManager {


/*
    private final int       INIT_GOOD = 100;//100 //50
    private final int       INIT_BAD  = 400;//400

    private final int       DEFAULT_GOOD_TRESHOLD = 1200;
    private final int       DEFAULT_BAD_TRESHOLD = -800;

    private final int       DEFAULT_GOOD_MAX    = 1600;
    private final int       DEFAULT_BAD_MAX     = 2000;

    private final float     DEFAULT_GOOD_MULT   = 2;
    private final float     DEFAULT_BAD_MULT    = 2;//2
    private final float     DEFAULT_STD_DIVISOR = 1.6f;//2
    private final int       MAX_INIT_GOOD_STD_REDUCTORS = 1;
    private final int       MAX_INIT_BAD_STD_REDUCTORS = 2;//1

*/

    private final int       INIT_GOOD = 5;
    private final int       INIT_BAD  = 2;

    private final int       DEFAULT_GOOD_TRESHOLD = 25;
    private final int       DEFAULT_BAD_TRESHOLD = -5;

    private final int       DEFAULT_GOOD_MAX    = 35;
    private final int       DEFAULT_BAD_MAX     = 45;

    private final float     DEFAULT_GOOD_MULT   = 2;
    private final float     DEFAULT_BAD_MULT    = 2;
    private final float     DEFAULT_STD_DIVISOR = 1.2f;
    private final int       MAX_INIT_GOOD_STD_REDUCTORS = 1;
    private final int       MAX_INIT_BAD_STD_REDUCTORS = 1;//1



    private class BeaconScore{




        Beacon beacon;
        int goodPoints = INIT_GOOD;
        int badPoints = INIT_BAD;


        private final int goodTrashold = DEFAULT_GOOD_TRESHOLD;
        private final int badTrashold = DEFAULT_BAD_TRESHOLD;
        private final float goodMult = DEFAULT_GOOD_MULT;
        private final float badMult = DEFAULT_BAD_MULT;
        private final float stdDiv = DEFAULT_STD_DIVISOR;
        private final int maxGood = DEFAULT_GOOD_MAX;
        private final int maxBad = DEFAULT_BAD_MAX;

        public BeaconScore(Beacon beacon) {
            this.beacon = beacon;
        }

        public void goodStep(){
            goodPoints *= goodMult;
            if(goodPoints > maxGood)
                goodPoints = maxGood;
        }

        public void standardStep() {
            goodPoints /= stdDiv;
            badPoints /= stdDiv;
            if(goodPoints < INIT_GOOD / (stdDiv*MAX_INIT_GOOD_STD_REDUCTORS))
                goodPoints = (int) (INIT_GOOD / (stdDiv*MAX_INIT_GOOD_STD_REDUCTORS));
            if(badPoints < INIT_BAD / (stdDiv*MAX_INIT_BAD_STD_REDUCTORS))
                badPoints = (int) (INIT_BAD / (stdDiv*MAX_INIT_BAD_STD_REDUCTORS));

            if(goodPoints < 1) goodPoints = 1;
            if(badPoints< 1 ) badPoints = 1;
        }

        public void badStep() {
            badPoints *= badMult;
            if(badPoints > maxBad)
                badPoints = maxBad;
        }

        int getPoints() {
            return goodPoints - badPoints;
        }

        public int checkState() {
            if(getPoints() >= goodTrashold)
                return 1;
            if(getPoints() <= badTrashold)
                return -1;
            else return 0;
        }
    }


    HashMap<Integer, BeaconScore> candidates = new HashMap<>();
    BeaconScore bestScore = null;
    Beacon bestBeacon = null;




    public GoodBadBeaconProximityManager(Activity activity, BeaconBestProximityListener listener) {
        super(activity, listener);
    }

    @Override
    public void OnBeaconProximity(List<Beacon> proximityBeacons) {






        // inNearProximity: COSTRUISCO LA LISTA DI TUTTI I BEACONS ENTRO LA PROSSIMITÁ SCELTA
        ArrayList<Beacon> inNearProximityBeacons = new ArrayList<>(proximityBeacons.size());
        for(Beacon beacon : proximityBeacons)
        {
            if(isInProximity(beacon))
            {
                inNearProximityBeacons.add(beacon);
            }
        }

        // candidatesNotNear: COSTRUISCO LA MAPPA CHE CONTIENE TUTTI I CANDIDATI CHEIN QUESTO STEP NON
        // SONO NEL RAGGIO DI PROSSIMITÁ SCELTO
        HashMap<Integer, BeaconScore> candidatesNotNear = new HashMap<>(candidates);
        for(BeaconScore candidateScore : candidates.values() )
        {
            candidatesNotNear.remove(getID(candidateScore.beacon));
        }



        //  BAD STEP * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

            // FACCIO UN BAD STEP PER TUTTI I CANDIDATI NON NEL RAGGIO SCELTO, E NEL CASO LI ELIMINO
            // DAI CANDIDATO SE SONO TROPPO BAD
            for(BeaconScore candidateNotNear : candidatesNotNear.values() )
            {
                candidateNotNear.badStep();
                if (candidateNotNear.checkState() == -1)
                    this.candidates.remove(getID(candidateNotNear.beacon));
            }

            // SE IL BEST ESISTE E NON È NEL RAGGIO SCELTO FACCIO UN BAD STEP ANCHE A LUI
            if(bestBeacon != null && inNearProximityBeacons.contains(bestBeacon) == false)
            {
                bestScore.badStep();

                // controllo lo stato del bestScore, se è fuori dal treshold lo elimino dal best (non è più il best)
                if (bestScore.checkState() == -1)
                {
                    proximityBestListener.OnNoneBeaconBestProximity(bestBeacon);
                    bestScore = null;
                    bestBeacon = null;
                }
            }




        //  GOOD STEP * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

            // PRENDO IL BEACON PIÙ VICINO TRA QUELLI NEAR
            Beacon nearest = getNearestBeacon(inNearProximityBeacons);

            if( nearest != null )
            {
                // se c'è un nearest, allora lo cerco tra i candidati o nel bestBeacon

                if(bestBeacon != null && getID(nearest) == getID(bestBeacon))
                    bestScore.goodStep();

                else
                {
                    BeaconScore nearestScore;

                    if (candidates.get(getID(nearest)) != null)
                        nearestScore = candidates.get(getID(nearest));

                    else // se non è né tra i candidati né nel bestBeacon allora è un nuovo candidato
                    {
                        nearestScore = new BeaconScore(nearest);
                        this.candidates.put(getID(nearest), nearestScore);
                    }


                    nearestScore.goodStep();

                    if(nearestScore.checkState() == 1)
                    {
                        if(bestScore == null || bestScore.checkState() != 1)
                        {
                            proximityBestListener.OnNewBeaconBestProximity(nearest, bestBeacon);
                            bestScore = nearestScore;
                            bestBeacon = nearest;
                            candidates.remove(getID(bestBeacon));
                        }

                    }
                }

        }



        //  STANDARD STEP * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
        // faccio uno standard step per tutti i candidati (compresi quelli che hanno giá fatto il bad)
        // e per il best (se esiste)
        for(BeaconScore candidate : candidates.values())
        {
            candidate.standardStep();
            if (candidate.checkState() == -1)
                this.candidates.remove(getID(candidate.beacon));
        }
        if(this.bestScore != null)
            this.bestScore.standardStep();







    }


}
