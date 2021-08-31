package huji.postpc2021.treasure_hunt.Utils;

import android.util.Patterns;
import android.widget.EditText;

public class UtilsFunctions {
    public static boolean validateEmail(EditText emailEditText) {
        String emailInput = emailEditText.getText().toString().trim();

        if (emailInput.isEmpty()) {
            emailEditText.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            emailEditText.setError("Please enter a valid email address");
            return false;
        } else {
            emailEditText.setError(null);
            return true;
        }
    }
}
