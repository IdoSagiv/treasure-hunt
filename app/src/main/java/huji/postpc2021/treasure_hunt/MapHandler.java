package huji.postpc2021.treasure_hunt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.DisplayMetrics;

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
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import huji.postpc2021.treasure_hunt.DataObjects.Clue;

public class MapHandler {
    private static final double MAP_DEFAULT_ZOOM = 18.0;
    private static final double MAP_MAX_ZOOM = 20.0;
    private static final double MAP_MIN_ZOOM = 9.0;
    private final MapView mMapView;
    private GeoPoint currentLocation = null;
    private final boolean centerToLoc;
    private final ViewerType viewerType;

    private OnMapLongPressCallback longPressCallback = null;
    private final Context context;

    public enum ViewerType {
        Player,
        CreatorEdit,
        CreatorOnPlay
    }

    /**
     * @param mapView the founded mapView
     */
    public MapHandler(MapView mapView, boolean centerToLoc, ViewerType viewerType) {
        this.mMapView = mapView;
        this.context = TreasureHuntApp.getInstance();
        this.centerToLoc = centerToLoc;
        this.viewerType = viewerType;

        initMap();
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            if (currentLocation == null && centerToLoc) {
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

    @SuppressLint("MissingPermission")
    public void initMap() {
        // initialize the map
        mMapView.getOverlay().clear();
        mMapView.setTileSource(TileSourceFactory.MAPNIK);
        mMapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT);
        mMapView.setMultiTouchControls(true);
        mMapView.getController().setZoom(MAP_DEFAULT_ZOOM);
        mMapView.setMaxZoomLevel(MAP_MAX_ZOOM);
        mMapView.setMinZoomLevel(MAP_MIN_ZOOM);

        // set default center position
        GeoPoint startPoint = new GeoPoint(32.1007, 34.8070);
        mMapView.getController().setCenter(startPoint);


        // enable user location todo: first request permissions
//        LocationManager mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
//        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, mLocationListener);
//        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, mLocationListener);

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
        addScaleBarOnMap();
    }

    private void addMyLocationIconOnMap() {
        // set my location on the map
        MyLocationNewOverlay mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(context), mMapView);
        mLocationOverlay.enableMyLocation();
//        mLocationOverlay.enableFollowLocation();  follow the user
        mLocationOverlay.setOptionsMenuEnabled(true);
//        mLocationOverlay.setPersonIcon();  todo: choose an icon

        // add to map
        mMapView.getOverlays().add(mLocationOverlay);
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
        if (currentLocation != null) centerMap(currentLocation, true, true);
    }

    void centerMap(IGeoPoint newCenter, boolean animate, boolean zoomToDefault) {
        if (animate) {
            mMapView.getController().animateTo(newCenter);
        } else {
            mMapView.setExpectedCenter(newCenter);
        }
        if (zoomToDefault) mMapView.getController().setZoom(MAP_DEFAULT_ZOOM);
    }

    public void showHintOnMap(Clue clue) {
        GeoPoint location = new GeoPoint(clue.getLocation().getLatitude(), clue.getLocation().getLongitude());

        Marker myMarker = new Marker(mMapView);
        myMarker.setId(clue.getId());
        myMarker.setPosition(location);
        myMarker.setTitle(clue.getDescription());
        myMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);

        // default icon
//        myMarker.setIcon(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_general_marker, context.getTheme()));

//        todo: implement
        switch (viewerType) {
            case Player: {
                // marker window only with the hint
                // icon - default (according to the marker index?)
                break;
            }
            case CreatorEdit: {
                // marker window with option to edit or delete
                // icon - default (according to the marker index?)
                myMarker.setInfoWindow(new CreatorEditHintWindow(R.layout.edit_hint_marker_window, mMapView, myMarker));
                break;
            }
            case CreatorOnPlay: {
                // marker window with the list of players who saw the hint
                // marker icon according to the players who saw it (??)
                break;
            }
        }

        myMarker.setOnMarkerClickListener((marker, mapView) -> {
            if (marker.isInfoWindowShown()) {
                System.out.println("----close window " + marker.getId());
                marker.closeInfoWindow();
            } else {
                marker.showInfoWindow();
                System.out.println("----open window " + marker.getId());
            }
            return false;
        });

        mMapView.getOverlays().add(myMarker);
    }

    public void setLongPressCallback(OnMapLongPressCallback longPressCallback) {
        this.longPressCallback = longPressCallback;
    }

    public void showHints(Collection<Clue> clues) {
        // remove the old markers
        List<Overlay> overlays = mMapView.getOverlays();
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
// ToDo:

/*
 * bugs:
 * 1) when leaving open infowindow, and creating a new marker, the opened infowindow isn't closed
 * when tapping the marker
 * */



