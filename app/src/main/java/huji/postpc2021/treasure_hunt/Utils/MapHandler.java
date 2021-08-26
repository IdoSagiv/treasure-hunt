package huji.postpc2021.treasure_hunt.Utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.Collection;

import huji.postpc2021.treasure_hunt.CreatorFlow.EditHintMarkerWindow;
import huji.postpc2021.treasure_hunt.R;
import huji.postpc2021.treasure_hunt.TreasureHuntApp;
import huji.postpc2021.treasure_hunt.Utils.DataObjects.Clue;

import static android.content.Context.LOCATION_SERVICE;

public class MapHandler {
    private static final GeoPoint DEFAULT_START_POINT = new GeoPoint(32.1007, 34.8070);
    private static final double MAP_DEFAULT_ZOOM = 18.0;
    private static final double MAP_MAX_ZOOM = 22.0;
    private static final double MAP_MIN_ZOOM = 9.0;
    private final MapView mMapView;
    private final GeoPoint startPoint;
    private GeoPoint currentLocation = null;
    private final MarkersType markersType;

    private OnMapLongPressCallback longPressCallback = null;
    private final Context context;

    /**
     * @param mapView the founded mapView
     */
    public MapHandler(MapView mapView, MarkersType markersType) {
        this(mapView, markersType, DEFAULT_START_POINT);
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            if (currentLocation == null) {
                // on the first update -> animate to current location
                currentLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
                mapToCurrentLocation();
            }
            currentLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    };

    /**
     * @param mapView the founded mapView
     */
    public MapHandler(MapView mapView, MarkersType markersType, GeoPoint startPoint) {
        this.startPoint = startPoint;
        this.mMapView = mapView;
        this.context = TreasureHuntApp.getInstance();
        this.markersType = markersType;

        initMap();
    }

    public void initMap() {
        // initialize the map
        mMapView.getOverlay().clear();
        mMapView.setTileSource(TileSourceFactory.MAPNIK);
        mMapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
        mMapView.setMultiTouchControls(true);
        mMapView.getController().setZoom(MAP_DEFAULT_ZOOM);
        mMapView.setMaxZoomLevel(MAP_MAX_ZOOM);
        mMapView.setMinZoomLevel(MAP_MIN_ZOOM);

        mMapView.getController().setCenter(startPoint);

        // enable user location
        LocationManager mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, mLocationListener);
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, mLocationListener);
        } else {
            Log.e("MapHandler", "missing permissions");
        }

        final MapEventsReceiver mReceive = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                if (longPressCallback != null) {
                    longPressCallback.OnLongPressCallback(p);
                }
                return false;
            }
        };
        mMapView.getOverlays().add(new MapEventsOverlay(mReceive));

        addMyLocationIconOnMap();
//        addScaleBarOnMap();
    }

    private void showHintOnMap(Clue clue) {
        GeoPoint location = new GeoPoint(clue.getLocation().getLatitude(), clue.getLocation().getLongitude());

        Marker myMarker = new Marker(mMapView);
        myMarker.setId(clue.getId());
        myMarker.setPosition(location);
        myMarker.setTitle(clue.getDescription());
        myMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);

        // default icon
//        myMarker.setIcon(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_general_marker, context.getTheme()));

//        todo: implement
        switch (markersType) {
            case HintOnly: {
                // icon - default (according to the marker index?)
                myMarker.setInfoWindow(new SeeHintMarkerWindow(R.layout.see_hint_marker_window, mMapView, myMarker));
                break;
            }
            case EditHint: {
                // icon - default (according to the marker index?)
                myMarker.setInfoWindow(new EditHintMarkerWindow(R.layout.edit_hint_marker_window, mMapView, myMarker));
                break;
            }
            case HintAndPlayers: {
                // marker window with the list of players who saw the hint
                // marker icon according to the players who saw it (??)
                break;
            }
        }

        myMarker.setOnMarkerClickListener((marker, mapView) -> {
            if (marker.isInfoWindowShown()) {
                InfoWindow.closeAllInfoWindowsOn(mapView);
            } else {
                InfoWindow.closeAllInfoWindowsOn(mapView);
                marker.showInfoWindow();
            }
            return false;
        });

        mMapView.getOverlays().add(myMarker);
    }

    private void addMyLocationIconOnMap() {
        // set my location on the map
        MyLocationNewOverlay myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(context), mMapView);
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.setOptionsMenuEnabled(true);
        myLocationOverlay.setDrawAccuracyEnabled(false);
        myLocationOverlay.getMyLocation();

//        myLocationOverlay.enableFollowLocation();  follow the user
//        myLocationOverlay.setPersonIcon();  todo: choose an icon

        // add to map
        mMapView.getOverlays().add(myLocationOverlay);
    }

    private void addScaleBarOnMap() {
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        // set scale bar
        ScaleBarOverlay mScaleBarOverlay = new ScaleBarOverlay(mMapView);
        mScaleBarOverlay.setCentred(true);
        mScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);

        // add to map
        mMapView.getOverlays().add(mScaleBarOverlay);
    }

    public void mapToCurrentLocation() {
        if (currentLocation != null) {
            centerMap(currentLocation);
        }
    }

    private void centerMap(IGeoPoint centerTo) {
        mMapView.getController().animateTo(centerTo);
        if (mMapView.getZoomLevelDouble() < MAP_DEFAULT_ZOOM) {
            mMapView.getController().setZoom(MAP_DEFAULT_ZOOM);
        }
    }

    public void openMarker(String markerId) {
        for (Overlay overlay : mMapView.getOverlays()) {
            if (overlay instanceof Marker && ((Marker) overlay).getId().equals(markerId)) {
                Marker marker = (Marker) overlay;
                InfoWindow.closeAllInfoWindowsOn(mMapView);
                marker.showInfoWindow();
                centerMap(marker.getPosition());
            }
        }
    }

    public enum MarkersType {
        HintOnly,
        EditHint,
        HintAndPlayers
    }

    public void setLongPressCallback(OnMapLongPressCallback longPressCallback) {
        this.longPressCallback = longPressCallback;
    }

    public void showHints(Collection<Clue> clues) {
        // remove the old markers
        for (Overlay overlay : mMapView.getOverlays()) {
            if (overlay instanceof Marker) {
                mMapView.getOverlays().remove(overlay);
            }
        }

        // show the new markers
        for (Clue clue : clues) {
            showHintOnMap(clue);
        }
    }
}

/*
  todo:
  1. usage:
  * load map
  * center to my location - done
  * show scale bar - done
  <p>
  2. Creator special usage:
  * on long press - add new hint where the press was
  * show all hintPoints
  * when click on hintPoint - allow edit or delete
  <p>
  3. Player special usage:
  * show hintPoints that where found
  * when click on hintPoint - show the actual hint (default behaviour)
  * show the users current location with an avatar
  * track the user (when the user moves, move its avatar on the map)
 */