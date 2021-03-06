package fr.ensicaen.projetintensif;

import android.app.FragmentManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

import fr.ensicaen.projetintensif.communication.Communication;
import fr.ensicaen.projetintensif.communication.GetTask;
import fr.ensicaen.projetintensif.communication.RefreshHelpTask;
import fr.ensicaen.projetintensif.event.CreateEvent;
import fr.ensicaen.projetintensif.login.LoginActivity;
import fr.ensicaen.projetintensif.map.MapManager;
import fr.ensicaen.projetintensif.map.MapOverlay;
import fr.ensicaen.projetintensif.profilConfig.ProfilConfigurationActivity;
import fr.ensicaen.projetintensif.travel.TravelTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener {

    private JSONObject getResult;
    private int getID = 0;
    private MapManager mapManager;
    private Road roadResult;
    private ArrayList<Polyline> roadOverlays;
    public Location location;
    private Marker startMarker;
    private MapView map;
    public Location currentLocation;
    private MapOverlay mapOverlay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fabDanger = (FloatingActionButton) findViewById(R.id.fabDanger);

        map = (MapView) findViewById(R.id.map);
        mapOverlay = new MapOverlay(this, getApplicationContext(), map);



        FloatingActionButton fabAssistance = (FloatingActionButton) findViewById(R.id.fabAssistance);
        fabAssistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Pour signaler un danger, appuyez sur la carte.", 10000)
                        .setAction("Action", null).show();
                mapOverlay.addEventReceiver();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mapOverlay.removeEventReceiver();
                    }
                }, 10000);

            }
        });

        roadOverlays = new ArrayList<Polyline>();

        mapManager = new MapManager(this, getApplicationContext());

        final TravelTask travelTask = new TravelTask(this, getApplicationContext(), map);
        travelTask.addEventReceiver();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.getHeaderView(0);

        final TextView nickname_view = (TextView) headerLayout.findViewById(R.id.nickname_view);
        nickname_view.setClickable(true);
        nickname_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfilConfigurationActivity.class);
                intent.putExtra("nickname", nickname_view.getText().toString());
                startActivity(intent);
            }
        });
        String nickname = getIntent().getStringExtra("nickname");
        if (!nickname.isEmpty())
            nickname_view.setText(nickname);



        //MapManager mapManager = new MapManager(this, getApplicationContext());
        location = mapManager.getLocation();
        getEvenements(location.getLatitude(),location.getLongitude());
        getObstacles(location.getLatitude(),location.getLongitude());

        new GetTask(this).execute(new Communication("test"));
        new RefreshHelpTask(this).execute(new Communication(location.getLatitude(),location.getLongitude(),Communication.RequestType.REFRESH_ASSIST));

        fabDanger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                askAssistance(location.getLatitude(),location.getLongitude());
                Toast toast = Toast.makeText(getApplicationContext(), "Une demande d'assistance a été lancée.", Toast.LENGTH_LONG);
                toast.show();

                /*Snackbar.make(view, "Assistance", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getApplication())
                                .setContentTitle("Un utilisateur est en difficulté.")
                                .setSmallIcon(R.drawable.alerte)
                                .setContentText("Proposez votre aide.");
                Intent resultIntent = new Intent(getApplication(), LoginActivity.class);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplication());
                stackBuilder.addParentStack(LoginActivity.class);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                mBuilder.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(1, mBuilder.build());*/
            }
        });


       /*Location location = mapManager.getLocation();
        getEvenements(location.getLatitude(), location.getLongitude());
        getObstacles(location.getLatitude(), location.getLongitude());*/

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                JSONArray events = Communication.get_JSONEvents();
                JSONArray obstacles = Communication.get_JSONObstacles();
                mapOverlay.getDataFromServer(events, obstacles, null);
            }
        }, 15000);
        //new GetTask(this).execute(new Communication("test"));
    }

    @Override
    public void onResume(){
        super.onResume();

        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.evenement) {
             FragmentManager fm = getFragmentManager();
            CreateEvent ce = new CreateEvent();
            ce.setMap(map);
            ce.setMapOverlay(mapOverlay);
            ce.show(fm,"Créer un évènement");


        /*} else if (id == R.id.parametres) {
            Fragment frag = new BlankFragment();
            FragmentManager ft = getSupportFragmentManager();
            ft.beginTransaction().replace(R.id.hello,frag).commit();*/
        } else if (id == R.id.recherche){
            /*FragmentManager fm = getFragmentManager();
            Recherche searchTaskFragment = new SearchTask();
            searchTaskFragment.show(fm,"Recherche");*/
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setGetResult(JSONObject res){
        getResult = res;
        getID++;
    }


    public void getEvenements(double latitude, double longitude) {
        new GetTask((MainActivity) this).execute(new Communication(latitude, longitude, Communication.RequestType.GET_ALL_EVENTS));
    }

    public void getObstacles(double latitude, double longitude){
        new GetTask((MainActivity) this).execute(new Communication(latitude, longitude, Communication.RequestType.GET_ALL_OBSTACLES));
    }

    public void askAssistance(double latitude, double longitude){
        new GetTask((MainActivity) this).execute(new Communication(latitude, longitude, Communication.RequestType.ASK_ASSIST));
    }

    public void refreshHelp(){
        new RefreshHelpTask(this).execute(new Communication(location.getLatitude(),location.getLongitude(),Communication.RequestType.REFRESH_ASSIST));
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    public void getRoadResult(Road road){
        if(roadOverlays.size() >= 1)
            roadOverlays.get(roadOverlays.size()-1).setVisible(false);
        roadOverlays.add(RoadManager.buildRoadOverlay(road));
        MapView map = (MapView) findViewById(R.id.map);
        map.getOverlays().add(roadOverlays.get(roadOverlays.size()-1));
        map.invalidate();
    }


    public void notifSomeoneAskedHelp() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplication())
                .setContentTitle("Un utilisateur est en difficulté.")
                .setSmallIcon(R.drawable.alerte)
                .setContentText("Proposez votre aide.");
        Intent resultIntent = new Intent(getApplication(), LoginActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplication());
        stackBuilder.addParentStack(LoginActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }


    @Override
    public void onLocationChanged(Location location) {
        mapManager.setLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {


    }
}
