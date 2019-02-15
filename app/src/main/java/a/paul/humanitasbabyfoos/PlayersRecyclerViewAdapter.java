package a.paul.humanitasbabyfoos;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class PlayersRecyclerViewAdapter
        extends RecyclerView.Adapter<PlayersRecyclerViewAdapter.ViewHolder> {

    private int grayBackgroundColor;
    private int noBackgroundColor;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout root;
        public TextView rank;
        public TextView elo;
        public TextView name;
        public TextView wins;
        public TextView matches;
        public TextView ratio;

        public ViewHolder(View v) {
            super(v);
            root = v.findViewById(R.id.player_root);
            rank = v.findViewById(R.id.player_rank);
            elo = v.findViewById(R.id.player_elo);
            name = v.findViewById(R.id.player_name);
            wins = v.findViewById(R.id.player_victory);
            matches = v.findViewById(R.id.player_matches);
            ratio = v.findViewById(R.id.player_ratio);
        }
    }

    private List<Player> players;

    public PlayersRecyclerViewAdapter(List<Player> players) {
        this.players = players;
    }

    public void setGraybackgroundColor(int color) {
        grayBackgroundColor = color;
    }
    public void setNoBackgroundColor(int color) {
        noBackgroundColor = color;
    }

    @NonNull
    @Override
    public PlayersRecyclerViewAdapter.ViewHolder
    onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.player_view, viewGroup, false
        );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayersRecyclerViewAdapter.ViewHolder viewHolder, int i) {
        Player player = players.get(i);
        if (i%2 == 1) {
            viewHolder.root.setBackgroundColor(grayBackgroundColor);
        } else {
            viewHolder.root.setBackgroundColor(noBackgroundColor);
        }

        int ratio = player.matchPlayed != 0
                ? player.matchWon * 100 / player.matchPlayed
                : 0;
        viewHolder.rank.setText(String.format(Locale.CANADA, "%2d", i + 1));
        viewHolder.elo.setText(
                String.format(Locale.CANADA, "%4d", player.score));
        viewHolder.name.setText(player.name);
        viewHolder.wins.setText(String.format(Locale.CANADA, "%2d", player.matchWon));
        viewHolder.matches.setText(String.format(Locale.CANADA, "%2d", player.matchPlayed));
        viewHolder.ratio.setText(
                String.format(Locale.CANADA, "%3d%%", ratio));
    }

    @Override
    public int getItemCount() {
        if (players == null) return 0;
        return players.size();
    }

}
