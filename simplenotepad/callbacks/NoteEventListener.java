package simplenotepad.callbacks;

import simplenotepad.model.Note;


public interface NoteEventListener {

    //note被单击
    void onNoteClick(Note note);

    //note被长按
    void onNoteLongClick(Note note);
}
