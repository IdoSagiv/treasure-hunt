package huji.postpc2021.treasure_hunt.PlayerFlow.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Pose;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;


import org.osmdroid.util.GeoPoint;

import java.util.Iterator;
import java.util.List;

import huji.postpc2021.treasure_hunt.PlayerFlow.PlayerViewModel;
import huji.postpc2021.treasure_hunt.R;

import static android.content.Context.LOCATION_SERVICE;

public class arFragment extends Fragment {
    private PlayerViewModel playerViewModel;
    private ArFragment arFragment;
    private Button showArButton;
    private ModelRenderable modelRenderable = null;
    private boolean ar_placed = false;
    private LocationListener locationListener;
    private View view;
    private Scene.OnUpdateListener onUpdateListener;

    public arFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ar, container, false);
        playerViewModel = PlayerViewModel.getInstance();

        arFragment = (ArFragment) getChildFragmentManager().findFragmentById(R.id.ar_fragment);
        showArButton = view.findViewById(R.id.temp_show_ar_button);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // build the AR object
        ModelRenderable.builder().setSource(requireContext(), Uri.parse("Mail.sfb")).build()
                .thenAccept(modelRenderable -> this.modelRenderable = modelRenderable)
                .exceptionally(throwable -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    builder.setMessage(throwable.getMessage()).show();
                    return null;
                });

        // set location listener
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                if (location == null) return;
                GeoPoint userLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
                if (!ar_placed && playerViewModel.isCloseEnoughToShowAr(userLocation)) {
                    Log.i("LocationListener", "try to add ar object");
                    Toast.makeText(requireContext(), "try to add ar object!!!!!!!", Toast.LENGTH_SHORT).show();
                    placeArObject();
                    playerViewModel.clueFound();
                }
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
            }
        };

        LocationManager locationManager = (LocationManager) requireContext().getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // start location updates
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener);
        }

    }

    private void placeArObject() {
         onUpdateListener = new Scene.OnUpdateListener() {
            @Override
            public void onUpdate(FrameTime frameTime) {
                //get the frame from the scene for shorthand
                Frame frame = arFragment.getArSceneView().getArFrame();
                if (frame != null && !ar_placed) {
                    //get the trackables to ensure planes are detected
                    Iterator<Plane> planeIterator = frame.getUpdatedTrackables(Plane.class).iterator();
                    if (planeIterator.hasNext()) {
                        System.out.println("----foundPlane");
                        Plane plane = planeIterator.next();

                        //If a plane has been detected & is being tracked by ARCore
                        if (plane.getTrackingState() == TrackingState.TRACKING) {

                            //Hide the plane discovery helper animation
                            arFragment.getPlaneDiscoveryController().hide();

                            //Perform a hit test at the center of the screen to place an object without tapping
                            List<HitResult> hitTest = frame.hitTest(screenCenter().x, screenCenter().y);

                            //iterate through all hits
                            Iterator<HitResult> hitTestIterator = hitTest.iterator();
                            if (hitTestIterator.hasNext()) {
                                ar_placed = true;
                                HitResult hitResult = hitTestIterator.next();

                                //Create an anchor at the plane hit
                                Anchor modelAnchor = plane.createAnchor(hitResult.getHitPose());

                                //Attach a node to this anchor with the scene as the parent
                                AnchorNode anchorNode = new AnchorNode(modelAnchor);
                                anchorNode.setParent(arFragment.getArSceneView().getScene());
                                anchorNode.setRenderable(modelRenderable);
                                anchorNode.setWorldPosition(new Vector3(modelAnchor.getPose().tx(),
                                        modelAnchor.getPose().compose(Pose.makeTranslation(0f, 0.05f, 0f)).ty(),
                                        modelAnchor.getPose().tz()));
                                anchorNode.setOnTapListener((hitTestResult, motionEvent) -> {
                                    Toast toast = Toast.makeText(requireContext(), "message", Toast.LENGTH_SHORT);
                                    toast.show();
                                });
                                arFragment.getArSceneView().getScene().removeOnUpdateListener(onUpdateListener);
                            }
                        }

                    }
                }
            }
        };
        arFragment.getArSceneView().getScene().addOnUpdateListener(onUpdateListener);
    }

    private Vector3 screenCenter() {
        View vw = view.findViewById(R.id.ar_fragment);
        return new Vector3(vw.getWidth() / 2f, vw.getHeight() / 2f, 0f);
    }

    @Override
    public void onDestroy() {
        stopLocationUpdates();
        super.onDestroy();
    }

    @Nullable
    @Override
    public Object getExitTransition() {
        stopLocationUpdates();
        return super.getExitTransition();
    }

    private void stopLocationUpdates() {
        LocationManager locationManager = (LocationManager) requireContext().getSystemService(LOCATION_SERVICE);
        locationManager.removeUpdates(locationListener);
    }
}
