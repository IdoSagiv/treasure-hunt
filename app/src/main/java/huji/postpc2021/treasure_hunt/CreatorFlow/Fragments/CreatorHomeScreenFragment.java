package huji.postpc2021.treasure_hunt.CreatorFlow.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import huji.postpc2021.treasure_hunt.CreatorFlow.CreatorViewModel;
import huji.postpc2021.treasure_hunt.R;


public class CreatorHomeScreenFragment extends Fragment {
    private CreatorViewModel creatorViewModel;

    public CreatorHomeScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creator_home_screen, container, false);
        creatorViewModel = CreatorViewModel.getInstance();

        Button existingGameButton = view.findViewById(R.id.buttonExistingGame);
        Button newGameButton = view.findViewById(R.id.buttonNewGame);
        ImageView logoutButton = view.findViewById(R.id.buttonLogOut);

        // show the button only if there is an existing game
        existingGameButton.setVisibility(creatorViewModel.currentGame.getValue() == null ? View.GONE : View.VISIBLE);

        existingGameButton.setOnClickListener(v -> creatorViewModel.enterExistingGame(view));

        newGameButton.setOnClickListener(v -> {
            if (creatorViewModel.currentGame.getValue() != null) {
                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE: {
                            newGamePopup(view);
                            break;
                        }
                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("There is an active existing game that will be deleted, do you want to continue?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener)
                        .show();
            } else {
                newGamePopup(view);
            }
        });

        logoutButton.setOnClickListener(v -> logout(view));

        // on back pressed callback for this fragment
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(view)
                        .navigate(CreatorHomeScreenFragmentDirections.actionCreatorHomeScreenFragmentToHomeScreenFragment());
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);


        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void logout(View view) {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE: {
                    creatorViewModel.logoutCreator(view);
                    break;
                }
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show();
    }


    private void newGamePopup(View view) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(requireContext());
        builder.setTitle("New Game");

        final EditText input = new EditText(requireContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setHint("choose game name");
        builder.setView(input);

        builder.setPositiveButton("create", (dialog, which) -> creatorViewModel.enterNewGame(view, input.getText().toString()));
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        final android.app.AlertDialog dialog = builder.create();
        dialog.show();

        final Button positiveButton = dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
        positiveButton.setEnabled(!input.getText().toString().isEmpty());

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                positiveButton.setEnabled(!input.getText().toString().isEmpty());
            }
        });
    }


}
