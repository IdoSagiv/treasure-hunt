package huji.postpc2021.treasure_hunt.Utils.DataObjects;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.UUID;

public class Clue {
    private String id;
    private String description;
    private double latitude;
    private double longitude;
    private int difficulty;

    private ArrayList<String> visitedPlayersId;
    private int index;
    // TODO AR

    public Clue() {
        // empty constructor for fireBase
    }

    public Clue(String description, int index, GeoPoint location) {
        this.id = UUID.randomUUID().toString();
        this.description = description;
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        this.visitedPlayersId = new ArrayList<>();
        this.index = index;
        this.difficulty = 1;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public ArrayList<String> getVisitedPlayersId() {
        return visitedPlayersId;
    }

    public void changeIndex(int newIndex) {
        this.index = newIndex;
    }

    public String getId() {
        return id;
    }

    public GeoPoint location() {
        return new GeoPoint(latitude, longitude);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    private void setVisitedPlayersId(ArrayList<String> visitedPlayersId) {
        this.visitedPlayersId = visitedPlayersId;
    }

    public void addVisitedPlayer(String playerId) {
        visitedPlayersId.add(playerId);
    }

    public int getIndex() {
        return index;
    }

    public void setLocation(GeoPoint location) {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }
}
