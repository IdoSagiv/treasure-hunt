package huji.postpc2021.treasure_hunt;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import org.osmdroid.views.MapView;

public class PlayerGameFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout scoreListDrawerLayout;
    private PlayerViewModel playerViewModel;

    public PlayerGameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_player_game, container, false);
        playerViewModel = PlayerViewModel.getInstance();

        ImageView openScoreListButton = view.findViewById(R.id.buttonSeeScore);
        openScoreListButton.setOnClickListener(v ->
                scoreListDrawerLayout.openDrawer(GravityCompat.START));

        ImageView exitGameButton = view.findViewById(R.id.buttonExitGamePlayerGame);
        exitGameButton.setOnClickListener(v -> leaveGame(view));

        MapView mMapView = view.findViewById(R.id.mapViewPlayerGame);
        MapHandler mapHandler = new MapHandler(mMapView, true, MapHandler.ViewerType.Player);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initScoreListDrawer(view);

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
    }


    private void initScoreListDrawer(View view) {
        scoreListDrawerLayout = view.findViewById(R.id.drawerLayoutScoreList);
        scoreListDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        scoreListDrawerLayout.findViewById(R.id.recyclerViewScoreListInDrawer);

        ScoreListAdapter adapter = new ScoreListAdapter();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewScoreListInDrawer);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));

        playerViewModel.gameLiveData.observe(getViewLifecycleOwner(), game ->
                adapter.setItems(game.getPlayers().values())
        );
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
