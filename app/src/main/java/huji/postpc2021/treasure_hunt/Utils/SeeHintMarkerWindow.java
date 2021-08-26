package huji.postpc2021.treasure_hunt.Utils;

import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import huji.postpc2021.treasure_hunt.CreatorFlow.CreatorViewModel;
import huji.postpc2021.treasure_hunt.R;
import huji.postpc2021.treasure_hunt.Utils.DataObjects.Clue;

public class SeeHintMarkerWindow extends InfoWindow {
    private final Marker relatedMarker;
    private Clue relatedClue = null;


    public SeeHintMarkerWindow(int layoutResId, MapView mapView, Marker marker) {
        super(layoutResId, mapView);
        this.relatedMarker = marker;
    }

    @Override
    public void onClose() {
    }

    @Override
    public void onOpen(Object arg0) {
        this.relatedClue = CreatorViewModel.getInstance().cluesLiveData.getValue().get(relatedMarker.getId());

        TextView hintContentEditText = mView.findViewById(R.id.textViewHintContent);
        TextView clueIndexTextView = mView.findViewById(R.id.textViewHintIndexSeeMarkerWindow);

        hintContentEditText.setText(this.relatedClue.getDescription());
        clueIndexTextView.setText("#" + relatedClue.getIndex());

        hintContentEditText.setMovementMethod(new ScrollingMovementMethod());
    }
}