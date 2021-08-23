package huji.postpc2021.treasure_hunt;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


public class CreatorHomeScreenFragment extends Fragment {
    private CreatorViewModel creatorViewModel;

    public CreatorHomeScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creator_home_screen, container, false);
        creatorViewModel = CreatorViewModel.getInstance();

        Button editGameButton = view.findViewById(R.id.buttonEditGame);
        Button newGameButton = view.findViewById(R.id.buttonNewGame);
        ImageView logoutButton = view.findViewById(R.id.buttonLogOut);

        // show the button only if there is a game to edit
        editGameButton.setVisibility(creatorViewModel.currentGame.getValue() == null ? View.GONE : View.VISIBLE);

        newGameButton.setOnClickListener(v ->
        {
            Navigation.findNavController(view)
                    .navigate(CreatorHomeScreenFragmentDirections.actionCreatorHomeScreenFragmentToCreatorNewGameFragment());

        });

        logoutButton.setOnClickListener(v -> logout(view));

        // on back pressed callback for this fragment
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                logout(view);
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

}
