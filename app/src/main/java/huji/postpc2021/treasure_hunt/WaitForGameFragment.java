package huji.postpc2021.treasure_hunt;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WaitForGameFragment extends Fragment {
    public WaitForGameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wait_for_game, container, false);

        view.findViewById(R.id.buttonLeaveGameInWaitScreen).setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.action_waitForGameFragment_to_homeScreenFragment));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}