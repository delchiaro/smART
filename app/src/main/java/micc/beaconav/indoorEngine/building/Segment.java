package micc.beaconav.indoorEngine.building;

import android.graphics.PointF;

/**
 * Created by Nagash on 17/10/2015.
 */

public class Segment {


    private static final double FLOAT_TOLLERANCE = 1e-8;


    private Vertex v1;
    private Vertex v2;


    public Segment(Vertex v1, Vertex v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Segment))
            return false;
        Segment ref = (Segment) obj;
        return (this.v1.equals(ref.v1) && this.v2.equals(ref.v2) ) ||
                ( this.v1.equals(ref.v2) && this.v2.equals(ref.v1 ) );
    }

    @Override
    public int hashCode() {
        return v1.hashCode() ^ v2.hashCode();
        // ^ è lo XOR che gode di proprietá commutativa.
        // v1 XOR v2 == v2 XOR v1
    }


    public Vertex getVertex1() {
        return v1;
    }
    public Vertex getVertex2() {
        return v2;
    }



    public static boolean floatEqual(final float a, final float b)
    {
        return Math.abs(a - b) <= FLOAT_TOLLERANCE;
    }
    public static boolean floatGreaterOrEqual(final float a, final float b)
    {
        return a>=b || floatEqual(a,b);
    }
    public static boolean floatMinorOrEqual(final float a, final float b)
    {
        return a<=b || floatEqual(a,b);
    }

    public static boolean intersect(final Segment sA, final Segment sB)
    {
        return intersection(sA, sB) != null;
    }

    public static PointF intersection(final Segment sA,final Segment sB)
    {
        PointF intersect = rectIntersection(sA, sB);


        if(intersect!= null)
        {

            float xa1 = sA.getVertex1().getX();
            float xa2 = sA.getVertex2().getX();
            float max_xa = Math.max(xa1, xa2);
            float min_xa = Math.min(xa1, xa2);
            float ya1 = sA.getVertex1().getY();
            float ya2 = sA.getVertex2().getY();
            float max_ya = Math.max(ya1, ya2);
            float min_ya = Math.min(ya1, ya2);

            boolean isIn_boxA = false;

            if (floatGreaterOrEqual(intersect.x, min_xa) && floatMinorOrEqual(intersect.x, max_xa)
                    && floatGreaterOrEqual(intersect.y, min_ya) &&  floatMinorOrEqual(intersect.y, max_ya) )
                isIn_boxA = true;


            float xb1 = sB.getVertex1().getX();
            float xb2 = sB.getVertex2().getX();
            float max_xb = Math.max(xb1, xb2);
            float min_xb = Math.min(xb1, xb2);
            float yb1 = sB.getVertex1().getY();
            float yb2 = sB.getVertex2().getY();
            float max_yb = Math.max(yb1, yb2);
            float min_yb = Math.min(yb1, yb2);

            boolean isIn_boxB = false;

            if (floatGreaterOrEqual(intersect.x, min_xb) && floatMinorOrEqual(intersect.x, max_xb)
                    && floatGreaterOrEqual(intersect.y, min_yb) &&  floatMinorOrEqual(intersect.y, max_yb) )
                isIn_boxB = true;


            if(isIn_boxA && isIn_boxB)
                return intersect;
            else return null;

        }
        else return null;

    }

    public static boolean rectIntersect(final Segment s1, final Segment s2)
    {
        return rectIntersection(s1, s2) != null;

    }

    public static PointF rectIntersection(final Segment s1, final Segment s2)
    {
        PointF intersection = null;

        float a1, b1, c1, a2, b2, c2, det;

        a1 = s1.v2.getY() - s1.v1.getY();
        b1 = s1.v1.getX() - s1.v2.getX();
        c1 = a1 * s1.v1.getX() + b1 * s1.v1.getY();

        a2 = s2.v2.getY() - s2.v1.getY();
        b2 = s2.v1.getX() - s2.v2.getX();
        c2 = a2 * s2.v1.getX() + b2 * s2.v1.getY();

        det = a1 * b2 - a2 * b1;

        if(!floatEqual(det, 0))
        {
            intersection = new PointF((b2*c1 - b1*c2)/det, (a1 * c2 - a2 * c1)/det);
        }

        return intersection;

    }






//
//
//    public static boolean intersect( Segment sa, Segment sb) {
//        return doIntersect(sa.v1,sa.v2,sb.v1,sb.v2);
//    }
//
//    // Given three colinear points p, q, r, the function checks if
//    // point q lies on line segment 'pr'
//    public static boolean onSegment(Vertex p,Vertex q,Vertex r){
//        if (q.getX() < Math.max(p.getX(),r.getX()) && q.getX() >= Math.min(p.getX(),r.getX())
//                && q.getY() <= Math.max(p.getY(),r.getY()) && q.getY() >= Math.min(p.getY(),r.getY())){
//            return true;
//
//        }
//        else
//            return false;
//
//    }
//
//    // To find orientation of ordered triplet (p, q, r).
//    // The function returns following values
//    // 0 --> p, q and r are colinear
//    // 1 --> Clockwise
//    // 2 --> Counterclockwise
//
//    public static int orientation(Vertex p, Vertex q,Vertex r){
//        float val = (q.getY() - p.getY()) * (r.getX() - q.getX()) - (q.getX() - p.getX()) * (r.getY() - q.getY());
//        if (val == 0)
//            return 0; //collinear
//        return (val > 0 ) ? 1 : 2; // clock or counterclockwise
//    }
//
//
//    // The main function that returns true if line segment 'p1q1'
//    // and 'p2q2' intersect
//
//    public static boolean doIntersect(Vertex p1,Vertex q1,Vertex p2,Vertex q2){
//
//        // Find the four orientations needed for general and
//        // special cases
//
//        int o1 = orientation(p1, q1, p2);
//        int o2 = orientation(p1, q1, q2);
//        int o3 = orientation(p2, q2, p1);
//        int o4 = orientation(p2, q2, q1);
//
//        // General case
//        if (o1 != o2 && o3 != o4)
//            return true;
//
//        // Special Cases
//        // p1, q1 and p2 are colinear and p2 lies on segment p1q1
//        if (o1 == 0 && onSegment(p1, p2, q1)) return true;
//
//        // p1, q1 and p2 are colinear and q2 lies on segment p1q1
//        if (o2 == 0 && onSegment(p1, q2, q1)) return true;
//
//        // p2, q2 and p1 are colinear and p1 lies on segment p2q2
//        if (o3 == 0 && onSegment(p2, p1, q2)) return true;
//
//        // p2, q2 and q1 are colinear and q1 lies on segment p2q2
//        if (o4 == 0 && onSegment(p2, q1, q2)) return true;
//
//        return false; // Doesn't fall in any of the above cases
//    }

}
