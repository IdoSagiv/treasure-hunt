package huji.postpc2021.treasure_hunt;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.FirebaseAuthCredentialsProvider;

import java.util.ArrayList;
import java.util.HashMap;

import huji.postpc2021.treasure_hunt.DataObjects.Creator;
import huji.postpc2021.treasure_hunt.DataObjects.Game;
import huji.postpc2021.treasure_hunt.DataObjects.GameStatus;

public class LocalDB {
    private final static String GAMES_FB_COLLECTION = "Games";
    private final static String CREATORS_FB_COLLECTION = "Creators";

    private final Context context; // todo: need?
    private final FirebaseFirestore fireStore;

    private final MutableLiveData<ArrayList<Game>> availableGamesMutableLD = new MutableLiveData<>();
    private final LiveData<ArrayList<Game>> availableGamesLD = availableGamesMutableLD;

    private HashMap<String, Game> allGames = new HashMap<>(); // todo: need?

    private HashMap<String, Creator> allCreators = new HashMap<>(); // todo: need?

    public FirebaseAuth auth;
    private FirebaseUser currentFbUser;

    public LocalDB(Context context) {
        this.context = context;
        this.fireStore = FirebaseFirestore.getInstance();
        this.auth = FirebaseAuth.getInstance();

        gamesListener();
        creatorsListener();
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

    private void creatorsListener() {
        fireStore.collection(CREATORS_FB_COLLECTION).addSnapshotListener((value, error) -> {
            allCreators.clear();
            if (error != null) {
                // todo: error
            } else if (value == null) {
                // todo: collection deleted -> error?
            } else {
                for (DocumentSnapshot creatorDoc : value.getDocuments()) {
                    Creator creator = creatorDoc.toObject(Creator.class);
                    allCreators.put(creator.getId(), creator);
                }
            }
        });
    }

    public LiveData<ArrayList<Game>> getAvailableGames() {
        return availableGamesLD;
    }

    public LiveData<Game> getGameInfo(String gameId) {
        if (!allGames.containsKey(gameId)) {
            return null;
        }

        MutableLiveData<Game> gameLD = new MutableLiveData<>(allGames.get(gameId));

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

    public LiveData<Game> getGameFromCode(String gameCode) {
        for (Game game : allGames.values()) {
            if (game.getCode().equals(gameCode)) {
                return getGameInfo(game.getId());
            }
        }
        return null;
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

    public void addCreator(String creatorId) {
        Creator newCreator = new Creator(creatorId);
        fireStore.collection(CREATORS_FB_COLLECTION).document(creatorId).set(newCreator);
    }

    public LiveData<Creator> getCreator(String creatorId) {
        if (!allCreators.containsKey(creatorId)) {
            return null;
        }

        MutableLiveData<Creator> creatorLD = new MutableLiveData<>(allCreators.get(creatorId));

        fireStore.collection(CREATORS_FB_COLLECTION).document(creatorId).addSnapshotListener((value, error) -> {
            if (error != null) {
                // todo: error
            } else if (value == null) {
                // todo: collection deleted -> error?
            } else {
                Creator c = value.toObject(Creator.class);
                creatorLD.setValue(c);
            }
        });

        return creatorLD;
    }

    // TODO add AR

}