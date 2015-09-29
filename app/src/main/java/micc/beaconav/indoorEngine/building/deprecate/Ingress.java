//package micc.beaconav.indoorEngine.building;
//
//import android.graphics.Canvas;
//import android.graphics.PointF;
//
//import micc.beaconav.indoorEngine.building.spot.Spot;
//import micc.beaconav.indoorEngine.drawable.Drawable;
//
///**
// * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
// */
//public class Ingress
//{
//
//
//    private final Spot virtualDoorA;
//    private final Spot virtualDoorB;
//    private final android.graphics.drawable.Drawable physicalDoorDrawable;
//
//    private final ConvexArea areaA;
//    private final ConvexArea areaB;
//
//
//
//    public Ingress(ConvexArea area_A, ConvexArea area_B, Spot virtualDoor_A, Spot virtualDoor_B,
//                   android.graphics.drawable.Drawable physicalDoor)
//    {
//        this.virtualDoorA = virtualDoor_A;
//        this.virtualDoorB = virtualDoor_B;
//        this.physicalDoorDrawable = physicalDoor;
//        this.areaA = area_A;
//        this.areaB = area_B;
//
//        ConvexArea.linkConvexAreaIngress(area_A, area_B, this);
//    }
//    public Ingress(ConvexArea area_A, ConvexArea area_B, Spot virtualDoor_A, Spot virtualDoor_B) {
//        this(area_A, area_B, virtualDoor_A, virtualDoor_B, null);
//    }
//    public Ingress(ConvexArea area_A, ConvexArea area_B, Spot virtualDoor) {
//        this(area_A, area_B, virtualDoor, virtualDoor, null);
//    }
//
//
//    public Spot getVirtualDoorA() {
//        return virtualDoorA;
//    }
//
//    public Spot getVirtualDoorB() {
//        return virtualDoorB;
//    }
//
//    public android.graphics.drawable.Drawable getPhysicalDoor() {
//        return this.physicalDoorDrawable;
//    }
//
//    public ConvexArea getAreaA() {
//        return areaA;
//    }
//
//    public ConvexArea getAreaB() {
//        return areaB;
//    }
//
//
//    protected void draw(Canvas canvas) {
//        if(physicalDoorDrawable != null){
//            physicalDoorDrawable.draw(canvas);
//        }
//    }
//}
