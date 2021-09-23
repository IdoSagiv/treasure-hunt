package huji.postpc2021.treasure_hunt.CreatorFlow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;

import huji.postpc2021.treasure_hunt.ReorderCluesCallback;
import huji.postpc2021.treasure_hunt.Utils.DataObjects.Clue;
import huji.postpc2021.treasure_hunt.R;
import huji.postpc2021.treasure_hunt.Utils.OnViewHolderClickCallback;

public class ClueLocationAdapter extends RecyclerView.Adapter<ClueLocationViewHolder> {
    private final ArrayList<Clue> clues = new ArrayList<>();
    private final Context context;
    public OnClueClickCallback goToMarkerBtnCallback = null;
    public OnClueClickCallback onDeleteCallback = null;
    public OnViewHolderClickCallback startDragCallback = null;
    public ReorderCluesCallback reorderClue = null;

    public ClueLocationAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setItems(Collection<Clue> clues) {
        this.clues.clear();
        this.clues.addAll(clues);
        this.clues.sort((c1, c2) -> Integer.compare(c1.getIndex(), c2.getIndex()));

        notifyDataSetChanged();
    }

    @SuppressLint("ClickableViewAccessibility")
    @NonNull
    @Override
    public ClueLocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.clue_location_item, parent, false);
        ClueLocationViewHolder viewHolder = new ClueLocationViewHolder(view);
        viewHolder.dragHandleButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN && startDragCallback != null) {
                startDragCallback.onClick(viewHolder);
            }
            return true;
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ClueLocationViewHolder holder, int position) {
        Clue clue = clues.get(position);
        holder.clueDescriptionTextView.setText(clue.getDescription());
        holder.goToClueButton.setOnClickListener(v -> {
            if (goToMarkerBtnCallback != null) {
                goToMarkerBtnCallback.onClick(clue);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.clues.size();
    }

    public void deleteItem(int position) {
        Clue toDelete = clues.get(position);
        if (onDeleteCallback != null) {
            onDeleteCallback.onClick(toDelete);
        }
    }

    public Context getContext() {
        return context;
    }

    public void moveItem(int from, int to) {
        if (reorderClue != null) {
            Clue movedClue = clues.get(from);
            reorderClue.moveClue(movedClue.getId(), to);
        }
    }
}
