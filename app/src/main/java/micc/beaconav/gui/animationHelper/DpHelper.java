package micc.beaconav.gui.animationHelper;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class DpHelper
{
    private Context context;
    public DpHelper(Context context)
    {
        this.context = context;
    }
    public int dpToPx(int dimensioninDp)
    {
        return dpToPx(dimensioninDp, this.context);
    }

    public static final int dpToPx(int dp, Context context)
    {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return px;
    }

    public  static int spToPx(int sp, Context context) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
        return px;
    }

    public static int dpToSp(int dp, Context context) {
        int sp = (int) ((float) dpToPx(dp, context) / (float) spToPx(dp, context));
        return sp;
    }
}
