package huji.postpc2021.treasure_hunt.PlayerFlow;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import java.util.HashMap;

import huji.postpc2021.treasure_hunt.Utils.DataObjects.Game;
import huji.postpc2021.treasure_hunt.Utils.DataObjects.Player;
import huji.postpc2021.treasure_hunt.Utils.LocalDB;
import huji.postpc2021.treasure_hunt.R;
import huji.postpc2021.treasure_hunt.TreasureHuntApp;

public class PlayerViewModel extends ViewModel {
    private static PlayerViewModel instance = null;
    private final LocalDB db;
    public LiveData<Game> gameLiveData = new MutableLiveData<>();
    private Player currentPlayer;

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

        currentPlayer = new Player(nickName.getValue(), gameLiveData.getValue().getId());

        game.addPlayer(currentPlayer);

        Navigation.findNavController(view).navigate(R.id.action_enterGame_to_waitForGame);
    }

    public String currentPlayerId() {
        if (currentPlayer == null) {
            return "";
        }
        return currentPlayer.getId();
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
            game.removePlayer(currentPlayer.getId());
        }
        currentPlayer = null;
        gameLiveData = new MediatorLiveData<>();
        gameCode.setValue("");
        nickName.setValue("");
    }
}