package a.paul.humanitasbabyfoos;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class PlayersRecyclerViewAdapter
        extends RecyclerView.Adapter<PlayersRecyclerViewAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView rank;
        public TextView playerName;
        public TextView playerScore;

        public ViewHolder(View v) {
            super(v);
            rank = v.findViewById(R.id.player_rank);
            playerName = v.findViewById(R.id.player_name);
            playerScore = v.findViewById(R.id.player_score);
        }
    }

    private List<Player> dataset;

    public PlayersRecyclerViewAdapter(List<Player> dataset) {
        this.dataset = dataset;
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
        Player player = dataset.get(i);
        viewHolder.rank.setText("");
        viewHolder.playerName.setText(player.name);
        viewHolder.playerScore.setText(
                String.format(Locale.CANADA, "%3d / %3d : %3d%%",
                        player.matchWon, player.matchPlayed,
                        ((int)(player.matchWon / (float)player.matchPlayed * 100)))
        );
    }

    @Override
    public int getItemCount() {
        if (dataset == null) return 0;
        return dataset.size();
    }

}
