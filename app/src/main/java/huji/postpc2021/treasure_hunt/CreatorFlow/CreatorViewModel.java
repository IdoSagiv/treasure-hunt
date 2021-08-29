package huji.postpc2021.treasure_hunt.CreatorFlow;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseUser;

import org.osmdroid.util.GeoPoint;

import java.util.HashMap;

import huji.postpc2021.treasure_hunt.Utils.DataObjects.Clue;
import huji.postpc2021.treasure_hunt.Utils.DataObjects.Creator;
import huji.postpc2021.treasure_hunt.Utils.DataObjects.Game;
import huji.postpc2021.treasure_hunt.Utils.DataObjects.GameStatus;
import huji.postpc2021.treasure_hunt.Utils.LocalDB;
import huji.postpc2021.treasure_hunt.R;
import huji.postpc2021.treasure_hunt.TreasureHuntApp;

public class CreatorViewModel extends ViewModel {
    private static final int MIN_NUM_OF_CLUES = 3;

    private static CreatorViewModel instance = null;
    private final LocalDB db;
    public LiveData<Game> currentGame = new MutableLiveData<>(null);
    private LiveData<Creator> currentCreator = new MutableLiveData<>();
    private final MutableLiveData<HashMap<String, Clue>> cluesMutableLiveData = new MutableLiveData<>();
    public LiveData<HashMap<String, Clue>> cluesLiveData = cluesMutableLiveData;

    private CreatorViewModel() {
        this.db = TreasureHuntApp.getInstance().getDb();
    }

    public static CreatorViewModel getInstance() {
        if (instance == null) {
            instance = new CreatorViewModel();
        }
        return instance;
    }

    public String addClue(GeoPoint p) {
        Game game = currentGame.getValue();
        if (game == null) {
            Log.e("CreatorEditGame", "null game value while trying to add new clue");
            return "";
        }

        int index = game.getClues().size() + 1;
        Clue clue = new Clue("", index,
                new com.google.firebase.firestore.GeoPoint(p.getLatitude(), p.getLongitude()));
        game.upsertClue(clue);
        cluesMutableLiveData.setValue(new HashMap<>(game.getClues()));

        return clue.getId();
    }

    public void removeClue(String clueId) {
        Game game = currentGame.getValue();
        if (game == null) {
            Log.e("CreatorEditGame", "null game value while trying to remove clue");
            return;
        }
        game.removeClue(clueId);
        cluesMutableLiveData.setValue(new HashMap<>(game.getClues()));
    }

    public void editClue(Clue clue) {
        Game game = currentGame.getValue();
        if (game == null) {
            Log.e("CreatorEditGame", "null game value while trying to edit clue");
            return;
        }
        game.upsertClue(clue);
        cluesMutableLiveData.setValue(new HashMap<>(game.getClues()));
    }

    public void registerNewUSer(View view) {
        FirebaseUser user = db.auth.getCurrentUser();
        if (user == null) {
            Log.e("CreatorRegister", "null user value while trying to register");
            return;
        }
        db.addCreator(user.getUid());
        Navigation.findNavController(view).navigate(R.id.action_creatorRegister_to_creatorHomeScreen);
    }

    public void loginCreator(View view) {
        FirebaseUser user = db.auth.getCurrentUser();
        if (user == null) {
            Log.e("CreatorLogin", "null user value while trying to login");
            return;
        }
        currentCreator = db.getCreator(user.getUid());
        loadGame(currentCreator.getValue().getGameId());
        Navigation.findNavController(view).navigate(R.id.action_creatorLogin_to_creatorHomeScreen);
    }

    private void loadGame(String gameId) {
        currentGame = db.getGameInfo(gameId);
        if (currentGame.getValue() == null) {
            Log.e("CreatorMAinScreen", "null game value while trying to load game");
            return;
        }
        cluesMutableLiveData.setValue(currentGame.getValue().getClues());
    }

    private void resetAllLiveData() {
        currentCreator = new MutableLiveData<>();
        currentGame = new MutableLiveData<>(null);
        cluesMutableLiveData.setValue(null);
    }

