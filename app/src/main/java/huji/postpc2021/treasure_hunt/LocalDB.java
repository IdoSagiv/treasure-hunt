package huji.postpc2021.treasure_hunt;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import huji.postpc2021.treasure_hunt.Utils.Game;

public class LocalDB extends Application {
    private final Context context;
    private final FirebaseFirestore fireStore;

    public LocalDB(Context context) {
        this.context = context;
        this.fireStore = FirebaseFirestore.getInstance();
    }

    public LiveData<ArrayList<Game>> getAvailableGames() {
        return null;
    }

    public LiveData<Game> getGameInfo(String gameId) {
        return null;
    }

    // TODO add AR

}

