package huji.postpc2021.treasure_hunt.Utils;

import java.util.HashMap;
import java.util.UUID;

import huji.postpc2021.treasure_hunt.TreasureHuntApp;

public class Game {
    private String id;   // UUID
    private String name;
    private String code;  // Game PIN for users
    private HashMap<String, Player> players;
    private HashMap<String, Clue> clues;
    private GameStatus status;


    public Game(String name) {
        this.id = UUID.randomUUID().toString();
        this.code = UUID.randomUUID().toString();
        this.name = name;
        this.players = new HashMap<>();
        this.clues = new HashMap<>();
        this.status = GameStatus.editMode;
    }

    /**
     * empty constructor for fireBase
     */
    public Game() {
    }

    public GameStatus getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    public void changeStatus(GameStatus newStatus) {
        status = newStatus;
        TreasureHuntApp.getInstance().getDb().updateGame(this);
    }

    public void addPlayer(Player newPlayer) {
        players.put(newPlayer.id, newPlayer);
        TreasureHuntApp.getInstance().getDb().updateGame(this);
    }

    public void removePlayer(String playerId) {
        if (players.containsKey(playerId)) {
            players.remove(playerId);
            TreasureHuntApp.getInstance().getDb().updateGame(this);
        }
    }

    public void addClue(Clue newClue) {
        clues.put(newClue.id, newClue);
        TreasureHuntApp.getInstance().getDb().updateGame(this);
    }

    public void removeClue(String clueId) {
        if (clues.containsKey(clueId)) {
            clues.remove(clueId);
            TreasureHuntApp.getInstance().getDb().updateGame(this);
        }
    }
}
