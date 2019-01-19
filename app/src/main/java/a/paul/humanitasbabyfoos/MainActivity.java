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
    public static List<Player> playerList;
    private List<Match> matchList;

    RecyclerView playersView;
    PlayerRecyclerViewAdapter playerAdapter;

    AddPlayerDialog addPlayerDialog;
    AddMatchDialog addMatchDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        matchList = new ArrayList<>();
        playerList = new ArrayList<>();

        playersView = findViewById(R.id.players_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        playersView.setLayoutManager(mLayoutManager);
        playersView.setItemAnimator(new DefaultItemAnimator());
        playersView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        playerAdapter = new PlayerRecyclerViewAdapter(playerList);
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
        updateMatches(null);
    }

    private void updateMatches(final Match match) {

        if(matchList == null) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(match != null) matchDao.insertAll(match);
                matchList.clear();
                matchList.addAll(matchDao.getAll());
            }
        }).start();

    }

    private void updatePlayers() {
        updatePlayers(null);
    }

    private void updatePlayers(final Player... players) {
        if(playerList == null) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(players != null) playerDao.updatePlayers(players);
                playerList.clear();
                playerList.addAll(playerDao.getAll());
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                Match newMatch = new Match();
                String[] datas = match.split(";");
                if(datas.length != 6) {
                    showToast("Couldn't add match: missing parameters");
                    return;
                }
                try {
                    newMatch.scoreBlue = Integer.parseInt(datas[0]);
                    newMatch.scoreRed = Integer.parseInt(datas[3]);
                } catch (NumberFormatException e) {
                    showToast("Couldn't add match: invalid score");
                    return;
                }
                if(
                    // If no winner
                        (newMatch.scoreBlue != 10 && newMatch.scoreRed != 10) ||

                                // If two winners ._.
                                (newMatch.scoreBlue == 10 && newMatch.scoreRed == 10)
                ){
                    showToast("Couldn't add match: invalid score");
                    return;
                }
                Player attackBlue = null;
                Player defenceBlue = null;
                Player attackRed = null;
                Player defenceRed = null;

                try {
                    attackBlue = playerDao.getPlayerByName(datas[1]).get(0);
                    defenceBlue = playerDao.getPlayerByName(datas[2]).get(0);
                    attackRed = playerDao.getPlayerByName(datas[4]).get(0);
                    defenceRed = playerDao.getPlayerByName(datas[5]).get(0);
                } catch(ArrayIndexOutOfBoundsException e) {
                    showToast("Couldn't add match: invalids players");
                }

                try {
                    int[] ids = {
                            attackBlue.id,
                            defenceBlue.id,
                            attackRed.id,
                            defenceRed.id
                    };

                    for (int i = 0; i < 4; i++) {
                        for (int j = i + 1; j < 4; j++) {
                            if(ids[i] == ids[j]) {
                                showToast("Couldn't add match: two or more players are the same");
                                return;
                            }
                        }
                    }


                    if (newMatch.scoreBlue == 10) {
                        attackBlue.matchWon++;
                        defenceBlue.matchWon++;
                    } else {
                        attackRed.matchWon++;
                        defenceRed.matchWon++;
                    }
                    attackBlue.matchPlayed++;
                    defenceBlue.matchPlayed++;
                    attackRed.matchPlayed++;
                    defenceRed.matchPlayed++;
                } catch (NullPointerException e) {
                    showToast("Couldn't add match: invalids players");
                }

                newMatch.timestamp = System.currentTimeMillis();

                updateMatches(newMatch);
                updatePlayers(attackBlue, defenceBlue, attackRed, defenceRed);
            }
        }).start();
    }

    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
