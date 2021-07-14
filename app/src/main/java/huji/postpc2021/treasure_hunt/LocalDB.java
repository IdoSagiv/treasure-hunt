package huji.postpc2021.treasure_hunt;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;

public class LocalDB extends Application {

    SharedPreferences sharedPref;
    Context context;

    public LocalDB(Context context) {
        this.context = context;
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public LiveData<ArrayList<Game>> getAvailableGames() {
        return null;
    }

    public LiveData<Game> getGameInfo(String gameId) {
        return null;
    }

}

