package huji.postpc2021.treasure_hunt.PlayerFlow.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

public class PlayerGameFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout scoreListDrawerLayout;
    private PlayerViewModel playerViewModel;
    private MapHandler mapHandler;

    public PlayerGameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_player_game, container, false);
        playerViewModel = PlayerViewModel.getInstance();

        // find views
        MapView mMapView = view.findViewById(R.id.mapViewPlayerGame);
        ImageView openScoreListButton = view.findViewById(R.id.buttonSeeScore);
        ImageView exitGameButton = view.findViewById(R.id.buttonExitGamePlayerGame);
        ImageView centerMapButton = view.findViewById(R.id.buttonCenterLocationPlayerGame);
        Button seeHintButton = view.findViewById(R.id.buttonSeeHint);

        mapHandler = new MapHandler(mMapView, MapHandler.MarkersType.HintOnly, getContext());

        // set buttons behavior

        centerMapButton.setOnClickListener(v -> mapHandler.mapToCurrentLocation());

        openScoreListButton.setOnClickListener(v ->
                scoreListDrawerLayout.openDrawer(GravityCompat.START));

        exitGameButton.setOnClickListener(v -> leaveGame(view));

        seeHintButton.setOnClickListener(v -> showNextClueHint("Next hint"));

        return view;
    }

    private void showNextClueHint(String title) {
        MessageBoxDialog dialog = new MessageBoxDialog(requireActivity());
        dialog.setTitle(title)
                .setMessage(playerViewModel.getCurrentClueHint())
                .setOkButton("Ok", v -> {
                })
                .show();
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
                    mapHandler.locationChangedCallback = null;
                    return;
                }

                // show the found hints
                mapHandler.showHints(playerViewModel.getHintsUntil(game.getPlayer(playerViewModel.currentPlayerId()).getClueIndex()));
            }
        });

        mapHandler.locationChangedCallback = location -> {
            GeoPoint userLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
            if (playerViewModel.isCloseEnoughToClue(userLocation)) {
                // todo: open AR
                playerViewModel.clueFound();

                if (playerViewModel.isFinishedGame(playerViewModel.currentPlayerId())) {
                    // found the last hint
                    MessageBoxDialog dialog = new MessageBoxDialog(requireActivity());
                    dialog.setTitle("Congrats!")
                            .setMessage("You found all hints!")
                            .setOkButton("Ok", v -> {
                            })
                            .show();
                    playerViewModel.gameOver(view);
                    mapHandler.locationChangedCallback = null;
                } else {
                    MessageBoxDialog dialog = new MessageBoxDialog(requireActivity());
                    dialog.setTitle("Congrats!")
                            .setMessage("You found a hint!\nGo search for the next one")
                            .setOkButton("Ok", v -> {
                            })
                            .show();
                }
            }
        };

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
        showNextClueHint("Get your first hint");
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

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("leave game?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}
