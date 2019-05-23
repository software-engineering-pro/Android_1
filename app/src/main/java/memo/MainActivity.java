package memo;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.coursetable.DBHelper;
import com.example.coursetable.R;
import com.example.coursetable.utils.BitmapUtils;

import memo.Utils.noteUtils;
import memo.adapter.notesAdapter;
import memo.callbacks.NoteEventListener;
import memo.db.NotesDB;
import memo.db.NotesDao;
import memo.model.Note;

import java.util.ArrayList;
import java.util.List;

import static memo.EditNoteActivity.NOTE_EXTRA_Key;


public class MainActivity extends AppCompatActivity implements NoteEventListener {

    private RecyclerView recyclerView;
    private ArrayList<Note> notes;
    private notesAdapter adapter;
    private NotesDao dao;
    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(new DBHelper(this).getTheme("theme"));
        setContentView(R.layout.rmain);
        Toolbar toolbar = findViewById(R.id.toolbar);   //TODO
        setSupportActionBar(toolbar);



        //加载顶部菜单栏（别的活动可能也需要）
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        //抽屉布局
        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle;
        toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView naviView = findViewById(R.id.nav_view);
//        setContentView(R.layout.rmain);
        View headerView = naviView.getHeaderView(0);

        DBHelper dbHelper = new DBHelper(this);
        de.hdodenhof.circleimageview.CircleImageView  headImage1 = headerView.findViewById(R.id.icon_image);
        byte[] data = dbHelper.getBitmapByName("pic");
        if (data != null)   {
            Bitmap bitmap = BitmapUtils.getImage(data);
            headImage1.setImageBitmap(bitmap);
        }

        //navi栏的默认选择
        naviView.setCheckedItem(R.id.nav_reminder);
        //对navi栏进行事件监听
        //你们的活动加载写在这个函数里面
        naviView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if(id == R.id.nav_syllabus){
                    //你们的Activity
                    //Toast.makeText(MainActivity.this, "Here is Calender", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    //intent.setComponent(new ComponentName(MainActivity.this, "com.example.coursetable.MainActivity"));
                    Log.d("qwq", "a");
                    intent.setComponent(new ComponentName(MainActivity.this, "com.example.coursetable.MainActivity"));
                    Log.d("qwq", "b");
                    //intent = new Intent(this, com.example.coursetable.MainActivity.class);
                    startActivity(intent);


                }else if(id == R.id.nav_calendar){
                    //你们的Activity
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(MainActivity.this, "calendarfinal.MainActivity"));
                    startActivity(intent);
                }else if(id == R.id.nav_reminder) {

                    //关闭弹窗即返回syllabus
                    mDrawerLayout.closeDrawer(GravityCompat.START);

                }else if(id == R.id.nav_setting) {
                    //          mDrawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(MainActivity.this, "com.example.coursetable.SettingActivity"));
                    startActivity(intent);
                }
                return true;
            }
        });



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
