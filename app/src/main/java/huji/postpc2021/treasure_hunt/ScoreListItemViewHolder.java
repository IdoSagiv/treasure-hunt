package huji.postpc2021.treasure_hunt;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ScoreListItemViewHolder extends RecyclerView.ViewHolder {
    View view;
    TextView nicknameTextView;
    TextView scoreTextView;


    public ScoreListItemViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
        this.nicknameTextView = itemView.findViewById(R.id.textViewNicknameScoreList);
        this.scoreTextView = itemView.findViewById(R.id.textViewScoreScoreList);
    }
}
