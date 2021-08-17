package huji.postpc2021.treasure_hunt;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ParticipantsListItemViewHolder extends RecyclerView.ViewHolder {
    View view;
    TextView ParticipantNicknameTextView;


    public ParticipantsListItemViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
        this.ParticipantNicknameTextView = itemView.findViewById(R.id.textViewParticipantNickname);
    }
}
