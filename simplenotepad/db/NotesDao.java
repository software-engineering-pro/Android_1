package simplenotepad.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import simplenotepad.model.Note;

import java.util.List;

/**
 * Notes Data Object access help to access the notes
 * Created by ixi.Dv on 20/06/2018.
 */
@Dao
public interface NotesDao {
    //将note存入数据库
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(Note note);

       @Delete
    void deleteNote(Note... note);


    @Update
    void updateNote(Note note);

    //列出所有notes
    @Query("SELECT * FROM notes")
    List<Note> getNotes();


    @Query("SELECT * FROM notes WHERE id = :noteId")
    Note getNoteById(int noteId);


    @Query("DELETE FROM notes WHERE id = :noteId")
    void deleteNoteById(int noteId);

}
