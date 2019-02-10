package a.paul.humanitasbabyfoos;

import android.content.Context;
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

public class MatchesFragment extends Fragment {

    private Context context;
    private ArrayList<Player> players;
    private ArrayList<Match> matches;
    RecyclerView matchesView;
    MatchesRecyclerViewAdapter matchesAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.matches_fragment, container, false);
        matchesView = rootView.findViewById(R.id.matches_rv);
        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(rootView.getContext().getApplicationContext());
        matchesView.setLayoutManager(mLayoutManager);
        matchesView.setItemAnimator(new DefaultItemAnimator());
        matchesView.addItemDecoration(
                new DividerItemDecoration(
                        rootView.getContext().getApplicationContext(),
                        LinearLayoutManager.VERTICAL));
        matchesAdapter = new MatchesRecyclerViewAdapter(context, players, matches);
        matchesView.setAdapter(matchesAdapter);

        return rootView;
    }

    public void setMatchesAdapter(Context context, ArrayList<Player> players, ArrayList<Match> matches) {
        this.context = context;
        this.players = players;
        this.matches = matches;
    }

    public void updateMatch() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                matchesAdapter.notifyDataSetChanged();
            }
        });
    }

}
