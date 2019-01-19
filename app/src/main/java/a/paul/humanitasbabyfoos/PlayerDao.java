package a.paul.humanitasbabyfoos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface PlayerDao {
    @Query("SELECT * FROM player")
    List<Player> getAll();

    @Query("SELECT * FROM player WHERE id = (:id)")
    List<Player> getPlayerById(int id);

    @Query("SELECT * FROM player WHERE name = (:name)")
    List<Player> getPlayerByName(String name);

    @Insert
    void insertAll(Player... players);

    @Update
    void updatePlayers(Player... players);

    @Delete
    void delete(Player player);
}
