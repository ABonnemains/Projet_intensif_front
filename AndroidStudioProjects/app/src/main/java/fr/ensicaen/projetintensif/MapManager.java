package fr.ensicaen.projetintensif;

import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceManager;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class MapManager {
    Activity _activity;
    Context _ctx;

    public MapManager(Activity activity, Context ctx) {
        _activity = activity;
        _ctx = ctx;
    }

    public void init()
    {
        Configuration.getInstance().load(_ctx, PreferenceManager.getDefaultSharedPreferences(_ctx));

        MapView map = (MapView) _activity.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        IMapController mapController = map.getController();
        mapController.setZoom(14);
        GeoPoint startPoint = new GeoPoint(49.180796, -0.348736);
        mapController.setCenter(startPoint);
    }

    
}
