package huji.postpc2021.treasure_hunt.CreatorFlow.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import huji.postpc2021.treasure_hunt.CreatorFlow.CreatorViewModel;
import huji.postpc2021.treasure_hunt.R;

public class CreatorDoneEditGameFragment extends Fragment {
    private CreatorViewModel creatorViewModel;

    public CreatorDoneEditGameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creator_done_editing, container, false);
        creatorViewModel = CreatorViewModel.getInstance();


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
