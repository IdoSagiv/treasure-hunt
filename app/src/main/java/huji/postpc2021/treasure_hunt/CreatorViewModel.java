package huji.postpc2021.treasure_hunt;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.osmdroid.views.MapView;

import huji.postpc2021.treasure_hunt.DataObjects.Clue;
import huji.postpc2021.treasure_hunt.DataObjects.Game;

public class CreatorViewModel extends ViewModel {

    public final MutableLiveData<ArrayList<Clue>> cluesMutableLiveData = new MutableLiveData<>();
//    private LiveData<ArrayList<Clue>> cluesLiveData = cluesMutableLiveData;

//    public MutableLiveData<String> changeHintId = new MutableLiveData<>("");
//    public MutableLiveData<String> changeHintText = new MutableLiveData<>("");


//    public MutableLiveData<Game> gameLiveData = new MutableLiveData<>();

    private ArrayList<Clue> clues = new ArrayList<>();


    public LiveData<ArrayList<Clue>> getCluesLiveData()
    {
        return cluesMutableLiveData;
    }

    private static CreatorViewModel instance = null;

    public static CreatorViewModel getInstance() {
        if (instance == null) {
            instance = new CreatorViewModel();
        }
        return instance;
    }

    public void addclue(Clue c) {
        clues.add(c);
        cluesMutableLiveData.setValue(clues);

    }

    public ArrayList<Clue> getclues() {
        return clues;
    }


    public void removeClue(String id) {
        for (Clue clue1 : clues) {
            if (clue1.getId().equals(id)) {
                clues.remove(clue1);
                break;
            }
        }
        cluesMutableLiveData.setValue(clues);
//            todo: update SP
    }

//    public MapHandler setScreen(MapView mMapView) {
//        mapHandler = new MapHandler(mMapView, true, MapHandler.ViewerType.CreatorEdit);
//        mapHandler.showHints(this.clues);
//        return mapHandler;
//    }

    public void editHint(String id, String newDescription) {
        for (Clue clue1 : clues) {
            if (clue1.getId().equals(id)) {
                clue1.setDescription(newDescription);
                break;
            }
        }
        cluesMutableLiveData.setValue(clues);

    }

    public ArrayList<Clue> getClues() {

        return new ArrayList<>( this.clues);
    }

    public void deleteAllClues() {
        this.clues.clear();
        cluesMutableLiveData.setValue(clues);
    }


//    private CreatorViewModal() {
//        db = TreasureHuntApp.getInstance().getDb();
//    }


}
