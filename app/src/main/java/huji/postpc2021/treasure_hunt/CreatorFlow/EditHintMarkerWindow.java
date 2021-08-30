package huji.postpc2021.treasure_hunt.CreatorFlow;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import huji.postpc2021.treasure_hunt.R;
import huji.postpc2021.treasure_hunt.Utils.DataObjects.Clue;

public class EditHintMarkerWindow extends InfoWindow {
    private final Marker relatedMarker;
    private EditText hintContentEditText;
    private RatingBar ratingBar;
    private final CreatorViewModel creatorViewModel = CreatorViewModel.getInstance();
    private boolean deleteHint;
    private boolean saveHint;
    private Clue relatedClue = null;


    public EditHintMarkerWindow(int layoutResId, MapView mapView, Marker marker) {
        super(layoutResId, mapView);
        this.relatedMarker = marker;
    }

    @Override
    public void onClose() {
        if (deleteHint) {
            creatorViewModel.removeClue(relatedMarker.getId());
        } else if (saveHint) {
            relatedClue.setDescription(hintContentEditText.getText().toString());
            relatedClue.setDifficulty((int) ratingBar.getRating());
            creatorViewModel.editClue(relatedClue);
        }
    }

    @Override
    public void onOpen(Object arg0) {
        this.deleteHint = false;
        this.saveHint = false;
        this.relatedClue = CreatorViewModel.getInstance().cluesLiveData.getValue().get(relatedMarker.getId());

        hintContentEditText = mView.findViewById(R.id.editTextHintContent);
        ratingBar = mView.findViewById(R.id.ratingBarEditHint);
        TextView clueIndexTextView = mView.findViewById(R.id.textViewHintIndexEditMarkerWindow);
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

        ratingBar.setRating(relatedClue.getDifficulty());
        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) ->
                ratingBar.setRating(Math.max(1, rating)));
    }
}