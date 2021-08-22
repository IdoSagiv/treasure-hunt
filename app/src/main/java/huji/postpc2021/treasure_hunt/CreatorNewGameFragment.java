package huji.postpc2021.treasure_hunt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.*;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


//        map = view.findViewById(R.id.mapView);
//
//        map.getOverlays().clear();
//        map.setTileSource(TileSourceFactory.OpenTopo);
//        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT);
//        map.setMultiTouchControls(true);
//        IMapController mapController = map.getController();
//        mapController.setZoom(18);
//        GeoPoint startPoint = new GeoPoint(32.1007, 34.8070);
//        mapController.setCenter(startPoint);
//


        creatorResetButton.setOnClickListener(v ->
        {
            creatorViewModel.deleteAllClues();
//            mapHandler.showHints(creatorViewModel.getClues());
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
//        mapHandler = creatorViewModel.setScreen(mMapView);
        mapHandler = new MapHandler(mMapView, true, MapHandler.ViewerType.CreatorEdit);

//        cluesLiveData = creatorViewModel.getCluesLiveData();

        creatorViewModel.cluesMutableLiveData.observe(requireActivity(), clues ->
                mapHandler.showHints(clues)
        );


        mapHandler.setLongPressCallback(new OnMapLongPressCallback() {
            @Override
            public void OnLongPressCallback(GeoPoint p) {
                Clue c = new Clue("my new hint!!!", 3, new com.google.firebase.firestore.GeoPoint(p.getLatitude(), p.getLongitude()));
                creatorViewModel.addclue(c);
            }
        });


//        mapHandler.showHintOnMap(c1);
//        mapHandler.showHintOnMap(c2);

    }


}
