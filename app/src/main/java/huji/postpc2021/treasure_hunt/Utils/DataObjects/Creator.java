package huji.postpc2021.treasure_hunt.Utils.DataObjects;

import huji.postpc2021.treasure_hunt.TreasureHuntApp;

public class Creator {
    private String id;
    private String gameId;

    public Creator() {
        // Required empty public constructor for FireBase
    }

    public Creator(String id) {
        this.id = id;
        this.gameId = null;
    }

    public String getId() {
        return id;
    }

    public String getGameId() {
        return gameId;
    }

    public void updateGameId(String gameId) {
        this.gameId = gameId;
        TreasureHuntApp.getInstance().getDb().upsertCreator(this);
    }
}
