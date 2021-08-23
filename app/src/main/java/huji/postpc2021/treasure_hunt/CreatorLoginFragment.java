package huji.postpc2021.treasure_hunt;

import android.Manifest;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;


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

        creatorRegisterButton.setOnClickListener(v ->
                Navigation.findNavController(view)
                        .navigate(CreatorLoginFragmentDirections.actionCreatorLoginFragmentToCreatorRegisterFragment()));

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

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser currentUser = db.auth.getCurrentUser();
        if (currentUser != null && !currentUser.isAnonymous()) {
            creatorViewModel.loginCreator(view);
        }
    }
}
