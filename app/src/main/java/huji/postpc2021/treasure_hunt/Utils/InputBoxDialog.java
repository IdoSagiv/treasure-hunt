package huji.postpc2021.treasure_hunt.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import huji.postpc2021.treasure_hunt.R;

public class InputBoxDialog {
    private final Dialog dialog;

    public InputBoxDialog(Activity activity) {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_input_box_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }

    public InputBoxDialog setInputValidation(InputValidationFunction inputValidation) {
        EditText inputEditText = dialog.findViewById(R.id.editTextDialogInput);
        FloatingActionButton fabPos = dialog.findViewById(R.id.fabPositiveButton);
        fabPos.setEnabled(false);
        inputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                fabPos.setEnabled(inputValidation.isValid(inputEditText));
            }
        });
        return this;
    }

    public InputBoxDialog setInputType(int type) {
        EditText inputEditText = dialog.findViewById(R.id.editTextDialogInput);
        inputEditText.setInputType(type);
        return this;
    }

    public InputBoxDialog setPositiveButton(String text, View.OnClickListener action) {
        FloatingActionButton fabPos = dialog.findViewById(R.id.fabPositiveButton);
        TextView posTextView = dialog.findViewById(R.id.textViewPositiveButtonText);
        fabPos.setOnClickListener(v -> {
            if (action != null) {
                action.onClick(v);
            }
            dialog.dismiss();
        });
        posTextView.setText(text);
        return this;
    }

    public InputBoxDialog setNegativeButton(String text, View.OnClickListener action) {
        FloatingActionButton fabPos = dialog.findViewById(R.id.fabNegativeButton);
        TextView posTextView = dialog.findViewById(R.id.textViewNegativeButtonText);
        fabPos.setOnClickListener(v -> {
            if (action != null) {
                action.onClick(v);
            }
            dialog.dismiss();
        });
        posTextView.setText(text);
        return this;
    }

    public InputBoxDialog setTitle(String title) {
        TextView titleTextView = dialog.findViewById(R.id.textViewDialogTitle);
        titleTextView.setText(title);
        return this;
    }

    public String getInput() {
        EditText inputEditText = dialog.findViewById(R.id.editTextDialogInput);
        return inputEditText.getText().toString();
    }

    public void show() {
        dialog.show();
    }

}

