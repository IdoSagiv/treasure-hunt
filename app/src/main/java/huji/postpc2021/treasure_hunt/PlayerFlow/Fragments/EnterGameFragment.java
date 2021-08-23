package huji.postpc2021.treasure_hunt.PlayerFlow.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import huji.postpc2021.treasure_hunt.PlayerFlow.PlayerViewModel;
import huji.postpc2021.treasure_hunt.R;

public class EnterGameFragment extends Fragment {
    public EnterGameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_enter_game, container, false);
//        PlayerViewModel playerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);
        PlayerViewModel playerViewModel = PlayerViewModel.getInstance();
        Button joinGameBtn = view.findViewById(R.id.buttonJoinGame);
        EditText nickNameEditText = view.findViewById(R.id.editTextEnterNickname);

        joinGameBtn.setEnabled(!nickNameEditText.getText().toString().isEmpty());

        nickNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                playerViewModel.nickName.setValue(nickNameEditText.getText().toString());
                joinGameBtn.setEnabled(!nickNameEditText.getText().toString().isEmpty());
            }
        });

        joinGameBtn.setOnClickListener(v -> playerViewModel.clickChooseNickName(view));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}