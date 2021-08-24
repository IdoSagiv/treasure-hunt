package huji.postpc2021.treasure_hunt.CreatorFlow.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.osmdroid.views.MapView;

import huji.postpc2021.treasure_hunt.CreatorFlow.ClueLocationAdapter;
import huji.postpc2021.treasure_hunt.CreatorFlow.CreatorViewModel;
import huji.postpc2021.treasure_hunt.Utils.DataObjects.Clue;
import huji.postpc2021.treasure_hunt.Utils.MapHandler;
import huji.postpc2021.treasure_hunt.R;
import huji.postpc2021.treasure_hunt.Utils.OnClueClickCallback;

public class CreatorEditGameFragment extends Fragment {
    private MapHandler mapHandler = null;
    private final CreatorViewModel creatorViewModel = CreatorViewModel.getInstance();
    private DrawerLayout gameSettingsDrawerLayout;


    public CreatorEditGameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creator_edit_game, container, false);

        // find views
        gameSettingsDrawerLayout = view.findViewById(R.id.drawerLayoutGameSettings);
        MapView mMapView = view.findViewById(R.id.mapViewCreatorNewGame);
        ImageView openHintsDrawerButton = view.findViewById(R.id.buttonOpenHintsListDrawer);
        Button launchGameButton = view.findViewById(R.id.buttonLaunchGame);
        ImageView centerMapButton = view.findViewById(R.id.buttonCenterLocationCreatorEditGame);
        EditText gameNameEditText = view.findViewById(R.id.editTextGameNameInSettings);

        // initialize map and drawer
        mapHandler = new MapHandler(mMapView, MapHandler.ViewerType.CreatorEdit);
        initializeSettingsDrawer(view);

        // set views behavior

        centerMapButton.setOnClickListener(c -> mapHandler.mapToCurrentLocation());

        openHintsDrawerButton.setOnClickListener(v -> gameSettingsDrawerLayout.openDrawer(GravityCompat.START));

        launchGameButton.setOnClickListener(v ->
        {
            //TODO upload all the data to firebase
        });

        gameNameEditText.setText(creatorViewModel.currentGame.getValue().getName());
        gameNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                creatorViewModel.updateCurrentGameName(gameNameEditText.getText().toString());
            }
        });

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        creatorViewModel.cluesLiveData.observe(getViewLifecycleOwner(), clues ->
                mapHandler.showHints(clues.values())
        );

        mapHandler.setLongPressCallback(creatorViewModel::addClue);
    }

    private void initializeSettingsDrawer(View view) {
        gameSettingsDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        ClueLocationAdapter adapter = new ClueLocationAdapter();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewHintsList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        adapter.goToMarkerBtnCallback = clue -> {
            gameSettingsDrawerLayout.closeDrawer(GravityCompat.START);
            mapHandler.openMarker(clue.getId());
        };

        creatorViewModel.currentGame.observe(getViewLifecycleOwner(), game ->
                {
                    if (game == null) {
                        return;
                    }
                    adapter.setItems(game.getClues().values());

                }
        );
    }
}