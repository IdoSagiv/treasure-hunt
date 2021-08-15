package huji.postpc2021.treasure_hunt;
import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

//
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.GeoPoint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import org.osmdroid.LocationListenerProxy;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import org.osmdroid.config.Configuration;

public class CreatorNewGameFragment extends Fragment {

    MapView map = null;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private GeoPoint currentLocation = null;

    public CreatorNewGameFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_creator_new_game, container, false);
        PlayerViewModel playerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);
        Button createNewGameButton = view.findViewById(R.id.buttonCreateNewGame);
        Button addHintButton = view.findViewById(R.id.buttonAddHint);
        Button saveButton = view.findViewById(R.id.buttonSave);




        map = view.findViewById(R.id.mapView);
        map.setTileSource(TileSourceFactory.OpenTopo);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(15);
        GeoPoint startPoint = new GeoPoint(48.8583, 2.2944);
        mapController.setCenter(startPoint);



//
//        creatorSignInButton.setOnClickListener(v ->
//        {
//            Navigation.findNavController(view)
//                    .navigate(CreatorRegisterFragmentDirections.actionCreatorRegisterFragmentToCreatorNewOrEditGameFragment());
//        });






        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


}
