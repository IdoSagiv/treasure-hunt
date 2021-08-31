package huji.postpc2021.treasure_hunt.CreatorFlow;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import huji.postpc2021.treasure_hunt.R;

public class ClueLocationViewHolder extends RecyclerView.ViewHolder {
    View view;
    TextView clueDescriptionTextView;
    TextView clueIndexTextView;
    ImageView goToClueButton;
    ImageView dragHandleButton;

    public ClueLocationViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
        this.clueDescriptionTextView = itemView.findViewById(R.id.textViewClueDescription);
        this.clueIndexTextView = itemView.findViewById(R.id.textViewClueIndex);
        this.goToClueButton = itemView.findViewById(R.id.buttonGoToClueLocation);
        this.dragHandleButton = itemView.findViewById(R.id.buttonDragHandle);
    }
}
