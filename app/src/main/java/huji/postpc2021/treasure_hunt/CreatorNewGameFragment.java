package huji.postpc2021.treasure_hunt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;

//
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;

import java.util.ArrayList;

import huji.postpc2021.treasure_hunt.DataObjects.Clue;

public class CreatorNewGameFragment extends Fragment {

    MapView map = null;
    MapHandler mapHandler = null;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private GeoPoint currentLocation = null;
    CreatorViewModal creatorViewModal = CreatorViewModal.getInstance();


    public CreatorNewGameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creator_new_game, container, false);
        Button createNewGameButton = view.findViewById(R.id.buttonCreateNewGame);
        Button addHintButton = view.findViewById(R.id.buttonAddHint);
        Button saveButton = view.findViewById(R.id.buttonSave);





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
//        creatorSignInButton.setOnClickListener(v ->
//        {
//            Navigation.findNavController(view)
//                    .navigate(CreatorRegisterFragmentDirections.actionCreatorRegisterFragmentToCreatorNewOrEditGameFragment());
//        });


        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MapView mMapView = view.findViewById(R.id.mapViewCreatorNewGame);
        mapHandler =creatorViewModal.setScreen(mMapView);

//
//        Clue c1 = new Clue("my first hint!!!", 1, new com.google.firebase.firestore.GeoPoint(32.1007, 34.8070));
//
//        Clue c2 = new Clue("my second hint!!!", 2, new com.google.firebase.firestore.GeoPoint(32.1015, 34.8079));

        mapHandler.setLongPressCallback(new OnMapLongPressCallback() {
            @Override
            public void OnLongPressCallback(GeoPoint p) {
                Clue c = new Clue("my new hint!!!", 3, new com.google.firebase.firestore.GeoPoint(p.getLatitude(), p.getLongitude()));
                creatorViewModal.addclue(c);
            }
        });
//        mapHandler.showHintOnMap(c1);
//        mapHandler.showHintOnMap(c2);

    }


}
