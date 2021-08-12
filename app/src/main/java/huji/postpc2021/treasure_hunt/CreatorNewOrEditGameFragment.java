package huji.postpc2021.treasure_hunt;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class CreatorNewOrEditGameFragment extends Fragment {

    public CreatorNewOrEditGameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_creator_new_or_edit_game, container, false);
        PlayerViewModel playerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);
        Button creatorEditGameButton = view.findViewById(R.id.buttonLoginEditGame);
        Button creatorNewGameButton = view.findViewById(R.id.buttonLoginNewGame);

        creatorNewGameButton.setOnClickListener(v ->
        {
            Navigation.findNavController(view)
                    .navigate(CreatorNewOrEditGameFragmentDirections.actionCreatorNewOrEditGameFragmentToCreatorNewGameFragment());

        });

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
