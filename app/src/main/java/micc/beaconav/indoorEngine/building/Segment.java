package micc.beaconav.indoorEngine.building;

/**
 * Created by Nagash on 17/10/2015.
 */

public class Segment {
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


//
//
//    public static boolean intersect( Segment sa, Segment sb)
//    {
//        //  Stackoverflow: http://stackoverflow.com/questions/3838329/how-can-i-check-if-two-segments-intersect
//        /**
//         *
//         *
//         * The formula for a line is:
//
//         f(x) = A*x + b = y
//         For a segment, it is exactly the same, except that x is included into an interval I.
//
//         If you have two segments, defined as follow:
//
//         Segment1 = {(X1, Y1), (X2, Y2)}
//         Segment2 = {(X3, Y3), (X4, Y4)}
//         The abcisse Xa of the potential point of intersection (Xa,Ya) must be contained in both interval I1 and I2, defined as follow :
//
//         I1 = [MIN(X1,X2), MAX(X1,X2)]
//         I2 = [MIN(X3,X4), MAX(X3,X4)]
//         And we could say that Xa is included into :
//
//         Ia = [MAX( MIN(X1,X2), MIN(X3,X4) ), MIN( MAX(X1,X2), MAX(X3,X4)] )]
//         Now, you need to check that this interval Ia exists :
//
//         if (MAX(X1,X2) < MIN(X3,X4))
//         return false; // There is no mutual abcisses
//         So, you got two line formula, and a mutual interval. Your line formulas are:
//
//         f1(x) = A1*x + b1 = y
//         f2(x) = A2*x + b2 = y
//         As we got two points by segment, we are able to determine A1, A2, b1 and b2:
//
//         A1 = (Y1-Y2)/(X1-X2) // Pay attention to not dividing by zero
//         A2 = (Y3-Y4)/(X3-X4) // Pay attention to not dividing by zero
//         b1 = Y1-A1*X1 = Y2-A1*X2
//         b2 = Y3-A2*X3 = Y4-A2*X4
//         If the segments are parallel, then A1 == A2 :
//
//         if (A1 == A2)
//         return false; // Parallel segments
//         A point (Xa,Ya) standing on both line must verify both formulas f1 and f2:
//
//         Ya = A1 * Xa + b1
//         Ya = A2 * Xa + b2
//         A1 * Xa + b1 = A2 * Xa + b2
//         Xa = (b2 - b1) / (A1 - A2) // Once again, pay attention to not dividing by zero
//         The last thing to do is check that Xa is included into Ia:
//
//         if ( (Xa < MAX( MIN(X1,X2), MIN(X3,X4) )) ||
//         (Xa > MIN( MAX(X1,X2), MAX(X3,X4) )) )
//         return false; // intersection is out of bound
//         else
//         return true;
//         In addition to this, you may check at startup that two of the four provided points are not equals to avoid all that testing.
//         *
//         */
//
//
//
//        float xa1 = sa.getVertex1().getX();
//        float xa2 = sa.getVertex2().getX();
//        float xb1 = sb.getVertex1().getX();
//        float xb2 = sb.getVertex2().getX();
//
//        float max_xa = Math.max(xa1, xa2);
//        float max_xb = Math.max(xb1, xb2);
//        float min_xa = Math.min(xa1, xa2);
//        float min_xb = Math.min(xb1, xb2);
//
//
//        //if(Math.min(max_xa,max_xb) <= Math.max(min_xa, min_xb)  )
//        if(Math.min(max_xa,max_xb) < Math.max(min_xa, min_xb)  )
//            return true;
//
//        float ya1 = sa.getVertex1().getY();
//        float ya2 = sa.getVertex2().getY();
//        float yb1 = sb.getVertex1().getY();
//        float yb2 = sb.getVertex2().getY();
//
//        float max_ya = Math.max(ya1, ya2);
//        float max_yb = Math.max(yb1, yb2);
//        float min_ya = Math.min(ya1, ya2);
//        float min_yb = Math.min(yb1, yb2);
//
//        //if(Math.min(max_ya,max_yb) <= Math.max(min_ya, min_yb)  )
//        if(Math.min(max_ya,max_yb) < Math.max(min_ya, min_yb)  )
//            return true;
//
//        //if(xa1 == xb1 && xa2 == xb2 || ya1 == yb1 && ya2 == yb2)
//        if(xa1 == xb1 && xa2 == xb2 && ya1 == yb1 && ya2 == yb2)
//            return true;
//
//
//
//        /*
//            fa(x) = A_a*x + b_a = y
//            fb(x) = A_b*x + b_b = y
//
//
//
//         */
//        float A_a = (ya1-ya2)/(xa1-xa2); // Pay attention to not dividing by zero
//        float A_b = (yb1-yb2)/(xb1-xb2); // Pay attention to not dividing by zero
//        // float b_a = ya1-A_a*xa1 = ya2-A_a*xa2;
//        //float b_b = yb1-A_b*xb1 = yb2-A_b*xb2;
//
//        if (A_a == A_b)
//            return false; // Parallel segments
//
//
//    }





    public static boolean intersect( Segment sa, Segment sb) {
        return doIntersect(sa.v1,sa.v2,sb.v1,sb.v2);




    }

    // Given three colinear points p, q, r, the function checks if
    // point q lies on line segment 'pr'
    public static boolean onSegment(Vertex p,Vertex q,Vertex r){
        if (q.getX() < Math.max(p.getX(),r.getX()) && q.getX() >= Math.min(p.getX(),r.getX())
                && q.getY() <= Math.max(p.getY(),r.getY()) && q.getY() >= Math.min(p.getY(),r.getY())){
            return true;

        }
        else
            return false;

    }

    // To find orientation of ordered triplet (p, q, r).
    // The function returns following values
    // 0 --> p, q and r are colinear
    // 1 --> Clockwise
    // 2 --> Counterclockwise

    public static int orientation(Vertex p, Vertex q,Vertex r){
        float val = (q.getY() - p.getY()) * (r.getX() - q.getX()) - (q.getX() - p.getX()) * (r.getY() - q.getY());
        if (val == 0)
            return 0; //collinear
        return (val > 0 ) ? 1 : 2; // clock or counterclockwise
    }


    // The main function that returns true if line segment 'p1q1'
    // and 'p2q2' intersect

    public static boolean doIntersect(Vertex p1,Vertex q1,Vertex p2,Vertex q2){

        // Find the four orientations needed for general and
        // special cases

        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        // General case
        if (o1 != o2 && o3 != o4)
            return true;

        // Special Cases
        // p1, q1 and p2 are colinear and p2 lies on segment p1q1
        if (o1 == 0 && onSegment(p1, p2, q1)) return true;

        // p1, q1 and p2 are colinear and q2 lies on segment p1q1
        if (o2 == 0 && onSegment(p1, q2, q1)) return true;

        // p2, q2 and p1 are colinear and p1 lies on segment p2q2
        if (o3 == 0 && onSegment(p2, p1, q2)) return true;

        // p2, q2 and q1 are colinear and q1 lies on segment p2q2
        if (o4 == 0 && onSegment(p2, q1, q2)) return true;

        return false; // Doesn't fall in any of the above cases
    }

}
