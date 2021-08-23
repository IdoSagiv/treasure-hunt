package huji.postpc2021.treasure_hunt.PlayerFlow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import huji.postpc2021.treasure_hunt.DataObjects.Player;
import huji.postpc2021.treasure_hunt.R;

public class ScoreListAdapter extends RecyclerView.Adapter<ScoreListItemViewHolder> {
    private final ArrayList<Player> players = new ArrayList<>();

    public void setItems(Collection<Player> players) {
        this.players.clear();
        this.players.addAll(players);

        // sort by score
        Collections.sort(this.players, (p1, p2) -> -1 * Integer.compare(p1.getScore(), p2.getScore()));
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ScoreListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.score_list_item, parent, false);
        return new ScoreListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreListItemViewHolder holder, int position) {
        Player player = players.get(position);
        holder.nicknameTextView.setText(player.getNickname());
        holder.scoreTextView.setText(Integer.toString(player.getScore()));
    }

    @Override
    public int getItemCount() {
        return players.size();
    }
}
