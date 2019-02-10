package a.paul.humanitasbabyfoos;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class PlayersFragment extends Fragment {

    private ArrayList<Player> players;
    RecyclerView playersView;
    PlayersRecyclerViewAdapter playersAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.players_fragment, container, false);
        playersView = rootView.findViewById(R.id.player_rv);
        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(rootView.getContext().getApplicationContext());
        playersView.setLayoutManager(mLayoutManager);
        playersView.setItemAnimator(new DefaultItemAnimator());
        playersView.addItemDecoration(
                new DividerItemDecoration(
                        rootView.getContext().getApplicationContext(),
                        LinearLayoutManager.VERTICAL));
        playersAdapter = new PlayersRecyclerViewAdapter(players);
        playersView.setAdapter(playersAdapter);

        return rootView;
    }

    public void setPlayersAdapter(ArrayList<Player> players) {
        this.players = players;
    }

    public void updatePlayers() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                playersAdapter.notifyDataSetChanged();
            }
        });
    }
}
