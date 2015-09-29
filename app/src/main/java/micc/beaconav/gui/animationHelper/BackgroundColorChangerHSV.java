package micc.beaconav.gui.animationHelper;

import android.graphics.Color;
import android.view.View;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class BackgroundColorChangerHSV
{

    private View view;
    private Color color;
    float hsv[] = new float[3];


    public BackgroundColorChangerHSV(View view, int baseR, int baseG, int baseB)
    {
        this.view = view;
        Color.RGBToHSV(baseR, baseG, baseB, hsv);
    }

    public BackgroundColorChangerHSV(View view, float[] hsv)
    {
        this.view = view;
        this.hsv = hsv;
    }

    public float getH()
    {
        return hsv[0];
    }
    public float getS()
    {
        return hsv[1];
    }
    public float getV()
    {
        return hsv[1];
    }
    public float[] getHsv()
    {
        float[] ret = new float[3];
        ret[0] = hsv[0];
        ret[1] = hsv[1];
        ret[2] = hsv[2];
        return ret;
    }

    public int getRGB()
    {
        return Color.HSVToColor(this.hsv);
    }

    public void setHue(float hue)
    {
        float newHsv[] = this.hsv.clone();
        newHsv[0] = hue;
        view.setBackgroundColor(Color.HSVToColor(newHsv) );
    }

    public void setSaturation(float sat)
    {
        float newHsv[] = this.hsv.clone();
        newHsv[1] = sat;
        view.setBackgroundColor(Color.HSVToColor(newHsv) );
    }

    public void setValue(float value)
    {
        float newHsv[] = this.hsv.clone();
        newHsv[2] = value;
        view.setBackgroundColor(Color.HSVToColor(newHsv) );
    }





}
