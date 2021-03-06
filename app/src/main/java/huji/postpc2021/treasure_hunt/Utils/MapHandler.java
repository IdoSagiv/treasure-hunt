package huji.postpc2021.treasure_hunt.Utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import androidx.annotation.NonNull;
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
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.Collection;

import huji.postpc2021.treasure_hunt.CreatorFlow.CreatorSeeHintMarkerWindow;
import huji.postpc2021.treasure_hunt.CreatorFlow.EditHintMarkerWindow;
import huji.postpc2021.treasure_hunt.PlayerFlow.PlayerSeeHintMarkerWindow;
import huji.postpc2021.treasure_hunt.R;
import huji.postpc2021.treasure_hunt.Utils.DataObjects.Clue;

import static android.content.Context.LOCATION_SERVICE;

public class MapHandler implements LocationListener {
    private static final GeoPoint DEFAULT_START_POINT = new GeoPoint(32.1007, 34.8070);
    private static final double MAP_DEFAULT_ZOOM = 18.0;
    private static final double MAP_MAX_ZOOM = 22.0;
    private static final double MAP_MIN_ZOOM = 9.0;
    private final MapView mMapView;
    private GeoPoint startPoint;
    private final MarkersType markersType;
    public OnMapLongPressCallback longPressCallback = null;
    private final Context context;
    public Marker.OnMarkerDragListener markerDragListener = null;
    private MyLocationNewOverlay myLocationOverlay = null;

    /**
     * @param mapView the founded mapView
     */
    public MapHandler(MapView mapView, MarkersType markersType, Context context, GeoPoint startPoint) {
        this.startPoint = startPoint;
        this.mMapView = mapView;
        this.context = context;
        this.markersType = markersType;

        initStartPoint();
        initMap();
    }

    /**
     * @param mapView the founded mapView
     */
    public MapHandler(MapView mapView, MarkersType markersType, Context context) {
        this(mapView, markersType, context, null);
    }

    private void showHintOnMap(Clue clue) {
        GeoPoint location = clue.location();

        Marker myMarker = new Marker(mMapView);
        myMarker.setId(clue.getId());
        myMarker.setPosition(location);
        myMarker.setTitle(clue.getDescription());
        myMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);

        // todo: choose marker icon
//        myMarker.setIcon(ResourcesCompat.getDrawable(context.getResources(), R.drawable.pirate_flag, context.getTheme()));

        switch (markersType) {
            case Player: {
                myMarker.setInfoWindow(new PlayerSeeHintMarkerWindow(R.layout.marker_window_player_see_hint, mMapView, myMarker));
                myMarker.setDraggable(false);
                break;
            }
            case CreatorEdit: {
                myMarker.setInfoWindow(new EditHintMarkerWindow(R.layout.marker_window_edit_hint, mMapView, myMarker));
                myMarker.setDraggable(true);
                if (markerDragListener != null) {
                    myMarker.setOnMarkerDragListener(markerDragListener);
                }
                break;
            }
            case CreatorInPlay: {
                myMarker.setInfoWindow(new CreatorSeeHintMarkerWindow(R.layout.marker_window_creator_see_hint, mMapView, myMarker));
                myMarker.setDraggable(false);
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
    }

    /**
     * if start point was not specified, start in the last known location of the user
     */
    private void initStartPoint() {
        if (startPoint != null) return;

        LocationManager mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, this);
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, this);
            Location lastLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (lastLocation != null) {
                startPoint = new GeoPoint(lastLocation.getLatitude(), lastLocation.getLongitude());
            } else {
                startPoint = DEFAULT_START_POINT;
            }
        } else {
            Log.e("MapHandler", "missing permissions for location updates");
        }
    }

    public void closeAllMarkers() {
        InfoWindow.closeAllInfoWindowsOn(mMapView);
    }

    private void addMyLocationIconOnMap() {
        // set my location on the map
        myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(context), mMapView);
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.setOptionsMenuEnabled(true);
        myLocationOverlay.setDrawAccuracyEnabled(false);
        myLocationOverlay.getMyLocation();

        myLocationOverlay.setPersonIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.pirate_icon));

        // add to map
        mMapView.getOverlays().add(myLocationOverlay);
    }

    public enum MarkersType {
        Player,
        CreatorEdit,
        CreatorInPlay
    }

    public void mapToCurrentLocation() {
        if (myLocationOverlay != null) {
            centerMapTo(myLocationOverlay.getMyLocation());
        }
    }

    public void centerMapTo(IGeoPoint centerTo) {
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
                centerMapTo(marker.getPosition());
            }
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.i("LocationListener", "map handler location listener");
        // listen once in order to remember
        LocationManager mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        mLocationManager.removeUpdates(this);
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