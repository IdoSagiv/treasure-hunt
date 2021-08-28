package huji.postpc2021.treasure_hunt.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import huji.postpc2021.treasure_hunt.R;

public class PositiveNegativeDialog extends Dialog {
    private FloatingActionButton fabPos;
    private FloatingActionButton fabNeg;
    private TextView titleTextView;
    private TextView messageTextView;
    private TextView posTextView;
    private TextView negTextView;

    public PositiveNegativeDialog(Activity activity) {
        super(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_box_positive_negative_layout);
        fabPos = findViewById(R.id.fabPositive);
        posTextView = findViewById(R.id.textViewPositiveButtonText);
        fabNeg = findViewById(R.id.fabNegative);
        negTextView = findViewById(R.id.textViewNegativeButtonText);
        titleTextView = findViewById(R.id.textViewDialogTitle);
        messageTextView = findViewById(R.id.textViewDialogMessage);
    }

    public PositiveNegativeDialog setPositiveButton(String text, View.OnClickListener action) {
        fabPos.setOnClickListener(v -> {
            if (action != null) {
                action.onClick(v);
            }
            dismiss();
        });
        posTextView.setText(text);
        return this;
    }

    public PositiveNegativeDialog setNegativeButton(String text, View.OnClickListener action) {
        fabNeg.setOnClickListener(v -> {
            if (action != null) {
                action.onClick(v);
            }
            dismiss();
        });
        negTextView.setText(text);
        return this;
    }

    public PositiveNegativeDialog setTitleTextView(String title) {
        titleTextView.setText(title);
        return this;
    }

    public PositiveNegativeDialog setMessageTextView(String message) {
        messageTextView.setText(message);
        return this;
    }

}
