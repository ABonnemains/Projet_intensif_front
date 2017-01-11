package fr.ensicaen.projetintensif;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.preference.PreferenceManager;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;

public class MapManager {
    private Activity _activity;
    private Context _ctx;

    public MapManager(Activity activity, Context ctx) {
        _activity = activity;
        _ctx = ctx;

        init();
    }

    private void init()
    {
        Configuration.getInstance().load(_ctx, PreferenceManager.getDefaultSharedPreferences(_ctx));

        MapView map = (MapView) _activity.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
/*
        GpsMyLocationProvider gpsMyLocationProvider = new GpsMyLocationProvider(_ctx);
        Location lastLocation = gpsMyLocationProvider.getLastKnownLocation();
*/
        IMapController mapController = map.getController();
        mapController.setZoom(9);
        //GeoPoint startPoint = new GeoPoint(lastLocation.getLatitude(), lastLocation.getLongitude());
        GeoPoint startPoint = new GeoPoint(48.8583, 2.2944);
        mapController.setCenter(startPoint);
    }
}
