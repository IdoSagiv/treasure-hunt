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
    public LiveData<Game> currentGame = new MutableLiveData<>(null);
    private LiveData<Creator> currentCreator = new MutableLiveData<>();
    private MutableLiveData<HashMap<String, Clue>> cluesMutableLiveData = new MutableLiveData<>();
    public LiveData<HashMap<String, Clue>> cluesLiveData = cluesMutableLiveData;

//    public MutableLiveData<Game> gameLiveData = new MutableLiveData<>();

    private static CreatorViewModel instance = null;

    public static CreatorViewModel getInstance() {
        if (instance == null) {
            instance = new CreatorViewModel();
        }
        return instance;
    }

    public String addClue(GeoPoint p) {
        int index = currentGame.getValue().getClues().size() + 1;
        Clue clue = new Clue("", index,
                new com.google.firebase.firestore.GeoPoint(p.getLatitude(), p.getLongitude()));
        Game game = currentGame.getValue();
        game.upsertClue(clue);
        cluesMutableLiveData.setValue(new HashMap<>(game.getClues()));

        return clue.getId();
    }

    public void removeClue(String clueId) {
        Game game = currentGame.getValue();
        game.removeClue(clueId);
        cluesMutableLiveData.setValue(new HashMap<>(game.getClues()));
    }

    public void editClue(Clue clue) {
        Game game = currentGame.getValue();
        game.upsertClue(clue);
        cluesMutableLiveData.setValue(new HashMap<>(game.getClues()));
    }

    public void registerNewUSer(View view) {
        LocalDB db = TreasureHuntApp.getInstance().getDb();
        // add user
        FirebaseUser user = db.auth.getCurrentUser();
        db.addCreator(user.getUid());

        // update UI
        Navigation.findNavController(view).navigate(R.id.action_creatorRegisterFragment_to_creatorHomeScreenFragment);
    }

    public void loginCreator(View view) {
        LocalDB db = TreasureHuntApp.getInstance().getDb();
        currentCreator = db.getCreator(db.auth.getCurrentUser().getUid());

        loadGame(currentCreator.getValue().getGameId());

        Navigation.findNavController(view).navigate(R.id.action_creatorLoginFragment_to_creatorHomeScreenFragment);
    }

    private void loadGame(String gameId) {
        LocalDB db = TreasureHuntApp.getInstance().getDb();
        currentGame = db.getGameInfo(gameId);
        if (currentGame.getValue() != null) {
            cluesMutableLiveData.setValue(currentGame.getValue().getClues());
        }
    }

    private void resetAllLiveData() {
        currentCreator = new MutableLiveData<>();
        currentGame = new MutableLiveData<>(null);
        cluesMutableLiveData = new MutableLiveData<>();
    }

    public void logoutCreator(View view) {
        LocalDB db = TreasureHuntApp.getInstance().getDb();
        db.auth.signOut();
        resetAllLiveData();

        Navigation.findNavController(view).navigate(R.id.action_creatorHomeScreenFragment_to_creatorLoginFragment);
    }

    public void enterNewGame(View view, String gameName) {
        LocalDB db = TreasureHuntApp.getInstance().getDb();

        if (currentGame.getValue() != null) {
            db.deleteGame(currentGame.getValue().getId());
        }

        Game game = new Game(gameName);
        db.upsertGame(game);

        loadGame(game.getId());
        currentCreator.getValue().updateGameId(game.getId());

        Navigation.findNavController(view).navigate(R.id.action_creatorHomeScreenFragment_to_creatorEditGameFragment);
    }

    public void enterExistingGame(View view) {
        Game game = currentGame.getValue();
        if (game == null) {
            Log.e("CreatorMainScreen", "null game value while trying to enter an existing game");
            return;
        }

        switch (game.getStatus()) {
            case editMode: {
                Navigation.findNavController(view).navigate(R.id.action_creatorHomeScreenFragment_to_creatorEditGameFragment);
                break;
            }
            case waiting: {
                Navigation.findNavController(view).navigate(R.id.action_creatorHomeScreenFragment_to_creatorDoneEditGameFragment);
                break;
            }
            case running: {
                Navigation.findNavController(view).navigate(R.id.action_creatorHomeScreenFragment_to_creatorInPlayFragment);
                break;
            }
            case finished: {
                // todo: navigate to the finished game fragment
                break;
            }
        }
    }

    public void updateCurrentGameName(String newName) {
        if (currentGame.getValue() != null) {
            currentGame.getValue().updateName(newName);
        }
    }

    public boolean launchGame(View view) {
        if (currentGame.getValue() != null && currentGame.getValue().getClues().size() >= 3) {
            currentGame.getValue().changeStatus(GameStatus.waiting);
            Navigation.findNavController(view).navigate(R.id.action_creatorEditGameFragment_to_creatorDoneEditGameFragment);
            return true;
        }
        return false;
    }

}
