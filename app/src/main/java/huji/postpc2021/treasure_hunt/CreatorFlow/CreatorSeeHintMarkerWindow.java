package huji.postpc2021.treasure_hunt.CreatorFlow;

import android.annotation.SuppressLint;
import android.widget.RatingBar;
import android.widget.TextView;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import huji.postpc2021.treasure_hunt.R;
import huji.postpc2021.treasure_hunt.Utils.DataObjects.Clue;
import huji.postpc2021.treasure_hunt.Utils.DataObjects.Game;

public class CreatorSeeHintMarkerWindow extends InfoWindow {
    private final Marker relatedMarker;

    public CreatorSeeHintMarkerWindow(int layoutResId, MapView mapView, Marker marker) {
        super(layoutResId, mapView);
        this.relatedMarker = marker;
    }

    @Override
    public void onClose() {
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onOpen(Object arg0) {
        CreatorViewModel creatorViewModel = CreatorViewModel.getInstance();
        Game game = creatorViewModel.currentGame.getValue();
        Clue relatedClue = creatorViewModel.cluesLiveData.getValue().get(relatedMarker.getId());

        TextView hintContentEditText = mView.findViewById(R.id.textViewHintContentCreatorSeeHint);
        TextView clueIndexTextView = mView.findViewById(R.id.textViewHintIndexCreatorSeeMarkerWindow);
        TextView visitedTextView = mView.findViewById(R.id.textViewVisited);
        RatingBar ratingBar = mView.findViewById(R.id.ratingBarSeeHint);

        hintContentEditText.setText(relatedClue.getDescription());
        clueIndexTextView.setText(String.format("#%d", relatedClue.getIndex()));
        visitedTextView.setText(String.format("visited: %d/%d",
                relatedClue.getVisitedPlayersId().size(), game.getPlayers().values().size()));

        ratingBar.setRating(relatedClue.getDifficulty());
    }
}