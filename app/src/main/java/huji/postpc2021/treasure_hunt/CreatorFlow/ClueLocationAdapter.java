package huji.postpc2021.treasure_hunt.CreatorFlow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import huji.postpc2021.treasure_hunt.Utils.DataObjects.Clue;
import huji.postpc2021.treasure_hunt.R;
import huji.postpc2021.treasure_hunt.Utils.OnClueClickCallback;

public class ClueLocationAdapter extends RecyclerView.Adapter<ClueLocationViewHolder> {
    private final ArrayList<Clue> clues = new ArrayList<>();
    public OnClueClickCallback goToMarkerBtnCallback = null;

    public void setItems(Collection<Clue> clues) {
        this.clues.clear();
        this.clues.addAll(clues);
        Collections.sort(this.clues, (c1, c2) -> Integer.compare(c1.getIndex(), c2.getIndex()));

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ClueLocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.clue_location_item, parent, false);
        return new ClueLocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClueLocationViewHolder holder, int position) {
        Clue clue = clues.get(position);
        holder.clueDescriptionTextView.setText(clue.getDescription());
        holder.clueIndexTextView.setText(Integer.toString(clue.getIndex()));
        holder.goToClueButton.setOnClickListener(v -> {
            if (goToMarkerBtnCallback != null) {
                goToMarkerBtnCallback.onClick(clue);
            }
        });
        // todo: set onClickListener to the button
    }

    @Override
    public int getItemCount() {
        return this.clues.size();
    }
}
