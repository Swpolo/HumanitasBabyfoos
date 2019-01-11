package a.paul.humanitasbabyfoos;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private MatchDao mMatchDao;
    private PlayerDao mPlayerDao;
    private BabyfootDatabase mDb;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        mDb = Room.inMemoryDatabaseBuilder(context, BabyfootDatabase.class).build();
        mMatchDao = mDb.matchDao();
        mPlayerDao = mDb.playerDao();
    }

    @After
    public void closeDb() throws IOException {
        mDb.close();
    }

    @Test
    public void writePlayerAndReadInList() throws Exception {
        Player paul = new Player();
        paul.name = "paul";
        mPlayerDao.insertAll(paul);

        List<Player> playersByName = mPlayerDao.getPlayerByName("paul");
        assertTrue(!playersByName.isEmpty());
        assertEquals(playersByName.get(0).name, paul.name);
    }

    @Test
    public void writeMatchAndReadInList() throws Exception {
        Player paul = new Player();
        paul.name = "paul";

        Player luca = new Player();
        luca.name = "luca";

        Player thibault = new Player();
        thibault.name = "thibault";

        Player vincent = new Player();
        vincent.name = "vincent";
        mPlayerDao.insertAll(paul, luca, thibault, vincent);

        paul = mPlayerDao.getPlayerByName("paul").get(0);
        luca = mPlayerDao.getPlayerByName("luca").get(0);
        thibault = mPlayerDao.getPlayerByName("thibault").get(0);
        vincent = mPlayerDao.getPlayerByName("vincent").get(0);

        Match match = new Match();
        match.attackBlue = thibault.id;
        match.defenceBlue = paul.id;
        match.attackRed = luca.id;
        match.defenceRed = vincent.id;
        mMatchDao.insertAll(match);

        List<Match> matches = mMatchDao.getAll();
        assertTrue(!matches.isEmpty());
        assertEquals(matches.get(0).attackBlue, thibault.id);
        assertEquals(matches.get(0).defenceBlue, paul.id);
        assertEquals(matches.get(0).attackRed, luca.id);
        assertEquals(matches.get(0).defenceRed, vincent.id);
    }
}
