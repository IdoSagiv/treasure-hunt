package huji.postpc2021.treasure_hunt.CreatorFlow.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.util.regex.Pattern;

import huji.postpc2021.treasure_hunt.CreatorFlow.CreatorViewModel;
import huji.postpc2021.treasure_hunt.Utils.LocalDB;
import huji.postpc2021.treasure_hunt.R;
import huji.postpc2021.treasure_hunt.TreasureHuntApp;
import huji.postpc2021.treasure_hunt.Utils.UtilsFunctions;

public class CreatorRegisterFragment extends Fragment {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[!@#$%^&*()_])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 6 characters
                    "$");


    private EditText emailEditText, passEditText, rePassEditText;
    private CreatorViewModel creatorViewModel;


    public CreatorRegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creator_register, container, false);
        creatorViewModel = CreatorViewModel.getInstance();

        Button creatorSignInButton = view.findViewById(R.id.buttonSignIn);

        emailEditText = view.findViewById(R.id.editTextNewEmail);
        passEditText = view.findViewById(R.id.editTextNewPassword);
        rePassEditText = view.findViewById(R.id.editTextNewRePassword);

        creatorSignInButton.setOnClickListener(v ->
                Navigation.findNavController(view)
                        .navigate(CreatorRegisterFragmentDirections.actionCreatorRegisterToCreatorHomeScreen()));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button sighInBtn = view.findViewById(R.id.buttonSignIn);

        sighInBtn.setOnClickListener(v -> {
            if (!UtilsFunctions.validateEmail(emailEditText) | !validatePassword() | !validateRePassword()) {
                return;
            }

            LocalDB db = TreasureHuntApp.getInstance().getDb();

            db.auth.createUserWithEmailAndPassword(emailEditText.getText().toString(),
                    passEditText.getText().toString()).addOnCompleteListener(requireActivity(), task -> {
                if (task.isSuccessful()) {
                    Log.d("CreatorRegisterFragment", "createUserWithEmail:success");
                    creatorViewModel.registerNewUSer(view);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("CreatorRegisterFragment", "createUserWithEmail:failure", task.getException());
                    Toast.makeText(requireContext(), "Register failed", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private boolean validatePassword() {
        String passwordInput = passEditText.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            passEditText.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            passEditText.setError("Password should contain at least:\n" +
                    "6 characters\n" +
                    "1 digit\n" +
                    "1 one lower case letter\n" +
                    "1 upper case letter\n" +
                    "1 special character");
            return false;
        } else {
            passEditText.setError(null);
            return true;
        }
    }

    private boolean validateRePassword() {
        if (!passEditText.getText().toString().equals(rePassEditText.getText().toString())) {
            rePassEditText.setError("passwords does not match");
            return false;
        }
        return true;
    }
}
