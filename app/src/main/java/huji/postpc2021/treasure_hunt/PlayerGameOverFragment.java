package huji.postpc2021.treasure_hunt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class PlayerGameOverFragment extends Fragment {
    private PlayerViewModel playerViewModel;

    public PlayerGameOverFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_over, container, false);
        playerViewModel = PlayerViewModel.getInstance();
        Button exitGameButton = view.findViewById(R.id.buttonExitGame);
        exitGameButton.setOnClickListener(v -> Navigation.findNavController(view)
                .navigate(PlayerGameOverFragmentDirections.actionPlayerGameOverFragmentToHomeScreenFragment()));
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // on back pressed callback for this fragment
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(view)
                        .navigate(PlayerGameOverFragmentDirections.actionPlayerGameOverFragmentToHomeScreenFragment());
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }
}
