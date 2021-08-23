package huji.postpc2021.treasure_hunt.DataObjects;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Clue {
    private final String id;
    private String description;
    private GeoPoint location;
    private List<Player> visitedPlayers;
    private int index;
    // TODO AR

    public Clue(String description, int index ,GeoPoint location) {
        this.id = UUID.randomUUID().toString();
        this.description = description;
        this.location = location;
        this.visitedPlayers = new ArrayList<>();
        this.index = index;
    }

    public void changeIndex(int newIndex) {
        this.index = newIndex;
    }

    public String getId() {
        return id;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public int getIndex() {
        return index;
    }
}
