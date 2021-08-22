package huji.postpc2021.treasure_hunt;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.firestore.GeoPoint;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import huji.postpc2021.treasure_hunt.DataObjects.Clue;

public class CreatorEditHintWindow extends InfoWindow {
    public int index = 0;
    private final Marker relatedMarker;
    private EditText hintContentEditText;
    private ImageView saveButton;
    private ImageView deleteButton;
    private CreatorViewModel creatorViewModel = CreatorViewModel.getInstance();
    private boolean isDeleted;


    public CreatorEditHintWindow(int layoutResId, MapView mapView, Marker marker) {
        super(layoutResId, mapView);
        this.relatedMarker = marker;
        this.isDeleted = false;
//        todo: get the ruleId in order to save/delete it
    }

    public void onClose() {
        if (isDeleted) {
            creatorViewModel.removeClue(relatedMarker.getId());
        } else {
            creatorViewModel.editHint(relatedMarker.getId(), hintContentEditText.getText().toString());
        }
    }

    public void onOpen(Object arg0) {
        hintContentEditText = mView.findViewById(R.id.editTextHintContent);
        saveButton = mView.findViewById(R.id.buttonSaveHint);
        deleteButton = mView.findViewById(R.id.buttonDeleteHint);

//        todo: get the clue
//        Clue clue = new Clue(this.relatedMarker.getTitle(), 0, new GeoPoint (this.relatedMarker.getPosition().getLatitude(),this.relatedMarker.getPosition().getLongitude()));

        hintContentEditText.setText(this.relatedMarker.getTitle());

        saveButton.setEnabled(!hintContentEditText.getText().toString().isEmpty());

        hintContentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
//                saveButton.setEnabled(!hintContentEditText.getText().toString().isEmpty());
//                relatedMarker.setTitle(hintContentEditText.getText().toString());

//                creatorViewModel.changeHintId.setValue(relatedMarker.getId().toString());
//                creatorViewModel.changeHintText.setValue(hintContentEditText.getText().toString());
                            }
        });


        deleteButton.setOnClickListener(v -> {
//            todo: delete hint and close the window
//            Toast.makeText(TreasureHuntApp.getInstance(), "todo: delete hint", Toast.LENGTH_SHORT).show();
//            creatorViewModel.removeClue(relatedMarker.getId());
//            relatedMarker.remove(mMapView);
            isDeleted = true;
            relatedMarker.closeInfoWindow();
        });

        saveButton.setOnClickListener(v -> {
//            todo: save hint and close the window
//            Toast.makeText(TreasureHuntApp.getInstance(), "todo: save hint", Toast.LENGTH_SHORT).show();
            relatedMarker.closeInfoWindow();
        });
    }
}

/*
    todo:
    1) need save button? or should we save automaticaly(afterTextChanged)

* */