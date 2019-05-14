package com.memo.callbacks;

import com.memo.model.Note;

public interface NoteEventListener {

    void onNoteClick(Note note);

    void onNoteLongClick(Note note);

}
