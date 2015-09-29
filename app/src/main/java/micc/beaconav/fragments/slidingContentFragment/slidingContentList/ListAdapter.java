package micc.beaconav.fragments.slidingContentFragment.slidingContentList;

import android.view.View;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import micc.beaconav.FragmentHelper;
import micc.beaconav.R;
import micc.beaconav.db.dbHelper.IArtRow;
import micc.beaconav.db.dbHelper.artwork.ArtworkRow;
import micc.beaconav.db.dbHelper.museum.MuseumRow;
import micc.beaconav.db.timeStatistics.TimeStatisticsManager;

import java.util.List;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
//A questo ListAdapter non interessa di che stiamo parlando (musei o opere),
//basta dargli dati che corrispondano al ViewHolder (si può fare in comune fra opere e musei in una ViewHolder grossa
// poi si gestisce quali visualizzare o no)
//a passargli questi dati, impacchettati in un ArtListItem, ci pensa ArtListFragment.

public class ListAdapter extends BaseAdapter {


    private Context context;
    private List<IArtRow> list;

    private class ViewHolder
    {
        ImageView _navButton;
        TextView _artPieceName;
        TextView _timeStat;
        //Altri elementi visuali qui e vanno ovviamente agganciati all'xml
    }

    public ListAdapter(Context context, List<IArtRow> list) {
        this.context = context;
        this.list = list;
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            // qui aggancio i vari componenti dell'oggetto base
            // della lista nell'xml
            holder._artPieceName = (TextView) convertView.findViewById(R.id.art_museum_name);
            holder._artPieceName.setSelected(true);
            holder._timeStat =(TextView) convertView.findViewById(R.id.time_stat);
            holder._timeStat.setSelected(true);
            holder._navButton = (ImageView) convertView.findViewById(R.id.navButton);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        final IArtRow artRow = (IArtRow) getItem(position);

        holder._artPieceName.setText(artRow.getName());
        //holder._navButton.setImageResource(artRow.getImageId());

        long time = TimeStatisticsManager.readInAppTime(artRow);
        holder._timeStat.setText("time: " + Long.toString(time)); //TODO Metterci la statistica sul tempo trascorso dentro


        final IArtRow currentRow = list.get(position);


        holder._navButton.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if( currentRow instanceof MuseumRow)
                {
                    FragmentHelper.instance().navigateToMuseumOnBtnClick((MuseumRow) currentRow, v);
                    FragmentHelper.instance().getMainActivity().getSlidingUpPanelLayout().setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }
                else if (currentRow instanceof ArtworkRow){
                    FragmentHelper.instance().indoorMapFragment.simulateArtSpotSelection((ArtworkRow)currentRow);
                    FragmentHelper.instance().indoorMapFragment.navigateToSelectedMarker();
                }
            }
        });

        //Questo listener è inutile, è già settato nell'artListFragment
//        holder._artPieceName.setOnClickListener( new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if( currentRow instanceof MuseumRow)
//                {
//                    FragmentHelper.instance().simulateMuseumOnMapClick((MuseumRow) currentRow);
//                }
//                //else {setta il listener per l'indoor per il focus sull'opera d'arte voluta}
//            }
//        });

        return convertView;

    }

  
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }


}