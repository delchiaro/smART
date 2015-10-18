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


}
