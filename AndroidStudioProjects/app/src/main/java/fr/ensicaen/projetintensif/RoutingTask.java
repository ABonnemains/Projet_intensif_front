package fr.ensicaen.projetintensif;

import java.util.ArrayList;

import android.os.AsyncTask;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;

/**
 * Class to get a route between a start and a destination point, going through a list of waypoints. It uses MapQuest open, public and free API, based on OpenStreetMap data. 
 * See http://open.mapquestapi.com/guidance
 * @author Dylan Stenico
 */
public class RoutingTask extends AsyncTask<ArrayList<GeoPoint>, Void, Road> {

    private MainActivity activity;

    public RoutingTask(MainActivity activity) {this.activity = activity;}

    @Override
    protected Road doInBackground(ArrayList<GeoPoint>... params) {

        RoadManager roadManager = new OSRMRoadManager(activity.getApplicationContext());
        Road road = roadManager.getRoad(params[0]);
        return road;
    }

    @Override
    protected void onPostExecute(Road result) {
        activity.getRoadResult(result);
    }
}