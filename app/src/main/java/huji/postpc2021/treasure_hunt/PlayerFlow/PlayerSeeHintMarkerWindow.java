package huji.postpc2021.treasure_hunt.PlayerFlow;

import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import huji.postpc2021.treasure_hunt.R;
import huji.postpc2021.treasure_hunt.Utils.DataObjects.Clue;

public class PlayerSeeHintMarkerWindow extends InfoWindow {
    private final Marker relatedMarker;


    public PlayerSeeHintMarkerWindow(int layoutResId, MapView mapView, Marker marker) {
        super(layoutResId, mapView);
        this.relatedMarker = marker;
    }

    @Override
    public void onClose() {
    }

    @Override
    public void onOpen(Object arg0) {
        Clue relatedClue = PlayerViewModel.getInstance().gameLiveData.getValue().getClues().get(relatedMarker.getId());

        TextView hintContentEditText = mView.findViewById(R.id.textViewHintContentPlayerSeeHint);
        TextView clueIndexTextView = mView.findViewById(R.id.textViewHintIndexPlayerSeeMarkerWindow);

        hintContentEditText.setText(relatedClue.getDescription());
        clueIndexTextView.setText("#" + relatedClue.getIndex());

        hintContentEditText.setMovementMethod(new ScrollingMovementMethod());
    }
}