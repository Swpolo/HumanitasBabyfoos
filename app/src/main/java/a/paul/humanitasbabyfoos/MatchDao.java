package a.paul.humanitasbabyfoos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MatchDao {
    @Query("SELECT * FROM `match`")
    List<Match> getAll();

    @Query("SELECT * " +
            "FROM `match` " +
            "WHERE attack_blue = (:playerId) " +
            "OR defence_blue = (:playerId) " +
            "OR attack_red = (:playerId)" +
            "OR defence_red = (:playerId)")
    List<Match> getByPlayer(int playerId);

    @Insert
    void insertAll(Match... matches);

    @Delete
    void delete(Match match);
}
