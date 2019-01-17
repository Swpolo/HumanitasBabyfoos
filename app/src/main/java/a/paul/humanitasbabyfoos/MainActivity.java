package a.paul.humanitasbabyfoos;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity
        extends AppCompatActivity
        implements AddPlayerDialog.Listener,
                    AddMatchDialog.Listener {

    private BabyfootDatabase db;
    private PlayerDao playerDao;
    private MatchDao matchDao;
    public static List<Player> players;
    private List<Match> matches;

    RecyclerView playersView;
    PlayerRecyclerViewAdapter playerAdapter;

    AddPlayerDialog addPlayerDialog;
    AddMatchDialog addMatchDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        matches = new ArrayList<>();
        players = new ArrayList<>();

        playersView = findViewById(R.id.players_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        playersView.setLayoutManager(mLayoutManager);
        playersView.setItemAnimator(new DefaultItemAnimator());
        playersView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        playerAdapter = new PlayerRecyclerViewAdapter(players);
        playersView.setAdapter(playerAdapter);

        addPlayerDialog = new AddPlayerDialog();
        addMatchDialog = new AddMatchDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Thread(new Runnable() {
            @Override
            public void run() {
                db = Room.databaseBuilder(getApplicationContext(),
                        BabyfootDatabase.class, "babyfoos-database").build();

                playerDao = db.playerDao();
                matchDao = db.matchDao();

                updateMatches();
                updatePlayers();
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_player:
                addPlayer();
                break;

            case R.id.add_match:
                addMatch();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateMatches() {
        if(matches == null) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                matches.clear();
                matches.addAll(matchDao.getAll());
            }
        }).start();
    }

    private void updatePlayers() {
        if(players == null) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                players.clear();
                players.addAll(playerDao.getAll());
                new Handler(getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        playersView.setAdapter(playerAdapter);
                        playerAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    private void addPlayer() {
        addPlayerDialog.show(getSupportFragmentManager(), "add_player");
    }

    private void addMatch() {
        addMatchDialog.show(getSupportFragmentManager(), "add_match");
    }

    @Override
    public void onAddPlayerPositiveClick(final String playerName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!playerDao.getPlayerByName(playerName).isEmpty()) {
                    // TODO: notify that name is already taken
                    return;
                }
                playerDao.insertAll(new Player(playerName));
                updatePlayers();
            }
        }).start();
    }

    @Override
    public void onAddMatchPositiveClick(final String match) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Add Match " + match, Toast.LENGTH_SHORT).show();
            }
        });
        updateMatches();
        updatePlayers();
    }
}
