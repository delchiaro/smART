package micc.beaconav.indoorEngine.building.spot.marker;

import android.graphics.PointF;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 *
 */
public class CollidableRect implements Collidable {

    PointF p1;
    PointF p2;

    public CollidableRect(float x1, float y1, float x2, float y2)
    {
        p1 = new PointF(x1, y1);
        p2 = new PointF(x2, y2);
    }

    @Override
    public boolean checkCollision(float x, float y) {

        if((x > p1.x && x > p2.x) || (x < p1.x && x < p2.x) || (y > p1.y && x > p2.y) || (y < p1.x && x < p2.y)  )
            return false;
        else return true;
    }
}
