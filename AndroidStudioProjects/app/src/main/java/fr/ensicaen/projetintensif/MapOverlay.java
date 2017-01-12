package fr.ensicaen.projetintensif;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.List;

public class MapOverlay {

    private Activity activity;
    private Context context;
    private MapView mapView;

    private int numberOverlay;

    public MapOverlay(Activity activity, Context context, MapView mapView) {
        this.activity = activity;
        this.context = context;
        this.mapView = mapView;
    }

    public void addOverlayPosition(final IGeoPoint point){
        OverlayItem myLocationOverlayItem = new OverlayItem("Here", "Current Position", point);

        Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.picto_lieux_rouge);
        Double height = bitmap.getHeight()*0.03;
        Double width = bitmap.getWidth()*0.03;
        bitmap = Bitmap.createScaledBitmap(bitmap, width.intValue(), height.intValue(), false);
        Drawable drawable = new BitmapDrawable(activity.getResources(), bitmap);
        myLocationOverlayItem.setMarker(drawable);

        final ArrayList<OverlayItem> items = new ArrayList<>();
        items.add(myLocationOverlayItem);

        ItemizedIconOverlay currentLocationOverlay = new ItemizedIconOverlay<>(items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        Toast toast = Toast.makeText(context, point.getLatitude() + ", " + point.getLongitude(), Toast.LENGTH_SHORT);
                        toast.show();
                        return true;
                    }
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        return true;
                    }
                }, context);
        mapView.getOverlays().add(currentLocationOverlay);
    }

    public void addOverlayAlert(final IGeoPoint point, final String description){
        OverlayItem myLocationOverlayItem = new OverlayItem("Here", "Current Position", point);
        myLocationOverlayItem.setMarker(ContextCompat.getDrawable(context,R.drawable.person));

        final ArrayList<OverlayItem> items = new ArrayList<>();
        items.add(myLocationOverlayItem);

        ItemizedIconOverlay currentLocationOverlay = new ItemizedIconOverlay<>(items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        Toast toast = Toast.makeText(context, description, Toast.LENGTH_SHORT);
                        toast.show();
                        return true;
                    }
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        return true;
                    }
                }, context);
        mapView.getOverlays().add(currentLocationOverlay);
    }

    public void addEventReceiver() {
        MapEventsReceiver mReceive = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(final GeoPoint p) {
                //Send point to server

                AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                alert.setTitle("Obstacle");
                alert.setMessage("Description");

                final EditText editText = new EditText(activity);
                editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                alert.setView(editText);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        final String description = editText.getText().toString();
                        Toast toast = Toast.makeText(context, description, Toast.LENGTH_SHORT);
                        toast.show();
                        addOverlayAlert(p, description);
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                alert.show();
                return true;
            }

            @Override
            public boolean longPressHelper(final GeoPoint p) {
                return true;
            }
        };
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(mReceive);
        List<Overlay> listOverlay = mapView.getOverlays();
        numberOverlay = listOverlay.size();
        mapView.getOverlays().add(mapEventsOverlay);
        mapView.invalidate();
    }

    public void removeEventReceiver(){
        List<Overlay> listOverlay = mapView.getOverlays();
        if(numberOverlay > 0 && numberOverlay < listOverlay.size()){
            listOverlay.remove(numberOverlay);
        }
    }
}
