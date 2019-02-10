package a.paul.humanitasbabyfoos;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity
        extends AppCompatActivity
        implements AddPlayerDialog.Listener,
                    UpdatePlayerDialog.Listener,
                    AddMatchDialog.Listener {

    private static final int SORT_ELO = 0;
    private static final int SORT_RATIO = 1;
    private int playerSort = SORT_ELO;

    private BabyfootDatabase db;
    private PlayerDao playerDao;
    private MatchDao matchDao;
    public static ArrayList<Player> playersList;
    private ArrayList<Match> matchList;

    MatchesFragment matchesFragment;
    PlayersFragment playersFragment;

    RecyclerView playersView;
    PlayersRecyclerViewAdapter playerAdapter;

    AddPlayerDialog addPlayerDialog;
    UpdatePlayerDialog updatePlayerDialog;
    AddMatchDialog addMatchDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        matchList = new ArrayList<>();
        playersList = new ArrayList<>();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        matchesFragment = new MatchesFragment();
        matchesFragment.setMatchesAdapter(getApplicationContext(), playersList, matchList);
        fragmentTransaction.add(R.id.matches_fragment, matchesFragment);

        playersFragment = new PlayersFragment();
        playersFragment.setPlayersAdapter(playersList);
        fragmentTransaction.add(R.id.players_fragment, playersFragment);
        fragmentTransaction.commit();

        addPlayerDialog = new AddPlayerDialog();
        updatePlayerDialog = new UpdatePlayerDialog();
        addMatchDialog = new AddMatchDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();


        new Thread(new Runnable() {
            @Override
            public void run() {
                db = Room.databaseBuilder(getApplicationContext(),
                        BabyfootDatabase.class, "babyfoos-database")
                        .fallbackToDestructiveMigration()
                        .build();

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

            case R.id.update_player:
                updatePlayer();
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
                Collections.sort(matchList, new Comparator<Match>() {
                    @Override
                    public int compare(Match o1, Match o2) {
                        long timestampO1 = o1.timestamp;
                        long timestampO2 = o2.timestamp;
                        return Long.compare(timestampO2, timestampO1);
                    }
                });
                matchesFragment.updateMatch();
            }
        }).start();

    }

    private void updatePlayers() {
        //noinspection ConfusingArgumentToVarargsMethod
        updatePlayers(null);
    }

    private void updatePlayers(final Player... players) {
        if(playersList == null) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(players != null) playerDao.updatePlayers(players);
                playersList.clear();
                playersList.addAll(playerDao.getAll());

                if (playerSort == SORT_ELO) {
                    Collections.sort(playersList, new Comparator<Player>() {
                        @Override
                        public int compare(Player o1, Player o2) {
                            return Integer.compare(o2.score, o1.score);
                        }
                    });
                } else if (playerSort == SORT_RATIO) {
                    Collections.sort(playersList, new Comparator<Player>() {
                        @Override
                        public int compare(Player o1, Player o2) {
                            float ratioO1 = o1.matchWon / (float) o1.matchPlayed;
                            float ratioO2 = o2.matchWon / (float) o2.matchPlayed;
                            return Float.compare(ratioO2, ratioO1);
                        }
                    });
                }
                new Handler(getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        playersFragment.updatePlayers();
                    }
                });
            }
        }).start();
    }

    private void addPlayer() {
        addPlayerDialog.show(getSupportFragmentManager(), "add_player");
    }

    private void updatePlayer() {
        updatePlayerDialog.show(getSupportFragmentManager(), "update_player");
    }

    private void addMatch() {
        addMatchDialog.show(getSupportFragmentManager(), "add_match");
    }

    @Override
    public void onAddPlayerPositiveClick(final String playerName, final int win, final int match) {
        if(playerName.isEmpty()) {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!playerDao.getPlayerByName(playerName).isEmpty()) {
                    showToast("That name is already taken");
                    return;
                }
                Player player = new Player(playerName);
                player.matchWon = player.initialWins = win;
                player.matchPlayed = player.initialMatches = match;
                playerDao.insertAll(player);
                updatePlayers();
            }
        }).start();
    }

    @Override
    public void onUpdatePlayerPositiveClick(final String oldName, final String newName) {
        if (oldName.isEmpty()) return;

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Player> players = playerDao.getPlayerByName(oldName);
                if(players.isEmpty()) {
                    showToast("Player not found, cannot update");
                    return;
                }
                Player player = players.get(0);

                players = playerDao.getPlayerByName(newName);
                if(!players.isEmpty()) {
                    showToast("This name is already taken, cannot update");
                    return;
                }

                player.name = newName;
                playerDao.updatePlayers(player);
                //noinspection ConfusingArgumentToVarargsMethod
                updatePlayers();
                showToast("Player updated");
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
                // Save Score
                try {
                    newMatch.scoreBlue = Integer.parseInt(datas[0]);
                    newMatch.scoreRed = Integer.parseInt(datas[3]);
                } catch (NumberFormatException e) {
                    showToast("Couldn't add match: invalid score");
                    return;
                }

                // Save Players
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
                    return;
                }
                newMatch.attackBlue = attackBlue.id;
                newMatch.defenceBlue = defenceBlue.id;
                newMatch.attackRed = attackRed.id;
                newMatch.defenceRed = defenceRed.id;


                if(
                    // If no winner
                        (newMatch.scoreBlue != 10 && newMatch.scoreRed != 10) ||

                                // If two winners ._.
                                (newMatch.scoreBlue == 10 && newMatch.scoreRed == 10)
                ){
                    showToast("Couldn't add match: invalid score");
                    return;
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
                    attackBlue.asAttacker++;

                    defenceBlue.matchPlayed++;
                    defenceBlue.asDefender++;

                    attackRed.matchPlayed++;
                    attackRed.asAttacker++;

                    defenceRed.matchPlayed++;
                    defenceRed.asDefender++;

                } catch (NullPointerException e) {
                    showToast("Couldn't add match: invalids players");
                    return;
                }

                newMatch.timestamp = System.currentTimeMillis();

                PlayerRating.computeRating(newMatch.scoreRed, newMatch.scoreBlue,
                        attackRed, defenceRed, attackBlue, defenceBlue);

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
