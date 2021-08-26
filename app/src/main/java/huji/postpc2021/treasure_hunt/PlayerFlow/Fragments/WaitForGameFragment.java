package huji.postpc2021.treasure_hunt.PlayerFlow.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import huji.postpc2021.treasure_hunt.Utils.DataObjects.GameStatus;
import huji.postpc2021.treasure_hunt.PlayerFlow.ParticipantsListAdapter;
import huji.postpc2021.treasure_hunt.PlayerFlow.PlayerViewModel;
import huji.postpc2021.treasure_hunt.R;

public class WaitForGameFragment extends Fragment {
    private PlayerViewModel playerViewModel;

    public WaitForGameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wait_for_game, container, false);
//        playerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);
        playerViewModel = PlayerViewModel.getInstance();

        view.findViewById(R.id.buttonLeaveGameInWaitScreen).setOnClickListener(v -> leaveGame(view));

        ParticipantsListAdapter adapter = new ParticipantsListAdapter();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewParticipantsPlayerWait);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));

        playerViewModel.gameLiveData.observe(getViewLifecycleOwner(), game ->
                {
                    if (game == null) {
                        return;
                    }
                    adapter.setItems(game.getPlayers().values());
                    if (game.getStatus() == GameStatus.running) {
//                        playerViewModel.startGame(); todo
                        Navigation.findNavController(view).navigate(WaitForGameFragmentDirections.actionWaitForGameToPlayerGame());
                    }
                }
        );


        //  todo: this is temporary for debugging!
        view.findViewById(R.id.temp_move_to_game).setOnClickListener(v ->
                Navigation.findNavController(view).navigate(WaitForGameFragmentDirections.actionWaitForGameToPlayerGame()));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // on back pressed callback for this fragment
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                leaveGame(view);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    private void leaveGame(View view) {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE: {
                    playerViewModel.leaveGameFromWaitScreen(view);
                    break;
                }
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("leave game?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show();
    }
}