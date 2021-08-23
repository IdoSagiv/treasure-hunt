package huji.postpc2021.treasure_hunt.CreatorFlow;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;

import huji.postpc2021.treasure_hunt.Utils.DataObjects.Clue;
import huji.postpc2021.treasure_hunt.Utils.DataObjects.Creator;
import huji.postpc2021.treasure_hunt.Utils.DataObjects.Game;
import huji.postpc2021.treasure_hunt.Utils.LocalDB;
import huji.postpc2021.treasure_hunt.R;
import huji.postpc2021.treasure_hunt.TreasureHuntApp;

public class CreatorViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Clue>> cluesMutableLiveData = new MutableLiveData<>();
    public LiveData<ArrayList<Clue>> cluesLiveData = cluesMutableLiveData;

//    public MutableLiveData<Game> gameLiveData = new MutableLiveData<>();

    private final HashMap<String, Clue> allClues = new HashMap<>();

    private static CreatorViewModel instance = null;

    private LiveData<Creator> currentCreator = new MutableLiveData<>();
    public LiveData<Game> currentGame = new MutableLiveData<>(null);

    public static CreatorViewModel getInstance() {
        if (instance == null) {
            instance = new CreatorViewModel();
        }
        return instance;
    }

    public void addClue(Clue clue) {
        allClues.put(clue.getId(), clue);
        cluesMutableLiveData.setValue(new ArrayList<>(allClues.values()));
    }

    public void removeClue(String clueId) {
        if (allClues.containsKey(clueId)) {
            allClues.remove(clueId);
            cluesMutableLiveData.setValue(new ArrayList<>(allClues.values()));
        }
    }

    public void editClue(String clueId, String newDescription) {
        if (allClues.containsKey(clueId)) {
            allClues.get(clueId).setDescription(newDescription);
            cluesMutableLiveData.setValue(new ArrayList<>(allClues.values()));
        }
    }

    public ArrayList<Clue> getClues() {
        return new ArrayList<>(allClues.values());
    }

    public void deleteAllClues() {
        this.allClues.clear();
        cluesMutableLiveData.setValue(new ArrayList<>(allClues.values()));
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

        currentGame = db.getGameInfo(currentCreator.getValue().getGameId());

        Navigation.findNavController(view).navigate(R.id.action_creatorLoginFragment_to_creatorHomeScreenFragment);
    }

    public void logoutCreator(View view) {
        LocalDB db = TreasureHuntApp.getInstance().getDb();
        db.auth.signOut();
        currentCreator = new MutableLiveData<>();
        currentGame = new MutableLiveData<>(null);

        Navigation.findNavController(view).navigate(R.id.action_creatorHomeScreenFragment_to_creatorLoginFragment);
    }

    public void enterExistingGame(View view) {
        Game game = currentGame.getValue();
        if (game == null) {
            Log.e("CreatorMainScreen", "null game value while trying to enter an existing game");
            return;
        }

        switch (game.getStatus()) {
            case editMode: {
                Navigation.findNavController(view).navigate(R.id.action_creatorHomeScreenFragment_to_creatorNewGameFragment);
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

}
