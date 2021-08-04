package huji.postpc2021.treasure_hunt.DataObjects;

import java.util.UUID;

public class Player {
    String id;  // For our use in DB
    String nickname;
    int score;
    String gameId;
    String currentClueId;

    public Player(String nickname, String gameId) {
        this.id = UUID.randomUUID().toString();
        this.nickname = nickname;
        this.gameId = gameId;
        this.score = 0;
//        todo: initialize the currentClueId (using the game?)
    }

    public String getNickname() {
        return nickname;
    }
}
