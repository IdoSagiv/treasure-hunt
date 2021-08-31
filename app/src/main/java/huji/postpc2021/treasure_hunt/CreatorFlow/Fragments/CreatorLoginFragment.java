package huji.postpc2021.treasure_hunt.CreatorFlow.Fragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import huji.postpc2021.treasure_hunt.CreatorFlow.CreatorViewModel;
import huji.postpc2021.treasure_hunt.Utils.InputBoxDialog;
import huji.postpc2021.treasure_hunt.Utils.LocalDB;
import huji.postpc2021.treasure_hunt.R;
import huji.postpc2021.treasure_hunt.TreasureHuntApp;


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

        creatorRegisterButton.setOnClickListener(v ->
                Navigation.findNavController(view)
                        .navigate(CreatorLoginFragmentDirections.actionCreatorLoginToCreatorRegister()));

        creatorLoginButton.setOnClickListener(v ->
                db.auth.signInWithEmailAndPassword(emailEditText.getText().toString(),
                        passwordEditText.getText().toString()).addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("CreatorLoginFragment", "signInWithEmail:success");
                        creatorViewModel.loginCreator(view);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("CreatorLoginFragment", "signInWithEmail:failure", task.getException());
                        Toast.makeText(requireContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                }));

        forgotPassButton.setOnClickListener(v -> {
            InputBoxDialog dialog = new InputBoxDialog(requireActivity());
            dialog.setTitle("Enter your email")
                    .setNegativeButton("cancel", null)
                    .setPositiveButton("send", v1 -> db.auth.sendPasswordResetEmail(dialog.getInput()))
                    .setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                    .setInputValidation(editText -> {
                        String emailInput = editText.getText().toString().trim();
                        if (emailInput.isEmpty()) {
                            editText.setError("Field can't be empty");
                            return false;
                        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                            editText.setError("Please enter a valid email address");
                            return false;
                        } else {
                            editText.setError(null);
                            return true;
                        }
                    })
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
}
