package simplenotepad;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursetable.DBHelper;
import com.example.coursetable.R;
import com.example.coursetable.utils.BitmapUtils;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import calendarfinal.DBOpenHelper;
import calendarfinal.User;
import simplenotepad.adapters.NotesAdapter;
import simplenotepad.callbacks.MainActionModeCallback;
import simplenotepad.callbacks.NoteEventListener;
import simplenotepad.db.NotesDB;
import simplenotepad.db.NotesDao;
import simplenotepad.model.Note;
import simplenotepad.utils.NoteUtils;

import java.util.ArrayList;
import java.util.List;

import simplenotepad.adapters.NotesAdapter;
import simplenotepad.callbacks.MainActionModeCallback;
import simplenotepad.db.NotesDB;

import static com.example.coursetable.global.GloablApplication.sCurrentTheme;
import static simplenotepad.EditNoteActivity.NOTE_EXTRA_Key;

public class MainActivity extends AppCompatActivity implements NoteEventListener, Drawer.OnDrawerItemClickListener {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private calendarfinal.DBOpenHelper dbOpenHelper;
    private ArrayList<Note> notes;
    private NotesAdapter adapter;
    private NotesDao dao;
    private MainActionModeCallback actionModeCallback;
    private int chackedCount = 0;
    private FloatingActionButton fab;
    private SharedPreferences settings;
    public static final String THEME_Key = "app_theme";
    public static final String APP_PREFERENCES="notepad_settings";
    private int theme;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        theme = settings.getInt(THEME_Key, R.style.AppTheme);
        setTheme(theme);

        super.onCreate(savedInstanceState);
        setTheme(sCurrentTheme);
        setContentView(R.layout.rmain);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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

        dbOpenHelper = new DBOpenHelper(this);
        TextView helloText = (TextView) headerView.findViewById(R.id.username);
        String s;
        ArrayList<User> data666 = dbOpenHelper.getAllData();
        s = data666.get(0).getName();
        helloText.setText(s);
        helloText.setTextColor(Color.WHITE);

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
                    finish();


                }else if(id == R.id.nav_calendar){
                    //你们的Activity
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(MainActivity.this, "calendarfinal.MainActivity"));
                    startActivity(intent);
                    finish();
                }else if(id == R.id.nav_reminder) {

                    //关闭弹窗即返回syllabus
                    mDrawerLayout.closeDrawer(GravityCompat.START);

                }else if(id == R.id.nav_setting) {
                    //          mDrawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(MainActivity.this, "com.example.coursetable.SettingActivity"));
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });


        /*setupNavigation(savedInstanceState, toolbar);*/
        // init recyclerView
        recyclerView = findViewById(R.id.notes_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // init fab Button
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 13/05/2018  add new note
                onAddNewNote();
            }
        });

        dao = NotesDB.getInstance(this).notesDao();
    }


    private void loadNotes() {
        this.notes = new ArrayList<>();
        List<Note> list = dao.getNotes();// get All notes from DataBase
        this.notes.addAll(list);
        this.adapter = new NotesAdapter(this, this.notes);
        this.adapter.setListener(this);
        this.recyclerView.setAdapter(adapter);
        showEmptyView();


        swipeToDeleteHelper.attachToRecyclerView(recyclerView);
    }


    private void showEmptyView() {
        if (notes.size() == 0) {
            this.recyclerView.setVisibility(View.GONE);
            findViewById(R.id.empty_notes_view).setVisibility(View.VISIBLE);

        } else {
            this.recyclerView.setVisibility(View.VISIBLE);
            findViewById(R.id.empty_notes_view).setVisibility(View.GONE);
        }
    }

    //创建新note
    private void onAddNewNote() {
        startActivity(new Intent(this, EditNoteActivity.class));

    }

    @SuppressLint("ResourceType")

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.layout.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_settings) {
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
        // note单击
        Intent edit = new Intent(this, EditNoteActivity.class);
        edit.putExtra(NOTE_EXTRA_Key, note.getId());
        startActivity(edit);

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onNoteLongClick(Note note) {
        // note长按
        note.setChecked(true);
        chackedCount = 1;
        adapter.setMultiCheckMode(true);


        adapter.setListener(new NoteEventListener() {
            @Override
            public void onNoteClick(Note note) {
                note.setChecked(!note.isChecked());
                if (note.isChecked())
                    chackedCount++;
                else chackedCount--;

                if (chackedCount > 1) {
                    actionModeCallback.changeShareItemVisible(false);
                } else actionModeCallback.changeShareItemVisible(true);

                if (chackedCount == 0) {
                    //  退出多选
                    actionModeCallback.getAction().finish();
                }

                actionModeCallback.setCount(chackedCount + "/" + notes.size());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNoteLongClick(Note note) {

            }
        });

        actionModeCallback = new MainActionModeCallback() {
            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_delete_notes)
                    onDeleteMultiNotes();
                else if (menuItem.getItemId() == R.id.action_share_note)
                    onShareNote();

                actionMode.finish();
                return false;
            }

        };


        startActionMode(actionModeCallback);
        // 隐藏fab按钮
        fab.setVisibility(View.GONE);
        actionModeCallback.setCount(chackedCount + "/" + notes.size());
    }

    private void onShareNote() {
        // 分享单条note

        Note note = adapter.getCheckedNotes().get(0);

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        String notetext = note.getNoteText() + "\n\n Create on : " +
                NoteUtils.dateFromLong(note.getNoteDate()) + "\n  By :" +
                getString(R.string.app_name);
        share.putExtra(Intent.EXTRA_TEXT, notetext);
        startActivity(share);


    }

    private void onDeleteMultiNotes() {
        // 同时删除多条笔记

        List<Note> chackedNotes = adapter.getCheckedNotes();
        if (chackedNotes.size() != 0) {
            for (Note note : chackedNotes) {
                dao.deleteNote(note);
            }
            // 更新一下
            loadNotes();
            Toast.makeText(this, chackedNotes.size() + " Note(s) Delete successfully !", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this, "No Note(s) selected", Toast.LENGTH_SHORT).show();

        //adapter.setMultiCheckMode(false);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onActionModeFinished(ActionMode mode) {
        super.onActionModeFinished(mode);

        adapter.setMultiCheckMode(false);
        adapter.setListener(this);
        fab.setVisibility(View.VISIBLE);
    }

    // 滑动删除
    private ItemTouchHelper swipeToDeleteHelper = new ItemTouchHelper(
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                    if (notes != null) {
                        // get swiped note
                        Note swipedNote = notes.get(viewHolder.getAdapterPosition());
                        if (swipedNote != null) {
                            swipeToDelete(swipedNote, viewHolder);

                        }

                    }
                }
            });

    private void swipeToDelete(final Note swipedNote, final RecyclerView.ViewHolder viewHolder) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage("Delete Note?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // TODO: 28/09/2018 delete note
                        dao.deleteNote(swipedNote);
                        notes.remove(swipedNote);
                        adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                        showEmptyView();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        recyclerView.getAdapter().notifyItemChanged(viewHolder.getAdapterPosition());


                    }
                })
                .setCancelable(false)
                .create().show();

    }

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

        Toast.makeText(this, "" + position, Toast.LENGTH_SHORT).show();
        return false;
    }
}



