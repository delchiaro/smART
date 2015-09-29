package micc.beaconav.indoorEngine;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import micc.beaconav.R;

/**
 * Created by nagash on 15/03/15.
 */
public class IndoorMapFragmentLite extends Fragment
{


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_indoor_map_lite, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        IndoorMap map = new IndoorMap(this.getActivity(), "http://trinity.micc.unifi.it/museumapp/database.sqlite");


        TextView tv = (TextView) this.getActivity().findViewById(R.id.textView7);
        tv.setText("downloading");
    }

}
