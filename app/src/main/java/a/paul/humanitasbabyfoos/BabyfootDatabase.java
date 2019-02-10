package a.paul.humanitasbabyfoos;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.support.annotation.NonNull;

@Database(entities = {Match.class, Player.class}, version = 1)
public abstract class BabyfootDatabase extends RoomDatabase {
    public abstract MatchDao matchDao();
    public abstract PlayerDao playerDao();
}
