package huji.postpc2021.treasure_hunt;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

import huji.postpc2021.treasure_hunt.DataObjects.Game;
import huji.postpc2021.treasure_hunt.DataObjects.GameStatus;

public class LocalDB {
    private final static String GAMES_FB_COLLECTION = "Games";

    private final Context context; // todo: need?
    private final FirebaseFirestore fireStore;

    private final MutableLiveData<ArrayList<Game>> availableGamesMutableLD = new MutableLiveData<>();
    private final LiveData<ArrayList<Game>> availableGamesLD = availableGamesMutableLD;

    private HashMap<String, Game> allGames = new HashMap<>(); // todo: need?

    public LocalDB(Context context) {
        this.context = context;
        this.fireStore = FirebaseFirestore.getInstance();

        gamesListener();
    }

    private void gamesListener() {
        fireStore.collection(GAMES_FB_COLLECTION).addSnapshotListener((value, error) -> {
            allGames.clear();
            ArrayList<Game> availableGames = new ArrayList<>();
            if (error != null) {
                // todo: error
            } else if (value == null) {
                // todo: collection deleted -> error?
            } else {
                for (DocumentSnapshot gameDoc : value.getDocuments()) {
                    Game game = gameDoc.toObject(Game.class);
                    allGames.put(game.getId(), game);
                    if (game.getStatus() == GameStatus.waiting) {
                        availableGames.add(game);
                    }
                }
                availableGamesMutableLD.setValue(availableGames);
            }
        });
    }

    public LiveData<ArrayList<Game>> getAvailableGames() {
        return availableGamesLD;
    }

    public LiveData<Game> getGameInfo(String gameId) {
        MutableLiveData<Game> gameLD = new MutableLiveData<>();
        fireStore.collection(GAMES_FB_COLLECTION).document(gameId).addSnapshotListener((value, error) -> {
            if (error != null) {
                // todo: error
            } else if (value == null) {
                // todo: collection deleted -> error?
            } else {
                Game game = value.toObject(Game.class);
                gameLD.setValue(game);
            }
        });

        return gameLD;
    }

    /**
     * @param gameCode code to check
     * @return true in the given code is associated to a game that is waiting for players
     */
    public boolean isAvailableGame(String gameCode) {
        for (Game game : availableGamesMutableLD.getValue()) {
            if (game.getCode().equals(gameCode)) {
                return true;
            }
        }
        return false;
    }

    /**
     * add or change game in the db
     *
     * @param game game to add/change
     */
    public void updateGame(Game game) {
        fireStore.collection(GAMES_FB_COLLECTION).document(game.getId()).set(game);
    }

    // TODO add AR

}