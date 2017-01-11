package fr.ensicaen.projetintensif;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;

import java.util.List;

public class MapManager {
    private Activity _activity;
    private Context _ctx;
    private Location _currentLocation;

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

        Location currentLocation = getLocation();
        if (currentLocation != null) {
            _currentLocation = currentLocation;
        }
        else {
            currentLocation = new Location("default");
            currentLocation.setLatitude(48.8583);
            currentLocation.setLongitude(2.2944);
        }

        IMapController mapController = map.getController();
        mapController.setZoom(14);

        GeoPoint startPoint = new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude());
        mapController.setCenter(startPoint);
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
