package huji.postpc2021.treasure_hunt.CreatorFlow.Fragments;

import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.osmdroid.views.MapView;

import huji.postpc2021.treasure_hunt.CreatorFlow.ClueLocationAdapter;
import huji.postpc2021.treasure_hunt.CreatorFlow.CreatorViewModel;
import huji.postpc2021.treasure_hunt.CreatorFlow.SwipeToDeleteClueCallback;
import huji.postpc2021.treasure_hunt.Utils.MessageBoxDialog;
import huji.postpc2021.treasure_hunt.Utils.MapHandler;
import huji.postpc2021.treasure_hunt.R;

public class CreatorEditGameFragment extends Fragment {
    private MapHandler mapHandler = null;
    private final CreatorViewModel creatorViewModel = CreatorViewModel.getInstance();
    private DrawerLayout gameSettingsDrawerLayout;
    private ClueLocationAdapter clueLocationAdapter;


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
        mapHandler = new MapHandler(mMapView, MapHandler.MarkersType.EditHint, getContext());
        initializeSettingsDrawer(view);

        // set views behavior

        centerMapButton.setOnClickListener(c -> mapHandler.mapToCurrentLocation());

        openHintsDrawerButton.setOnClickListener(v -> gameSettingsDrawerLayout.openDrawer(GravityCompat.START));

        launchGameButton.setOnClickListener(v ->
        {
            if (!creatorViewModel.launchGame(view)) {
                MessageBoxDialog dialog = new MessageBoxDialog(requireActivity());
                dialog.setTitle("Notice")
                        .setMessage("Game should include at least 3 clues")
                        .setOkButton("Ok", v1 -> {
                        })
                        .show();
            }
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

        creatorViewModel.cluesLiveData.observe(getViewLifecycleOwner(), clues -> {
            if (clues != null) {
                mapHandler.showHints(clues.values());
                clueLocationAdapter.setItems(clues.values());
            }
        });

        mapHandler.setLongPressCallback(creatorViewModel::addClue);
        mapHandler.setLongPressCallback(p -> mapHandler.openMarker(creatorViewModel.addClue(p)));
    }

    private void initializeSettingsDrawer(View view) {
        gameSettingsDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        clueLocationAdapter = new ClueLocationAdapter(requireContext());
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewHintsList);
        recyclerView.setAdapter(clueLocationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteClueCallback(clueLocationAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        clueLocationAdapter.goToMarkerBtnCallback = clue -> {
            gameSettingsDrawerLayout.closeDrawer(GravityCompat.START);
            mapHandler.openMarker(clue.getId());
        };

        clueLocationAdapter.onDeleteCallback = clue -> creatorViewModel.removeClue(clue.getId());

        Button deleteGameButton = view.findViewById(R.id.buttonDeleteGameEditGameScreen);
        deleteGameButton.setOnClickListener(v -> {
            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE: {
                        creatorViewModel.deleteGameFromEditScreen(view);
                        break;
                    }
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setMessage("Are you sure you want to delete the game?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener)
                    .show();
        });
    }
}