package a.paul.humanitasbabyfoos;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Match {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "timestamp")
    public long timestamp;

    @ForeignKey(entity = Player.class, parentColumns = "id", childColumns = "attack_blue")
    @ColumnInfo(name = "attack_blue")
    public int attackBlue;

    @ForeignKey(entity = Player.class, parentColumns = "id", childColumns = "defence_blue")
    @ColumnInfo(name = "defence_blue")
    public int defenceBlue;

    @ColumnInfo(name = "score_blue")
    public int scoreBlue;

    @ForeignKey(entity = Player.class, parentColumns = "id", childColumns = "attack_red")
    @ColumnInfo(name = "attack_red")
    public int attackRed;

    @ForeignKey(entity = Player.class, parentColumns = "id", childColumns = "attack_red")
    @ColumnInfo(name = "defence_red")
    public int defenceRed;

    @ColumnInfo(name = "score_red")
    public int scoreRed;
}
