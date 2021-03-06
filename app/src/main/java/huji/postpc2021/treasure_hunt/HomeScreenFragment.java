package huji.postpc2021.treasure_hunt;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import huji.postpc2021.treasure_hunt.PlayerFlow.PlayerViewModel;

public class HomeScreenFragment extends Fragment {

    public HomeScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_screen, container, false);
        PlayerViewModel playerViewModel = PlayerViewModel.getInstance();

        Button enterGameButton = view.findViewById(R.id.buttonEnterGame);
        EditText gameCodeEditText = view.findViewById(R.id.editTextEnterCodeGame);
        Button enterGameAsCreator = view.findViewById(R.id.buttonLoginAsCreator);

        gameCodeEditText.setText(playerViewModel.gameCode.getValue());
        enterGameButton.setEnabled(!gameCodeEditText.getText().toString().isEmpty());

        gameCodeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                playerViewModel.gameCode.setValue(gameCodeEditText.getText().toString());
                enterGameButton.setEnabled(!gameCodeEditText.getText().toString().isEmpty());
            }
        });

        // enter game button
        enterGameButton.setOnClickListener(v -> playerViewModel.clickEnterGame(view));

        enterGameAsCreator.setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.action_homeScreen_to_creatorLogin));


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // on back pressed callback for this fragment
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE: {
                            requireActivity().finish();
                            break;
                        }
                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setMessage("close app?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener)
                        .show();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }
}