package micc.beaconav.gui.animationHelper;

import android.content.Context;
import android.util.TypedValue;
import android.view.ViewGroup;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class LayoutDimensionChanger
{
    private ViewGroup layout;
    private ViewGroup.LayoutParams layoutParams;
    private Context context;

    public LayoutDimensionChanger(ViewGroup layout, Context context)
    {
        this.layout = layout;
        this.layoutParams = layout.getLayoutParams();
        this.context = context;
    }


    public void setHeight(int height)
    {
        layout.setMinimumHeight(height);
        layoutParams.height = height;
    }

    public void setWidth(int width)
    {
        layout.setMinimumWidth(width);
        layoutParams.width = width;
    }

    public void setDpHeight(int dpHeight)
    {
            this.setHeight( dpToPx(dpHeight, context) );
    }

    public void setDpWidth(int dpWidth)
    {
        this.setWidth( dpToPx(dpWidth, context));
    }



    public static int dpToPx(int dimensionInDp, Context context)
    {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dimensionInDp, context.getResources().getDisplayMetrics());
        return px;
    }
}
