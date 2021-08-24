package huji.postpc2021.treasure_hunt.Utils.DataObjects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
        TreasureHuntApp.getInstance().getDb().upsertGame(this);
    }

    public void addPlayer(Player newPlayer) {
        players.put(newPlayer.getId(), newPlayer);
        TreasureHuntApp.getInstance().getDb().upsertGame(this);
    }

    public void removePlayer(String playerId) {
        if (players.containsKey(playerId)) {
            players.remove(playerId);
            TreasureHuntApp.getInstance().getDb().upsertGame(this);
        }
    }

    public void upsertClue(Clue newClue) {
        clues.put(newClue.getId(), newClue);
        TreasureHuntApp.getInstance().getDb().upsertGame(this);
    }

    public void removeClue(String clueId) {
        if (clues.containsKey(clueId)) {
            clues.remove(clueId);

            List<Clue> sortedClues = new ArrayList<>(clues.values());
            Collections.sort(sortedClues, (c1, c2) -> Integer.compare(c1.getIndex(), c2.getIndex()));
            int i = 1;
            for (Clue clue : sortedClues) {
                clue.changeIndex(i);
                i++;
            }

            TreasureHuntApp.getInstance().getDb().upsertGame(this);
        }
    }

    public Clue getClue(String clueId) {
        if (clues.containsKey(clueId)) {
            return clues.get(clueId);
        }
        return null;
    }

    public HashMap<String, Clue> getClues() {
        return new HashMap<>(clues);
    }

    public HashMap<String, Player> getPlayers() {
        return new HashMap<>(players);
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
