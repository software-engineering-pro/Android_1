package com.memo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.memo.Utils.noteUtils;
import com.memo.callbacks.NoteEventListener;
import com.memo.db.NotesDB;
import com.memo.db.NotesDao;
import com.memo.model.Note;
import com.memo.adapter.notesAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.memo.EditNoteActivity.NOTE_EXTRA_Key;


public class MainActivity extends AppCompatActivity implements NoteEventListener {

    private RecyclerView recyclerView;
    private ArrayList<Note> notes;
    private notesAdapter adapter;
    private NotesDao dao;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);   //TODO
        setSupportActionBar(toolbar);

        // init recycleView
        recyclerView = findViewById(R.id.notes_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add new Notes
                onAddNewNote();
            }
        });

        dao = NotesDB.getInstances(this).notesDao();
    }

    private void loadNotes() {
        this.notes = new ArrayList<>();
       /*
       display test:
       for (int i = 0; i <3 ; i++) {
            notes.add(new Note("test " + i,new Date().getTime()));
        }*/
        List<Note> list = dao.getNotes(); //get all notes from db
        this.notes.addAll(list);
        this.adapter = new notesAdapter(this,notes);
        // set listener to adapter
        this.adapter.setListener(this);
        this.recyclerView.setAdapter(adapter);
    }

    /*private void showEmptyView() {
        if (notes.size() == 0) {
            this.recyclerView.setVisibility(View.GONE);
            findViewById(R.id.empty_notes_view).setVisibility(View.VISIBLE);

        } else {
            this.recyclerView.setVisibility(View.VISIBLE);
            findViewById(R.id.empty_notes_view).setVisibility(View.GONE);
        }
    }*/

    private void onAddNewNote() {
        /*
        add button test:
        if(notes != null){
            notes.add(new Note("This is a new note", new Date().getTime()));
        }
        if(adapter != null){
            adapter.notifyDataSetChanged();
        }*/

        // Start Edit Note
        startActivity(new Intent(this,EditNoteActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save_note) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }

    @Override
    public void onNoteClick(Note note) {
        // note clicked : edit
        Intent edit = new Intent(this,EditNoteActivity.class);
        edit.putExtra(NOTE_EXTRA_Key,note.getId());
        startActivity(edit);
    }

    @Override
    public void onNoteLongClick(final Note note) {
        // note long clicked : delete / share
        new AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dao.deleteNote(note); // delete from db
                        loadNotes();  // refresh
                    }
                })
                .setNegativeButton("Share", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // share note text
                        Intent share = new Intent(Intent.ACTION_SEND);

                        String text = note.getNoteText() + "\n create on: " +
                                noteUtils.dateFromLong(note.getNoteDate());
                        share.setType("text/plain");
                        share.putExtra(Intent.EXTRA_TEXT, text);
                        startActivity(share);
                    }
                })
                .create()
                .show();
    }
}
