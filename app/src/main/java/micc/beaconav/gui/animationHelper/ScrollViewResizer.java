package micc.beaconav.gui.animationHelper;

import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class ScrollViewResizer {

    private SlidingUpPanelLayout mSlidingUpPanelLayout;
    private RelativeLayout mRelativeLayout;

    public ScrollViewResizer(SlidingUpPanelLayout mSlidingUpPanelLayout, RelativeLayout mRelativeLayout) {
        this.mSlidingUpPanelLayout = mSlidingUpPanelLayout;
        this.mRelativeLayout = mRelativeLayout;
    }

    public void resizeScrollView(final float slideOffset) {
    // The scrollViewHeight calculation would need to change based on what views are
    // in the sliding panel. The calculation below works because the layout has
    // 2 views. 1) The row with the drag view which is layout.getPanelHeight() high.
    // 2) The ScrollView.

    if( slideOffset == 0.0f)
    {
        ViewGroup.LayoutParams params = mRelativeLayout.getLayoutParams();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        mRelativeLayout.setLayoutParams(params);
    }
    else
    {
        final int scrollViewHeight = (int) ((mSlidingUpPanelLayout.getHeight() - mSlidingUpPanelLayout.getPanelHeight()) * (1.0f - slideOffset));
        final ViewGroup.LayoutParams currentLayoutParams = mRelativeLayout.getLayoutParams();
        currentLayoutParams.height = scrollViewHeight;
        mRelativeLayout.setLayoutParams(currentLayoutParams);
    }

        //final int scrollViewHeight = (int) ((mSlidingUpPanelLayout.getHeight() - mSlidingUpPanelLayout.getPanelHeight()) * (1.0f - slideOffset));


    }
}
