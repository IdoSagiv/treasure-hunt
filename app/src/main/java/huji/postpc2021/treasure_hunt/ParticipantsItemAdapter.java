package huji.postpc2021.treasure_hunt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;

import huji.postpc2021.treasure_hunt.DataObjects.Player;

public class ParticipantsItemAdapter extends RecyclerView.Adapter<ParticipantItemViewHolder> {
    private final ArrayList<Player> players = new ArrayList<>();


    public void setItems(Collection<Player> players) {
        this.players.clear();
        this.players.addAll(players);

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ParticipantItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.participant_item, parent, false);
        return new ParticipantItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantItemViewHolder holder, int position) {
        Player player = players.get(position);
        holder.ParticipantNicknameTextView.setText(player.getNickname());
    }

    @Override
    public int getItemCount() {
        return players.size();
    }
}
