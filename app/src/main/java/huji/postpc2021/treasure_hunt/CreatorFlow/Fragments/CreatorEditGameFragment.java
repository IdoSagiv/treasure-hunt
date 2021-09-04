package huji.postpc2021.treasure_hunt.CreatorFlow.Fragments;

import android.annotation.SuppressLint;
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
import android.widget.TextView;

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
import org.osmdroid.views.overlay.Marker;

import huji.postpc2021.treasure_hunt.CreatorFlow.ClueLocationAdapter;
import huji.postpc2021.treasure_hunt.CreatorFlow.CreatorViewModel;
import huji.postpc2021.treasure_hunt.CreatorFlow.DragToRearrangeCluesCallback;
import huji.postpc2021.treasure_hunt.CreatorFlow.SwipeToDeleteClueCallback;
import huji.postpc2021.treasure_hunt.Utils.MessageBoxDialog;
import huji.postpc2021.treasure_hunt.Utils.MapHandler;
import huji.postpc2021.treasure_hunt.R;
import huji.postpc2021.treasure_hunt.Utils.UtilsFunctions;

public class CreatorEditGameFragment extends Fragment {
    private MapHandler mapHandler = null;
    private final CreatorViewModel creatorViewModel = CreatorViewModel.getInstance();
    private DrawerLayout gameSettingsDrawerLayout;
    private ClueLocationAdapter clueLocationAdapter;


    public CreatorEditGameFragment() {
        // Required empty public constructor
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creator_edit_game, container, false);

        // find views
        gameSettingsDrawerLayout = view.findViewById(R.id.drawerLayoutGameSettings);
        MapView mMapView = view.findViewById(R.id.mapViewCreatorNewGame);
        ImageView openHintsDrawerButton = view.findViewById(R.id.buttonOpenHintsListDrawer);
        Button launchGameButton = view.findViewById(R.id.buttonLaunchGame);
        ImageView centerMapButton = view.findViewById(R.id.buttonCenterLocationCreatorEditGame);
        ImageView helpButton = view.findViewById(R.id.buttonHelpCreatorEditGame);
        EditText gameNameEditText = view.findViewById(R.id.editTextGameNameInSettings);

        mMapView.setOnTouchListener((v, event) -> {
            UtilsFunctions.hideKeyboard(requireActivity());
            return false;
        });

        // initialize map and drawer
        mapHandler = new MapHandler(mMapView, MapHandler.MarkersType.CreatorEdit, getContext());
        initializeSettingsDrawer(view);

        // set views behavior

        centerMapButton.setOnClickListener(c -> mapHandler.mapToCurrentLocation());

        openHintsDrawerButton.setOnClickListener(v -> {
            mapHandler.closeAllMarkers();
            UtilsFunctions.hideKeyboard(requireActivity());
            gameSettingsDrawerLayout.openDrawer(GravityCompat.START);
        });

        launchGameButton.setOnClickListener(v ->
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Notice")
                    .setMessage("Once you launch the game you wont be able to edit it anymore")
                    .setPositiveButton("Continue", (dialog1, which) -> {
                        if (!creatorViewModel.launchGame(view)) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(requireContext());
                            dialog.setTitle("Notice")
                                    .setMessage("Game should include at least 3 clues")
                                    .setPositiveButton("Ok", null)
                                    .show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        helpButton.setOnClickListener(v -> showInstructionsPopup());

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
    public void onDestroy() {
        super.onDestroy();
        mapHandler.stopLocationUpdates();
    }

    @Nullable
    @Override
    public Object getExitTransition() {
        mapHandler.stopLocationUpdates();
        return super.getExitTransition();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewHintsList);
        TextView emptyRecyclerViewTextView = view.findViewById(R.id.textViewEmptyRecycler);

        recyclerView.setVisibility(View.GONE);
        emptyRecyclerViewTextView.setVisibility(View.VISIBLE);


        creatorViewModel.cluesLiveData.observe(getViewLifecycleOwner(), clues -> {
            if (clues != null) {
                mapHandler.showHints(clues.values());
                clueLocationAdapter.setItems(clues.values());
                if (clues.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyRecyclerViewTextView.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    emptyRecyclerViewTextView.setVisibility(View.VISIBLE);
                }
            }
        });

        mapHandler.longPressCallback = p -> mapHandler.openMarker(creatorViewModel.addClue(p));
        mapHandler.markerDragListener = new Marker.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                creatorViewModel.changeClueLocation(marker.getId(), marker.getPosition());
            }

            @Override
            public void onMarkerDragStart(Marker marker) {
                mapHandler.closeAllMarkers();
            }
        };

        showInstructionsPopup();
    }

    private void showInstructionsPopup() {
        MessageBoxDialog dialog = new MessageBoxDialog(requireActivity());
        dialog.setTitle("Instructions")
                .setMessage("1. Add clues on long press\n" +
                        "2. For each clue choose difficulty and write a hint to it's location\n" +
                        "3. Change clue location by dragging it\n" +
                        "4. Click on the edit clues icon in order to view all clues and reorder them\n" +
                        "5. Locate at least 3 clues and launch the game")
                .setOkButton("OK", null)
                .show();
    }

    private void initializeSettingsDrawer(View view) {
        gameSettingsDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        clueLocationAdapter = new ClueLocationAdapter(requireContext());
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewHintsList);
        recyclerView.setAdapter(clueLocationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));

        ItemTouchHelper swipeTouchHelper = new ItemTouchHelper(new SwipeToDeleteClueCallback(clueLocationAdapter));
        swipeTouchHelper.attachToRecyclerView(recyclerView);
        ItemTouchHelper dragTouchHelper = new ItemTouchHelper(new DragToRearrangeCluesCallback(clueLocationAdapter));
        dragTouchHelper.attachToRecyclerView(recyclerView);

        clueLocationAdapter.goToMarkerBtnCallback = clue -> {
            gameSettingsDrawerLayout.closeDrawer(GravityCompat.START);
            mapHandler.openMarker(clue.getId());
        };

        clueLocationAdapter.onDeleteCallback = clue -> creatorViewModel.removeClue(clue.getId());
        clueLocationAdapter.startDragCallback = dragTouchHelper::startDrag;
        clueLocationAdapter.reorderClue = creatorViewModel::changeClueIndex;

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