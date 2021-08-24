package huji.postpc2021.treasure_hunt.CreatorFlow.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.osmdroid.views.MapView;

import huji.postpc2021.treasure_hunt.CreatorFlow.CreatorViewModel;
import huji.postpc2021.treasure_hunt.Utils.DataObjects.Clue;
import huji.postpc2021.treasure_hunt.Utils.MapHandler;
import huji.postpc2021.treasure_hunt.R;

public class CreatorEditGameFragment extends Fragment {
    private MapHandler mapHandler = null;
    private final CreatorViewModel creatorViewModel = CreatorViewModel.getInstance();


    public CreatorEditGameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creator_edit_game, container, false);

        // find views
        MapView mMapView = view.findViewById(R.id.mapViewCreatorNewGame);
        ImageView openHintsDrawerButton = view.findViewById(R.id.buttonOpenHintsListDrawer);
        Button saveGameButton = view.findViewById(R.id.buttonSave);
        ImageView centerMapButton = view.findViewById(R.id.buttonCenterLocationCreatorEditGame);
        TextView location = view.findViewById(R.id.listOfLocationCreatorNewGameScreen);

        // initialize map
        mapHandler = new MapHandler(mMapView, true, MapHandler.ViewerType.CreatorEdit);

        // set buttons behavior

        location.setVisibility(View.INVISIBLE);

        centerMapButton.setOnClickListener(c -> mapHandler.mapToCurrentLocation());

        openHintsDrawerButton.setOnClickListener(v ->
        {
            // todo: open the drawer
        });

        saveGameButton.setOnClickListener(v ->
        {
            //TODO upload all the data to firebase
        });

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        creatorViewModel.cluesLiveData.observe(getViewLifecycleOwner(), clues ->
                mapHandler.showHints(clues)
        );

        mapHandler.setLongPressCallback(p -> {
            // todo: keep track of the index
            Clue c = new Clue("", 3, new com.google.firebase.firestore.GeoPoint(p.getLatitude(), p.getLongitude()));
            creatorViewModel.addClue(c);
        });
    }
}