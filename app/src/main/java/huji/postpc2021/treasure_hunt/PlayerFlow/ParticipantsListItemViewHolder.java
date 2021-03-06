package huji.postpc2021.treasure_hunt.PlayerFlow;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import huji.postpc2021.treasure_hunt.R;

public class ParticipantsListItemViewHolder extends RecyclerView.ViewHolder {
    View view;
    TextView nicknameTextView;


    public ParticipantsListItemViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
        this.nicknameTextView = itemView.findViewById(R.id.textViewNicknameParticipantList);
    }
}
