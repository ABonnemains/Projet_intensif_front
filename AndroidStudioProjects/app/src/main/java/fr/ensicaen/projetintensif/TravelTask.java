package fr.ensicaen.projetintensif;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amine on 12/01/2017.
 */

public class TravelTask {
    private MainActivity activity;
    private Context context;
    private MapView mapView;
    private GeoPoint startPoint;
    private GeoPoint endPoint;

    private int index_end_point;

    public TravelTask(MainActivity activity, Context context, MapView mapView) {
        this.activity = activity;
        this.context = context;
        this.mapView = mapView;
        MapManager mapManager = activity.getMapManager();
        startPoint = new GeoPoint(mapManager.getLocation());
        endPoint = new GeoPoint(49.17511, -0.35044);
        index_end_point = -1;
    }

    public void addEventReceiver() {
        MapEventsReceiver mReceive = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                return true;
            }

            @Override
            public boolean longPressHelper(final GeoPoint p) {
                //Send point to server
                endPoint = p;
                Marker mPin = new Marker(mapView);
                mPin.setPosition(p);
                mPin.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                mPin.setTitle("GO");

                // add new marker pin to map
                mapView.getOverlays().add(mPin);

                return true;
            }
        };
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(mReceive);
        List<Overlay> listOverlay = mapView.getOverlays();
        index_end_point = listOverlay.size();
        mapView.getOverlays().add(mapEventsOverlay);

        RoadManager road_manager = new OSRMRoadManager(activity.getApplicationContext());

        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
        waypoints.add(startPoint);
        waypoints.add(endPoint);

        new RoutingTask(activity).execute(waypoints);

        mapView.invalidate();
    }

    public void removeEventReceiver(){
        List<Overlay> listOverlay = mapView.getOverlays();
        if(index_end_point > 0 && index_end_point < listOverlay.size()){
            listOverlay.remove(index_end_point);
        }
    }
}
