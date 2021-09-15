package huji.postpc2021.treasure_hunt.PlayerFlow;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import huji.postpc2021.treasure_hunt.Utils.DataObjects.Clue;
import huji.postpc2021.treasure_hunt.Utils.DataObjects.Game;
import huji.postpc2021.treasure_hunt.Utils.DataObjects.Player;
import huji.postpc2021.treasure_hunt.Utils.LocalDB;
import huji.postpc2021.treasure_hunt.R;
import huji.postpc2021.treasure_hunt.TreasureHuntApp;

public class PlayerViewModel extends ViewModel {
    private static final double OPEN_CAMERA_THRESHOLD_DISTANCE = 10d;
    private static final double SHOW_AR_THRESHOLD_DISTANCE = 3d;
    private static PlayerViewModel instance = null;
    private final LocalDB db;
    public LiveData<Game> gameLiveData = new MutableLiveData<>();
    private String currentPlayerId;

    public final MutableLiveData<String> gameCode = new MutableLiveData<>("");
    public final MutableLiveData<String> nickName = new MutableLiveData<>("");

    public static PlayerViewModel getInstance() {
        if (instance == null) {
            instance = new PlayerViewModel();
        }
        return instance;
    }

    private PlayerViewModel() {
        db = TreasureHuntApp.getInstance().getDb();
    }

    public void clickEnterGame(View view) {
        String code = gameCode.getValue().toUpperCase();
        if (db.isAvailableGame(code)) {
            gameLiveData = db.getGameFromCode(code);
            Navigation.findNavController(view).navigate(R.id.action_homeScreen_to_enterGame);
        } else {
            ((EditText) view.findViewById(R.id.editTextEnterCodeGame)).setError("invalid code");
        }
    }

    public void clickChooseNickName(View view) {
        Game game = gameLiveData.getValue();
        if (game == null) {
            Log.e("EnterGame", "null game value while trying to choose nickname");
            return;
        }
        HashMap<String, Player> players = game.getPlayers();
        if (players != null) {
            for (Player player : players.values()) {
                if (player.getNickname().equals(nickName.getValue())) {
                    ((EditText) view.findViewById(R.id.editTextEnterNickname)).setError("nickname is taken");
                    return;
                }
            }
        }

        Player player = new Player(nickName.getValue(), gameLiveData.getValue().getId());

        game.addPlayer(player);
        currentPlayerId = player.getId();

        Navigation.findNavController(view).navigate(R.id.action_enterGame_to_waitForGame);
    }

    public String currentPlayerId() {
        return currentPlayerId;
    }

    public void leaveGameFromWaitScreen(View view) {
        resetGameData();
        Navigation.findNavController(view).navigate(R.id.action_waitForGame_to_homeScreen);
    }

    public void leaveGameFromGameScreen(View view) {
        resetGameData();
        Navigation.findNavController(view).navigate(R.id.action_playerGame_to_homeScreen);
    }

    public void leaveGameFromGameOverScreen(View view) {
        resetGameData();
        Navigation.findNavController(view).navigate(R.id.action_playerGameOver_to_homeScreen);
    }

    private void resetGameData() {
        Game game = gameLiveData.getValue();
        if (game != null) {
            game.removePlayer(currentPlayerId);
        }
        currentPlayerId = null;
        gameLiveData = new MediatorLiveData<>();
        gameCode.setValue("");
        nickName.setValue("");
    }

    public String getCurrentClueHint() {
        Game game = gameLiveData.getValue();
        if (game == null) {
            Log.e("PlayerGame", "null game value while trying to get clue");
            return "";
        }

        Clue clue = game.getClueAt(game.getPlayer(currentPlayerId).getClueIndex());
        return clue != null ? clue.getDescription() : "";
    }

    public Collection<Clue> getHintsUntil(Integer clueIndex) {
        Game game = gameLiveData.getValue();
        if (game == null) {
            Log.e("PlayerGame", "null game value while trying to get clues");
            return new ArrayList<>();
        }

        return game.getCluesUntil(clueIndex);
    }


    public boolean isCloseEnoughToOpenCamera(GeoPoint userLocation) {
        Game game = gameLiveData.getValue();
        if (game == null) {
            Log.e("PlayerGame", "null game value while trying to get clues");
            return false;
        }

        Clue clue = game.getClueAt(game.getPlayer(currentPlayerId).getClueIndex());
        return clue.location().distanceToAsDouble(userLocation) < OPEN_CAMERA_THRESHOLD_DISTANCE;
    }

    public boolean isCloseEnoughToShowAr(GeoPoint userLocation) {
        Game game = gameLiveData.getValue();
        if (game == null) {
            Log.e("PlayerGame", "null game value while trying to get clues");
            return false;
        }

        Clue clue = game.getClueAt(game.getPlayer(currentPlayerId).getClueIndex());
        return clue.location().distanceToAsDouble(userLocation) < SHOW_AR_THRESHOLD_DISTANCE;
    }

    public void clueFound() {
        Game game = gameLiveData.getValue();
        if (game == null) {
            Log.e("PlayerGame", "null game value");
            return;
        }
        game.foundClueUpdates(currentPlayerId);
    }

    public void gameOver(View view) {
        Navigation.findNavController(view).navigate(R.id.action_playerGame_to_playerGameOver);
    }

    public void allCluesFound(View view) {
        Navigation.findNavController(view).navigate(R.id.action_arScreen_to_playerGameOver);
    }

    public void backToGameFromAr(View view){
        Navigation.findNavController(view).navigate(R.id.action_arScreen_to_playerGame);
    }

    public boolean isFinishedGame(String playerId) {
        Game game = gameLiveData.getValue();
        if (game == null) {
            Log.e("PlayerGame", "null game value");
            return false;
        }
        Player player = game.getPlayer(playerId);
        return player.getClueIndex() >= game.getClues().size();
    }
}