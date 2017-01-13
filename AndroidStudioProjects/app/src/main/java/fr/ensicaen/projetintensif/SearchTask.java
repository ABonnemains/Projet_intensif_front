package fr.ensicaen.projetintensif;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;


public class SearchTask extends DialogFragment {

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
                Toast.makeText(getActivity(), "Recherche en cours...", Toast.LENGTH_LONG).show();
                /*
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

                Drawable nodeIcon = getResources().getDrawable(R.drawable.marker_node);
                for (int i=0; i<road.mNodes.size(); i++){
                    RoadNode node = road.mNodes.get(i);
                    Marker nodeMarker = new Marker(map);
                    nodeMarker.setPosition(node.mLocation);
                    nodeMarker.setIcon(nodeIcon);
                    nodeMarker.setTitle("Step "+i);
                    map.getOverlays().add(nodeMarker);
                }

                map.invalidate();
                */
            }
        });
    }
}
