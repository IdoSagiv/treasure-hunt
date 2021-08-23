package huji.postpc2021.treasure_hunt.Utils;

import android.widget.EditText;
import android.widget.ImageView;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import huji.postpc2021.treasure_hunt.CreatorFlow.CreatorViewModel;
import huji.postpc2021.treasure_hunt.R;

public class CreatorEditHintWindow extends InfoWindow {
    private final Marker relatedMarker;
    private EditText hintContentEditText;
    private final CreatorViewModel creatorViewModel = CreatorViewModel.getInstance();
    private boolean deleteHint;
    private boolean saveHint;


    public CreatorEditHintWindow(int layoutResId, MapView mapView, Marker marker) {
        super(layoutResId, mapView);
        this.relatedMarker = marker;
        this.deleteHint = false;
        this.saveHint = false;
    }

    public void onClose() {
        if (deleteHint) {
            creatorViewModel.removeClue(relatedMarker.getId());
        } else if (saveHint) {
            relatedMarker.setTitle(hintContentEditText.getText().toString()); // todo: temp
            creatorViewModel.editClue(relatedMarker.getId(), hintContentEditText.getText().toString());
        }
    }

    public void onOpen(Object arg0) {
        hintContentEditText = mView.findViewById(R.id.editTextHintContent);
        ImageView saveButton = mView.findViewById(R.id.buttonSaveHint);
        ImageView deleteButton = mView.findViewById(R.id.buttonDeleteHint);

        // todo: get the hints description and set it as the text
        hintContentEditText.setText(this.relatedMarker.getTitle()); // todo: temp

        saveButton.setEnabled(!hintContentEditText.getText().toString().isEmpty());

        deleteButton.setOnClickListener(v -> {
            deleteHint = true;
            relatedMarker.closeInfoWindow();
        });

        saveButton.setOnClickListener(v -> {
            saveHint = true;
            relatedMarker.closeInfoWindow();
        });
    }
}