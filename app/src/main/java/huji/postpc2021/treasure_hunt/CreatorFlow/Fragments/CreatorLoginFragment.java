package huji.postpc2021.treasure_hunt.CreatorFlow.Fragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import huji.postpc2021.treasure_hunt.CreatorFlow.CreatorViewModel;
import huji.postpc2021.treasure_hunt.Utils.InputBoxDialog;
import huji.postpc2021.treasure_hunt.Utils.LocalDB;
import huji.postpc2021.treasure_hunt.R;
import huji.postpc2021.treasure_hunt.TreasureHuntApp;
import huji.postpc2021.treasure_hunt.Utils.UtilsFunctions;


public class CreatorLoginFragment extends Fragment {
    private CreatorViewModel creatorViewModel;
    private LocalDB db;

    public CreatorLoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creator_login, container, false);
        creatorViewModel = CreatorViewModel.getInstance();
        db = TreasureHuntApp.getInstance().getDb();

        Button creatorRegisterButton = view.findViewById(R.id.buttonRegister);
        Button creatorLoginButton = view.findViewById(R.id.buttonLogin);
        EditText emailEditText = view.findViewById(R.id.editTextEmailLogin);
        EditText passwordEditText = view.findViewById(R.id.editTextPasswordLogin);
        TextView forgotPassButton = view.findViewById(R.id.textViewForgotPassword);

        creatorRegisterButton.setOnClickListener(v -> creatorViewModel.goToRegister(view));

        creatorLoginButton.setOnClickListener(v -> {
            if (UtilsFunctions.validateEmail(emailEditText) & validatePassword(passwordEditText)) {
                // disable button until get result
                creatorLoginButton.setEnabled(false);
                db.auth.signInWithEmailAndPassword(emailEditText.getText().toString(),
                        passwordEditText.getText().toString()).addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("CreatorLoginFragment", "signInWithEmail:success");
                        creatorViewModel.loginCreator(view);
                    } else {
                        Exception exc = task.getException();
                        if (exc instanceof FirebaseAuthInvalidUserException) {
                            Toast.makeText(requireContext(), "Unknown email", Toast.LENGTH_SHORT).show();
                        } else if (exc instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(requireContext(), "Invalid password", Toast.LENGTH_SHORT).show();
                        } else if (exc instanceof FirebaseNetworkException) {
                            Toast.makeText(requireContext(), "Network error", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                        Log.w("CreatorLoginFragment", "signInWithEmail:failure", exc);
                    }
                    // enable button after result came
                    creatorLoginButton.setEnabled(true);
                });
            }
        });

        forgotPassButton.setOnClickListener(v -> {
            InputBoxDialog dialog = new InputBoxDialog(requireActivity());
            dialog.setTitle("Enter your email")
                    .setNegativeButton("cancel", null)
                    .setPositiveButton("send", v1 -> db.auth.sendPasswordResetEmail(dialog.getInput()))
                    .setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                    .setInputValidation(UtilsFunctions::validateEmail)
                    .show();
        });

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser currentUser = db.auth.getCurrentUser();
        if (currentUser != null && !currentUser.isAnonymous()) {
            creatorViewModel.loginCreator(view);
        }

        // on back pressed callback for this fragment
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                creatorViewModel.leaveLoginScreen(view);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    private boolean validatePassword(EditText passEditText) {
        String passwordInput = passEditText.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            passEditText.setError("Field can't be empty");
            return false;
        } else {
            passEditText.setError(null);
            return true;
        }
    }
}
