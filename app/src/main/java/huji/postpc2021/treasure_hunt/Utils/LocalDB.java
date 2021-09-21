package huji.postpc2021.treasure_hunt.Utils;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.HashSet;

import huji.postpc2021.treasure_hunt.Utils.DataObjects.Creator;
import huji.postpc2021.treasure_hunt.Utils.DataObjects.Game;
import huji.postpc2021.treasure_hunt.Utils.DataObjects.GameStatus;

public class LocalDB {
    private final static String GAMES_FB_COLLECTION = "Games";
    private final static String CREATORS_FB_COLLECTION = "Creators";

    private final FirebaseFirestore fireStore;

    // set with the game codes of the available games
    private final HashSet<String> availableGamesCodes = new HashSet<>();

    private final HashMap<String, Game> allGames = new HashMap<>();

    private final HashMap<String, Creator> allCreators = new HashMap<>();

    public FirebaseAuth auth;

    public LocalDB() {
        this.fireStore = FirebaseFirestore.getInstance();
        this.auth = FirebaseAuth.getInstance();

        gamesListener();
        creatorsListener();
    }

    private void gamesListener() {
        fireStore.collection(GAMES_FB_COLLECTION).addSnapshotListener((value, error) -> {
            allGames.clear();
            availableGamesCodes.clear();
            if (error != null) {
                Log.e("LocalDB", "error occurred while trying to read the Games collection from FireStore");
            } else if (value == null) {
                Log.e("LocalDB", "Games collection dose not exists in FireStore while trying to read it");
            } else {
                for (DocumentSnapshot gameDoc : value.getDocuments()) {
                    Game game = gameDoc.toObject(Game.class);
                    if (game != null) {
                        allGames.put(game.getId(), game);
                        if (game.getStatus() == GameStatus.waiting) {
                            availableGamesCodes.add(game.getCode());
                        }
                    }
                }
            }
        });
    }

    private void creatorsListener() {
        fireStore.collection(CREATORS_FB_COLLECTION).addSnapshotListener((value, error) -> {
            allCreators.clear();
            if (error != null) {
                Log.e("LocalDB", "error occurred while trying to read the Creators collection from FireStore");
            } else if (value == null) {
                Log.e("LocalDB", "Creators collection dose not exists in FireStore while trying to read it");
            } else {
                for (DocumentSnapshot creatorDoc : value.getDocuments()) {
                    Creator creator = creatorDoc.toObject(Creator.class);
                    if (creator != null) {
                        allCreators.put(creator.getId(), creator);
                    }
                }
            }
        });
    }

    public LiveData<Game> getGameInfo(String gameId) {
        if (!allGames.containsKey(gameId)) {
            return new MutableLiveData<>(null);
        }

        MutableLiveData<Game> gameLD = new MutableLiveData<>(allGames.get(gameId));

        fireStore.collection(GAMES_FB_COLLECTION).document(gameId).addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.e("LocalDB", "error occurred while trying to read a games document from FireStore");
            } else if (value == null) {
                Log.e("LocalDB", "a game document dose not exists in FireStore while trying to read it");
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
        return new MutableLiveData<>(null);
    }

    /**
     * @param gameCode code to check
     * @return true in the given code is associated to a game that is waiting for players
     */
    public boolean isAvailableGame(String gameCode) {
        return availableGamesCodes.contains(gameCode);
    }

    /**
     * add or change game in the db
     *
     * @param game game to add/change
     */
    public void upsertGame(Game game) {
        allGames.put(game.getId(), game);
        fireStore.collection(GAMES_FB_COLLECTION).document(game.getId()).set(game);
    }

    public void deleteGame(String gameId) {
        fireStore.collection(GAMES_FB_COLLECTION).document(gameId).delete();
    }

    public void upsertCreator(Creator creator) {
        allCreators.put(creator.getId(), creator);
        fireStore.collection(CREATORS_FB_COLLECTION).document(creator.getId()).set(creator);
    }

    public void addCreator(String creatorId) {
        Creator newCreator = new Creator(creatorId);
        upsertCreator(newCreator);
    }

    public LiveData<Creator> getCreator(String creatorId) {
        if (!allCreators.containsKey(creatorId)) {
            return null;
        }

        MutableLiveData<Creator> creatorLD = new MutableLiveData<>(allCreators.get(creatorId));

        fireStore.collection(CREATORS_FB_COLLECTION).document(creatorId).addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.e("LocalDB", "error occurred while trying to read a creator document from FireStore");
            } else if (value == null) {
                Log.e("LocalDB", "a creator document dose not exists in FireStore while trying to read it");
            } else {
                Creator c = value.toObject(Creator.class);
                creatorLD.setValue(c);
            }
        });

        return creatorLD;
    }
}