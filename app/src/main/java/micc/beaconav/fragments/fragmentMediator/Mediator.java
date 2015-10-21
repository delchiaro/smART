package micc.beaconav.fragments.fragmentMediator;

import java.util.ArrayList;
import java.util.List;

import micc.beaconav.fragments.mainFragment.MapFragment;
import micc.beaconav.fragments.slidingContentFragment.slidingContentDescription.ArtworkDescrFragment;
import micc.beaconav.fragments.slidingContentFragment.slidingContentDescription.MuseumDescrFragment;
import micc.beaconav.fragments.slidingContentFragment.slidingContentList.ArtListFragment;
import micc.beaconav.fragments.slidingHeaderFragment.NameHeaderFragment;
import micc.beaconav.fragments.slidingHeaderFragment.SeekBarHeaderFragment;
import micc.beaconav.indoorEngine.IndoorMapFragmentLite;

/**
 * Created by nagash on 04/03/15.
 */
public class Mediator
{


    // MAIN FRAGMENT
    protected MapFragment       main_mapFragment = null;
    protected IndoorMapFragmentLite main_indoorMapFragment = null;


    // SLIDING CONTENT FRAGMENT
    protected ArtworkDescrFragment  slidingContent_artworkDescrFragment = null;
    protected MuseumDescrFragment   slidingContent_museumDescrFragment = null;
    protected ArtListFragment       slidingContent_artListFragment = null;


    // SLIDING HEADER FRAGMENT
    protected NameHeaderFragment    slidingHeader_nameFragment = null;
    protected SeekBarHeaderFragment slidingHeader_seekBarFragment = null;




    public MapFragment get_main_mapFragment() {
        return main_mapFragment;
    }

    public IndoorMapFragmentLite get_main_indoorMapFragment() {
        return main_indoorMapFragment;
    }

    public ArtworkDescrFragment get_slidingContent_artworkDescrFragment() {
        return slidingContent_artworkDescrFragment;
    }

    public MuseumDescrFragment get_slidingContent_museumDescrFragment() {
        return slidingContent_museumDescrFragment;
    }

    public ArtListFragment get_slidingContent_artListFragment() {
        return slidingContent_artListFragment;
    }

    public NameHeaderFragment get_slidingHeader_nameFragment() {
        return slidingHeader_nameFragment;
    }

    public SeekBarHeaderFragment get_slidingHeader_seekBarFragment() {
        return slidingHeader_seekBarFragment;
    }


}
