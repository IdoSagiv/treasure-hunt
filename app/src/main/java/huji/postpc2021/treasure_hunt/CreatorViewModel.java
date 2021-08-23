package huji.postpc2021.treasure_hunt;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;

import huji.postpc2021.treasure_hunt.DataObjects.Clue;

public class CreatorViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Clue>> cluesMutableLiveData = new MutableLiveData<>();
    public LiveData<ArrayList<Clue>> cluesLiveData = cluesMutableLiveData;

//    public MutableLiveData<Game> gameLiveData = new MutableLiveData<>();

    private final HashMap<String, Clue> allClues = new HashMap<>();

    private static CreatorViewModel instance = null;

    public static CreatorViewModel getInstance() {
        if (instance == null) {
            instance = new CreatorViewModel();
        }
        return instance;
    }

    public void addClue(Clue clue) {
        allClues.put(clue.getId(), clue);
        cluesMutableLiveData.setValue(new ArrayList<>(allClues.values()));
    }

    public void removeClue(String clueId) {
        if (allClues.containsKey(clueId)) {
            allClues.remove(clueId);
            cluesMutableLiveData.setValue(new ArrayList<>(allClues.values()));
        }
    }

    public void editClue(String clueId, String newDescription) {
        if (allClues.containsKey(clueId)) {
            allClues.get(clueId).setDescription(newDescription);
            cluesMutableLiveData.setValue(new ArrayList<>(allClues.values()));
        }
    }

    public ArrayList<Clue> getClues() {
        return new ArrayList<>(allClues.values());
    }

    public void deleteAllClues() {
        this.allClues.clear();
        cluesMutableLiveData.setValue(new ArrayList<>(allClues.values()));
    }
}
