package simplenotepad.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import simplenotepad.model.Note;

/**
 * Created by ixi.Dv on 20/06/2018.
 */
@Database(entities = Note.class, version = 1, exportSchema = false)
public abstract class NotesDB extends RoomDatabase {
    public abstract NotesDao notesDao();

    public static final String DATABSE_NAME = "notesDb";
    private static NotesDB instance;

    public static NotesDB getInstance(Context context) {
        if (instance == null)
            instance = Room.databaseBuilder(context, NotesDB.class, DATABSE_NAME)
                    .allowMainThreadQueries()
                    .build();
        return instance;
    }
}
