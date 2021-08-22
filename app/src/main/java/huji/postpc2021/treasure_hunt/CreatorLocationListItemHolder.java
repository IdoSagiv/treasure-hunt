package huji.postpc2021.treasure_hunt;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CreatorLocationListItemHolder extends RecyclerView.ViewHolder {
    View view;
    TextView hintTextView;

    public CreatorLocationListItemHolder(@NonNull View itemView)
    {
        super(itemView);
        this.view = itemView;
        this.hintTextView = itemView.findViewById(R.id.textViewLocation);
    }

}
