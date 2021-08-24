package huji.postpc2021.treasure_hunt.Utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import huji.postpc2021.treasure_hunt.CreatorFlow.CreatorViewModel;
import huji.postpc2021.treasure_hunt.R;
import huji.postpc2021.treasure_hunt.Utils.DataObjects.Clue;

public class CreatorEditHintWindow extends InfoWindow {
    private final Marker relatedMarker;
    private EditText hintContentEditText;
    private final CreatorViewModel creatorViewModel = CreatorViewModel.getInstance();
    private boolean deleteHint;
    private boolean saveHint;
    private Clue relatedClue = null;


    public CreatorEditHintWindow(int layoutResId, MapView mapView, Marker marker) {
        super(layoutResId, mapView);
        this.relatedMarker = marker;
    }

    public void onClose() {
        if (deleteHint) {
            creatorViewModel.removeClue(relatedMarker.getId());
        } else if (saveHint) {
            relatedClue.setDescription(hintContentEditText.getText().toString());
            creatorViewModel.editClue(relatedClue);
        }
    }

    public void onOpen(Object arg0) {
        this.deleteHint = false;
        this.saveHint = false;
        this.relatedClue = CreatorViewModel.getInstance().cluesLiveData.getValue().get(relatedMarker.getId());

        hintContentEditText = mView.findViewById(R.id.editTextHintContent);
        TextView clueIndexTextView = mView.findViewById(R.id.editTextHintIndexEditMarkerWindow);
        ImageView saveButton = mView.findViewById(R.id.buttonSaveHint);
        ImageView deleteButton = mView.findViewById(R.id.buttonDeleteHint);

        hintContentEditText.setText(this.relatedClue.getDescription());
        clueIndexTextView.setText("#" + relatedClue.getIndex());

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
                saveButton.setEnabled(!hintContentEditText.getText().toString().isEmpty());
            }
        });

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