package huji.postpc2021.treasure_hunt.PlayerFlow.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
import huji.postpc2021.treasure_hunt.Utils.MessageBoxDialog;

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

//        ActivityResultLauncher<String> requestPermissionLauncher =
//                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
//                    if (isGranted) {
//                        // Permission is granted
//                        playerViewModel.clickChooseNickName(view);
//                    } else {
//                        // Permission is not granted
//                        MessageBoxDialog dialog = new MessageBoxDialog(requireActivity());
//                        dialog.setTitle("Permission not granted")
//                                .setMessage("This game contains AR usage and for that we need permission to your camera")
//                                .setOkButton("Ok", null)
//                                .show();
//                    }
//                });

        joinGameBtn.setOnClickListener(v -> {
            // todo: if game has AR request camera permissions, else just navigate to the game
//            if (ContextCompat.checkSelfPermission(
//                    requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//                // You can use the API that requires the permission.
//                playerViewModel.clickChooseNickName(view);
//            } else {
//                // You can directly ask for the permission.
//                // The registered ActivityResultCallback gets the result of this request.
//                requestPermissionLauncher.launch(Manifest.permission.CAMERA);
//            }

            playerViewModel.clickChooseNickName(view);
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}