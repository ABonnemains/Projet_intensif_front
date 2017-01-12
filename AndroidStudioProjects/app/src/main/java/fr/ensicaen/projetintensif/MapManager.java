package fr.ensicaen.projetintensif;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.List;

public class MapManager {
    private MainActivity _activity;
    private Context _ctx;
    private Location _currentLocation;
    private GeoPoint startPoint;
    private MapView map;
    private Marker startMarker;

    public MapManager(MainActivity activity, Context ctx) {
        _activity = activity;
        _ctx = ctx;

        init();
    }

    private void init() {
        Configuration.getInstance().load(_ctx, PreferenceManager.getDefaultSharedPreferences(_ctx));

        LocationManager locationManager = (LocationManager) _ctx.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(_ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(_ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1.0f, _activity);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 1.0f, _activity);
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
        _currentLocation = bestLocation;

        map = (MapView) _activity.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);

        IMapController mapController = map.getController();
        mapController.setZoom(14);

        startPoint = new GeoPoint(_currentLocation.getLatitude(), _currentLocation.getLongitude());
        mapController.setCenter(startPoint);

        startMarker = new Marker(map);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setTitle("Vous");
        Bitmap bitmap = BitmapFactory.decodeResource(_activity.getResources(), R.drawable.picto_lieux_rouge);
        Double height = bitmap.getHeight()*0.03;
        Double width = bitmap.getWidth()*0.03;
        bitmap = Bitmap.createScaledBitmap(bitmap, width.intValue(), height.intValue(), false);
        Drawable drawable = new BitmapDrawable(_activity.getResources(), bitmap);
        startMarker.setIcon(drawable);
        startMarker.setPosition(startPoint);
        map.getOverlays().add(startMarker);
    }

    public Location getLocation() {
        return _currentLocation;
    }

    public void setLocation(Location location) {
        if( _currentLocation.getAccuracy() < location.getAccuracy()) {
            _currentLocation = location;
            startPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
            startMarker.setPosition(new GeoPoint(location.getLatitude(), location.getLongitude()));
            map.invalidate();
        }
    }
}
