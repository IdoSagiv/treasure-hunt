package huji.postpc2021.treasure_hunt.CreatorFlow.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.osmdroid.views.MapView;

import huji.postpc2021.treasure_hunt.CreatorFlow.CreatorViewModel;
import huji.postpc2021.treasure_hunt.PlayerFlow.ScoreListAdapter;
import huji.postpc2021.treasure_hunt.R;
import huji.postpc2021.treasure_hunt.Utils.MapHandler;

public class CreatorInPlayFragment extends Fragment {
    private CreatorViewModel creatorViewModel;

    public CreatorInPlayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creator_in_play, container, false);
        creatorViewModel = CreatorViewModel.getInstance();

        MapView mapView = view.findViewById(R.id.mapViewCreatorInPlay);
        DrawerLayout playersStatusDrawerLayout = view.findViewById(R.id.drawerLayoutPlayersStatus);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewParticipantsCreatorPlay);
        Button endGameButton = view.findViewById(R.id.buttonEndGame);
        ImageView openPlayersStatusButton = view.findViewById(R.id.buttonPlayersStatus);
        ImageView centerMapButton = view.findViewById(R.id.buttonCenterLocationCreatorInPlay);

        MapHandler mapHandler = new MapHandler(mapView, MapHandler.MarkersType.HintAndPlayers, getContext());

        playersStatusDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        ScoreListAdapter adapter = new ScoreListAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));

        creatorViewModel.currentGame.observe(getViewLifecycleOwner(), game -> {
            if (game == null) {
                return;
            }
            adapter.setItems(game.getPlayers().values());
            mapHandler.showHints(game.getClues().values());
        });

        openPlayersStatusButton.setOnClickListener(v ->
                playersStatusDrawerLayout.openDrawer(GravityCompat.START));

        endGameButton.setOnClickListener(v -> creatorViewModel.endGame(view));

        centerMapButton.setOnClickListener(v -> mapHandler.mapToCurrentLocation());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // on back pressed callback for this fragment
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                creatorViewModel.leaveInPlayScreen(view);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }
}
