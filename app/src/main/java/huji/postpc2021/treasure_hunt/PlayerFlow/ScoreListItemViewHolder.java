package huji.postpc2021.treasure_hunt.PlayerFlow;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import huji.postpc2021.treasure_hunt.R;

public class ScoreListItemViewHolder extends RecyclerView.ViewHolder {
    View view;
    TextView nicknameTextView;
    TextView scoreTextView;
    LinearLayout linearLayout;


    public ScoreListItemViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
        this.nicknameTextView = itemView.findViewById(R.id.textViewNicknameScoreList);
        this.scoreTextView = itemView.findViewById(R.id.textViewScoreScoreList);
        this.linearLayout = itemView.findViewById(R.id.linearLayoutScoreListItem);
    }
}
