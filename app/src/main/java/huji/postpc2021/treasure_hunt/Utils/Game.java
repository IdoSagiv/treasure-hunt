package huji.postpc2021.treasure_hunt.Utils;

import java.util.List;

enum GameStatus {
    editMode,
    waiting,
    running,
    finished
}

public class Game {
    String id;   // UUID
    String name;
    String code;  // Game PIN for users
    List<Player> players;
    List<Clue> clues;
    GameStatus status;
}
