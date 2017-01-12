package fr.ensicaen.projetintensif;

import android.app.FragmentManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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

import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.views.MapView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private JSONObject getResult;
    private int getID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fabDanger = (FloatingActionButton) findViewById(R.id.fabDanger);

        MapView map = (MapView) findViewById(R.id.map);
        final MapOverlay mapOverlay = new MapOverlay(this, getApplicationContext(), map);

        fabDanger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Assistance", Snackbar.LENGTH_LONG)
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
                mNotificationManager.notify(1, mBuilder.build());
            }
        });

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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        MapManager mapManager = new MapManager(this, getApplicationContext());

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
            ce.show(fm,"Créer un évènement");


        } else if (id == R.id.parametres) {
            /*Fragment frag = new BlankFragment();
            FragmentManager ft = getSupportFragmentManager();
            ft.beginTransaction().replace(R.id.hello,frag).commit();*/
        } else if (id == R.id.recherche){
            FragmentManager fm = getFragmentManager();
            Recherche rechercheFragment = new Recherche();
            rechercheFragment.show(fm,"Recherche");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setGetResult(JSONObject res){
        getResult = res;
        getID++;
    }
}
