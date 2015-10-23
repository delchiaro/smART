package micc.beaconav.fragments.slidingContentFragment.slidingContentList;


import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;

import micc.beaconav.FragmentHelper;
import micc.beaconav.R;
import micc.beaconav.db.dbHelper.IArtRow;
import micc.beaconav.db.dbHelper.museum.MuseumRow;
import micc.beaconav.indoorEngine.ArtworkRow;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */

public class ArtListFragment extends Fragment
{

    private boolean         listInflated = false;
    private ListView        listView     = null;
    private List<IArtRow>   iArtRowList  = null;


    public ArtListFragment() {}


//    //probabilmente questo metodo non serve
//    @Override
//    public void onAttach(Activity parentActivity) {
//        super.onAttach(parentActivity);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_art_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView = (ListView) getView().findViewById(R.id.descriptionList);
        refreshList();
    }




// * * * * * * * * * * * * * * *  HELPERS * * * * * * * * * * * * * * * * * * * * *

    public void insertRows(final IArtRow[] result) {

        iArtRowList = new ArrayList<>();
        for (int i = 0; i < result.length; i++) {
            iArtRowList.add(result[i]);
        }
        inflateList();
    }
    public void insertRows(final List<IArtRow> result) {
        iArtRowList = new ArrayList<>();
        for(IArtRow row : result )
            iArtRowList.add(row);
        inflateList();
    }

    public void refreshList() {
        listInflated = false;
        inflateList();
    }

    private void inflateList() {

        if(iArtRowList != null && listView != null && listInflated == false)
        {
            ListAdapter adapter = new ListAdapter(getActivity(), iArtRowList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new ListView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    IArtRow artRow = iArtRowList.get(position);

                    if (artRow instanceof MuseumRow) {
                        FragmentHelper.instance().simulateMuseumOnMapClick((MuseumRow) artRow);
                    } else if (artRow instanceof ArtworkRow) {
                        FragmentHelper.instance().showArtworkDetailsFragment((ArtworkRow) artRow);
                    }

                }
            });

            listView.setItemsCanFocus(true);

            listInflated = true;
        }
    }

}