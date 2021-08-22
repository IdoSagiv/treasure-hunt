package huji.postpc2021.treasure_hunt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;

import huji.postpc2021.treasure_hunt.DataObjects.Clue;
import huji.postpc2021.treasure_hunt.DataObjects.Player;


public class LocationListAdapter  extends RecyclerView.Adapter<CreatorLocationListItemHolder>  {
    private final ArrayList<Clue> clues = new ArrayList<>();

    public void setItems(Collection <Clue> clues) {
        this.clues.clear();
        this.clues.addAll(clues);

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CreatorLocationListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.creator_one_location_row, parent, false);
        return new CreatorLocationListItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CreatorLocationListItemHolder holder, int position) {
        Clue Clue = clues.get(position);
        holder.hintTextView.setText(Clue.getDescription());
    }

    @Override
    public int getItemCount() {
        return this.clues.size();
    }
}
