package huji.postpc2021.treasure_hunt.PlayerFlow.Fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import huji.postpc2021.treasure_hunt.Utils.MessageBoxDialog;
import huji.postpc2021.treasure_hunt.Utils.DataObjects.GameStatus;
import huji.postpc2021.treasure_hunt.Utils.MapHandler;
import huji.postpc2021.treasure_hunt.PlayerFlow.PlayerViewModel;
import huji.postpc2021.treasure_hunt.PlayerFlow.ScoreListAdapter;
import huji.postpc2021.treasure_hunt.R;

import static android.content.Context.LOCATION_SERVICE;

public class PlayerGameFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener, LocationListener {
    private DrawerLayout scoreListDrawerLayout;
    private PlayerViewModel playerViewModel;
    private MapHandler mapHandler;
    private ImageView openArButton;
    private View view;

    public PlayerGameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_player_game, container, false);
        playerViewModel = PlayerViewModel.getInstance();

        // find views
        MapView mMapView = view.findViewById(R.id.mapViewPlayerGame);
        ImageView openScoreListButton = view.findViewById(R.id.buttonSeeScore);
        ImageView exitGameButton = view.findViewById(R.id.buttonExitGamePlayerGame);
        ImageView centerMapButton = view.findViewById(R.id.buttonCenterLocationPlayerGame);
        Button seeHintButton = view.findViewById(R.id.buttonSeeHint);
        openArButton = view.findViewById(R.id.buttonOpenAr);

        mapHandler = new MapHandler(mMapView, MapHandler.MarkersType.Player, getContext());

        LocationManager locationManager = (LocationManager) requireContext().getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // start location updates
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, this);
        }

        // set buttons behavior

        centerMapButton.setOnClickListener(v -> mapHandler.mapToCurrentLocation());

        openScoreListButton.setOnClickListener(v ->
                scoreListDrawerLayout.openDrawer(GravityCompat.START));

        exitGameButton.setOnClickListener(v -> leaveGame(view));

        // first check of the users location
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (lastKnownLocation != null) {
            if (playerViewModel.isCloseEnoughToShowAr(new GeoPoint(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()))) {
                openArButton.setVisibility(View.VISIBLE);
            }
        }

        openArButton.setOnClickListener(v -> openArScreen());

        seeHintButton.setOnClickListener(v -> showNextClueHint());

        return view;
    }

    private void showNextClueHint() {
        MessageBoxDialog dialog = new MessageBoxDialog(requireActivity());
        dialog.setTitle("Hint to the next clue")
                .setMessage(playerViewModel.getCurrentClueHint())
                .setOkButton("Ok", null)
                .show();
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
        locationManager.removeUpdates(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scoreListDrawerLayout = view.findViewById(R.id.drawerLayoutScoreList);
        scoreListDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        ScoreListAdapter adapter = new ScoreListAdapter();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewScoreListInDrawer);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));

        playerViewModel.gameLiveData.observe(getViewLifecycleOwner(), game -> {
            if (game != null) {
                adapter.setItems(game.getPlayers().values());
                if (game.getStatus() == GameStatus.finished) {
                    playerViewModel.gameOver(view);
                    return;
                }

                // show the found hints
                mapHandler.showHints(playerViewModel.getHintsUntil(game.getPlayer(playerViewModel.currentPlayerId()).getClueIndex()));
            }
        });

        // on back pressed callback for this fragment
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (scoreListDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    scoreListDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    leaveGame(view);
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        // show the first hint at the beginning
        showNextClueHint();
    }

    private void leaveGame(View view) {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE: {
                    playerViewModel.leaveGameFromGameScreen(view);
                    break;
                }
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("leave game?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.i("LocationListener", "player game location listener");

        GeoPoint userLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
        if (playerViewModel.isCloseEnoughToOpenCamera(userLocation)) {
            if (openArButton.getVisibility() == View.VISIBLE) {
                return;
            }

            openArButton.setVisibility(View.VISIBLE);

            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("You are close!")
                    .setMessage("The hint is very close to you!\nOpen the camera and search it")
                    .setPositiveButton("Open camera", (dialogInterface, i) -> openArScreen())
                    .setNegativeButton("Not now", null)
                    .show();
        } else {
            openArButton.setVisibility(View.GONE);
        }
    }


    private void openArScreen() {
        // check if there is permission to use the camera
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            playerViewModel.openAr(view);
        } else {
            ActivityResultLauncher<String> requestPermissionLauncher =
                    registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                        if (isGranted) {
                            playerViewModel.openAr(view);
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                            builder.setTitle("Permissions needed")
                                    .setMessage("In order to search the clue using AR\nwe need your permission to use the camera")
                                    .setPositiveButton("Grant permissions", (dialogInterface, i) -> openArScreen())
                                    .setNegativeButton("Not now", null)
                                    .show();
                        }
                    });

            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }
}
