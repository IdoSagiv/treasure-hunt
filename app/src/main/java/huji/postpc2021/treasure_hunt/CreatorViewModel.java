package huji.postpc2021.treasure_hunt;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import org.osmdroid.views.MapView;
import huji.postpc2021.treasure_hunt.DataObjects.Clue;

public class CreatorViewModel extends ViewModel {

    public LiveData<Clue> cluesLiveData = new MutableLiveData<>();
    public MutableLiveData<String> deleteid = new MutableLiveData<>("");
    public MutableLiveData<String> changeHintId = new MutableLiveData<>("");
    public MutableLiveData<String> changeHintText = new MutableLiveData<>("");

    private ArrayList<Clue> clues = new ArrayList<Clue>();
    MapHandler mapHandler = null;


    private static CreatorViewModel instance = null;

    public static CreatorViewModel getInstance() {
        if (instance == null) {
            instance = new CreatorViewModel();
        }
        return instance;
    }

    public void addclue (Clue c)
    {
        clues.add(c);
        mapHandler.showHints(this.clues);
    }

    public ArrayList<Clue> getclues()
    {
        return clues;
    }

    public void removeClue() {
        for (Clue clue1 : clues) {
            if (clue1.getId().equals(deleteid.getValue())) {
                clues.remove(clue1);
                break;
            }
        }
        mapHandler.showHints(clues);
    }

    public MapHandler setScreen(MapView mMapView)
    {
        mapHandler = new MapHandler(mMapView, true, MapHandler.ViewerType.CreatorEdit);
        mapHandler.showHints(this.clues);
        return mapHandler;
    }
    public void changeHint()
    {
        for (Clue clue1 : clues) {
            if (clue1.getId().equals(changeHintId.getValue())) {
                clue1.setDescription(changeHintText.getValue());
                break;
            }
        }

    }

    public ArrayList<Clue> getClues(){return this.clues;}

    public void  deleteAllClues( )
    {
        this.clues.clear();
    }



//    private CreatorViewModal() {
//        db = TreasureHuntApp.getInstance().getDb();
//    }


}
