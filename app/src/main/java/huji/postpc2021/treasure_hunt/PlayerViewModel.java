package huji.postpc2021.treasure_hunt;

import android.view.View;
import android.widget.EditText;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import huji.postpc2021.treasure_hunt.DataObjects.Game;
import huji.postpc2021.treasure_hunt.DataObjects.Player;

public class PlayerViewModel extends ViewModel {
    private final LocalDB db;
    public LiveData<Game> gameLiveData = new MutableLiveData<>();


    public MutableLiveData<String> gameCode = new MutableLiveData<>("");
    public MutableLiveData<String> nickName = new MutableLiveData<>("");


    public PlayerViewModel() {
        db = TreasureHuntApp.getInstance().getDb();
    }

    public void clickEnterGame(View view) {
        if (TreasureHuntApp.getInstance().getDb().isAvailableGame(gameCode.getValue())) {
            gameLiveData = db.getGameFromCode(gameCode.getValue());
            Game game = gameLiveData.getValue();

            Navigation.findNavController(view)
                    .navigate(HomeScreenFragmentDirections
                            .actionHomeScreenToEnterGame(gameCode.getValue()));
        } else {
            ((EditText) view.findViewById(R.id.editTextEnterCodeGame)).setError("invalid code");
        }
    }


    public void clickChooseNickName(View view) {
        if(gameLiveData.getValue().getPlayers()!=null){
            for (Player player : gameLiveData.getValue().getPlayers().values()) {
                if (player.getNickname().equals(nickName.getValue())) {
                    ((EditText) view.findViewById(R.id.editTextEnterNickname)).setError("nickname is taken");
                    return;
                }
            }
        }

        Player newPlayer = new Player(nickName.getValue(), gameLiveData.getValue().getId());

        gameLiveData.getValue().addPlayer(newPlayer);

        Navigation.findNavController(view)
                .navigate(R.id.action_enterGame_to_waitForGame);
    }

}
