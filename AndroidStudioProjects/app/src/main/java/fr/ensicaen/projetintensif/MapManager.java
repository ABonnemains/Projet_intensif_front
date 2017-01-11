package fr.ensicaen.projetintensif;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.List;

public class MapManager {
    private Activity _activity;
    private Context _ctx;
    private GeoPoint _currentLocation;
    private LocationListener _locationListener;
    private LocationManager _locationManager;

    public MapManager(Activity activity, Context ctx) {
        _activity = activity;
        _ctx = ctx;

        init();
    }

    private void init() {
        Configuration.getInstance().load(_ctx, PreferenceManager.getDefaultSharedPreferences(_ctx));

        MapView map = (MapView) _activity.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);

        /*Intent intent = new Intent(_activity.getApplicationContext(), LocationService.class);

        _activity.startService(intent);

        GeoPoint point = new GeoPoint(intent.getDoubleExtra("Latitude", 49.17523), intent.getDoubleExtra("Longitude", -0.34740));

        if( point != null ) {
            _currentLocation = point;
        }
        else {
            _currentLocation = new GeoPoint(48.8583, 2.2944);
        }
        */

        IMapController mapController = map.getController();
        mapController.setZoom(14);

        /*mapController.setCenter(_currentLocation);*/
        GpsMyLocationProvider gpsMyLocationProvider = new GpsMyLocationProvider(_activity.getApplicationContext());
        gpsMyLocationProvider.setLocationUpdateMinDistance(2.0f);
        gpsMyLocationProvider.setLocationUpdateMinTime(1000);
        MyLocationNewOverlay myLocationNewOverlay = new MyLocationNewOverlay(map);
        myLocationNewOverlay.enableFollowLocation();
        _currentLocation = myLocationNewOverlay.getMyLocation();
        map.getOverlays().add(myLocationNewOverlay);

        map.invalidate();
    }

    private Location getLocation()
    {
        if(ContextCompat.checkSelfPermission( _ctx, android.Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_DENIED &&
                ContextCompat.checkSelfPermission( _ctx, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_DENIED) {
            return null;
        }

        LocationManager locationManager= (LocationManager) _ctx.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;

        for (String provider : providers) {
            Location location = locationManager.getLastKnownLocation(provider);

            if (location == null) {
                continue;
            }

            if (bestLocation == null || location.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = location;
            }
        }

        return bestLocation;
    }
}
