package huji.postpc2021.treasure_hunt.Utils.DataObjects;

import java.util.UUID;

public class Player {
    private String id;  // For our use in DB
    private String nickname;
    private int score;
    private String gameId;
    private String currentClueId;

    public Player(String nickname, String gameId) {
        this.id = UUID.randomUUID().toString();
        this.nickname = nickname;
        this.gameId = gameId;
        this.score = 0;
//        todo: initialize the currentClueId (using the game?)
        this.currentClueId = "0";
    }

    // empty constructor for firebase
    public Player() {
    }

    public String getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public int getScore() {
        return score;
    }

    public String getGameId() {
        return gameId;
    }

    public String getCurrentClueId() {
        return currentClueId;
    }
}