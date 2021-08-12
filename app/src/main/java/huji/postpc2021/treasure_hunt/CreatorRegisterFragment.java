package huji.postpc2021.treasure_hunt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

public class CreatorRegisterFragment extends Fragment {

    public CreatorRegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_creator_register, container, false);
        PlayerViewModel playerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);
        Button creatorSignInButton = view.findViewById(R.id.buttonSignIn);

        creatorSignInButton.setOnClickListener(v ->
        {
            Navigation.findNavController(view)
                    .navigate(CreatorRegisterFragmentDirections.actionCreatorRegisterFragmentToCreatorNewGameFragment());
        });
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
