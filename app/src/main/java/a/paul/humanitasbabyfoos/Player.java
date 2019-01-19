package a.paul.humanitasbabyfoos;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Player {

    public Player(String name) {
        this.name = name;
    }

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "match_played")
    public int matchPlayed;

    @ColumnInfo(name = "match_won")
    public int matchWon;

    @ColumnInfo(name = "as_defencer")
    public int asDefencer;

    @ColumnInfo(name = "as_attacker")
    public int asAttacker;

    @ColumnInfo(name = "initial_wins")
    public int initialWins;

    @ColumnInfo(name = "initial_matches")
    public int initialMatches;
}
