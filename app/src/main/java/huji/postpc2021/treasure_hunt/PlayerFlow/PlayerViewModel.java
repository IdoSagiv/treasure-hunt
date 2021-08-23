package huji.postpc2021.treasure_hunt.PlayerFlow;

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
import huji.postpc2021.treasure_hunt.HomeScreenFragmentDirections;
import huji.postpc2021.treasure_hunt.Utils.LocalDB;
import huji.postpc2021.treasure_hunt.R;
import huji.postpc2021.treasure_hunt.TreasureHuntApp;

public class PlayerViewModel extends ViewModel {
    private final LocalDB db;
    public LiveData<Game> gameLiveData = new MutableLiveData<>();
    private Player currentPlayer;


    public MutableLiveData<String> gameCode = new MutableLiveData<>("");
    public MutableLiveData<String> nickName = new MutableLiveData<>("");

    private static PlayerViewModel instance = null;

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
        if (db.isAvailableGame(gameCode.getValue())) {
            gameLiveData = db.getGameFromCode(gameCode.getValue());

            Navigation.findNavController(view)
                    .navigate(HomeScreenFragmentDirections
                            .actionHomeScreenToEnterGame(gameCode.getValue()));
        } else {
            ((EditText) view.findViewById(R.id.editTextEnterCodeGame)).setError("invalid code");
        }
    }


    public void clickChooseNickName(View view) {
        Game game = gameLiveData.getValue();
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

        Navigation.findNavController(view)
                .navigate(R.id.action_enterGame_to_waitForGame);
    }

    public String currentPlayerId() {
        if(currentPlayer==null){
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
        Navigation.findNavController(view).navigate(R.id.action_playerGameFragment_to_homeScreenFragment);
    }

    public void leaveGameFromGameOverScreen(View view) {
        resetGameData();
        Navigation.findNavController(view).navigate(R.id.action_playerGameOverFragment_to_homeScreenFragment);
    }


    private void resetGameData() {
        gameLiveData.getValue().removePlayer(currentPlayer.getId());
        currentPlayer = null;
        gameLiveData = new MediatorLiveData<>();
        gameCode.setValue("");
        nickName.setValue("");
    }
}