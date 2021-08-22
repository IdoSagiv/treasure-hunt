package huji.postpc2021.treasure_hunt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

//

import java.util.ArrayList;

import huji.postpc2021.treasure_hunt.DataObjects.Clue;

public class CreatorNewGameFragment extends Fragment {


    MapHandler mapHandler = null;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private GeoPoint currentLocation = null;
    CreatorViewModel creatorViewModel = CreatorViewModel.getInstance();
    MapView mMapView = null;
    private LiveData<ArrayList<Clue>> cluesLiveData;


    public CreatorNewGameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("----oncreate view1");
        View view = inflater.inflate(R.layout.fragment_creator_new_game, container, false);

        Button creatorResetButton = view.findViewById(R.id.buttonCreateNewGame);
        Button addHintButton = view.findViewById(R.id.buttonAddHint);
        Button saveButton = view.findViewById(R.id.buttonSave);

        TextView location = view.findViewById(R.id.listOfLocationCreatorNewGameScreen);
        location.setVisibility(View.INVISIBLE);

        creatorResetButton.setOnClickListener(v ->
        {
            creatorViewModel.deleteAllClues();
            //TODO check
        });

        addHintButton.setOnClickListener(v ->
                {
//                    LocationListAdapter adapter = new LocationListAdapter();
//                    RecyclerView recyclerView = view.findViewById(R.id.recyclerLocationInNewGameScreen);
//                    recyclerView.setAdapter(adapter);
//                    recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
//
//
//                    cluesLiveData.observe(requireActivity(), clues ->
//                            adapter.setItems(cluesLiveData.getValue())
//                    );

//
//                    if (location.getVisibility() == View.INVISIBLE) {
//                        location.setVisibility(View.VISIBLE);
//                        recyclerView.setVisibility(View.VISIBLE);
//
//                    } else {
//                        location.setVisibility(View.INVISIBLE);
//                        recyclerView.setVisibility(View.INVISIBLE);
//                    }
//
//
                }
        );

        saveButton.setOnClickListener(v ->
        {
            //TODO upload all the data to firebase
        });

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = view.findViewById(R.id.mapViewCreatorNewGame);
        mapHandler = new MapHandler(mMapView, true, MapHandler.ViewerType.CreatorEdit);

        creatorViewModel.cluesLiveData.observe(getViewLifecycleOwner(), clues ->
                mapHandler.showHints(clues)
        );

        mapHandler.setLongPressCallback(p -> {
            Clue c = new Clue("my new hint!!!", 3, new com.google.firebase.firestore.GeoPoint(p.getLatitude(), p.getLongitude()));
            creatorViewModel.addClue(c);
        });
    }
}