    public void logoutCreator(View view) {
        db.auth.signOut();
        resetAllLiveData();

        Navigation.findNavController(view).navigate(R.id.action_creatorHomeScreen_to_creatorLogin);
    }

    public void enterNewGame(View view, String gameName) {
        if (currentGame.getValue() != null) {
            deleteCurrentGame();
        }

        Game game = new Game(gameName);
        db.upsertGame(game);
        currentCreator.getValue().updateGameId(game.getId());

        loadGame(game.getId());

        Navigation.findNavController(view).navigate(R.id.action_creatorHomeScreen_to_creatorEditGame);
    }

    public void enterExistingGame(View view) {
        Game game = currentGame.getValue();
        if (game == null) {
            Log.e("CreatorMainScreen", "null game value while trying to enter an existing game");
            return;
        }

        switch (game.getStatus()) {
            case editMode: {
                Navigation.findNavController(view).navigate(R.id.action_creatorHomeScreen_to_creatorEditGame);
                break;
            }
            case waiting: {
                Navigation.findNavController(view).navigate(R.id.action_creatorHomeScreenFragment_to_creatorDoneEditGameFragment);
                break;
            }
            case running: {
                Navigation.findNavController(view).navigate(R.id.action_creatorHomeScreen_to_creatorInPlay);
                break;
            }
            case finished: {
                // todo: navigate to the finished game fragment
                break;
            }
        }
    }

    public void updateCurrentGameName(String newName) {
        if (currentGame.getValue() == null) {
            Log.e("CreatorEditGame", "null game value while trying to edit game name");
            return;
        }
        currentGame.getValue().updateName(newName);
    }

    public boolean launchGame(View view) {
        if (currentGame.getValue() != null &&
                currentGame.getValue().getClues().size() >= MIN_NUM_OF_CLUES) {
            currentGame.getValue().changeStatus(GameStatus.waiting);
            Navigation.findNavController(view).navigate(R.id.action_creatorEditGame_to_creatorDoneEditGame);
            return true;
        }
        return false;
    }

    public void startGame(View view) {
        Game game = currentGame.getValue();
        if (game == null) {
            Log.e("CreatorDoneEditing", "null game value while trying to start the game");
            return;
        }
        game.changeStatus(GameStatus.running);
        Navigation.findNavController(view).navigate(R.id.action_creatorDoneEditGame_to_creatorInPlay);
    }

    public void leaveHomeScreen(View view) {
        Navigation.findNavController(view).navigate(R.id.action_creatorHomeScreen_to_homeScreen);
    }

    public void leaveDoneEditScreen(View view) {
        Navigation.findNavController(view).navigate(R.id.action_creatorDoneEditGame_to_creatorHomeScreen);
    }

    public void leaveInPlayScreen(View view) {
        Navigation.findNavController(view).navigate(R.id.action_creatorInPlay_to_creatorHomeScreen);
    }

    public void deleteGameFromEditScreen(View view) {
        deleteCurrentGame();
        Navigation.findNavController(view).navigate(R.id.action_creatorEditGame_to_creatorHomeScreen);
    }

    public void deleteGameFromDoneEditScreen(View view) {
        deleteCurrentGame();
        Navigation.findNavController(view).navigate(R.id.action_creatorDoneEditGame_to_creatorHomeScreen);
    }

    private void deleteCurrentGame() {
        Game game = currentGame.getValue();
        if (game == null) {
            Log.e("CreatorEdit/DoneEditing", "null game value while trying to delete game");
            return;
        }
        db.deleteGame(currentGame.getValue().getId());
        currentCreator.getValue().updateGameId(null);
        currentGame = new MutableLiveData<>(null);
        cluesMutableLiveData.setValue(null);
    }

    public void endGame(View view) {
        // todo
    }

    public String getShareMsg() {
        Game game = currentGame.getValue();
        if (game == null) {
            Log.e("CreatorDoneEditing", "null game value while trying to share game code");
            return "";
        }
        return "Hey!\n" +
                "Please join my Treasure Hunt game - '" + game.getName() + "'!\n" +
                "The code is " + game.getCode();
    }
}
