package huji.postpc2021.treasure_hunt.PlayerFlow.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

import huji.postpc2021.treasure_hunt.PlayerFlow.PlayerViewModel;
import huji.postpc2021.treasure_hunt.PlayerFlow.ScoreListAdapter;
import huji.postpc2021.treasure_hunt.R;
import huji.postpc2021.treasure_hunt.Utils.DataObjects.GameStatus;
import huji.postpc2021.treasure_hunt.Utils.DataObjects.Player;

public class PlayerGameOverFragment extends Fragment {
    private PlayerViewModel playerViewModel;
    private final HashMap<String, Player> finishedPlayers = new HashMap<>();

    public PlayerGameOverFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_over, container, false);
        playerViewModel = PlayerViewModel.getInstance();
        Button exitGameButton = view.findViewById(R.id.buttonExitGame);
        exitGameButton.setOnClickListener(v -> playerViewModel.leaveGameFromGameOverScreen(view));
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //  RecyclerView initialization
        ScoreListAdapter adapter = new ScoreListAdapter();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewScoreListInGameOverScreen);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));

        playerViewModel.gameLiveData.observe(getViewLifecycleOwner(), game -> {
            for (Player player : game.getPlayers().values()) {
                if (playerViewModel.isFinishedGame(player.getId()) || game.getStatus() == GameStatus.finished) {
                    finishedPlayers.put(player.getId(), player);
                }
            }
            adapter.setItems(finishedPlayers.values());
        });

        // on back pressed callback for this fragment
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                playerViewModel.leaveGameFromGameOverScreen(view);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }
}
