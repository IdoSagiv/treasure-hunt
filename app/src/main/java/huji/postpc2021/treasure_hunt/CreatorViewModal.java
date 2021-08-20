package huji.postpc2021.treasure_hunt;

import android.view.View;
import android.widget.EditText;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.HashMap;
import org.osmdroid.views.MapView;
import huji.postpc2021.treasure_hunt.DataObjects.Clue;
import huji.postpc2021.treasure_hunt.DataObjects.Game;

public class CreatorViewModal extends ViewModel {

    public LiveData<Clue> clueLiveData = new MutableLiveData<>();
    public MutableLiveData<String> deleteid = new MutableLiveData<>("");
    public MutableLiveData<String> changeHintId = new MutableLiveData<>("");
    public MutableLiveData<String> changeHintText = new MutableLiveData<>("");

    private ArrayList<Clue> clues = new ArrayList<Clue>();
    MapHandler mapHandler = null;


    private static CreatorViewModal instance = null;

    public static CreatorViewModal getInstance() {
        if (instance == null) {
            instance = new CreatorViewModal();
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
