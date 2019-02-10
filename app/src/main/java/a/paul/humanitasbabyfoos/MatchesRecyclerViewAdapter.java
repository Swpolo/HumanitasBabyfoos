package a.paul.humanitasbabyfoos;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

public class MatchesRecyclerViewAdapter
        extends RecyclerView.Adapter<MatchesRecyclerViewAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView timestamp;
        public TextView attackRed;
        public TextView defenseRed;
        public TextView attackBlue;
        public TextView defenseBlue;
        public TextView scoreRed;
        public TextView scoreBlue;
        public ConstraintLayout scoreLayout;

        public ViewHolder(View v) {
            super(v);
            timestamp = v.findViewById(R.id.match_timestamp);
            attackRed = v.findViewById(R.id.attack_red_name);
            defenseRed = v.findViewById(R.id.defense_red_name);
            attackBlue = v.findViewById(R.id.attack_blue_name);
            defenseBlue = v.findViewById(R.id.defense_blue_name);
            scoreRed = v.findViewById(R.id.match_view_red_score);
            scoreBlue = v.findViewById(R.id.match_view_blue_score);
            scoreLayout = v.findViewById(R.id.match_score_block);
        }
    }

    private Context context;
    private List<Player> players;
    private List<Match> matches;

    public MatchesRecyclerViewAdapter(Context context, List<Player> players, List<Match> matches) {
        this.context = context;
        this.players = players;
        this.matches = matches;
    }

    @NonNull
    @Override
    public MatchesRecyclerViewAdapter.ViewHolder
    onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.match_view, viewGroup, false
        );

        return new MatchesRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchesRecyclerViewAdapter.ViewHolder viewHolder, int i) {
        Match match = matches.get(i);

        viewHolder.timestamp.setText(new SimpleDateFormat("dd/MM/yy kk:mm").format(match.timestamp));
        int p = 0;
        for(Player player: players) {
            if (player.id == match.attackRed) {
                p++;
                viewHolder.attackRed.setText(player.name);
            }
            if (player.id == match.defenceRed) {
                p++;
                viewHolder.defenseRed.setText(player.name);
            }
            if (player.id == match.attackBlue) {
                p++;
                viewHolder.attackBlue.setText(player.name);
            }
            if (player.id == match.defenceBlue) {
                p++;
                viewHolder.defenseBlue.setText(player.name);
            }
            if (p == 4) break;
        }
        viewHolder.scoreRed.setText(Integer.toString(match.scoreRed));
        viewHolder.scoreBlue.setText(Integer.toString(match.scoreBlue));
        if (match.scoreRed == 10) {
            viewHolder.scoreLayout.setBackgroundColor(
                    context.getResources().getColor(R.color.red_block_background));
        } else {
            viewHolder.scoreLayout.setBackgroundColor(
                    context.getResources().getColor(R.color.blue_block_background));
        }
    }

    @Override
    public int getItemCount() {
        if (matches == null) return 0;
        return matches.size();
    }
}
