package huji.postpc2021.treasure_hunt;

import android.app.Application;

import huji.postpc2021.treasure_hunt.Utils.LocalDB;

public class TreasureHuntApp extends Application {
    private static TreasureHuntApp instance = null;
    private LocalDB localDdb;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        localDdb = new LocalDB(this);
    }

    public static TreasureHuntApp getInstance() {
        return instance;
    }

    public LocalDB getDb() {
        return localDdb;
    }
}
