package com.memo;

import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.memo.db.NotesDB;
import com.memo.db.NotesDao;
import com.memo.model.Note;

public class EditNoteActivity extends AppCompatActivity {
    private EditText inputNote;
    private NotesDao dao;
    private Note temp;
    public static final String NOTE_EXTRA_Key = "note_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        inputNote = findViewById(R.id.input_note);
        dao = NotesDB.getInstances(this).notesDao();
        if(getIntent().getExtras() != null){
            int id = getIntent().getExtras().getInt(NOTE_EXTRA_Key,0);
            temp = dao.getNoteById(id);
            inputNote.setText(temp.getNoteText());
        }
        else{
            //temp = new Note();
            inputNote.setFocusable(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_note_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.save_note) {
            onSaveNote();
        }
        return super.onOptionsItemSelected(item);
    }

    private void onSaveNote() {
        // Save Note
        String text = inputNote.getText().toString();
        if(!text.isEmpty()){
            long date = new Date().getTime();
            // if note existed edit. else create  new
            /*temp.setNoteDate(date);
            temp.setNoteText(text);

            if(temp.getId() == -1) {
                dao.insertNote(temp);  // insert and save note to db
            }
            else {
                dao.updateNote(temp);
            }*/

            if (temp == null) {
                temp = new Note(text, date);
                dao.insertNote(temp); // create new note and inserted to database
            } else {
                temp.setNoteText(text);
                temp.setNoteDate(date);
                dao.updateNote(temp); // change text and date and update note on database
            }

            finish(); //return to the mainActivity
        }
    }
}
