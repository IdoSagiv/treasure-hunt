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

        hintContentEditText.setText(this.relatedMarker.getTitle());

        saveButton.setEnabled(!hintContentEditText.getText().toString().isEmpty());

        deleteButton.setOnClickListener(v -> {
//            todo: delete hint and close the window
            isDeleted = true;
            relatedMarker.closeInfoWindow();
        });

        saveButton.setOnClickListener(v -> {
//            todo: save hint and close the window
            relatedMarker.closeInfoWindow();
        });


    }
}

/*
    todo:
    1) need save button? or should we save automaticaly(afterTextChanged)

* */