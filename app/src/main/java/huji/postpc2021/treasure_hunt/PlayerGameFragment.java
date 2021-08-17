package huji.postpc2021.treasure_hunt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

public class PlayerGameFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout scoreListDrawerLayout;
    private PlayerViewModel playerViewModel;

    public PlayerGameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_player_game, container, false);
        playerViewModel = PlayerViewModel.getInstance();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initScoreListDrawer(view);

        ImageView openScoreListButton = view.findViewById(R.id.buttonSeeScore);
        openScoreListButton.setOnClickListener(v ->
                scoreListDrawerLayout.openDrawer(GravityCompat.START));
    }


    private void initScoreListDrawer(View view) {
        scoreListDrawerLayout = view.findViewById(R.id.drawerLayoutScoreList);
        scoreListDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        scoreListDrawerLayout.findViewById(R.id.recyclerViewScoreListInDrawer);

        ScoreListAdapter adapter = new ScoreListAdapter();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewScoreListInDrawer);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));

        playerViewModel.gameLiveData.observe(requireActivity(), game ->
                adapter.setItems(game.getPlayers().values())
        );
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}
