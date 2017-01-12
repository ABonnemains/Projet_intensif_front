package fr.ensicaen.projetintensif;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.util.Log;

import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amine on 12/01/2017.
 */

public class TravelTask {
    private MainActivity activity;
    private Context context;
    private MapView mapView;
    private ArrayList<GeoPoint> waypoints;
    private boolean isEndPointOn;
    private Marker mPin;

    private int indexEndPoint;

    public TravelTask(MainActivity activity, Context context, MapView mapView) {
        this.activity = activity;
        this.context = context;
        this.mapView = mapView;
        MapManager mapManager = activity.getMapManager();
        waypoints = new ArrayList<GeoPoint>();
        waypoints.add(new GeoPoint(mapManager.getLocation()));
        indexEndPoint = -1;
        isEndPointOn = false;
        mPin = new Marker(mapView);
        mPin.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mPin.setTitle("Arrivée");
        Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.arrivee);
        Drawable drawable = new BitmapDrawable(activity.getResources(), bitmap);
        mPin.setIcon(drawable);

        // add new marker pin to map
        this.mapView.getOverlays().add(mPin);
    }

    public void addEventReceiver() {
        MapEventsReceiver mReceive = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                return true;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                if(waypoints.size() >= 2){
                    waypoints.remove(1);
                }
                waypoints.add(p);

                mPin.setPosition(p);

                isEndPointOn = true;

                if(waypoints.size() >= 2) {
                    new RoutingTask(activity).execute(waypoints);
                }
                mapView.invalidate();

                return true;
            }
        };
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(mReceive);
        List<Overlay> listOverlay = mapView.getOverlays();
        if(isEndPointOn) {
            listOverlay.get(indexEndPoint).setEnabled(false);
            listOverlay.remove(indexEndPoint);
        }
        listOverlay.add(mapEventsOverlay);
        indexEndPoint = listOverlay.size();

        mapView.invalidate();
    }

    public void removeEventReceiver(){
        List<Overlay> listOverlay = mapView.getOverlays();
        if(waypoints.size() >= 2){
            waypoints.remove(1);
            listOverlay.remove(indexEndPoint);
        }
    }
}
