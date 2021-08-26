package huji.postpc2021.treasure_hunt.CreatorFlow.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.osmdroid.views.MapView;

import huji.postpc2021.treasure_hunt.CreatorFlow.CreatorViewModel;
import huji.postpc2021.treasure_hunt.PlayerFlow.ParticipantsListAdapter;
import huji.postpc2021.treasure_hunt.R;
import huji.postpc2021.treasure_hunt.Utils.MapHandler;

public class CreatorDoneEditGameFragment extends Fragment {
    private CreatorViewModel creatorViewModel;

    public CreatorDoneEditGameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creator_done_editing, container, false);
        creatorViewModel = CreatorViewModel.getInstance();
        MapView mapView = view.findViewById(R.id.mapViewCreatorDoneEditing);
        MapHandler mapHandler = new MapHandler(mapView, MapHandler.MarkersType.HintOnly);

        TextView gameCodeTextView = view.findViewById(R.id.textViewGameCodeDoneEditingScreen);
        TextView gameNameTextView = view.findViewById(R.id.textViewGameNameDoneEditing);
        RecyclerView participantsListRecyclerView = view.findViewById(R.id.recyclerViewParticipantsCreatorWait);
        ParticipantsListAdapter participantsListAdapter = new ParticipantsListAdapter();
        participantsListRecyclerView.setAdapter(participantsListAdapter);
        participantsListRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));

        creatorViewModel.currentGame.observe(getViewLifecycleOwner(), game -> {
            if (game == null) return;

            gameCodeTextView.setText(game.getCode());
            gameNameTextView.setText(game.getName());
            participantsListAdapter.setItems(game.getPlayers().values());
        });

        creatorViewModel.cluesLiveData.observe(getViewLifecycleOwner(), clues ->
                mapHandler.showHints(clues.values()));


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
