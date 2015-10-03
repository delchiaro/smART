//package micc.beaconav.indoorEngine.building;
//
//import android.graphics.Canvas;
//import android.graphics.PointF;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//import micc.beaconav.indoorEngine.spot.drawable.DrawableSpot;
//import micc.beaconav.indoorEngine.spot.Spot;
//import micc.beaconav.indoorEngine.spot.SpotManager;
//import micc.beaconav.util.containerContained.ContainerContained;
//
///**
// * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
// */
//public class ConvexArea extends ContainerContained<Room, SpotManager<Spot>>
//        //extends Drawable
//{
//    private ArrayList<Ingress> _ingresses = new ArrayList<>();
//    private ArrayList<Vertex> _vertices = new ArrayList<Vertex>();
//
//
//    public final SpotManager<DrawableSpot> drawableSpotManager = new SpotManager<>(this);
//    public final List<SpotManager<Spot>> spotManagerList = new ArrayList<SpotManager<Spot>>();
//
//
//    public ConvexArea(){
//
//    }
//
//
//    // * * * * * * * * * * * * * * * INGRESSESS * * * * * * * * * * * * * * * * * * * * *
//
//    // Package private function: dovrebbe essere friend della classe Ingress, ma java non supporta friend methods.
//    boolean addIngress(Ingress newIngress) {
//        if(newIngress.getAreaA() == this  ||  newIngress.getAreaB() == this)
//        {
//            this._ingresses.add(newIngress);
//            return true;
//        }
//        else return false;
//    }
//
//    // * * * * * * * * * * * * * * * CONTAINER GETTERS * * * * * * * * * * * * * * * * *
//    public final Room getContainerRoom() {
//        return super.getContainer();
//    }
//    public final Floor getContainerFloor() {
//        return getContainerRoom().getContainerFloor();
//    }
//    public final building getContainerBuilding() {
//        return getContainerFloor().getContainerBuilding();
//    }
//
//
//    // * * * * * * * * * * * * * * * VERTICES * * * * * * * * * * * * * * * * * * * * *
//    public void pushVertex(Vertex newVertex) {
//        this._vertices.add(newVertex);
//    }
//    public void addVertex(int index, Vertex newVertex ) {
//        this._vertices.add(index, newVertex);
//    }
//    public Vertex setCorner(int index, Vertex newVertex) {
//        return this._vertices.set(index, newVertex);
//    }
//    public int indexOfVertex(Vertex corner){
//        return this._vertices.indexOf(corner);
//    }
//    public Vertex addRoomVertex(int roomVertexIndex) {
//        Vertex corner = super.getContainer().getVertex(roomVertexIndex);
//        this._vertices.add(super.getContainer().getVertex(roomVertexIndex));
//        return corner;
//    }
//
//
//
//
//
//
//    // TODO: change Vertex to Spot
//    public boolean checkSpotInArea(Vertex spot) {
//        int size = _vertices.size();
//        Iterator<Vertex> iter = _vertices.iterator();
//
//        float[] vert_x = new float[size];
//        float[] vert_y = new float[size];
//
//        for(int i = 0; iter.hasNext(); i++)
//        {
//            Vertex vertex = iter.next();
//            vert_x[i] = vertex.getX();
//            vert_y[i] = vertex.getY();
//        }
//
//        return ConvexArea.pnpoly(size, vert_x, vert_y, spot.getX(), spot.getY());
//    }
//
//    public boolean isConvex() {
//        if (_vertices.size()<4)
//            return true;
//
//        boolean sign=false;
//        int n=_vertices.size();
//        for(int i=0;i<n;i++)
//        {
//            double dx1 = _vertices.get((i+2)%n).getX()-_vertices.get((i+1)%n).getX();
//            double dy1 = _vertices.get((i+2)%n).getY()-_vertices.get((i+1)%n).getY();
//            double dx2 = _vertices.get(i).getX()-_vertices.get((i+1)%n).getX();
//            double dy2 = _vertices.get(i).getY()-_vertices.get((i+1)%n).getY();
//            double zcrossproduct = dx1*dy2 - dy1*dx2;
//            if (i==0)
//                sign=zcrossproduct>0;
//            else
//            {
//                if (sign!=(zcrossproduct>0))
//                    return false;
//            }
//        }
//        return true;
//    }
//
//
//
//
//    protected void draw(Canvas canvas, PointF padding) {
//        Iterator<Ingress> ingressIter = _ingresses.iterator();
//        while(ingressIter.hasNext()) {
//            ingressIter.next().draw(canvas); //delego disegno delle porte ad ogni Ingress
//        }
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//// * * * * STATIC FUNCTIONS * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//
//    static void linkConvexAreaIngress(ConvexArea area_a, ConvexArea area_b, Ingress newIngress){
//        area_a.addIngress(newIngress);
//        area_b.addIngress(newIngress);
//    }
//
//    // ritorna true se test è all'interno del poligono
//    private static boolean pnpoly(int nvert, float[] vertx, float[] verty, float testx, float testy){
//        int i, j;
//        boolean c = false;
//        for (i = 0, j = nvert-1; i < nvert; j = i++)
//        {
//            if ( ((verty[i]>testy) != (verty[j]>testy)) &&
//                    (testx < (vertx[j]-vertx[i]) * (testy-verty[i]) / (verty[j]-verty[i]) + vertx[i]) )
//                c = !c;
//        }
//        return c;
//    }
//
//}
//
//
