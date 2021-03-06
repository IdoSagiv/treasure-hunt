package huji.postpc2021.treasure_hunt.CreatorFlow.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collection;

import huji.postpc2021.treasure_hunt.CreatorFlow.CreatorViewModel;
import huji.postpc2021.treasure_hunt.PlayerFlow.ParticipantsListAdapter;
import huji.postpc2021.treasure_hunt.R;
import huji.postpc2021.treasure_hunt.Utils.DataObjects.Player;

public class CreatorDoneEditGameFragment extends Fragment {
    private CreatorViewModel creatorViewModel;

    public CreatorDoneEditGameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creator_done_editing, container, false);
        creatorViewModel = CreatorViewModel.getInstance();

        // find views
        TextView gameCodeTextView = view.findViewById(R.id.textViewGameCodeDoneEditingScreen);
        TextView gameNameTextView = view.findViewById(R.id.textViewGameNameDoneEditing);
        RecyclerView participantsListRecyclerView = view.findViewById(R.id.recyclerViewParticipantsCreatorWait);
        Button deleteGameButton = view.findViewById(R.id.buttonDeleteGameDoneEditingScreen);
        Button startGameButton = view.findViewById(R.id.buttonStartGameDoneEditingScreen);
        ImageView shareGameCodeButton = view.findViewById(R.id.buttonShareGameCode);
        TextView emptyRecyclerTextView = view.findViewById(R.id.textViewEmptyRecycler);

        // init views
        startGameButton.setEnabled(false);
        participantsListRecyclerView.setVisibility(View.GONE);
        emptyRecyclerTextView.setVisibility(View.VISIBLE);

        // participants recyclerView
        ParticipantsListAdapter participantsListAdapter = new ParticipantsListAdapter();
        participantsListRecyclerView.setAdapter(participantsListAdapter);
        participantsListRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(),
                RecyclerView.VERTICAL, false));

        // observe the game
        creatorViewModel.currentGame.observe(getViewLifecycleOwner(), game -> {
            if (game != null) {
                gameCodeTextView.setText(game.getCode());
                gameNameTextView.setText(game.getName());
                Collection<Player> players = game.getPlayers().values();
                participantsListAdapter.setItems(players);
                if (players.size() > 0) {
                    participantsListRecyclerView.setVisibility(View.VISIBLE);
                    emptyRecyclerTextView.setVisibility(View.GONE);
                    startGameButton.setEnabled(true);
                } else {
                    participantsListRecyclerView.setVisibility(View.GONE);
                    emptyRecyclerTextView.setVisibility(View.VISIBLE);
                    startGameButton.setEnabled(false);
                }
            }
        });

        // buttons listeners
        deleteGameButton.setOnClickListener(v -> {
            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE: {
                        creatorViewModel.deleteGameFromDoneEditScreen(view);
                        break;
                    }
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setMessage("Are you sure you want to delete the game?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener)
                    .show();
        });

        startGameButton.setOnClickListener(v -> creatorViewModel.startGame(view));

        shareGameCodeButton.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, creatorViewModel.getShareMsg());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // on back pressed callback for this fragment
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                creatorViewModel.leaveDoneEditScreen(view);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }
}
