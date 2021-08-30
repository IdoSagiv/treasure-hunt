package huji.postpc2021.treasure_hunt.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import huji.postpc2021.treasure_hunt.R;

public class MessageBoxDialog {
    private final Dialog dialog;

    public MessageBoxDialog(Activity activity) {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.message_box_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public MessageBoxDialog setOkButton(String text, View.OnClickListener action) {
        FloatingActionButton fabPos = dialog.findViewById(R.id.fabOkButton);
        TextView posTextView = dialog.findViewById(R.id.textViewOkButtonText);
        fabPos.setOnClickListener(v -> {
            if (action != null) {
                action.onClick(v);
            }
            dialog.dismiss();
        });
        posTextView.setText(text);
        return this;
    }

    public MessageBoxDialog setTitle(String title) {
        TextView titleTextView = dialog.findViewById(R.id.textViewDialogTitle);
        titleTextView.setText(title);
        return this;
    }

    public MessageBoxDialog setMessage(String message) {
        TextView messageTextView = dialog.findViewById(R.id.textViewDialogMessage);
        messageTextView.setText(message);
        return this;
    }

    public void show() {
        dialog.show();
    }

}

