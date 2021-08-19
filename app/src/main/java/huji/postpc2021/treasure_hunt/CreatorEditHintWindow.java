package huji.postpc2021.treasure_hunt;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

public class CreatorEditHintWindow extends InfoWindow {
    public int index = 0;
    private final Marker relatedMarker;
    private EditText hintContentEditText;
    ImageView saveButton;
    ImageView deleteButton;
    CreatorViewModal creatorViewModal = CreatorViewModal.getInstance();



    public CreatorEditHintWindow(int layoutResId, MapView mapView, Marker marker) {
        super(layoutResId, mapView);
        this.relatedMarker = marker;
//        todo: get the ruleId in order to save/delete it
    }

    public void onClose() {
    }

    public void onOpen(Object arg0) {
        hintContentEditText = mView.findViewById(R.id.editTextHintContent);
        saveButton = mView.findViewById(R.id.buttonSaveHint);
        deleteButton = mView.findViewById(R.id.buttonDeleteHint);

        hintContentEditText.setText(relatedMarker.getTitle());

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
                relatedMarker.setTitle(hintContentEditText.getText().toString());

                creatorViewModal.changeHintId.setValue(relatedMarker.getId().toString());
                creatorViewModal.changeHintText.setValue(hintContentEditText.getText().toString());
                creatorViewModal.changeHint();
            }
        });


        deleteButton.setOnClickListener(v -> {
//            todo: delete hint and close the window
            Toast.makeText(TreasureHuntApp.getInstance(), "todo: delete hint", Toast.LENGTH_SHORT).show();
            creatorViewModal.deleteid.setValue(relatedMarker.getId().toString());
            creatorViewModal.removeClue();
            relatedMarker.remove(mMapView);
            relatedMarker.closeInfoWindow();
        });

        saveButton.setOnClickListener(v -> {
//            todo: save hint and close the window
            Toast.makeText(TreasureHuntApp.getInstance(), "todo: save hint", Toast.LENGTH_SHORT).show();
            relatedMarker.closeInfoWindow();
        });
    }
}

/*
    todo:
    1) need save button? or should we save automaticaly(afterTextChanged)

* */