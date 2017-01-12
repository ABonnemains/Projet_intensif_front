package fr.ensicaen.projetintensif;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;


public class Recherche extends DialogFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recherche, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        final Button rechercheButton = (Button) getView().findViewById(R.id.button_search);
        rechercheButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(getView().getContext(),
                        R.style.AppTheme);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Recherche en cours...");
                progressDialog.show();

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {

                                RoadManager road_manager = new OSRMRoadManager(getActivity().getApplicationContext());
                                MapManager mapManager = ((MainActivity) getActivity()).getMapManager();
                                GeoPoint startPoint = new GeoPoint(mapManager.getLocation());
                                GeoPoint endPoint = new GeoPoint(49.17516, -0.35455);

                                ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
                                waypoints.add(startPoint);
                                waypoints.add(endPoint);

                                Road road = road_manager.getRoad(waypoints);

                                Polyline roadOverlay = RoadManager.buildRoadOverlay(road);

                                MapView map = (MapView) ((MainActivity) getActivity()).findViewById(R.id.map);
                                map.getOverlays().add(roadOverlay);

                                map.invalidate();
                                progressDialog.dismiss();
                            }
                        }, 1000);
            }
        });
    }
}
