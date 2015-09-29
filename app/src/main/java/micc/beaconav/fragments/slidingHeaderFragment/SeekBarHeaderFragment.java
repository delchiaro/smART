package micc.beaconav.fragments.slidingHeaderFragment;


import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import micc.beaconav.FragmentHelper;
import micc.beaconav.R;
import micc.beaconav.__external.seekBar.DiscreteSeekBar;
import micc.beaconav.outdoorEngine.Map;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */

public class SeekBarHeaderFragment extends Fragment {

    private Button backBtn = null;
    private DiscreteSeekBar discreteSeekBar = null;



    private static int SEEKBAR_MIN = 0; // NON CAMBIARE. GLI ARRAY PARTANO DA 0! NON SPRECARE MEMORIA..
    private static int SEEKBAR_MAX = 15;
    private static int SEEKBAR_STEPS = SEEKBAR_MAX - SEEKBAR_MIN + 1;
    private static int km = 1000;

    private static int values[] = new int[]
    {
        300, 500, 1*km, 3*km, 5*km, 10*km, 15*km, 20*km, 30*km, 50*km, 70*km, 100*km, 150*km, 200*km, 300*km, 500*km
    };

    private String intValueToString(int value) {
        if(value < 1000)
        {
            return Integer.toString(value) + "m";
        }
        else
        {
            return Integer.toString(value/km) + "km";
        }
    }


    public SeekBarHeaderFragment() {
        // Required empty public constructor
    }

    public void resetInitialSeekBarRadius(){
        discreteSeekBar.setProgress(2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)   {
        return inflater.inflate(R.layout.fragment_seekbar_header, container, false);


    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        backBtn = (Button)getView().findViewById(R.id.back_button);
        discreteSeekBar = (DiscreteSeekBar)getView().findViewById(R.id.seekBar);


        discreteSeekBar.setMin(SEEKBAR_MIN);
        discreteSeekBar.setMax(SEEKBAR_MAX);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentHelper.instance().getMainActivity().getSlidingUpPanelLayout().setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
            }

        });

        discreteSeekBar.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {



            @Override
            public int transform(int value) {
                return 0;
            }

            @Override
            public String transformToString(int value) {
                return intValueToString(values[value]);
            }

            @Override
            public boolean useStringTransform() {
                return true;
            }
        });



        discreteSeekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                FragmentHelper.instance().getOutdoorMap().setCircleRadius(values[value]);
            }
        });


        resetInitialSeekBarRadius();
    }


}
